
import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

public class CloneRepository {
	private static final String REMOTE_URL = "https://github.com/dharmangbhavsar/ok-eclipse.git";

    public static void main(String[] args) throws IOException, GitAPIException {

    	File gitWorkDir = new File("C:\\Users\\tushi\\Documents\\GitHub\\gittest");        
        gitWorkDir.mkdir();
        	
        // then clone
        System.out.println("Cloning from " + REMOTE_URL + " to " + gitWorkDir);
        
        try (Git result = Git.cloneRepository()
                .setURI(REMOTE_URL)
                .setDirectory(gitWorkDir)
                .call()) {
	        // Note: the call() returns an opened repository already which needs to be closed to avoid file handle leaks!
	        System.out.println("Having repository: " + result.getRepository().getDirectory());
        }
    }  
}