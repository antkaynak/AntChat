package Client.Utility;

import ChatBubble.Bubble;
import ChatBubble.BubbleSpec;
import ChatBubble.BubbledLabel;
import Client.ApplicationContext;
import Client.Main;
import com.jfoenix.effects.JFXDepthManager;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageConfiguration {


    private List<String> extractUrls(String text){
        List<String> containedUrls = new ArrayList<String>();
        //Regex for capturing URLs.
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);
        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }
        return containedUrls;
    }

    private String linkBox(ListView listView, String text, String color, boolean server){
        List<String> extractedUrls = extractUrls(text);
        for (String url : extractedUrls)
        {
            text = text.replace(url,"");
            Hyperlink hyperlink = new Hyperlink(url);
            hyperlink.setOnAction(t -> new Main().link(hyperlink.getText()));
            hyperlink.setStyle("-fx-background-color: "+color);
            hyperlink.setWrapText(true);
            hyperlink.setMaxSize(250,500);
            hyperlink.setFont(new Font(12));
            HBox hBox = new HBox();
            hBox.setMaxWidth(listView.getWidth()-20);
            String name;

            //If request comes from the server message get the name from the message with split then wrap it with the link.
            if(server){
                name = text.split(":")[0];
                hBox.setAlignment(Pos.TOP_LEFT);
                HBox hBoxWrapper = new HBox();
                BubbledLabel linkName = new BubbledLabel(name+": ");
                linkName.setStyle("-fx-background-color: "+color);
                linkName.setWrapText(true);
                linkName.setMaxSize(250,500);
                linkName.setFont(new Font(12));
                linkName.setBubbleSpec(BubbleSpec.FACE_LEFT_CENTER);
                hBoxWrapper.setMaxWidth(listView.getWidth()-20);
                hBoxWrapper.setAlignment(Pos.TOP_CENTER);
                hBoxWrapper.getChildren().add(linkName);
                hBox.getChildren().addAll(linkName, hyperlink);

                //If request comes from the client get the name from application context, wrap it with the link and then add a tail to the end.
            }else{
                name = ApplicationContext.getStreamConfiguration().getName();
                hBox.setAlignment(Pos.TOP_RIGHT);
                HBox hBoxWrapper = new HBox();
                Label linkName = new Label(" "+name+": ");
                linkName.setStyle("-fx-background-color: "+color);
                linkName.setWrapText(true);
                linkName.setMaxSize(250,500);
                linkName.setFont(new Font(12));
                hBoxWrapper.setMaxWidth(listView.getWidth()-20);
                hBoxWrapper.setAlignment(Pos.TOP_CENTER);
                hBoxWrapper.getChildren().add(linkName);
                BubbledLabel tail = new BubbledLabel("  ");
                tail.setStyle("-fx-background-color: "+color);
                tail.setWrapText(true);
                tail.setFont(new Font(12));
                tail.setMaxSize(250,500);
                tail.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
                hBox.getChildren().addAll(linkName, hyperlink,tail);
            }

            JFXDepthManager.setDepth(hBox,2);
            listView.getItems().add(hBox);
            listView.scrollTo(listView.getItems().lastIndexOf(hBox));

        }

        return text;
    }

    private HBox createBubble(ListView listView, String text, String color, Pos pos, BubbleSpec bubbleSpec){
        HBox hBox = new HBox();
        BubbledLabel b1 = new BubbledLabel();
        b1.setText(text);
        b1.setFont(new Font(12));
        b1.setMaxSize(250, 500);
        b1.setCursor(Cursor.HAND);
        b1.setStyle("-fx-background-color: "+color);
        b1.setWrapText(true);
        hBox.setMaxWidth(listView.getWidth() - 20);
        hBox.setAlignment(pos);
        b1.setBubbleSpec(bubbleSpec);
        hBox.getChildren().add(b1);
        JFXDepthManager.setDepth(hBox, 2);
        return  hBox;
    }

    public void getClientNotification(ListView listView, String text){
        HBox hBox = new HBox();
        BubbledLabel b1 = new BubbledLabel();
        b1.setText(text);
        b1.setFont(new Font(14));
        b1.setMaxSize(500,500);
        b1.setCursor(Cursor.HAND);
        b1.setBackground(new Background(new BackgroundFill(Color.GHOSTWHITE,null,null)));
        hBox.setAlignment(Pos.CENTER);
        b1.setBubbleSpec(BubbleSpec.FACE_RIGHT_CENTER);
        hBox.getChildren().add(b1);
        listView.getItems().add(hBox);
        listView.scrollTo(listView.getItems().lastIndexOf(hBox));
    }

    public void getClientRespond(ListView listView, String text){
        String name = ApplicationContext.getStreamConfiguration().getName();
        String msg = linkBox(listView, text, "lightgreen", false);
        if(msg.trim().isEmpty()){  //If the message after the link is empty simply do not send a blank message.
            return;
        }
        HBox hBox = createBubble(listView, name + ": "+ msg , "lightgreen", Pos.TOP_RIGHT, BubbleSpec.FACE_RIGHT_CENTER);
        listView.getItems().add(hBox);
        listView.scrollTo(listView.getItems().lastIndexOf(hBox));
    }

    public void getServerRespond(ListView listView, String text){
        String msg = linkBox(listView, text, "white", true);
        if(msg.trim().isEmpty() || msg.split(":")[1].trim().isEmpty()){ //If the message after the link is empty simply do not send a blank message.
            return;
        }
        HBox hBox = createBubble(listView, " "+ msg , "white", Pos.TOP_LEFT, BubbleSpec.FACE_LEFT_CENTER);
        listView.getItems().add(hBox);
        listView.scrollTo(listView.getItems().lastIndexOf(hBox));
    }





}
