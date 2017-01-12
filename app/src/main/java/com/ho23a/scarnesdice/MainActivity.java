package com.ho23a.scarnesdice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int MAX_ROLL = 6;
    private static final int WIN_SCORE = 25;
    private static final double DECISION_SCORE = 10.5;

    private final Handler timerHandler = new Handler();
    private Random random = new Random();

    private int playerTotal;
    private int computerTotal;
    private int currentTurn;
    private boolean gameWon;

    private Players whosTurn;
    private ImageView diceView;
    private EditText turnScoreText;
    private TextView playerScoreText;
    private TextView computerScoreText;
    private TextView actionText;

    enum Players {
        YOU,
        COMPUTER,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerTotal = 0;
        computerTotal = 0;
        currentTurn = 0;
        whosTurn = Players.YOU;
        diceView = ((ImageView) findViewById(R.id.diceView));
        turnScoreText = ((EditText) findViewById(R.id.turnScoreText));
        gameWon = false;

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

    private void computerTurnIn500() {
        timerHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                computerTurn();
                if (whosTurn == Players.COMPUTER && gameWon == false) {
                    computerTurnIn500();
                }
            }
        }, 1000);
    }

    private void computerTurn() {
        if (computerTotal + currentTurn >= WIN_SCORE || (currentTurn > DECISION_SCORE)) {
            hold();
        } else {
            roll();
        }
    }

    public void roll() {
        int rolled = rollDice();

        switch(rolled) {
            case(1):
                diceView.setImageResource(R.drawable.dice1);
                Toast toast = Toast.makeText(getApplicationContext(), R.string.change_player, Toast.LENGTH_SHORT);
                toast.show();
                changePlayers();
                break;
            case(2):
                diceView.setImageResource(R.drawable.dice2);
                break;
            case(3):
                diceView.setImageResource(R.drawable.dice3);
                break;
            case(4):
                diceView.setImageResource(R.drawable.dice4);
                break;
            case(5):
                diceView.setImageResource(R.drawable.dice5);
                break;
            case(6):
                diceView.setImageResource(R.drawable.dice6);
                break;
            default:
                break;
        }

        if (rolled != 1) {
            currentTurn += rolled;
            turnScoreText.setText(Integer.toString(currentTurn));
            checkForWin();
        } else {
            changePlayers();
        }
    }

    public void hold() {
        if (whosTurn == Players.YOU) {
            addPlayerScore(currentTurn);
        } else {
            addComputerScore(currentTurn);
        }
        changePlayers();
//        String.format(getString(R.string.))
    }

    public void reset() {
        resetScore();
//        enableButtons();
    }

    private int rollDice() {
        return random.nextInt(MAX_ROLL) + 1;
    }

    private void changePlayers() {
        String message;
        if (whosTurn == Players.YOU) {
            whosTurn = Players.COMPUTER;
            message = "Computer's Turn";
            computerTurnIn500();
        } else {
            whosTurn = Players.YOU;
            message = "Your Turn";
        }
        ((TextView) findViewById(R.id.actionText)).setText(message);

        currentTurn = 0;
        turnScoreText.setText(Integer.toString(currentTurn));
    }

    private void addPlayerScore(int currentTurn) {
        playerTotal += currentTurn;
        ((EditText) findViewById(R.id.playerScoreText)).setText(Integer.toString(playerTotal));
    }

    private void addComputerScore(int currentTurn) {
        computerTotal += currentTurn;
        ((EditText) findViewById(R.id.computerScoreText)).setText(Integer.toString(computerTotal));
    }

    private void checkForWin() {
        if (whosTurn == Players.YOU && playerTotal + currentTurn >= WIN_SCORE) {
            playerWins();
        } else if (whosTurn == Players.COMPUTER && computerTotal + currentTurn >= WIN_SCORE) {
            computerWins();
        }
    }

    public static final String USER_SCORE = "com.ho23a.scarnesdice.USER_SCORE";
    private void playerWins() {
        gameWon = true;
        Intent intent = new Intent(this, WinActivity.class);
        intent.putExtra(USER_SCORE, String.valueOf(playerTotal + currentTurn));
        startActivity(intent);
        resetScore();
    }

    private void computerWins() {
        gameWon = true;
        startActivity(new Intent(this, LoseActivity.class));
        resetScore();
    }

    private void resetScore() {
        playerTotal = 0;
        computerTotal = 0;
        whosTurn = Players.YOU;
        currentTurn = 0;
        gameWon = false;
        ((EditText) findViewById(R.id.playerScoreText)).setText("0");
        ((EditText) findViewById(R.id.computerScoreText)).setText("0");
        ((TextView) findViewById(R.id.actionText)).setText(R.string.player_turn);
        turnScoreText.setText("0");
    }

    private void disableButtons() {
        ((Button) findViewById(R.id.rollButton)).setEnabled(false);
        ((Button) findViewById(R.id.holdButton)).setEnabled(false);
    }

    private void enableButtons() {
        ((Button) findViewById(R.id.rollButton)).setEnabled(true);
        ((Button) findViewById(R.id.holdButton)).setEnabled(true);
    }
}
