package com.uzoebere.gasify.models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.parse.ParseUser;
import com.uzoebere.gasify.R;
import com.uzoebere.gasify.distributors.DistDashboard;
import com.uzoebere.gasify.distributors.DistributorLogin;

import java.text.BreakIterator;
import java.util.List;


public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private List<NewOrders> mOrders;
    private Context mContext;

    public OrdersAdapter(Context context, List<NewOrders> orders) {
        mOrders = orders;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.order_list, parent, false);

        ViewHolder holder = new ViewHolder(contactView, new ViewHolder.MyClickListener() {
            @Override
            public void status(int p) {
                // Implement your functionality for onEdit here
                confirmDialog();
            }

            @Override
            public void rating(int p) {
                // Implement your functionality for onDelete here
            }
        });
        return holder;
        // Return a new holder instance
    //    return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {

        NewOrders orders = mOrders.get(position);

        holder.textOrderID.setText(orders.getOrderId());
        holder.textDate.setText((CharSequence) orders.getOrderDate());
        holder.textQty.setText(Double.toString(orders.getQuantity()));
        holder.textViewTitle.setText(orders.getProductName());
        holder.textViewPrice.setText(Double.toString(orders.getPrice()));
        holder.textStatus.setText(orders.getOrderStatus());

        ImageView prodImage = holder.imageView;
        Glide.with(mContext).load(orders.getImage().getImage()).into(prodImage);

    }

    @Override
    public int getItemCount() {
        return mOrders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView textOrderID, textDate, textViewTitle, textViewPrice, textQty, textStatus;
        ImageView imageView;
        Button buttonStatus, buttonRating;
        MyClickListener listener;

        public ViewHolder(@NonNull View itemView, MyClickListener listener) {

            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewPrice = itemView.findViewById(R.id.textViewPrice);
            this.imageView = itemView.findViewById(R.id.prodImage);
            buttonStatus = itemView.findViewById(R.id.buttonStatus);
            buttonRating = itemView.findViewById(R.id.buttonRating);

            this.listener = listener;

            buttonStatus.setOnClickListener(this);
            buttonRating.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonStatus:
                    listener.status(this.getLayoutPosition());
                    break;
                case R.id.buttonRating:
                    listener.rating(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }

        public interface MyClickListener {
            void status(int p);
            void rating(int p);
        }
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder
                .setTitle("Order Status")
                .setMessage("Has your order been delivered?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Yes-code

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
