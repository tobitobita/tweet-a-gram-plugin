package dsk.tweet_a_gram.plugin.modules;

import java.lang.reflect.Method;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matchers;

import dsk.common.exception.DskRuntimeException;
import dsk.common.logging.LogInterceptor;
import dsk.tweet_a_gram.core.delegates.MediaDelegate;
import dsk.tweet_a_gram.plugin.service.impl.CurrentDiagramService;

public class PluginModule extends AbstractModule {

	@Override
	protected void configure() {
		try {
			this.bind(ProjectAccessor.class).toInstance(AstahAPI.getAstahAPI().getProjectAccessor());
		} catch (ClassNotFoundException e) {
			throw new DskRuntimeException(e);
		}
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
