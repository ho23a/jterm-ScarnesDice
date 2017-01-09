package com.ho23a.scarnesdice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int MAX_ROLL = 6;
    private static final int WIN_SCORE = 20;
    private int playerTotal;
    private int computerTotal;
    private int currentTurn;
    private Players whosTurn;
    private Random random = new Random();
    private Toast toast;

    enum Players {
        PLAYER,
        COMPUTER,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerTotal = 0;
        computerTotal = 0;
        currentTurn = 0;
        whosTurn = Players.PLAYER;
        String message = "It's Your Turn";
        toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();

        ((Button) findViewById(R.id.rollButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                roll();
            }
        });

        ((Button) findViewById(R.id.holdButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hold();
            }
        });

        ((Button) findViewById(R.id.resetButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
            }
        });
    }

    public void roll() {
        int rolled = rollDice();
        ((EditText) findViewById(R.id.rollText)).setText(Integer.toString(rolled), TextView.BufferType.EDITABLE);

        if (rolled == 1) {
            changePlayers();
        } else {
            currentTurn += rolled;
            ((EditText) findViewById(R.id.turnScoreText)).setText(Integer.toString(currentTurn), TextView.BufferType.EDITABLE);
        }


    }

    public void hold() {
        if (whosTurn == Players.PLAYER) {
            addPlayerScore(currentTurn);
        } else {
            addComputerScore(currentTurn);
        }
    }

    public void reset() {
        resetScore();
    }

    private int rollDice() {
        return random.nextInt(MAX_ROLL) + 1;
    }

    private void changePlayers() {
        currentTurn = 0;
        ((EditText) findViewById(R.id.turnScoreText)).setText(Integer.toString(currentTurn), TextView.BufferType.EDITABLE);

        String message = "";
        if (whosTurn == Players.PLAYER) {
            whosTurn = Players.COMPUTER;
            message = "It's Computer's Turn";
        } else {
            whosTurn = Players.PLAYER;
            message = "It's Your Turn";
        }
        toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toast.show();
    }

    private void addPlayerScore(int currentTurn) {
        playerTotal += currentTurn;
        ((EditText) findViewById(R.id.playerScoreText)).setText(Integer.toString(playerTotal), TextView.BufferType.EDITABLE);
        if (playerTotal >= WIN_SCORE) {
            toast = Toast.makeText(getApplicationContext(), "You Won!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            changePlayers();
        }
    }

    private void addComputerScore(int currentTurn) {
        computerTotal += currentTurn;
        ((EditText) findViewById(R.id.computerScoreText)).setText(Integer.toString(computerTotal), TextView.BufferType.EDITABLE);
        if (computerTotal >= WIN_SCORE) {
            toast = Toast.makeText(getApplicationContext(), "Sorry, you lost! Computer Won!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            changePlayers();
        }
    }

    private void resetScore() {
        playerTotal = 0;
        computerTotal = 0;
        whosTurn = Players.PLAYER;
        currentTurn = 0;
        ((EditText) findViewById(R.id.playerScoreText)).setText("0", TextView.BufferType.EDITABLE);
        ((EditText) findViewById(R.id.computerScoreText)).setText("0", TextView.BufferType.EDITABLE);
        ((EditText) findViewById(R.id.rollText)).setText("0", TextView.BufferType.EDITABLE);
        ((EditText) findViewById(R.id.turnScoreText)).setText("0", TextView.BufferType.EDITABLE);
    }
}
