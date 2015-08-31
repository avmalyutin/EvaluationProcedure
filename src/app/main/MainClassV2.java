package app.main;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import weka.core.Instance;
import weka.core.Instances;
import app.files.DeleteScript;
import app.files.TrainAndTestData;
import app.files.UtilityClass;
import app.model.LocationModule;
import app.model.MainModel;

public class MainClassV2 {
	
	//paths
	public static final String ROOT_PATH_PREF = "E://SwedenData//NewLife//dataset//18. Second experiment + location";
	private static final String OP_COST_FILE = "E://SwedenData//NewLife//opCost//operationCost.csv";
	
	//other stuff
	public static HashMap<String, Integer> operationCost = new HashMap<String, Integer>();
	
	
	public static String userCountry = "null";
	
	
	public static void main(String [] args) throws Exception{
		
		DeleteScript.deleteEvaluationResults(MainClassV2.ROOT_PATH_PREF);
		operationCost = UtilityClass.extractCostFuncFromCVS(OP_COST_FILE);
		
		File folder = new File(ROOT_PATH_PREF);
		ArrayList<GroupAccuracy> listOfAccuracy = new ArrayList<GroupAccuracy>();
		
		File [] files = folder.listFiles();
		
		
		int countryIndex = 1;
		for(int i=0; i<1; i++){
			
			long startTime = System.nanoTime();
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
			
			
			userCountry = LocationModule.getTheCountryByIndex(countryIndex);
			countryIndex ++;
			if(countryIndex > 3)
				countryIndex = 0;
			
			UtilityClass.modifyCSVLocation(fileToExtract.getAbsolutePath(), userCountry);
			
			TrainAndTestData dataObject = UtilityClass.extractOneSetFromCVS(fileToExtract.getAbsolutePath());
			
			String generalFile = files[i].getAbsolutePath() + "//" + fileToExtract.getName() + i + ".general.arff";
			
			
			UtilityClass.writeToARFFFile(generalFile, dataObject.trainData, dataObject.getListOfServers());
			
			MainModel model = new MainModel();
			EvaluationController controller = new EvaluationController(model);
			
			Instances generalSet = UtilityClass.readInstancesFromFile(generalFile);
			
			ArrayList<Instances> listToCheckProcess = UtilityClass.splitInstancesArrayToArrays(generalSet, generalSet.numInstances());
			
			listToCheckProcess.sort(new Comparator<Instances>() {
				@Override
				public int compare(Instances o1, Instances o2) {
					double value1 = o1.instance(0).value(1);
					double value2 = o2.instance(0).value(1);
					return (int) (value1-value2);
				}
			});
			
			
			
			for(int k = 0; k<listToCheckProcess.size() - 2; k++){
				model.trainFromInstances(listToCheckProcess.get(k));
				controller.setModel(model);
				controller.evaluateData(k, files[i].getAbsolutePath(), new File(generalFile), listToCheckProcess.get(k + 1), 100, 0, 10);
			}
			
        	
			
        	String evaluationPath = files[i].getAbsolutePath() + "//" + fileToExtract.getName() + "_" + i + ".EVAL.xls";
        	controller.writeEvaluationResults(new File(evaluationPath));
        	controller.getGroupAccuracy().setFile(evaluationPath);
        	listOfAccuracy.add(controller.getGroupAccuracy());
        	
        	
        	//writing the evaluation results
        	String evaluationPathForAccuracy = files[i].getAbsolutePath() + "//" + fileToExtract.getName() + "_" + i + "finalResults.xls";
        	GroupAccuracy.writeGroupArray(new File(evaluationPathForAccuracy), listOfAccuracy);
        	
        	
        	insideFolder = files[i].listFiles();
        	//building graphs
        	for(int j=0; j < insideFolder.length; j++){
				if(insideFolder[j].getName().contains("generated_profiled_realAndPredicted") && 
						insideFolder[j].getName().contains(".R")){
					//execute script
					UtilityClass.runScript(insideFolder[j].getAbsolutePath());
				}
        	}
        	
        	
        	
        	
        	long endTime = System.nanoTime();

        	long duration = (endTime - startTime)/1000000;
        	
        	String time = String.format("%d min, %d sec", 
        		    TimeUnit.MILLISECONDS.toMinutes(duration),
        		    TimeUnit.MILLISECONDS.toSeconds(duration) - 
        		    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
        		);
        	
        	System.out.println("Spended time: " + time);
        	
		}
		
		
		
		GroupAccuracy.writeGroupArray(new File(ROOT_PATH_PREF + "//final.xls"), listOfAccuracy);
		
		
		System.out.println("Done!");
		
	}
}
