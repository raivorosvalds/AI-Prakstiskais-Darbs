import javax.swing.*;
import java.awt.*;
import java.util.Random;

/*TODO: Pievienot iespeju izveleties skaitlus kas ir blakus viens otram
 * virknes garuma izveli(jslider?)
 */
public class MainWindow extends JFrame {
    private JTable table;
    private JMenu turnMenu;
    private JMenu algorithmMenu;
    private JRadioButtonMenuItem player1;
    private JRadioButtonMenuItem player2;
    private JRadioButtonMenuItem minimax;
    private JRadioButtonMenuItem alphabeta;

    public MainWindow() {
        // Tiek definets JFrame nav vajadzibas izmantot mainigo kurs ir JFrame objekts, jo tiek extends JFrame
        setTitle("AI Spele");
        setSize(500, 400);
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
        /*Poga prieks speles sakuma, ideja ir velak izdarit lai pec pogas nospiesanas start poga pazud un paradas poga turpinat
         vinas vieta kuru speletajs nospiez lai varetu turpinat pec 2 ciparu izveles
         */
        JButton startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            System.out.println("Start button clicked!");
            int[] array = generateArray(15);
            String arrayText = "Skaitlu virkne: " + java.util.Arrays.toString(array);
            JLabel arrayLabel = new JLabel(arrayText);
            JPanel arrayPanel = new JPanel();
            arrayPanel.add(arrayLabel);
            add(arrayPanel, BorderLayout.NORTH);
            revalidate();
            repaint();
        });
        menuBar.add(turnMenu);
        menuBar.add(algorithmMenu);
        setJMenuBar(menuBar);
        // Table kas paradis speles vesturi ieklauj Speletaja punktus ta briza virkni un Datora punktus
        String[] columnNames = {"Speletajs", "Virkne", "Dators"};
        Object[][] data = {};
        table = new JTable(data, columnNames);
        table.getTableHeader().setReorderingAllowed(false);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);
        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
    protected int[] generateArray(int size) {
        Random rand = new Random();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(9) + 1;
        }
        return array;
    }
    
}