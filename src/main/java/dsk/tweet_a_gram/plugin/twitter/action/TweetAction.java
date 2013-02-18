package dsk.tweet_a_gram.plugin.twitter.action;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Twitter;
import twitter4j.TwitterException;

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
import dsk.common.exception.DskWarningException;
import dsk.common.util.R;
import dsk.tweet_a_gram.core.service.TweetService;
import dsk.tweet_a_gram.plugin.modules.PluginModule;
import dsk.tweet_a_gram.plugin.twitter.module.TwitterModule;

public class TweetAction implements IPluginActionDelegate {
	private static final Logger LOG = LoggerFactory.getLogger(TweetAction.class);

	@Override
	public Object run(IWindow window) throws UnExpectedException {
		LOG.trace("run");
		Injector injector = Guice.createInjector(Stage.PRODUCTION, new TwitterModule(), new PluginModule());
		TweetService<Twitter> tweetService = injector.getInstance(Key.get(new TypeLiteral<TweetService<Twitter>>() {
		}));
		try {
			injector.getInstance(ProjectAccessor.class).getProject();
		} catch (ProjectNotFoundException e) {
			LOG.error(e.getLocalizedMessage());
			JOptionPane.showMessageDialog(window.getParent(), R.m("プロジェクトを開いていません。既存のプロジェクトを開くか、新しくプロジェクトを作成してください"),
					"Warning", JOptionPane.WARNING_MESSAGE);
			return null;
		}
		try {
			tweetService.tweet();
		} catch (DskWarningException e) {
			if (TwitterException.UNAUTHORIZED == ((TwitterException) e.getCause()).getStatusCode()) {
				tweetService.getAuthService().deleteAccessToken();
			}
			LOG.warn(e.getMessage());
			JOptionPane.showMessageDialog(window.getParent(), R.m("うまくつぶやけませんでした"), "Warning",
					JOptionPane.ERROR_MESSAGE);
		} catch (DskException e) {
			LOG.warn(e.getMessage());
			throw new UnExpectedException();
		}
		return null;
	}
}
