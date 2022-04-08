package model;
import java.util.Date;

public class Commit {

	private String hash;
	private String parent;
	private Date date;
	private String message;

	public Commit(String hash) {
		this.hash = hash;
	}

	public boolean equals(Commit c) {
		boolean equals = false;
		if (this.hash.equals(c.getHash())) {
			equals = true;
		}
		return equals;
	}

	public String toString() {
		return "hash: " + this.hash + "; parent hash: " + this.parent;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
