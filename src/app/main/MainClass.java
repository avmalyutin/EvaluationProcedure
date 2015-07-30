package app.main;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import weka.core.Instance;
import weka.core.Instances;
import app.files.DeleteScript;
import app.files.TrainAndTestData;
import app.files.UtilityClass;
import app.model.MainModel;

public class MainClass {
	
	

	//paths
	public static final String ROOT_PATH_PREF = "E://SwedenData//NewLife//dataset//1. NORMAL_DERIVATION";
	private static final int NUMBER_ARRAYS = 30;
	private static final String OP_COST_FILE = "E://SwedenData//NewLife//opCost//operationCost.csv";
	
	//other stuff
	public static HashMap<String, Integer> operationCost = new HashMap<String, Integer>();
	
	
	public static void main(String [] args) throws Exception{
		
		DeleteScript.deleteEvaluationResults(MainClass.ROOT_PATH_PREF);
		
		operationCost = UtilityClass.extractCostFuncFromCVS(OP_COST_FILE);
		
		File folder = new File(ROOT_PATH_PREF);
		
		ArrayList<GroupAccuracy> listOfAccuracy = new ArrayList<GroupAccuracy>();
		
		File [] files = folder.listFiles();
		
		for(int i=0; i<files.length; i++){
			
			File fileToExtract = new File(files[i].getAbsolutePath());
			
			File [] insideFolder = files[i].listFiles();
			if(insideFolder == null || insideFolder.length <= 0){
				break;
			}
			for(int j=0; j < insideFolder.length; j++){
				if(insideFolder[j].getName().contains("generated_profiled_") && 
						insideFolder[j].getName().contains(".csv")){
					fileToExtract = insideFolder[j];
					break;
				}
			}
			
			
			TrainAndTestData dataObject = UtilityClass.extractTrainAndTestSetFromCVS(fileToExtract.getAbsolutePath());
			
			String trainFile = files[i].getAbsolutePath() + "//" + fileToExtract.getName() + i + ".train.arff";
			String testFile = files[i].getAbsolutePath() + "//" + fileToExtract.getName() + i + ".test.arff";
			
			
			UtilityClass.writeToARFFFile(trainFile, dataObject.trainData, dataObject.getListOfServers());
			UtilityClass.writeToARFFFile(testFile, dataObject.testData, dataObject.getListOfServers());
			
			MainModel model = new MainModel();
			EvaluationController controller = new EvaluationController(model);
			
			Instances train = UtilityClass.readInstancesFromFile(trainFile);
			Instances test = UtilityClass.readInstancesFromFile(testFile);
			
			ArrayList<Instances> listToTrainingProcess = UtilityClass.splitInstancesArrayToArrays(train, NUMBER_ARRAYS);
			ArrayList<Instances> listToTestProcess = UtilityClass.splitInstancesArrayToArrays(test, NUMBER_ARRAYS);
			
			
			for(int k=0; k<listToTrainingProcess.size() - 1; k++){
				model.trainFromInstances(listToTrainingProcess.get(k));
				controller.setModel(model);
				controller.evaluateData(k, new File(testFile), listToTestProcess.get(k), 75, 25, 15);
			}
			
        	
			
        	String evaluationPath = files[i].getAbsolutePath() + "//" + fileToExtract.getName() + "_" + i + ".EVAL.xls";
        	controller.writeEvaluationResults(new File(evaluationPath));
        	controller.getGroupAccuracy().setFile(evaluationPath);
        	listOfAccuracy.add(controller.getGroupAccuracy());
			
		}
		
		
		
		GroupAccuracy.writeGroupArray(new File(ROOT_PATH_PREF + "//final.xls"), listOfAccuracy);
		
		
		System.out.println("Done!");
		
	}
}
