package dsk.tweet_a_gram.plugin.modules;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import dsk.tweet_a_gram.core.service.AuthService;
import dsk.tweet_a_gram.core.service.TweetService;
import dsk.tweet_a_gram.core.twitter.TweetTwitter;
import dsk.tweet_a_gram.core.twitter.TwitterAuthService;

public class FacebookModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(Twitter.class).toInstance(new TwitterFactory().getInstance());
		this.bind(new TypeLiteral<TweetService<Twitter>>() {
		}).to(TweetTwitter.class);
		this.bind(new TypeLiteral<AuthService<Twitter>>() {
		}).to(TwitterAuthService.class);
	}
}
