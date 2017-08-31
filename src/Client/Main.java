package Client;



import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {


    public static Boolean isSplashLoaded = false;

    public void link(String message){
        getHostServices().showDocument(message);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
       ApplicationContext.getStageConfiguration().getLoginGUIStage().show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
