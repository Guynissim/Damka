package com.example.damka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GameActivity extends AppCompatActivity {

    private TextView player2Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Retrieve game details from Intent
        Intent intent = getIntent();
        String gameId = intent.getStringExtra("gameId");
        String playerId = intent.getStringExtra("playerId");
        String player1Name = intent.getStringExtra("player1Name");

        // Update UI with game info
        TextView gameIdText = findViewById(R.id.game_id_text);
        gameIdText.setText("Game ID: " + gameId);

        TextView player1Text = findViewById(R.id.player1_name);
        player1Text.setText("Player 1: " + player1Name);

        player2Text = findViewById(R.id.player2_name);
        player2Text.setText("Player 2: Waiting for opponent...");

        // Create and add the BoardGame view to the container
        GameSessionManager gameSessionManager = new GameSessionManager();
        BoardGame boardGame = new BoardGame(this, gameSessionManager, gameId, playerId);

        FrameLayout boardContainer = findViewById(R.id.board_container);
        boardContainer.addView(boardGame); // Add the BoardGame view dynamically

        // Listen for updates when Player 2 joins
        listenForPlayer2(gameId);
    }

    private void listenForPlayer2(String gameId) {
        DatabaseReference gameRef = FirebaseDatabase.getInstance().getReference("games").child(gameId);

        // Add a listener to detect when Player 2 joins the game
        gameRef.child("player2Name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String player2Name = snapshot.getValue(String.class);
                if (player2Name != null && !player2Name.isEmpty()) {
                    player2Text.setText("Player 2: " + player2Name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error (e.g., log it)
            }
        });
    }
}