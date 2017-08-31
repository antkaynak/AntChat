package Client.Utility;

import Client.ApplicationContext;
import javafx.scene.Scene;

public class ThemeConfiguration {

    private String activeTheme;

    private void changeTheme(String themeName){
        activeTheme = themeName;
        changeAllScenesThemes();
    }

    private void changeAllScenesThemes() {
        for(Scene i : ApplicationContext.getStageConfiguration().getAllScenes()){
            if(i == null) continue;
            confirmTheme(i);
        }
    }

    public void confirmTheme(Scene scene){
        scene.getStylesheets().clear();
        scene.getStylesheets().add(getClass().getClassLoader().getResource(activeTheme).toExternalForm());
    }

    public void selectTheme(String themeName){
        switch (themeName){
            case "Dark":
                changeTheme("Client/Resources/css/Dark.css");
                break;
            case "Blue":
                changeTheme("Client/Resources/css/Blue.css");
                break;
            case "Green":
                changeTheme("Client/Resources/css/Green.css");
                break;
            case "Light":
                changeTheme("Client/Resources/css/Light.css");
                break;
            default:
                changeTheme("Client/Resources/css/Dark.css");

        }
    }


    public String getActiveTheme() {
        return activeTheme;
    }

    public void setActiveTheme(String activeTheme) {
        this.activeTheme = activeTheme;
    }


}
