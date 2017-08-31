package Client.Controllers;


import Client.ApplicationContext;
import Client.Utility.*;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import java.net.URL;
import java.util.ResourceBundle;



public class MenuController implements Initializable{

    @FXML
    private JFXButton buttonSettings;

    @FXML
    private JFXButton buttonExit2;

    @FXML
    private JFXButton buttonAbout;

    @FXML
    private VBox menuFxmlVBox;


    @FXML
    private JFXButton buttonConnection;

    public static VBox staticmenuFxmlVBox;

    private boolean isSettingsOpen;
    private boolean isAboutOpen;
    private boolean isConnectionOpen;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        staticmenuFxmlVBox = menuFxmlVBox;
        isAboutOpen = false;
        isSettingsOpen = false;
        isConnectionOpen = false;
    }

    @FXML
    void Settings(ActionEvent event) {
        if (!isSettingsOpen) {
            isSettingsOpen = true;
            try {
                ApplicationContext.getStageConfiguration().showSettingsStage();
            isSettingsOpen = false;
            }catch (Exception e) {
                ApplicationContext.getStageConfiguration().showErrorStage();
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    @FXML
    void Connection(ActionEvent event) {
        if(!isConnectionOpen){
            //Preventing spamming connection windows.
            isConnectionOpen = true;
            try {
                ApplicationContext.getStageConfiguration().showConnectionStage();
                isConnectionOpen = false;

            } catch (Exception e) {
                ApplicationContext.getStageConfiguration().showErrorStage();
                e.printStackTrace();
                System.exit(0);
            }

        }
    }


    @FXML
    void About(ActionEvent event) {

        if(!isAboutOpen){
            //Preventing spamming about windows.
            isAboutOpen = true;
        try {
            ApplicationContext.getStageConfiguration().showAboutStage();
            isAboutOpen = false;

        } catch (Exception e) {
            ApplicationContext.getStageConfiguration().showErrorStage();
            e.printStackTrace();
            System.exit(0);
        }

        }
    }


    @FXML
    void Exit(ActionEvent event) {
        //ApplicationContext.getStageConfiguration().getExitStage().showAndWait();
        ApplicationContext.getStageConfiguration().showExitStage();
        if(ApplicationContext.getStageConfiguration().isExit()) {
            ApplicationContext.getStreamConfiguration().stopStream();
                Platform.exit();
                System.exit(0);
            }
        }

}
