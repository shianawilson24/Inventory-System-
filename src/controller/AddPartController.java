package controller;
import mainpackage.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.lang.*;

/**
 * @author Shiana Wilson
 */

public class AddPartController implements Initializable {
    public ToggleGroup TG;
    public TextField AddPartIdText;
    private Stage stage; // Required for displaying application
    private Parent scene;

    @FXML
    private RadioButton inHousePartRadioBtn;

    @FXML
    private RadioButton outsourcedPartRadioBtn;

    @FXML
    private Label PartCompanyMachineLabel;

    @FXML
    private Label ErrorLabel;

    @FXML
    private TextField PartNameText;

    @FXML
    private TextField InvPartText;

    @FXML
    private TextField PricePartText;

    @FXML
    private TextField MaxPartText;

    @FXML
    private TextField PartCompanyMachineIdText;

    @FXML
    private TextField MinPartText;

    /** this creates the cancel button that exits to the main screen.
     Compatible features: In a future version of this project I would be able to utilize
     the same alert feature and append it to my add button. Then the
     confirmation will be displayed to the user in a popup user interface dialog whenever
     a user saves.

     * @param event The button click event object.
     * @throws IOException
     */
    @FXML
    void onActionCancelPart(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to exit and discard changes made?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This RadioButton ActionEvent changes the Company Name
     to the Machine ID.

     * Logic issues: along with the validateRadioButtonAction()
     * static method, after deciding how to validate the adjacent
     * TextField, I decided to leave it up to the RadioButton and
     * not use the label to validate.
     * <p>
     * Compatible features: I would like to create a popup in future
     * versions letting users know that their Company Name to
     * Machine ID will create an error if the values are not Integers.
     *
     * @param event The RadioButton ActionEvent
     */
    @FXML
    void inHousePartRadioAct(ActionEvent event) {
        PartCompanyMachineLabel.setText("Machine ID");
    }

    /**
     This RadioButton ActionEvent changes the Machine ID Label to Company Name.

     Logic issues: When trying to figure out how I should validate the adjacent
     TextField, I decided to not use the label to validate it, but instead let the radio button do it.

     Future Enhancements: I would like to create a popup in future
     version letting users know that their Company Name to
     Machine ID will create an error if the values are not Integers.
     @param event The RadioButton ActionEvent
    **/
    @FXML
    void OutsourcedPartRadioAct(ActionEvent event) {
        PartCompanyMachineLabel.setText("Company Name");
    }

    /**
     Logic issues:when trying to validate both the TextFields and the
     RadioButton selections somewhat of a struggle.
     I had originally put the validation within this method. Then realized that I
     would have to duplicate the code for the other stages validation.

     I had separated the validation into two separate static
     methods in the Main.java class.

     @param event The ActionEvent object created when the save button is
                 triggered.
     @throws IOException
     **/
    @FXML
    void SavePartAct(ActionEvent event) throws IOException {
        // After validation passes, InHouse or Outsourced object
        // be created, and stage will move to MainController.
        if (Main.validate(
                PartNameText,
                InvPartText,
                PricePartText,
                MaxPartText,
                MinPartText
        ) & Main.validateRadioButtonAction( // use bitwise operator for forced evaluation
                PartCompanyMachineIdText,
                inHousePartRadioBtn,
                outsourcedPartRadioBtn
        )) {
            // Check to see whether InHouse or Outsourced radio button is
            // selected, then create the corresponding InHouse or Outsourced
            // Part object.
            if (inHousePartRadioBtn.isSelected()) {
                Inventory.addPart(new InHouse(
                        ++MainController.makePartId,
                        PartNameText.getText(),
                        Double.parseDouble(PricePartText.getText()),
                        Integer.parseInt(InvPartText.getText()),
                        Integer.parseInt(MinPartText.getText()),
                        Integer.parseInt(MaxPartText.getText()),
                        Integer.parseInt(PartCompanyMachineIdText.getText()))
                );
            } else {
                Inventory.addPart(new Outsourced(
                        ++MainController.makePartId,
                        PartNameText.getText(),
                        Double.parseDouble(PricePartText.getText()),
                        Integer.parseInt(InvPartText.getText()),
                        Integer.parseInt(MinPartText.getText()),
                        Integer.parseInt(MaxPartText.getText()),
                        PartCompanyMachineIdText.getText())
                );
            }

            // Set stage
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
        // Display any errors in UI
        ErrorLabel.setText(String.valueOf(Main.errorMessage));
    }

    /**
     This is the first method that is called when the AddPartController executes.
     It initially sets the default label for the Machine Id and the Company
     Name to Machine ID.
     *
     * @param url            Unused
     * @param resourceBundle Unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PartCompanyMachineLabel.setText("Machine Id"); // Sets default label
    }
}
