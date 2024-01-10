import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class SimpleWebBrowserJavaFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple Web Browser");

        TextField urlTextField = new TextField();
        Button loadButton = new Button("Load");

        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        loadButton.setOnAction(e -> {
            String url = urlTextField.getText();
            if (!url.isEmpty()) {
                Platform.runLater(() -> webEngine.load(url));
            }
        });

        BorderPane root = new BorderPane();
        root.setTop(urlTextField);
        root.setCenter(webView);
        root.setBottom(loadButton);

        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        primaryStage.show();
    }
}
