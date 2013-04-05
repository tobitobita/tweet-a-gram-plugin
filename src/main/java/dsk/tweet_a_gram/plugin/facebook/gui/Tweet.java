package dsk.tweet_a_gram.plugin.facebook.gui;

import static dsk.tweet_a_gram.core.Const.MESSAGE;
import static dsk.tweet_a_gram.plugin.facebook.Const.FACEBOOK_ICON;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsk.common.exception.DskRuntimeException;
import dsk.common.util.R;
import dsk.tweet_a_gram.core.delegates.TweetDelegate;
import dsk.tweet_a_gram.plugin.utils.AstahSwingTools;

public class Tweet extends JDialog implements TweetDelegate {
	private static final long serialVersionUID = -2198301704134244051L;

	private static final Logger LOG = LoggerFactory.getLogger(Tweet.class);
	private static final int MAX_COUNT = 140;
	/** #5C0002 */
	private static final Color DARK_RED = new Color(92, 0, 2);
	/** #D40D12 */
	private static final Color RED = new Color(212, 13, 18);

	private JPanel contentPane;
	private JPanel imagePanel;

	private Image image;
	private JScrollPane scrollPane;
	private JLabel count;
	private boolean tweet;
	private JTextArea textArea;

	/**
	 * Create the frame.
	 */
	public Tweet() {
		setModal(true);
		setResizable(false);
		setTitle(R.m(MESSAGE, "あすったー") + " (Facebook)");
		setIconImage(AstahSwingTools.createImage(FACEBOOK_ICON));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 500);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnNewButton = new JButton(R.m(MESSAGE, "投稿する"));
		btnNewButton.setBounds(166, 108, 117, 29);
		btnNewButton.addActionListener(new AbstractAction() {
			private static final long serialVersionUID = -3796215656017723061L;

			@Override
			public void actionPerformed(ActionEvent event) {
				LOG.debug(textArea.getText());
				tweet = true;
				setVisible(false);
			}
		});
		contentPane.add(btnNewButton);

		imagePanel = new JPanel();
		imagePanel.setBounds(6, 149, 438, 323);
		contentPane.add(imagePanel);

		count = new JLabel(Integer.toString(MAX_COUNT));
		count.setHorizontalAlignment(SwingConstants.RIGHT);
		count.setBounds(383, 113, 61, 16);
		contentPane.add(count);

		scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(6, 6, 438, 98);
		contentPane.add(scrollPane);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				int c = MAX_COUNT - textArea.getText().length();
				count.setForeground(Color.BLACK);
				if (20 > c) {
					count.setForeground(DARK_RED);
				}
				if (10 > c) {
					count.setForeground(RED);
				}
				count.setText(Integer.toString(c));
			}
		});
		scrollPane.setViewportView(textArea);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (null != this.image) {
			imagePanel.getGraphics().drawImage(this.image, 0, 0, this);
		}
	}

	public void loadImage(String path) throws IOException {
		BufferedImage orgImg = ImageIO.read(new File(path));
		int panelWidth = imagePanel.getWidth();
		int panelHeight = imagePanel.getHeight();
		LOG.debug(String.format("%s, %s", panelWidth, panelHeight));
		int width = orgImg.getWidth();
		int height = orgImg.getHeight();
		LOG.debug(String.format("%s, %s", width, height));
		if (panelWidth < width) {
			double parcent = (double) height / (double) width;
			width = panelWidth;
			height = (int) ((double) panelWidth * parcent);
		} else if (panelHeight < height) {
			double parcent = (double) width / (double) height;
			width = (int) ((double) panelHeight * parcent);
			height = panelHeight;
		}
		LOG.debug(String.format("%s, %s", width, height));
		this.image = orgImg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}

	@Override
	public boolean isTweet() {
		return this.tweet;
	}

	@Override
	public String getTweet() {
		setVisible(true);
		if (!this.tweet) {
			return "";
		}
		return this.textArea.getText();
	}

	@Override
	public void setMediaPath(String path) {
		try {
			this.loadImage(path);
		} catch (IOException e) {
			throw new DskRuntimeException(e);
		}
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tweet frame = new Tweet();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
