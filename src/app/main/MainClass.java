package app.main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import weka.core.Instances;
import app.files.TrainAndTestData;
import app.files.UtilityClass;
import app.model.MainModel;



public class MainClass {

	public static HashMap<String, Integer> operationCost = new HashMap<String, Integer>();
	
	
	public static void main(String [] args) throws Exception{
		
		
		operationCost = UtilityClass.extractCostFuncFromCVS("D://Experiment//operationCost.csv");
		
		File folder = new File("D://Experiment//Source");
		
		ArrayList<GroupAccuracy> listOfAccuracy = new ArrayList<GroupAccuracy>();
		
		File [] files = folder.listFiles();
		
		for(int i=0; i<files.length; i++){
			
			TrainAndTestData dataObject = UtilityClass.extractTrainAndTestSetFromCVS(files[i].getAbsolutePath());
			
			String trainFile = "D://Experiment//SplittedData//" + files[i].getName()+ ".train.arff";
			String testFile = "D://Experiment//SplittedData//" + files[i].getName()+ ".test.arff";
			
			UtilityClass.writeToARFFFile(trainFile, dataObject.trainData);
			UtilityClass.writeToARFFFile(testFile, dataObject.testData);
			
			MainModel model = new MainModel();
			Instances train = UtilityClass.readInstancesFromFile(trainFile);
			model.trainFromInstances(train);
			
			EvaluationController controller = new EvaluationController(model);
        	controller.evaluateData(new File(testFile), 75, 25, 15);
			
        	listOfAccuracy.add(controller.getGroupAccuracy());
			
        	String evaluationPath = "D://Experiment//Evaluation//"+files[i].getName()+".EVAL.xls";
        	controller.writeEvaluationResults(new File(evaluationPath));
			
		}
		
		
		
		GroupAccuracy.writeGroupArray(new File("D://Experiment//Evaluation//final.xls"), listOfAccuracy);
		
		
		System.out.println("Done!");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
