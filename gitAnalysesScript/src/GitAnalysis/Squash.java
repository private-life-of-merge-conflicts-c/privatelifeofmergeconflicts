package GitAnalysis;

import java.util.ArrayList;

public class Squash {
	
	private String finalHash = "";
	private ArrayList<LogAction> associatedCommits = new ArrayList<LogAction>();
	private ArrayList<LogAction> missingMergeCommits = new ArrayList<LogAction>();
	private boolean integration = false;
	private int totalFailures = 0;
	private int totalSkipped = 0;
	private boolean successfull = true;
	
	public Squash() {
		super();
	}

	public Squash(int totalFailures, int totalSkipped, boolean successfull) {
		super();
		this.totalFailures = totalFailures;
		this.totalSkipped = totalSkipped;
		this.successfull = successfull;
	}

	public String getFinalHash() {
		return finalHash;
	}

	public void setFinalHash(String finalHash) {
		this.finalHash = finalHash;
	}

	public ArrayList<LogAction> getAssociatedCommits() {
		return associatedCommits;
	}

	public void setAssociatedCommits(ArrayList<LogAction> associatedCommits) {
		for(LogAction commit : associatedCommits) {
			if(!commit.getGitHash().equals(this.finalHash)) {
				this.associatedCommits.add(commit);
			}
		}		
	}
	
	public void addAssociatedCommit(LogAction commit) {
		if(!commit.getGitHash().equals(this.finalHash)) {	
			associatedCommits.add(commit);
		}
	}
	
	public void addMissingMergeCommit(LogAction mergeCommit) {
		missingMergeCommits.add(mergeCommit);
	}	

	public ArrayList<LogAction> getMissingMergeCommits() {
		return missingMergeCommits;
	}

	public void setMissingMergeCommits(ArrayList<LogAction> missingMergeCommits) {
		this.missingMergeCommits = missingMergeCommits;
	}

	public boolean hasIntegration() {
		return integration;

	}

	public void setIntegration(boolean integration) {
		this.integration = integration;
	}

	public int getTotalFailures() {
		return totalFailures;
	}

	public void setTotalFailures(int totalFailures) {
		this.totalFailures = totalFailures;
	}

	public boolean isIntegration() {
		return integration;
	}

	public int getTotalSkipped() {
		return totalSkipped;
	}

	public void setTotalSkipped(int totalSkipped) {
		this.totalSkipped = totalSkipped;
	}

	public boolean isSuccessfull() {
		return successfull;
	}

	public void setSuccessfull(boolean successfull) {
		this.successfull = successfull;
	}
}
