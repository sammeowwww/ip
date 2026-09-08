package bob.gui;

import java.util.Objects;

import bob.Bob;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls Bob's main chat window.
 */
public class MainWindow extends AnchorPane {
    private static final double BOTTOM_SCROLL_POSITION = 1.0;

    private final Image bobAvatar = new Image(Objects.requireNonNull(
            getClass().getResourceAsStream("/images/bob.png")));

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private Bob bob;

    /**
     * Configures behavior that is available after FXML injection.
     */
    @FXML
    public void initialize() {
        assertFxmlFieldsAreInjected();
        enableAutomaticScrolling();
    }

    /**
     * Documents the fields that the FXML loader must inject.
     */
    private void assertFxmlFieldsAreInjected() {
        assert scrollPane != null : "scrollPane must be injected by FXMLLoader";
        assert dialogContainer != null : "dialogContainer must be injected by FXMLLoader";
        assert userInput != null : "userInput must be injected by FXMLLoader";
        assert sendButton != null : "sendButton must be injected by FXMLLoader";
    }

    /**
     * Keeps the latest conversation messages visible as the dialog grows.
     */
    private void enableAutomaticScrolling() {
        dialogContainer.heightProperty().addListener(
                observable -> scrollPane.setVvalue(BOTTOM_SCROLL_POSITION));
    }

    /**
     * Supplies the chatbot used to answer commands.
     *
     * @param bob Chatbot backing this window.
     */
    public void setBob(Bob bob) {
        this.bob = bob;
        showBobMessage(bob.getStartupMessage());
        userInput.requestFocus();
    }

    /**
     * Sends the entered command to Bob and displays both sides of the conversation.
     */
    @FXML
    public void submitUserCommand() {
        String userCommand = userInput.getText().trim();
        if (userCommand.isEmpty() || bob == null) {
            return;
        }

        String bobResponse = bob.executeUserCommand(userCommand);
        showConversationTurn(userCommand, bobResponse);
        userInput.clear();
    }

    /**
     * Displays a user command followed by Bob's response.
     *
     * @param userCommand User command to display.
     * @param bobResponse Bob response to display.
     */
    private void showConversationTurn(String userCommand, String bobResponse) {
        dialogContainer.getChildren().addAll(
                DialogBox.createUserDialog(userCommand),
                DialogBox.createBobDialog(bobResponse, bobAvatar));
    }

    /**
     * Displays a message from Bob with Bob's avatar.
     *
     * @param message Message to display.
     */
    private void showBobMessage(String message) {
        dialogContainer.getChildren().add(DialogBox.createBobDialog(message, bobAvatar));
    }
}
