package com.goliveira.spendingcontrol.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.goliveira.spendingcontrol.MainActivity;
import com.goliveira.spendingcontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.onesignal.OneSignal;

import greco.lorenzo.com.lgsnackbar.LGSnackbarManager;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarThemeManager;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    public FirebaseAuth mAuth;
    EditText emailTextInput;
    EditText passwordTextInput;
    Button signInButton;
    Button forgotPasswordButton;
    Button sendVerifyMailAgainButton;
    Button signUpButtom;
    public FirebaseUser currentUser;

    private void ToastError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void TryToSignInSavedUser(){
        if (mAuth.getCurrentUser() != null) {
            mAuth.getCurrentUser().reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    currentUser = mAuth.getCurrentUser();
                    if (currentUser != null && currentUser.isEmailVerified()) {
                        System.out.println("Email Verified : " + currentUser.isEmailVerified());
                        Intent MainActivity = new Intent(SignInActivity.this, MainActivity.class);
                        startActivity(MainActivity);
                        SignInActivity.this.finish();
                    }
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LGSnackbarManager.prepare(getApplicationContext(),
                LGSnackBarThemeManager.LGSnackbarThemeName.SHINE);

        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        TryToSignInSavedUser();

        LoadFragmentViews();

        LoadFragmentButtonListeners();
    }

    public void LoadFragmentViews() {
        emailTextInput = findViewById(R.id.signInEmailTextInput);
        passwordTextInput = findViewById(R.id.signInPasswordTextInput);
        signInButton = findViewById(R.id.signInButton);
        forgotPasswordButton = findViewById(R.id.forgotPasswordButton);
        sendVerifyMailAgainButton = findViewById(R.id.verifyEmailAgainButton);
        signUpButtom = findViewById(R.id.signUpButtom);
    }

    public void LoadFragmentButtonListeners() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ValidateActivity() == false) return;

                String email = emailTextInput.getText().toString();
                String password = passwordTextInput.getText().toString();

                FireBaseSignIn(email, password);
            }
        });


        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTextInput.getText().toString();

                Intent forgotPasswordActivity = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                forgotPasswordActivity.putExtra("USER_EMAIL" , email);

                startActivity(forgotPasswordActivity);
            }
        });

        signUpButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailTextInput.getText().toString();

                Intent signUpIntent = new Intent(SignInActivity.this, SignUpActivity.class);
                signUpIntent.putExtra("USER_EMAIL" , email);

                startActivity(signUpIntent);
            }
        });
    }

    private boolean ValidateActivity() {
        if (emailTextInput.getText().toString().contentEquals("")) {
            ToastError("Email can't be empty");
            return false;
        }

        if (passwordTextInput.getText().toString().contentEquals("")) {
            ToastError("Password can't be empty");
            return false;
        }

        return true;
    }

    public void FireBaseSignIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            if (user != null) {
                                if (user.isEmailVerified()) {

                                    System.out.println("Email Verified : " + user.isEmailVerified());
                                    Intent HomeActivity = new Intent(SignInActivity.this, MainActivity.class);
                                    setResult(RESULT_OK, null);
                                    startActivity(HomeActivity);
                                    SignInActivity.this.finish();

                                    OneSignal.sendTag("email", email);
                                    OneSignal.setExternalUserId(user.getUid());


                                } else {
                                    sendVerifyMailAgainButton.setVisibility(View.VISIBLE);
                                    ToastError("You need to verify your E-mail!");
                                }
                            }

                        } else {
                            ToastError("Authentication failed :(");

                            if (task.getException() != null) {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                            }

                            forgotPasswordButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}