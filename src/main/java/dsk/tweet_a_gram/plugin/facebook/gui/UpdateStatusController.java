package dsk.tweet_a_gram.plugin.facebook.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateStatusController implements Initializable {
	private static final Logger LOG = LoggerFactory.getLogger(UpdateStatusController.class);
	@FXML
	private Button tweetButton;
	@FXML
	private TextArea inputText;
	@FXML
	private ImageView image;

	private StageDelegate stageDelegate;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		LOG.trace("initialize");
	}

	@FXML
	public void handleTweet(ActionEvent event) {
		LOG.trace("handleTweet : {}", event.toString());
		LOG.trace("inputText : {}", this.inputText.getText());
		this.stageDelegate.hide();
	}

	public String getText() {
		return this.inputText.getText();
	}

	public void setStageDelegate(StageDelegate stageDelegate) {
		this.stageDelegate = stageDelegate;
	}

	public void setMedia(String path) {
		try {
			this.image.setImage(new Image(new FileInputStream(new File(path))));
		} catch (FileNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public static void main(String[] args) {
		Application.launch(UpdateStatusApplication.class);
	}
}
