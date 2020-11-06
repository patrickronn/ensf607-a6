package exercise4;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;

public class PlayerGUI extends JFrame {

    /**
     * Input for a username.
     */
    private JTextField usernameField;

    /**
     * Outputs user's assigned mark.
     */
    private JTextField markField;

    /**
     * Outputs messages from the server.
     */
    private JTextArea serverMessages;

    /**
     * Outputs tic-tac-toe board tile values.
     */
    private JButton[][] tiles;

    public PlayerGUI(String title) {
        // Add link to PlayerClient or make this a client

        // Instantiate GUI components
        JLabel usernamePrompt = new JLabel("Username:");
        usernameField = new JTextField(15);
        usernameField.setText("");
        usernameField.setEditable(false);

        JLabel markLabel = new JLabel("Player mark:");
        markField = new JTextField(1);
        markField.setEditable(false);

        JLabel messageLabel = new JLabel("Server Messages:");

        serverMessages = new JTextArea(5, 30);
        serverMessages.setWrapStyleWord(true);
        serverMessages.setWrapStyleWord(true);
        serverMessages.setEditable(false);
        DefaultCaret caret = (DefaultCaret) serverMessages.getCaret();  // auto-scroll when messages update
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        JScrollPane scrollPane = new JScrollPane(serverMessages);

        // Instantiate board and 3x3 tiles
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3,3));
        tiles = new JButton[3][];
        for (int i = 0; i < 3; i ++) {
            tiles[i] = new JButton[3];
            for (int j = 0; j < 3; j++) {
                // Display tile on board
                tiles[i][j] = new JButton("");
                tiles[i][j].setContentAreaFilled(false);
                boardPanel.add(tiles[i][j]);
            }
        }

        boardPanel.setSize(1000,1000);

        JPanel usernamePanel = new JPanel();
        usernamePanel.setLayout(new FlowLayout());
        usernamePanel.add(usernamePrompt);
        usernamePanel.add(usernameField);
        usernamePanel.add(markLabel);
        usernamePanel.add(markField);

        JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
        messagePanel.add(usernamePanel);
        messagePanel.add(messageLabel);
        messagePanel.add(scrollPane);

        messageLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Add components to content pane
        Container contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
        contentPane.add(boardPanel);
        contentPane.add(messagePanel);
        this.setResizable(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800,300);
        setTitle(title);
        setVisible(true);
    }

    public void setPlayerMark(char mark) {
        this.markField.setText(String.valueOf(mark));
    }

    public void setPlayerUsername(String username) {
        this.usernameField.setText(username);
    }

    public String getUserInput(String prompt) {
        return JOptionPane.showInputDialog(prompt);
    }

    public void appendServerMessageText(String message) {
        serverMessages.append("\n" + message);
    }

    public final JButton[][] getTiles() {
        return tiles;
    }

    public void updateTileValues(char[][] values) {
        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j++) {
                tiles[i][j].setText(String.valueOf(values[i][j]));
            }
        }
    }

    public static void main(String[] args) {
        PlayerGUI frame = new PlayerGUI("Tic Tac Toe Player");
        frame.setPlayerMark('O');
        String username = frame.getUserInput("Please enter your username");
        frame.setPlayerUsername(username);
        char[][] values = new char[3][3];

        for (int i = 0; i < 3; i ++) {
            for (int j = 0; j < 3; j++) {
                values[i][j] = 'X';
            }
        }

        frame.updateTileValues(values);
    }
}
