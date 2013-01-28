package dsk.tweet_a_gram.plugin.facebook.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AuthController implements Initializable {
	@FXML
	private TextField url;
	@FXML
	private WebView webView;

	private String accessToken;

	private String authUrl;

	private StageDelegate stageDelegate;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		System.out.println("AuthContorller.initialize()");
		webView.getEngine().getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> value, State oldState, State newState) {
				System.out.println("\t" + value);
				System.out.println("\t" + oldState);
				System.out.println("\t" + newState);
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
				if (!"Success".equals(body.getTextContent())) {
					return;
				}
				stageDelegate.hide();
				System.out.println(engine.getLocation());
				accessToken = getAccessToken(engine.getLocation().toString());
			}
		});
	}

	@FXML
	private void handleGo(ActionEvent event) {
		System.out.println(url.getText());
		WebEngine engine = this.webView.getEngine();
		engine.load(this.authUrl);
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
}
