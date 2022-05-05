import java.net.URL;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;

public class MainWeb extends Application {

    private WebView webView = new WebView();

    private WebEngine engine = webView.getEngine();

    private Button home = new Button("H");

    private Button refresh = new Button("R");

    private Button back = new Button("Back");

    private Button search = new Button("Search");

    private Button forward = new Button("Forward");

    private Button zoomIn = new Button("+");

    private Button zoomOut = new Button("-");

    private TextField textField = new TextField();

    private TextArea textArea = new TextArea();

    private ProgressBar progressBar = new ProgressBar();

    private WebHistory history;

    private double Valuezoom = 1.0;

    private String URL = textField.getText();

    @Override
    public void start(Stage stage) throws Exception {

        webView.setLayoutX(11);
        webView.setLayoutY(64);
        webView.setPrefHeight(725);
        webView.setPrefWidth(1225);

        textField.setLayoutX(195);
        textField.setLayoutY(14);
        textField.setPrefHeight(25);
        textField.setPrefWidth(913);

        progressBar.setLayoutX(11);
        progressBar.setLayoutY(46);
        progressBar.setPrefHeight(18);
        progressBar.setPrefWidth(1225);

        Home();
        home.setLayoutX(94);
        home.setLayoutY(14);

        Refresh();
        refresh.setLayoutX(61);
        refresh.setLayoutY(14);

        Back();
        back.setLayoutX(14);
        back.setLayoutY(14);

        Forward();
        forward.setLayoutX(128);
        forward.setLayoutY(14);

        Search();
        EnterSearch();
        SourceCode();
        search.setLayoutX(1114);
        search.setLayoutY(14);

        ZoomIn();
        zoomIn.setLayoutX(1173);
        zoomIn.setLayoutY(14);
        zoomIn.setPrefHeight(25);
        zoomIn.setPrefWidth(26);

        ZoomOut();
        zoomOut.setLayoutX(1207);
        zoomOut.setLayoutY(14);
        zoomOut.setPrefHeight(25);
        zoomOut.setPrefWidth(26);

        // AnchorPane
        AnchorPane pane = new AnchorPane();
        pane.prefHeight(200);
        pane.prefWidth(200);
        pane.getChildren().addAll(webView, home, refresh, back, forward, search, zoomIn, zoomOut, textField,
                progressBar);

        // Tab Web
        Tab Tab1 = new Tab("Web");
        Tab1.setContent(pane);
        Tab1.setClosable(false);

        Tab Tab2 = new Tab("Source Code Web");
        Tab2.setContent(textArea);
        Tab2.setClosable(false);

        // Tab Source Code
        TabPane tabPane = new TabPane();
        tabPane.prefHeight(200);
        tabPane.prefWidth(200);
        tabPane.getTabs().addAll(Tab1, Tab2);

        // Display
        Scene scene = new Scene(tabPane);
        stage.setTitle("WebView");
        stage.setScene(scene);
        stage.setHeight(860);
        stage.setWidth(1265);
        stage.setTitle("ONEPIECE WEB");
        stage.getIcons().add(new Image("icon.jpg"));
        stage.show();

    }

    public void Home() {
        home.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent Event) {
                engine.load("https://www.google.com");
                textField.setText("www.google.com");
                progressBar.progressProperty().bind(engine.getLoadWorker().progressProperty());
            }
        });
    }

    public void Refresh() {
        refresh.setOnAction(e -> {
            engine.reload();
        });

        webView.setOnKeyPressed(Event -> {
            if (Event.getCode() == KeyCode.F5) {
                engine.reload();
            }
        });
    }

    public void ZoomIn() {
        zoomIn.setOnAction(e -> {
            webView.setZoom(Valuezoom += 0.25);

        });
    }

    public void ZoomOut() {
        zoomOut.setOnAction(e -> {
            webView.setZoom(Valuezoom -= 0.25);
        });
    }

    public void Back() {
        history = engine.getHistory();
        ObservableList<WebHistory.Entry> entries = history.getEntries();
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                history.go(-1);
                textField.setText(entries.get(history.getCurrentIndex()).getUrl());
                progressBar.progressProperty().bind(engine.getLoadWorker().progressProperty());
            }
        });

    }

    public void Forward() {
        history = engine.getHistory();
        forward.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<WebHistory.Entry> entries = history.getEntries();
                history.go(1);
                textField.setText(entries.get(history.getCurrentIndex()).getUrl());
                progressBar.progressProperty().bind(engine.getLoadWorker().progressProperty());
            }
        });

    }

    public void Search() {
        search.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent Event) {
                if (URL.contains(".")) {
                    if (isValid(URL) == true) {
                        engine.load(textField.getText());
                    } else {
                        engine.load("https://" + URL);
                    }
                } else {
                    engine.load("https://www.google.com/search?q=" + URL);
                }
                progressBar.progressProperty().bind(engine.getLoadWorker().progressProperty());
            }
        });
    }

    public void EnterSearch() {
        textField.setOnKeyPressed(Event -> {
            if (Event.getCode() == KeyCode.ENTER) {
                if (URL.contains(".")) {
                    if (isValid(URL) == true) {
                        engine.load(URL);
                    } else {
                        engine.load("https://" + URL);
                    }
                } else {
                    engine.load("https://www.google.com/search?q=" + URL);
                }
                progressBar.progressProperty().bind(engine.getLoadWorker().progressProperty());
            }
        });
    }

    public void SourceCode() {
        webView.setOnKeyPressed(Event -> {
            if (Event.getCode() == KeyCode.F12) {
                textArea.clear();
                textArea.setText(engine.executeScript("document.documentElement.outerHTML").toString());
            }
        });
    }

    public boolean isValid(String url) {
        try {
            new URL(url).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}