package com.sohelper.datatypes;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.sohelper.exceptions.StackoverflowParserException;

/**
 * Copyright (c) 2016 Maroun Maroun
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE. 
 * 
 * This class is used to construct a Stack Overflow answer.
 * @modified
 */
public class StackoverflowAnswer {

	private Element element;
	private String user = "";
	private String reputation;
	private String url = "";
	private String body;
	private String voteCount;
	private String userUrl;
	private boolean isAccepted;
	private boolean isUpVoted;

	/**
	 * @modified
	 * @param element
	 * @throws StackoverflowParserException
	 */
	public StackoverflowAnswer(Element element) throws StackoverflowParserException {
		try {
			this.element = element;

			String acceptedAnswerText = element.getElementsByAttributeValue("itemprop", "acceptedAnswer")
					.attr("itemprop").toString();
			this.isAccepted = "acceptedAnswer".equals(acceptedAnswerText);

			String votedUp = element.select("span[class=vote-count-post]").text();
			if (votedUp != null && !votedUp.isEmpty()) {
				int votedUpCount = Integer.parseInt(votedUp);
				this.isUpVoted = votedUpCount != 0 && votedUpCount > 0;
			} else {
				this.isUpVoted = false;
			}

			this.body = element.select("div.post-text").html().replace("<code>",
					"<span style=\"background-color: #DCDCDC\"><code>");
			this.body = this.body.replace("</code>", "</span></code>");

			this.voteCount = element.select("span.vote-count-post").text();

			Elements reputationElement = element.select("div.user-details").select("span.reputation-score");
			if (reputationElement.size() == 1) {
				this.reputation = reputationElement.text();
			} else if (reputationElement.size() > 1) {
				this.reputation = reputationElement.text().split("\\s+")[1];
			} else {
				this.reputation = "community wiki";
			}

			this.url = "stackoverflow.com" + element.select("a.short-link").attr("href");

			Element userElement = element.select("table.fw").select("div.user-info").last().select("a").last();
			this.user = userElement.text();
			this.userUrl = "stackoverflow.com" + userElement.attr("href");

		} catch (Exception e) {
			//@modified
			// throw new StackoverflowParserException(
			// "Couldn't parse Stack Overflow post with element: " + element.toString());
		}
	}

	public Element getElement() {
		return this.element;
	}

	public String getUser() {
		return this.user;
	}

	// String because of Jon Skeet
	public String getReputation() {
		return this.reputation;
	}

	public String getUrl() {
		return this.url;
	}

	public String getBody() {
		return this.body;
	}

	public String getVoteCount() {
		return voteCount;
	}

	public void setVoteCount(String voteCount) {
		this.voteCount = voteCount;
	}

	public String getUserUrl() {
		return userUrl;
	}

	public void setUserUrl(String userUrl) {
		this.userUrl = userUrl;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public boolean isUpVoted() {
		return isUpVoted;
	}

	public void setUpVoted(boolean isUpVoted) {
		this.isUpVoted = isUpVoted;
	}

}
