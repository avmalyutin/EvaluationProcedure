package app.model;

import java.util.HashMap;
import java.util.Map;

public class LocationModule {
	
	static Map <String, Integer> locationMatrix;
	static String swedCountry = "Sweden";
	static String gerCountry = "Germany";
	static String usaCountry = "USA";
	static String argCountry = "Argentina";


	static{
		locationMatrix = new HashMap<String, Integer>();
		locationMatrix.put(gerCountry + "-" + gerCountry, 0);
		locationMatrix.put(gerCountry + "-" + swedCountry, 4);
		locationMatrix.put(gerCountry + "-" + usaCountry, 34);
		locationMatrix.put(gerCountry + "-" + argCountry, 60);
		locationMatrix.put(swedCountry + "-" + swedCountry, 0);
		locationMatrix.put(swedCountry + "-" + usaCountry, 33);
		locationMatrix.put(swedCountry + "-" + argCountry, 63);
		locationMatrix.put(usaCountry + "-" + usaCountry, 0);
		locationMatrix.put(usaCountry + "-" + argCountry, 42);
		locationMatrix.put(argCountry + "-" + argCountry, 0);
	}
	
	
	public static String getTheCountryByIndex(int index){
		
		switch(index){
			case 0:
				return swedCountry;
			case 1:
				return gerCountry;
			case 2:
				return usaCountry;
			case 3:
				return argCountry;
			default:
				return "error";
		}
	}
	
	
	public static int getDistanceByTwoPlaces(String place1, String place2){
		
		if(place1.equals(place2)){
			return locationMatrix.get(place1 + "-" + place2);
		}
		
		for (Map.Entry<String, Integer> entry : locationMatrix.entrySet()){
			String key = entry.getKey();
			if(key.contains(place1) && key.contains(place2)){
				return entry.getValue();
			}
		}
		return -1;
	}
	
}
