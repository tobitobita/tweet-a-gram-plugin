package dsk.tweet_a_gram.plugin.facebook.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AuthController implements Initializable {
	@FXML
	private Label label;

	@FXML
	private Button ok;

	@FXML
	private WebView webView;

	@FXML
	private AnchorPane anchorPane;

	private String accessToken;

	private String authUrl;

	private StageDelegate stageDelegate;

	private ResizeDelegate resizeDelegate;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		this.webView.setVisible(false);
		WebEngine engine = webView.getEngine();
		engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> value, State oldState, State newState) {
				if (State.SUCCEEDED != newState) {
					return;
				}
				WebEngine engine = webView.getEngine();
				Document doc = engine.getDocument();
				NodeList nodeList = doc.getElementsByTagName("BODY");
				if (nodeList == null || nodeList.getLength() != 1) {
					return;
				}
				Node body = nodeList.item(0);
				String content = body.getTextContent();
				if (!StringUtils.isEmpty(content) && !content.startsWith("Success")) {
					return;
				}
				accessToken = getAccessToken(engine.getLocation().toString());
				stageDelegate.hide();
			}
		});
	}

	@FXML
	public void handleOk(ActionEvent event) {
		this.anchorPane.getChildren().remove(this.label);
		this.anchorPane.getChildren().remove(this.ok);
		this.webView.setVisible(true);
		this.anchorPane.resize(800d, 600d);
		this.resizeDelegate.resize(800d, 600d);
		this.webView.getEngine().load(this.authUrl);
	}

	private static String getAccessToken(String url) {
		String querystr = url.substring(url.indexOf("#") + 1);
		String[] queryParams = querystr.split("&");
		for (String queryParam : queryParams) {
			String[] param = queryParam.split("=");
			if (param == null || param.length != 2) {
				continue;
			}
			if ("access_token".equals(param[0])) {
				System.out.println(param[1]);
				return param[1];
			}
		}
		return null;
	}

	public String getAccessToken() {
		return this.accessToken;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
	}

	public void setStageDelegate(StageDelegate stageDelegate) {
		this.stageDelegate = stageDelegate;
	}

	public void setResizeDelegate(ResizeDelegate resizeDelegate) {
		this.resizeDelegate = resizeDelegate;
	}

	public static void main(String[] args) {
		Application.launch(AuthApplication.class);
	}
}
