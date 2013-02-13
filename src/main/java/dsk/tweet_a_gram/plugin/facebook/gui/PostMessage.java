package dsk.tweet_a_gram.plugin.facebook.gui;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

import javax.swing.JDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsk.common.exception.DskRuntimeException;
import dsk.tweet_a_gram.core.delegates.TweetDelegate;

public class PostMessage extends JDialog implements TweetDelegate {
	private static final long serialVersionUID = -8729198020798554593L;
	private static final Logger LOG = LoggerFactory.getLogger(PostMessage.class);

	private UpdateStatusController controller;

	private String imagePath;

	private boolean init;

	private boolean tweet;

	public PostMessage() {
		LOG.trace("PostMessage.PostMessage()");
	}

	private void initUI() {
		this.tweet = false;
		if (this.init) {
			return;
		}
		// DISPOSEすると、JavaFXスレッドが終了してしまう
		this.setBounds(100, 100, 450, 300);
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
				UpdateStatusApplication app = new UpdateStatusApplication();
				app.setClassLoader(classLoader);
				fxPanel.setScene(app.createScene());
				controller = app.getController();
				controller.setMedia(imagePath);
				controller.setStageDelegate(new StageDelegate() {
					@Override
					public void hide() {
						tweet = true;
						setVisible(false);
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

	@Override
	public boolean isTweet() {
		return this.tweet;
	}

	@Override
	public String getTweet() {
		initUI();
		this.setVisible(true);
		if (!this.isTweet()) {
			return "";
		}
		return this.controller.getText();
	}

	@Override
	public void setMediaPath(String path) {
		this.imagePath = path;
	}
}
