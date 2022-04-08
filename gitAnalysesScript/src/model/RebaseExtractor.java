package model;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Properties;

public class RebaseExtractor {

	private ArrayList<Rebase> rebaseList;

	private String repoLocation;

	public void ExtractRebases() {
		boolean abortRebase = false;
		this.rebaseList = new ArrayList<Rebase>();
		ArrayList<String> logActions = this.extractLogActions();
		int i = logActions.size() - 1;
		while (i > 0) {
			String action = logActions.get(i).split(":")[0];
			if(action.contains("rebase") && action.contains("finish")) {
				String hash_p1 = action.split(" ")[0];
				Commit parent1 = new Commit(hash_p1);
				while (action.contains("rebase")) {
					i--;
					action = logActions.get(i).split(":")[0];
					if(action.contains("abort")) {
						abortRebase = true;
					}
				}
				if(!abortRebase) {
					action = logActions.get(i + 1).split(":")[0];
					String[] parts = action.split(" ");
					Commit parent2 = new Commit(parts[1]);
					Commit oldParent1 = new Commit(parts[0]);
					Rebase rebase = new Rebase(parent1, parent2, oldParent1);
					this.rebaseList.add(rebase);
				}
			}
			i--;
		}
	}

	public void retrieveBases() {
		for (Rebase rebase : this.rebaseList) {
			String hash_base = this.retrieveBase(rebase);
			Commit base = new Commit(hash_base);
			rebase.setBase(base);
		}
	}

	public String retrieveBase(Rebase rebase) {
		String hash = "";
		ProcessBuilder pb = new ProcessBuilder("git", "merge-base", rebase.getOldParent1().getHash(),
				rebase.getParent2().getHash());
		pb.directory(new File(this.repoLocation));
		try {
			Process p = pb.start();
			BufferedReader buf = new BufferedReader(new InputStreamReader(p.getInputStream()));
			hash = buf.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hash;
	}

	public ArrayList<String> extractLogActions(){
		ArrayList<String> logActions = new ArrayList<String>();
		File logFile = this.openLogFile();
		try {
			BufferedReader br = new BufferedReader(new FileReader(logFile));
			String line;
			while ((line = br.readLine()) != null) {
				logActions.add(line);
			} 
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return logActions;
	}

	public File openLogFile() {
		File logFile = null;
		Properties prop = new Properties();
		InputStream input;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			this.repoLocation = prop.getProperty("repo");
			String path = this.repoLocation + File.separator + ".git" + File.separator + "logs" +
					File.separator + "HEAD";
			logFile = new File(path);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return logFile;
	}

	public ArrayList<Rebase> getRebaseList() {
		return rebaseList;
	}

	public void setRebaseList(ArrayList<Rebase> rebaseList) {
		this.rebaseList = rebaseList;
	}

	public String getRepoLocation() {
		return repoLocation;
	}

	public void setRepoLocation(String repoLocation) {
		this.repoLocation = repoLocation;
	}

	public static void main(String[] args) {
		RebaseExtractor extractor = new RebaseExtractor();
		extractor.ExtractRebases();
		extractor.retrieveBases();
	}

}
