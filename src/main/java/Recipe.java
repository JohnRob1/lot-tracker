import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;

public class Recipe {
    public File file;

    public Recipe() {}

    public Recipe (File file) {
        this.file = file;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void newLotNumber (Scanner scanner) { //Used to create new lot number
        System.out.println("Input name of the ingredient");
        String ingredient = scanner.next();
        System.out.println("Input the lot number");
        String lotNumber = scanner.next();
        //ingredient and lot number are now in reference vars
        try {
            File file = new File("lotNumbers.txt");
            //"lotNumbers.txt is the text file that the program will read from to add lot numbers to an Excel file recipe
            if (!file.createNewFile()) System.out.println("File Found\n");
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime ldt = LocalDateTime.now();
            String output = ingredient + "    " + lotNumber + "    " + dtf.format(ldt) + "\n";
            Files.write(Paths.get("lotNumbers.txt"),output.getBytes(), StandardOpenOption.APPEND);
            //Makes sure to append to the file and not replace
        } catch (IOException e) {
            System.out.println("Error\n");
            e.printStackTrace();
        }
    }

    public void addLotNumbers(File excelFile) {
        try {
            FileInputStream fis = new FileInputStream(excelFile);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> iterator = sheet.iterator();
            while(iterator.hasNext()) {
                Row row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                while(cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    CellType type = cell.getCellType();
                    if (type == CellType.STRING) {
                        System.out.println(cell.getStringCellValue() + "\t\t\t");
                    } else if (type == CellType.NUMERIC) {
                        System.out.println( (int) cell.getNumericCellValue() + "\t\t\t");
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
