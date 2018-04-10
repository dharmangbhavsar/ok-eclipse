import java.io.ByteArrayOutputStream;
import java.io.File;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

 
public class JGitPrintContent
{
  public static void main(String[] args) throws Exception
  {
	
    File gitWorkDir = new File("C:\\Users\\tushi\\Documents\\GitHub\\gittest\\test.txt");
    
    gitWorkDir.createNewFile();
    Git git = Git.init().setDirectory( gitWorkDir.getParentFile() ).call();
    assertTrue( git.status().call().getUntracked().contains( gitWorkDir.getName() ) );

    
//    Git git = Git.open(gitWorkDir);
//    Repository repo = git.getRepository();
// 
//    ObjectId lastCommitId = repo.resolve(Constants.HEAD);
// 
//    RevWalk revWalk = new RevWalk(repo);
//    RevCommit commit = revWalk.parseCommit(lastCommitId);
// 
//    RevTree tree = commit.getTree();
// 
//    TreeWalk treeWalk = new TreeWalk(repo);
//    treeWalk.addTree(tree);
//    treeWalk.setRecursive(true);
//    treeWalk.setFilter(PathFilter.create("file1.txt"));
//    if (!treeWalk.next()) 
//    {
//      System.out.println("Nothing found!");
//      return;
//    }
// 
//    ObjectId objectId = treeWalk.getObjectId(0);
//    ObjectLoader loader = repo.open(objectId);
// 
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    loader.copyTo(out);
//    System.out.println("file1.txt:\n" + out.toString());
    
    
    //Repository localRepo = new FileRepository(localPath);
    //Git git = new Git(localRepo); 

    // add remote repo:
    RemoteAddCommand remoteAddCommand = git.remoteAdd();
    remoteAddCommand.setName("origin");
    remoteAddCommand.setUri(new URIish("https://github.com/tushitarc/test"));
    // you can add more settings here if needed
    remoteAddCommand.call();

    // push to remote:
    PushCommand pushCommand = git.push();
    pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("username", "password"));
    // you can add more settings here if needed
    pushCommand.call();
    
    }
}