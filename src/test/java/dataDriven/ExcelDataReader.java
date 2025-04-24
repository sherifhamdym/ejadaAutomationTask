package dataDriven;

import org.apache.poi.ss.usermodel.*;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelDataReader {

    // Path to the Excel file
    // This method provides data for login tests from an Excel file
    @DataProvider(name = "loginDataFromExcel")
    public static Object[][] provideLoginDataFromExcel() throws IOException {
       String excelFilePath = "src/test/resources/TestData/InvalidCredentials_Data.xlsx"; // Adjust path
        return readExcelData(excelFilePath, "Sheet1"); // Assuming data is in "Sheet1"
    }

    // This method provides data for full scenario tests from an Excel file
    @DataProvider(name = "FullScenarioTestData")
    public static Object[][] provideFullScenarioTestData() throws IOException {
        String excelFilePath = "src/test/resources/TestData/FullScenario_Data.xlsx"; // Adjust path
        return readExcelData(excelFilePath, "Sheet1"); // Assuming data is in "Sheet1"
    }

    // This method reads data from an Excel file and returns it as a 2D Object array
    public static Object[][] readExcelData(String filePath, String sheetName) throws IOException {
        File file = new File(filePath);
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet == null) {
            workbook.close();
            throw new IllegalArgumentException("Sheet not found: " + sheetName);
        }

        List<Object[]> dataRows = new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();

        // Skip header row if it exists (assuming first row is header)
        if (rowIterator.hasNext()) {
            rowIterator.next();
        }

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            List<Object> cellData = new ArrayList<>();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                CellType cellType = cell.getCellType();
                switch (cellType) {
                    case STRING:
                        cellData.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            cellData.add(cell.getDateCellValue().toString());
                        } else {
                            cellData.add(String.valueOf((long) cell.getNumericCellValue())); // Avoid decimal issues for IDs etc.
                        }
                        break;
                    case BOOLEAN:
                        cellData.add(cell.getBooleanCellValue());
                        break;
                    case BLANK:
                        cellData.add(""); // Or null, depending on your needs
                        break;
                    default:
                        cellData.add(null); // Handle other cell types as needed
                }
            }
            dataRows.add(cellData.toArray());
        }

        workbook.close();
        return dataRows.toArray(new Object[dataRows.size()][]);
    }
}