import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class MainWindow{
    private JFrame frame;
    private final JMenu turnMenu;
    private final JMenu algorithmMenu;
    private final JRadioButtonMenuItem player1;
    private final JRadioButtonMenuItem player2;
    private final JRadioButtonMenuItem minimax;
    private final JRadioButtonMenuItem alphabeta;
    private JSlider slider;
    private JFrame resultFrame;
    int player1Score;
    int player2Score;
    public boolean playerTurn;
    public boolean Minimax;
    private JFrame computerTurn;
    private JButton startButton;
    private JLabel label;
    private GameButtons buttons;
    private JButton continueButton;

    public MainWindow() {
        frame = new JFrame();
        // Tiek definets JFrame nav vajadzibas izmantot mainigo kurs ir JFrame objekts, jo tiek extends JFrame
        frame.setTitle("AI Spele");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        // Tiek definets Menu bar prieks algoritma un gajiena izveles, abos default izvele ir "Speletajs" un "Minimax"
        JMenuBar menuBar = new JMenuBar();
        turnMenu = new JMenu("Gajiens");
        algorithmMenu = new JMenu("Algoritms");
        player1 = new JRadioButtonMenuItem("Speletajs");
        player2 = new JRadioButtonMenuItem("Dators");
        ButtonGroup playerGroup = new ButtonGroup();
        playerGroup.add(player1);
        playerGroup.add(player2);
        player1.setSelected(true);
        turnMenu.add(player1);
        turnMenu.add(player2);
        minimax = new JRadioButtonMenuItem("Minimax");
        alphabeta = new JRadioButtonMenuItem("Alpha-Beta");
        ButtonGroup algorithmGroup = new ButtonGroup();
        algorithmGroup.add(minimax);
        algorithmGroup.add(alphabeta);
        minimax.setSelected(true);
        algorithmMenu.add(minimax);
        algorithmMenu.add(alphabeta);
        slider = new JSlider(JSlider.HORIZONTAL, 15, 25, 15);
        slider.setMajorTickSpacing(1);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        frame.add(slider, BorderLayout.NORTH);
        startButton = new JButton("Start"); // Spēles sākšanas loģika
        startButton.addActionListener(e -> {
            if (resultFrame != null) {
                resultFrame.dispose();
            }
            switch (getSelectedPlayer().getText()) {
                case "Speletajs" ->  {
                    playerTurn = true;
                }
                case "Dators" -> {
                    playerTurn = false;
                }
                default -> throw new AssertionError();
            }
            switch (getSelectedAlgorithm().getText()) {
                case "Minimax" -> {
                    Minimax = true;
                    player1Score = 0;
                    player2Score = 0;
                }
                case "Alpha-Beta" -> {
                    Minimax = false;
                    player1Score = 0;
                    player2Score = 0;
                }
                default -> throw new AssertionError();
            }
            label = new JLabel("Spelētājs " + player1Score + " : " + "Dators " + player2Score);
            frame.add(label);
            ArrayList<Integer> gameArray = generateArray(slider.getValue());
            frame.remove(slider);
            frame.revalidate();
            frame.repaint();
            startButton.setVisible(false);
            GameTree rootNode = new GameTree(player1Score, player2Score, gameArray, playerTurn);

            //tests game tree
            System.out.println("Skaitlu rinda: " + gameArray);
            rootNode.generateChildren(2, 0);//max depth 2, jo ar lielaku parak bremze
            rootNode.printTree(0);


            buttons = new GameButtons(gameArray);
            continueButton = new JButton("Turpinat"); // Loģika kas tiek darbināta kad tiek nospiesta turpināt poga
            continueButton.addActionListener(event -> {
                if (computerTurn != null) {
                    computerTurn.dispose();
                }
                if (!buttons.check()) {
                    JOptionPane.showMessageDialog(frame, "Izvēlētie cipari nav blakus!!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Integer[] chosen = new Integer[2];
                byte result = buttons.calculateScore(gameArray);
                ArrayList<Integer> selectedIndices = buttons.getSelectedIndices();
                chosen[0] = gameArray.get(selectedIndices.get(0));
                chosen[1] = gameArray.get(selectedIndices.get(1));
                switch (result) {
                    case 2 -> {
                        if (playerTurn) {
                            player1Score += 2;
                        } else {
                            player2Score += 2;
                        }
                        gameArray.set(selectedIndices.get(0), 2);
                        gameArray.remove((int)selectedIndices.get(1));
                    }
                    case -1 -> {
                        if (playerTurn) {
                            player1Score--;
                        } else {
                            player2Score--;
                        }
                        gameArray.set(selectedIndices.get(0), 3);
                        gameArray.remove((int)selectedIndices.get(1));
                    }
                    case 1 -> {
                        if (playerTurn) {
                            player1Score++;
                        } else {
                            player2Score++;
                        }
                        gameArray.set(selectedIndices.get(0), 1);
                        gameArray.remove((int)selectedIndices.get(1));
                    }
                    default -> throw new AssertionError();
                }
                label.setText("Spelētājs " + player1Score + " : " + "Dators " + player2Score);
                // Debug kods priekš lai būtu redzams datora gājiens vēlāk pārvietot augstāk ar visu loģiku ko izpilda dators
                if (!playerTurn) {
                    computerTurn = new JFrame("Datora gājiens");
                    computerTurn.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    computerTurn.setLayout(new FlowLayout());
                    JLabel turn = new JLabel("Dators izvēlējās " + chosen[0] + " un " + chosen[1] + " ar atrašanos vietu " + (selectedIndices.get(0)+1) + " un " + (selectedIndices.get(1)+1));
                    computerTurn.add(turn);
                    computerTurn.pack();
                    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                    int x = (screenSize.width - computerTurn.getWidth()) / 2;
                    int y = (screenSize.height - computerTurn.getHeight()) / 2 - 150;
                    computerTurn.setLocation(x, y);
                    computerTurn.setVisible(true);
                    playerTurn = !playerTurn;
                    buttons.updateButtons(gameArray);
                    if (gameArray.size() == 1) {
                        endGame();
                        return;
                    }
                    else return;
                }
                playerTurn = !playerTurn; // Gājiena maiņa
                buttons.updateButtons(gameArray);
                if (gameArray.size() == 1) {
                    endGame();
                }
            });
            frame.add(continueButton, BorderLayout.SOUTH);
            frame.revalidate();
            frame.repaint();
        });
        menuBar.add(turnMenu);
        menuBar.add(algorithmMenu);
        frame.setJMenuBar(menuBar);
        frame.add(startButton, BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    public JRadioButtonMenuItem getSelectedPlayer() {
    if (player1.isSelected()) {
        return player1;
    } else if (player2.isSelected()) {
        return player2;
    }
    return null;
    }
    public JRadioButtonMenuItem getSelectedAlgorithm() {
        if (minimax.isSelected()) {
            return minimax;
        } else if (alphabeta.isSelected()) {
            return alphabeta;
        }
        return null;
    }
    protected ArrayList<Integer> generateArray(int size) {
        Random rand = new Random();
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            array.add(i, rand.nextInt(9) + 1);
        }
        return array;
    }
    protected void endGame() {
        frame.remove(continueButton);
        buttons.dispose();
        // Rezultāts
        resultFrame = new JFrame("Spēles rezultāts");
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setLayout(new FlowLayout());
        resultFrame.add(label);
        resultFrame.pack();
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setVisible(true);
        startButton.setVisible(true);
        frame.add(slider, BorderLayout.NORTH);
        frame.remove(label);
        frame.revalidate();
        frame.repaint();
    }
}
