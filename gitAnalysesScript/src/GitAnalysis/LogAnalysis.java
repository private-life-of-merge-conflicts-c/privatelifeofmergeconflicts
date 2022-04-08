package GitAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LogAnalysis {
	
	private String repoLocation;
	private ArrayList<LogAction> logs;
	private ArrayList<LogAction> commitAmendList = new ArrayList<>();
	private ArrayList<LogAction> cherryPickList = new ArrayList<>();
	private ArrayList<LogAction> failedCherryPickList = new ArrayList<>();
	private ArrayList<Squash> squashList = new ArrayList<>(); 
	private ArrayList<LogAction> mergeList = new ArrayList<>();
	private ArrayList<LogAction> failedMergeList = new ArrayList<>();
	private ArrayList<LogAction> missingMergeCommitList = new ArrayList<>();
	private ArrayList<LogAction> failedRebaseList = new ArrayList<>();
	private ArrayList<LogAction> skippedRebaseList = new ArrayList<>();
	private ArrayList<LogAction> abortedRebaseList = new ArrayList<>();
	private ArrayList<Rebase> rebases = new ArrayList<>();
	private long totalFailedRebases = 0;
    private long totalIntegrationErrors = 0;
	private Date initialDate;
	private Date finalDate;
	private long period;
	private int nLogFiles;

	public LogAnalysis(String path) {
		this.repoLocation = path;
		this.logs = extractLogActions();
		this.initialDate = calculateInitialDate();
		this.finalDate = calculateFinalDate();
		this.period = calculatePeriodBetweenDates();
		this.nLogFiles = 1;
	}
	
	public Date calculateInitialDate() {
		LogAction firstLog = logs.get(0);
		
		return firstLog.getDate();
	}
	
	public Date calculateFinalDate() {
		int logsSize = logs.size();
		LogAction lastLog = logs.get(logsSize - 1);
		
		return lastLog.getDate();
	}
	
	public long calculatePeriodBetweenDates() {
		Date initialDate = getInitialDate();
		Date finalDate = getFinalDate();

		long diff = finalDate.getTime() - initialDate.getTime();
		long diffDays = diff / (24 * 60 * 60 * 1000);
		
		return diffDays;
	}
	
	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}
	
	public ArrayList<LogAction> extractLogActions(){
		LogAction log;
		ArrayList<LogAction> logActions = new ArrayList<LogAction>();
		String logPath = this.repoLocation;
		File logFile = new File(logPath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(logFile));
			String line;
			while ((line = br.readLine()) != null) {

				String[] parts = line.split(" ");
				if(parts.length > 2) {
					log = new LogAction(line);
					logActions.add(log);
				}
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
	
	public void collectAllActions() {
		int index = 0;
		
		ArrayList<LogAction> associatedCommitsSquash = new ArrayList<>();
		Boolean newSquash = false;
		int rebaseStartIndex = 0;
		String rebaseStartHash = "";
		
		boolean newRebase = false;
		int totalCommitsRebase = 0;
		int totalFailedRebases = 0;
		int totalSkippedRebases = 0;
		int totalFailedSquash = 0;
		int totalSkippedSquash = 0;

		for (LogAction log : this.logs) { 
			
			if (log.getCommand() == null) {
				continue;
			}
			if (log.getCommand().equals("cherry-pick")) {
				cherryPickList.add(log);
			}
			else if (log.getCommand().equals("commit (cherry-pick)")) {
				failedCherryPickList.add(log);
			}
			else if (log.getCommand().startsWith("merge") && !(log.getMessage().startsWith("Fast-forward"))) {
				mergeList.add(log);
			}
			else if (log.getCommand().equals("commit (merge)")) {
				failedMergeList.add(log);
			}
			else if (log.getCommand().equals("commit (amend)")) {
				commitAmendList.add(log);
			}
			else if (log.getCommand().startsWith("rebase -i")) {
				int next = index + 1;

				if (log.getCommand().equals("rebase -i (start)")) {
					
					if (!logs.get(next).getCommand().equals("rebase -i (finish)")) {
						newRebase = true;
						totalCommitsRebase++;
					}
					
					rebaseStartIndex = index;
					rebaseStartHash = log.getGitHash();
				}
				else if ((log.getCommand().equals("rebase -i")) && (log.getMessage().equals("fast-forward"))) {
					newSquash = false;

					newRebase = false;
					totalCommitsRebase=0;
					totalFailedRebases=0;
					totalSkippedRebases=0;
				}
				else if (newRebase && log.getCommand().equals("rebase -i (squash)")) {
					totalCommitsRebase++;
					
					associatedCommitsSquash.add(log);
					newSquash = true;
				}
				else if (newRebase && log.getCommand().equals("rebase -i (continue)")) {
					failedRebaseList.add(log);
					totalCommitsRebase++;
					totalFailedRebases++;

					if (newSquash) {
						totalFailedSquash++;
					}
				}
				else if(newRebase && log.getCommand().equals("rebase -i (skip)")) {
					skippedRebaseList.add(log);
					totalCommitsRebase++;
					totalSkippedRebases++;

					if (newSquash) {
						totalSkippedSquash++;
					}
				}
				else if (log.getCommand().equals("rebase -i (abort)")) {
					totalCommitsRebase++;

					if (newSquash) {
						Squash currentSquash = new Squash(totalFailedRebases, totalSkippedRebases, false);
						currentSquash.setFinalHash(log.getGitHash());
						currentSquash.setAssociatedCommits(associatedCommitsSquash);
						this.getMissingMergeCommit(currentSquash, rebaseStartIndex, rebaseStartHash);
						squashList.add(currentSquash);

						newSquash = false;
						associatedCommitsSquash.clear();
					}
					else {
						Rebase rebase = new Rebase(totalCommitsRebase, totalFailedRebases, totalSkippedRebases, false);
						rebases.add(rebase);
						abortedRebaseList.add(log);
					}

					newRebase = false;
					totalCommitsRebase=0;
					totalFailedRebases=0;
					totalSkippedRebases=0;
				} 
				else if (newRebase && log.getCommand().equals("rebase -i (finish)")) {
					totalCommitsRebase++;
					
					if (newSquash) {
						Squash currentSquash = new Squash(totalFailedRebases, totalSkippedRebases, true);
						currentSquash.setFinalHash(log.getGitHash());
						currentSquash.setAssociatedCommits(associatedCommitsSquash);
						this.getMissingMergeCommit(currentSquash, rebaseStartIndex, rebaseStartHash);
						squashList.add(currentSquash);
						
						newSquash = false;
						associatedCommitsSquash.clear();
					}
					else {
						Rebase rebase = new Rebase(totalCommitsRebase, totalFailedRebases, totalSkippedRebases, true);
						rebases.add(rebase);
					}
					
					newRebase = false;
					totalCommitsRebase=0;
					totalFailedRebases=0;
					totalSkippedRebases=0;
				}
				else if (newRebase) {
					totalCommitsRebase++;
				}
			}
			else if (log.getCommand().startsWith("rebase (")) {
				int next = index + 1;

				if (log.getCommand().equals("rebase (start)")) {

					if (!logs.get(next).getCommand().equals("rebase (finish)")) {
						newRebase = true;
						totalCommitsRebase++;
					}

					rebaseStartIndex = index;
					rebaseStartHash = log.getGitHash();
				}
				else if ((log.getMessage().equals("fast-forward"))) {
					newSquash = false;

					newRebase = false;
					totalCommitsRebase=0;
					totalFailedRebases=0;
					totalSkippedRebases=0;
				}
				else if (newRebase && log.getCommand().equals("rebase (squash)")) {
					totalCommitsRebase++;

					associatedCommitsSquash.add(log);
					newSquash = true;
				}
				else if (newRebase && log.getCommand().equals("rebase (continue)")) {
					failedRebaseList.add(log);
					totalCommitsRebase++;
					totalFailedRebases++;
				}
				else if(newRebase && log.getCommand().equals("rebase (skip)")) {
					skippedRebaseList.add(log);
					totalCommitsRebase++;
					totalSkippedRebases++;
				}
				else if (log.getCommand().equals("rebase (abort)")) {
					totalCommitsRebase++;

					if (newSquash) {
						Squash currentSquash = new Squash(totalFailedRebases, totalSkippedRebases, false);
						currentSquash.setFinalHash(log.getGitHash());
						currentSquash.setAssociatedCommits(associatedCommitsSquash);
						this.getMissingMergeCommit(currentSquash, rebaseStartIndex, rebaseStartHash);
						squashList.add(currentSquash);

						newSquash = false;
						associatedCommitsSquash.clear();
					}
					else {
						Rebase rebase = new Rebase(totalCommitsRebase, totalFailedRebases, totalSkippedRebases, false);
						rebases.add(rebase);
						abortedRebaseList.add(log);
					}

					newRebase = false;
					totalCommitsRebase=0;
					totalFailedRebases=0;
					totalSkippedRebases=0;
				}
				else if (newRebase && log.getCommand().equals("rebase (finish)")) {
					totalCommitsRebase++;

					if (newSquash) {
						Squash currentSquash = new Squash(totalFailedRebases, totalSkippedRebases, true);
						currentSquash.setFinalHash(log.getGitHash());
						currentSquash.setAssociatedCommits(associatedCommitsSquash);
						this.getMissingMergeCommit(currentSquash, rebaseStartIndex, rebaseStartHash);
						squashList.add(currentSquash);

						newSquash = false;
						associatedCommitsSquash.clear();
					}
					else {
						Rebase rebase = new Rebase(totalCommitsRebase, totalFailedRebases, totalSkippedRebases, true);
						rebases.add(rebase);
					}

					newRebase = false;
					totalCommitsRebase=0;
					totalFailedRebases=0;
					totalSkippedRebases=0;
				}
				else if (newRebase) {
					totalCommitsRebase++;
				}
			}
			else if (log.getCommand().equals("rebase") && !(log.getMessage().startsWith("fast-forward"))) {
				int next = index + 1;
				
				if ((log.getMessage().startsWith("checkout")) && !(logs.get(next).getCommand().equals("rebase finished"))) {
					newRebase = true;
					totalCommitsRebase++;
				}
				else if (newRebase && (log.getMessage().startsWith("aborting")) && !(logs.get(next).getCommand().contains("rebase -i (abort)"))) {
					totalCommitsRebase = totalCommitsRebase + 2;

					if (newSquash) {
						Squash currentSquash = new Squash(totalFailedRebases, totalSkippedRebases, false);
						currentSquash.setFinalHash(log.getGitHash());
						currentSquash.setAssociatedCommits(associatedCommitsSquash);
						this.getMissingMergeCommit(currentSquash, rebaseStartIndex, rebaseStartHash);
						squashList.add(currentSquash);

						newSquash = false;
						associatedCommitsSquash.clear();
					}
					else {
						Rebase rebase = new Rebase(totalCommitsRebase, totalFailedRebases, totalSkippedRebases, false);
						rebases.add(rebase);
						abortedRebaseList.add(log);
					}

					newRebase = false;
					totalCommitsRebase=0;
					totalFailedRebases=0;
					totalSkippedRebases=0;
				}
				else if (!(log.getMessage().startsWith("aborting")) && !(logs.get(next).getCommand().equals("rebase"))
						&& !(logs.get(next).getCommand().equals("rebase finished"))) {
					totalCommitsRebase++;

					if (newSquash) {
						Squash currentSquash = new Squash(totalFailedRebases, totalSkippedRebases, true);
						currentSquash.setFinalHash(log.getGitHash());
						currentSquash.setAssociatedCommits(associatedCommitsSquash);
						this.getMissingMergeCommit(currentSquash, rebaseStartIndex, rebaseStartHash);
						squashList.add(currentSquash);

						newSquash = false;
						associatedCommitsSquash.clear();
					}
					else {
						Rebase rebase = new Rebase(totalCommitsRebase, totalFailedRebases, totalSkippedRebases, true);
						rebases.add(rebase);
					}

					newRebase = false;
					totalCommitsRebase=0;
					totalFailedRebases=0;
					totalSkippedRebases=0;
				}
				else if (newRebase) {
					totalCommitsRebase++;
				}
				else {
					newRebase = true;
					totalCommitsRebase++;
				}
			}
			else if (newRebase && log.getCommand().equals("rebase finished")) {
				totalCommitsRebase++;

				if (newSquash) {
					Squash currentSquash = new Squash(totalFailedRebases, totalSkippedRebases, true);
					currentSquash.setFinalHash(log.getGitHash());
					currentSquash.setAssociatedCommits(associatedCommitsSquash);
					this.getMissingMergeCommit(currentSquash, rebaseStartIndex, rebaseStartHash);
					squashList.add(currentSquash);

					newSquash = false;
					associatedCommitsSquash.clear();
				}
				else {
					Rebase rebase = new Rebase(totalCommitsRebase, totalFailedRebases, totalSkippedRebases, true);
					rebases.add(rebase);
				}

				newRebase = false;
				totalCommitsRebase=0;
				totalFailedRebases=0;
				totalSkippedRebases=0;
			}
			else if (log.getCommand().startsWith("pull ") && (log.getCommand().contains("--rebase ") || log.getCommand().contains("-r ")) && !(log.getMessage().startsWith("Fast-forward"))) {
				int next = index + 1;

				totalCommitsRebase++;

				if (logs.get(next).getCommand().equals("rebase")) {
					totalFailedRebases++;
					failedRebaseList.add(logs.get(next));
				}

				if (!newRebase) {
					newRebase = true;
				}
			}
			
			index++;
		}
	}
	
	public void getMissingMergeCommit(Squash currentSquash, int indexHash, String hash) {
		int indexList = indexHash - 1;
		LogAction log = this.logs.get(indexList);
		
		while(!log.getGitHash().equals(hash) && (indexList > 0)) {
			
			if((log.getCommand().equals("commit (merge)")) || (log.getCommand().startsWith("merge") &&
					!(log.getMessage().startsWith("Fast-forward")))) {
				missingMergeCommitList.add(log);
				currentSquash.addMissingMergeCommit(log);
				currentSquash.setIntegration(true);
			}
			
			indexList--;
			log = this.logs.get(indexList);
		}
	}

	public void calculateTotalFailedRebases() {
		long result = 0;
		
		for(Rebase rebase : rebases) {
			if((rebase.getTotalFailedRebases() > 0) || (rebase.getTotalSkippedRebases() > 0)) {
				result++;
			}
		}
		
		setTotalFailedRebase(result);
	}
	
	public void calculateIntegrationErrors() {
		setTotalIntegrationErrors(
			failedRebaseList.size() + skippedRebaseList.size() + abortedRebaseList.size());
	}

	public void compareAndSetInitialDates(LogAnalysis logAnalysis){
		Date initialDateLogAnalysis = logAnalysis.getInitialDate();

		if (initialDateLogAnalysis.before(getInitialDate())) {
			setInitialDate(initialDateLogAnalysis);
		}
	}

	public void compareAndSetFinalDates(LogAnalysis logAnalysis){
		Date finalDateLogAnalysis = logAnalysis.getFinalDate();

		if (finalDateLogAnalysis.after(getInitialDate())) {
			setFinalDate(finalDateLogAnalysis);
		}
	}

	public void mergeLogAnalysis(LogAnalysis logAnalysis){
		incrementNLogFiles();

		compareAndSetInitialDates(logAnalysis);
		compareAndSetFinalDates(logAnalysis);
		setPeriod(calculatePeriodBetweenDates());

		addLogs(logAnalysis.getLogs());

		addCommitAmendList(logAnalysis.getCommitAmendList());

		addCherryPickList(logAnalysis.getCherryPickList());
		addFailedCherryPickList(logAnalysis.getFailedCherryPickList());

		addSquashList(logAnalysis.getSquashList());
		addCommitMergeGone(logAnalysis.getMissingMergeCommitList());

		addMergeList(logAnalysis.getMergeList());
		addFailedMergeList(logAnalysis.getFailedMergeList());

		addFailedRebaseList(logAnalysis.getFailedRebaseList());
		addSkippedRebaseList(logAnalysis.getSkippedRebaseList());
		addAbortedRebaseList(logAnalysis.getAbortedRebaseList());
		addRebases(logAnalysis.getRebases());
		addTotalFailedRebases(logAnalysis.getTotalFailedRebase());
		addTotalIntegrationErrors(logAnalysis.getTotalIntegrationErrors());
	}
	
	public String getRepoLocation() {
		return repoLocation;
	}

	public void setRepoLocation(String repoLocation) {
		this.repoLocation = repoLocation;
	}

	public ArrayList<LogAction> getLogs() {
		return logs;
	}

	public void setLogs(ArrayList<LogAction> logs) {
		this.logs = logs;
	}
	
	public void addLogs(ArrayList<LogAction> logs) {
		this.logs.addAll(logs);
	}

	public ArrayList<LogAction> getCommitAmendList() {
		return commitAmendList;
	}

	public void setCommitAmendList(ArrayList<LogAction> commitAmendList) {
		this.commitAmendList = commitAmendList;
	}

	public void addCommitAmendList(ArrayList<LogAction> commitAmendList) {
		this.commitAmendList.addAll(commitAmendList);
	}

	public ArrayList<LogAction> getCherryPickList() {
		return cherryPickList;
	}

	public void setCherryPickList(ArrayList<LogAction> cherryPickList) {
		this.cherryPickList = cherryPickList;
	}
	
	public void addCherryPickList(ArrayList<LogAction> cherryPickList) {
		this.cherryPickList.addAll(cherryPickList);
	}
	
	public ArrayList<Squash> getSquashList() {
		return squashList;
	}
	
	public void setSquashList(ArrayList<Squash> squashList) {
		this.squashList = squashList;
	}
	
	public void addSquashList(ArrayList<Squash> squashList) {
		this.squashList.addAll(squashList);
	}
	
	public ArrayList<LogAction> getMergeList() {
		return mergeList;
	}

	public void setMergeList(ArrayList<LogAction> mergeCommitList) {
		this.mergeList = mergeCommitList;
	}
	
	public void addMergeList(ArrayList<LogAction> mergeList) {
		this.mergeList.addAll(mergeList);
	}

	public ArrayList<LogAction> getMissingMergeCommitList() {
		return missingMergeCommitList;
	}

	public void setMissingMergeCommitList(ArrayList<LogAction> missingMergeCommitList) {
		this.missingMergeCommitList = missingMergeCommitList;
	}
	
	public void addCommitMergeGone(ArrayList<LogAction> commitMergeGone) {
		this.missingMergeCommitList.addAll(commitMergeGone);
	}
	
	public ArrayList<Rebase> getRebases() {
		return rebases;
	}

	public void setRebases(ArrayList<Rebase> rebases) {
		this.rebases = rebases;
	}
	
	public void addRebases(ArrayList<Rebase> rebases) {
		this.rebases.addAll(rebases);
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

	public long getPeriod() {
		return period;
	}

	public void setPeriod(long period) {
		this.period = period;
	}

	public ArrayList<LogAction> getFailedCherryPickList() {
		return failedCherryPickList;
	}

	public void setFailedCherryPickList(ArrayList<LogAction> failedCherryPickList) {
		this.failedCherryPickList = failedCherryPickList;
	}

	public ArrayList<LogAction> getSkippedRebaseList() {
		return skippedRebaseList;
	}

	public void setSkippedRebaseList(ArrayList<LogAction> skippedRebaseList) {
		this.skippedRebaseList = skippedRebaseList;
	}

	public ArrayList<LogAction> getAbortedRebaseList() {
		return abortedRebaseList;
	}

	public void setAbortedRebaseList(ArrayList<LogAction> abortedRebaseList) {
		this.abortedRebaseList = abortedRebaseList;
	}

	public ArrayList<LogAction> getFailedMergeList() {
		return failedMergeList;
	}

	public void setFailedMergeList(ArrayList<LogAction> failedMergeList) {
		this.failedMergeList = failedMergeList;
	}

	public long getTotalFailedRebase() {
		return totalFailedRebases;
	}

	public void setTotalFailedRebase(long totalFailedRebase) {
		this.totalFailedRebases = totalFailedRebase;
	}

	public void addFailedCherryPickList(ArrayList<LogAction> failedCherryPickList) {
		this.failedCherryPickList.addAll(failedCherryPickList);
	}

	public void addFailedMergeList(ArrayList<LogAction> failedMergeList) {
		this.failedMergeList.addAll(failedMergeList);
	}

	public void addSkippedRebaseList(ArrayList<LogAction> skippedRebaseList) {
		this.skippedRebaseList.addAll(skippedRebaseList);
	}

	public void addAbortedRebaseList(ArrayList<LogAction> abortedRebaseList2) {
		this.abortedRebaseList.addAll(abortedRebaseList2);
	}

	public void addFailedRebaseList(ArrayList<LogAction> failedRebaseList) {
		this.failedRebaseList.addAll(failedRebaseList);
	}

	public ArrayList<LogAction> getFailedRebaseList() {
		return failedRebaseList;
	}

	public void setFailedRebaseList(ArrayList<LogAction> failedRebaseList) {
		this.failedRebaseList = failedRebaseList;
	}

	public void addTotalFailedRebases(long failedRebases) {
		this.totalFailedRebases = this.totalFailedRebases + failedRebases;
	}

	public long getTotalIntegrationErrors() {
		return totalIntegrationErrors;
	}

	public void setTotalIntegrationErrors(long totalIntegrationErros) {
		this.totalIntegrationErrors = totalIntegrationErros;
	}

	public void addTotalIntegrationErrors(long integrationErrors) {
		this.totalIntegrationErrors = this.totalIntegrationErrors + integrationErrors;
	}

	public boolean haveIntegration() {
		return (calculateHiddenIntegrations() + calculateTotalMerge()) > 0;
	}
	
	public int calculateHiddenIntegrations() {

		return this.cherryPickList.size() + this.failedCherryPickList.size() + this.rebases.size() + this.squashList.size();
	}
	
	public int calculateHiddenIntegrationErrors() {
		
		return this.failedCherryPickList.size() + calculateFailedRebases() + calculateFailedSquashes();
	}

	public int calculateTotalCherryPick() {
		return this.cherryPickList.size() + this.failedCherryPickList.size();
	}

	public int calculateTotalMerge() {

		return this.getMergeList().size() + this.getFailedMergeList().size();
	}

	public int calculateTotalIntegration() {
		return this.calculateHiddenIntegrations() + this.calculateTotalMerge();
	}

	public float calculateHIFrequency() {
		float hiddenIntegration = this.calculateHiddenIntegrations();
		float totalIntegration = hiddenIntegration + this.calculateTotalMerge();

		if(totalIntegration > 0) {
			return (hiddenIntegration / totalIntegration) ;
		}

		return 0;
	}

	public float calculateMergeFrequency() {
		float totalMerge = this.calculateTotalMerge();
		float totalIntegration = totalMerge + this.calculateHiddenIntegrations();

		if(totalIntegration > 0) {
			return (totalMerge / totalIntegration) ;
		}

		return 0;
	}

	public float calculateFailedMergeFrequency() {
		float totalMerge = this.calculateTotalMerge();

		if(totalMerge > 0) {
			return (( this.getFailedMergeList().size() / totalMerge) * 100) ;
		}

		return 0;
	}
	
	public int calculateFailedRebases() {
		int result = 0;
		
		for (Rebase rebase : this.rebases) {
			if (!rebase.getRebaseSuccessfull() || (rebase.getTotalFailedRebases() > 0) || (rebase.getTotalSkippedRebases() > 0)) {
				result = result + 1;
			}
		}
		
		return result;
	}

	public int calculateFailedSquashes() {
		int result = 0;

		for (Squash squash : this.squashList) {
			if (!squash.isSuccessfull() || (squash.getTotalFailures() > 0) || (squash.getTotalSkipped() > 0)) {
				result = result + 1;
			}
		}

		return result;
	}

	public int calculateTotalLogAction() {
		int result = this.logs.size();
		int linesRebase = 0;
		
		for(Rebase rebase : this.rebases) {
			linesRebase = linesRebase + rebase.getTotalCommits();
		}
		
		result = result - linesRebase;
		
		result = result + this.rebases.size();
		
		return result;
	}

	public int getnLogFiles() {
		return nLogFiles;
	}

	public void setnLogFiles(int nLogFiles) {
		this.nLogFiles = nLogFiles;
	}

	public void incrementNLogFiles(){
		int nLogFiles = getnLogFiles();
		setnLogFiles(nLogFiles + 1);
	}

	public float calculatePercentage(int number, int total) {
			return roundTwoDecimals(((float) number / (float) total) * 100)  ;
	}

	public float roundTwoDecimals(float number) {
		DecimalFormat twoDForm = new DecimalFormat("#.##",  new DecimalFormatSymbols(Locale.ENGLISH));
		return Float.parseFloat(twoDForm.format(number));
	}

}
