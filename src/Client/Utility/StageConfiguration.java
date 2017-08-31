package Client.Utility;

import Client.ApplicationContext;
import Client.Controllers.MenuController;
import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StageConfiguration {


    private Stage loginGUIStage;
    private Scene loginGUIScene;

    private Stage clientGUIStage;
    private Scene clientGUIScene;

    private Stage settingsStage;
    private Scene settingsScene;
    private boolean settingsRadioButton;

    private Stage connectionStage;
    private Scene connectionScene;

    private Stage aboutStage;
    private Scene aboutScene;

    private Stage errorStage;
    private Scene errorScene;
    private Label errorMessage;

    private Stage exitStage;
    private Scene exitScene;
    private boolean exit;

    public StageConfiguration(){
        initError();
        initLoginGUI();
        initExit();
        initSettings();
        initAbout();

        //initClientGUI(); This happens in LoginGUI controller.
        //If I init ClientGUI here Streams and Sockets are going to be null.
    }

    private Stage setStageXY(Stage stage){
        stage.setX(clientGUIStage.getX() + clientGUIStage.getWidth() / 3d);
        stage.setY(clientGUIStage.getY() + clientGUIStage.getHeight() / 4d);
        return stage;
    }

    public void showExitStage(){
        //setStageXY(exitStage).showAndWait();
        exitStage.setX(clientGUIStage.getX() + clientGUIStage.getWidth() / 2.5d);
        exitStage.setY(clientGUIStage.getY() + clientGUIStage.getHeight() / 4d);
        exitStage.showAndWait();
    }

    public void showErrorStage(){
        this.errorMessage.setText("An error occurred. Please restart the client and try again...");
        if(clientGUIStage == null){
            errorStage.showAndWait();
            return;
        }
        setStageXY(errorStage).showAndWait();
    }

    public void showErrorStage(String errorMessage){
        this.errorMessage.setText(errorMessage);
        if(clientGUIStage == null){
            errorStage.showAndWait();
            return;
        }
        setStageXY(errorStage).showAndWait();
    }

    public void showAboutStage(){
        setStageXY(aboutStage).showAndWait();
    }

    public void showConnectionStage(){
        setStageXY(connectionStage).showAndWait();}

    public void showSettingsStage(){
        setStageXY(settingsStage).showAndWait();
    }




    private void initLoginGUI(){
       FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/LoginGUI.fxml"));
        try {
           Parent loginGUIRoot = (Parent) fxmlLoader.load();
            loginGUIStage = new Stage();
            loginGUIStage.setTitle("AntChat v1 Login");
            addIcon(loginGUIStage);
            loginGUIScene = new Scene(loginGUIRoot, 508, 360);
            //Default dark theme
            loginGUIScene.getStylesheets().add(getClass().getClassLoader().getResource("Client/Resources/css/Dark.css").toExternalForm());
            loginGUIStage.setScene(loginGUIScene);
            loginGUIStage.setResizable(false);
        }catch (Exception e) {
            showErrorStage();
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void initClientGUI(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/ClientGUI.fxml"));
            Parent clientGUIRoot = (Parent) fxmlLoader.load();
            clientGUIStage = new Stage();
            addIcon(clientGUIStage);
            clientGUIStage.setOnCloseRequest(e -> {
                e.consume();
                showExitStage();
                if (exit) {
                    try {
                        ApplicationContext.getStreamConfiguration().stopStream();
                        Platform.exit();
                        System.exit(0);
                    }catch(Exception ex){
                        ex.printStackTrace();
                        System.exit(0);

                    }

                }
            });
            clientGUIStage.setTitle("AntChat v1");
            clientGUIScene = new Scene(clientGUIRoot);
            //Responsive menu slider
            clientGUIScene.heightProperty().addListener(((observableValue, oldSceneHeight, newSceneHeight) -> MenuController.staticmenuFxmlVBox.setPrefHeight((Double) newSceneHeight - 65)));
            clientGUIStage.setScene(clientGUIScene);
        }catch(Exception e){
            errorStage.showAndWait();
            e.printStackTrace();
            System.exit(0);

        }
    }

    public VBox initMenu(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/Menu.fxml"));
            loader.setController(new MenuController());
            return loader.load();
        }catch (Exception e){
            showErrorStage();
            e.printStackTrace();
        }
        return null;
    }


    private void initSettings(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/Settings.fxml"));
        try {
            Parent settingsRoot = (Parent) fxmlLoader.load();
            settingsStage = new Stage();
            settingsStage.initStyle(StageStyle.UNDECORATED);
            settingsStage.setResizable(false);
            settingsStage.setTitle("Settings AntChat v1");
            settingsRadioButton = true;
            settingsScene = new Scene(settingsRoot);
            settingsStage.setScene(settingsScene);
            settingsStage.setAlwaysOnTop(true);

        } catch (IOException e) {
            showErrorStage();
            e.printStackTrace();
        }

    }

    public void initConnection(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/Connection.fxml"));
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/Connection.fxml"));
        try {
            Parent connectionRoot = (Parent) fxmlLoader.load();
            connectionStage = new Stage();
            connectionStage.initStyle(StageStyle.UNDECORATED);
            connectionStage.setResizable(false);
            connectionStage.setTitle("Connection AntChat v1");
            connectionScene = new Scene(connectionRoot);
            connectionStage.setScene(connectionScene);
            connectionStage.setAlwaysOnTop(true);
        } catch (IOException e) {
            showErrorStage();
            e.printStackTrace();
        }
    }

    private void initAbout(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("Client/Views/About.fxml"));
        try {
            Parent aboutRoot = (Parent) fxmlLoader.load();
            aboutStage = new Stage();
            aboutStage.initStyle(StageStyle.UNDECORATED);
            aboutStage.setResizable(false);
            aboutStage.setTitle("About AntChat v1");
            aboutScene = new Scene(aboutRoot);
            aboutStage.setScene(aboutScene);
            aboutStage.setAlwaysOnTop(true);
        } catch (IOException e) {
            showErrorStage();
            e.printStackTrace();
        }

    }

    private void initError(){
        errorStage = new Stage();
        errorStage.initModality(Modality.APPLICATION_MODAL);
        errorStage.setTitle("Error");
        addIcon(errorStage);
        errorMessage = new Label("An error occurred. Please restart the client and try again...");
        Button closeButton = new Button("Close the window");
        closeButton.setOnAction(e -> errorStage.close());
        VBox layout = new VBox(40);
        layout.setPrefSize(350,250);
        layout.getChildren().addAll(errorMessage,closeButton);
        layout.setAlignment(Pos.CENTER);
        errorScene = new Scene(layout);
        errorStage.setScene(errorScene);
    }

    private void initExit(){
        exitStage = new Stage();
        exitStage.initModality(Modality.APPLICATION_MODAL);
        exitStage.initStyle(StageStyle.UNDECORATED);
        Label label = new Label("Sure you want to exit?");
        label.setTextFill(Color.WHITE);
        label.setFont(new Font(18));
        Button yesButton  = new JFXButton("Yes");
        yesButton.setPrefWidth(100);
        yesButton.setStyle("-fx-background-color: #FFFFFF");
        Button noButton = new JFXButton("No");
        noButton.setPrefWidth(100);
        noButton.setStyle("-fx-background-color: #FFFFFF");
        yesButton.setOnAction(e -> {
            exit = true;
            exitStage.close();
        });

        noButton.setOnAction(e -> {
            exit = false;
            exitStage.close();
        });
        VBox layout = new VBox(20);
        layout.setId("exitBox");
        layout.setPrefSize(250,250);
        layout.getChildren().addAll(label,yesButton,noButton);
        layout.setAlignment(Pos.CENTER);
        exitScene = new Scene(layout,250,250);
        exitStage.setScene(exitScene);

    }

    List<Scene> getAllScenes(){
        List<Scene> tempList = new ArrayList<Scene>();
        tempList.add(loginGUIScene);
        tempList.add(clientGUIScene);
        tempList.add(settingsScene);
        tempList.add(aboutScene);
        tempList.add(connectionScene);
        tempList.add(errorScene);
        tempList.add(exitScene);


        return tempList;

    }

    private void addIcon(Stage stage){
        stage.getIcons().add(new Image("Client/Resources/img/logo.png"));
    }

    public Stage getLoginGUIStage() {
        return loginGUIStage;
    }

    public void setLoginGUIStage(Stage loginGUIStage) {
        this.loginGUIStage = loginGUIStage;
    }

    public Scene getLoginGUIScene() {
        return loginGUIScene;
    }

    public void setLoginGUIScene(Scene loginGUIScene) {
        this.loginGUIScene = loginGUIScene;
    }

    public Stage getClientGUIStage() {
        return clientGUIStage;
    }

    public void setClientGUIStage(Stage clientGUIStage) {
        this.clientGUIStage = clientGUIStage;
    }

    public Scene getClientGUIScene() {
        return clientGUIScene;
    }

    public void setClientGUIScene(Scene clientGUIScene) {
        this.clientGUIScene = clientGUIScene;
    }

    public Stage getAboutStage() {
        return aboutStage;
    }

    public void setAboutStage(Stage aboutStage) {
        this.aboutStage = aboutStage;
    }

    public Scene getAboutScene() {
        return aboutScene;
    }

    public void setAboutScene(Scene aboutScene) {
        this.aboutScene = aboutScene;
    }

    public Stage getSettingsStage() {
        return settingsStage;
    }

    public void setSettingsStage(Stage settingsStage) {
        this.settingsStage = settingsStage;
    }

    public Scene getSettingsScene() {
        return settingsScene;
    }

    public void setSettingsScene(Scene settingsScene) {
        this.settingsScene = settingsScene;
    }

    public Stage getErrorStage() {
        return errorStage;
    }

    public void setErrorStage(Stage errorStage) {
        this.errorStage = errorStage;
    }

    public Scene getErrorScene() {
        return errorScene;
    }

    public void setErrorScene(Scene errorScene) {
        this.errorScene = errorScene;
    }

    public Label getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(Label errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Stage getConnectionStage() {
        return connectionStage;
    }

    public void setConnectionStage(Stage connectionStage) {
        this.connectionStage = connectionStage;
    }

    public Scene getConnectionScene() {
        return connectionScene;
    }

    public void setConnectionScene(Scene connectionScene) {
        this.connectionScene = connectionScene;
    }


    public Stage getExitStage() {
        return exitStage;
    }

    public void setExitStage(Stage exitStage) {
        this.exitStage = exitStage;
    }

    public Scene getExitScene() {
        return exitScene;
    }

    public void setExitScene(Scene exitScene) {
        this.exitScene = exitScene;
    }

    public boolean isExit() {
        return exit;
    }

    public void setExit(boolean exit) {
        this.exit = exit;
    }

    public boolean isSettingsRadioButton() {
        return settingsRadioButton;
    }

    public void setSettingsRadioButton(boolean settingsRadioButton) {
        this.settingsRadioButton = settingsRadioButton;
    }


}
