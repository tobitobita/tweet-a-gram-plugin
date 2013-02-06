package dsk.tweet_a_gram.plugin.facebook.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class UpdateStatusController implements Initializable {
	@FXML
	private Button tweetButton;
	@FXML
	private TextArea inputText;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		System.out.println("UpdateStatusController.initialize()");
	}

	@FXML
	public void handleTweet(ActionEvent event) {
		System.out.println("UpdateStatusController.handleTweet()");
		System.out.println(this.inputText.getText());
	}

	public static void main(String[] args) {
		Application.launch(UpdateStatusApplication.class);
	}
}
