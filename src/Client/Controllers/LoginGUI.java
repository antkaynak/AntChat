package Client.Controllers;

import Client.ApplicationContext;
import Client.Main;
import com.jfoenix.controls.JFXComboBox;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginGUI implements Initializable{

    @FXML
    private TextField fieldIpAdress;

    @FXML
    private Button buttonEnter;

    @FXML
    private TextField fieldUserName;

    @FXML
    private TextField fieldPort;

    @FXML
    private VBox vbox;

    @FXML
    private AnchorPane anchor;

    @FXML
    private JFXComboBox<String> comboBox;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!Main.isSplashLoaded){
            loadSplashScreen();
        }

        //Max 10 characters for username text field
        textFieldLimit(fieldUserName, 10);

        //Max 20 characters for ip address text field
        textFieldLimit(fieldIpAdress, 20);

        //Max 10 characters for port text field
        textFieldLimit(fieldPort, 10);

        Platform.runLater(()-> vbox.requestFocus());

        comboBox.getItems().addAll("Dark","Light","Blue","Green");

        vbox.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                buttonEnter.fire();
            }
        });
    }

    private void textFieldLimit(TextField textField, int limit){
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > oldValue.intValue()){
                if(textField.getText().length() >= 10){
                    textField.setText(textField.getText().substring(0,limit));
                }
            }
        });
    }


    @FXML
    void ComboBoxAction(ActionEvent event) {
        ApplicationContext.getThemeConfiguration().selectTheme(comboBox.getValue());
    }


    @FXML
    void pressedEnter(ActionEvent event) {
        if(comboBox.getSelectionModel().isEmpty()){
            return;
        }
        String name;
        int port;
        String ip;
        try{
        name = fieldUserName.getText();
        port = Integer.parseInt(fieldPort.getText());
        ip = fieldIpAdress.getText();
        }catch(Exception e){
            ApplicationContext.getStageConfiguration().showErrorStage("Invalid input. Please try again...");
            e.printStackTrace();
            return;
        }
        try {
            initStreamConfiguration(name, ip, port);
            ApplicationContext.getStageConfiguration().initClientGUI();
            ApplicationContext.getStageConfiguration().initConnection();
            ApplicationContext.getThemeConfiguration().confirmTheme(ApplicationContext.getStageConfiguration().getClientGUIScene());
            ApplicationContext.getThemeConfiguration().confirmTheme(ApplicationContext.getStageConfiguration().getConnectionScene());
            ApplicationContext.getStageConfiguration().getClientGUIStage().show();
            ApplicationContext.getStageConfiguration().getLoginGUIStage().close();

        }catch(IOException ex){
            ApplicationContext.getStageConfiguration().showErrorStage();
            ex.printStackTrace();
        }

        catch (Exception e) {
            ApplicationContext.getStageConfiguration().showErrorStage();
            e.printStackTrace();
            System.exit(0);

        }


    }

    private void initStreamConfiguration(String name, String ip, int port) throws IOException {
        ApplicationContext.getStreamConfiguration().setName(name);
        ApplicationContext.getStreamConfiguration().setHostAddr(ip);
        ApplicationContext.getStreamConfiguration().setPortAddr(port);
        ApplicationContext.getStreamConfiguration().initSocket();
        ApplicationContext.getStreamConfiguration().initStream();
    }



    private void loadSplashScreen() {
        try {
            Main.isSplashLoaded = true;
            StackPane pane = (StackPane) FXMLLoader.load(getClass().getClassLoader().getResource(("Client/Views/SplashFXML.fxml")));
            vbox.getChildren().setAll(pane);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(3), pane);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setCycleCount(1);

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), pane);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setCycleCount(1);

            fadeIn.play();

            fadeIn.setOnFinished((e) -> {
                fadeOut.play();
            });

            fadeOut.setOnFinished((e) -> {
                try {
                    AnchorPane parentContent = FXMLLoader.load(getClass().getClassLoader().getResource(("Client/Views/LoginGUI.fxml")));
                    vbox.getChildren().setAll(parentContent);

                } catch (IOException ex) {
                   ex.printStackTrace();
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }





}
