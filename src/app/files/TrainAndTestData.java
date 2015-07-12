package app.files;


import java.util.ArrayList;

public class TrainAndTestData{
	
	public ArrayList<String> trainData;
	public ArrayList<String> testData;
	
	private ArrayList<String> listOfServers; 
	
	public TrainAndTestData(){
		this.trainData = new ArrayList<String>();
		this.testData = new ArrayList<String>();
		this.listOfServers = new ArrayList<String>();
	}
	
	public void addServer(String serverName){
		if(listOfServers.size() <=0 || !listOfServers.contains(serverName))
			listOfServers.add(serverName);
	}

	public ArrayList<String> getListOfServers() {
		return listOfServers;
	}
}