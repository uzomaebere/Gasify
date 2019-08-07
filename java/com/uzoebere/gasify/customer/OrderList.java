package com.uzoebere.gasify.customer;

import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.parse.*;
import com.uzoebere.gasify.R;
import com.uzoebere.gasify.models.NewOrders;
import com.uzoebere.gasify.models.OrdersAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OrderList extends AppCompatActivity {

    ArrayList<NewOrders> orders = new ArrayList<>();
    private RecyclerView rvProducts;
    private OrdersAdapter adapter;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        ParseUser currentUser = ParseUser.getCurrentUser();
        // Lookup the recyclerview in activity layout
        rvProducts = findViewById(R.id.recyclerView);
        rvProducts.setHasFixedSize(true);

        progressBar = findViewById(R.id.progressBar8);
        progressBar.setVisibility(View.VISIBLE);
        // associate the LayoutManager with the RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrderList.this);
        rvProducts.setLayoutManager(linearLayoutManager);

        ParseQuery<NewOrders> query = ParseQuery.getQuery(NewOrders.class);
        query.whereEqualTo("custObjectId", currentUser.getObjectId());
        query.findInBackground(new FindCallback<NewOrders>() {
            @Override
            public void done(List<NewOrders> objects, ParseException e) {
                if (e == null){
                    for(int i=0; i < objects.size(); i++){
                        String orderId = objects.get(i).getObjectId();
                     //   System.out.println("Product name"+ i + "= " + prodName);
                        int price = objects.get(i).getInt("totalPrice");
                        Date date = objects.get(i).getCreatedAt();
                        String prodName = objects.get(i).getString("productName");
                        Double qty = objects.get(i).getQuantity();
                        String status = objects.get(i).getOrderStatus();
                    //    ParseFile image = objects.get(i).getImage();
                        NewOrders orderList = new NewOrders();
                        orderList.setProductName(prodName);
                        orderList.setPrice(price);
                        orderList.setOrderDate(date);
                        orderList.setQuantity(qty);
                        orderList.setOrderStatus(status);
                    //    orderList.setImage(image);
                        orders.add(orderList);
                    }
                    adapter = new OrdersAdapter(OrderList.this, orders);
                    rvProducts.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                }

            }
        });
    }
}
