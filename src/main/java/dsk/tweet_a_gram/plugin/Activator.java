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

import dsk.tweet_a_gram.plugin.utils.AstahSwingTools;

public class Activator implements BundleActivator {
	public void start(BundleContext context) {
		JToolBar toolBar = null;
		try {
			JFrame frame = AstahAPI.getAstahAPI().getViewManager().getMainFrame();
			toolBar = AstahSwingTools.getAstahToolBar(frame.getContentPane().getComponents());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidUsingException e) {
			e.printStackTrace();
		}
		if (toolBar != null) {
			AstahSwingTools.addMenu(
					toolBar,
					AstahSwingTools.createAstahMenuButton(
							new ImageIcon(getClass().getClassLoader().getResource(
									"dsk/tweet_a_gram/plugin/twitter/twitter-icon.png")), new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									System.out.println("twitter");
								}
							}), true);
			AstahSwingTools.addMenu(
					toolBar,
					AstahSwingTools.createAstahMenuButton(
							new ImageIcon(getClass().getClassLoader().getResource(
									"dsk/tweet_a_gram/plugin/facebook/facebook-icon.png")), new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									System.out.println("facebook");
								}
							}), false);
		}
	}

	public void stop(BundleContext context) {
	}
}
