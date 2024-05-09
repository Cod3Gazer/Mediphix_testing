package main.java.com.mediphix.firebase;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseInitializer {

    public static void initialize() {
        try {
            System.out.println("In FirebaseInitializer - initialize!");
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/mediphix-firebase-adminsdk-6h8zc-8f3dcf5082.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://mediphix-default-rtdb.firebaseio.com/")
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase Initialized");
        } catch (IOException e) {
            System.out.println("Error initializing Firebase");
            e.printStackTrace();
        }
    }
}
