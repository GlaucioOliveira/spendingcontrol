package com.goliveira.spendingcontrol.ui.notifications;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.Transaction;
import com.goliveira.spendingcontrol.model.TransactionType;
import com.goliveira.spendingcontrol.model.User;
import com.goliveira.spendingcontrol.ui.auth.ForgotPasswordActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NotificationsFragment extends Fragment {
    private final String TAG = "ConfigurationFragment";
    private NotificationsViewModel notificationsViewModel;
    private EditText editTextTextEmailAddress;
    private Switch switchBuddyNotification;
    private Button btnRequestBuddyPermission;
    private Button btnGetMyId;

    private void ToastError(String message) {
        Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_notifications, container, false);

//        final TextView textView = root.findViewById(R.id.text_notifications);
//
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        LoadFragmentViews(root);

        LoadFragmentButtonListeners();

        DisplayFirebaseData();

        return root;
    }


    private boolean ValidateActivity() {

        if (TextUtils.isEmpty(editTextTextEmailAddress.getText().toString())) {
            ToastError("Buddy Id can't be empty");
            return false;
        }

        if (editTextTextEmailAddress.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            ToastError("Your Buddy Id can't be your User Id.");
            return false;
        }

        return true;
    }

    private void LoadFragmentViews(View root) {
        editTextTextEmailAddress = root.findViewById(R.id.editTextTextEmailAddress);
        switchBuddyNotification = root.findViewById(R.id.switchBuddyNotification);
        btnRequestBuddyPermission = root.findViewById(R.id.btnRequestBuddyPermission);
        btnGetMyId = root.findViewById(R.id.btnGetMyId);
    }

    private void LoadFragmentButtonListeners() {
        btnRequestBuddyPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ValidateActivity() == false) return;
                String buddyId = editTextTextEmailAddress.getText().toString();

                isUserIdValid(buddyId);
            }
        });

        btnGetMyId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowUserId();
            }
        });


    }

    private void ShowUserId() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Current User ID");

        alertDialogBuilder
                .setMessage("Your User ID is: " + userId + "\n\nShare it with a Buddy to let him know about your transactions.")
                .setCancelable(false)
                .setPositiveButton("Copy User Id", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        // Gets a handle to the clipboard service.
                        ClipboardManager clipboard = (ClipboardManager)
                                getActivity().getSystemService(Context.CLIPBOARD_SERVICE);

                        // Creates a new text clip to put on the clipboard
                        ClipData clip = ClipData.newPlainText("User Id", userId);

                        // Set the clipboard's primary clip.
                        clipboard.setPrimaryClip(clip);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    private void DisplayFirebaseData() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User userObject = dataSnapshot.getValue(User.class);

                editTextTextEmailAddress.setText(userObject.getBuddy());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching database");
            }
        });
    }

    private void updateBuddyInformation(String buddyId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User userObject = dataSnapshot.getValue(User.class);

                String buddyId = editTextTextEmailAddress.getText().toString();

                userObject.setBuddy(buddyId);

                userRef.setValue(userObject);

                ToastError("Buddy Id saved!");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching database");
            }
        });

    }

    private void isUserIdValid(String userId) {
        DatabaseReference validUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        validUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot == null || dataSnapshot.getValue() == null) {
                    ToastError("The Buddy Id typed is invalid. Try another one.");
                } else {
                    String buddyId = editTextTextEmailAddress.getText().toString();

                    updateBuddyInformation(buddyId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Error fetching database");
            }
        });


    }

}