import java.util.ArrayList;

public class GameTree {
    int player1Score; // Spēlētāja punkti katrā instancē kad notiks izmaiņa
    int player2Score; // Datora punkti katrā instancē kad notiks izmaiņa
    ArrayList<GameTree> children; // Koks kurš sastāv no bērna bērniem
    ArrayList<Integer> gameState; // Spēles instance katrā brīdi kad tiek izmainīts
    // Ideja ir ka mēs izmainam gameState nosakot kurus skaitļus mēs gribam mainīt skatoties pēc node index katrā līmenī
    // Mana ideja var būt ļoti atmiņu ziņā ļoti dārga
    public GameTree(int player1Score, int player2Score, ArrayList<Integer> gameState) {
        children = new ArrayList<>();
        this.gameState = new ArrayList<>(gameState);
        this.player1Score = player1Score;
        this.player2Score = player2Score;
    }

    public void addChild(GameTree child) {
        children.add(child);
    }

    public ArrayList<GameTree> getChildren() {
        return children;
    }
}

