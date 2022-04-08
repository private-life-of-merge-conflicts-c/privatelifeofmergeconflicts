package GitAnalysis;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class ReportCreator {
	
	private String resultsFilePath;
	
	public void createFile(String content, String folderName, String fileName) {
		 try{
			 // Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
            // If folder doesn't exists, then create it
	        if (!folder.exists()) {
	        	folder.mkdir();
	        }
	        
            // Create new file
            String resultFilePath = resultFolderPath +  File.separator  + fileName + "-Report.txt";
            File file = new File(resultFilePath);
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            // Write in file
            bw.write(content);

            // Close connection
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
	}
	
	public void createCSVFile(String folderName, String fileName, HashMap<String, LogAnalysis> map) {
		 try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
            // If folder doesn't exists, then create it
	        if (!folder.exists()) {
	        	folder.mkdir();
	        }
	        
            // Create new file
            String resultFilePath = resultFolderPath +  File.separator  + fileName +".csv";
            File file = new File(resultFilePath);
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            	            
            String content = "Name;"
            		+ "Logs;"
					+ "Total Cherry-Pick;"
					+ "Cherry-Pick;"
					+ "Cherry-Pick-Failed;"
            		+ "Squash;"
					+ "Squash-Failed;"
					+ "Squash-MergeMissing;"
            		+ "Merge;"
					+ "Merge-Failed;"
            		+ "Rebase;"
					+ "\n";
            
            for (Entry<String, LogAnalysis> entry : map.entrySet()) {
    		    String keyName = entry.getKey();
    		    LogAnalysis log = entry.getValue();

    		    int totalCherryPick = log.getCherryPickList().size() + log.getFailedCherryPickList().size();
    		    
    		    content = content.concat(keyName +
    		    		";" + log.getLogs().size() +

						";" + totalCherryPick +
	            		";" + log.getCherryPickList().size() +
	            		";" + log.getFailedCherryPickList().size() + 
	            		
	            		";" + log.getSquashList().size() +
						";" + log.calculateFailedSquashes() +
						";" + log.getMissingMergeCommitList().size()+

	            		";" + log.getMergeList().size() +
        				";" + log.getFailedMergeList().size()) +

						";" + log.getRebases().size()  + "\n" ;
    		}
            
            // Write in file
            bw.write(content);

            // Close connection
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
	}

	public void createHIFrequencyCSVFile(String folderName, String fileName, HashMap<String, LogAnalysis> map) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
			// If folder doesn't exists, then create it
			if (!folder.exists()) {
				folder.mkdir();
			}

			// Create new file
			String resultFilePath = resultFolderPath +  File.separator  + fileName +".csv";
			File file = new File(resultFilePath);
			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			String content = "Name;"
					+ "HI/TI \n";

			for (Entry<String, LogAnalysis> entry : map.entrySet()) {
				String keyName = entry.getKey();
				LogAnalysis log = entry.getValue();

				content = content.concat(keyName +
						";" + log.calculateHIFrequency() + "\n");

			}

			// Write in file
			bw.write(content);

			// Close connection
			bw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public void createIntegrationTotalCSVFile(String folderName, String fileName, HashMap<String, LogAnalysis> map) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
			// If folder doesn't exists, then create it
			if (!folder.exists()) {
				folder.mkdir();
			}

			// Create new file
			String resultFilePath = resultFolderPath +  File.separator  + fileName +".csv";
			File file = new File(resultFilePath);
			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			String content = "Name;"
					+ "Hidden Integration;"
					+ "Merge Integration;"
					+ "Did any integration ? \n";


			for (Entry<String, LogAnalysis> entry : map.entrySet()) {
				String keyName = entry.getKey();
				LogAnalysis log = entry.getValue();

				content = content.concat(keyName +
						";" + log.calculateHiddenIntegrations() +
						";" + log.calculateTotalMerge() +
						";" + log.haveIntegration() + "\n");

			}

			// Write in file
			bw.write(content);

			// Close connection
			bw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public void createIntegrationTotalAnalysisCSVFile(String folderName, String fileName, HashMap<String, LogAnalysis> map) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
			// If folder doesn't exists, then create it
			if (!folder.exists()) {
				folder.mkdir();
			}

			// Create new file
			String resultFilePath = resultFolderPath +  File.separator  + fileName +".csv";
			File file = new File(resultFilePath);
			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			String content = "Name;"
					+ "Hidden Integration;"
					+ "Total Rebase;"
					+ "Total Cherry-Pick;"
					+ "Merge Integration;"
					+ "HI / TI \n";


			for (Entry<String, LogAnalysis> entry : map.entrySet()) {
				String keyName = entry.getKey();
				LogAnalysis log = entry.getValue();

				content = content.concat(keyName +
						";" + log.calculateHiddenIntegrations() +
						";" + log.getRebases().size() +
						";" + log.calculateTotalCherryPick() +
						";" + log.calculateTotalMerge() +
						";" + log.calculateHIFrequency() + "\n");

			}

			// Write in file
			bw.write(content);

			// Close connection
			bw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public void createIntegrationTotalAnalysisMergeCSVFile(String folderName, String fileName, HashMap<String, LogAnalysis> map) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
			// If folder doesn't exists, then create it
			if (!folder.exists()) {
				folder.mkdir();
			}

			// Create new file
			String resultFilePath = resultFolderPath +  File.separator  + fileName +".csv";
			File file = new File(resultFilePath);
			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			String content = "Name;"
					+ "Hidden Integration;"
					+ "Total Rebase;"
					+ "Total Cherry-Pick;"
					+ "Merge Integration;"
					+ "Merge / TI \n";


			for (Entry<String, LogAnalysis> entry : map.entrySet()) {
				String keyName = entry.getKey();
				LogAnalysis log = entry.getValue();

				content = content.concat(keyName +
						";" + log.calculateHiddenIntegrations() +
						";" + log.getRebases().size() +
						";" + log.calculateTotalCherryPick() +
						";" + log.calculateTotalMerge() +
						";" + log.calculateMergeFrequency() + "\n");

			}

			// Write in file
			bw.write(content);

			// Close connection
			bw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public void createMergeFrequencyCSVFile(String folderName, String fileName, HashMap<String, LogAnalysis> map) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
			// If folder doesn't exists, then create it
			if (!folder.exists()) {
				folder.mkdir();
			}

			// Create new file
			String resultFilePath = resultFolderPath +  File.separator  + fileName +".csv";
			File file = new File(resultFilePath);
			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			String content = "Name;"
					+ "MERGE/TI \n";

			for (Entry<String, LogAnalysis> entry : map.entrySet()) {
				String keyName = entry.getKey();
				LogAnalysis log = entry.getValue();

				content = content.concat(keyName +
						";" + log.calculateMergeFrequency() + "\n");

			}

			// Write in file
			bw.write(content);

			// Close connection
			bw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public void createFailedMergeFrequencyCSVFile(String folderName, String fileName, HashMap<String, LogAnalysis> map) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
			// If folder doesn't exists, then create it
			if (!folder.exists()) {
				folder.mkdir();
			}

			// Create new file
			String resultFilePath = resultFolderPath +  File.separator  + fileName +".csv";
			File file = new File(resultFilePath);
			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			String content = "Name;"
					+ "Failed Merge / Total Merge \n";

			for (Entry<String, LogAnalysis> entry : map.entrySet()) {
				String keyName = entry.getKey();
				LogAnalysis log = entry.getValue();

				content = content.concat(keyName +
						";" + log.calculateFailedMergeFrequency() + "\n");

			}

			// Write in file
			bw.write(content);

			// Close connection
			bw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public void createFailedMergeFrequencyRestrictedGroupCSVFile(String folderName, String fileName, HashMap<String, LogAnalysis> map) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
			// If folder doesn't exists, then create it
			if (!folder.exists()) {
				folder.mkdir();
			}

			// Create new file
			String resultFilePath = resultFolderPath +  File.separator  + fileName +".csv";
			File file = new File(resultFilePath);
			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			String content = "Name;"
					+ "Failed Merge / Total Merge \n";

			for (Entry<String, LogAnalysis> entry : map.entrySet()) {
				String keyName = entry.getKey();
				LogAnalysis log = entry.getValue();

				if(log.haveIntegration()) {
					content = content.concat(keyName +
							";" + log.calculateFailedMergeFrequency() + "\n");
				}

			}

			// Write in file
			bw.write(content);

			// Close connection
			bw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}

	public void createCSVFile(String folderName, String fileName, LogAnalysis log) {
		 try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
            // If folder doesn't exists, then create it
	        if (!folder.exists()) {
	        	folder.mkdir();
	        }
	        
            // Create new file
            String resultFilePath = resultFolderPath +  File.separator  + fileName +".csv";
            File file = new File(resultFilePath);
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            	            		    
            String content = "Name;"
            		+ "Initial Date;Final Date;Period of Days;"
            		+ "Logs;"
            		+ "Cherry-Pick;"
					+ "Cherry-Pick-Failed;"
//            		+ "Squash;"
            		+ "Merge;"
					+ "Merge-Failed;"
//            		+ "Merge-Missing;"
            		+ "Rebase;"
					+ "Rebase-Failed;"
//					+ "Rebase-Failed;"
//					+ "Rebase-Aborted;"
//					+ "Rebase-IE"
					+ "\n";
            
		    content = content.concat("Total" +
		    		";" + formatDate(log.getInitialDate()) +
		    		";" + formatDate(log.getFinalDate()) +
		    		";" + log.getPeriod() + 
		    		";" + log.getLogs().size() + 
		    		
            		";" + log.getCherryPickList().size() +
            		";" + log.getFailedCherryPickList().size() + 
            		
//            		";" + log.getSquashList().size() +
            		
            		";" + log.getMergeList().size() +
            		";" + log.getFailedMergeList().size());		
		    
//            		if (log.getMissingMergeCommitList().size() > 0) {
//            			content = content.concat(
//            				";" + log.getMissingMergeCommitList().size());
//            		}
		    
        		content = content.concat(
	            		";" + log.getRebases().size() +
						";" + log.calculateFailedRebases() +
//	            		";" + log.getTotalFailedRebase() +
//	            		";" + log.getAbortedRebaseList().size() +
//	            		";" + log.getTotalIntegrationErrors() +
								"\n");
            
            // Write in file
            bw.write(content);

            // Close connection
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
	}

	public void createTotalHTMLTable(String folderName, String fileName, LogAnalysis logAnalysis) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
			// If folder doesn't exists, then create it
			if (!folder.exists()) {
				folder.mkdir();
			}

			// Create new file
			String resultFilePath = resultFolderPath +  File.separator  + fileName +"-Table.html";
			File file = new File(resultFilePath);
			// If file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			String content = "<html>";

			content = content.concat("<head><style>"
					+ "table, th, td {"
					+ "border: 1px solid black;"
					+ "border-collapse: collapse;"
					+ "}"
					+ "th, td {"
					+ "padding: 10px;"
					+ "}"
					+ "</style></head>");

			content = content.concat("<body><table>"
					+ "<tr>"
					+ "<th>Name</th>"
					+ "<th>N. Log Files</th>"
//					+ "<th>Initial Date</th>"
//					+ "<th>Final Date</th>"
//					+ "<th>Period of Days</th>"
					+ "<th>Line Logs</th>"
					+ "<th>Total Action</th>"
					+ "<th>Total Merge </th>"
					+ "<th>Successful Merge</th>"
					+ "<th>Failed Merge &darr; </th>"
//					+ "<th>Total Commit Amend </th>"
					+ "<th>Total Cherry-Pick </th>"
					+ "<th>Successful Cherry-Pick &uarr; </th>"
					+ "<th>Failed Cherry-Pick &darr; </th>"
					+ "<th>Total Rebase </th>"
					+ "<th>Successful Rebase &uarr;</th>"
					+ "<th>Failed Rebase &darr;</th>"
					+ "<th>Total Squash </th>"
					+ "<th>Successful Squash &uarr;</th>"
					+ "<th>Failed Squash &darr;</th>"
					+ "</tr>");

			int failedRebase = logAnalysis.calculateFailedRebases();
			int successfulRebase = logAnalysis.getRebases().size() - failedRebase;

			int failedSquash = logAnalysis.calculateFailedSquashes();
			int successfulSquash = logAnalysis.getSquashList().size() - failedSquash;

			int totalMerge = logAnalysis.getMergeList().size() + logAnalysis.getFailedMergeList().size();
			int totalCherryPick = logAnalysis.getCherryPickList().size() + logAnalysis.getFailedCherryPickList().size();
			int totalHI = logAnalysis.calculateHiddenIntegrations();

			content = content.concat("<tr>"
					+ "<td>" + "TOTAL" + "</td>"
					+ "<td>" + logAnalysis.getnLogFiles() + "</td>"
//					+ "<td>" + formatDate(logAnalysis.getInitialDate()) + "</td>"
//					+ "<td>" + formatDate(logAnalysis.getFinalDate()) + "</td>"
//					+ "<td>" + logAnalysis.getPeriod() + "</td>"
					+ "<td>" + logAnalysis.getLogs().size() + "</td>"
					+ "<td>" + logAnalysis.calculateTotalLogAction() + "</td>"
					+ "<td>" + totalMerge + "</td>"
					+ "<td>" + logAnalysis.getMergeList().size() + "</td>"
					+ "<td>" + logAnalysis.getFailedMergeList().size() + "</td>"
//					+ "<td>" + logAnalysis.getCommitAmendList().size() + "</td>"
					+ "<td>" + totalCherryPick + "</td>"
					+ "<td>" + logAnalysis.getCherryPickList().size() + "</td>"
					+ "<td>" + logAnalysis.getFailedCherryPickList().size() + "</td>"
					+ "<td>" + logAnalysis.getRebases().size() + "</td>"
					+ "<td>" + successfulRebase + "</td>"
					+ "<td>" + failedRebase + "</td>"
					+ "<td>" + logAnalysis.getSquashList().size() + "</td>"
					+ "<td>" + successfulSquash + "</td>"
					+ "<td>" + failedSquash + "</td>"
					+ "</tr>");

			content = content.concat("<tr>"
					+ "<td>" + "TOTAL" + "</td>"
					+ "<td>" + logAnalysis.getnLogFiles() + "</td>"
					+ "<td>" + logAnalysis.getLogs().size() + "</td>"
					+ "<td>" + logAnalysis.calculateTotalLogAction() + "</td>"
					+ "<td>" + logAnalysis.calculatePercentage(totalMerge, logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(logAnalysis.getMergeList().size(), logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(logAnalysis.getFailedMergeList().size(), logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(totalCherryPick, logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(logAnalysis.getCherryPickList().size(), logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(logAnalysis.getFailedCherryPickList().size(), logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(logAnalysis.getRebases().size(), logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(successfulRebase, logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(failedRebase, logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(logAnalysis.getSquashList().size(), logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(successfulSquash, logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(failedSquash, logAnalysis.calculateTotalLogAction()) + "% </td>"
					+ "</tr>");

			content = content.concat("<tr>"
					+ "<td>" + "TOTAL" + "</td>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + logAnalysis.calculatePercentage(totalCherryPick, totalHI) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(logAnalysis.getCherryPickList().size(), totalHI) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(logAnalysis.getFailedCherryPickList().size(), totalHI) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(logAnalysis.getRebases().size(), totalHI) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(successfulRebase, totalHI) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(failedRebase, totalHI) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(logAnalysis.getSquashList().size(), totalHI) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(successfulSquash, totalHI) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(failedSquash, totalHI) + "% </td>"
					+ "</tr>");

			content = content.concat("</table></body></html>");

			// Write in file
			bw.write(content);

			// Close connection
			bw.close();
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void createRebaseCSVFile(String folderName, String fileName, ArrayList<Rebase> rebases) {
		 try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
            // If folder doesn't exists, then create it
	        if (!folder.exists()) {
	        	folder.mkdir();
	        }
	        
            // Create new file
            String resultFilePath = resultFolderPath +  File.separator  + fileName + "-Rebase.csv";
            File file = new File(resultFilePath);
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            int index = 1;
            String content = "Rebase;"
            		+ "Total-Commits;"
            		+ "Total-Rebase-Failed;"
            		+ "Total-Rebase-Skipped;"
            		+ "Rebase-Successfull\n";
            
            for(Rebase rebase : rebases) {
            	content = content.concat(index +
            			";" + rebase.getTotalCommits() +
            			";" + rebase.getTotalFailedRebases() +
            			";" + rebase.getTotalSkippedRebases() +
            			";" + rebase.getRebaseSuccessfull() + "\n");
            	
            	index++;
    		}
            
            // Write in file
            bw.write(content);

            // Close connection
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
	}
	
	public void createHTMLTable(String folderName, String fileName, HashMap<String, LogAnalysis> map) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
            // If folder doesn't exists, then create it
	        if (!folder.exists()) {
	        	folder.mkdir();
	        }
	        
            // Create new file
            String resultFilePath = resultFolderPath +  File.separator  + fileName +"-Table.html";
            File file = new File(resultFilePath);
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            String content = "<html>";
            
            content = content.concat("<head><style>"
            		+ "table, th, td {"
            		+ "border: 1px solid black;"
            		+ "border-collapse: collapse;"
            		+ "}"
            		+ "th, td {"
            		+ "padding: 10px;"
            		+ "}"
            		+ "</style></head>");
            
            content = content.concat("<body><table>"
            		+ "<tr>"
            		+ "<th>Name</th>"
					+ "<th>N. Log Files</th>"
            		+ "<th>Period of Days</th>"
            		+ "<th>Line Logs</th>"
            		+ "<th>Total Action</th>"
            		+ "<th>Successful Merge</th>"
            		+ "<th>Failed Merge &darr; </th>"
            		+ "<th>Successful Cherry-Pick &uarr; </th>"
            		+ "<th>Failed Cherry-Pick &darr; </th>"
            		+ "<th>Successful Rebase &uarr;</th>"
            		+ "<th>Failed Rebase &darr;</th>"
            		+ "</tr>");
            	                        
            for (Entry<String, LogAnalysis> entry : map.entrySet()) {
    		    String keyName = entry.getKey();
    		    LogAnalysis log = entry.getValue();
    		    
    		    int failedRebase = log.calculateFailedRebases();
    		    int successfulRebase = log.getRebases().size() - failedRebase;
    		    
    		    content = content.concat("<tr>"
    		    		+ "<td>" + keyName + "</td>"
						+ "<td>" + log.getnLogFiles() + "</td>"
    		    		+ "<td>" + log.getPeriod() + "</td>"
    		    		+ "<td>" + log.getLogs().size() + "</td>" 
    	    		    + "<td>" + log.calculateTotalLogAction() + "</td>"
    	    		    + "<td>" + log.getMergeList().size() + "</td>"
    	    		    + "<td>" + log.getFailedMergeList().size() + "</td>"
    		    		+ "<td>" + log.getCherryPickList().size() + "</td>"
    		    		+ "<td>" + log.getFailedCherryPickList().size() + "</td>"
    		    		+ "<td>" + successfulRebase + "</td>"
    		    		+ "<td>" + failedRebase + "</td>"
    		    		+ "</tr>");
    		        		    
    		}
            
            content = content.concat("</table></body></html>");
            
            // Write in file
            bw.write(content);

            // Close connection
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
	}
	
	public void createRebaseHTMLTable(String folderName, String fileName, HashMap<String, LogAnalysis> map) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
            // If folder doesn't exists, then create it
	        if (!folder.exists()) {
	        	folder.mkdir();
	        }
	        
            // Create new file
            String resultFilePath = resultFolderPath +  File.separator  + fileName +"-Rebase.html";
            File file = new File(resultFilePath);
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            String content = "<html>";
            
            content = content.concat("<head><style>"
            		+ "table, th, td {"
            		+ "border: 1px solid black;"
            		+ "border-collapse: collapse;"
            		+ "}"
            		+ "th, td {"
            		+ "padding: 10px;"
            		+ "}"
            		+ "</style></head>");
            
            content = content.concat("<body><table>"
            		+ "<tr>"
            		+ "<th>Name</th>"
            		+ "<th>Initial Date</th>"
            		+ "<th>Final Date</th>"
            		+ "<th>Period of Days</th>"
            		+ "<th>Line Logs</th>"
            		+ "<th>Total Action</th>"
            		+ "<th>Total Rebase</th>"
            		+ "<th>Failed Rebase  &darr; </th>"
            		+ "<th>Skipped Rebase  &darr; </th>"
            		+ "<th>Aborted Rebase</th>"
            		+ "</tr>");
            	                        
            for (Entry<String, LogAnalysis> entry : map.entrySet()) {
    		    String keyName = entry.getKey();
    		    LogAnalysis log = entry.getValue();
    		    
    		    content = content.concat("<tr>"
    		    		+ "<td>" + keyName + "</td>"
    		    		+ "<td>" + formatDate(log.getInitialDate()) + "</td>"
    		    		+ "<td>" + formatDate(log.getFinalDate()) + "</td>"
    		    		+ "<td>" + log.getPeriod() + "</td>"
    		    		+ "<td>" + log.getLogs().size() + "</td>" 
    	    		    + "<td>" + log.calculateTotalLogAction() + "</td>"
    	    		    + "<td>" + log.getRebases().size() + "</td>"
    		    		+ "<td>" + log.getTotalFailedRebase() + "</td>"
    		    		+ "<td>" + log.getSkippedRebaseList().size() + "</td>"
    	    		    + "<td>" + log.getAbortedRebaseList().size() + "</td>" 
    		    		+ "</tr>");
    		        		    
    		}
            
            content = content.concat("</table></body></html>");
            
            // Write in file
            bw.write(content);

            // Close connection
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
	}
	
	public void createResumeReportHTML(String folderName, String fileName, LogAnalysis logAnalysis,
									   int totalLogsWithIntegration, int totalLogsWithoutIntegration) {
		try{
			// Create new folder
			String resultFolderPath = resultsFilePath + File.separator + folderName;
			File folder = new File(resultFolderPath);
            // If folder doesn't exists, then create it
	        if (!folder.exists()) {
	        	folder.mkdir();
	        }
	        
            // Create new file
            String resultFilePath = resultFolderPath +  File.separator  + fileName +"-ResumeReport.html";
            File file = new File(resultFilePath);
            // If file doesn't exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            
            String content = "<html>";
            
            content = content.concat("<head><style>"
            		+ "table, th, td {"
            		+ "border: 1px solid black;"
            		+ "border-collapse: collapse;"
            		+ "}"
            		+ "th, td {"
            		+ "padding: 10px;"
            		+ "}"
            		+ "</style></head>");
            
            content = content.concat("<body><table>"
            		+ "<tr>"
					+ "<th>Logs With Integration</th>"
					+ "<th>Logs Without Integration</th>"
					+ "<th>Line Logs</th>"
            		+ "<th>Total Action</th>"
            		+ "<th>Total Merge  &darr; </th>"
            		+ "<th>Failed Merge  &darr; </th>"
            		+ "<th>Total Hidden-Integrations  &darr; </th>"
            		+ "<th>Failed Hidden-Integrations  &darr; </th>"
            		+ "</tr>");
            
            int logLines = logAnalysis.getLogs().size();
            int totalAction = logAnalysis.calculateTotalLogAction();
            int totalMerge = logAnalysis.getMergeList().size() + logAnalysis.getFailedMergeList().size();
            int totalFailedMerge = logAnalysis.getFailedMergeList().size();
            int totalHI = logAnalysis.calculateHiddenIntegrations();
            int totalFailedHI = logAnalysis.calculateHiddenIntegrationErrors();
			int totalIntegration = logAnalysis.calculateTotalIntegration();
      
		    content = content.concat("<tr>"
		    		+ "<td>" + totalLogsWithIntegration + "</td>"
					+ "<td>" + totalLogsWithoutIntegration + "</td>"
					+ "<td>" + logLines + "</td>"
		    		+ "<td>" + totalAction + "</td>"
		    		+ "<td>" + totalMerge + "</td>"
		    		+ "<td>" + totalFailedMerge + "</td>"
		    		+ "<td>" + totalHI + "</td>"
		    		+ "<td>" + totalFailedHI + "</td>"
		    		+ "</tr>");

			content = content.concat("<tr>"
					+ "<td>" + totalLogsWithIntegration + "</td>"
					+ "<td>" + totalLogsWithoutIntegration + "</td>"
					+ "<td>" + logLines + "</td>"
					+ "<td>" + totalAction + "</td>"
					+ "<td>" + logAnalysis.calculatePercentage(totalMerge, totalAction) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(totalFailedMerge, totalAction)+ "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(totalHI, totalAction) + "% </td>"
					+ "<td>" + logAnalysis.calculatePercentage(totalFailedHI, totalAction) + "% </td>"
					+ "</tr>");

			content = content.concat("<tr>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + logAnalysis.calculatePercentage(totalMerge, totalIntegration) + "% </td>"
					+ "<td>" + "-" + "</td>"
					+ "<td>" + logAnalysis.calculatePercentage(totalHI, totalIntegration) + "% </td>"
					+ "<td>" + "-" + "</td>"
					+ "</tr>");

            content = content.concat("</table></body></html>");
            
            // Write in file
            bw.write(content);

            // Close connection
            bw.close();
        }
        catch(Exception e){
            System.out.println(e);
        }
	}
	
	public int getQtdRebaseFailed(ArrayList<Rebase> rebases) {
		int qtdRebaseFailed = 0;
		
		for(Rebase rebase : rebases) {
			if(rebase.getTotalFailedRebases() > 0) {
				qtdRebaseFailed++;
			}
		}
		
		return qtdRebaseFailed;
	}

	public int getQtdRebaseAborted(ArrayList<Rebase> rebases) {
		int qtdRebaseAborted = 0;
		
		for(Rebase rebase : rebases) {
			if(!rebase.getRebaseSuccessfull()) {
				qtdRebaseAborted++;
			}
		}
		
		return qtdRebaseAborted;
	}
	
	public String formatDate(Date date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

		return dateFormat.format(date); 
	}

	public String getResultPath() {
		return resultsFilePath;
	}

	public void setResultPath(String resultPath) {
		this.resultsFilePath = resultPath;
	}

}
