package com.uzoebere.gasify;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.uzoebere.gasify.distributors.DistributorLogin;
import es.dmoral.toasty.Toasty;

public class SignUp extends AppCompatActivity {

    EditText fieldUsername, fieldPassword, fieldEmail;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fieldUsername  = findViewById(R.id.fieldUsername);
        fieldPassword  = findViewById(R.id.fieldPassword);
        fieldEmail  = findViewById(R.id.fieldEmail);

        /* Login Details */
        final Button registerButton = findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String username = fieldUsername.getText().toString();
                String password = fieldPassword.getText().toString();
                String email = fieldEmail.getText().toString();

                boolean validationError = false;

                StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");

                if (username.isEmpty()) {
                    validationError = true;
                    validationErrorMessage.append("an username");
                }

                if (password.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("a password");
                }
                if (email.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your emai");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toasty.warning(SignUp.this, validationErrorMessage.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar = findViewById(R.id.progressBar);
                progressBar.setVisibility(View.VISIBLE);
                registerButton.setEnabled(false);

                ParseUser user = new ParseUser();
                user.setEmail(email);
                user.setPassword(password);
                user.setUsername(username);
                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            progressBar.setVisibility(View.GONE);
                            alertDisplayer("Sign Up Successful!","Welcome!");

                        } else {
                            progressBar.setVisibility(View.GONE);
                            ParseUser.logOut();
                            registerButton.setEnabled(true);
                            Toast.makeText(SignUp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        final Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            //    progressBar.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SignUp.this, DistributorLogin.class);
             //   progressBar.setVisibility(View.GONE);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }

    private void alertDisplayer( String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent intent = new Intent(SignUp.this, IntroScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
