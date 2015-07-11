package app.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class GroupAccuracy {
	
	private String file;
	private float randomAccuracy;
	private float simpleAccuracy;
	private float gapAccuracy;
	
	public GroupAccuracy(String file, float randomAccuracy,
			float simpleAccuracy, float gapAccuracy) {
		super();
		this.file = file;
		this.randomAccuracy = randomAccuracy;
		this.simpleAccuracy = simpleAccuracy;
		this.gapAccuracy = gapAccuracy;
	}

	
	public static void writeGroupArray(File file, ArrayList<GroupAccuracy> list){
		
		Workbook workbook = new XSSFWorkbook();
		
		
		Sheet sheet = workbook.createSheet("Final results");
	    int rownum = 0;
		for(GroupAccuracy obj : list){
			    
			Row row = sheet.createRow(rownum++);
			    		
			Cell cell1 = row.createCell(0);
			cell1.setCellValue((String)obj.getFile());
			Cell cell2 = row.createCell(1);
			cell2.setCellValue((float)obj.getRandomAccuracy());
			Cell cell3 = row.createCell(2);
			cell3.setCellValue((float)obj.getSimpleAccuracy());
			Cell cell4 = row.createCell(3);
			cell4.setCellValue((float)obj.getGapAccuracy());
			    		
		}
		
		Row row = sheet.createRow(rownum++);
		
		Cell cell1 = row.createCell(0);
		cell1.setCellValue((String)"Total:");
		Cell cell2 = row.createCell(1);
		cell2.setCellFormula("AVERAGE(B1:B180)");
		Cell cell3 = row.createCell(2);
		cell3.setCellFormula("AVERAGE(C1:C180)");
		Cell cell4 = row.createCell(3);
		cell4.setCellFormula("AVERAGE(D1:D180)");
		
		
	    try {
	        FileOutputStream out = 
	                new FileOutputStream(file);
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
	
	
	
	
	
	
	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public float getRandomAccuracy() {
		return randomAccuracy;
	}

	public void setRandomAccuracy(float randomAccuracy) {
		this.randomAccuracy = randomAccuracy;
	}

	public float getSimpleAccuracy() {
		return simpleAccuracy;
	}

	public void setSimpleAccuracy(float simpleAccuracy) {
		this.simpleAccuracy = simpleAccuracy;
	}

	public float getGapAccuracy() {
		return gapAccuracy;
	}

	public void setGapAccuracy(float gapAccuracy) {
		this.gapAccuracy = gapAccuracy;
	}

}
