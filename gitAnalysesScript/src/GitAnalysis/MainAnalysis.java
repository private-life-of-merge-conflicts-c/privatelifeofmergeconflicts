package GitAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class MainAnalysis {
	
	private String logInfo;
	private HashMap<String, LogAnalysis> projectAnalysis = new HashMap<>();
	private HashMap<String, LogAnalysis> collaboratorAnalysis = new HashMap<>();
	private LogAnalysis totalAnalysis = null;
	private ReportCreator reportCreator;
	private int totalLogsWithIntegration = 0;
	private int totalLogsWithoutIntegration = 0;

	public MainAnalysis(ReportCreator reportCreator) {
		super();
		this.reportCreator = reportCreator;
	}

	public File openLogFile() {
		File logInfoFile = null;
		Properties prop = new Properties();
		InputStream input;
        String csvFile = "gitLogInfo.csv";

		try {
			input = new FileInputStream("config.properties");
			prop.load(input);
			String resultsFilePath = prop.getProperty("resultsFilePath");
			reportCreator.setResultPath(resultsFilePath);
			logInfo = prop.getProperty("logInfo");
			String path = logInfo + File.separator + csvFile;
			logInfoFile = new File(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return logInfoFile;		
	}
	
	public void getGitLogCollaboratorInfo(){
		File logInfoFile = this.openLogFile();
		LogAnalysis logAnalysis;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(logInfoFile));
			String line;
	        String cvsSplitBy = ";";

			while ((line = br.readLine()) != null) {
                String[] info = line.split(cvsSplitBy);

                logAnalysis = new LogAnalysis(info[2]);
                logAnalysis.collectAllActions();
                logAnalysis.calculateTotalFailedRebases();
                logAnalysis.calculateIntegrationErrors();
                
                checkColaboratorLog(info[0], info[1], logAnalysis);
			  } 
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getGitLogProjectInfo(){
		File logInfoFile = this.openLogFile();
		LogAnalysis logAnalysis;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(logInfoFile));
			String line;
	        String cvsSplitBy = ";";

			while ((line = br.readLine()) != null) {
                String[] info = line.split(cvsSplitBy);
                
                logAnalysis = new LogAnalysis(info[2]);
                logAnalysis.collectAllActions();
                logAnalysis.calculateTotalFailedRebases();
                logAnalysis.calculateIntegrationErrors();

                
                checkProjectLog(info[1], logAnalysis);
			  } 
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void getGitLogTotalInfo(){
		File logInfoFile = this.openLogFile();
		LogAnalysis logAnalysis;
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(logInfoFile));
			String line;
	        String cvsSplitBy = ";";

			while ((line = br.readLine()) != null) {
                String[] info = line.split(cvsSplitBy);
                
                logAnalysis = new LogAnalysis(info[2]);
                logAnalysis.collectAllActions();
                logAnalysis.calculateTotalFailedRebases();
                logAnalysis.calculateIntegrationErrors();
                
                checkTotalLog(logAnalysis);
			  } 
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void checkProjectLog(String projectName, LogAnalysis logAnalysis) {
		LogAnalysis currentLogAnalysis;
		
		if(!projectAnalysis.containsKey(projectName)) {
			projectAnalysis.put(projectName, logAnalysis);
		} else {
			currentLogAnalysis = projectAnalysis.get(projectName);
			currentLogAnalysis.mergeLogAnalysis(logAnalysis);
			projectAnalysis.put(projectName, currentLogAnalysis);
		}
	}
	
	public void checkColaboratorLog(String collaboratorName, String projectName, LogAnalysis logAnalysis) {
		LogAnalysis currentLogAnalysis;

		String collaboratorProjectName = collaboratorName;
		if (projectName == null || !projectName.trim().isEmpty()) {
			collaboratorProjectName = collaboratorProjectName.concat(" - " + projectName);
		}

		if(!collaboratorAnalysis.containsKey(collaboratorProjectName)) {
			collaboratorAnalysis.put(collaboratorProjectName, logAnalysis);
			
		} else {
			currentLogAnalysis = collaboratorAnalysis.get(collaboratorProjectName);
			currentLogAnalysis.mergeLogAnalysis(logAnalysis);
			collaboratorAnalysis.put(collaboratorProjectName, currentLogAnalysis);
		}
	}

	public void checkTotalLog(LogAnalysis logAnalysis) {
		LogAnalysis currentLogAnalysis;

		if (logAnalysis.haveIntegration()) {
			if(totalAnalysis == null) {
				setTotalAnalysis(logAnalysis);

			} else {
				currentLogAnalysis = getTotalAnalysis();
				currentLogAnalysis.mergeLogAnalysis(logAnalysis);
				setTotalAnalysis(currentLogAnalysis);
			}

			totalLogsWithIntegration++;
		}
		else {
			totalLogsWithoutIntegration++;
		}
	}
	
	public void createProjectReport() {
	    reportCreator.createCSVFile("Project", "Project-TOTAL", projectAnalysis);
	    reportCreator.createHTMLTable("Project", "Project-TOTAL", projectAnalysis);
	    reportCreator.createRebaseHTMLTable("Project", "Project-TOTAL", projectAnalysis);
	}
	
	public void createCollaboratorReport() {
	    reportCreator.createCSVFile("Collaborator", "Collaborator-TOTAL", collaboratorAnalysis);
	    reportCreator.createHTMLTable("Collaborator", "Collaborator-TOTAL", collaboratorAnalysis);
		reportCreator.createHIFrequencyCSVFile("Collaborator", "Collaborator-HIFrequency", collaboratorAnalysis);
		reportCreator.createMergeFrequencyCSVFile("Collaborator", "Collaborator-MergeFrequency", collaboratorAnalysis);
		reportCreator.createIntegrationTotalCSVFile("Collaborator", "Collaborator-IntegrationTotal", collaboratorAnalysis);
		reportCreator.createIntegrationTotalAnalysisCSVFile("Collaborator", "Collaborator-IntegrationTotal-Analysis-HI", collaboratorAnalysis);
		reportCreator.createIntegrationTotalAnalysisMergeCSVFile("Collaborator", "Collaborator-IntegrationTotal-Analysis-Merge", collaboratorAnalysis);

		reportCreator.createFailedMergeFrequencyCSVFile("Collaborator", "Collaborator-FailedMergeFrequency", collaboratorAnalysis);
		reportCreator.createFailedMergeFrequencyRestrictedGroupCSVFile("Collaborator", "Collaborator-FailedMergeFrequency-RestrictedGroup", collaboratorAnalysis);

	}
	
	public void createTotalReport() {
	    reportCreator.createCSVFile("Total", "Total-TOTAL", totalAnalysis);
		reportCreator.createTotalHTMLTable("Total","Total-TOTAL", totalAnalysis);
	    reportCreator.createResumeReportHTML("Total", "Total-Resume", totalAnalysis,
				totalLogsWithIntegration, totalLogsWithoutIntegration);
	}

	public void removeCollaboratorAnalysisWithoutIntegration() {

		HashMap<String, LogAnalysis> logsWithtIntegration = new HashMap<>();

		for(String analysisKey : collaboratorAnalysis.keySet()) {
			LogAnalysis logAnalysis = collaboratorAnalysis.get(analysisKey);

			if(logAnalysis.haveIntegration()) {
				logsWithtIntegration.put(analysisKey, logAnalysis);
			}
		}

		setCollaboratorAnalysis(logsWithtIntegration);
	}

	public String getLogInfo() {
		return logInfo;
	}

	public void setLogInfo(String logInfo) {
		this.logInfo = logInfo;
	}

	public HashMap<String, LogAnalysis> getProjectAnalysis() {
		return this.projectAnalysis;
	}

	public void setProjectAnalysis(HashMap<String, LogAnalysis> projectAnalysis) {
		this.projectAnalysis = projectAnalysis;
	}

	public HashMap<String, LogAnalysis> getCollaboratorAnalysis() {
		return this.collaboratorAnalysis;
	}

	public void setCollaboratorAnalysis(HashMap<String, LogAnalysis> collaboratorAnalysis) {
		this.collaboratorAnalysis = collaboratorAnalysis;
	}

	public LogAnalysis getTotalAnalysis() {
		return this.totalAnalysis;
	}

	public void setTotalAnalysis(LogAnalysis totalAnalysis) {
		this.totalAnalysis = totalAnalysis;
	}
	
	public static void main(String[] args) {
		ReportCreator report = new ReportCreator();
		MainAnalysis mainAnalysis = new MainAnalysis(report);

		mainAnalysis.getGitLogTotalInfo();
		mainAnalysis.createTotalReport();
		
		mainAnalysis.getGitLogCollaboratorInfo();
		mainAnalysis.removeCollaboratorAnalysisWithoutIntegration();
		mainAnalysis.createCollaboratorReport();
    }
	
}
