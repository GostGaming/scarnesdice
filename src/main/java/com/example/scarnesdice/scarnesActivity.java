package com.example.scarnesdice;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class scarnesActivity extends AppCompatActivity {

    private final int MAX = 100;
    private int turnScore = 0;
    private int playerTotal = 0;
    private int compTotal = 0;
    private Button rollButton, holdButton, resetButton;
    private String scoreStr;

    private boolean playerTurn = true;
    private ImageView die;
    private Drawable die1, die2, die3, die4, die5, die6;
    private TextView scoreView, titleBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rollButton = findViewById(R.id.rollButton);
        holdButton = findViewById(R.id.holdButton);
        resetButton = findViewById(R.id.resetButton);

        // overwrite default text immediately
        scoreStr = String.format("Your score: %d Computer Score: %d Turn score %d", playerTotal, compTotal, turnScore);
        scoreView = findViewById(R.id.scores);
        titleBar = findViewById(R.id.titleBar);
        // Would be better as an array, would also get rid of switch stuff
        die1 = getResources().getDrawable(R.drawable.dice1);
        die2 = getResources().getDrawable(R.drawable.dice2);
        die3 = getResources().getDrawable(R.drawable.dice3);
        die4 = getResources().getDrawable(R.drawable.dice4);
        die5 = getResources().getDrawable(R.drawable.dice5);
        die6 = getResources().getDrawable(R.drawable.dice6);

        die = findViewById(R.id.diceView);


    }

    public void onRollClick(View v) {
        /* roll random 1-6, add to turn,
         * if it's a 1, end turn and set turn to 0
         */

        // Would be better as an array, would also get rid of switch stuff

        int result = rollDie();
        Log.d("debug", Integer.toString(result));
        if (playerTurn) {
            titleBar.setText("Rolling for Player...");
        }
        else titleBar.setText("Rolling for Computer...");

        switch (result) {

            case 0:

                // set turn score to 0 and display 1 face
                die.setImageDrawable(die1);
                turnScore = 0;

                if (playerTurn) {
                    playerTurn = false;
                    titleBar.setText("You rolled a 1!");
                    holdButton.setEnabled(false);
                    rollButton.setEnabled(false);
                } else {
                    titleBar.setText("Computer rolled a 1!");
                    holdButton.setEnabled(true);
                    rollButton.setEnabled(true);
                    playerTurn = true;
                }

                break;
            case 1:
                // add 2 to turn score and display 2 face
                die.setImageDrawable(die2);
                turnScore += 2;

                break;
            case 2:
                // add 3 to turn score and display 3 face
                die.setImageDrawable(die3);
                turnScore += 3;

                break;
            case 3:
                // add 4 to turn score and display 4 face
                die.setImageDrawable(die4);
                turnScore += 4;

                break;
            case 4:
                // add 5 to turn score and display 5 face
                die.setImageDrawable(die5);
                turnScore += 5;

                break;
            case 5:
                // add 6 to turn score and display 6 face
                die.setImageDrawable(die6);
                turnScore += 6;

                break;
        }
        scoreStr = String.format("Your score: %d Computer Score: %d Turn score %d", playerTotal, compTotal, turnScore);
        scoreView.setText(scoreStr);
        if(!playerTurn) computerTurn();
    }

    public void onHoldClick(View v) {
        // add score to total, THEN reset turn to zero
        Log.d("debug", "HOLDING" + turnScore);
        if (playerTurn) {
            playerTotal += turnScore;
        } else {
            compTotal += turnScore;
        }
        turnScore = 0;


        if (playerTurn && playerTotal < MAX && compTotal < MAX) {
            playerTurn = false;
            scoreStr = String.format("Your score: %d Computer Score: %d Turn score %d", playerTotal, compTotal, turnScore);
            scoreView.setText(scoreStr);
            computerTurn();
        }
        else if (playerTotal >= MAX && compTotal < MAX) {
            titleBar.setText("YOU WIN! Please press RESET");
            playerTurn = true;
            holdButton.setEnabled(false);
            rollButton.setEnabled(false);
        }
        else if (playerTotal < MAX && compTotal >= MAX) {
            titleBar.setText("YOU LOSE! Please press RESET");
            playerTurn = true;
            holdButton.setEnabled(false);
            rollButton.setEnabled(false);
        }
        else {
            playerTurn = true;
            scoreStr = String.format("Your score: %d Computer Score: %d Turn score %d", playerTotal, compTotal, turnScore);
        }
       scoreView.setText(scoreStr);
    }

    public void onResetClick(View v) {
        // reset all scores to zero
        Log.d("debug", "RESETTING");
        titleBar.setText("Scarne's Dice!");
        playerTotal = 0;
        compTotal = 0;
        turnScore = 0;
        scoreStr = String.format("Your score: %d Computer Score: %d Turn score %d", playerTotal, compTotal, turnScore);
        scoreView.setText(scoreStr);
        playerTurn = true;
        holdButton.setEnabled(true);
        rollButton.setEnabled(true);

    }
    final Handler hands = new Handler();


    public void computerTurn() {

        if (turnScore < 20 && (turnScore + 20 < MAX)) {
            holdButton.setEnabled(false);
            rollButton.setEnabled(false);
            // add small delay so we can follow comp rolls
            hands.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onRollClick(rollButton);
                }
            }, 700);
        } else {
            holdButton.setEnabled(true);
            rollButton.setEnabled(true);
            onHoldClick(holdButton);
            playerTurn = true;

        }

    }

    public int rollDie() {
        Random roll = new Random();
        return roll.nextInt(6);
    }
}

