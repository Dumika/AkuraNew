package com.example.hp.akura;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {
    Button b1,b2,b3,b4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
//            finish();
//            startActivity(new Intent(this, Login.class));
//
//        }

//        b1=findViewById(R.id.btnHard);
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(Home.this,  HardAsset.class);
//                startActivity(i);
//            }
//        });
//
//        b2=findViewById(R.id.btnSoft);
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(Home.this,  SoftAdd.class);
//                startActivity(i);
//            }
//        });
//
//        b3=findViewById(R.id.btnMaintenance);
//        b3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(Home.this,  MaintenanceAdd.class);
//                startActivity(i);
//            }
//        });
//
//
//        b4=findViewById(R.id.btnReserve);
//        b4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(Home.this,  Reservation.class);
//                startActivity(i);
//            }
//        });
    }
}
