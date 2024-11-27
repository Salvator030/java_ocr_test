package dot.javaFX.controller;

import dot.asserts.EPurpose;
import dot.business.receipt.Receipt;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DialogChancheValuesController {

    @FXML
    private DialogPane dialogPane;

    @FXML
    private TextField dateValue = null;

    @FXML
    private TextField nameValue = null;

    @FXML
    private TextField summValue = null;

    @FXML
    private ChoiceBox<EPurpose> usesValue = null;

    @FXML
    private Button changValuesOkBtn = null;

    @FXML
    private Button changeValuesCancelBtn = null;

    private MainInteractor mainInteractor;
    private Receipt receipt;
    private ObjectProperty<ObservableList<EPurpose>> purposes = new SimpleObjectProperty<ObservableList<EPurpose>>(
            FXCollections.observableArrayList(EPurpose.values()));

    public void setMainInteractor(MainInteractor interactor) {
        this.mainInteractor = interactor;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
        dateValue.textProperty().bindBidirectional(receipt.dateProperty());
        nameValue.textProperty().bindBidirectional(receipt.shopNameProperty());
        summValue.textProperty().bindBidirectional(receipt.summProperty());
        usesValue.valueProperty().bindBidirectional(receipt.purposeProperty());
    }

    @FXML
    public void handleChangeValuesOklBtn() {
        System.out.println("OklBtn");
        this.mainInteractor.handleChangeValuesOklBtn();
    }

    @FXML
    public void handleChangeValueCancleBtn() {
        System.out.println("CancelBtn");
        this.mainInteractor.handleChangeValuesCancelBtn();
    }

    @FXML
    private void initialize() {

        usesValue.getItems().addAll(purposes.get());
    }

}