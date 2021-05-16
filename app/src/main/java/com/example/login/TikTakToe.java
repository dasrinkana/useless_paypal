package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TikTakToe extends AppCompatActivity implements View.OnClickListener {

    private TextView playerOneScore, playerTwoScore, playerStatus;
    private Button[] buttons = new Button[9];
    private Button resetGame;

    private int playerOneScoreCount, playerTwoScoreCount, roundCount;
    boolean activePlayer;

    int[] gameState = {2,2,2,2,2,2,2,2,2};                          //alle 3 Zustände: X , O , nichts
    int[][] winningPositions = { {0, 1, 2}, {3, 4, 5}, {6, 7, 8},   //waagerrecht
                                 {0, 3, 6}, {1, 4, 7}, {2, 5, 8},   //senkrecht
                                 {0, 4, 8}, {2, 4, 6}               //diagonal
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tik_tak_toe);      //hollt sich die daten von der XML datei

        playerOneScore =(TextView) findViewById(R.id.playerOneScore);       //holt sich die daten aus der XML datei und überschreibt diese in eine Variable
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        playerStatus = (TextView) findViewById(R.id.playerStatus);

        resetGame = (Button) findViewById(R.id.resetGame);

        for (int i=0; i<buttons.length; i++){                   //findet hierdurch heraus welcher Button geklickt wurde
            String buttonID = "btn_" + i;                       //diese Button ID wird dann in die Variable Button Id überschrieben
            int resourceID = getResources().getIdentifier(buttonID,"id",getPackageName());      //
            buttons[i]=(Button) findViewById(resourceID);           //übergibt hier diese resourceIds an der stelle des arrays (0,1,2,...,8): bt_0;btn_1;btn_2;btn_3; ... ;btn_8
            buttons[i].setOnClickListener(this);                    //wenn ein button geklickt wird, wird der Button an der Stelle angesprochen
        }
        roundCount = 0;                                 //
        playerOneScoreCount = 0;
        playerTwoScoreCount = 0;
        activePlayer = true;
    }


    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {            //wenn nicht leer ist dann gehe aus der Funktion raus und gebe nichts zurück
            return;
        }
        String buttonID = v.getResources().getResourceEntryName(v.getId());         //holt sich die ID von dem geklickten Button
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length() - 1, buttonID.length()));          //

        if (activePlayer) {             //der erste Spieler beginnt immer mit X also dem Zustand 0 bei gamestate
            ((Button) v).setText("X");
            ((Button) v).setTextColor(Color.parseColor("#FFC34A"));
            gameState[gameStatePointer] = 0;
        } else {                        //der zweite Spieler bekommt immer O also den Zustand 1
            ((Button) v).setText("O");
            ((Button) v).setTextColor(Color.parseColor("#70FFEA"));
            gameState[gameStatePointer] = 1;
        }
        roundCount++;                       //Runden-Zähler

        if (checkWinner()) {            //wird gecheckt in Methode
            if (activePlayer) {         //wenn der erste Spieler
                playerOneScoreCount++;      //erste Spieler bekommt einen Punkt
                updatePlayerScore();        //Methode zur veränderung der Punkteanzeige wird aufgerufen
                Toast.makeText(this, "Player One Won!", Toast.LENGTH_SHORT).show();     //Text wird eingelendet das der erste Spieler gewonnen hat
                playAgain();                            //reset aber ohne Punkte reset
            } else {                                    //sonst bekommt der andere Spieler einen Punkt
                playerTwoScoreCount++;
                updatePlayerScore();
                Toast.makeText(this, "Player Two Won!", Toast.LENGTH_SHORT).show();
                playAgain();
            }

        } else if (roundCount == 9) {           //falls schon auf alle 9 buttons geklickt wurde beginne ein neues Spiel und gebe aus das niemand gewonnen hat
            playAgain();
            Toast.makeText(this, "No Winnner!", Toast.LENGTH_SHORT).show();
        } else {                //falls nicht von dem dann sage das der nicht activePlayer der activePlayer ist
            activePlayer = !activePlayer;
        }
        if (playerOneScoreCount > playerTwoScoreCount) {            //wenn der eine mehr Punkte hat als der andere hat der gewonnen
            playerStatus.setText("Player One is Winning!");
        } else if (playerTwoScoreCount > playerOneScoreCount) {         //und andersrum
            playerStatus.setText("Player Two is Winning!");
        } else {                                            //oder keiner von beiden
            playerStatus.setText("");
        }

        resetGame.setOnClickListener(new View.OnClickListener() {           //game wrd komplett zurückgesetzt durch das clicke auf den Button mit der id resetGame
            @Override
            public void onClick(View v) {
                playAgain();
                playerOneScoreCount = 0;
                playerTwoScoreCount = 0;
                playerStatus.setText("");
                updatePlayerScore();
            }


        });
    }
    public boolean checkWinner(){
        boolean winnerResult = false;                   //erstmal false gesetzt damit wenn die Bedingung nicht Zutrifft false zurückgegeben wird

        for (int []winningPosion:winningPositions){                 //vererbung der Gewinner Möglichkeiten in die Schleife
            if (gameState[winningPosion[0]]== gameState[winningPosion[1]]&&         //bedingung wird abgefragt ob eine dieser 8 Möglichkeiten zutrifft
                    gameState[winningPosion[1]]==gameState[winningPosion[2]]&&
                    gameState[winningPosion[0]]!=2){
                winnerResult=true;                              //wenn ja dann true

            }
        }
        return winnerResult;                    //rückgabe ob der Zustand des Gewinnens eingetreten ist oder halt nicht

    }
    public void updatePlayerScore(){                                    //Punkteanzeige wird hier aktualisiert
        playerOneScore.setText(Integer.toString(playerOneScoreCount));
        playerTwoScore.setText(Integer.toString(playerTwoScoreCount));
    }
    public void playAgain(){                //nach runden schluss wird alles wieder auf anfang gesetzt außer die Punkteanzeige
        roundCount = 0;
        activePlayer = true;

        for (int i= 0; i<buttons.length; i++){              //schleife für das zurücksetzten der Werte alle auf 2
            gameState[i]=2;
            buttons[i].setText("");
        }
    }
}