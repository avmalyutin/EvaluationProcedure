package app.main;

public class EvaluationData {
	
	private int attepmtNumber;
	private int rigthAttemps;
	private int totalAttempts;
	private float percentage;
	
	public EvaluationData(int attepmtNumber, int rigthAttemps, int totalAttempts, float percentage1) {
		super();
		this.attepmtNumber = attepmtNumber;
		this.rigthAttemps = rigthAttemps;
		this.totalAttempts = totalAttempts;
		this.percentage = percentage1;
	}


	public int getAttepmtNumber() {
		return attepmtNumber;
	}


	public void setAttepmtNumber(int attepmtNumber) {
		this.attepmtNumber = attepmtNumber;
	}


	public int getRigthAttemps() {
		return rigthAttemps;
	}


	public void setRigthAttemps(int rigthAttemps) {
		this.rigthAttemps = rigthAttemps;
	}


	public int getTotalAttempts() {
		return totalAttempts;
	}


	public void setTotalAttempts(int totalAttempts) {
		this.totalAttempts = totalAttempts;
	}


	public float getPercentage() {
		return percentage;
	}


	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}
	
	
	
	
	
	

}
