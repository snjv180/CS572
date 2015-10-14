/**
 * 
 */
package com.comp572.proj4;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/**
 * @author Sanjeev
 * 
 */
public class WriteExcelGP {
    // Blank workbook
    private XSSFWorkbook workbook = new XSSFWorkbook();

    public void createSheet(String sheetName,
	    Map<String, PopulationStatistics> data) {
	// Create a blank sheet
	XSSFSheet sheet = workbook.createSheet(sheetName);
	sheet.setAutoFilter(CellRangeAddress.valueOf("A1:F" + (data.size() + 1)));

	// Iterate over data and write to sheet
	Set<String> keyset = data.keySet();
	int rownum = 0;
	if (rownum == 0) {
	    Row row = sheet.createRow(rownum++);
	    int cellnum = 0;
	    setCellValue(row, cellnum++, (Object) "Generation Number");
	    setCellValue(row, cellnum++,
		    (Object) "Average Fitness for Generation");
	    setCellValue(row, cellnum++,
		    (Object) "Time to evolve (in microseconds)");
	    setCellValue(row, cellnum++, (Object) "Best Individual's Fitness");
	    setCellValue(row, cellnum++,
		    (Object) "Best Individual For Generation");
	}
	for (String key : keyset) {
	    Row row = sheet.createRow(rownum++);
	    PopulationStatistics popStats = data.get(key);
	    int cellnum = 0;
	    setCellValue(row, cellnum++, popStats.getGenerationNumer());
	    setCellValue(row, cellnum++, popStats.getAvgFitness4Gen());
	    setCellValue(row, cellnum++, popStats.getTimeToEvolve4Gen());
	    setCellValue(row, cellnum++,
		    popStats.getBestIndividualFitness4Gen());
	    setCellValue(row, cellnum++, popStats.getBestIndividual4Gen());

	}
	for (int i = 1; i < 5; i++) {
	    sheet.autoSizeColumn(i);
	}

    }

    private void setCellValue(Row row, int cellnum, Object object) {
	Cell cell = row.createCell(cellnum);
	if (object instanceof String)
	    cell.setCellValue((String) object);
	else if (object instanceof Integer)
	    cell.setCellValue((Integer) object);
	else if (object instanceof Float)
	    cell.setCellValue((Float) object);
	else if (object instanceof Double)
	    cell.setCellValue((Double) object);

    }

    public void closeExcel(String fileName) {
	try {
	    // Write the workbook in file system
	    FileOutputStream out = new FileOutputStream(new File("tests"
		    + File.separator + fileName + ".xlsx"));
	    workbook.write(out);
	    out.close();
	    System.out
		    .println("TestResults.xlsx written successfully on disk.");
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
