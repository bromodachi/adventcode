package advent.code.days;

import advent.code.utils.ConverterUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class FourthDay extends DaySolver {
    List<Integer> caller;
    List<Board> boards;

    private static class Board {
        private boolean won;
        private final boolean [][] board;
        private final Map<Integer, Integer> map;
        private final int[] rowColumnToValue;

        private final static int BOARD_LENGTH = 5;

        public Board() {
            board = new boolean[BOARD_LENGTH][BOARD_LENGTH];
            map = new HashMap<>();
            rowColumnToValue = new int[BOARD_LENGTH * BOARD_LENGTH + BOARD_LENGTH];
        }

        public boolean containsValue(final int key) {
            return map.containsKey(key);
        }

        public void setAsWon() {
            won = true;
        }

        public boolean hasWon() {
            return won;
        }

        public int sumOfAllUnmarked() {
            int sum = 0;
            for (int row = 0; row < BOARD_LENGTH; row ++) {
                for (int column = 0; column < BOARD_LENGTH; column ++) {
                    if (!board[row][column]) {
                        sum += rowColumnToValue[twoDToOneD(row, column)];
                    }
                }
            }
            return sum;
        }

        public void addValue(int key, int row, int column) {
            map.put(key, twoDToOneD(row, column));
            rowColumnToValue[twoDToOneD(row, column)] = key;
        }


        /**
         * marks a value as true and checks if the corresponding row or column is true.
         * @param key - the number that we will mark as true
         * @return true if all values in row or column is true.
         */
        public boolean markValue(final int key) {
            final int row = toRow(map.get(key));
            final int column = toColumn(map.get(key));
            board[row][column] = true;

            boolean columnIsAllTrue = true;
            boolean rowIsAllTrue = true;
            for (int c = 0; c < BOARD_LENGTH; c ++) {
                if (!board[row][c]) {
                    rowIsAllTrue = false;
                    break;
                }
            }
            for (int r = 0; r < BOARD_LENGTH; r ++) {
                if (!board[r][column]) {
                    columnIsAllTrue = false;
                    break;
                }
            }
            return columnIsAllTrue || rowIsAllTrue;
        }

        public int twoDToOneD(int row, int column) {
            return row * BOARD_LENGTH + column;
        }

        private int toRow(int value) {
            return value / BOARD_LENGTH;
        }

        private int toColumn(int value) {
            return value % BOARD_LENGTH;
        }
    }

    protected FourthDay(String fileName) throws Exception {
        super(fileName);
        boards = new ArrayList<>();
        Scanner scanner = readFile.toScanner();
        caller = ConverterUtil.readLineAsList(scanner.nextLine());
        Board board = null;
        int row = 0;
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.isBlank()) {
                if (board != null) {
                    boards.add(board);
                }
                board = new Board();
                row = 0;
            }
            else {
                List<Integer> values = ConverterUtil.readLineAsList(line);
                for (int column = 0; column < Board.BOARD_LENGTH; column ++ ) {
                    board.addValue(values.get(column), row, column);
                }
                row += 1;
            }
        }
        boards.add(board);
    }

    @Override
    public void solveFirstProblem() {
        for (int value : caller) {
            for (Board board : boards) {
                if (board.containsValue(value) && board.markValue(value)) {
                    printAnswer( board.sumOfAllUnmarked() * value);
                    return;
                }
            }
        }
    }

    @Override
    public void solveSecondProblem() {
        int boardWonCount = 0;
        for (int value : caller) {
            for (int i = 0; i < boards.size(); i ++) {
                Board board = boards.get(i);
                if (board.hasWon()) {
                    continue;
                }
                if (board.containsValue(value) && board.markValue(value)) {
                    board.setAsWon();
                    if (boardWonCount == boards.size() - 1) {
                        printAnswer(  board.sumOfAllUnmarked() * value);
                        return;
                    }
                    boardWonCount ++;
                }
            }
        }
    }
}
