package main.java.com.mediphix.firebase;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, Firebase!");
        FirebaseInitializer.initialize();
        FirebaseDataFetcher.fetchLatestImageUrl();
    }
}
