package dsk.tweet_a_gram.plugin.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToolBar;

public final class AstahSwingTools {
	private AstahSwingTools() {
	}

	public static JToolBar getAstahToolBar(Component[] components) {
		JToolBar toolBar = null;
		for (Component c : components) {
			if (c instanceof JToolBar) {
				toolBar = (JToolBar) c;
				break;
			}
			if (c instanceof Container) {
				toolBar = getAstahToolBar(((Container) c).getComponents());
				if (toolBar != null) {
					break;
				}
			}
		}
		return toolBar;
	}

	public static JButton createAstahMenuButton(Icon icon, ActionListener actionListener) {
		JButton button = new JButton(icon);
		button.setPreferredSize(new Dimension(26, 22));
		button.setRequestFocusEnabled(false);
		button.setFocusable(false);
		button.setMargin(new Insets(1, 1, 1, 1));
		button.addActionListener(actionListener);
		return button;
	}

	public static void addMenu(JToolBar toolBar, JButton button, boolean openSpace) {
		Component[] components = toolBar.getComponents();
		for (int i = components.length - 1; i >= 0; --i) {
			if (components[i] instanceof Box.Filler) {
				if (openSpace) {
					Dimension fillerSize = new Dimension(5, 26);
					toolBar.add(new Box.Filler(fillerSize, fillerSize, fillerSize), i);
					++i;
				}
				toolBar.add(button, i);
				return;
			}
		}
	}
}
