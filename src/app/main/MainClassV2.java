package app.main;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import weka.core.Instance;
import weka.core.Instances;
import app.files.DeleteScript;
import app.files.TrainAndTestData;
import app.files.UtilityClass;
import app.model.MainModel;

public class MainClassV2 {
	
	//paths
	public static final String ROOT_PATH_PREF = "E://SwedenData//NewLife//dataset//6. Diff levels";
	private static final String OP_COST_FILE = "E://SwedenData//NewLife//opCost//operationCost.csv";
	
	//other stuff
	public static HashMap<String, Integer> operationCost = new HashMap<String, Integer>();
	
	
	public static void main(String [] args) throws Exception{
		
		DeleteScript.deleteEvaluationResults(MainClassV2.ROOT_PATH_PREF);
		
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
			
			model.trainFromInstances(listToCheckProcess.get(0));
			
			for(int k = 1; k<listToCheckProcess.size() - 1; k++){
				
				controller.setModel(model);
				controller.evaluateData(k, files[i].getAbsolutePath(), new File(generalFile), listToCheckProcess.get(k), 100, 0, 10);
				model.trainFromInstances(listToCheckProcess.get(k));
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
