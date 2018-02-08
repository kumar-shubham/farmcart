package com.farmkart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.farmkart.Interface.ItemClickListener;
import com.farmkart.ViewHolder.MenuViewHolder;
import com.farmkart.ViewHolder.ProductViewHolder;
import com.farmkart.models.Category;
import com.farmkart.models.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ProductList extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference productList;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    String categoryId = "";

    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //Init Database
        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Products");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        //get intent
        if(getIntent() != null){
            categoryId = getIntent().getStringExtra("categoryId");
            Log.d("TAGKUMAR1", categoryId);
            if(categoryId != null && !categoryId.isEmpty()){
                loadProductList(categoryId);
            }
        }
    }

    private void loadProductList(String categoryId) {

        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>
                (Product.class, R.layout.product_item, ProductViewHolder.class,
                          productList.orderByChild("MenuId").equalTo(categoryId)
                ) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.txtProductName.setText(model.getName());
                viewHolder.txtProductPrice.setText(model.getPrice());
                Picasso.with(getBaseContext()).load(model.getImage())
                        .into(viewHolder.imageView);
                final Product clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent productDetail = new Intent(ProductList.this, ProductDetail.class);
                        productDetail.putExtra("productId", adapter.getRef(position).getKey());
                        startActivity(productDetail);
                    }
                });
            }
        };
        Log.d("TAGKUMAR", "" + adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }
}
