package exercise4;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PlayerGUI extends JFrame {

    /**
     * Input for a username.
     */
    private JTextField inputUsername;

    /**
     * Outputs user's assigned mark.
     */
    private JTextField inputMark;

    /**
     * Outputs messages from the server.
     */
    private JTextArea serverMessages;

    public PlayerGUI(String title) {
        // Add link to PlayerClient or make this a client

        // Instantiate GUI components
        JLabel usernamePrompt = new JLabel("Username:");
        inputUsername = new JTextField(15);
        inputUsername.setText("");

        JLabel markLabel = new JLabel("Player mark:");
        inputMark = new JTextField(1);
        inputMark.setEditable(false);

        JLabel messageLabel = new JLabel("Server Messages:");
        serverMessages = new JTextArea(5, 30);
        serverMessages.setWrapStyleWord(true);
        serverMessages.setWrapStyleWord(true);
        serverMessages.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(serverMessages);

        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3,3));
        JButton[] tiles = new JButton[9];
        for (JButton tile: tiles) {
            tile = new JButton("?");
            tile.setContentAreaFilled(false);
            boardPanel.add(tile);
        }
        boardPanel.setSize(1000,1000);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout());
        usernamePanel.add(usernamePrompt);
        usernamePanel.add(inputUsername);
        usernamePanel.add(markLabel);
        usernamePanel.add(inputMark);

        JPanel messagePanel = new JPanel();
        messagePanel.add(usernamePanel);
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.add(messageLabel);
        messagePanel.add(scrollPane);

        messageLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Add components to content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        contentPane.add(boardPanel);
        contentPane.add(messagePanel);
        this.setResizable(false);

        setSize(800,300);
        setTitle(title);
        setVisible(true);
    }

    public static void main(String[] args) {
        new PlayerGUI("Tic Tac Toe Player");
    }
}
