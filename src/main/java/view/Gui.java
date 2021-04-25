package view;

import model.GameEngine;
import model.GameLogic;
import model.exceptions.InitializationException;
import model.exceptions.ResourceException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Gui {
    public static final String NAME = "Don't stop moving!";
    private int width = 1280;
    private int height = 720;
    private JFrame frame;

    public Gui() {
        setupGui();
    }

    private void setupGui() {
        frame = new JFrame();

        try {
            JLabel background = new JLabel(new ImageIcon(ImageIO.read(new File("src/textures/background.png"))));
            frame.setContentPane(background);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        frame.setLayout(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;

        JLabel title = new JLabel(NAME, SwingConstants.CENTER);
        title.setBounds(width / 2 - 320, height / 4 - 64, 640, 128);
        title.setFont(new Font("Corbel", Font.PLAIN, 64));
        title.setForeground(Color.WHITE);
        frame.add(title, gridBagConstraints);

        JButton button = new JButton("Start");
        button.setBounds(width / 2 - 64, height / 2 - 32, 128, 64);
        button.addActionListener(event -> startButtonPressed());
        button.setFont(new Font("Corbel", Font.PLAIN, 32));
        button.setForeground(Color.GREEN);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        frame.add(button);

        frame.setTitle(NAME);
        frame.setSize(width, height);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void startButtonPressed() {
        String levelName = JOptionPane.showInputDialog("Enter level name (e.g. \"Frozen Land\"): ");
        if (levelName == null || levelName.isEmpty()) {
            return;
        }

        levelName = levelName.trim().toLowerCase().replaceAll(" ", "_");
        if (levelName.length() >= 2 && levelName.startsWith("\"") && levelName.endsWith("\"")) {
            levelName = levelName.substring(1, levelName.length() - 1);
        }
        if (levelName.isEmpty()) {
            return;
        }

        if (!levelName.endsWith(".lvl")) {
            levelName = levelName + ".lvl";
        }

        runGame(levelName);
    }

    private void runGame(String levelName) {
        frame.setVisible(false);
        try {
            GameLogic gameLogic = new GameLogic(levelName);
            GameEngine gameEngine = new GameEngine(gameLogic);
            gameEngine.run();
        } catch (InitializationException | ResourceException exception) {
            exception.printStackTrace();
            System.exit(-1);
        }
        frame.setVisible(true);
    }
}
