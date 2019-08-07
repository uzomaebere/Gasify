package com.uzoebere.gasify;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.ParseUser;
import com.uzoebere.gasify.customer.OrderList;

public class UserProfile extends AppCompatActivity {


    private TextView vTextViewName;
    private TextView vTextViewEmail;
    private TextView vTextViewUserName;
    private TextView vTextViewAddress;
    private TextView vTextViewPhoneNo;
    private TextView vTextViewDate;
//    private TextView vTextViewCity;
    private TextView vTextViewVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        vTextViewName = findViewById(R.id.fieldName);
        vTextViewEmail =  findViewById(R.id.fieldEmail);
        vTextViewUserName = findViewById(R.id.fieldUsername);
        vTextViewAddress = findViewById(R.id.fieldAddress);
     //   vTextViewCity = findViewById(R.id.fieldCity);
        vTextViewPhoneNo = findViewById(R.id.fieldPhone);
        vTextViewDate =  findViewById(R.id.fieldDate);
        vTextViewVerify =  findViewById(R.id.fieldVerified);

        final ImageView closeImageView = findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final Button buttonOrders = findViewById(R.id.buttonOrders);
        buttonOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, OrderList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();
        // Set up the profile page based on the current user.
        ParseUser user = ParseUser.getCurrentUser();
        showProfile(user);
    }

    private void showProfile(ParseUser user) {
        if (user != null) {

            vTextViewEmail.setText(user.getEmail());
            vTextViewUserName.setText("@" + user.getUsername());
            vTextViewDate.setText(user.getCreatedAt().toString());
        //    vTextViewVerify.setText(user.getEmailVerified());

            String fullName = user.getString("name");
            if (fullName != null) {
                vTextViewName.setText(fullName);
            } else {
                vTextViewName.setText("-");
            }

            String address = user.getString("address") + ", " + user.getString("city");
            if (address != null) {
                vTextViewAddress.setText(address);
            } else {
                vTextViewAddress.setText("-");
            }

            String phoneNo = user.getString("phoneNo");
            if (phoneNo != null) {
                vTextViewPhoneNo.setText(phoneNo);
            } else {
                vTextViewPhoneNo.setText("-");
            }

            String verifiyStatus = user.getString("emailVerified");
            if (verifiyStatus != null) {
                vTextViewVerify.setText(verifiyStatus);
            } else {
                vTextViewVerify.setText("Not Verified");
            }
        }
    }
}
