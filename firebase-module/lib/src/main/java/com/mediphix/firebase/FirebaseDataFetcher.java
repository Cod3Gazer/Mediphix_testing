package main.java.com.mediphix.firebase;

import com.google.firebase.database.*;

public class FirebaseDataFetcher {

    public static void fetchLatestImageUrl() {
        System.out.println("In FirebaseDataFetcher - fetchLatestImageUrl!");


        // Get a reference to our posts
final FirebaseDatabase database = FirebaseDatabase.getInstance();
DatabaseReference ref = FirebaseDatabase.getInstance()
.getReference("Drugs");

// Attach a listener to read the data at our posts reference
ref.addValueEventListener(new ValueEventListener() {
  @Override
  public void onDataChange(DataSnapshot dataSnapshot) {
    System.out.println(dataSnapshot);
  }

  @Override
  public void onCancelled(DatabaseError databaseError) {
    System.out.println("The read failed: " + databaseError.getCode());
  }
});

    }
}
