import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    private JTable table;
    private JMenu turnMenu;
    private JMenu algorithmMenu;
    private JRadioButtonMenuItem player1;
    private JRadioButtonMenuItem player2;
    private JRadioButtonMenuItem minimax;
    private JRadioButtonMenuItem alphabeta;
    private JSlider slider;

    public MainWindow() {
        // Tiek definets JFrame nav vajadzibas izmantot mainigo kurs ir JFrame objekts, jo tiek extends JFrame
        setTitle("AI Spele");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
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
        add(slider, BorderLayout.NORTH);
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            // Debug
            System.out.println(getSelectedAlgorithm());
            System.out.println(getSelectedPlayer());
            System.out.println("Start button clicked!");
            ArrayList<Integer> array = generateArray(slider.getValue());
            remove(slider);
            revalidate();
            repaint();
            startButton.setVisible(false);
            GameButtons buttons = new GameButtons(array);
            JButton continueButton = new JButton("Turpinat");
            continueButton.addActionListener(event -> {
                if (!buttons.check()) {
                    JOptionPane.showMessageDialog(this, "Izvēlētie cipari nav blakus!!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                array.remove(0); // Placeholder
                System.out.println(array); // Debug
                buttons.updateButtons(array);
                if (array.size() == 1) { // Placeholder
                    remove(continueButton);
                    continueButton.setVisible(false);
                    buttons.dispose();
                    startButton.setVisible(true);
                    add(slider, BorderLayout.NORTH);
                    revalidate();
                    repaint();
                }
            });
            add(continueButton, BorderLayout.SOUTH);
            revalidate();
            repaint();
        });
        menuBar.add(turnMenu);
        menuBar.add(algorithmMenu);
        setJMenuBar(menuBar);
        // Table kas paradis speles vesturi ieklauj Speletaja punktus ta briza virkni un Datora punktus
        String[] columnNames = {"Speletajs", "Dators"};
        Object[][] data = {};
        table = new JTable(data, columnNames);
        table.getTableHeader().setReorderingAllowed(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
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
    
}