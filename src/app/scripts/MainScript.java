package app.scripts;

import java.io.File;

import app.files.TrainAndTestData;
import app.files.UtilityClass;

public class MainScript {
	
	
	public static void main(String[] args){
		
		File fileToExtract = new File("D:\\20.07.2015\\1. first test set old\\generated_1000_0.0_7.csv");
		
		TrainAndTestData dataObject = UtilityClass.extractTrainAndTestSetFromCVS(fileToExtract.getAbsolutePath());
		
		String trainFile = fileToExtract.getAbsolutePath()  + ".train.arff";
		String testFile = fileToExtract.getAbsolutePath() + ".test.arff";
		
		
		UtilityClass.writeToARFFFile(trainFile, dataObject.trainData, dataObject.getListOfServers());
		UtilityClass.writeToARFFFile(testFile, dataObject.testData, dataObject.getListOfServers());
		
		
		
	}
	

}
