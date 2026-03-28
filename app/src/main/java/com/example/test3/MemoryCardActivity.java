package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryCardActivity extends AppCompatActivity {

    private GridLayout gridLayout;
    private TextView tvScore, tvMessage;
    private Button[][] cards = new Button[3][4];  // 3 rows, 4 columns
    private int[][] cardValues = new int[3][4];   // Values for each card
    private boolean[][] cardFlipped = new boolean[3][4];
    private boolean[][] cardMatched = new boolean[3][4];

    private int firstRow = -1, firstCol = -1;
    private int secondRow = -1, secondCol = -1;
    private boolean isChecking = false;
    private int matchesFound = 0;
    private int totalPairs = 6;

    // Card icons (emojis for visual appeal)
    private String[] cardIcons = {
            "🐶", "🐱", "🐭", "🐹",  // Dogs, Cats, Mice, Hamsters
            "🐰", "🦊", "🐻", "🐼",  // Rabbits, Foxes, Bears, Pandas
            "🐨", "🐯", "🦁", "🐮"   // Koalas, Tigers, Lions, Cows
    };

    private ArrayList<Integer> shuffledIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_card);

        gridLayout = findViewById(R.id.gridLayout);
        tvScore = findViewById(R.id.tvScore);
        tvMessage = findViewById(R.id.tvMessage);

        Button btnReset = findViewById(R.id.btnReset);
        Button btnBack = findViewById(R.id.btnBack);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemoryCardActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        initializeGame();
    }

    private void initializeGame() {
        // Create shuffled deck (6 pairs = 12 cards)
        shuffledIcons = new ArrayList<>();
        for (int i = 0; i < totalPairs; i++) {
            shuffledIcons.add(i);
            shuffledIcons.add(i);  // Add each icon twice (pair)
        }
        Collections.shuffle(shuffledIcons);

        // Fill the card values
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                cardValues[i][j] = shuffledIcons.get(index++);
                cardFlipped[i][j] = false;
                cardMatched[i][j] = false;
            }
        }

        // Clear the grid
        gridLayout.removeAllViews();
        gridLayout.setColumnCount(4);
        gridLayout.setRowCount(3);

        // Create buttons dynamically
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                final int row = i;
                final int col = j;

                Button button = new Button(this);
                button.setText("?");  // Back of card
                button.setTextSize(24);
                button.setPadding(0, 20, 0, 20);

                // Set layout parameters
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.columnSpec = GridLayout.spec(col, 1f);
                params.rowSpec = GridLayout.spec(row, 1f);
                params.setMargins(5, 5, 5, 5);
                button.setLayoutParams(params);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCardClick(row, col);
                    }
                });

                cards[row][col] = button;
                gridLayout.addView(button);
            }
        }

        matchesFound = 0;
        tvScore.setText("Matches: 0 / " + totalPairs);
        tvMessage.setText("Flip cards to match pairs!");
    }

    private void onCardClick(int row, int col) {
        // Don't allow clicks if checking or card already matched or flipped
        if (isChecking || cardMatched[row][col] || cardFlipped[row][col]) {
            return;
        }

        // Flip the card
        cardFlipped[row][col] = true;
        cards[row][col].setText(cardIcons[cardValues[row][col]]);

        // First card selected
        if (firstRow == -1 && firstCol == -1) {
            firstRow = row;
            firstCol = col;
        }
        // Second card selected
        else if (secondRow == -1 && secondCol == -1 &&
                (firstRow != row || firstCol != col)) {
            secondRow = row;
            secondCol = col;

            // Check for match
            checkMatch();
        }
    }

    private void checkMatch() {
        int firstValue = cardValues[firstRow][firstCol];
        int secondValue = cardValues[secondRow][secondCol];

        if (firstValue == secondValue) {
            // Match found!
            cardMatched[firstRow][firstCol] = true;
            cardMatched[secondRow][secondCol] = true;
            cardFlipped[firstRow][firstCol] = true;
            cardFlipped[secondRow][secondCol] = true;

            matchesFound++;
            tvScore.setText("Matches: " + matchesFound + " / " + totalPairs);

            if (matchesFound == totalPairs) {
                tvMessage.setText("🎉 Congratulations! You won! 🎉");
            } else {
                tvMessage.setText("Match found! Great job!");
            }

            // Reset temporary selection
            firstRow = -1;
            firstCol = -1;
            secondRow = -1;
            secondCol = -1;
        } else {
            // No match - flip cards back after delay
            isChecking = true;
            tvMessage.setText("No match! Try again!");

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Flip back the cards
                    cards[firstRow][firstCol].setText("?");
                    cards[secondRow][secondCol].setText("?");
                    cardFlipped[firstRow][firstCol] = false;
                    cardFlipped[secondRow][secondCol] = false;

                    firstRow = -1;
                    firstCol = -1;
                    secondRow = -1;
                    secondCol = -1;
                    isChecking = false;
                    tvMessage.setText("Flip cards to match pairs!");
                }
            }, 1000);
        }
    }

    private void resetGame() {
        firstRow = -1;
        firstCol = -1;
        secondRow = -1;
        secondCol = -1;
        isChecking = false;
        initializeGame();
    }
}