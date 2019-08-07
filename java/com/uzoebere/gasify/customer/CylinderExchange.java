package com.uzoebere.gasify.customer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.parse.*;
import com.uzoebere.gasify.R;
import es.dmoral.toasty.Toasty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CylinderExchange extends AppCompatActivity {

    private EditText textDistributor, textSalutation, text, textMiddleName, textConsumerNo, textLastName, textDOB, textFatherNm, textMotherNm, textSpouseNm, textState, textCity, textArea, textAddress, textPhone, textEmail;
    private Button btnChoose;
    private ImageView imageView;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cylinder_exchange);

        final ImageView closeImageView = findViewById(R.id.closeImageView);
        closeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    protected void onStart() {

        super.onStart();

        textDistributor = findViewById(R.id.textDistributor);
        textSalutation = findViewById(R.id.textSalutation);
        text = findViewById(R.id.text);
        textMiddleName = findViewById(R.id.textMiddleName);
        textConsumerNo = findViewById(R.id.textConsumerNo);
        textLastName = findViewById(R.id.textLastName);
        textDOB = findViewById(R.id.textDOB);
        textFatherNm = findViewById(R.id.textFatherNm);
        textMotherNm = findViewById(R.id.textMotherNm);
        textSpouseNm = findViewById(R.id.textSpouseNm);
        textState = findViewById(R.id.textState);
        textCity = findViewById(R.id.textCity);
        textArea = findViewById(R.id.textArea);
        textAddress = findViewById(R.id.textAddress);
        textPhone = findViewById(R.id.textPhone);
        textEmail = findViewById(R.id.textEmail);
        btnChoose = findViewById(R.id.buttonChoose);
        imageView = findViewById(R.id.imagePassport);
        progressBar = findViewById(R.id.progressBar5);

        final String name = textLastName.getText().toString() + " " + text.getText().toString();
        final String phoneNo = textPhone.getText().toString();
        final String closestJunction = textArea.getText().toString();
        final String address = textAddress.getText().toString();
        final String city = textCity.getText().toString();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        final Button buttonRequest = findViewById(R.id.buttonRequest);
        buttonRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                buttonRequest.setEnabled(false);
                String distro = textDistributor.getText().toString();
                String salutation = textSalutation.getText().toString();
                final String firstNm = text.getText().toString();
                final String middleNm = textMiddleName.getText().toString();
                String consumerNo = textConsumerNo.getText().toString();
                final String lastNm = textLastName.getText().toString();
                String dob = textDOB.getText().toString();
                String fatherNm = textFatherNm.getText().toString();
                String motherNm = textMotherNm.getText().toString();
                String state = textState.getText().toString();
                final String city1 = textCity.getText().toString();
                String area = textArea.getText().toString();
                final String address1 = textAddress.getText().toString();
                final String phone = textPhone.getText().toString();
                String email = textEmail.getText().toString();

                boolean validationError = false;
                StringBuilder validationErrorMessage = new StringBuilder("Please, insert ");

                if (distro.isEmpty()) {
                    validationError = true;
                    validationErrorMessage.append("the distributor's name");
                }

                if (salutation.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("the quantity to order");
                }

                if (firstNm.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your first name");
                }

                if (consumerNo.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("the consumer number");
                }

                if (lastNm.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your last name");
                }

                if (dob.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("the date of birth");
                }

                if (fatherNm.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your father's name");
                }

                if (motherNm.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your mother's name");
                }

                if (state.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your state");
                }

                if (city1.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your city");
                }

                if (area.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your area");
                }

                if (address1.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your address");
                }

                if (phone.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your phone number");
                }

                if (email.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your email address");
                }

                if (middleNm.isEmpty()) {
                    if (validationError) {
                        validationErrorMessage.append(" and ");
                    }
                    validationError = true;
                    validationErrorMessage.append("your middle name");
                }
                validationErrorMessage.append(".");

                if (validationError) {
                    Toasty.error(CylinderExchange.this, validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                    buttonRequest.setEnabled(true);
                    //    buttonCart.setEnabled(true);
                    return ;
                }

                Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageInByte = baos.toByteArray();

                ParseFile file = new ParseFile("passport.jpg",imageInByte);
                file.saveInBackground();

                final ParseUser currentUser = ParseUser.getCurrentUser();

                ParseObject newExchange = new ParseObject("cylinderExchange");
                newExchange.put("user", currentUser);
                newExchange.put("salutation", salutation);
                newExchange.put("distName", distro);
                newExchange.put("firstName", firstNm);
                newExchange.put("middleName", middleNm);
                newExchange.put("lastName", lastNm);
                newExchange.put("consumerNo", consumerNo);
                newExchange.put("dob", dob);
                newExchange.put("fatherName", fatherNm);
                newExchange.put("motherName", motherNm);
                newExchange.put("spouseName", textSpouseNm.getText().toString());
                newExchange.put("email", email);
                newExchange.put("phoneNo", phone);
                newExchange.put("address", address1);
                newExchange.put("city", city1);
                newExchange.put("area", area);
                newExchange.put("passport", file);
                newExchange.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){
                            // Email to the Admin and rider
                            String fromEmailAddress = currentUser.getEmail() ;
                            String toEmailAddress = "zomybee@gmail.com";
                            String emailSubject = "New Cylinder Exchange Request";
                            String emailBody = "A request has been made for new cylinder. Kindly find details about it below.\n\n Kind Regards. \n\n Customer Name: " + firstNm + " " + middleNm + " " + lastNm +
                                    "\nAddress: " + address1 + ", " + city1 + "\nPhone Number: " + phone ;

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
                                        Toasty.success(CylinderExchange.this, "Request Sent Successfully.", Toast.LENGTH_SHORT, true).show();
                                        Intent intent = new Intent(CylinderExchange.this, CustomerWelcome.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        // Something went wrong
                                        Toast.makeText(CylinderExchange.this, "Request Not Sent.", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        } else {
                            System.out.println("Error message: " + e);
                            Toasty.error(CylinderExchange.this, "Request Not Sent. Please try again", Toast.LENGTH_SHORT, true).show();
                            buttonRequest.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            return;
                        }
                    }
                });
            }
        });

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}
