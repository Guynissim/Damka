package com.example.damka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConnectToGameActivity extends AppCompatActivity implements View.OnClickListener {

    Button createGameButton, joinGameButton, joinRanGameButton;
    EditText joinGameEditText;
    GameSessionManager gameSessionManager;
    AuthManager authManager;
    FirestoreManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_game);

        createGameButton = findViewById(R.id.createGameButton);
        createGameButton.setOnClickListener(this);
        joinRanGameButton = findViewById(R.id.joinRanGameButton);
        joinRanGameButton.setOnClickListener(this);
        joinGameButton = findViewById(R.id.joinGameButton);
        joinGameButton.setOnClickListener(this);
        joinGameEditText = findViewById(R.id.joinGameEditText);

        gameSessionManager = new GameSessionManager();
        authManager = new AuthManager();
        firestoreManager = new FirestoreManager();
    }

    @Override
    public void onClick(View v) {
        if (v == createGameButton)
            createGame();
        if (v == joinGameButton)
            joinSpecificGame();
        if (v == joinRanGameButton)
            joinRanGame();
    }

    private void createGame() {
        String currentPlayerId = authManager.getCurrentUserId();
        String gameId = UUID.randomUUID().toString(); // Unique game ID
        Map<String, Object> gameData = new HashMap<>();
        gameData.put("gameId", gameId);
        gameData.put("createdAt", System.currentTimeMillis());

        firestoreManager.addGameToWaitingList(gameId, gameData, task -> {
            if (task.isSuccessful()) {
                Log.d("DEBUG", "Successfully added game: " + gameId);
                gameSessionManager.createGameSession(gameId, currentPlayerId);
                startGameActivity(gameId, currentPlayerId);
            } else {
                Log.e("DEBUG", "Failed to create game.");
            }
        });
    }

    private void joinSpecificGame() {
        String gameId = String.valueOf(joinGameEditText.getText());
        String currentPlayerId = authManager.getCurrentUserId();

        gameSessionManager.joinGameSession(gameId, currentPlayerId);
        startGameActivity(gameId, currentPlayerId);
    }

    private void joinRanGame() {
        String currentPlayerId = authManager.getCurrentUserId();
        firestoreManager.getWaitingGame(task -> {
            if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                String gameId = task.getResult().getDocuments().get(0).getId();
                Log.d("DEBUG", "Joining random game: " + gameId);

                firestoreManager.removeGameFromWaitingList(gameId, task2 -> {
                    if (task2.isSuccessful()) {
                        Log.d("DEBUG", "Successfully removed waiting game: " + gameId);
                    } else {
                        Log.e("DEBUG", "Failed to remove waiting game: " + gameId);
                    }
                });
            } else {
                Log.d("DEBUG", "No random games available.");
                Toast.makeText(this, "No random games available. Try creating a game.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startGameActivity(String gameId, String playerId) {
        Intent intent = new Intent(this, Game2Activity.class);
        intent.putExtra("gameId", gameId);
        intent.putExtra("playerId", playerId);
        startActivity(intent);
    }
}