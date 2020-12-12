package com.goliveira.spendingcontrol.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private String uid;
    private String email;
    private String displayName;

    private boolean hasBuddy;
    private String buddy;
    private String buddyName;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = database.getReference();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
        this.hasBuddy = false;
        this.buddy = "";
        this.buddyName = "";
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() { return displayName; }

    public void setDisplayName(String displayName) {  this.displayName = displayName; }

    public boolean isHasBuddy() {
        return hasBuddy;
    }

    public void setHasBuddy(boolean hasBuddy) {
        this.hasBuddy = hasBuddy;
    }

    public String getBuddy() {
        return buddy;
    }

    public void setBuddy(String buddy) {
        this.buddy = buddy;
    }

    public String getBuddyName() {
        return buddyName;
    }

    public void setBuddyName(String buddyName) {
        this.buddyName = buddyName;
    }

    public void save() {
        databaseRef.child("users/" + this.getUid()).setValue(this);
        Log.d("SignUpActivity", "Created user in database");
    }
}
