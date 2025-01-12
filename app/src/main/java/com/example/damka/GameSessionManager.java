package com.example.damka;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSessionManager {
    private DatabaseReference gameRef;

    public GameSessionManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        gameRef = database.getReference("GameSessions");
    }

    public void createGameSession(String gameId, String playerId) {
        HashMap<String, Object> initialState = new HashMap<>();
        initialState.put("player1", playerId);
        initialState.put("player2", null);
        initialState.put("turn", "player1");
        initialState.put("boardState", null);

        gameRef.child(gameId).setValue(initialState).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("DEBUG", "Game session created successfully");
            } else {
                Log.e("DEBUG", "Failed to create game session: " + task.getException());
            }
        });
    }

    public void joinGameSession(String gameId, String playerId) {
        gameRef.child(gameId).child("player2").setValue(playerId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("DEBUG", "Player 2 has joined the game!");
            } else {
                Log.e("DEBUG", "Failed to join game session: " + task.getException());
            }
        });
    }

    public void updateGameState(String gameId, Map<String, Object> gameState) {
        gameRef.child(gameId).updateChildren(gameState).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("DEBUG", "Game state updated successfully");
            } else {
                Log.e("DEBUG", "Failed to update game state: " + task.getException());
            }
        });
    }

    public void listenToGameSession(String gameId, GameStateListener listener) {
        gameRef.child(gameId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String, Object> gameState = (HashMap<String, Object>) snapshot.getValue();
                if (gameState != null) {
                    listener.onGameStateChanged(gameState);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onError(error.toException());
            }
        });
    }

    public interface GameStateListener {
        void onGameStateChanged(Map<String, Object> gameState);

        void onError(@NonNull Exception e);
    }
}