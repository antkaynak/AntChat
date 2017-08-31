package Client.Controllers;



import Client.ApplicationContext;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.ResourceBundle;


public class AboutController {

    @FXML
    private JFXButton buttonClose;


    @FXML
    void Close(ActionEvent event) {
        ApplicationContext.getStageConfiguration().getAboutStage().close();
    }


}
