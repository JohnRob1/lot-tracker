import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        int option = 0;
        String input;
        Scanner scan = new Scanner(System.in).useDelimiter("\n");
        while (option != 3) {
            System.out.print("Welcome to lot-tracker \n" +
                    "Please choose an option:\n" +
                    "1) New Lot Number\n" +
                    "2) Update Excel Sheet With Lot Numbers\n" +
                    "3) Exit\n");
            input = scan.next();
            try {
                option = Integer.parseInt(input);
                if (option == 1) {
                    Recipe recipe = new Recipe();
                    recipe.newLotNumber(scan);
                } else if (option == 2) {
                    String filename;
                    File excelFile;

                    System.out.println("Please type Excel file name without the .xlsx");
                    filename = scan.next();

                    excelFile = new File(filename + ".xlsx");
                    Recipe recipe = new Recipe(excelFile);
                    recipe.addLotNumbers(excelFile);
                }
            } catch (NumberFormatException e) {
                System.out.println("Please input a number and try again.");
            }
        }
        return;
    }
}
