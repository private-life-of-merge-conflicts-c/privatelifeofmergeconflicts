package GitAnalysis;

import javax.xml.validation.Validator;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

public class LogAction {
	
	private String originalLine;
	private String gitHash;
	private Date date;
	private String command;
	private String message;
	
	public LogAction(String originalLine) {
		super();
		this.originalLine = originalLine;
		findGitHashFromLine();
		findCommandFromLine();
		findMessageFromLine();
		findDateFromLine();
	}
	
	protected void findGitHashFromLine() {
		String[] parts = this.originalLine.split(" ");
		setGitHash(parts[1]);
	}
	
	protected void findCommandFromLine() {
		String[] parts = this.originalLine.split("\\s\\-\\d+\\s");
		if (parts.length > 1) {
			String[] actionParts = parts[1].split(":");
			String command = actionParts[0];
			setCommand(command);
		}
	}
	
	protected void findDateFromLine() {
		String[] parts = this.originalLine.split("> ");
		String[] actionParts = parts[1].split(" ");
		String dateInfo = actionParts[0];
		Date date = Date.from(Instant.ofEpochSecond(Long.parseLong(dateInfo)));
		setDate(date);
	}
	
	protected void findMessageFromLine() {
		String[] parts = this.originalLine.split(": ");
		if (parts.length > 1) {
			String message = parts[1];
			setMessage(message);
		}
		else {
			setMessage("");
		}
	}
	
	public String getOriginalLine() {
		return originalLine;
	}
	
	public void setOriginalLine(String originalLine) {
		this.originalLine = originalLine;
	}
	
	public String getGitHash() {
		return gitHash;
	}
	
	public void setGitHash(String gitHash) {
		this.gitHash = gitHash;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getCommand() {
		return command;
	}
	
	public void setCommand(String command) {
		this.command = command;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}
