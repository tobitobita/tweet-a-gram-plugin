package dsk.tweet_a_gram.plugin.action.facebook;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Twitter;

import com.change_vision.jude.api.inf.ui.IPluginActionDelegate;
import com.change_vision.jude.api.inf.ui.IWindow;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;

import dsk.common.util.R;
import dsk.tweet_a_gram.core.service.TweetService;
import dsk.tweet_a_gram.plugin.modules.TwitterModule;
import dsk.tweet_a_gram.plugin.modules.PluginModule;

public class LogoutAction implements IPluginActionDelegate {
	private static final Logger LOG = LoggerFactory.getLogger(LogoutAction.class);

	@Override
	public Object run(IWindow window) throws UnExpectedException {
		int result = JOptionPane.showConfirmDialog(null, R.m("ログアウトします。よろしいですか？"), "", JOptionPane.OK_CANCEL_OPTION);
		switch (result) {
		case JOptionPane.OK_OPTION:
			Injector injector = Guice.createInjector(Stage.PRODUCTION, new TwitterModule(), new PluginModule());
			TweetService<Twitter> tweetService = injector.getInstance(Key.get(new TypeLiteral<TweetService<Twitter>>() {
			}));
			tweetService.getAuthService().deleteAccessToken();
			break;
		case JOptionPane.CANCEL_OPTION:
			LOG.info("キャンセルしました");
			break;
		default:
			throw new IllegalStateException(String.format("不正な状態です。result:%d", result));
		}
		return null;
	}
}
