package com.goliveira.spendingcontrol.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class User {

    private String uid;
    private String email;
    private String displayName;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseRef = database.getReference();

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String uid, String email, String displayName) {
        this.uid = uid;
        this.email = email;
        this.displayName = displayName;
    }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() { return displayName; }

    public void setDisplayName(String displayName) {  this.displayName = displayName; }

    public void save() {
        databaseRef.child("users/" + this.getUid()).setValue(this);
        Log.d("SignUpActivity", "Created user in database");
    }
}
