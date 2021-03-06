package com.example.hp.akura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;

public class HardAsset extends AppCompatActivity {
    CardView cardAdd, cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_asset);

        cardAdd = findViewById(R.id.card_add);
        cardView = findViewById(R.id.card_view);

        cardAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HardAsset.this, ItemAdd.class);
                startActivity(i);
            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HardAsset.this, ItemListing.class);
                startActivity(i);
            }
        });


    }
}

