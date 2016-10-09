package com.isp.medpresc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.isp.medpresc.forms.AFormController;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Main application class.
 * 
 * @author ispozo
 *
 */
public class MedPrescriptorMain extends Application {

    /**
     * Logger
     */
    private static Logger LOG = Logger.getLogger(MedPrescriptorMain.class);

    /**
     * Configured properties.
     */
    private Properties configProperties;

    /**
     * Configuration path.
     */
    private static String configPath;

    /**
     * Main method to launch application.
     * 
     * @param arg
     *            Arguments for the application.
     *            <ol>
     *            <li>configPath</li>
     *            </ol>
     */
    public static void main(String[] arg) {
        configPath = arg[0];
        if (configPath == null) {
            throw new NullPointerException("Null config path");
        }
        launch();
    }

    /**
     * Loads configuration.
     */
    private void loadConfiguration() {
        LOG.info("Loading configuration from " + configPath);
        File file = new File(configPath);
        configProperties = new Properties();
        FileInputStream fileInput;
        try {
            fileInput = new FileInputStream(file);
            configProperties.loadFromXML(fileInput);
        } catch (IOException e) {
            LOG.error("ERROR loading configuration", e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        loadConfiguration();

        primaryStage.centerOnScreen();
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.setResizable(false);
        primaryStage.requestFocus();

        VBox layout = new VBox();
        layout.getStyleClass().add("layout");

        // HEADER
        AnchorPane header = new AnchorPane();
        header.getStyleClass().add("header");
        ImageView logo = new ImageView(new Image("images/logo.png"));
        header.getChildren().add(logo);
        AnchorPane.setTopAnchor(logo, 2.0);
        AnchorPane.setLeftAnchor(logo, 1.0);

        ToggleButton closeBtn = new ToggleButton();
        closeBtn.getStyleClass().add("closeButton");
        closeBtn.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                ((Stage) closeBtn.getScene().getWindow()).close();
            }

        });
        header.getChildren().add(closeBtn);
        AnchorPane.setTopAnchor(closeBtn, 2.0);
        AnchorPane.setRightAnchor(closeBtn, 0.0);
        
        ToggleButton minimizeButton = new ToggleButton();
        minimizeButton.getStyleClass().add("minimizeButton");
        minimizeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent arg0) {
                primaryStage.setIconified(true);
            }
        });
        header.getChildren().add(minimizeButton);
        AnchorPane.setTopAnchor(minimizeButton, 2.0);
        AnchorPane.setRightAnchor(minimizeButton, 25.0);

        // CONTENT
        StackPane content = new StackPane();
        content.getStyleClass().add("content");
        content.setMinHeight(500);

        // MENU BAR
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("menuBar");
        Menu prescriptionType = new Menu("Tipo Receta");
        MenuItem menuMuface = new MenuItem("MUFACE");
        MenuItem menuIsfas = new MenuItem("ISFAS");

        menuMuface.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                try {
                    Parent mufaceForm = loadForm("/forms/muface-form.fxml");
                    content.getChildren().add(mufaceForm);
                } catch (IOException e1) {
                    LOG.error("ERROR!! form resource not found.", e1);
                }

            }

        });

        // TODO Pending form for isfas.
        menuIsfas.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    Parent isfasForm = loadForm("/forms/muface-form.fxml");
                    content.getChildren().add(isfasForm);
                } catch (IOException e1) {
                    LOG.error("ERROR!! form resource not found.", e1);
                }
            }
        });

        prescriptionType.getItems().addAll(menuMuface, menuIsfas);
        menuBar.getMenus().addAll(prescriptionType);

        layout.getChildren().add(header);
        layout.getChildren().add(menuBar);
        layout.getChildren().add(content);

        Scene scene = new Scene(layout, 920, 650);
        final Delta delta = new Delta();
        scene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                delta.xoffset = primaryStage.getX() - event.getScreenX();
                delta.yoffset = primaryStage.getY() - event.getScreenY();
            }
        });
        
        scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                primaryStage.setX(event.getScreenX() + delta.xoffset);
                primaryStage.setY(event.getScreenY() + delta.yoffset);
            }
        });
        scene.getStylesheets().add("MedPrescriptor.css");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    /**
     * Loads the form and set the context to the controller.
     * 
     * @param form
     *            Form
     * @return Form.
     * @throws IOException
     *             in case of form not found.
     */
    private Parent loadForm(String form) throws IOException {

        // Parent mufaceForm = FXMLLoader.load(getClass().getResource("/forms/muface-form.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(form));
        Parent formNode = loader.load(getInputStream((form)));
        AFormController controller = (AFormController) loader.getController();
        controller.setContext(configProperties);
        return formNode;
    }

    /**
     * Returns the input stream for the given resource.
     * 
     * @param resource
     *            Resource.
     * @return InputStream.
     */
    private static InputStream getInputStream(String resource) {
        InputStream is = Object.class.getResourceAsStream(resource);
        if (is == null) {
            File file = new File(resource);
            if (file.exists()) {
                try {
                    is = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    LOG.error("ERROR!! loading resource " + resource, e);
                }
            }
        }

        return is;
    }
    
    /**
     * Delta for drag and drop app.
     * @author ispozo
     *
     */
    private class Delta {
        double xoffset, yoffset;
    }

}
