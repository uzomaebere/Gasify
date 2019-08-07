package com.uzoebere.gasify.distributors;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import com.parse.*;
import com.uzoebere.gasify.AboutGasify;
import com.uzoebere.gasify.R;
import com.uzoebere.gasify.UserProfile;
import es.dmoral.toasty.Toasty;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PieChartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DistDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView fieldRefCode, textTotalSales, textStoreName,textDailyEarnings, textTotalCom, textBalance;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dist_dashboard);

        progressBar = findViewById(R.id.progressBar4);
        progressBar.setVisibility(View.VISIBLE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textTotalSales = findViewById(R.id.textTotalSales);
        textDailyEarnings = findViewById(R.id.textDailyEarnings);
        textTotalCom = findViewById(R.id.textTotalCom);
        textBalance = findViewById(R.id.textBalance);

        FloatingActionButton fab = findViewById(R.id.fab);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        // Display the referral code
        final ParseUser currentUser = ParseUser.getCurrentUser();

        String store = (String) currentUser.get("storeName");
        View headerView = navigationView.getHeaderView(0);
        TextView storeName = headerView.findViewById(R.id.textSalutation);
        storeName.setText(store.toUpperCase());

        final String refCode = currentUser.getString("referralCode");
        final TextView refText = headerView.findViewById(R.id.textEmail);

        if (refCode == null){
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Distributors");
            query.whereEqualTo("userObjectId", currentUser.getObjectId());
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    if(object == null){
                        Toasty.error(DistDashboard.this, "No user found", Toast.LENGTH_SHORT, true).show();
                    } else{
                     //   refCode = object.getObjectId();
                        refText.setText("(" + object.getObjectId() + ")");
                        currentUser.put("referralCode", object.getObjectId());
                        currentUser.saveInBackground();
                    }
                }
            });
        } else {
            refText.setText("(" + refCode + ")");
        }

        // Display the required information on the dashboard
        // To display the total number of sales
        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("NewOrders");
        query3.whereEqualTo("refCode", refCode);
    //    query3.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query3.countInBackground(new CountCallback() {
            @Override
            public void done(int count, ParseException e) {
                if (e == null){
                    System.out.println(count);
                    textTotalSales.setText(Integer.toString(count) + " sales");
                }
                else {
                    textTotalSales.setText("-");
                }
            }
        });

        // To display daily earnings

        ParseQuery<ParseObject> query1 = ParseQuery.getQuery("NewOrders");
        query1.whereEqualTo("refCode", refCode);
        query1.whereGreaterThanOrEqualTo("createdAt", new Date());
    //    query1.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
    //    System.out.println("Today's date is " +  SimpleDateFormat.getDateInstance().format(new Date()));
        query1.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                int sales = 0 ;
            //    System.out.println("Objects: " + objects);
                if (e == null){
                    for(int i=0; i < objects.size(); i++){
                        System.out.println("Objects: " + objects);
                        int gasCom = objects.get(i).getInt("gasCommission");
                        int accCom = objects.get(i).getInt("accCommission");
                        int totalCom = gasCom + accCom;
                        sales += totalCom;
                    }
                    textDailyEarnings.setText("N" + Double.toString(sales));
                }

            }
        });

        // To display Total Commission Earned
        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("NewOrders");
        query2.whereEqualTo("refCode", refCode);
    //    query2.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        //query2.whereEqualTo("productType", "Accessories");
        query2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                double totalSales = 0.0;
                if (e == null){
                    for(int i=0; i < objects.size(); i++){
                        double gasCom = objects.get(i).getInt("gasCommission");
                        double accCom = objects.get(i).getInt("accCommission");
                        double totalCom = gasCom + accCom;
                        totalSales += totalCom;
                    }
                    textTotalCom.setText("N" + Double.toString(totalSales));
                    // Update the database
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Distributors");
                    // Retrieve the object by id
                    final double finalTotalSales = totalSales;
                    query.getInBackground(refCode, new GetCallback<ParseObject>() {
                        public void done(ParseObject entity, ParseException e) {
                            if (e == null) {
                                entity.put("totalCommission", finalTotalSales);
                                entity.saveInBackground();
                            }
                        }
                    });
                }
            }
        });

        // To display remaining balance
        // First get the total amount paid before
        ParseQuery<ParseObject> query4 = ParseQuery.getQuery("PaymentRequests");
        query4.whereEqualTo("Distributor", currentUser.getObjectId());
        query4.whereEqualTo("paymentStatus", "Paid");
    //    query4.setCachePolicy(ParseQuery.CachePolicy.CACHE_THEN_NETWORK);
        query4.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                double totalPaid = 0.0;
                if (e == null){
                    for(int i=0; i < objects.size(); i++){
                        int amount = objects.get(i).getInt("amount");
                        totalPaid += amount;
                    }
                   // int totalCom = Integer.parseInt(textTotalSales.getText().toString());
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Distributors");
                    query.whereEqualTo("userObjectId", currentUser.getObjectId());
                    final double finalTotalPaid = totalPaid;
                //    System.out.println("Total Commission Paid: " + totalPaid);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        public void done(ParseObject object, ParseException e) {
                            int totalCom = 0;
                            if (object == null) {
                                Log.d("score", "The getFirst request failed.");
                            } else {
                                Log.d("score", "Retrieved the object.");
                                totalCom = object.getInt("totalCommission");
                                System.out.println("Total Commission is " + totalCom);
                            }
                            double currentBalance = totalCom - finalTotalPaid;
                            textBalance.setText("N" + Double.toString(currentBalance));
                            // Update the database
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Distributors");
                            // Retrieve the object by id
                            final double finalCurrentBalance = currentBalance;
                            query.getInBackground(refCode, new GetCallback<ParseObject>() {
                                public void done(ParseObject entity, ParseException e) {
                                    if (e == null) {
                                        entity.put("currentBalance", finalCurrentBalance);
                                        entity.saveInBackground();
                                    }
                                }
                            });
                        }
                    });

                }
            }
        });

        progressBar.setVisibility(View.GONE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DistDashboard.this, PaymentRequest.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {

        super.onStart();

    //    LineChartView lineChartView = findViewById(R.id.chart);
        final PieChartView pieChartView = findViewById(R.id.chart2);

        final List pieData = new ArrayList<>();

        final ParseUser currentUser = ParseUser.getCurrentUser();
        final String refCode = currentUser.getString("referralCode");


        // Get Total Sales
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Distributors");
        query.getInBackground(refCode, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your game score
                    int totalSales = object.getInt("totalSales");
                    int gasSales = object.getInt("noOfGasSales");
                    int accSales = object.getInt("noOfAccSales");

                    if (totalSales == 0){

                    } else {
                        float gas = (gasSales * 100 / totalSales);
                        //   System.out.println("Gas sales end: " + gasSales);
                        float accessory = (accSales * 100 / totalSales);
                        //   System.out.println("Accessory sales end: " + accSales);

                        // Put the values gotten from the database into the piedata
                        pieData.add(new SliceValue(gas, Color.parseColor("#FAC926")).setLabel("Gas Refill\n" + gas + "%"));
                        pieData.add(new SliceValue(accessory, Color.parseColor("#019ADD")).setLabel("Accessories\n" + accessory + "%"));

                        PieChartData pieChartData = new PieChartData(pieData);
                        pieChartData.setHasLabels(true).setValueLabelTextSize(14);
                        pieChartData.setHasCenterCircle(true).setCenterText1("Sales in \npercentage").setCenterText1FontSize(14).setCenterText1Color(Color.parseColor("#212121"));
                        pieChartView.setPieChartData(pieChartData);
                    }
                } else {
                    // something went wrong
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dist_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
    //    if (id == R.id.action_settings) {
     //       return true;
     //   }

        return super.onOptionsItemSelected(item);
    }

 //   @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_payment) {
            Intent intent = new Intent (this, PaymentRequest.class);
            startActivity(intent);
        } else if (id == R.id.nav_profile) {
            Intent intent = new Intent (this, UserProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent (this, AboutGasify.class);
            startActivity(intent);
        } else if (id == R.id.nav_logout) {
            confirmDialog();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DistDashboard.this);

        builder
                .setMessage("Are you sure you would like to log out?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code
                        ParseUser.logOut();
                        ParseUser currentUser = ParseUser.getCurrentUser();
                        Intent intent = new Intent(DistDashboard.this, DistributorLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
