import javax.swing.*;
import java.awt.*;

public class TicTacToeAI {
    private static final char PLAYER = 'X';
    private static final char AI = 'O';
    private static final int SIZE = 3;
    private JButton[][] buttons = new JButton[SIZE][SIZE];
    private char[][] board = new char[SIZE][SIZE];
    private JLabel statusLabel;

    public TicTacToeAI() {
        JFrame frame = new JFrame("Tic Tac Toe - AI");
        frame.setSize(400, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = ' ';
                buttons[i][j] = new JButton(" ");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                buttons[i][j].setFocusPainted(false);
                int row = i, col = j;
                buttons[i][j].addActionListener(e -> playerMove(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }

        statusLabel = new JLabel("Your turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));
        frame.add(statusLabel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void playerMove(int row, int col) {
        if (board[row][col] == ' ' && !isGameOver()) {
            board[row][col] = PLAYER;
            buttons[row][col].setText(String.valueOf(PLAYER));
            if (isGameOver()) {
                updateStatus();
                return;
            }
            aiMove();
        }
    }

    private void aiMove() {
        int[] bestMove = bestMove();
        if (bestMove[0] != -1) {
            board[bestMove[0]][bestMove[1]] = AI;
            buttons[bestMove[0]][bestMove[1]].setText(String.valueOf(AI));
        }
        if (isGameOver()) {
            updateStatus();
        }
    }

    private int[] bestMove() {
        int bestScore = Integer.MIN_VALUE;
        int[] move = {-1, -1};

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = AI;
                    int score = minimax(board, 0, false);
                    board[i][j] = ' ';
                    if (score > bestScore) {
                        bestScore = score;
                        move[0] = i;
                        move[1] = j;
                    }
                }
            }
        }
        return move;
    }

    private int minimax(char[][] board, int depth, boolean isMax) {
        int score = evaluate(board);
        if (score == 10 || score == -10) return score;
        if (isBoardFull()) return 0;

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = AI;
                        best = Math.max(best, minimax(board, depth + 1, false));
                        board[i][j] = ' ';
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (board[i][j] == ' ') {
                        board[i][j] = PLAYER;
                        best = Math.min(best, minimax(board, depth + 1, true));
                        board[i][j] = ' ';
                    }
                }
            }
            return best;
        }
    }

    private int evaluate(char[][] board) {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') {
                return board[i][0] == AI ? 10 : -10;
            }
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' ') {
                return board[0][i] == AI ? 10 : -10;
            }
        }
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') {
            return board[0][0] == AI ? 10 : -10;
        }
        if (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ') {
            return board[0][2] == AI ? 10 : -10;
        }
        return 0;
    }

    private boolean isBoardFull() {
        for (char[] row : board) {
            for (char cell : row) {
                if (cell == ' ') return false;
            }
        }
        return true;
    }

    private boolean isGameOver() {
        return evaluate(board) != 0 || isBoardFull();
    }

    private void updateStatus() {
        int result = evaluate(board);
        if (result == 10) {
            statusLabel.setText("AI Wins!");
        } else if (result == -10) {
            statusLabel.setText("Player Wins!");
        } else {
            statusLabel.setText("It's a Draw!");
        }

        // Close the application after a short delay
        Timer timer = new Timer(2000, e -> System.exit(0)); // 2-second delay before exit
        timer.setRepeats(false);
        timer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeAI::new);
    }
}
