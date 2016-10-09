package com.isp.medpresc.forms;

import java.util.Properties;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.StageStyle;

/**
 * Abstract class used as starting point to implement a form controller.
 * 
 * @author ispozo
 *
 */
public abstract class AFormController implements Initializable {

    /**
     * Context properties.
     */
    private Properties properties;

    /**
     * Set context properties.
     * 
     * @param properties
     *            Properties to be set.
     */
    public void setContext(Properties properties) {
        if (this.properties == null) {
            this.properties = properties;
        }
    }

    /**
     * Returns the context properties.
     * 
     * @return {@code Properties}.
     */
    protected Properties getContext() {
        return properties;
    }

    /**
     * Show alert dialog.
     * 
     * @param alertType
     *            Alert type.
     * @param msg
     *            Message
     * @param btnType
     *            Button type.
     * @param e
     *            Exception.
     */
    protected void showDialog(AlertType type, String msg, ButtonType btnType, Exception e) {
        Alert alert = new Alert(AlertType.ERROR, msg +  (e != null ? " " + e.getMessage() : ""), ButtonType.OK);
        DialogPane pane = alert.getDialogPane();
        pane.getStylesheets().add("MedPrescriptor.css");
        pane.getStyleClass().add("alerts");
        alert.initStyle(StageStyle.TRANSPARENT);
        alert.setResizable(false);
        alert.showAndWait();
    }
}
