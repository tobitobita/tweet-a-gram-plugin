package dsk.tweet_a_gram.plugin;

import static dsk.tweet_a_gram.plugin.facebook.Const.FACEBOOK_ICON;
import static dsk.tweet_a_gram.plugin.twitter.Const.TWITTER_ICON;
import static dsk.tweet_a_gram.plugin.utils.AstahSwingTools.addMenu;
import static dsk.tweet_a_gram.plugin.utils.AstahSwingTools.createAstahMenuButton;
import static dsk.tweet_a_gram.plugin.utils.AstahSwingTools.createImage;
import static dsk.tweet_a_gram.plugin.utils.AstahSwingTools.getAstahToolBar;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;

import dsk.common.exception.DskRuntimeException;

public class Activator implements BundleActivator {
	public void start(BundleContext context) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final JFrame frame = AstahAPI.getAstahAPI().getViewManager().getMainFrame();
					JToolBar toolBar = getAstahToolBar(frame.getContentPane().getComponents());
					if (toolBar != null) {
						addMenu(toolBar,
								createAstahMenuButton(createImage(TWITTER_ICON),
										new dsk.tweet_a_gram.plugin.twitter.action.TweetAction()), true);
						addMenu(toolBar,
								createAstahMenuButton(createImage(FACEBOOK_ICON),
										new dsk.tweet_a_gram.plugin.facebook.action.TweetAction()), false);
					}
				} catch (ClassNotFoundException | InvalidUsingException e) {
					throw new DskRuntimeException(e);
				}
			}
		});
	}

	public void stop(BundleContext context) {
	}
}
