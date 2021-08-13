package controller;

import mainpackage.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.util.Optional;

/**
 * @author Shiana Wilson  class creates the ModifyPartController window.
 */
public class ModifyPartController {
    private Stage stage; // Required in order to build window
    private Parent scene;

    @FXML
    private RadioButton modPartInHouseRadBtn;

    @FXML
    private RadioButton modPartOutsourcedRadBtn;

    @FXML
    private TextField ModIdPartText;

    @FXML
    private TextField modNamePartText;

    @FXML
    private TextField modInvPartText;

    @FXML
    private TextField modPricePartText;

    @FXML
    private TextField modMaxPartText;

    @FXML
    private TextField modMachineIDPartText;

    @FXML
    private TextField modMinPartText;

    @FXML
    private Label modPartMachineOrCompLabel;

    @FXML
    private Label ErrorLabel;

    /**
     * This method triggers the InHouse RadioButton ActionEvent
     * and sets the Label value to Machine ID.
     * <p>
     * Logic issues: None.
     * <p>
     * Compatible features: This feature can be used to make
     * the save button disabled and ensure that values in the
     * Machine ID TextField are integers only.
     *
     * @param event The ActionEvent triggered by clicking on
     *              the InHouse RadioButton.
     */
    @FXML
    void modPartInHouseRadBtn(ActionEvent event) {
        modPartMachineOrCompLabel.setText("Machine ID");
    }

    
    @FXML
    void onActmodPartOutsourcedRadBtn(ActionEvent event) {
        modPartMachineOrCompLabel.setText("Company Name");
    }

    /**
     When this button is clicked it will exit to the Main controller window.
     An alert will be called in order telling the user that changes made will be discarded.
     Then if the user selects OK, the if statement is executed.
     
     Logic issues: at first I tried to use a try and catch statement,
     but I changed my mind if seemed to be a tad bit more difficult.
     
     Future enhancements: This cancel button is generic but can also
     be updated to not prompt the user and simply exit if there are no
     values currently inputted in the TextFields, or if there are no
     added associated Parts.
     @param event The ActionEvent object created by clicking the Cancel button.
     * @throws IOException
     */
    @FXML
    void onActionModifyPartCancelButton(ActionEvent event) throws IOException {
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
     This method will save any modified changes to the Part object that's selected.
     it will update the previous part by removing it from the Inventory
     then it will be replaced at that index a new Part object containing the original
     and any changed fields.
     
     Logic issues: Previously I had made all validation within this method, 
     but since the code was duplicated in other methods I had decided to provide validation 
     through outside static methods instead. 
    
     Future Enhancements: Future version of this method would have the ability to
     disable the save button if there are any real-time validation errors with either the
     validate() method or the validateRadioButtonAction() method.
     @param event The ActionEvent triggered when clicking the Save button.
     @throws IOException
     **/
    @FXML
    void onActModPartSaveBtn(ActionEvent event) throws IOException {
        // Get old Part object in order to find its index
        Part oldPart = Inventory.PartLookUp(Integer.parseInt(ModIdPartText.getText()));
        int oldPartIndex = Inventory.getAllParts().indexOf(oldPart);

        // Validates
        if (Main.validate(
                modNamePartText,
                modInvPartText,
                modPricePartText,
                modMaxPartText,
                modMinPartText
        ) & Main.validateRadioButtonAction(
                modMachineIDPartText,
                modPartInHouseRadBtn,
                modPartOutsourcedRadBtn)) {
            // Is called if InHouse RadioButton is selected
            if (modPartInHouseRadBtn.isSelected()) {
                // Replaces old Part object
                Inventory.updatePart(
                        oldPartIndex,
                        new InHouse(
                                Integer.parseInt(ModIdPartText.getText()),
                                modNamePartText.getText(),
                                Double.parseDouble(modPricePartText.getText()),
                                Integer.parseInt(modInvPartText.getText()),
                                Integer.parseInt(modMinPartText.getText()),
                                Integer.parseInt(modMaxPartText.getText()),
                                Integer.parseInt(modMachineIDPartText.getText())
                        )
                );
            } else {
                // Is called if Outsourced RadioButton is selected
                // Replaces old Part object
                Inventory.updatePart(
                        oldPartIndex,
                        new Outsourced(
                                Integer.parseInt(ModIdPartText.getText()),
                                modNamePartText.getText(),
                                Double.parseDouble(modPricePartText.getText()),
                                Integer.parseInt(modInvPartText.getText()),
                                Integer.parseInt(modMinPartText.getText()),
                                Integer.parseInt(modMaxPartText.getText()),
                                modMachineIDPartText.getText()
                        )
                );
            }

            // Returns user back to Main Controller window
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        // Sets any errors to display in the UI
        ErrorLabel.setText(String.valueOf(Main.errorMessage));
    }

    /**
      @param part The selected Part imported that populates the TextFields
    **/
    public void sendPart(Part part) {
        // Populate all TextFields with received Part object values
        ModIdPartText.setText(String.valueOf(part.getId()));
        modNamePartText.setText(part.getName());
        modInvPartText.setText(String.valueOf(part.getStock()));
        modPricePartText.setText(String.format("%,.2f", part.getPrice()));
        modMaxPartText.setText(String.valueOf(part.getMax()));
        modMinPartText.setText(String.valueOf(part.getMin()));

        // Select InHouse RadioButton if the part is an InHouse object
        if (part instanceof InHouse) {
            modPartInHouseRadBtn.setSelected(true);
            modPartOutsourcedRadBtn.setSelected(false);
            modPartMachineOrCompLabel.setText("Machine ID");
            // Cast to temporarily get access to subclass methods
            modMachineIDPartText.setText(String.valueOf(((InHouse) part).getMachineId()));
        }

        // Select Outsourced RadioButton if the part is an Outsourced object
        if (part instanceof Outsourced) {
            modPartOutsourcedRadBtn.setSelected(true);
            modPartInHouseRadBtn.setSelected(false);
            modPartMachineOrCompLabel.setText("Company Name");
            // Cast to temporarily get access to subclass methods
            modMachineIDPartText.setText(((Outsourced) part).getCompanyName());
        }
    }
}
