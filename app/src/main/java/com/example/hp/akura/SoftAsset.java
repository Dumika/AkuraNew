package com.example.hp.akura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SoftAsset extends AppCompatActivity {
    Button b1,b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soft_asset);

        b1=findViewById(R.id.btnAdd);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SoftAsset.this,  SoftAdd.class);
                startActivity(i);
            }
        });


//        b2=findViewById(R.id.btnView);
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(SoftAsset.this,  ItemView.class);
//                startActivity(i);
//            }
//        });
//
//        b3=findViewById(R.id.btnUpdate);
//        b3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(SoftAsset.this,  ItemUpdate.class);
//                startActivity(i);
//            }
//        });

        b4=findViewById(R.id.btnDelete);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SoftAsset.this,  ItemDelete.class);
                startActivity(i);
            }
        });

    }
}
