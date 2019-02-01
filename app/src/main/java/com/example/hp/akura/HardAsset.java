package com.example.hp.akura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HardAsset extends AppCompatActivity {
    Button b1,b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hard_asset);

        b1=findViewById(R.id.btnAdd);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HardAsset.this,  ItemAdd.class);  //
                startActivity(i);
            }
        });


        b2=findViewById(R.id.btnView);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HardAsset.this,  ItemListing.class);
                startActivity(i);
            }
        });

//        b3=findViewById(R.id.btnUpdate);
//        b3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(HardAsset.this,  ItemUpdate.class);
//                startActivity(i);
//            }
//        });
//
//        b4=findViewById(R.id.btnDelete);
//        b4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(HardAsset.this,  ItemDelete.class);
//                startActivity(i);
//            }
//        });
    }
}
