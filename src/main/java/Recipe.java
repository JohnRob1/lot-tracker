import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;
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

    // adapted from https://stackoverflow.com/questions/1377279/find-a-line-in-a-file-and-remove-it
    public void newLotNumber (String lotNumber, String ingredient) { //Used to create new lot number

        ingredient = ingredient.toLowerCase();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime ldt = LocalDateTime.now();

        try {
            Path lotNumbersPath = Path.of(Objects.requireNonNull(getClass().getResource("lotNumbers.txt")).getPath());
            File lotNumbers = new File(String.valueOf(lotNumbersPath));
            setFile(lotNumbers);

            //check if lot number already exists and replace it
            String source = Paths.get(Objects.requireNonNull(this.getClass().getResource("/")).getPath()).toString();
            File tempFile = new File(source + "myTempFile.txt");
            BufferedReader reader = new BufferedReader(new FileReader(lotNumbers));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            String line;
            String[] info;

            while ((line = reader.readLine()) != null) {
                //System.out.println(line);
                info = line.split(" {4}");
                info[0] = info[0].toLowerCase();
                if (info[0].equals(ingredient)) continue;
                writer.append(line + "\n");
            }
            writer.close();
            reader.close();
            boolean successful = tempFile.renameTo(lotNumbers);
            //System.out.println(successful);

            //"lotNumbers.txt is the text file that the program will read from to add lot numbers to an Excel file recipe
            // DEBUG if (!file.createNewFile()) System.out.println("File Found\n");
            String output = ingredient + "    " + lotNumber + "    " + dtf.format(ldt) + "\n";
            Files.write(lotNumbersPath, output.getBytes(), StandardOpenOption.APPEND);
            //Makes sure to append to the file and not replace

        } catch (IOException e) {
            System.out.println("Error\n");
            e.printStackTrace();
        }
    }

    public void addLotNumbers(File excelFile) {
        //reads Excel file
        try {

            FileInputStream fis = new FileInputStream(excelFile);
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);

            for (Row row : sheet) {

                Iterator<Cell> cellIterator = row.cellIterator();

                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();
                    CellType type = cell.getCellType();

                    if (type == CellType.STRING) {
                        // DEBUG System.out.println("Ingredient to find: " + cell.getStringCellValue());
                        String ingredient = cell.getStringCellValue();
                        ingredient = ingredient.toLowerCase();

                        //start parsing through text file to find ingredient that matches that of Excel file
                        File lotNumbers = new File(Objects.requireNonNull(getClass().getResource("lotNumbers.txt")).getPath());
                        setFile(lotNumbers);
                        FileReader fr = new FileReader(file);
                        BufferedReader reader = new BufferedReader(fr);
                        String line = reader.readLine();
                        line = line.toLowerCase();

                        while (line != null) {
                            String[] lot = line.split(" {4}");
                            // DEBUG System.out.println("Lot Number: " + lot[1]);
                            // DEBUG System.out.println(Arrays.toString(lot));

                            if (ingredient.equals(lot[0])) {
                                Cell cellToEdit = row.getCell((cell.getColumnIndex() + 1));
                                if (cellToEdit == null) cellToEdit = row.createCell(cell.getColumnIndex() + 1);
                                //DEBUG System.out.println("Column index: " + (int) (cell.getColumnIndex() + 1));
                                cellToEdit.setCellValue(lot[1]);
                                //DEBUG System.out.println("Placed lot number: " + lot[1] +  "; with ingredient: " + lot[0]);
                            }

                            line = reader.readLine();
                        }
                    }
                }
            }

            fis.close();
            String excelFilename = excelFile.toString();

            FileOutputStream outFile = new FileOutputStream(new File(excelFilename));
            wb.write(outFile);
            outFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
