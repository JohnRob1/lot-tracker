import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    }
}
