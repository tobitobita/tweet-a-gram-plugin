package dsk.tweet_a_gram.plugin.service.impl;

import javax.inject.Inject;
import javax.swing.JOptionPane;

import com.change_vision.jude.api.inf.model.IDiagram;
import com.change_vision.jude.api.inf.project.ProjectAccessor;
import com.change_vision.jude.api.inf.view.IViewManager;

import dsk.common.exception.DskRuntimeException;
import dsk.common.util.R;
import dsk.tweet_a_gram.core.delegates.MediaDelegate;
import dsk.tweet_a_gram.core.utils.TweetTool;

public class CurrentDiagramService implements MediaDelegate {
	private ProjectAccessor projectAccessor;

	@Inject
	public CurrentDiagramService(ProjectAccessor projectAccessor) {
		this.projectAccessor = projectAccessor;
	}

	@Override
	public String getMediaPath() {
		String mediaPath = null;
		try {
			IViewManager viewManager = this.projectAccessor.getViewManager();
			IDiagram d = viewManager.getDiagramViewManager().getCurrentDiagram();
			if (null == d) {
				JOptionPane.showMessageDialog(viewManager.getMainFrame(), R.m("図を表示してください"), "",
						JOptionPane.WARNING_MESSAGE);
				return null;
			}
			String filename = d.exportImage(TweetTool.getAsutterDirectoryPath(), "png", 72);
			mediaPath = String.format("%s/%s", TweetTool.getAsutterDirectoryPath(), filename);
		} catch (Exception e) {
			throw new DskRuntimeException(e);
		}
		return mediaPath;
	}
}
