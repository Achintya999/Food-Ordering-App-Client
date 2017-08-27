 package com.example.achintya.foodorderingforclient;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

 public class SingleFoodActivity extends AppCompatActivity {

     private String food_key = null;
     private DatabaseReference mDatabase, user_data;
     private TextView singleFoodTitle, singleFoodDesc, singleFoodPrice;
     private ImageView singleFoodImage;
     private Button orderButton;
     private FirebaseAuth mAuth;
     private FirebaseUser current_user;

     private DatabaseReference myRef;

     private String food_name, food_price, food_desc, food_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_food);

        food_key = getIntent().getExtras().getString("FoodId");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Item");


        singleFoodTitle = (TextView)findViewById(R.id.singleTitle);
        singleFoodDesc = (TextView)findViewById(R.id.singleDesc);
        singleFoodPrice = (TextView)findViewById(R.id.singelPrice);

        singleFoodImage = (ImageView)findViewById(R.id.singleImageView);

        mAuth = FirebaseAuth.getInstance();

        current_user = mAuth.getCurrentUser();

        user_data = FirebaseDatabase.getInstance().getReference().child("users").child(current_user.getUid());
        myRef = FirebaseDatabase.getInstance().getReference().child("Orders");

        mDatabase.child(food_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                food_name = (String)dataSnapshot.child("name").getValue();
                food_price = (String)dataSnapshot.child("price").getValue();
                food_desc = (String)dataSnapshot.child("desc").getValue();
                food_image = (String)dataSnapshot.child("image").getValue();

                singleFoodTitle.setText(food_name);
                singleFoodPrice.setText(food_price);
                singleFoodDesc.setText(food_desc);

                Picasso.with(SingleFoodActivity.this).load(food_image).into(singleFoodImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    public void orderItemClicked(View view){

        final DatabaseReference newOrder = myRef.push();
        user_data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                newOrder.child("itemname").setValue(food_name);
                newOrder.child("username").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        startActivity(new Intent(SingleFoodActivity.this, MenuActivity.class));

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
