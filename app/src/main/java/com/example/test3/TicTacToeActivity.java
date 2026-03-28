package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TicTacToeActivity extends AppCompatActivity {

    // Game board: 3x3 array
    private char[][] board = new char[3][3];

    // Current player: 'X' or 'O'
    private char currentPlayer = 'X';

    // Track if game is over
    private boolean gameOver = false;

    // References to UI elements
    private TextView tvTurn;
    private Button[][] buttons = new Button[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe);

        tvTurn = findViewById(R.id.tvTurn);

        // Initialize all buttons
        initializeButtons();

        // Initialize game board
        initializeBoard();

        // Set up restart button
        Button btnRestart = findViewById(R.id.btnRestart);
        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
            }
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TicTacToeActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close current activity
            }
        });

    }

    private void initializeButtons() {
        // Assign all 9 buttons to the array
        buttons[0][0] = findViewById(R.id.btn00);
        buttons[0][1] = findViewById(R.id.btn01);
        buttons[0][2] = findViewById(R.id.btn02);
        buttons[1][0] = findViewById(R.id.btn10);
        buttons[1][1] = findViewById(R.id.btn11);
        buttons[1][2] = findViewById(R.id.btn12);
        buttons[2][0] = findViewById(R.id.btn20);
        buttons[2][1] = findViewById(R.id.btn21);
        buttons[2][2] = findViewById(R.id.btn22);

        // Set click listener for each button
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCellClick(row, col);
                    }
                });
            }
        }
    }

    private void initializeBoard() {
        // Clear the board array
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }

        // Clear all button texts
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }

        gameOver = false;
        currentPlayer = 'X';
        tvTurn.setText("Player X's Turn");
    }

    private void onCellClick(int row, int col) {
        // Check if cell is empty and game is not over
        if (board[row][col] == ' ' && !gameOver) {
            // Place the mark
            board[row][col] = currentPlayer;
            buttons[row][col].setText(String.valueOf(currentPlayer));

            // Check for win or draw
            if (checkWin(row, col)) {
                gameOver = true;
                tvTurn.setText("Player " + currentPlayer + " WINS!");
                disableAllButtons();
            } else if (checkDraw()) {
                gameOver = true;
                tvTurn.setText("It's a DRAW!");
                disableAllButtons();
            } else {
                // Switch player
                currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
                tvTurn.setText("Player " + currentPlayer + "'s Turn");
            }
        }
    }

    private boolean checkWin(int row, int col) {
        // Check row
        if (board[row][0] == currentPlayer &&
                board[row][1] == currentPlayer &&
                board[row][2] == currentPlayer) {
            return true;
        }

        // Check column
        if (board[0][col] == currentPlayer &&
                board[1][col] == currentPlayer &&
                board[2][col] == currentPlayer) {
            return true;
        }

        // Check diagonal (top-left to bottom-right)
        if (row == col &&
                board[0][0] == currentPlayer &&
                board[1][1] == currentPlayer &&
                board[2][2] == currentPlayer) {
            return true;
        }

        // Check anti-diagonal (top-right to bottom-left)
        if (row + col == 2 &&
                board[0][2] == currentPlayer &&
                board[1][1] == currentPlayer &&
                board[2][0] == currentPlayer) {
            return true;
        }

        return false;
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void disableAllButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    private void restartGame() {
        initializeBoard();
    }
}