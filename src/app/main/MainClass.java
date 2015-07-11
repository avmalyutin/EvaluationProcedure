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
		
		
		operationCost = UtilityClass.extractCostFuncFromCVS("E://SwedenData//NewLife//opCost//operationCost.csv");
		
		File folder = new File("E://SwedenData//NewLife//dataset//TYPE_1");
		
		ArrayList<GroupAccuracy> listOfAccuracy = new ArrayList<GroupAccuracy>();
		
		File [] files = folder.listFiles();
		
		for(int i=0; i<files.length; i++){
			
			File fileToExtract = new File(files[i].getAbsolutePath());
			
			File [] insideFolder = files[i].listFiles();
			for(int j=0; i<insideFolder.length; j++){
				if(insideFolder[j].getName().contains("generated_profiled_") && 
						insideFolder[j].getName().contains(".csv")){
					fileToExtract = insideFolder[j];
					break;
				}
			}
			
			
			TrainAndTestData dataObject = UtilityClass.extractTrainAndTestSetFromCVS(fileToExtract.getAbsolutePath());
			
			String trainFile = files[i].getAbsolutePath() + "//" + fileToExtract.getName() + ".train.arff";
			String testFile = files[i].getAbsolutePath() + "//" + fileToExtract.getName() + ".test.arff";
			
			UtilityClass.writeToARFFFile(trainFile, dataObject.trainData);
			UtilityClass.writeToARFFFile(testFile, dataObject.testData);
			
			MainModel model = new MainModel();
			Instances train = UtilityClass.readInstancesFromFile(trainFile);
			model.trainFromInstances(train);
			
			EvaluationController controller = new EvaluationController(model);
        	controller.evaluateData(new File(testFile), 75, 25, 15);
			
        	listOfAccuracy.add(controller.getGroupAccuracy());
			
        	String evaluationPath = files[i].getAbsolutePath() + "//" + fileToExtract.getName() + ".EVAL.xls";
        	controller.writeEvaluationResults(new File(evaluationPath));
			
		}
		
		
		
		GroupAccuracy.writeGroupArray(new File("E://SwedenData//NewLife//dataset//final.xls"), listOfAccuracy);
		
		
		System.out.println("Done!");
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
