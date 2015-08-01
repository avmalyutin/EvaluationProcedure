package app.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import weka.core.Instances;
import app.files.UtilityClass;
import app.model.MainModel;

public class EvaluationController {

	private MainModel model;
	
	private HashMap<String, ArrayList<EvaluationData>> listOfEvaluatioObjects;
	private GroupAccuracy groupAccuracy;
	
	private static String RANDOM_LABEL = "Random";
	private static String TOP1_LABEL = "Top1";
	private static String TOPGAP_LABEL = "TopGap";
	
	
	public EvaluationController(MainModel model1) {
		super();
		this.model = model1;
		this.listOfEvaluatioObjects = new HashMap<String, ArrayList<EvaluationData>>();
		ArrayList<EvaluationData> list1 = new ArrayList<EvaluationData>();
		this.listOfEvaluatioObjects.put(RANDOM_LABEL, list1);
		ArrayList<EvaluationData> list2 = new ArrayList<EvaluationData>();
		this.listOfEvaluatioObjects.put(TOP1_LABEL, list2);
		ArrayList<EvaluationData> list3 = new ArrayList<EvaluationData>();
		this.listOfEvaluatioObjects.put(TOPGAP_LABEL, list3);
		
		this.groupAccuracy = new GroupAccuracy();
		
	}


	public void evaluateData(int index, File fileToEvaluate, Instances listOfInstances, int respTimePer, int costOpPerc, int gapPerc){
		
		
    	HashMap<Double, EvaluationProcedure> map = new HashMap<Double, EvaluationProcedure>();
    	
    	for(int i=0; i<listOfInstances.numInstances();i++){
    		String nameservice = listOfInstances.instance(i).stringValue(0);
    		double daterequest = listOfInstances.instance(i).value(1);
    		double respTime = listOfInstances.instance(i).value(2);
    		
    		if(!map.containsKey(daterequest) || map.size() <=0){
    			EvaluationProcedure newObject = new EvaluationProcedure(daterequest, model);
				map.put(daterequest, newObject);
			}
        	map.get(daterequest).addReadedItem(nameservice, respTime);
    	}
    	
    	int counter = 0;
    	int randomEvaluationCounter = 0;
    	int simpleEvaluationCounter = 0;
    	int topRangeEvaluationCounter = 0;
    	float percentageRandomEval = 0;
    	float percentageSimpleEval = 0;
    	float percentageTopRange = 0;
    	
    	
    	
    	Iterator<Entry<Double, EvaluationProcedure>> it = map.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry imp = (Map.Entry) it.next();
	    	
	    	counter++;
	    	EvaluationProcedure obj = (EvaluationProcedure)imp.getValue();
	    	obj.predictAndCalculate(respTimePer, costOpPerc);
	    	int x = obj.proceedRandomEvaluation();
	    	int y = obj.proceedSimpleEvaluation();
	    	int z = obj.processTopRangeEvaluation(gapPerc);
	    	randomEvaluationCounter = randomEvaluationCounter + x;
	    	simpleEvaluationCounter = simpleEvaluationCounter + y;
	    	topRangeEvaluationCounter = topRangeEvaluationCounter + z;
	   
	    	percentageRandomEval = (float)randomEvaluationCounter*100/counter;
	    	percentageSimpleEval = (float)simpleEvaluationCounter*100/counter;
	    	percentageTopRange = (float)topRangeEvaluationCounter*100/counter;

	    	//EvaluationData objectRandom = new EvaluationData(index, randomEvaluationCounter, counter, percentageRandomEval);
	    	//EvaluationData objectTopOne = new EvaluationData(index, simpleEvaluationCounter, counter, percentageSimpleEval);
	    	//EvaluationData objectTopGap = new EvaluationData(index, topRangeEvaluationCounter, counter, percentageTopRange);
	    	
	    	//listOfEvaluatioObjects.get(RANDOM_LABEL).add(objectRandom);
	    	//listOfEvaluatioObjects.get(TOP1_LABEL).add(objectTopOne);
	    	//listOfEvaluatioObjects.get(TOPGAP_LABEL).add(objectTopGap);
	    	
	    }
    	
	    
	    //this.writeRealAndPredictedValues("D://results.csv", map);

    	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    	System.out.println("Random evaluation");
    	System.out.println("Total: " + counter + " Right: " + randomEvaluationCounter);
    	System.out.println("Percentage:" + percentageRandomEval);
    	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    	System.out.println("Simple evaluation");
    	System.out.println("Total: " + counter + " Right: " + simpleEvaluationCounter);
    	System.out.println("Percentage:" + percentageSimpleEval);
    	System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    	System.out.println("Top range evaluation");
    	System.out.println("Total: " + counter + " Right: " + topRangeEvaluationCounter);
    	System.out.println("Percentage:" + percentageTopRange);
    	
    	EvaluationData objectRandom = new EvaluationData(index, randomEvaluationCounter, counter, percentageRandomEval);
    	EvaluationData objectTopOne = new EvaluationData(index, simpleEvaluationCounter, counter, percentageSimpleEval);
    	EvaluationData objectTopGap = new EvaluationData(index, topRangeEvaluationCounter, counter, percentageTopRange);
    	
    	listOfEvaluatioObjects.get(RANDOM_LABEL).add(objectRandom);
    	listOfEvaluatioObjects.get(TOP1_LABEL).add(objectTopOne);
    	listOfEvaluatioObjects.get(TOPGAP_LABEL).add(objectTopGap);
    	
    	this.groupAccuracy.addCounters(randomEvaluationCounter, simpleEvaluationCounter, topRangeEvaluationCounter, counter); 
    	
	}
	
	
	public void writeEvaluationResults(File file) {

		Workbook workbook = new XSSFWorkbook();

		Iterator<Entry<String, ArrayList<EvaluationData>>> it = listOfEvaluatioObjects
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry imp = (Map.Entry) it.next();

			Sheet sheet = workbook.createSheet((String) imp.getKey());

			int rownum = 0;
			for (EvaluationData obj : (ArrayList<EvaluationData>) imp.getValue()) {

				Row row = sheet.createRow(rownum++);

				Cell cell1 = row.createCell(0);
				cell1.setCellValue((int) obj.getAttepmtNumber());
				Cell cell2 = row.createCell(1);
				cell2.setCellValue((int) obj.getRigthAttemps());
				Cell cell3 = row.createCell(2);
				cell3.setCellValue((int) obj.getTotalAttempts());
				Cell cell4 = row.createCell(3);
				cell4.setCellValue((float) obj.getPercentage());

			}

			Row row = sheet.createRow(rownum++);
			Cell cell1 = row.createCell(0);
			cell1.setCellFormula("COUNT(A1:A" + (rownum - 1) + ")");
			Cell cell2 = row.createCell(1);
			cell2.setCellFormula("SUM(B1:B" + (rownum - 1) + ")");
			Cell cell3 = row.createCell(2);
			cell3.setCellFormula("SUM(C1:C" + (rownum - 1) + ")");
			Cell cell4 = row.createCell(3);
			cell4.setCellFormula("SUM(D1:D" + (rownum - 1) + ")/"
					+ (rownum - 1));

		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
			workbook.close();
			System.out.println("Excel written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	
	
	
	
	private void writeRealAndPredictedValues(String path, HashMap<Double, EvaluationProcedure> map){
		
		Workbook workbook = new XSSFWorkbook();
		
		Iterator<Entry<Double, EvaluationProcedure>> it = map.entrySet().iterator();
		Sheet sheet = workbook.createSheet("Timing");
	    while (it.hasNext()) {
	    	Map.Entry imp = (Map.Entry) it.next();
		
	    	
	         
		    int rownum = 0;
		    
		    Row rowTitle = sheet.createRow(rownum++);
		    Cell cell1 = rowTitle.createCell(0);
		    cell1.setCellValue("Date");
		    Cell cell2 = rowTitle.createCell(1);
		    cell2.setCellValue("Date");
		    Cell cell3 = rowTitle.createCell(2);
		    cell3.setCellValue("Date");
		    Cell cell4 = rowTitle.createCell(3);
		    cell4.setCellValue("Date");
		    Cell cell5 = rowTitle.createCell(4);
		    cell5.setCellValue("Date");
		    
		    
		    for(EvaluationProcedure obj : (ArrayList<EvaluationProcedure>)imp.getValue()){
			    
			    Row row = sheet.createRow(rownum++);
			    		
			    cell1 = row.createCell(0);
			    cell1.setCellValue(obj.getDate());
			    cell2 = row.createCell(1);
			    cell2.setCellValue(obj.getActualList().get(0).getResponceTims());
			    cell3 = row.createCell(2);
			    cell3.setCellValue(obj.getActualList().get(1).getResponceTims());
			    cell4 = row.createCell(3);
			    cell4.setCellValue(obj.getActualList().get(2).getResponceTims());
			    cell5 = row.createCell(3);
			    cell5.setCellValue(obj.getActualList().get(3).getResponceTims());
			    
			    row = sheet.createRow(rownum++);
			    cell1 = row.createCell(0);
			    cell1.setCellValue(obj.getDate());
			    cell2 = row.createCell(1);
			    cell2.setCellValue(obj.getPredictedList().get(0).getResponceTims());
			    cell3 = row.createCell(2);
			    cell3.setCellValue(obj.getPredictedList().get(1).getResponceTims());
			    cell4 = row.createCell(3);
			    cell4.setCellValue(obj.getPredictedList().get(2).getResponceTims());
			    cell5 = row.createCell(3);
			    cell5.setCellValue(obj.getPredictedList().get(3).getResponceTims());
			    
			    		
			}
		    /*
		    Row row = sheet.createRow(rownum++);
		    Cell cell11 = row.createCell(0);
		    cell1.setCellFormula("COUNT(A1:A"+ (rownum -1) +")");
		    Cell cell12 = row.createCell(1);
		    cell2.setCellFormula("SUM(B1:B"+ (rownum -1) +")");
		    Cell cell13 = row.createCell(2);
		    cell3.setCellFormula("SUM(C1:C"+ (rownum -1) +")");
		    Cell cell14 = row.createCell(3);
		    cell4.setCellFormula("SUM(D1:D"+ (rownum -1) +")/"+ (rownum -1) );
		    */
	    }    
	    try {
	        FileOutputStream out = 
	                new FileOutputStream(new File(path));
	        workbook.write(out);
	        out.close();
	        workbook.close();
	        System.out.println("Excel written successfully..");
	             
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	
	
	
	
	

	public GroupAccuracy getGroupAccuracy() {
		return groupAccuracy;
	}


	public void setGroupAccuracy(GroupAccuracy groupAccuracy) {
		this.groupAccuracy = groupAccuracy;
	}


	public MainModel getModel() {
		return model;
	}


	public void setModel(MainModel model) {
		this.model = model;
	}
	
}
