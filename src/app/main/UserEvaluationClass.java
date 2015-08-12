package app.main;

import java.util.HashMap;
import java.util.Map;

public class UserEvaluationClass {
	
	
	public static int MAX_COUNTER = 200;
	private int counter;
	private Map<String, Integer> mapOfServices;
	
	public UserEvaluationClass() {
		this.counter = 0;
		this.mapOfServices = new HashMap<String, Integer>();
	}
	
	
	public void resetData(){
		this.counter = 0;
		this.mapOfServices = new HashMap<String, Integer>();
	}
	
	public void incrementCounter(){
		this.counter++;
	}
	
	public String getTheBestService() {
		String serviceToReturn = "";
		Integer minimum = Integer.valueOf(Integer.MAX_VALUE);
		for (Map.Entry<String, Integer> e : this.mapOfServices.entrySet()) {
			if (minimum.compareTo(e.getValue()) > 0) {
				serviceToReturn = e.getKey();
				minimum = e.getValue();
			}
		}

		return serviceToReturn;

	}
	
	
	
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public Map<String, Integer> getMapOfServices() {
		return mapOfServices;
	}
	public void setMapOfServices(Map<String, Integer> mapOfServices) {
		this.mapOfServices = mapOfServices;
	}
	
	
	
	
	

}
