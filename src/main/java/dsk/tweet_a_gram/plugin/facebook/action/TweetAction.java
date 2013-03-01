package dsk.tweet_a_gram.plugin.facebook.action;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.TwitterException;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.exception.ProjectNotFoundException;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;

import dsk.common.exception.DskException;
import dsk.common.exception.DskRuntimeException;
import dsk.common.exception.DskWarningException;
import dsk.common.util.R;
import dsk.tweet_a_gram.core.service.TweetService;
import dsk.tweet_a_gram.plugin.facebook.module.FacebookModule;
import dsk.tweet_a_gram.plugin.modules.PluginModule;

public class TweetAction implements IPluginActionDelegate, ActionListener {
	private static final Logger LOG = LoggerFactory.getLogger(TweetAction.class);

	@Override
	public Object run(IWindow window) throws UnExpectedException {
		execute(window.getParent());
		return null;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		try {
			Class.forName("javafx.application.Application");
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
			StringBuilder sb = new StringBuilder();
			sb.append(R.m("JavaFXを使用します。"));
			sb.append("\n");
			sb.append(R.m("\"$JAVA_HOME/lib/jfxrt.jar\"を\"$インストールフォルダ/lib/\"へコピーし、再起動してください。"));
			JOptionPane.showMessageDialog(null, sb.toString(), "Error", JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			JFrame frame = AstahAPI.getAstahAPI().getViewManager().getMainFrame();
			execute(frame);
		} catch (ClassNotFoundException | InvalidUsingException e) {
			throw new DskRuntimeException(e);
		} catch (UnExpectedException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	public static void execute(Window window) throws UnExpectedException {
		LOG.trace("run");
		Injector injector = Guice.createInjector(Stage.PRODUCTION, new FacebookModule(), new PluginModule());
		TweetService<String> tweetService = injector.getInstance(Key.get(new TypeLiteral<TweetService<String>>() {
		}));
		try {
			// プロジェクトを開いているか判断するためにgetProject()を呼んでいる
			injector.getInstance(ProjectAccessor.class).getProject();
		} catch (ProjectNotFoundException e) {
			LOG.error(e.getLocalizedMessage());
			JOptionPane.showMessageDialog(window, R.m("プロジェクトを開いていません。既存のプロジェクトを開くか、新しくプロジェクトを作成してください"), "Warning",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		try {
			tweetService.tweet();
		} catch (DskWarningException e) {
			if (TwitterException.UNAUTHORIZED == ((TwitterException) e.getCause()).getStatusCode()) {
				tweetService.getAuthService().deleteAccessToken();
			}
			LOG.warn(e.getMessage());
			JOptionPane.showMessageDialog(window, R.m("うまくつぶやけませんでした"), "Warning", JOptionPane.ERROR_MESSAGE);
		} catch (DskException e) {
			LOG.warn(e.getMessage());
			throw new UnExpectedException();
		}
	}
}
