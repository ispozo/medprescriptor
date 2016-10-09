package com.isp.medpresc.forms;

import java.net.URL;
import java.util.ResourceBundle;

import javax.print.PrintException;

import org.apache.log4j.Logger;

import com.isp.medpresc.print.PrinterManager;
import com.isp.medpresc.reports.MufaceReportManager;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javafx.util.Callback;

/**
 * Form controller for MUFACE prescriptions.
 * 
 * @author ispozo
 *
 */
public class MufaceFormController extends AFormController {

    /**
     * Logger.
     */
    private static Logger LOG = Logger.getLogger(MufaceFormController.class);

    /**
     * Maximum number of chars in text area.
     */
    private static final int MAX_CHARS = 200;

    /**
     * Main container.
     */
    @FXML
    BorderPane mainContainer;

    /**
     * Left container.
     */
    @FXML
    VBox leftContainer;

    /**
     * Print button.
     */
    @FXML
    Button printBtn;

    /**
     * Clean button.
     */
    @FXML
    Button cleanBtn;

    /**
     * Text area for prescriptions.
     */
    private TextArea text = new TextArea();

    /**
     * Report manager instance.
     */
    private MufaceReportManager reportMng;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        mainContainer.getStyleClass().add("content-muface");
        leftContainer.setPadding(new Insets(75, 0, 0, 100));
        leftContainer.setSpacing(5);
        text.setWrapText(true);
        text.setMaxSize(300, 120);
        text.setTextFormatter(new TextFormatter<String>(
                change -> change.getControlNewText().length() <= MAX_CHARS ? change : null));
        leftContainer.getChildren().add(text);
    }

    /**
     * Handles printing.
     */
    @FXML
    void onPrintPrescription() {
        
        // GET PRESCRIPTION.
        if (text.getText() == null || text.getText().trim().equals("")) {
            LOG.warn("WARNING!! Missing prescription. Couldn't print.");
            showDialog(AlertType.WARNING, "No se ha incluido ninguna prescripción.", ButtonType.OK, null);
            return;
        }

        if (reportMng == null) {
            reportMng = new MufaceReportManager(getContext());
        }

        // GET NUMBER OF COPIES.
        TextInputDialog numCopies = new TextInputDialog("1");
        DialogPane pane = numCopies.getDialogPane();
        pane.getStylesheets().add("MedPrescriptor.css");
        pane.getStyleClass().add("alerts");
        numCopies.initStyle(StageStyle.TRANSPARENT);
        numCopies.setResizable(false);
        numCopies.setHeaderText("Imprimir");
        numCopies.setContentText("Número de copias");
        numCopies.setResultConverter(new Callback<ButtonType, String>() {

            @Override
            public String call(ButtonType param) {
                return param.getText();
            }
            
        });
        numCopies.showAndWait();

        if (numCopies.getResult() == ButtonType.CANCEL.getText()) {
            return;
        }
        if (numCopies.getContentText() == null || numCopies.getContentText().equals("")) {
            LOG.warn("WARNING!! Missing number of copies. Couldn't print.");
            showDialog(AlertType.WARNING, "No se ha incluido el número de copias.", ButtonType.OK, null);
            return;
        }
        int copies = 0;
        try {
            copies = Integer.parseInt(numCopies.getEditor().getText());
            if (copies <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showDialog(AlertType.ERROR, "El número de copias debe ser mayor que cero.", ButtonType.OK, null);
            return;
        }
        
        // PRINT COPIES.
        try {
            String filePath = reportMng.createMufacePrescriptionPdf(text.getText());
            PrinterManager.print(filePath, copies);
            // text.clear();
        } catch (PrintException e) {
            showDialog(AlertType.ERROR, "No se ha podido imprimir: ", ButtonType.OK, e);
        } catch (Exception e) {
            LOG.error("ERROR!! form resource not found.", e);
        }
    }

    

    /**
     * Handles cleaning.
     */
    @FXML
    void onCleanPrescription() {
        text.clear();
    }

}
