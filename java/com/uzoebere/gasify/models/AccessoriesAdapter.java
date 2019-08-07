package com.uzoebere.gasify.models;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.uzoebere.gasify.R;
import com.uzoebere.gasify.customer.AccessoryDetails;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AccessoriesAdapter extends RecyclerView.Adapter<AccessoriesAdapter.ViewHolder>{

    private List<Products> mProducts;
    private Context mContext;

    // Pass in the contact array into the constructor
    public AccessoriesAdapter(Context context, List<Products> products) {
        mProducts = products;
        mContext = context;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.accessories_layout, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NotNull AccessoriesAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Products products = mProducts.get(position);

        // Set item views based on your views and data model
        holder.textViewTitle.setText(products.getProductName());

        holder.textViewPrice.setText(Integer.toString(products.getPrice()));

        ImageView prodImage = holder.imageView;
        Glide.with(mContext).load(products.getImage().getUrl()).into(prodImage);


    }

    @Override
    public int getItemCount() {
        return mProducts.size();
    }

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewTitle, textViewPrice;
        ImageView imageView;
        private Context context;

        public ViewHolder(View itemView) {
            super(itemView);

            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewPrice = itemView.findViewById(R.id.textViewPrice);
            this.imageView = itemView.findViewById(R.id.prodImage);
            this.context = itemView.getContext();
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            Context context = v.getContext();
         //   Toast.makeText(context, textViewTitle.getText() , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, AccessoryDetails.class);
            intent.putExtra("productName", textViewTitle.getText());
            intent.putExtra("price", textViewPrice.getText());
            context.startActivity(intent);

        }
    }
}
