package mainpackage;

import controller.MainController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Product;

/**
 * @author Shiana Wilson
 * This is the main class that launches the app. Static public methods
 * are included here.
 */
public class Main extends Application {
    public static StringBuilder errorMessage = new StringBuilder();

    /**
     This method will show the main stage.The first stage is called by the main() method
     after launch() is called.

     Logic issue: it was a break trying to understand how the application could start without
     having the containers immediately set up.

     Future Enhancements: More than a single window can be instantiated, and possibly have
     the Parts TableView and the Products TableView in separate scenes.

      @param primaryStage The first stage created when application starts.
      @throws Exception The required exception for all JavaFX projects.
     **/
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml")); // Load Main fxml
        primaryStage.setTitle("Inventory Management");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        // Create default Part objects
        InHouse IH1 = new InHouse(++MainController.makePartId, "Skateboard Wheels", 21.99, 12, 1, 40, 1);
        InHouse IH2 = new InHouse(++MainController.makePartId, "Bike Handle", 10.99, 5, 1, 10, 3);
        InHouse IH3 = new InHouse(++MainController.makePartId, "Bike Wheels", 25.25, 12, 1, 16, 4);
        Outsourced outsourced1 = new Outsourced(++MainController.makePartId, "Skateboard Deck", 59.99, 40, 5, 100, "Pro Form");
        Outsourced outsourced2 = new Outsourced(++MainController.makePartId, "Bike Pedals", 13.95, 5, 1, 6, "Huffy");
        Outsourced outsourced3 = new Outsourced(++MainController.makePartId, "Scooter Handle", 42.95, 22, 3, 200, "WalMart");

        // Add Part objects to Inventory
        Inventory.addPart(IH1);
        Inventory.addPart(IH2);
        Inventory.addPart(IH3);
        Inventory.addPart(outsourced1);
        Inventory.addPart(outsourced2);
        Inventory.addPart(outsourced3);

        // Create default Products objects
        Product product1 = new Product(++MainController.makeProductId, "Bike", 125.99, 3, 1, 13);
        Product product2 = new Product(++MainController.makeProductId, "Scooter", 100.95, 13, 1, 45);
        Product product3 = new Product(++MainController.makeProductId, "skateboard", 140.99, 8, 1, 30);

        // Add Product objects to Inventory
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);

        // Launch application after Inventory has been populated.
        launch(args);
    }

    public static boolean validate(
            TextField name,
            TextField stock,
            TextField price,
            TextField max,
            TextField min) {

        // Initialize variables
        int vInv = 0;
        int vMax = 0;
        int vMin = 0;
        double vPrice = 0;
        String regexInt = "^-?\\d+";
        String regexDouble = "^-?\\d+(\\.\\d+)?";
        String regexWord = "^\\w+(.*\\w+)*";

        boolean clearToSave = true;

        // Clear errorMessage StringBuilder
        errorMessage.delete(0, errorMessage.length());

        try {
            // Test name TextField
            if (!(name.getText().matches(regexWord))) {
                clearToSave = false;
                errorMessage.append("Name: enter a valid name\n");
            }

            // Test stock TextField for Integer
            if (!(stock.getText().matches(regexInt))) {
                clearToSave = false;
                errorMessage.append("Inv: enter a valid integer\n");
            } else {
                vInv = Integer.parseInt(stock.getText());
            }

            // Test price TextField for Double
            if (!(price.getText().matches(regexDouble))) {
                clearToSave = false;
                errorMessage.append("Price: enter a valid double\n");
            } else {
                vPrice = Double.parseDouble(price.getText());
            }

            // Test max TextField for Integer
            if (!(max.getText().matches(regexInt))) {
                clearToSave = false;
                errorMessage.append("Max: enter a valid integer\n");
            } else {
                vMax = Integer.parseInt(max.getText());
            }

            // Test min TextField for Integer
            if (!(min.getText().matches(regexInt))) {
                clearToSave = false;
                errorMessage.append("Min: enter a valid integer\n");
            } else {
                vMin = Integer.parseInt(min.getText());
            }

            // Max logic checking
            if (vMax < 0) {
                clearToSave = false;
                errorMessage.append("Max: must be greater than 0\n");
            } else if (vMax < vMin) {
                clearToSave = false;
                errorMessage.append("Max: must be greater than or equal to Min\n");
            }

            // Min logic checking
            if (vMin < 0) {
                clearToSave = false;
                errorMessage.append("Min: has to be greater than 0\n");
            } else if (vMin > vMax) {
                clearToSave = false;
                errorMessage.append("Min: has to be less than or equal to Max\n");
            }

            // Inventory logic checking, must be between Max and Min.
            if (vInv > vMax || vInv < vMin) {
                clearToSave = false;
                errorMessage.append("Inv: has to be between Max and Min\n");
            } else if (vInv < 0) {
                clearToSave = false;
                errorMessage.append("Inv: has to be greater than or equal to 0\n");
            }

            // Price logic checking must be greater than zero.
            if (vPrice < 0) {
                clearToSave = false;
                errorMessage.append("Price: has to be greater than 0\n");
            }

            // Log to console
            System.out.println(errorMessage);
        } catch (Exception e) { // Catches any unusual errors.
            System.out.println("Exception: " + e);
            Alert alert = new Alert(Alert.AlertType.ERROR, "An Error Occurred");
            alert.setTitle("Error Dialog");
            alert.showAndWait();
        }

        return clearToSave;
    }

    public static boolean validateRadioButtonAction(
            TextField machineIdOrCompany,
            RadioButton inHouse,
            RadioButton outsourced) {

        // Initialize variables
        boolean clearToSave = true;
        String regexInt = "^-?\\d+";
        String regexWord = "^\\w+(.*\\w+)*.?";

        // If InHouse radio button is selected and the Machine ID is not
        // an integer, raise an error.
        if (inHouse.isSelected() && !(machineIdOrCompany.getText().matches(regexInt))) {
            clearToSave = false;
            Main.errorMessage.append("Machine ID: Please enter a valid integer\n");

            // If Outsourced radio button is selected and the Company name is
            // empty, raise an error.
        } else if (outsourced.isSelected() && !(machineIdOrCompany.getText().matches(regexWord))) {
            clearToSave = false;
            Main.errorMessage.append("Company Name: please enter a valid string\n");
        }

        // Log errorMessage to console
        System.out.println(Main.errorMessage);

        return clearToSave;
    }
}


