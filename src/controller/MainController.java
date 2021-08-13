package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * @author Shiana Wilson  class creates the Main controller.
 */
public class MainController implements Initializable {
    private Stage stage;  // Stage required to display application.
    private Parent scene;
    public static int makePartId; // Provides a unique ID for parts among all packages.
    public static int makeProductId; // Provides a unique ID for products among all packages.

    @FXML
    private TextField partSearTxt; // Search box for Parts TableView

    @FXML
    private TableView<Part> partTabView; // The Parts TableView

    @FXML
    private TableColumn<Part, Integer> partIdCol; // Part ID column

    @FXML
    private TableColumn<Part, String> partNameCol; // Part Name column

    @FXML
    private TableColumn<Part, Integer> partInvLevCol; // Part Inv column

    @FXML
    private TableColumn<Part, Double> partPricePerUnitCol; // Part Price column

    @FXML
    private TextField searchProdText; // Search box for Product TableView

    @FXML
    private TableView<Product> prodTableV; // The Product TableView

    @FXML
    private TableColumn<Product, Integer> prodIdCol; // Product ID column

    @FXML
    private TableColumn<Product, String> prodNameCol; // Product Name column

    @FXML
    private TableColumn<Product, Integer> prodInvLevCol; // Product Inv column

    @FXML
    private TableColumn<Product, Double> prodPricePerUnitCol; // Product Price column

    @FXML
    private Label partErrorLabel;

    @FXML
    private Label prodErrorLabel;

    /**
     Future Enhancements: In the future this method could include
     more visual feedback for the user to show that the button has been chosen.
     @param event The event object generated after clicking the Add button.
      @throws IOException
     **/
    @FXML
    void onActAddPart(ActionEvent event) throws IOException {
        // Get event source from button
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // Load resources from view directory
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     When you click the Modify button it opens the ModifyPart stage.
     The button responds to click events to load and display the ModifyPart stage.

     * @param event The event object generated after clicking the Modify button.
     * @throws IOException
     */
    @FXML
    void onActModifyPart(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();

            // use getController() to get access to an instance of ModifyPartController
            ModifyPartController MPartController = loader.getController();
            MPartController.sendPart(partTabView.getSelectionModel().getSelectedItem());

            // Get event source from button
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item.");
            alert.setTitle("Error Dialog");
            alert.showAndWait();
        }
    }

    /**
     * The Delete button will remove a selected Part object from Inventory.

     * @param event The event object generated after clicking the Delete button.
     */
    @FXML
    void onActDeletePart(ActionEvent event) {
        try {
            // Trigger exception of no part selected
            Inventory.PartLookUp(partTabView.getSelectionModel().getSelectedItem().getId());

            // Confirm with user to delete part
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete part?");
            Optional<ButtonType> result = alert.showAndWait();

            // Checks whether OK button pressed then deletes part
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(partTabView.getSelectionModel().getSelectedItem()); // deletes Part object
                partTabView.setItems(Inventory.PartLookUp(partSearTxt.getText())); // refresh filtered table
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a part.");
            alert.setTitle("Error Dialog");
            alert.showAndWait();
        }
    }

    /**
     When you click the Add button it will open the AddProduct stage.
     The button responds to click events to load and display the AddProduct stage.
     Logic errors: none
     *
     * @param event The event object generated after clicking the Add button.
     * @throws IOException
     */
    @FXML
    void onActAddProduct(ActionEvent event) throws IOException {
        // Get event source from button
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        // Load resources from view directory
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     The Modify button opens the ModifyProduct stage.
     @param event The event object generated after clicking the Modify button.
     @throws IOException
     **/
    @FXML
    void onActModifyProduct(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
            loader.load();

            // use getController() to get access to an instance of ModifyProductController
            ModifyProductController MProductController = loader.getController();
            MProductController.sendProduct(prodTableV.getSelectionModel().getSelectedItem());

            // Get event source from button
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item.");
            alert.setTitle("Error Dialog");
            alert.showAndWait();
        }

    }

    /**
     The Delete button removes a selected Product object from Inventory.
     @param event The event object generated after clicking the Delete button.
    * */
    @FXML
    void onActDeleteProduct(ActionEvent event) {
        try {
            // Trigger exception if no product selected
            Inventory.ProdLookUp(prodTableV.getSelectionModel().getSelectedItem().getId());

            // Confirm with user to delete part
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete product?");
            Optional<ButtonType> result = alert.showAndWait();

            // Checks wither OK button pressed then deletes product
            if (result.isPresent() && result.get() == ButtonType.OK) {

                // Checks if product has an associatedPart
                if (Inventory.ProdLookUp(prodTableV
                        .getSelectionModel().getSelectedItem().getId())
                        .getAllAssociatedParts().size() > 0) {

                    // Alert user that a part is still associated with the product
                    Alert alert1 = new Alert(Alert.AlertType.ERROR,
                            "Has part associated with it!\nPlease remove part then delete.");
                    alert1.setTitle("Error Dialog");
                    alert1.showAndWait();
                } else {
                    // Delete the product
                    Inventory.deleteProduct(prodTableV.getSelectionModel().getSelectedItem());
                    // refresh filtered table
                    prodTableV.setItems(Inventory.ProdLookUp(searchProdText.getText()));
                }
            }
        } catch (NullPointerException e) {
            // Alert user to select a product
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a product.");
            alert.setTitle("Error Dialog");
            alert.showAndWait();
        }
    }

    /**
     @param event The event object generated after clicking the Exit button.
     **/
    @FXML
    void onActExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Exit application?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK)
            System.exit(0); // Closes application
    }

    /**
     * This method responds upon key entry in the Part search field.
     * The method updates the Part TableView with filtered Part objects
     * as a result of the search text.
     * <p>
     * Logic issue: I had trouble making the filter for both integers and
     * for strings. After watching several of the online resources on
     * creating a search TextField, I discovered that I could add all
     * searches into an ObservableList within this method itself and
     * utilize the lookup() Inventory method to return an observable
     * list of filtered parts.
     * <p>
     * Compatible features: If a updated version of this method were created,
     * I could create an UI message or alert that notifies the user when
     * a search comes up unsuccessful.
     *
     * @param event The event object generated after text is entered in the search field.
     */
    @FXML
    void onKeySearchPartIdOrName(KeyEvent event) {
        ObservableList<Part> partFilteredList = Inventory.PartLookUp(partSearTxt.getText());
        partTabView.setItems(partFilteredList);

        // Highlight if only a single row is filtered
        if (partFilteredList.size() == 1) {
            partTabView.getSelectionModel().select(partFilteredList.get(0));
        } else if (partFilteredList.size() == 0) {
            // Provide error message in UI if no results from search
            partErrorLabel.setText("No parts matching search field");
        } else {
            // Clear error message label if search field is simply blank
            partErrorLabel.setText("");
            partTabView.getSelectionModel().clearSelection();
        }
    }


    @FXML
    void onKeySearchProductIdOrName(KeyEvent event) {
        ObservableList<Product> productFilteredList = Inventory.ProdLookUp(searchProdText.getText());
        prodTableV.setItems(productFilteredList);

        if (productFilteredList.size() == 1) {
            prodTableV.getSelectionModel().select(productFilteredList.get(0));
        } else if (productFilteredList.size() == 0) {
            // Provide error message in UI if no results from search
            prodErrorLabel.setText("No parts matching search field");
        } else {
            // Clear error message label if search field is simply blank
            prodErrorLabel.setText("");
            prodTableV.getSelectionModel().clearSelection();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set up table view, let table know which objects will be working with.
        partTabView.setItems(Inventory.getAllParts());
        prodTableV.setItems(Inventory.getAllProducts());

        // Get id, and populate cell of ID column
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvLevCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        prodIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        prodNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        prodInvLevCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prodPricePerUnitCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
