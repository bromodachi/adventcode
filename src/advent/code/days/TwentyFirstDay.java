package advent.code.days;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TwentyFirstDay extends DaySolver{

    Player [] players;
    static class Player {
        int position;
        int totalScore = 0;

        public boolean isHasWon() {
            return hasWon;
        }

        boolean hasWon =false;

        public Player(int startingPosition) {
            this.position = startingPosition;
        }

        public void incrementScore(int score) {
            totalScore += score;
            if (isOver1000()) {
                hasWon = true;
            }
        }

        public int moveSpace(int space) {
            position = (position + space) % 10;
            int newPosition = position == 0 ? 10 : position;
            incrementScore(newPosition);
            return newPosition;
        }

        public boolean isOver1000(){
            return totalScore >= 1000;
        }
    }
    protected TwentyFirstDay(String fileName) throws FileNotFoundException {
        super(fileName);
        Scanner scanner = readFile.toScanner();
        players = new Player[2];
        int index = 0;
        while (scanner.hasNext()) {
            final String line = scanner.nextLine();
            players[index++] = new Player(Integer.parseInt(line.split(": ")[1]));
        }
    }

    @Override
    public void solveFirstProblem() {
        int diceOne = 1;
        int diceTwo = 2;
        int diceThree = 3;
        int diceRolled = 0;
        boolean playerOneTurn = true;
        Player[] players = new Player[] {new Player(this.players[0].position), new Player(this.players[1].position)};
        while (!players[0].isOver1000() && !players[1].isOver1000()) {
            int spaceToMove = diceOne + diceTwo + diceThree;
            if (playerOneTurn) {
                players[0].moveSpace(spaceToMove);
            }
            else {
                players[1].moveSpace(spaceToMove);
            }
            diceOne = diceThree + 1;
            diceTwo = diceThree + 2;
            diceThree = diceThree + 3;
            playerOneTurn = !playerOneTurn;
            diceRolled += 3;

        }

        printAnswer(players[0].isHasWon() ? players[1].totalScore * diceRolled : players[0].totalScore * diceRolled );
    }

    Map<String, long []> memo  = new HashMap<>();

    private String key(int player1Score, int player2Score, int player1Starting, int player2Starting, boolean playerOneTurn, int dieRoll) {
        return "" + player1Score + "," + player2Score + "," + player1Starting + "," + player2Starting + "," + playerOneTurn + "," + dieRoll;
    }

    long [] dp(int player1Score, int player2Score, int player1Starting, int player2Starting, boolean playerOneTurn, int dieRoll) {
        final String key = key(player1Score, player2Score, player1Starting, player2Starting, playerOneTurn, dieRoll);
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        if (player1Score >= 21) {
            return new long[] {1, 0};
        }
        if (player2Score >= 21) {
            return new long[] {0, 1};
        }
        long [] total = new long[] {0, 0};
        for (int i = 1; i <= 3; i ++) {
            int position = ((playerOneTurn ? player1Starting : player2Starting) + i) % 10;
            int newPosition = position == 0 ? 10 : position;
            long[] back;
            if (playerOneTurn) {
                back = dp(dieRoll == 0 ? newPosition + player1Score : player1Score, player2Score, newPosition, player2Starting, (dieRoll != 0 || !playerOneTurn) && playerOneTurn, dieRoll == 0 ? 2 : dieRoll - 1);
            }
            else {
                back = dp(player1Score, dieRoll == 0 ? newPosition + player2Score : player2Score, player1Starting, newPosition, dieRoll == 0 && !playerOneTurn || playerOneTurn, dieRoll == 0 ? 2 : dieRoll - 1);
            }
            total[0] += back[0];
            total[1] += back[1];
        }
        memo.put(key, total);
        return total;
    }

    @Override
    public void solveSecondProblem() {
        long [] ans = dp(0, 0,this.players[0].position, this.players[1].position, true, 2);
        printAnswer(Math.max(ans[0], ans[1]));
    }
}
