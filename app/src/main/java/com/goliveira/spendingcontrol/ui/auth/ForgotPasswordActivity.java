package com.goliveira.spendingcontrol.ui.auth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.goliveira.spendingcontrol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ForgotPasswordActivity";
    public FirebaseAuth mAuth;
    Button resetPasswordButton;
    EditText emailTextInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        LoadFragmentViews();

        LoadFragmentButtonListeners();

        TryToRecoverEmailInput();
    }

    private void TryToRecoverEmailInput(){
        String userEmail = getIntent().getStringExtra("USER_EMAIL");

        if(TextUtils.isEmpty(userEmail) == false){
            emailTextInput.setText(userEmail);
        }
    }

    private boolean ValidateActivity() {

        if (TextUtils.isEmpty(emailTextInput.getText().toString())) {
            ToastError("Email can't be empty");
            return false;
        }

        if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailTextInput.getText().toString()).matches() == false) {
            ToastError("The Email typed is invalid!");
            return false;
        }

        return true;
    }

    private void LoadFragmentViews() {
        emailTextInput = findViewById(R.id.fpEmailTextInput);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
    }

    private void LoadFragmentButtonListeners() {
        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ValidateActivity() == false) return;

                String email = emailTextInput.getText().toString();

                FireBaseResetPassword(email);
            }
        });
    }

    private void FireBaseResetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) Log.d(TAG, "Email sent.");

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                ForgotPasswordActivity.this);

                        // set title
                        alertDialogBuilder.setTitle("Reset Password");

                        // set dialog message
                        alertDialogBuilder
                                .setMessage("An Email with a reset password link was sent to your Email.")
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        ForgotPasswordActivity.this.finish();
                                    }
                                });

                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    }
                });
    }

    private void ToastError(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
}