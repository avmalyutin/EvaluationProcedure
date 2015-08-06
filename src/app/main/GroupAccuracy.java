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
	

	private int randomRigthCount;
	private int userRightCount;
	private int simpleRigthCount;
	private int gapRightCount;
	private int attempsCount;
	
	
	public void addCounters(int randomCounter, int userCounter, int simpleCounter, int gapCounter, int totalCounters){
		
		this.randomRigthCount += randomCounter;
		this.userRightCount += userCounter;
		this.simpleRigthCount += simpleCounter;
		this.gapRightCount += gapCounter;
		this.attempsCount += totalCounters;
	}
	
	public float calculateRandomAccuracy(){
		return ((float)this.randomRigthCount/this.attempsCount)*100;
	}
	
	public float calculateUserAccuracy(){
		return ((float)this.userRightCount/this.attempsCount)*100;
	}
	
	public float calculateSimpleAccuracy(){
		return ((float)this.simpleRigthCount/this.attempsCount)*100;
	}

	public float calculateTopGapAccuracy(){
		return ((float)this.gapRightCount/this.attempsCount)*100;
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
			cell2.setCellValue((float)obj.calculateRandomAccuracy());
			Cell cell3 = row.createCell(2);
			cell3.setCellValue((float)obj.calculateUserAccuracy());
			Cell cell4 = row.createCell(3);
			cell4.setCellValue((float)obj.calculateSimpleAccuracy());
			Cell cell5 = row.createCell(4);
			cell5.setCellValue((float)obj.calculateTopGapAccuracy());
			    		
		}
		
		Row row = sheet.createRow(rownum++);
		
		Cell cell1 = row.createCell(0);
		cell1.setCellValue((String)"Total:");
		Cell cell2 = row.createCell(1);
		cell2.setCellFormula("AVERAGE(B1:B"+ (rownum - 1)+")");
		Cell cell3 = row.createCell(2);
		cell3.setCellFormula("AVERAGE(C1:C"+ (rownum - 1)+")");
		Cell cell4 = row.createCell(3);
		cell4.setCellFormula("AVERAGE(D1:D"+ (rownum - 1)+")");
		Cell cell5 = row.createCell(4);
		cell5.setCellFormula("AVERAGE(E1:E"+ (rownum - 1)+")");
		
		
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
	
}
