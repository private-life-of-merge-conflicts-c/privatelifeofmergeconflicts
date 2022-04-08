# Git Analysis

This project was built for read git log files. For each file, this script will extract infomation from your git commands as: merge, rebase, cherry-pick, rebase and stash. The result is the creation of a file informing the frequency of each command and another file about the rebase actions.

# Set Up
Before anything, you must create a file named 'gitLogInfo.csv' that contains 3 columns:
 - Collaborator Name
 - Collaborator Project Name
 - Path of the folder that contains the git log file belonging to the same developer and project 
 
Example:
```sh
Maria;ProjectName1;C:\\Users\\Adm\\Documents\\Logs\\logs_maria
Pedro;ProjectName2;C:\\Users\\Adm\\Documents\\Logs\\logs_pedro
```

There is a file in the project named _'config.properties'_ that contains some setup properties needed to run the script correctly.

 - **logInfo**: copy the folder path that contains the entrance info for the script (the file you created in the previous step) 
 - **resultsFilePath**: copy the folder path to input the results
 
 # Run the script
Just run the 'MainAnalysis.java' class and check the results in the folder path that you set in 'resultsFilePath'.
