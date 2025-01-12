package com.example.damka;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import java.util.HashMap;
import java.util.Map;
public class FirestoreManager {
    private final FirebaseFirestore db;

    public FirestoreManager() {
        db = FirebaseFirestore.getInstance();
    }

    /**
     * Save a user's profile to Firestore.
     *
     * @param userId   The user's unique ID.
     * @param username The user's chosen username.
     * @param wins     The number of wins.
     * @param losses   The number of losses.
     * @param listener Listener for the completion of the operation.
     */
    public void saveUserProfile(String userId, String username, int wins, int losses, OnCompleteListener<Void> listener) {
        Map<String, Object> userProfile = new HashMap<>();
        userProfile.put("username", username);
        userProfile.put("wins", wins);
        userProfile.put("losses", losses);

        db.collection("Users").document(userId)
                .set(userProfile)
                .addOnCompleteListener(listener);
    }

    /**
     * Fetch a user's profile from Firestore.
     *
     * @param userId   The user's unique ID.
     * @param listener Listener for the completion of the operation.
     */
    public void getUserProfile(String userId, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("Users").document(userId)
                .get()
                .addOnCompleteListener(listener);
    }

    /**
     * Query the Firestore database for the first waiting game.
     *
     * @param listener Listener for the completion of the operation.
     */
    public void getWaitingGame(OnCompleteListener<QuerySnapshot> listener) {
        db.collection("WaitingGames").limit(1)
                .get()
                .addOnCompleteListener(listener);
    }

    /**
     * Add a new game to the "WaitingGames" collection in Firestore.
     *
     * @param gameId   The unique ID for the game.
     * @param gameData The game data to be stored.
     * @param listener Listener for the completion of the operation.
     */
    public void addGameToWaitingList(String gameId, Map<String, Object> gameData, OnCompleteListener<Void> listener) {
        db.collection("WaitingGames").document(gameId)
                .set(gameData)
                .addOnCompleteListener(listener);
    }

    /**
     * Remove a game from the "WaitingGames" collection once it's joined.
     *
     * @param gameId   The unique ID for the game.
     * @param listener Listener for the completion of the operation.
     */
    public void removeGameFromWaitingList(String gameId, OnCompleteListener<Void> listener) {
        db.collection("WaitingGames").document(gameId)
                .delete()
                .addOnCompleteListener(listener);
    }

    /**
     * Update game metadata in Firestore.
     *
     * @param gameId   The unique ID for the game.
     * @param gameData The updated game data.
     * @param listener Listener for the completion of the operation.
     */
    public void updateGameMetadata(String gameId, Map<String, Object> gameData, OnCompleteListener<Void> listener) {
        db.collection("Games").document(gameId)
                .update(gameData)
                .addOnCompleteListener(listener);
    }

    /**
     * Fetch game metadata from Firestore.
     *
     * @param gameId   The unique ID for the game.
     * @param listener Listener for the completion of the operation.
     */
    public void getGameMetadata(String gameId, OnCompleteListener<DocumentSnapshot> listener) {
        db.collection("Games").document(gameId)
                .get()
                .addOnCompleteListener(listener);
    }
}
