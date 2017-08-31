package Client.Controllers;

import Client.ApplicationContext;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class ConnectionController implements Initializable{

    @FXML
    private JFXButton closeButton;

    @FXML
    private Label portAddrLabel;

    @FXML
    private Label hostAddrLabel;

    @FXML
    void Close(ActionEvent event) {
        ApplicationContext.getStageConfiguration().getConnectionStage().close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        String hostAddress = ApplicationContext.getStreamConfiguration().getHostAddr();
        int port = ApplicationContext.getStreamConfiguration().getPortAddr();
        hostAddrLabel.setText("Host Address  " + hostAddress);
        portAddrLabel.setText("Port Address  "+ String.valueOf(port));
    }
}

