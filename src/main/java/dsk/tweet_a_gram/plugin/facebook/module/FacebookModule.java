package dsk.tweet_a_gram.plugin.facebook.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;

import dsk.tweet_a_gram.core.facebook.FacebookAuthService;
import dsk.tweet_a_gram.core.facebook.TweetFacebook;
import dsk.tweet_a_gram.core.service.AuthService;
import dsk.tweet_a_gram.core.service.TweetService;

public class FacebookModule extends AbstractModule {

	@Override
	protected void configure() {
		this.bind(new TypeLiteral<TweetService<String>>() {
		}).to(TweetFacebook.class);
		this.bind(new TypeLiteral<AuthService<String>>() {
		}).to(FacebookAuthService.class);
	}
}
