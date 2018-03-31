package edu.ncstate.csc510.okeclipse.builder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;

import com.sohelper.datatypes.GoogleResult;
import com.sohelper.datatypes.StackoverflowAnswer;
import com.sohelper.datatypes.StackoverflowPost;
import com.sohelper.fetchers.GoogleFetcher;
import com.sohelper.fetchers.StackoverflowFetcher;
import com.sohelper.ui.QuestionPage;

import edu.ncstate.csc510.okeclipse.util.Util;

/**
 * 
 * @author Shrikanth N C
 * To handle and generate stackoverflow report
 *
 */
public class SOAnswerBuilder {

	private static final String FILENAME = "soresponse.html";

	private StringBuffer content = new StringBuffer();

	/**
	 * @author M.S.Karthik
	 * @param questions
	 * @throws IOException
	 * @throws PartInitException
	 */
	// function to get the page from stackoverflow and display it in a readable form
	public void build(List<String> questions) throws IOException, PartInitException {

		IProgressMonitor monitor = new NullProgressMonitor();

		content.append("<html>");
		content.append("<style>");
		content.append("* {\r\n");
		content.append("  margin: 0;\r\n");
		content.append("  padding: 0;\r\n");
		content.append("  font-family: Lato;\r\n");
		content.append("}\r\n");
		content.append("body {\r\n");
		content.append("  padding: 0px;\r\n");
		content.append("  background: #e5e2e6;\r\n");
		content.append("}\r\n");
		content.append(".flatTable {\r\n");
		content.append("  width: 100%;\r\n");
		content.append("  min-width: 500px;\r\n");
		content.append("  border-collapse: collapse;\r\n");
		content.append("  font-weight: bold;\r\n");
		content.append("  color: #6b6b6b;\r\n");
		content.append("}\r\n");
		content.append(".flatTable tr {\r\n");
		content.append("  height: 50px;\r\n");
		content.append("  background: #e0ecf8;\r\n");
		content.append("  border-bottom: rgba(0, 0, 0, 0.05) 1px solid;\r\n");
		content.append("}\r\n");
		content.append(".flatTable td {\r\n");
		content.append("  box-sizing: border-box;\r\n");
		content.append("  padding-left: 30px;\r\n");
		content.append("}\r\n");
		content.append(".flatTable .titleTr {\r\n");
		content.append("  height: 70px;\r\n");
		content.append("  color: #f6f3f7;\r\n");
		content.append("  background: #418a95;\r\n");
		content.append("  border: 0px solid;\r\n");
		content.append("}\r\n");
		content.append(".flatTable .headingTr {\r\n");
		content.append("  height: 30px;\r\n");
		content.append("  background: #63acb7;\r\n");
		content.append("  color: #f6f3f7;\r\n");
		content.append("  font-size: 8pt;\r\n");
		content.append("  border: 0px solid;\r\n");
		content.append("}");
		content.append("</style></head><body>");
		content.append("<h2>");
		content.append("Ok Eclipse Recommendations");
		content.append("</h2>");
		for (String question : questions) {

			content.append("<table class=\"flatTable\">");
			content.append("<tr class=\"titleTr\">");
			content.append("<td class=\"titleTd\">");
			content.append(question);
			content.append("</td>");
			content.append("<td colspan=\"4\">");
			content.append("</td>");
			content.append("<tr class=\"headingTr\">");
			content.append("<td>ACCEPTED</td>");
			content.append("<td>UPVOTES</td>");
			content.append("<td>");
			content.append("SOLUTION");
			content.append("</td>");
			content.append("</tr>");

			buildHTMLBodyContent(extractAnswers(question, monitor));

			content.append("</table>");
			content.append("<br></br>");

			String utubeUrl = "https://www.youtube.com/results?search_query=" + question;
			content.append("<a target=\"_blank\" href=\"" + utubeUrl
					+ "\"><img src=\"https://upload.wikimedia.org/wikipedia/commons/2/2e/YoutubeLogoLink.png\" alt=\"Smiley face\"></a>");

		}

		content.append("</body></html>");

		write();

		openBrowser();

	}

	public List<StackoverflowAnswer> extractAnswers(String question, IProgressMonitor monitor) {
		List<StackoverflowAnswer> stackoverflowAnswers = new LinkedList<>();

		Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(activeShell);
		try {
			dialog.run(false, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {

					monitor.beginTask("Fetching recommendations from Q&A repository...", 3);

					List<GoogleResult> googleResults = GoogleFetcher.getGoogleResults(question, monitor);
					List<StackoverflowPost> stackoverflowPosts;
					try {
						stackoverflowPosts = StackoverflowFetcher.getStackoverflowPosts(googleResults, monitor);
						monitor.worked(1);
						CustomQuestionPage questionPage = new CustomQuestionPage();
						stackoverflowAnswers.addAll(StackoverflowFetcher.getStackoverflowAnswers(stackoverflowPosts,
								monitor, questionPage));

						if (stackoverflowAnswers.isEmpty()) {
							questionPage.setUpvoted(false);
							stackoverflowAnswers.addAll(StackoverflowFetcher.getStackoverflowAnswers(stackoverflowPosts,
									monitor, questionPage));
						}

						if (stackoverflowAnswers.isEmpty()) {
							questionPage.setAccepted(false);
							stackoverflowAnswers.addAll(StackoverflowFetcher.getStackoverflowAnswers(stackoverflowPosts,
									monitor, questionPage));
						}

						monitor.worked(2);
						monitor.done();

					} catch (IOException e) {
						e.printStackTrace();
						Util.showError(e, "Unable to get Q&A posts " + e.getMessage());
					}

				}
			});
		} catch (InvocationTargetException | InterruptedException e) {
			e.printStackTrace();
			Util.showError(e, "Unable to fetch from Q&A repository " + e.getMessage());
		}

		return stackoverflowAnswers;
	}

//	private void openExternalBrowser(URL url) throws PartInitException, MalformedURLException {
//		final IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
//		browser.openURL(url);
//	}

	private void openBrowser() throws PartInitException, MalformedURLException {
		final IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport()
				.createBrowser("org.eclipse.ui.browser");
		browser.openURL((getResponseFile()).toURI().toURL());
	}

	private void write() throws IOException {

		try (PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(getResponseFile()), StandardCharsets.UTF_8)))) {

			out.println(content);

		} catch (IOException e) {
			throw e;
		}
	}

	private static File getResponseFile() {
		return new File(Util.getPrefixPath() + FILENAME);
	}

	private void buildHTMLBodyContent(List<StackoverflowAnswer> stackoverflowAnswers) {

		for (StackoverflowAnswer answer : stackoverflowAnswers) {

			content.append("<tr><td>");
			if (answer.isAccepted()) {
				content.append("Yes");
			} else {
				content.append("No");
			}

			content.append("</td>");
			content.append("<td>");
			content.append(answer.getVoteCount());
			content.append("</td>");
			content.append("<td>");
			content.append(answer.getBody());
			content.append("<br /><br />");
			content.append(answer.getUrl());
			content.append("</td></tr>");
		}

	}

	class CustomQuestionPage extends QuestionPage {

		private boolean accepted = true;
		private boolean upvoted = true;

		public void setAccepted(boolean accepted) {
			this.accepted = accepted;
		}

		public void setUpvoted(boolean upvoted) {
			this.upvoted = upvoted;
		}

		@Override
		public boolean isAcceptedOnly() {
			return accepted;
		}

		@Override
		public boolean isUpVotedOnly() {
			return upvoted;
		}
	}

}
