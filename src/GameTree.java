import java.util.ArrayList;

public class GameTree {
    int player1Score; // Spēlētāja punkti katrā instancē kad notiks izmaiņa
    int player2Score; // Datora punkti katrā instancē kad notiks izmaiņa
    ArrayList<GameTree> children; // Koks kurš sastāv no bērna bērniem
    ArrayList<Integer> gameState; // Spēles instance katrā brīdi kad tiek izmainīts
    // Ideja ir ka mēs izmainam gameState nosakot kurus skaitļus mēs gribam mainīt skatoties pēc node index katrā līmenī
    // Mana ideja var būt ļoti atmiņu ziņā ļoti dārga
    boolean player1turn;

    public GameTree(int player1Score, int player2Score, ArrayList<Integer> gameState, boolean player1turn) {
        children = new ArrayList<>();
        this.gameState = new ArrayList<>(gameState);
        this.player1Score = player1Score;
        this.player2Score = player2Score;
        this.player1turn = player1turn;
    }

    public void generateChildren(int maxDepth, int currentDepth){
        if(currentDepth >= maxDepth || isTerminal()) return;
        for(int i = 0; i <gameState.size() - 1; i++){
            ArrayList<Integer> newGameState = new ArrayList<>(gameState);
            int a = gameState.get(i), b = gameState.get(i+1);
            int sum = a+b, newScore1= player1Score, newScore2=player2Score;
            int replacement;

            if(sum>7){
                replacement = 1;
                if(!player1turn) newScore2+=1;
                else newScore1+=1;
            }else if(sum<7){
                replacement = 3;
                if(!player1turn) newScore2-=1;
                else newScore1-=1;
            }else{
                replacement=2;
                if(!player1turn) newScore2+=2;
                else newScore1+=2;
            }
           
            newGameState.set(i, replacement);
            newGameState.remove(i+1);

            GameTree child = new GameTree(newScore1,newScore2,newGameState, !player1turn);
            children.add(child);

            child.generateChildren(maxDepth, currentDepth + 1);
        }
    }


    public ArrayList<GameTree> getChildren() {
        return children;
    }


    public boolean isTerminal(){
    return gameState.size()==1;
    }


//testesanai parada visus iespejamus stavoklus 1 un 2 limeni 
    public void printTree(int depth){
            String indent = " ".repeat(depth*2);
            int heuristicValue=heuristic();
            int minimaxValue = minmax(this, 2, !player1turn); 
            int alphaBetaValue = alphaBeta(this, 2, Integer.MIN_VALUE, Integer.MAX_VALUE, !player1turn);
            System.out.println(indent + "stavoklis: " + gameState + 
                   " |P1 " + player1Score + 
                   " |P2 " + player2Score + 
                   " | Heuristic " + heuristicValue + 
                   " | MinMax " + minimaxValue + 
                   " | AlphaBeta " + alphaBetaValue);
            for(GameTree child : getChildren()){
                child.printTree(depth +1);
            }
    }

    //man liekas nav tipiska funkcija jo nav -1 0 1, bet viņa vislabāk apraksta stāvokļus     
    public int heuristic(){
        return player2Score - player1Score;
    }

    public int minmax(GameTree node, int depth, boolean player1turn){
        int eval, min, max;
        if(depth == 0 || node.isTerminal() || node.getChildren().isEmpty()) return node.heuristic();

        if(!player1turn){
            max = Integer.MIN_VALUE;
            for(GameTree child : node.getChildren()){
                eval = minmax(child, depth -1, true);
                max= Math.max(max, eval);
            }
            return max;
        }else{
            min = Integer.MAX_VALUE;
            for(GameTree child : node.getChildren()){
                eval = minmax(child, depth-1, false);
                min=Math.min(min, eval);
            }
            return min;
        }
    }

    public GameTree bestMove(int depth){
        if(this.getChildren().isEmpty() && !this.isTerminal()){
            this.generateChildren(depth, 0);
        }

        GameTree bestChild = null;
        int bestVal;

        if(!player1turn){
            bestVal=Integer.MIN_VALUE;
            for(GameTree child: getChildren()){
                int value = minmax(child, depth-1, true);
                if(value>bestVal ){
                    bestVal=value;
                    bestChild=child;
                //tests
                    System.out.println("Child: " + child.gameState + 
                       " | Minimax: " + value + 
                       " | Heuristic: " + child.heuristic());    
                }
            }
        }else{
            bestVal=Integer.MAX_VALUE;
            for(GameTree child: getChildren()){
                int value = minmax(child, depth-1, false);
                if(value<bestVal ){
                    bestVal=value;
                    bestChild=child;
                    //tests
                    System.out.println("Child: " + child.gameState + 
                       " | Minimax: " + value + 
                       " | Heuristic: " + child.heuristic());    
                 }
             }
        }
        return bestChild;
    }

    public int alphaBeta(GameTree node, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || node.isTerminal() || node.getChildren().isEmpty()) {
            return node.heuristic();
        }
    
        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (GameTree child : node.getChildren()) {
                int eval = alphaBeta(child, depth - 1, alpha, beta, false);
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break; // Beta cut-off
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (GameTree child : node.getChildren()) {
                int eval = alphaBeta(child, depth - 1, alpha, beta, true);
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break; // Alpha cut-off
                }
            }
            return minEval;
        }
    }
    
    public GameTree bestMoveAlphaBeta(int depth) {
        if (this.getChildren().isEmpty() && !this.isTerminal()) {
            this.generateChildren(depth, 0);
        }
    
        GameTree bestChild = null;
        int bestVal;
    
        if (!player1turn) {
            bestVal = Integer.MIN_VALUE;
            for (GameTree child : getChildren()) {
                int value = alphaBeta(child, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
                if (value > bestVal) {
                    bestVal = value;
                    bestChild = child;
                }
            }
        } else {
            bestVal = Integer.MAX_VALUE;
            for (GameTree child : getChildren()) {
                int value = alphaBeta(child, depth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                if (value < bestVal) {
                    bestVal = value;
                    bestChild = child;
                }
            }
        }
        return bestChild;
    }
}