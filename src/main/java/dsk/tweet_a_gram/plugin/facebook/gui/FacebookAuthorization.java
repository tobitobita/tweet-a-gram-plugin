package dsk.tweet_a_gram.plugin.facebook.gui;

import static dsk.tweet_a_gram.core.Const.MESSAGE;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dsk.common.exception.DskRuntimeException;
import dsk.common.util.R;
import dsk.tweet_a_gram.core.delegates.AuthDelegate;

public class FacebookAuthorization extends JDialog implements AuthDelegate {
    private static final long serialVersionUID = -7125988251336550150L;
    private static final Logger LOG = LoggerFactory.getLogger(FacebookAuthorization.class);

    private JPanel contentPane;
    private JTextField textField;

    private String authUrl;

    /**
     * Create the frame.
     */
    public FacebookAuthorization() {
        setModal(true);
        setResizable(false);
        setTitle(R.m(MESSAGE, "あすったー"));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setBounds(100, 100, 317, 188);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JTextPane lblNewLabel = new JTextPane();
        lblNewLabel.setText(R.m(MESSAGE, "下記リンクよりtwitterの認証を行ってください。"));
        lblNewLabel.setBackground(new Color(238, 238, 238));
        lblNewLabel.setBounds(14, 6, 288, 49);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setBounds(107, 95, 152, 28);
        contentPane.add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton(R.m(MESSAGE, "認証する"));
        btnNewButton.setBounds(100, 129, 117, 29);
        btnNewButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 2694805711786965093L;

            @Override
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
            }
        });
        contentPane.add(btnNewButton);

        JLabel lblNewLabel_1 = new JLabel(R.m(MESSAGE, "PIN入力"));
        lblNewLabel_1.setBounds(46, 101, 61, 16);
        contentPane.add(lblNewLabel_1);

        JEditorPane dtrpnl = new JEditorPane();
        dtrpnl.setBackground(new Color(238, 238, 238));
        dtrpnl.setContentType("text/html");
        dtrpnl.setEditable(false);
        dtrpnl.setBounds(29, 67, 258, 16);
        dtrpnl.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        dtrpnl.setText(String.format("<a href=\"%s\">%s</a>", this.authUrl,
                R.m(MESSAGE, "クリックするとブラウザが起動します。")));
        dtrpnl.addHyperlinkListener(new HyperlinkListener() {
            @Override
            public void hyperlinkUpdate(HyperlinkEvent event) {
                if (HyperlinkEvent.EventType.ACTIVATED == event.getEventType()) {
                    try {
                        Desktop.getDesktop().browse(new URI(authUrl));
                    } catch (IOException e) {
                        throw new DskRuntimeException(e);
                    } catch (URISyntaxException e) {
                        throw new DskRuntimeException(e);
                    }
                }
            }
        });
        contentPane.add(dtrpnl);
    }

    @Override
    public String doAuthTwitter(String url) {
        LOG.debug(url);
        this.authUrl = url;
        this.setVisible(true);
        if (StringUtils.isEmpty(this.textField.getText())) {
            return null;
        }
        return this.textField.getText();
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    FacebookAuthorization frame = new FacebookAuthorization();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
