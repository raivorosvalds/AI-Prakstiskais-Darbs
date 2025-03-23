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
    private JButton startButton;
    private JLabel label;
    private GameButtons buttons;
    private JButton continueButton;
    private fakeButtons fakeButtons;

    public MainWindow() {
        frame = new JFrame();
        frame.setTitle("AI Spele");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
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

// //šo varēsim dzēst ārā, tas tikai testēšanai
//             GameTree rootNode = new GameTree(player1Score, player2Score, gameArray, !playerTurn);
//             System.out.println("Skaitlu rinda: " + gameArray);
//             rootNode.generateChildren(2, 0);//max depth 2, jo ar lielaku parak bremze
//             rootNode.printTree(0);
//             GameTree bestMove = rootNode.bestMove(2);
//             if (bestMove != null) {
//                 System.out.println("Best Move:");
//                 System.out.println("Game State: " + bestMove.gameState);
//                 System.out.println("Player1 Score: " + bestMove.player1Score);
//                 System.out.println("Player2 Score: " + bestMove.player2Score);
//                 System.out.println("Heuristic: " + bestMove.heuristic());
//             } else {
//                 System.out.println("No valid best move found.");
//             }


            buttons = new GameButtons(gameArray);

            if(!playerTurn){
                computerMove(gameArray);
                playerTurn=true;
            }
            continueButton = new JButton("Turpinat"); // Loģika kas tiek darbināta kad tiek nospiesta turpināt poga
            continueButton.addActionListener(event -> {
                if (fakeButtons != null) {
                    fakeButtons.dispose();
                }
                if (playerTurn && !buttons.check()) {
                    JOptionPane.showMessageDialog(frame, "Izvēlētie cipari nav blakus!!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                byte result = buttons.calculateScore(gameArray);
                ArrayList<Integer> selectedIndices = buttons.getSelectedIndices();
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
                buttons.updateButtons(gameArray);
                if (gameArray.size() == 1) {
                        endGame();
                        return;
                    }

                playerTurn=false;
                computerMove(gameArray);    
                playerTurn = true; // Gājiena maiņa
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
    if (fakeButtons != null) {
        fakeButtons.dispose();
    }
    resultFrame = new JFrame("Spēles rezultāts");
    resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    resultFrame.setLayout(new FlowLayout());
    resultFrame.add(label);
    resultFrame.pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int x = (screenSize.width - resultFrame.getWidth()) / 2;
    int y = (screenSize.height - resultFrame.getHeight()) / 2 - 150;
    resultFrame.setLocation(x, y);
    resultFrame.setVisible(true);
    startButton.setVisible(true);
    frame.add(slider, BorderLayout.NORTH);
    frame.remove(label);
    frame.revalidate();
    frame.repaint();
}

    public class fakeButtons extends JFrame {
        public fakeButtons(ArrayList<Integer> gameArray, ArrayList<Integer> selectedIndices) {
            setTitle("Datora izvēle");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLayout(new FlowLayout());
            createFakeButtons(gameArray, selectedIndices);
            pack();
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int x = (screenSize.width - getWidth()) / 2;
            int y = (screenSize.height - getHeight()) / 2 - 150;
            setLocation(x, y);
        }
        protected final void createFakeButtons(ArrayList<Integer> gameArray, ArrayList<Integer> selectedIndices) {
            for (int i = 0; i < gameArray.size(); i++) {
                int num = gameArray.get(i);
                JButton button = new JButton(Integer.toString(num));
                button.setEnabled(false);
                if (i == selectedIndices.get(0) || i == selectedIndices.get(1)) {
                    button.setBackground(Color.GREEN);
                }
                add(button);
            }
        }
    }

     private void computerMove(ArrayList<Integer> gameArray) {
        ArrayList<Integer> oldGameArray = new ArrayList<>(gameArray);

        GameTree tree = new GameTree(player1Score, player2Score, gameArray, false);
        GameTree bestMove;
        if (Minimax) {
            bestMove = tree.bestMove(2); 
        } else {
            bestMove = tree.bestMoveAlphaBeta(2); 
        }
        if (bestMove != null) {
            ArrayList<Integer> moveIndices = moveIndices(oldGameArray, bestMove.gameState);

            fakeButtons = new fakeButtons(oldGameArray, moveIndices);
            fakeButtons.setVisible(true);

            gameArray.clear();
            gameArray.addAll(bestMove.gameState);
            player1Score = bestMove.player1Score;
            player2Score = bestMove.player2Score;

            label.setText("Spelētājs " + player1Score + " : " + "Dators " + player2Score);
            buttons.updateButtons(gameArray);
        } else {
            System.out.println("No valid move found for computer.");
        }
      
        if (gameArray.size() == 1) {
            endGame();
        }
    }

       private ArrayList<Integer> moveIndices(ArrayList<Integer> oldList, ArrayList<Integer> newList) {
         for (int i = 0; i < oldList.size() - 1; i++) {
            int a = oldList.get(i);
            int b = oldList.get(i + 1);
            int replacement;
            int sum = a + b;
            if (sum > 7) {
                replacement = 1;
            } else if (sum < 7) {
                replacement = 3;
            } else {
                replacement = 2;
            }
            ArrayList<Integer> candidate = new ArrayList<>(oldList);
            candidate.set(i, replacement);
            candidate.remove(i + 1);
            if (candidate.equals(newList)) {
                ArrayList<Integer> indices = new ArrayList<>();
                indices.add(i);
                indices.add(i + 1);
                return indices;
            }
        }
        return new ArrayList<>(); // No valid move found
    }
}
