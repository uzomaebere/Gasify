package com.uzoebere.gasify;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.FindCallback;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseException;
import com.parse.RequestPasswordResetCallback;
import com.uzoebere.gasify.distributors.DistributorLogin;
import es.dmoral.toasty.Toasty;

import java.util.List;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        final Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText mEmail = (EditText) findViewById(R.id.txtEmail);
                    if(!isEmailValid(mEmail.getText().toString())){
                        Toast.makeText(ForgotPassword.this, "Enter a valid email address.", Toast.LENGTH_LONG).show();
                        return;
                    }
                if(!isEmailValid(mEmail.getText().toString())){
                    Toast.makeText(ForgotPassword.this, "Enter a valid email address.", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!mEmail.getText().toString().isEmpty()){
                    final String email = mEmail.getText().toString();
                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("email", email);
                    query.setLimit(1);
                    query.findInBackground(new FindCallback<ParseUser>() {

                        public void done(List<ParseUser> user, ParseException e) {
                            if (e == null && user.size() > 0) {
                                // The query was successful.

                                ParseUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {

                                    public void done(ParseException e) {
                                        if (e == null) {
                                            // An email was successfully sent with reset instructions.
                                            Toast.makeText(ForgotPassword.this, "An email was successfully sent to you with reset instructions.", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(ForgotPassword.this, DistributorLogin.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(ForgotPassword.this, "Something went wrong. Try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            } else {
                                // Something went wrong.
                                Toasty.info(ForgotPassword.this, "We couldn\'t find a user associated to this email. Try again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
                else{
                    Toasty.warning(ForgotPassword.this, "Insert your email to reset your password.", Toast.LENGTH_SHORT, true).show();
                }
            }
        });

        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, DistributorLogin.class);
                startActivity(intent);
            }
        });
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
