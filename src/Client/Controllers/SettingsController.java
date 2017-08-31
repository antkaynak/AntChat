package Client.Controllers;


import Client.ApplicationContext;
import Client.Utility.StageConfiguration;
import Client.Utility.ThemeConfiguration;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;


public class SettingsController implements Initializable {

    @FXML
    private JFXButton buttonClose;

    @FXML
    private JFXComboBox<String> comboBox;

    @FXML
    private VBox settingsVBox;

    @FXML
    private RadioButton radioButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*this.stageConfiguration = ApplicationContext.getStageConfiguration();
        this.themeConfiguration = ApplicationContext.getThemeConfiguration();*/
        radioButton.setSelected(true);
        comboBox.getItems().addAll("Dark","Light","Blue","Green");
    }

    @FXML
    void RadioButtonAction(ActionEvent event) {
        if(radioButton.isSelected()){
            ApplicationContext.getStageConfiguration().setSettingsRadioButton(true);
        }
        else{
            ApplicationContext.getStageConfiguration().setSettingsRadioButton(false);
        }

    }

    @FXML
    void Select(ActionEvent event) {
        ApplicationContext.getThemeConfiguration().selectTheme(comboBox.getValue());
    }

    @FXML
    void Close(ActionEvent event) {
        ApplicationContext.getStageConfiguration().getSettingsStage().close();
    }


}
