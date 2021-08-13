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
 *@author shiana wilson This class creates the AddProductController.
 */
public class AddProductController implements Initializable {
    private Stage stage; // Stage required to display application
    private Parent scene;
    // List to temporarily hold associated parts until committed to
    // the associatedProduct() method in the Product object.
    private ObservableList<Part> linkedParts = FXCollections.observableArrayList();

    @FXML
    private TextField AddProdNameText;

    @FXML
    private TextField AddProdInvText;

    @FXML
    private TextField AddProdPriceText;

    @FXML
    private TextField AddProdMaxText;

    @FXML
    private TextField AddProdMinText;

    @FXML
    private TextField AddProdSearchText;

    @FXML
    private TableView<Part> addProdTableV1;

    @FXML
    private TableColumn<Part, Integer> addProdPartIdCol;

    @FXML
    private TableColumn<Part, String> addProdParNameCol;

    @FXML
    private TableColumn<Part, Integer> addProdInvLevCol;

    @FXML
    private TableColumn<Part, Double> addProdPriceCol;

    @FXML
    private TableView<Part> addProdTableV2;

    @FXML
    private TableColumn<Part, Integer> addProductPartIDCol;

    @FXML
    private TableColumn<Part, String> addProdPNameCol;

    @FXML
    private TableColumn<Part, Integer> addProductInvLevelCol;

    @FXML
    private TableColumn<Part, Double> addProductPriceCol;

    @FXML
    private Label errorLabel;

    @FXML
    private Label addPartErrorLabel;

    /**
     * This method utilizes a search TextField and displays items
     * matching the query.
     * <p>
     * Logic issues: The search feature was not working because I had initially
     * set the event type as Action event. After receiving an argument type
     * mismatch I fixed the issues by changing the even type from ActionEvent to
     * KeyEvent.
     * <p>
     * Compatible features: This method can be reversed in order to search
     * for items NOT part of the searched string.
     *
     * @param event A KeyEvent object.
     */
    @FXML
    void onKeyTypedSearchPart(KeyEvent event) {
        // Call PartLookUp and return a filtered list.
        ObservableList<Part> partFilteredList = Inventory.PartLookUp(
                AddProdSearchText.getText());
        // Set table with filtered list of searched items.
        addProdTableV1.setItems(partFilteredList);

        // Highlight if only a single row is filtered
        if (partFilteredList.size() == 1) {
            addProdTableV1.getSelectionModel().select(partFilteredList.get(0));
        } else if (partFilteredList.size() == 0) {
            addPartErrorLabel.setText("No parts matching search field");
        } else {
            addPartErrorLabel.setText("");
            addProdTableV1.getSelectionModel().clearSelection();
        }
    }


    @FXML
    void onActionAddProductAddButton(ActionEvent event) {
        try {
            // Trigger exception of no part selected
            Inventory.PartLookUp(addProdTableV1.getSelectionModel().getSelectedItem().getId());

            // Add selected item to linked parts list
            linkedParts.add(addProdTableV1.getSelectionModel().getSelectedItem());
            addProdTableV2.setItems(linkedParts); // Refresh TableView
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item.");
            alert.setTitle("Error Dialog");
            alert.showAndWait();
        }
    }

    /**
     * Button returns to Main window.
     * This button alerts the user before returning the user to the main
     * window upon clicking.
     * <p>
     * Compatible features: The cancel button can be used to clear the TextField
     * items after alerting the user that all fields will be discarded.
     *
     * @param event The ActionEvent button click
     */
    @FXML
    void onActionAddProductCancelButton(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Exit and discard changes?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     This method will removes any associated parts to the product.

     Logic issues: I had to figure out how I could trigger an exception
     I couldn't use the PartLookUp() method from Inventory since it did not reference
     the temporary list of associated parts.
     I had to created a helper method in AddProductController
     called PartLookUp in order to trigger this exception.

     Future Enhancements: The future version of ActionEvent should
     make the remove association button disabled by default until an item is selected.

     * @param event The ActionEvent object created when button clicked
     */
    @FXML
    void onActionAddProductRemoveAssociation(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove part?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Trigger exception if no part selected
                PartLookUp(addProdTableV2.getSelectionModel().getSelectedItem().getId());

                linkedParts.remove(addProdTableV2.getSelectionModel().getSelectedItem()); // deletes Part object
                addProdTableV2.setItems(linkedParts); // refresh filtered table
            } catch (NullPointerException e) {
                Alert alert2 = new Alert(Alert.AlertType.ERROR, "Please select a part.");
                alert2.setTitle("Error Dialog");
                alert2.showAndWait();
            }
        }
    }

    /**
     Logic issues: I decided on creating the product object before
     saving the class that way I can add associated parts.

     Future Enhancements: I might want to use the
     logic in here in a separate helper method.

     @param event The ActionEvent object created by the Save button
     @throws IOException
     **/
    @FXML
    void onActionAddProductSaveButton(ActionEvent event) throws IOException {
        // Validate all TextFields
        if (Main.validate(
                AddProdNameText,
                AddProdInvText,
                AddProdPriceText,
                AddProdMaxText,
                AddProdMinText
        )) {
            // Automatically create a product ID then assign to a variable
            // for later use.
            int id = ++MainController.makeProductId;

            // Create Product object and add to Inventory ObservableList
            Inventory.addProduct(new Product(
                    id,
                    AddProdNameText.getText(),
                    Double.parseDouble(AddProdPriceText.getText()),
                    Integer.parseInt(AddProdInvText.getText()),
                    Integer.parseInt(AddProdMinText.getText()),
                    Integer.parseInt(AddProdMaxText.getText())
            ));

            // Iterate through all linked parts and add them to the Product
            // object's associated list.
            Product p = Inventory.ProdLookUp(id);
            for (Part part : linkedParts) {
                p.addAssociatedPart(part);
            }

            // Go to Main page
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }

        // Log any errors to user's UI
        errorLabel.setText(String.valueOf(Main.errorMessage));
    }



    private Part PartLookUp(int partId) {
        for (int i = 0; i < linkedParts.size(); i++) {
            Part p = linkedParts.get(i);

            if (p.getId() == linkedParts.get(i).getId()) {
                return p;
            }
        }
        return null;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addProdTableV1.setItems(Inventory.getAllParts());
        addProdTableV2.setItems(linkedParts);

        addProdPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProdPNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        addProdPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProdParNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProdInvLevCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProdPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
}



