package com.uzoebere.gasify.customer;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.*;
import com.uzoebere.gasify.MainActivity;
import com.uzoebere.gasify.R;
import es.dmoral.toasty.Toasty;

import java.util.HashMap;
import java.util.Map;

public class Checkout extends AppCompatActivity {

    ProgressBar progressBar5;
    private TextView textName, textEmail, textPhone, textAddress, textClosestJunction, textState, textBank;
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        textName = findViewById(R.id.textSalutation);
        textEmail = findViewById(R.id.textEmail);
        textPhone = findViewById(R.id.textPhone);
        textAddress = findViewById(R.id.textAddress);
        textClosestJunction = findViewById(R.id.textCity);
        textState = findViewById(R.id.textState);
        textBank = findViewById(R.id.textBank);

        progressBar5 = findViewById(R.id.progressBar5);
        spinner = findViewById(R.id.spinPmtMthd);

        final ParseUser currentUser = ParseUser.getCurrentUser();
        textName.setText(currentUser.getString("name"));
        textEmail.setText(currentUser.getEmail());
        textPhone.setText(currentUser.getString("phoneNo"));
        textAddress.setText(currentUser.getString("address"));
        textClosestJunction.setText(currentUser.getString("closestJunction"));
        textState.setText(currentUser.getString("city"));

        final Button buttonCheckout = findViewById(R.id.buttonRequest);
        buttonCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar5.setVisibility(View.VISIBLE);
                buttonCheckout.setEnabled(false);
                final String name = textName.getText().toString();
                String email = textEmail.getText().toString();
                final String phoneNo = textPhone.getText().toString();
                final String address = textAddress.getText().toString();
                final String closestJunction = textClosestJunction.getText().toString();
                final String city = textState.getText().toString();
                final String value = spinner.getSelectedItem().toString();

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
                if (email.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append(" your email");
                }
                if (phoneNo.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append(" your phone number");
                }
                if (city.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("city");
                }
                if (closestJunction.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your closest junction");
                }
                if (value.equals("Please Select A Payment Method")) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("a payment method");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toasty.error(Checkout.this, validationErrorMessage.toString(), Toast.LENGTH_LONG, true).show();
                    progressBar5.setVisibility(View.GONE);
                        buttonCheckout.setEnabled(true);
                    //    buttonCart.setEnabled(true);
                    return ;
                }

                final Double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
                final Double cylinderWeight = getIntent().getDoubleExtra("cylinderWeight", 0.0);
                final Double quantity = getIntent().getDoubleExtra("quantity", 0.0);
                final Integer price = getIntent().getIntExtra("prodPrice", 0);
                final String refCode = getIntent().getStringExtra("refCode");
                final String productType = getIntent().getStringExtra("productType");
                final String productNm = getIntent().getStringExtra("productName");

                // Update user for any changes
                currentUser.put("name", name);
                currentUser.put("phoneNo", phoneNo);
                currentUser.put("address", address);
                currentUser.put("closestJunction", closestJunction);
                currentUser.put("city", city);
                currentUser.saveInBackground();

                final ParseObject newCusOrder = new ParseObject("NewOrders");
                newCusOrder.put("custObjectId", currentUser.getObjectId());
                newCusOrder.put("totalPrice", totalPrice);
                newCusOrder.put("orderStatus", "Pending");
                newCusOrder.put("customer", currentUser);
            //    newCusOrder.put("orderCompleted", "false");
                newCusOrder.put("cylinderWeight", cylinderWeight);
                newCusOrder.put("refCode", refCode);
                newCusOrder.put("price", price);
                newCusOrder.put("qtyPerKg", quantity);
                newCusOrder.put("productType", productType);
                newCusOrder.put("paymentMethod", value);
                newCusOrder.put("productName", productNm);
                newCusOrder.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            // Calculate all the commission
                            if (refCode.equals("-")){

                            } else {
                                if (productType.equals("Gas Refill")){

                                    Double gasCom = 0.08 * totalPrice;
                                    newCusOrder.put("gasCommission", gasCom);
                                    newCusOrder.saveInBackground();

                                    // Increment the counter
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Distributors");
                                    query.getInBackground(refCode, new GetCallback<ParseObject>() {
                                        public void done(ParseObject object, ParseException e) {
                                            if (e == null) {
                                                object.increment("noOfGasSales");
                                                object.increment("totalSales");
                                                object.saveInBackground();
                                            } else {
                                                // something went wrong
                                            }
                                        }
                                    });
                                } else if (productType.equals("Accessories")){

                                    Double accCom = 0.15 * totalPrice;
                                    newCusOrder.put("accCommission", accCom);
                                    newCusOrder.saveInBackground();
                                    // Increment the counter
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Distributors");
                                    query.getInBackground(refCode, new GetCallback<ParseObject>() {
                                        public void done(ParseObject object, ParseException e) {
                                            if (e == null) {
                                                object.increment("noOfAccSales");
                                                object.increment("totalSales");
                                                object.saveInBackground();
                                            } else {
                                                // something went wrong
                                            }
                                        }
                                    });
                                }
                            }
                            // Procedure to send an email to the rider and admin and a confirmatory email to the customer
                            // Email to the Admin and rider
                            String fromEmailAddress = currentUser.getEmail() ;
                            String toEmailAddress = "pickndropng@gmail.com, kingfrankhenshaw@gmail.com";
                            String emailSubject = "New Gasify Order ";
                            String emailBody = "A new order has been placed. Kindly find details about it below.\n Kind Regards. \n\n Customer Name: " + name + "\n Address: " + address + ", " + city +
                                    "\n Closest Junction: " + closestJunction + "\n Phone Number: " + phoneNo + "\nProduct Ordered: " + productNm + "\n Cylinder Weight: " + cylinderWeight + "kg" + "\n Quantity Ordered: " + quantity + "kg" +
                                    "\n Payment Method: " + value;

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
                                    //    Toast.makeText(Checkout.this, "Order Received Successfully. Kindly check your inbox", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        // Something went wrong
                                        Toasty.error(Checkout.this, "Order Not Received.", Toast.LENGTH_SHORT, true).show();

                                    }
                                }
                            });

                            // Confirmatory email to the customer
                            String fromEmailAddress1 = "kingfrankhenshaw@gmail.com, pickndropng@gmail.com" ;
                            String toEmailAddress1 = currentUser.getEmail();
                            String emailSubject1 = currentUser.getString("name") + ", Your Order Has Been Received" ;
                            String emailBody1 = "Dear Customer, \n\n Your order has been received and a rider will be handling the delivery shortly." +
                                    "\n\n\nKind Regards, \nThe Gasify Team.";

                            Map<String, String> params1 = new HashMap<>();
                            params1.put("fromEmail", fromEmailAddress1);
                            params1.put("toEmail", toEmailAddress1);
                            params1.put("subject", emailSubject1);
                            params1.put("body", emailBody1);

                            ParseCloud.callFunctionInBackground("sendEMail", params1, new FunctionCallback<Object>() {
                                @Override
                                public void done(Object response, ParseException exc) {
                                    if(exc == null) {
                                        // The function executed, but still has to check the response
                                        Toasty.success(Checkout.this, "Your Order Has Been Received Successfully.", Toast.LENGTH_SHORT, true).show();
                                    }
                                    else {
                                        // Something went wrong
                                        Toasty.error(Checkout.this, "Error in sending your order.", Toast.LENGTH_SHORT, true).show();

                                    }
                                }
                            });

                        }
                        else {
                            Log.e("Check ... ", " Ohhh Error  ...." + e.getMessage());
                            System.out.println("Not saved");
                            Toast.makeText(Checkout.this, "Oops! There seems to be a network problem. Please try again.", Toast.LENGTH_SHORT).show();
                            buttonCheckout.setEnabled(true);
                        }
                    }
                });





                // Go to the last activity
                Intent intent = new Intent(Checkout.this, OrderCompleted.class);
                intent.putExtra("name", name);
                intent.putExtra("address", address + ", " + city);
                intent.putExtra("phoneNo", phoneNo);
                intent.putExtra("total", totalPrice);
                intent.putExtra("pymt", value);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        final ImageView closeImageView = findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
