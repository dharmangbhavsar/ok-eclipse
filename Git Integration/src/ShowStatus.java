import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;


public class ShowStatus {

    public static void main(String[] args) throws IOException, GitAPIException {
    	FileRepositoryBuilder builder = new FileRepositoryBuilder();
    	try (Repository repository =builder
        	    .setGitDir(new File("C:/Users/tushi/Documents/GitHub/ok-eclipse/.git"))
        	    .build()) {
            try (Git git = new Git(repository)) {
                Status status = git.status().call();
                System.out.println("Added: " + status.getAdded());
                System.out.println("Changed: " + status.getChanged());
                System.out.println("Conflicting: " + status.getConflicting());
                System.out.println("ConflictingStageState: " + status.getConflictingStageState());
                System.out.println("IgnoredNotInIndex: " + status.getIgnoredNotInIndex());
                System.out.println("Missing: " + status.getMissing());
                System.out.println("Modified: " + status.getModified());
                System.out.println("Removed: " + status.getRemoved());
                System.out.println("Untracked: " + status.getUntracked());
                System.out.println("UntrackedFolders: " + status.getUntrackedFolders());
            }
        }
    }
    
    
    public static Repository openJGitRepository() throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
//        return builder
//                .readEnvironment() // scan environment GIT_* variables
//                .findGitDir() // scan up the file system tree
//                .build();
        
        return builder
        	    .setGitDir(new File("C:/Users/tushi/Documents/GitHub/ok-eclipse/.git"))
        	    .build();
    }
}