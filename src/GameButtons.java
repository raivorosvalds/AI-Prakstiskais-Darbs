import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

public class GameButtons extends JFrame {
    private final ArrayList<JButton> buttons;
    private final ArrayList<Integer> selectedIndices;

    public GameButtons(ArrayList<Integer> array) {
        setTitle("Ciparu izvele");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        buttons = new ArrayList<>();
        selectedIndices = new ArrayList<>();
        createButtons(array);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2 + 100;
        setLocation(x, y);
        setVisible(true);
    }

    private void createButtons(ArrayList<Integer> array) {
        for (Integer num : array) {
            JButton button = new JButton(num.toString());
            button.addActionListener(event-> {
            int index = buttons.indexOf(button);
            if (selectedIndices.contains(index)) {
                selectedIndices.remove((Integer) index);
                button.setBackground(null);
            } else {
                if (selectedIndices.size() < 2) {
                    selectedIndices.add(index);
                    button.setBackground(Color.GREEN);
                }
            }
            System.out.println("Selected indices: " + selectedIndices);
            });
            buttons.add(button);
            add(button);
        }
    }

    public void updateButtons(ArrayList<Integer> array) {
        for (JButton button : buttons) {
            remove(button);
        }
        buttons.clear();
        selectedIndices.clear();
        createButtons(array);
        revalidate();
        repaint();
    }
    public boolean check() {
        if (selectedIndices.size() != 2) {
            return false;
        }
        return Math.max(selectedIndices.get(0), selectedIndices.get(1)) - Math.min(selectedIndices.get(0), selectedIndices.get(1)) == 1;
    }
    public byte calculateScore(ArrayList<Integer> array) {
        if (array.get(selectedIndices.get(0)) + array.get(selectedIndices.get(1)) == 7) {
            return 2;
        }
        if (array.get(selectedIndices.get(0)) + array.get(selectedIndices.get(1)) < 7) {
            return -1;
        }
        if (array.get(selectedIndices.get(0)) + array.get(selectedIndices.get(1)) > 7) {
            return 1;
        }
        return 0;
    }
    public ArrayList<Integer> getSelectedIndices() {
        return selectedIndices;
    }
}
