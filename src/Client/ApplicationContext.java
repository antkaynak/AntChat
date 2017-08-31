package Client;

import Client.Utility.*;

public class ApplicationContext {

    private static final StageConfiguration stageConfiguration = new StageConfiguration();
    private static final ThemeConfiguration themeConfiguration = new ThemeConfiguration();
    private static final StreamConfiguration streamConfiguration = new StreamConfiguration();
    private static final MessageConfiguration messageConfiguration = new MessageConfiguration();

    public static StageConfiguration getStageConfiguration() {
        return stageConfiguration;
    }

    public static ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }

    public static StreamConfiguration getStreamConfiguration() {
        return streamConfiguration;
    }

    public static MessageConfiguration getMessageConfiguration() {
        return messageConfiguration;
    }
}
