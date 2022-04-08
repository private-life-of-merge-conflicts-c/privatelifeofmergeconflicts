package GitAnalysis;

public class Rebase {
	
	private int totalCommits;
	private int totalFailedRebases;
	private int totalSkippedRebases;
	private boolean rebaseSuccessfull;
	
	public Rebase(int totalCommits, int totalFailedRebases, int totalSkippedRebases, boolean rebaseSuccessfull) {
		super();
		this.totalCommits = totalCommits;
		this.totalFailedRebases = totalFailedRebases;
		this.setTotalSkippedRebases(totalSkippedRebases);
		this.rebaseSuccessfull = rebaseSuccessfull;
	}

	public int getTotalCommits() {
		return totalCommits;
	}
	
	public void setTotalCommits(int totalCommits) {
		this.totalCommits = totalCommits;
	}
	
	public int getTotalFailedRebases() {
		return totalFailedRebases;
	}
	
	public void setTotalFailedRebases(int totalFailedRebases) {
		this.totalFailedRebases = totalFailedRebases;
	}
	
	public Boolean getRebaseSuccessfull() {
		return rebaseSuccessfull;
	}
	
	public void setRebaseSuccessfull(Boolean rebaseSuccessfull) {
		this.rebaseSuccessfull = rebaseSuccessfull;
	}

	public int getTotalSkippedRebases() {
		return totalSkippedRebases;
	}

	public void setTotalSkippedRebases(int totalSkippedRebases) {
		this.totalSkippedRebases = totalSkippedRebases;
	}
}
