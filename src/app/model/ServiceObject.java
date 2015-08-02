package app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import app.main.MainClass;


public class ServiceObject implements Serializable{

	private static final long serialVersionUID = 790955032602057420L;
	
	private String serviceName;
	private int responceTims;
	private int operationCost;
	private float normalizedRespTime = -1;
	private float normalizedOpCost = -1;
	private float overAllRanking = -1;
	private float diffInPerc = -1;
	
	
	public ServiceObject(String serviceName, int responceTims) {
		super();
		this.serviceName = serviceName;
		this.responceTims = responceTims;
		
		//if(MainClass.operationCost.containsKey(this.serviceName)){
		//	this.operationCost = MainClass.operationCost.get(this.serviceName);
		//}
		//else{
			Random rand = new Random();
			this.operationCost= serviceName.length() + rand.nextInt(2);
		//}
	}
	
	
	public static String listToString(ArrayList<ServiceObject> list){
		String strToReturn = "";
		for(ServiceObject obj: list){
			strToReturn += (obj.getServiceName() + " : " + obj.getResponceTims() + " : "  
					+ obj.getNormalizedRespTime() + " : " + obj.getOperationCost() + " : " 
					+ obj.getNormalizedOpCost() + " : " + obj.getOverAllRanking() + "\n");
		}
		return strToReturn;
	}
	
	public static ArrayList<ServiceObject> normalizeRespTimeInArray(ArrayList<ServiceObject> list){
		
		float max = -100;
		float ratio;
		for(ServiceObject obj:list){
			if(obj.getResponceTims() > max){
				max = obj.getResponceTims();
			}
		}
		ratio = max / 100;
		
		for(ServiceObject obj:list){
			obj.setNormalizedRespTime(((float)obj.getResponceTims())/ratio);
		}
		return list;
	}
	
	public static ArrayList<ServiceObject> normalizeCostInArray(ArrayList<ServiceObject> list){
		
		float max = -100;
		float ratio;
		for(ServiceObject obj:list){
			if(obj.getOperationCost() > max){
				max = obj.getOperationCost();
			}
		}
		ratio = max / 100;
		
		for(ServiceObject obj:list){
			obj.setNormalizedOpCost(((float)obj.getOperationCost())/ratio);
		}
		return list;
	}
	
	
	public static ArrayList<ServiceObject> computeOverallRanking(ArrayList<ServiceObject> list, float respTime, float cost){
		
		for(ServiceObject obj:list){
			obj.setOverAllRanking((respTime/100)*obj.getNormalizedRespTime() + (cost/100)*obj.getNormalizedOpCost());
		}
		
		return list;
	}
	
	public static String getWinnerServiceName(ArrayList<ServiceObject> list){
		String buffer = "";
		float rating = list.get(0).getOverAllRanking();
		for(ServiceObject obj: list){
			
			float bufRating = obj.getOverAllRanking();
			if(rating >= bufRating){
				buffer = obj.getServiceName();
				rating = bufRating;
			}
		}
		return buffer;
	}
	
	public static ArrayList<ServiceObject> computePercDiff(ArrayList<ServiceObject> list){
		
		float minRating = list.get(0).getOverAllRanking();
		for(ServiceObject obj: list){
			float bufRating = obj.getOverAllRanking();
			if(minRating >= bufRating){
				minRating = bufRating;
			}
		}
		
		for(ServiceObject obj: list){
			float bufRating = obj.getOverAllRanking();
			float ratingToWrite = (bufRating - minRating) / 100;
			obj.setDiffInPerc(ratingToWrite);
		}
		
		return list;
	}
	
	
	public static ServiceObject returnServiceObjectByServiceName(ArrayList<ServiceObject> list, String serviceName){
		
		for(ServiceObject obj:list){
			if(obj.getServiceName().equals(serviceName))
				return obj;
		}
		return null;
		
		
	}
	
	
	
	public String getServiceName() {
		return serviceName;
	}
	
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public int getResponceTims() {
		return responceTims;
	}
	
	public void setResponceTims(int responceTims) {
		this.responceTims = responceTims;
	}


	public float getNormalizedRespTime() {
		return normalizedRespTime;
	}


	public void setNormalizedRespTime(float normalizedRespTime) {
		this.normalizedRespTime = normalizedRespTime;
	}


	public int getOperationCost() {
		return operationCost;
	}


	public void setOperationCost(int operationCost) {
		this.operationCost = operationCost;
	}


	public float getNormalizedOpCost() {
		return normalizedOpCost;
	}


	public void setNormalizedOpCost(float normalizedOpCost) {
		this.normalizedOpCost = normalizedOpCost;
	}


	public float getOverAllRanking() {
		return overAllRanking;
	}


	public void setOverAllRanking(float overAllRanking) {
		this.overAllRanking = overAllRanking;
	}


	public float getDiffInPerc() {
		return diffInPerc;
	}


	public void setDiffInPerc(float diffInPerc) {
		this.diffInPerc = diffInPerc;
	}

}
