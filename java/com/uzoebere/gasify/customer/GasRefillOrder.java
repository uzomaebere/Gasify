package com.uzoebere.gasify.customer;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.*;
import com.uzoebere.gasify.R;
import com.uzoebere.gasify.distributors.DistDashboard;
import com.uzoebere.gasify.distributors.DistributorLogin;
import es.dmoral.toasty.Toasty;

public class GasRefillOrder extends AppCompatActivity {

    private EditText fieldWeight, fieldRefCode, fieldQty;
    private TextView fieldPrice;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_refill_order);


        //Setting up a progress bar
        progressBar = findViewById(R.id.pBarGasOrder);
        progressBar.setVisibility(View.VISIBLE);

        // Initialize views
        fieldQty = findViewById(R.id.fieldQty);
        fieldWeight = findViewById(R.id.fieldWeight);
        fieldRefCode = findViewById(R.id.fieldRefCode);
        fieldPrice = findViewById(R.id.textPrice);
        progressBar.setVisibility(View.GONE);

        // Get the store price
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
    //    query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query.getInBackground("YPloHngrUt", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    int price = object.getInt("price");
                    fieldPrice.setText(Integer.toString(price));
                    System.out.println(price);
                    //    fieldPrice.setText(Integer.toString(price));
                } else {
                    // something went wrong
                    fieldPrice.setText(0);
                }
            }
        });


        final Button buttonOrder = findViewById(R.id.buttonRequest);

        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                buttonOrder.setEnabled(false);

                String weight1 = fieldWeight.getText().toString();
                String quantity1 = fieldQty.getText().toString();
                String price1 = fieldPrice.getText().toString();


                boolean validationError = false;
                StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");

                if (price1.isEmpty()){
                    Toast.makeText(GasRefillOrder.this, "Please hold on for the price", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (weight1.isEmpty() || weight1.equals("0")) {
                    validationError = true;
                    validationErrorMessage.append("your cylinder weight");
                }

                if (quantity1.isEmpty() || quantity1.equals("0")) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("the quantity to order");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toasty.error(GasRefillOrder.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    buttonOrder.setEnabled(true);
                //    buttonCart.setEnabled(true);
                    return ;
                }

                // initialize all the variables
                final double weight = Double.parseDouble(fieldWeight.getText().toString());
                final double quantity = Double.parseDouble(fieldQty.getText().toString());
                final String refCode = fieldRefCode.getText().toString();
                final int gasPrice = Integer.parseInt(fieldPrice.getText().toString());

                double totalPrice = gasPrice * quantity;
                AlertDialog.Builder builder = new AlertDialog.Builder(GasRefillOrder.this);

                builder
                        .setMessage("The total price is N" + totalPrice + ". Would you like to continue with your order?")
                        .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // Yes-code
                                if (refCode.isEmpty()) {
                                    double totalPrice = gasPrice * quantity;

                                    progressBar.setVisibility(View.GONE);
                                    buttonOrder.setEnabled(true);
                                    // Pass the order object id to the next activity to approve the order
                                    Intent intent = new Intent(GasRefillOrder.this, Checkout.class);
                                    intent.putExtra("totalPrice", totalPrice );
                                    intent.putExtra("quantity", quantity);
                                    intent.putExtra("cylinderWeight", weight);
                                    intent.putExtra("prodPrice", gasPrice);
                                    intent.putExtra("refCode", "-");
                                    intent.putExtra("productType", "Gas Refill");
                                    intent.putExtra("size", "-");
                                    intent.putExtra("color", "-");
                                    intent.putExtra("productName", "Gas Refill Order");
                                    startActivity(intent);
                                }
                                else {

                                    progressBar.setVisibility(View.VISIBLE);
                                    final double totalPrice = gasPrice * quantity;
                                    // Check if the referral code is a valid one
                                //    confirmDialog(totalPrice);
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Distributors");
                                 //   query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                                    query.getInBackground(refCode, new GetCallback<ParseObject>() {
                                        public void done(ParseObject object, ParseException e) {
                                            if (e == null) {

                                                String distroId = object.getObjectId();
                                                // Go to the next activity
                                                progressBar.setVisibility(View.GONE);
                                                buttonOrder.setEnabled(true);

                                                Intent intent = new Intent(GasRefillOrder.this, Checkout.class);
                                                intent.putExtra("totalPrice", totalPrice );
                                                intent.putExtra("quantity", quantity);
                                                intent.putExtra("cylinderWeight", weight);
                                                intent.putExtra("prodPrice", gasPrice);
                                                intent.putExtra("refCode", distroId);
                                                intent.putExtra("productType", "Gas Refill");
                                                intent.putExtra("productName", "Gas Refill Order");
                                                startActivity(intent);


                                            } else {
                                                // display a toast showing invalid referral code
                                                Toasty.error(getApplicationContext(), "Invalid Referral Code", Toast.LENGTH_SHORT).show();
                                                fieldRefCode.setText("");
                                                progressBar.setVisibility(View.GONE);
                                                buttonOrder.setEnabled(true);
                                            }
                                        }
                                    });
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                                progressBar.setVisibility(View.GONE);
                                buttonOrder.setEnabled(true);
                            }
                        })
                        .show();

                // If referral code is empty

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
