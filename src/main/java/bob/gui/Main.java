package bob.gui;

import java.io.IOException;
import java.nio.file.Path;

import bob.Bob;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Displays Bob's JavaFX user interface.
 */
public class Main extends Application {
    private static final double WINDOW_WIDTH = 430;
    private static final double WINDOW_HEIGHT = 700;

    private final Bob bob = new Bob(Path.of("data", "bob.txt"));

    /**
     * Creates and displays Bob's main window.
     *
     * @param stage Primary stage supplied by JavaFX.
     * @throws IOException If the main-window FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane mainWindow = fxmlLoader.load();
        fxmlLoader.<MainWindow>getController().setBob(bob);

        Scene scene = new Scene(mainWindow);
        stage.setTitle("Bob");
        stage.setMinWidth(380);
        stage.setMinHeight(560);
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
