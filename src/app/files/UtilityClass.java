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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import weka.core.Instances;

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
	
	
	public static void writeToARFFFile(String filePath, ArrayList<String> servicesData){
			
			String bufferToWrite = "@relation ServiceSelectorelector " +"\n"+
				"@attribute championServiceInstance {CyclicUp1, CyclicUp2, CyclicUp3, CyclicUp4} " +"\n"+
				"@attribute timestamp numeric " +"\n"+
				"@attribute responsetime numeric  " +"\n"+"\n"+
				"@data" + "\n";
			
			for(String str:servicesData){
				
				String [] buffer = str.split("#");
				
				
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
				try{
					buffer[1] = String.valueOf(format.parse(buffer[1]).getTime());
				}
				catch(Exception ex){
					ex.printStackTrace();
				}
				
				bufferToWrite += "\""+buffer[0] + "\"," +buffer[1]+","+buffer[2]+"\n"; 
				
			}
			
			
			try{
				BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
				writer.write(bufferToWrite);
				writer.newLine();
				writer.flush();
				writer.close();
			}
			catch(Exception ex){
				ex.printStackTrace();
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
