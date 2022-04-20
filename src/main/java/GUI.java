import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Objects;

public class GUI {

    JFrame frame;
    JButton fishers;
    JButton newLotNumber;
    JButton updateRecipe;
    JLabel welcome = new JLabel("Welcome to Lot Tracker!", SwingConstants.CENTER);
    JFileChooser fileChooser = new JFileChooser();
    Icon logo = new ImageIcon(Objects.requireNonNull(getClass().getResource("logo.png")));
    boolean warning = true;

    GUI() {
        frame = new JFrame("Lot Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 250);
        frame.setLocationRelativeTo(null);

        fishers = new JButton(logo);
        fishers.addActionListener(this::ButtonOpenWebActionPerformed);
        fishers.setSize(100, 100);

        newLotNumber = new JButton("Add Lot Number");
        newLotNumber.addActionListener(this::NewLotNumber);

        updateRecipe = new JButton("Update Recipe");
        updateRecipe.addActionListener(this::UpdateRecipe);

        frame.setLayout(new BorderLayout(20, 15));
        frame.add(welcome, BorderLayout.NORTH);
        frame.add(fishers, BorderLayout.CENTER);
        frame.add(newLotNumber, BorderLayout.WEST);
        frame.add(updateRecipe, BorderLayout.EAST);
        frame.setVisible(true);
    }

    // from https://stackoverflow.com/questions/10967451/open-a-link-in-browser-with-java-button
    private void ButtonOpenWebActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String url = "https://fishermeats.com";
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void NewLotNumber(java.awt.event.ActionEvent evt) {
        String ingredientAndName = JOptionPane.showInputDialog("FORMAT: INGREDIENT NAME, LOT NUMBER");
        if (ingredientAndName == null || ingredientAndName.equals("")) return;
        ingredientAndName = ingredientAndName.replaceAll(" ", "");
        String[] ingredientCommaName = ingredientAndName.split(",");

        Recipe recipe = new Recipe();
        recipe.newLotNumber(ingredientCommaName[1], ingredientCommaName[0]);
    }

    public void UpdateRecipe(java.awt.event.ActionEvent evt) {
        if (warning) {
            int answer = JOptionPane.showConfirmDialog(frame,
                    "Make sure the excel file is closed when it is being updated\n Show again?",
                    "Reminder", JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.NO_OPTION) warning = false;
        }
        int i = fileChooser.showOpenDialog(frame);
        if (i == JFileChooser.APPROVE_OPTION) {
            File excelFile = fileChooser.getSelectedFile();
            Recipe recipe = new Recipe(excelFile);
            recipe.addLotNumbers(excelFile);
        }
    }

    public static void main(String[] args) {
        new GUI();
    }
}
