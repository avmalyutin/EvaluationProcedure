package app.files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import weka.core.Instance;
import weka.core.Instances;
import app.main.EvaluationProcedure;

public class UtilityClass {
	
	
	public static Instances readInstancesFromFile(String filePath){
		try{
			BufferedReader breader = null;
			breader = new BufferedReader(new FileReader(filePath));
			Instances retrievedInstances = new Instances (breader);
			return retrievedInstances;
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
		
	
	
	public static ArrayList<Instances> splitInstancesArrayToArrays(Instances train, int numArrays){
		
		Map <String, Instances> mapOfTrainInstances = new HashMap<String, Instances>();
		
		for(int g=0; g<train.numInstances(); g++){
			Instance inter = (Instance)train.instance(g);
			String argeth = new BigDecimal(inter.value(1)).toPlainString();
			if(!mapOfTrainInstances.containsKey(argeth)){
				Instances ingerg = new Instances(train, 0, 0);
				mapOfTrainInstances.put(argeth, ingerg);
			}
			mapOfTrainInstances.get(argeth).add(inter);
		}
		
		int mapSize = mapOfTrainInstances.entrySet().size();
		int arrayElemCount = mapSize/numArrays;
		arrayElemCount = arrayElemCount + (mapSize%numArrays !=0 ? 1 : 0);
		
		ArrayList<Instances> listToTrainingProcess = new ArrayList<Instances>(numArrays);
		Instances bufferInstances = new Instances(train, 0, 0);
		int counter = 0;
		int counterToStop = 0;
		
		
		for (Map.Entry<String, Instances> entry : mapOfTrainInstances.entrySet()){
		    
			counter++;
			counterToStop++;
			Instances innerBufferInstances = entry.getValue();
			for(int q=0; q<innerBufferInstances.numInstances();q++){
				bufferInstances.add(innerBufferInstances.instance(q));
			}
			 
			if(counter == arrayElemCount || counterToStop == mapSize){
				listToTrainingProcess.add(bufferInstances);
				bufferInstances = new Instances(train, 0, 0);
				counter = 0;
		    }
		}		
		
		return listToTrainingProcess;
		
	}
	
	
	
	
	public static String readTextFromFile(File f) throws IOException{
		
		BufferedReader br = new BufferedReader(new FileReader(f));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
	
	
	public static void writeTextToFile(String stringTowrite, File f){
		try (FileOutputStream fop = new FileOutputStream(f)) {
		
			 
			// if file doesn't exists, then create it
			if (!f.exists()) {
				f.createNewFile();
			}
	
			// get the content in bytes
			byte[] contentInBytes = stringTowrite.getBytes();
	
			fop.write(contentInBytes);
			fop.flush();
			fop.close();
	
			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static HashMap<String, Integer> extractCostFuncFromCVS (String filename) throws FileNotFoundException, IOException{
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		HashMap<String, Integer> mapOfServicesOpCost = new HashMap<String, Integer>();
		try {
	 
			br = new BufferedReader(new FileReader(filename));
			while ((line = br.readLine()) != null) {
	 
				String[] base = line.split(cvsSplitBy);
				
				if(base.length == 2){
					String serviceName = base[0];
					int operationCost = Integer.parseInt(base[1]);
					mapOfServicesOpCost.put(serviceName, operationCost);
				}
			}
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	  
	  return mapOfServicesOpCost;
	}
	
	
	
	public static TrainAndTestData extractTrainAndTestSetFromCVS(String filename){
		
		TrainAndTestData dataObject = new TrainAndTestData();
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		try {
		 
			br = new BufferedReader(new FileReader(filename));
			int counter = 0;
			int counter2 = 0;
			while ((line = br.readLine()) != null) {
		
				String[] country = line.split(cvsSplitBy);
					
				if(country.length == 2){
					String[] info = country[1].split(";");
					String date = country[0];
					String nameServer = info[1];
					String responceTime = info[2];
					String buffer = nameServer + "#" + date + "#" + responceTime;
					dataObject.addServer(nameServer);
					if(counter < 36){
						dataObject.trainData.add(buffer);
						counter++;
					}
					else{
						dataObject.testData.add(buffer);
						if(counter2 == 3){
							counter = 0;
							counter2 = 0;
						}else{
							counter2++;
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return dataObject;
	}
	
	
	public static void writeToARFFFile(String filePath, ArrayList<String> servicesData, ArrayList<String> serversNames){
			
		String serversString = "";
		for (String serverName : serversNames) {
			serversString += serverName + ",";
		}
		
		serversString = serversString.substring(0, serversString.lastIndexOf(","));
		String bufferToWrite = "@relation ServiceSelectorelector " + "\n"
				+ "@attribute championServiceInstance {" + serversString + "} "
				+ "\n" + "@attribute timestamp numeric " + "\n"
				+ "@attribute responsetime numeric  " + "\n" + "\n" + "@data"
				+ "\n";

		for (String str : servicesData) {

			String[] buffer = str.split("#");

			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.ENGLISH);
			try {
				buffer[1] = String.valueOf(format.parse(buffer[1]).getTime());
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			bufferToWrite += "\"" + buffer[0] + "\"," + buffer[1] + ","
					+ buffer[2] + "\n";

		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			writer.write(bufferToWrite);
			writer.newLine();
			writer.flush();
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	public static void writeRealAndPredictedValues(String path, HashMap<Double, EvaluationProcedure> map){
		
		Workbook workbook = new XSSFWorkbook();
		
		Iterator<Entry<Double, EvaluationProcedure>> it = map.entrySet().iterator();
		Sheet sheet = workbook.createSheet("Real and predicted values");
		
		int rownum = 0;
		Row rowTitle = sheet.createRow(rownum++);
	    Cell cell1 = rowTitle.createCell(0);
	    cell1.setCellValue("Date");
	    
	    Cell cell2 = rowTitle.createCell(1);
	    cell2.setCellValue("Service 1 real");
	    Cell cell3 = rowTitle.createCell(2);
	    cell3.setCellValue("Service 1 predicted");
	    Cell cell4 = rowTitle.createCell(3);
	    cell4.setCellValue("Service 2 real");
	    Cell cell5 = rowTitle.createCell(4);
	    cell5.setCellValue("Service 2 predicted");
	    Cell cell6 = rowTitle.createCell(5);
	    cell6.setCellValue("Service 3 real");
	    Cell cell7 = rowTitle.createCell(6);
	    cell7.setCellValue("Service 3 predicted");
	    Cell cell8 = rowTitle.createCell(7);
	    cell8.setCellValue("Service 4 real");
	    Cell cell9 = rowTitle.createCell(8);
	    cell9.setCellValue("Service 4 predicted");
		
		while (it.hasNext()) {
			
			Map.Entry imp = (Map.Entry) it.next();

			EvaluationProcedure obj = (EvaluationProcedure) imp.getValue();

			Row row = sheet.createRow(rownum++);

			cell1 = row.createCell(0);
			cell1.setCellValue(UtilityClass.convertDoubleToString(obj.getDate()));

			cell2 = row.createCell(1);
			cell2.setCellValue(obj.getActualList().get(0).getServiceName() + ":"+ obj.getActualList().get(0).getResponceTims());
			cell3 = row.createCell(2);
			cell3.setCellValue(obj.getPredictedList().get(0).getServiceName() + ":"+ obj.getPredictedList().get(0).getResponceTims());

			cell4 = row.createCell(3);
			cell4.setCellValue(obj.getActualList().get(1).getServiceName() + ":"+ obj.getActualList().get(1).getResponceTims());
			cell5 = row.createCell(4);
			cell5.setCellValue(obj.getPredictedList().get(1).getServiceName() + ":"+ obj.getPredictedList().get(1).getResponceTims());

			cell6 = row.createCell(5);
			cell6.setCellValue(obj.getActualList().get(2).getServiceName() + ":"+ obj.getActualList().get(2).getResponceTims());
			cell7 = row.createCell(6);
			cell7.setCellValue(obj.getPredictedList().get(2).getServiceName() + ":"+ obj.getPredictedList().get(2).getResponceTims());

			cell8 = row.createCell(7);
			cell8.setCellValue(obj.getActualList().get(3).getServiceName() + ":"+ obj.getActualList().get(3).getResponceTims());
			cell9 = row.createCell(8);
			cell9.setCellValue(obj.getPredictedList().get(3).getServiceName() + ":"+ obj.getPredictedList().get(3).getResponceTims());

		}
		try {
			FileOutputStream out = new FileOutputStream(new File(path));
			workbook.write(out);
			out.close();
			workbook.close();
			System.out.println("Excel real and predicted values written successfully..");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	public static double convertStrToDouble(String date){
		
		try{
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			return Double.valueOf(format.parse(date).getTime());
		}
		catch(Exception ex){
			ex.printStackTrace();
			return -1;
		}
	}
	
	
	public static String convertDoubleToString(double date){
		
		long itemLong = (long) (date);
		Date itemDate = new Date(itemLong);
		String itemDateStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(itemDate);
		return itemDateStr;
	} 
	
	
	
	public static Object deepClone(Object object) {
		try {
		     ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     ObjectOutputStream oos = new ObjectOutputStream(baos);
		     oos.writeObject(object);
		     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		     ObjectInputStream ois = new ObjectInputStream(bais);
		     return ois.readObject();
		}
		catch (Exception e) {
		     e.printStackTrace();
		     return null;
		}
	}
}
