package com.uzoebere.gasify.distributors;

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
import com.parse.*;
import com.parse.ParseUser;
import com.uzoebere.gasify.*;
import com.uzoebere.gasify.R;
import com.uzoebere.gasify.customer.CustomerWelcome;
import es.dmoral.toasty.Toasty;

public class DistributorLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText mEmailField;
    private EditText mPasswordField;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distributor_login);

        // views
        mEmailField = findViewById(R.id.fieldEmail);
        mPasswordField = findViewById(R.id.fieldPassword);

        //buttons
        findViewById(R.id.forgotButton).setOnClickListener(this);
        findViewById(R.id.createAccountButton).setOnClickListener(this);


        final Button login_button = findViewById(R.id.loginButton);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmailField.getText().toString();
                String password = mPasswordField.getText().toString();
                //Validating the log in data
                boolean validationError = false;

                StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");
                if (email.isEmpty()) {
                    validationError = true;
                    validationErrorMessage.append("a username");
                }
                if (password.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("a password");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toasty.warning(DistributorLogin.this, validationErrorMessage.toString(), Toast.LENGTH_SHORT, true).show();
                    return;
                }

                //Setting up a progress bar
                progressBar = findViewById(R.id.progressBar2);
                progressBar.setVisibility(View.VISIBLE);
                login_button.setEnabled(false);
                ParseUser.logInInBackground(email, password, new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (parseUser != null) {
                            progressBar.setVisibility(View.GONE);
                            alertDisplayer("Successful Login","Welcome back!");
                            login_button.setEnabled(true);

                            ParseUser currentUser = ParseUser.getCurrentUser();

                            if (currentUser.getString("userType") == null){
                                Intent intent = new Intent(DistributorLogin.this, IntroScreen.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else if (currentUser.getString("userType").equals("Customer")){
                                Intent intent = new Intent(DistributorLogin.this, CustomerWelcome.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            } else if (currentUser.getString("userType").equals("Distributor")){
                                Intent intent = new Intent(DistributorLogin.this, DistDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        } else {
                            progressBar.setVisibility(View.GONE);
                            ParseUser.logOut();
                            Toasty.error(DistributorLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            login_button.setEnabled(true);
                        }
                    }
                });
            }
        });


    }

    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.forgotButton) {
              Intent intent = new Intent (this, ForgotPassword.class);
              startActivity(intent);
        } else if (i == R.id.createAccountButton) {
            Intent intent = new Intent (this, SignUp.class);
            startActivity(intent);
        }
    }

    private void alertDisplayer( String title,String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(DistributorLogin.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                     }
                });
        AlertDialog ok = builder.create();
        ok.show();
    }
}
