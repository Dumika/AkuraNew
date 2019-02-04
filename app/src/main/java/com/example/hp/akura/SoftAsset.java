package com.example.hp.akura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class SoftAsset extends AppCompatActivity {
    CardView cardAdd, cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_asset);



        cardAdd = findViewById(R.id.card_add);
        cardView = findViewById(R.id.card_view);


        cardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SoftAsset.this, SoftAdd.class);
                startActivity(i);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SoftAsset.this, SoftListing.class);
                startActivity(i);
            }
        });


    }
}

