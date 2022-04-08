package model;

public class Rebase {

	private Commit parent1;

	private Commit parent2;

	private Commit oldParent1;

	private Commit base;

	public Rebase(Commit p1, Commit p2, Commit oldP1) {
		this.parent1 = p1;
		this.parent2 = p2;
		this.oldParent1 = oldP1;
	}

	public Commit getParent1() {
		return parent1;
	}

	public void setParent1(Commit parent1) {
		this.parent1 = parent1;
	}

	public Commit getParent2() {
		return parent2;
	}

	public void setParent2(Commit parent2) {
		this.parent2 = parent2;
	}

	public Commit getBase() {
		return base;
	}

	public void setBase(Commit base) {
		this.base = base;
	}

	public Commit getOldParent1() {
		return oldParent1;
	}

	public void setOldParent1(Commit oldParent1) {
		this.oldParent1 = oldParent1;
	}

}
