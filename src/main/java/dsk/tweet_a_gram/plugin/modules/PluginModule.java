package dsk.tweet_a_gram.plugin.modules;

import java.lang.reflect.Method;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;

import dsk.common.logging.LogInterceptor;
import dsk.tweet_a_gram.core.delegates.AuthDelegate;
import dsk.tweet_a_gram.core.delegates.MediaDelegate;
import dsk.tweet_a_gram.core.delegates.TweetDelegate;
import dsk.tweet_a_gram.plugin.gui.Tweet;
import dsk.tweet_a_gram.plugin.gui.TwitterAuthorization;
import dsk.tweet_a_gram.plugin.service.impl.CurrentDiagramService;

public class PluginModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(AuthDelegate.class).to(TwitterAuthorization.class);
		this.bind(TweetDelegate.class).to(Tweet.class);
		this.bind(MediaDelegate.class).to(CurrentDiagramService.class);

		this.bindInterceptor(Matchers.inSubpackage("dsk.tweet_a_gram.core"), new NoSyntheticMatcher(),
				new LogInterceptor());
	}

	private class NoSyntheticMatcher extends AbstractMatcher<Method> {
		@Override
		public boolean matches(Method method) {
			return !method.isSynthetic();
		}
	}
}
