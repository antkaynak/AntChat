package Client.Controllers;

import Client.ApplicationContext;
import Client.ChatClientThread;
import Client.Utility.LabelClear;
import Client.Utility.MessageConfiguration;
import Client.Utility.StageConfiguration;
import Client.Utility.StreamConfiguration;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import ChatBubble.BubbledLabel;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.util.Duration;
import tray.animations.AnimationType;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;


public class ClientGUI implements Initializable{


    @FXML
    private JFXHamburger buttonMenu;
    @FXML
    private JFXButton buttonSend;
    @FXML
    private ListView<HBox> listView;
    @FXML
    private ListView<HBox> listViewStatus;
    @FXML
    private JFXTextField textField;
    @FXML
    private Label label1;
    @FXML
    private HBox menuBar;
    @FXML
    private VBox vBox;
    @FXML
    private Font x3;
    @FXML
    private Color x4;
    @FXML
    private AnchorPane drawerAnchorPane;
    @FXML
    private JFXDrawer drawer;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Label nameLabel;


    private NotificationType notification;
    private TrayNotification tray;
    private HamburgerSlideCloseTransition transition;

    private String name;
    private DataOutputStream streamOut;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       // buttonSend.setGraphic(new ImageView("Client/Resources/img/enterbutton2.png"));
        streamOut = ApplicationContext.getStreamConfiguration().getStreamOut();
        name = ApplicationContext.getStreamConfiguration().getName();
        nameLabel.setText(name);

        initFields();
        initOnlineList();
        initNotification();
        new LabelClear(label1);
        new ChatClientThread(this);
        ClientNotification("Establishing connection. Please wait...");
        ClientNotification("Welcome "+ name);

    }


        @FXML //Butonun methodu
        void pressedSend(ActionEvent event) {
            if(textField.getText().startsWith("//")){
                //There is no command support yet.
                ClientNotification("Unknown Command");
            }
            else if(textField.getText() != null && !textField.getText().trim().isEmpty()){ //Prevent sending empty messages.
                Send(textField.getText());
            }
            textField.clear();
        }

        private void Send(String message){
            try{
                streamOut.writeUTF(name+": "+message);
                streamOut.flush();
                ClientMessage(message);
            }catch(IOException e){
                ClientNotification("Sending error : "+ e);
                ApplicationContext.getStreamConfiguration().stopStream();
                ApplicationContext.getStageConfiguration().showErrorStage();
            }catch (Exception e){
                ClientNotification("An error occurred. Please restart the client...");
                ApplicationContext.getStreamConfiguration().stopStream();
                ApplicationContext.getStageConfiguration().showErrorStage();
            }
        }

        private void ClientNotification(String text){
            ApplicationContext.getMessageConfiguration().getClientNotification(listView, text);

        }

        private void ClientMessage(String text){
                ApplicationContext.getMessageConfiguration().getClientRespond(listView,text);
        }


        private void ServerMessage(String text){
                ApplicationContext.getMessageConfiguration().getServerRespond(listView, text);
                //Check if the stage is iconified or tabbed. If so show notification.
                if( ApplicationContext.getStageConfiguration().getClientGUIStage().isIconified()
                    || !(ApplicationContext.getStageConfiguration().getClientGUIStage().isFocused() )&& ApplicationContext.getStageConfiguration().isSettingsRadioButton() ){
                tray.setTray("A New Message!",text, NotificationType.NOTICE);
                tray.showAndDismiss(Duration.seconds(1));
            }

        }


    private void initFields(){
        listView.setOnMousePressed(e-> {
            if(drawer.isShown()){
                drawer.close();
                transition.setRate(-1);
                transition.play();
            }

        });
        textField.setOnMousePressed(e-> {
            if(drawer.isShown()){
                drawer.close();
                transition.setRate(-1);
                transition.play();
            }
        });

        //Get focus except on control , shift, alt and capslock keys.
        listView.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.CONTROL && event.getCode() != KeyCode.SHIFT && event.getCode() != KeyCode.ALT
                    && event.getCode() != KeyCode.CAPS)
                textField.requestFocus();

        });

        //Textfield limit is 240 characters.
        textField.lengthProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.intValue() > oldValue.intValue()){
                if(textField.getText().length() >= 240){
                    textField.setText(textField.getText().substring(0,240));
                }
            }
        });

        drawer.toFront();
        drawer.setSidePane(ApplicationContext.getStageConfiguration().initMenu());
        transition = new HamburgerSlideCloseTransition(buttonMenu);
        transition.setRate(-1);
        buttonMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, (e)->{
            transition.setRate(transition.getRate()*-1);
            transition.play();
            if(!drawer.isShown()){
                drawer.setPrefSize(listViewStatus.getWidth(),listViewStatus.getHeight());
                drawer.open();
            }
            else{
                drawer.close();
            }
        });

        textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                buttonSend.fire();
            }
            else if(event.getCode() == KeyCode.BACK_SPACE){
                return;
            }
            else{
                try {
                    streamOut.writeUTF("//typing");
                } catch (IOException e) {
                    ApplicationContext.getStageConfiguration().showErrorStage();
                }
            }

        });
        Platform.runLater(()-> textField.requestFocus());

    }

    private void initNotification(){
        notification = NotificationType.NOTICE;
        tray = new TrayNotification();
        tray.setAnimationType(AnimationType.POPUP);
        tray.setRectangleFill(Paint.valueOf("#FFFFFF"));
        tray.setNotificationType(notification);

    }

    private void initOnlineList(){
        //You can convert this method to FXML.
        HBox x = new HBox();
        Label l1 = new Label();
        l1.setText("Online List");
        l1.setTextFill(Color.WHITE);
        l1.setFont(new Font(17));
        l1.setMaxSize(500,500);
        l1.setCursor(Cursor.CLOSED_HAND);
        x.setAlignment(Pos.CENTER);
        x.getChildren().add(l1);
        listViewStatus.getItems().add(x);
    }

    private void statusListConnect(String message){
        HBox x = new HBox();
        Label l1 = new Label();
        l1.setText(message);
        l1.setTextFill(Color.GHOSTWHITE);
        l1.setFont(new Font(14));
        l1.setMaxSize(500,500);
        l1.setCursor(Cursor.HAND);
        x.setAlignment(Pos.CENTER);
        x.getChildren().add(l1);
        listViewStatus.getItems().add(x);
    }

    private void statusListDisconnect(String message){
        int who = Integer.parseInt(message);
        listViewStatus.getItems().remove(who);
    }


    //This method gets handled by ChatClientThread when a new message is received.
    public void handle(String msg){
        //When someone is typing display it. There is no support for multiple user typing notification.
        if(msg.startsWith("//info-")){
            String[] message = msg.split("-");
            Platform.runLater(()-> label1.setText(message[1]));
        }
        else if(msg.startsWith("//whoisonline")){
            String[] message = msg.split("-");
            //whoisonline gets called when the application loaded. That means there are multiple names received.
            //Skip whoisonline so method does not add an user named //whoisonline.
            for(String i : message){
                if(i.equals("//whoisonline")){continue;}
                Platform.runLater(() -> statusListConnect(i));
            }
        }
        else if(msg.startsWith("//status-")){
            String[] message = msg.split("-");
            Platform.runLater(() -> statusListConnect(message[1]));

        }
        else if(msg.startsWith("//statusdisconnect-")){
            String[] message = msg.split("-");
            Platform.runLater(() -> statusListDisconnect(message[1]));
        }
        else{

            Platform.runLater(()-> label1.setText(""));
           Platform.runLater(()-> ServerMessage(msg));
        }

    }

}





















