package controller;

import mainpackage.Main;
import javafx.collections.FXCollections;
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
 *@author Shiana Wilson  This class creates the ModifyProductController window.
 */
public class ModifyProductController implements Initializable {
    private Stage stage; // Required in order to build the window
    private Parent scene;
    // Temporary holding list for associated Parts
    private ObservableList<Part> linkedParts = FXCollections.observableArrayList();

    @FXML
    private TextField modIdProdText;

    @FXML
    private TextField modNameProdText;

    @FXML
    private TextField modInvProdText;

    @FXML
    private TextField modPriceProdText;

    @FXML
    private TextField modMaxProdText;

    @FXML
    private TextField modMinProdText;

    @FXML
    private TextField modProdSearchText;

    @FXML
    private TableView<Part> modProdTableV1;

    @FXML
    private TableColumn<Part, Integer> modProdPartIdCol;

    @FXML
    private TableColumn<Part, String> modProdPartNameCol;

    @FXML
    private TableColumn<Part, Integer> modProdInvLevCol;

    @FXML
    private TableColumn<Part, Double> modProdPartPriceCol;

    @FXML
    private TableView<Part> modProdTableV2;

    @FXML
    private TableColumn<Part, Integer> modProductPartIdCol;

    @FXML
    private TableColumn<Part, String> modProductPartNameCol;

    @FXML
    private TableColumn<Part, Integer> modifyProductInvLevelCol;

    @FXML
    private TableColumn<Part, Double> modProdPriceCol;

    @FXML
    private Label addPartErrorLabel;

    @FXML
    private Label ErrorLabel;

    /**
     * This KeyEvent method responds to user input in the Search TextField.
     * <p>
     * Logic issues: I couldn't figure out why the list wouldn't populate even
     * though all logic was apparently correct, that is, until I realized that the
     * event object provided by a key event is not an ActionEvent, but a KeyEvent
     * object. I fixed the populating bug by renaming the parameter type from
     * ActionEvent to KeyEvent.
     * <p>
     * Compatible features: Extending the functionality of this method, I would
     * disable the Add button whenever the search results come up null.
     *
     * @param event The KeyEvent object generated when users type into the search
     *              TextField
     */
    @FXML
    void onKeyTypedSearchPart(KeyEvent event) {
        // Create a temporary filtered list of object matching search
        ObservableList<Part> partFilteredList = Inventory.PartLookUp(modProdSearchText.getText());
        modProdTableV1.setItems(partFilteredList);

        // Highlight if only a single row is filtered
        if (partFilteredList.size() == 1) {
            modProdTableV1.getSelectionModel().select(partFilteredList.get(0));
        } else if (partFilteredList.size() == 0) {
            // Provide error message in UI if no results from search
            addPartErrorLabel.setText("No parts matching search field");
        } else {
            // Clear error message label if search field is simply blank
            addPartErrorLabel.setText("");
            modProdTableV1.getSelectionModel().clearSelection();
        }
    }

    /**
     * This method copies and pasts any selected item in TableView1 to
     * TableView2. Part objects that are added to TableView2 can then be
     * added to the object's associated parts list.
     * <p>
     * Logic issues: None.
     * <p>
     * Compatible features: Future versions of this ActionEvent method should
     * disable the Add button by default, until at least one item is selected--
     * that way we could eliminate the alert popup messages.
     *
     * @param event The ActionEvent object created when the user clicks the Add button
     */
    @FXML
    void onActModProdAdd(ActionEvent event) {
        try {
            // Trigger exception if none selected
            Inventory.PartLookUp(modProdTableV1.getSelectionModel().getSelectedItem().getId());

            // Add selected item to linked parts list
            linkedParts.add(modProdTableV1.getSelectionModel().getSelectedItem());
            modProdTableV2.setItems(linkedParts); // Refresh TableView
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item.");
            alert.setTitle("Error Dialog");
            alert.showAndWait();
        }
    }

    /**
     * This ActionEvent method exits the user to the Main controller window.
     * <p>
     * Logic issues: The UI error message was showing an error dialog but
     * wasn't showing any option to click OK. I fixed this bug by changing
     * AlertType from ERROR to CONFIRMATION enumeration.
     * <p>
     * Compatible features: Instead of using a UI popup dialog to confirm with
     * the user that they would like to cancel, future versions of this method
     * could change the color of the cancel button to red, signaling to the
     * user that an action to exit will be taking place if they click again.
     *
     * @param event The ActionEvent object created when a user clicks the Cancel button
     * @throws IOException
     */
    @FXML
    void onActModProdCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit?\nAll changes will be lost.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method removes associated parts from TableView2. The Remove Associated
     * Parts button first confirms whether the user would like to remove the associated
     * part, then, if no item is selected, alerts the user to make a selection
     * first.
     * <p>
     * Logic issues: None.
     * <p>
     * Compatible features: Instead of having the user wait for two separate
     * alert messages when no item is clicked, future versions of this app
     * could disable the Remove Associated Parts button if nothing is selected.
     *
     * @param event The ActionEvent object created when the user clicks the
     *              Remove Associated Parts button
     */
    @FXML
    void onActModProdRemoveAssoc(ActionEvent event) {
        // Confirm with user that part is being unassociated
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Trigger exception if no part selected
                lookupPart(modProdTableV2.getSelectionModel().getSelectedItem().getId());

                // Remove selected part
                linkedParts.remove(modProdTableV2.getSelectionModel().getSelectedItem());
                modProdTableV2.setItems(linkedParts);
            } catch (NullPointerException e) {
                // Alert user if no part is selected
                Alert alert2 = new Alert(Alert.AlertType.ERROR, "Please select a part.");
                alert2.setTitle("Error Dialog");
                alert2.showAndWait();
            }
        }
    }

    /**
     This method will save any modifications that's made to the product object.
     it updates the Product object within Inventory with another product at the same index.

     Logic issues: when implementing this method validation was a little challenging.
     Originally I had implemented the validate() method which had both the
     TextField validations and radio button validations. I tried utilizing this method
     with the original validate() method, but I had trouble separating the button fields from the textfields
     since declaring them as null from within the method would affect the modify Part textfields.
     Rather than doing that I decided to make the radio buttons validations separate which fixed the issue by
     allowing me to use a single validate() method.

     Future Enhancements: in the future this could include
     highlighting all associated parts within TableView1 before confirming
     to save the modified product object.

      @param event The ActionEvent object created when the Save button is clicked
      @throws IOException
     **/
    @FXML
    void onActModProdSave(ActionEvent event) throws IOException {
        Product oldProduct = Inventory.ProdLookUp(Integer.parseInt(modIdProdText.getText()));
        int oldProductIndex = Inventory.getAllProducts().indexOf(oldProduct);
        Product newProduct;


        // Validate TextFields
        if (Main.validate(
                modNameProdText,
                modInvProdText,
                modPriceProdText,
                modMaxProdText,
                modMinProdText
        )) { // If validates, creates object and replaces old.
            Inventory.updateProduct(oldProductIndex, newProduct = new Product(
                            Integer.parseInt(modIdProdText.getText()),
                            modNameProdText.getText(),
                            Double.parseDouble(modPriceProdText.getText()),
                            Integer.parseInt(modInvProdText.getText()),
                            Integer.parseInt(modMinProdText.getText()),
                            Integer.parseInt(modMaxProdText.getText())
                    )
            );

            // Any linked parts are added to the new Product associatedParts.
            for (Part p : linkedParts) {
                newProduct.addAssociatedPart(p);
            }

            // Return user to Main controller window
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        // Inform user of any validation errors in UI
        ErrorLabel.setText(String.valueOf(Main.errorMessage));
    }

    /**
      @param product The Product object received from Main Controller's TableView
     **/
    public void sendProduct(Product product) {
        // Populate all TextFields
        modIdProdText.setText(String.valueOf(product.getId()));
        modNameProdText.setText(product.getName());
        modInvProdText.setText(String.valueOf(product.getStock()));
        modPriceProdText.setText(String.format("%,.2f", product.getPrice()));
        modMaxProdText.setText(String.valueOf(product.getMax()));
        modMinProdText.setText(String.valueOf(product.getMin()));
        // Add associated parts to linkedParts list
        for (Part p : product.getAllAssociatedParts()) {
            linkedParts.add(p);
        }
    }

    /**

      @param partId The integer ID representing the selected Part
      @return Returns the Part object if exists, otherwise returns null
     **/
    private Part lookupPart(int partId) {
        for (int i = 0; i < linkedParts.size(); i++) {
            Part p = linkedParts.get(i);

            if (p.getId() == linkedParts.get(i).getId()) {
                return p;
            }
        }
        return null;
    }

    /**
     @param url            Unused parameter
     @param resourceBundle Unused parameter
    **/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modProdTableV1.setItems(Inventory.getAllParts());

        modProductPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProductPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        modProdTableV2.setItems(linkedParts);

        modProdPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        modProdPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        modProdInvLevCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modProdPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}
