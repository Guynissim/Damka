package com.example.damka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

public class Game2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String gameId = intent.getStringExtra("gameId");
        String playerId = intent.getStringExtra("playerId");
        String player1Name = intent.getStringExtra("player1Name");
        GameSessionManager gameSessionManager = new GameSessionManager();
        BoardGame boardGame = new BoardGame(this, gameSessionManager, gameId, playerId);
        setContentView(boardGame);
    }
}