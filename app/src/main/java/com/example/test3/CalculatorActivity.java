package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private String currentInput = "";
    private double firstNumber = 0;
    private double secondNumber = 0;
    private String operator = "";
    private boolean isNewOperation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        tvDisplay = findViewById(R.id.tvDisplay);
        tvDisplay.setText("0");

        // Number buttons
        setupNumberButton(R.id.btn0, "0");
        setupNumberButton(R.id.btn1, "1");
        setupNumberButton(R.id.btn2, "2");
        setupNumberButton(R.id.btn3, "3");
        setupNumberButton(R.id.btn4, "4");
        setupNumberButton(R.id.btn5, "5");
        setupNumberButton(R.id.btn6, "6");
        setupNumberButton(R.id.btn7, "7");
        setupNumberButton(R.id.btn8, "8");
        setupNumberButton(R.id.btn9, "9");
        setupNumberButton(R.id.btnDot, ".");

        // Operator buttons
        setupOperatorButton(R.id.btnAdd, "+");
        setupOperatorButton(R.id.btnSubtract, "-");
        setupOperatorButton(R.id.btnMultiply, "×");
        setupOperatorButton(R.id.btnDivide, "÷");

        // Special buttons
        Button btnClear = findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll();
            }
        });

        Button btnBackspace = findViewById(R.id.btnBackspace);
        btnBackspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backspace();
            }
        });

        Button btnPercent = findViewById(R.id.btnPercent);
        btnPercent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculatePercentage();
            }
        });

        Button btnEqual = findViewById(R.id.btnEqual);
        btnEqual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalculatorActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setupNumberButton(int buttonId, final String value) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNumberPressed(value);
            }
        });
    }

    private void setupOperatorButton(int buttonId, final String op) {
        Button button = findViewById(buttonId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOperatorPressed(op);
            }
        });
    }

    private void onNumberPressed(String value) {
        if (isNewOperation) {
            currentInput = "";
            isNewOperation = false;
        }

        // Prevent multiple leading zeros
        if (currentInput.equals("0") && !value.equals(".")) {
            currentInput = "";
        }

        // Prevent multiple decimals
        if (value.equals(".") && currentInput.contains(".")) {
            return;
        }

        currentInput += value;
        tvDisplay.setText(currentInput);
    }

    private void onOperatorPressed(String op) {
        if (!currentInput.isEmpty()) {
            firstNumber = Double.parseDouble(currentInput);
            operator = op;
            isNewOperation = true;
        } else if (firstNumber != 0 && operator.isEmpty()) {
            operator = op;
            isNewOperation = true;
        }
    }

    private void backspace() {
        if (!currentInput.isEmpty() && !isNewOperation) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            if (currentInput.isEmpty()) {
                currentInput = "0";
                isNewOperation = true;
            }
            tvDisplay.setText(currentInput);
        } else if (isNewOperation && !currentInput.isEmpty()) {
            currentInput = "0";
            tvDisplay.setText(currentInput);
        }
    }

    private void calculatePercentage() {
        if (!currentInput.isEmpty() && !isNewOperation) {
            double number = Double.parseDouble(currentInput);
            number = number / 100;

            // Format the result
            if (number == (int) number) {
                currentInput = String.valueOf((int) number);
            } else {
                currentInput = String.valueOf(number);
            }
            tvDisplay.setText(currentInput);
        }
    }

    private void calculateResult() {
        if (operator.isEmpty() || currentInput.isEmpty() || isNewOperation) {
            return;
        }

        secondNumber = Double.parseDouble(currentInput);
        double result = 0;

        switch (operator) {
            case "+":
                result = firstNumber + secondNumber;
                break;
            case "-":
                result = firstNumber - secondNumber;
                break;
            case "×":
                result = firstNumber * secondNumber;
                break;
            case "÷":
                if (secondNumber == 0) {
                    tvDisplay.setText("Error: Can't divide by 0");
                    clearAll();
                    return;
                }
                result = firstNumber / secondNumber;
                break;
        }

        // Format result to remove .0 if it's a whole number
        if (result == (int) result) {
            currentInput = String.valueOf((int) result);
        } else {
            // Limit decimal places to 8
            currentInput = String.format("%.8f", result).replaceAll("0*$", "").replaceAll("\\.$", "");
        }

        tvDisplay.setText(currentInput);
        firstNumber = result;
        operator = "";
        isNewOperation = true;
    }

    private void clearAll() {
        currentInput = "";
        firstNumber = 0;
        secondNumber = 0;
        operator = "";
        isNewOperation = true;
        tvDisplay.setText("0");
    }
}