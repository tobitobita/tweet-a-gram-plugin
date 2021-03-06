package dsk.tweet_a_gram.plugin.facebook.gui;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import javax.swing.JDialog;
import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsk.common.exception.DskRuntimeException;
import dsk.tweet_a_gram.core.delegates.AuthDelegate;

public class FacebookAuthorization extends JDialog implements AuthDelegate {
	private static final long serialVersionUID = -1092669266633396872L;

	private static final Logger LOG = LoggerFactory.getLogger(FacebookAuthorization.class);

	private AuthController controller;

	private boolean init;

	private String authUrl;

	public FacebookAuthorization() {
		super();
		LOG.trace("FacebookAuthorization.FacebookAuthorization()");
	}

	@Override
	public String doAuthTwitter(String authUrl) {
		this.authUrl = authUrl;
		initUI();
		this.setVisible(true);
		return this.controller.getAccessToken();
	}

	private void initUI() {
		if (this.init) {
			return;
		}
		// DISPOSEすると、JavaFXスレッドが終了してしまう
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setBounds(100, 100, 310, 126);
		this.setModal(true);

		final ClassLoader classLoader = getClass().getClassLoader();
		// JavaFXのコンポーネントを貼り付けるPanel
		final JFXPanel fxPanel = new JFXPanel();
		this.add(fxPanel);
		this.init = true;
		// JavaFXのThreadを使用すること
		FutureTask<Void> futureTask = new FutureTask<Void>(new Callable<Void>() {
			@Override
			public Void call() throws Exception {
				AuthApplication app = new AuthApplication();
				app.setClassLoader(classLoader);
				fxPanel.setScene(app.createScene());
				controller = app.getController();
				controller.setAuthUrl(authUrl);
				controller.setStageDelegate(new StageDelegate() {
					@Override
					public void hide() {
						setVisible(false);
					}
				});
				controller.setResizeDelegate(new ResizeDelegate() {
					@Override
					public void resize(double width, double height) {
						setSize((int) width, (int) height);
					}
				});
				return null;
			}
		});
		Platform.runLater(futureTask);
		try {
			futureTask.get();
		} catch (InterruptedException e) {
			throw new DskRuntimeException(e);
		} catch (ExecutionException e) {
			throw new DskRuntimeException(e);
		}
	}
}
