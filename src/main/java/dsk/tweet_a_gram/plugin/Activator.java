package dsk.tweet_a_gram.plugin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JToolBar;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.change_vision.jude.api.inf.AstahAPI;
import com.change_vision.jude.api.inf.exception.InvalidUsingException;
import com.change_vision.jude.api.inf.ui.IPluginActionDelegate.UnExpectedException;

import dsk.common.exception.DskRuntimeException;
import dsk.tweet_a_gram.plugin.utils.AstahSwingTools;

public class Activator implements BundleActivator {
	public void start(BundleContext context) {
		try {
			final JFrame frame = AstahAPI.getAstahAPI().getViewManager().getMainFrame();
			JToolBar toolBar = AstahSwingTools.getAstahToolBar(frame.getContentPane().getComponents());
			if (toolBar != null) {
				AstahSwingTools.addMenu(
						toolBar,
						AstahSwingTools.createAstahMenuButton(
								new ImageIcon(getClass().getClassLoader().getResource(
										"dsk/tweet_a_gram/plugin/twitter/twitter-icon.png")), new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent event) {
										try {
											dsk.tweet_a_gram.plugin.twitter.action.TweetAction.execute(frame);
										} catch (UnExpectedException e) {
											e.printStackTrace();
										}
									}
								}), true);
				AstahSwingTools.addMenu(
						toolBar,
						AstahSwingTools.createAstahMenuButton(
								new ImageIcon(getClass().getClassLoader().getResource(
										"dsk/tweet_a_gram/plugin/facebook/facebook-icon.png")), new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent event) {
										try {
											dsk.tweet_a_gram.plugin.facebook.action.TweetAction.execute(frame);
										} catch (UnExpectedException e) {
											e.printStackTrace();
										}
									}
								}), false);
			}
		} catch (ClassNotFoundException | InvalidUsingException e) {
			throw new DskRuntimeException(e);
		}
	}

	public void stop(BundleContext context) {
	}
}
