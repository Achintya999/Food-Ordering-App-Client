package com.example.achintya.foodorderingforclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static com.example.achintya.foodorderingforclient.R.id.foodImage;

public class MenuActivity extends AppCompatActivity {

    private RecyclerView mFoodList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mFoodList = (RecyclerView)findViewById(R.id.foodList);
        mFoodList.setHasFixedSize(true);
        mFoodList.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){

                    Intent loginIntent = new Intent(MenuActivity.this, MainActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginIntent);

                }

            }
        };

    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter<Food, FoodViewHolder> FBRA = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(
                Food.class, R.layout.singlemenuitem, FoodViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                viewHolder.setName(model.getName());
                viewHolder.setPrice(model.getPrice());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(), model.getImage());


                final String food_key = getRef(position).getKey().toString();

                viewHolder.view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent singleFoodActivity = new Intent(MenuActivity.this, SingleFoodActivity.class);
                        singleFoodActivity.putExtra("FoodId", food_key);
                        startActivity(singleFoodActivity);


                    }
                });


            }
        };
        mFoodList.setAdapter(FBRA);
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder{

        View view;

        public FoodViewHolder(View itemName){

            super(itemName);
            view = itemName;

        }


        public void setName(String name){

            TextView food_name = (TextView)view.findViewById(R.id.foodName);
            food_name.setText(name);

        }
        public void setDesc(String desc){

            TextView food_desc = (TextView)view.findViewById(R.id.foodDesc);
            food_desc.setText(desc);


        }
        public void setPrice(String price){

            TextView food_price = (TextView)view.findViewById(R.id.foodPrice);
            food_price.setText(price);


        }
        public void setImage(Context ctx, String image){

            ImageView food_image = (ImageView)view.findViewById(foodImage);

            Picasso.with(ctx).load(image).into(food_image);


        }

    }
}
