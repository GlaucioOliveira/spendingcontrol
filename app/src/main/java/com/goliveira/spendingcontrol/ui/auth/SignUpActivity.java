package com.goliveira.spendingcontrol.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goliveira.spendingcontrol.R;
import com.goliveira.spendingcontrol.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    public FirebaseAuth mAuth;
    Button signUpButton;
    EditText signUpEmailTextInput;
    EditText signUpPasswordTextInput;
    EditText signUpDisplayNameTextInput;
    CheckBox agreementCheckBox;
    TextView errorView;
    TextView lnkTermsAndConditions;

    private void ToastError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        LoadFragmentViews();

        LoadFragmentButtonListeners();

        TryToRecoverEmailInput();
    }

    private void TryToRecoverEmailInput(){
        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        if(TextUtils.isEmpty(userEmail) == false){
            signUpEmailTextInput.setText(userEmail);
        }
    }

    private boolean ValidateActivity() {
        if (signUpDisplayNameTextInput.getText().toString().contentEquals("")) {
            ToastError("You need yo time your name");
            return false;
        }

        if (signUpEmailTextInput.getText().toString().contentEquals("")) {
            ToastError("Email can't be empty");
            return false;
        }

        if (signUpPasswordTextInput.getText().toString().contentEquals("")) {
            ToastError("Password can't be empty");
            return false;
        }

        if (!agreementCheckBox.isChecked()) {
            ToastError("Please agree to terms and the Condition!");
            return false;
        }

        return true;
    }

    private void LoadFragmentViews() {
        signUpEmailTextInput = findViewById(R.id.signUpEmailTextInput);
        signUpPasswordTextInput = findViewById(R.id.signUpPasswordTextInput);
        signUpDisplayNameTextInput = findViewById(R.id.signUpDisplayNameTextInput);
        signUpButton = findViewById(R.id.signUpButton);
        agreementCheckBox = findViewById(R.id.agreementCheckbox);
        errorView = findViewById(R.id.signUpErrorView);

        lnkTermsAndConditions = findViewById(R.id.lnkTermsAndConditions);
        lnkTermsAndConditions.setPaintFlags(lnkTermsAndConditions.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    private void LoadFragmentButtonListeners() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ValidateActivity() == false) return;

                String email = signUpEmailTextInput.getText().toString();
                String password = signUpPasswordTextInput.getText().toString();
                String displayName = signUpDisplayNameTextInput.getText().toString();

                FireBaseSignUp(email, password, displayName);
            }
        });

        lnkTermsAndConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goliveira.com/spendingcontrol/policies.html"));
                startActivity(browserIntent);
            }
        });
    }

    private void FireBaseSignUp(String email, String password, String displayName) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(displayName)
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "User profile updated.");
                                    }
                                }
                            });
                    try {
                        if (user != null)
                            user.sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Email sent.");

                                                User newUser = new User(user.getUid(), user.getEmail(), user.getDisplayName());
                                                newUser.save();
                                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                                        SignUpActivity.this);
                                                // set title
                                                alertDialogBuilder.setTitle("Please Verify Your EmailID");
                                                // set dialog message
                                                alertDialogBuilder
                                                        .setMessage("A verification Email Is Sent To Your Registered EmailID, please click on the link and Sign in again!")
                                                        .setCancelable(false)
                                                        .setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                Intent signInIntent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                                SignUpActivity.this.finish();
                                                            }
                                                        });
                                                // create alert dialog
                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                // show it
                                                alertDialog.show();

                                            }
                                        }
                                    });
                    } catch (Exception e) {
                        errorView.setText(e.getMessage());
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    if (task.getException() != null) {
                        errorView.setText(task.getException().getMessage());
                    }
                }
            }
        });

    }

}