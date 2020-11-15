package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// implementacja OnClickListener zostala dodana do klasy poniewaz bede uzywac napisanej funckji
// onClick w celu wywolywania akcji po wcisnieciu przycisku
// dziedziczenie po AppCompatActivity to standard androida
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    // deklaracje przyciskow i zmiennych streujacych gra
    private TextView playerOneScore, playerTwoScore, winner;
    private Button[] buttons = new Button[9];
    private Button resetGame;

    private int playerOneScoreInt, playerTwoScoreIn, roundInt;
    boolean who;

    // ulozenie kolek lub krzyzykow ktore daja wygrana
    int [][] winningOptions = {
            {0,1,2}, {3,4,5}, {6,7,8}, //wiersze
            {0,3,6}, {1,4,7}, {2,5,8},  //kolumny
            {0,4,8}, {2,4,6}  //przekatne
    };

    //aktualny stan gry
    int state[] = {2,2,2,2,2,2,2,2,2}; // ta tablica przechowuje wartosci ktore mowia o tym ktory gracz gdzie kliknal

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // przypisanie text view do zmiennych
        playerOneScore = (TextView) findViewById(R.id.playerOneScore);
        playerTwoScore = (TextView) findViewById(R.id.playerTwoScore);
        winner = (TextView) findViewById(R.id.winner);

        //przypisanie przyciskow do zmiennych
        resetGame = (Button) findViewById(R.id.btnReset);

        // mamy 9 przyciskow wiec najlepiej jest je przypisac w petli
        // od razu zostanie dodany tez onClick listner ktory odpowiada za akcje po kliknieciu
        for(int i = 0; i < buttons.length; i++){
            String buttonId = "btn" + i;
            int trueId = getResources().getIdentifier(buttonId, "id", getPackageName());

            buttons[i] = (Button) findViewById(trueId);
            buttons[i].setOnClickListener(this);
        }

        resetGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgain();
                playerOneScoreInt=0;
                playerTwoScoreIn=0;
                winner.setText("");
                updateScore();
            }
        });


        roundInt = 0;
        playerOneScoreInt = 0;
        playerTwoScoreIn = 0;
        who = true;

    }

    // implementacja onClick funkcji ktora wywoluje sie po wcisnieciu buttona
    @Override
    public void onClick(View v) {
        //sprawdza czy text przycisku jest pusty czy nie
        if(!((Button)v).getText().toString().equals("")){
            return;
        }

        //zwraca nazwe zasobu
        String buttonId = v.getResources().getResourceEntryName(v.getId());
        int statePointer = Integer.parseInt(buttonId.substring(buttonId.length()-1, buttonId.length()));

        // sterowanie zalezne od tego ktory gracz aktualnie wybiera
        if(who){
            ((Button)v).setText("X");
            ((Button)v).setTextColor(Color.parseColor("#FFC3A4"));
            //0-playerone 1-playertwo
            state[statePointer] = 0;
        }else{
            ((Button)v).setText("O");
            ((Button)v).setTextColor(Color.parseColor("#70FFEA"));
            //0-playerone 1-playertwo
            state[statePointer] = 1;
        }
        roundInt++;

        // sprawdza wygranego i wyswietla informacje
        if(isWinner()){
            if(who){
                playerOneScoreInt++;
                updateScore();
                Toast.makeText(this, "Gracz 1 wygral", Toast.LENGTH_SHORT).show();
                playAgain();
            }else{
                playerTwoScoreIn++;
                updateScore();
                Toast.makeText(this, "Gracz 2 wygral", Toast.LENGTH_SHORT).show();
                playAgain();
            }
        }else if(roundInt == 9){
            playAgain();
            Toast.makeText(this, "Brak zwyciescy", Toast.LENGTH_SHORT).show();
        }else{
            who = !who;
        }

        // sprawdza wygranego
        if(playerOneScoreInt > playerTwoScoreIn){
            winner.setText("Gracz 1 wygral");
        }else if(playerTwoScoreIn > playerOneScoreInt){
            winner.setText("Gracz 2 wygral");
        }else{
            winner.setText("Brak zwyciescy");
        }
    }

    // funckja zwracajaca prawde lub falsz w zaleznosci  czy ktos jest juz zwiciesca czy jeszcze nie
    public boolean isWinner(){
        boolean result = false;

        //sprawdza czy ktorys z graczy wygral
        for(int [] winningPosition: winningOptions){
            if(state[winningPosition[0]] == state[winningPosition[1]] &&
               state[winningPosition[1]] == state[winningPosition[2]] &&
               state[winningPosition[0]] != 2){
                result = true;
            }
        }
        return result;
    }

    // funkcja updatujaca wynik gracza na ekranie
    public void updateScore(){
        playerOneScore.setText(Integer.toString(playerOneScoreInt));
        playerTwoScore.setText(Integer.toString(playerTwoScoreIn));
    }


    // implementacja resetu czyli gry od nowa
    public void playAgain(){
        roundInt = 0;
        who = true;

        for(int i=0;i< buttons.length;i++){
            state[i] = 2;
            buttons[i].setText("");
        }
    }
}