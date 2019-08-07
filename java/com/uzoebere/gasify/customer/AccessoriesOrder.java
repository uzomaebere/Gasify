package com.uzoebere.gasify.customer;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.parse.*;
import com.uzoebere.gasify.R;
import com.uzoebere.gasify.models.AccessoriesAdapter;
import com.uzoebere.gasify.models.Products;

import java.util.ArrayList;
import java.util.List;

public class AccessoriesOrder extends AppCompatActivity {

    ArrayList<Products> products = new ArrayList<>();
    private RecyclerView rvProducts;
    private AccessoriesAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories_order);

        // Lookup the recyclerview in activity layout
        rvProducts = findViewById(R.id.recyclerView);
        rvProducts.setHasFixedSize(true);

        progressBar = findViewById(R.id.progressBar6);
        progressBar.setVisibility(View.VISIBLE);
       // associate the LayoutManager with the RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(AccessoriesOrder.this);
        rvProducts.setLayoutManager(linearLayoutManager);


        ParseQuery<Products> query = ParseQuery.getQuery(Products.class);
        query.whereEqualTo("productType", "Accessories");
        query.findInBackground(new FindCallback<Products>() {
            @Override
            public void done(List<Products> objects, ParseException e) {
                if (e == null){
                    for(int i=0; i < objects.size(); i++){
                        String prodName = (String) objects.get(i).get("ProductName");
                        System.out.println("Product name"+ i + "= " + prodName);
                        int price = (int) objects.get(i).get("price");
                        ParseFile image = (ParseFile) objects.get(i).get("Image");
                        Products productList = new Products();
                        productList.setProductName(prodName);
                        productList.setPrice(price);
                        productList.setImage(image);
                        products.add(productList);
                    }
                    adapter = new AccessoriesAdapter(AccessoriesOrder.this, products);
                    rvProducts.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

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
