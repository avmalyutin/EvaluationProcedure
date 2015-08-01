package app.main;

import java.util.ArrayList;
import java.util.Random;

import app.files.UtilityClass;
import app.model.MainModel;
import app.model.NothingToEvaluateException;
import app.model.ServiceObject;

public class EvaluationProcedure {

	private double date;
	private MainModel modelToEvaluate;
	
	private ArrayList<ServiceObject> actualList;
	private ArrayList<ServiceObject> predictedList;
	
	
	public EvaluationProcedure(double date, MainModel model1) {
		this.date = date;
		this.modelToEvaluate = model1;
		this.actualList = new ArrayList<ServiceObject>();
		this.predictedList = new ArrayList<ServiceObject>();
	}
	
	
	public void addReadedItem(String servicename, String responseTime){
		ServiceObject obj = new ServiceObject(servicename, Integer.valueOf(responseTime));
		this.actualList.add(obj);
		this.predictedList.add(obj);
	}
	
	public void addReadedItem(String servicename, double responseTime){
		ServiceObject obj = new ServiceObject(servicename, (int)responseTime);
		this.actualList.add(obj);
		ServiceObject obj2 = (ServiceObject)UtilityClass.deepClone(obj);
		this.predictedList.add(obj2);
	}
	
	
	
	public void predictAndCalculate(int respTime, int costval){
		
		this.predictValues(respTime, costval);
		this.calculatevalues(respTime, costval);
	}
	
	public int proceedRandomEvaluation(){
		
		Random rand = new Random();
		int randomNumber = rand.nextInt(100);
		int randomService = randomNumber % actualList.size();
		String namePredicted = actualList.get(randomService).getServiceName();
		String nameActual = this.getTheLeastRankingActualServiceName();
		if(nameActual.equals(namePredicted))
			return 1;
		else
			return 0;
	}
	
	
	public int proceedSimpleEvaluation(){
		
		String namePredicted = this.getTheLeastRankingPredictedServiceName(); 
		String nameActual = this.getTheLeastRankingActualServiceName();
		if(nameActual.equals(namePredicted))
			return 1;
		else
			return 0;
	}
	
	public int processTopRangeEvaluation(int percentageRange){
		
		String nameActual = this.getTheLeastRankingActualServiceName();
		ArrayList<String> list = this.getTheListOfServicesInRange(percentageRange);
		for(String str:list){
			if(str.equals(nameActual))
				return 1;
		}
		return 0;
	}
	
	
	//predict values for predictedList
	public void predictValues(int respTime, int costval){
		
		try{
    		this.predictedList = modelToEvaluate.classifyOneInstance(this.date);
    		this.predictedList = ServiceObject.normalizeRespTimeInArray(this.predictedList);
    		this.predictedList = ServiceObject.normalizeCostInArray(this.predictedList);
    		this.predictedList = ServiceObject.computeOverallRanking(this.predictedList, respTime, costval);
    	}
    	catch(NothingToEvaluateException ex){
    		String serviceToChoose = "Model is not trained properly";
    		System.out.println(serviceToChoose);
    	}
	}
	
	public String getTheLeastRankingPredictedServiceName(){
		
		float min = this.predictedList.get(0).getOverAllRanking();
		String serviceName = this.predictedList.get(0).getServiceName();
		for(ServiceObject obj: this.predictedList){
			if(obj.getOverAllRanking() < min){
				min = obj.getOverAllRanking();
				serviceName = obj.getServiceName();
			}
		}
		return serviceName;
	}
	
	
	public ArrayList<String> getTheListOfServicesInRange(int percentage){
		
		ArrayList<String> listToReturn = new ArrayList<String>();
		
		float min = this.predictedList.get(0).getOverAllRanking();
		for(ServiceObject obj: this.predictedList){
			if(obj.getOverAllRanking() < min){
				min = obj.getOverAllRanking();
			}
		}
		
		float max = min * (100 + percentage) / 100;
		
		for(ServiceObject obj: this.predictedList){
			if(obj.getOverAllRanking() >= min && obj.getOverAllRanking() <= max){
				listToReturn.add(obj.getServiceName());
			}
		}
		return listToReturn;
	}
	
	
	//calculate values for actual
	public void calculatevalues(int respTime, int costval){
		
    	this.actualList = ServiceObject.normalizeRespTimeInArray(this.actualList);
    	this.actualList = ServiceObject.normalizeCostInArray(this.actualList);
    	this.actualList = ServiceObject.computeOverallRanking(this.actualList, respTime, costval);
    	
	}
	
	
	public String getTheLeastRankingActualServiceName(){
		
		float min = this.actualList.get(0).getOverAllRanking();
		String serviceName = this.actualList.get(0).getServiceName();
		for(ServiceObject obj: this.actualList){
			if(obj.getOverAllRanking() < min){
				min = obj.getOverAllRanking();
				serviceName = obj.getServiceName();
			}
		}
		return serviceName;
	}


	public double getDate() {
		return date;
	}


	public MainModel getModelToEvaluate() {
		return modelToEvaluate;
	}


	public ArrayList<ServiceObject> getActualList() {
		return actualList;
	}


	public ArrayList<ServiceObject> getPredictedList() {
		return predictedList;
	}
}
