package com.uzoebere.gasify.customer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.parse.*;
import com.uzoebere.gasify.R;
import es.dmoral.toasty.Toasty;

public class AccessoryDetails extends AppCompatActivity {

    private TextView descTextView, priceTextView, textName;
    private ImageView detailImageView, detailShadowImageView, plusImageView, minusImageView;
    private Context context;
    ProgressBar progressBar;
    private EditText qtyEditText, textRefCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessory_details);

         detailImageView = findViewById(R.id.detailImageView);
    //    detailShadowImageView = findViewById(R.id.detailShadowImageView);
         descTextView = findViewById(R.id.descTextView);
         priceTextView = findViewById(R.id.priceTextView);
         textName = findViewById(R.id.textSalutation);
        qtyEditText = findViewById(R.id.qtyEditText);
        plusImageView = findViewById(R.id.plusImageView);
        minusImageView = findViewById(R.id.minusImageView);
        textRefCode = findViewById(R.id.textRefCode);
        progressBar = findViewById(R.id.progressBar7);
                //get data from the intents
        final String productName = getIntent().getStringExtra("productName");
        final int price = Integer.parseInt(getIntent().getStringExtra("price"));
        priceTextView.setText("N" + Integer.toString(price));
        textName.setText(productName);

        // retrieve the image
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Products");
        query.whereEqualTo("ProductName", productName);
        query.whereEqualTo("price", price);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                } else {
                    Log.d("score", "Retrieved the object.");
                    descTextView.setText(object.getString("productDescription"));
                    ParseFile image = object.getParseFile("Image");
                    loadImages( image, detailImageView);
                }
            }
        });

        final Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                orderButton.setEnabled(false);
                final String refCode = textRefCode.getText().toString();
                if (refCode.isEmpty()){
                    int qty = Integer.parseInt(qtyEditText.getText().toString());
                    double total = price * qty;
                    Intent intent = new Intent(AccessoryDetails.this, Checkout.class);
                    intent.putExtra("totalPrice", total);
                    intent.putExtra("quantity", qty);
                    intent.putExtra("productType", "Accessories");
                    intent.putExtra("productName",productName);
                    intent.putExtra("refCode", "-");
                    startActivity(intent);
                }
                else{
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Distributors");
                    //   query.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
                    query.getInBackground(refCode, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                int qty = Integer.parseInt(qtyEditText.getText().toString());
                                double total = price * qty;
                                Intent intent = new Intent(AccessoryDetails.this, Checkout.class);
                                intent.putExtra("totalPrice", total);
                                intent.putExtra("quantity", qty);
                                intent.putExtra("productType", "Accessories");
                                intent.putExtra("productName",productName);
                                intent.putExtra("refCode", refCode);
                                startActivity(intent);
                            }
                            else {
                                Toasty.error(AccessoryDetails.this, "Invalid Referral Code", Toast.LENGTH_SHORT).show();
                                textRefCode.setText("");
                                progressBar.setVisibility(View.GONE);
                                orderButton.setEnabled(true);
                            }
                        }
                    });
                }


            }
        });

        plusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(qtyEditText.getText().toString());
                int newQty = qty + 1 ;
                qtyEditText.setText(Integer.toString(newQty));
            }
        });

        minusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(qtyEditText.getText().toString());
                int newQty = qty - 1 ;
                if(newQty <= 0){
                    return;
                }
                qtyEditText.setText(Integer.toString(newQty));
            }
        });
    }

    private void loadImages(ParseFile image, final ImageView img) {
        if (image != null) {
            image.getDataInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        img.setImageBitmap(bmp);
                    }
                }
            });
        } else {
            img.setImageResource(R.drawable.baseline_image_black_24);
        }
    }
}
