package com.uzoebere.gasify.distributors;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.*;
import com.uzoebere.gasify.R;
import es.dmoral.toasty.Toasty;

import java.util.HashMap;
import java.util.Map;

public class DistRegistrationPage extends AppCompatActivity {

    private EditText fieldName, fieldAddress, fieldCity, fieldStoreName, fieldPhone;
    ProgressBar progressBar3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dist_registration_page);

        fieldName = findViewById(R.id.fieldName);
        fieldAddress = findViewById(R.id.fieldAddress);
        fieldCity = findViewById(R.id.fieldCity);
        fieldStoreName = findViewById(R.id.fieldStore);
        fieldPhone = findViewById(R.id.fieldPhone);

        final ImageView closeImageView = findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button button_Register = findViewById(R.id.buttonRegister);
        button_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = fieldName.getText().toString();
                String address = fieldAddress.getText().toString();
                String city = fieldCity.getText().toString();
                String storeName = fieldStoreName.getText().toString();
                String phone = fieldPhone.getText().toString();

                // Validate the input
                boolean validationError = false;
                StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");

                if (name.isEmpty()) {
                    validationError = true;
                    validationErrorMessage.append("your name");
                }

                if (address.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("address");
                }
                if (storeName.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append(" your store name");
                }
                if (phone.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append(" your phone number");
                }
                if (city.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" , ");
                    }
                    validationError = true;
                    validationErrorMessage.append("city");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toasty.error(DistRegistrationPage.this, validationErrorMessage.toString(), Toast.LENGTH_LONG, true).show();
                    return;
                }

                // Display progress bar
                progressBar3 = findViewById(R.id.progressBar3);
                progressBar3.setVisibility(View.VISIBLE);
                button_Register.setEnabled(false);

                final ParseUser currentUser = ParseUser.getCurrentUser();
                final ParseObject distro = new ParseObject("Distributors");
                distro.put("userObjectId", currentUser.getObjectId());
                distro.put("storeName", storeName);
                distro.put("user", currentUser);
                distro.saveInBackground();
                currentUser.put("name", name);
                currentUser.put("storeName", storeName);
                currentUser.put("address", address);
                currentUser.put("phoneNo", phone);
                currentUser.put("city", city);
                currentUser.put("userType", "Distributor");
                currentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            String fromEmailAddress = "kingfrankhenshaw@gmail.com" ;
                            String toEmailAddress = currentUser.getEmail();
                            String emailSubject = "Welcome To Gasify, " + currentUser.getString("name");
                            String emailBody = "Welcome To Gasify. Your referral code is  " + currentUser.getString("referralCode");

                            Map<String, String> params = new HashMap<>();
                            params.put("fromEmail", fromEmailAddress);
                            params.put("toEmail", toEmailAddress);
                            params.put("subject", emailSubject);
                            params.put("body", emailBody);

                            ParseCloud.callFunctionInBackground("sendEMail", params, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object response, ParseException exc) {
                                    if(exc == null) {
                                        // The function executed, but still has to check the response
                                        Toasty.success(DistRegistrationPage.this, "Registration Successfully.", Toast.LENGTH_SHORT, true).show();
                                    }
                                    else {
                                        // Something went wrong
                                        Toasty.error(DistRegistrationPage.this, "Unable to create account. Check your network and try again", Toast.LENGTH_SHORT, true).show();

                                    }
                                }
                            });
                        //    System.out.println("Distributor's ref code is: " + distro.getObjectId());
                            String refCode = distro.getObjectId();
                            currentUser.put("referralCode", refCode);

                            Intent intent = new Intent (DistRegistrationPage.this, DistDashboard.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        } else {
                            Toasty.error(DistRegistrationPage.this, "Network Error. Check your network and try again", Toast.LENGTH_SHORT, true).show();
                            progressBar3.setVisibility(View.GONE);
                            button_Register.setEnabled(true);
                            return;
                        }
                    }
                });
            }
        });



    }


}
