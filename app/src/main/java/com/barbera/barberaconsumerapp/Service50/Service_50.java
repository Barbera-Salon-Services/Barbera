package com.barbera.barberaconsumerapp.Service50;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.barbera.barberaconsumerapp.Bookings.BookingPage;
import com.barbera.barberaconsumerapp.R;

public class Service_50 extends AppCompatActivity {

    private CardView c1;
    private CardView c2;
    private CardView c3;
    private CardView c4;
    private CardView c5;
    private CardView c6;
    private CardView c7;
    private CardView c8;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_50);

        c1= findViewById(R.id.c1);
        c2= findViewById(R.id.c2);
        c3= findViewById(R.id.c3);
        c4= findViewById(R.id.c4);
        c5= findViewById(R.id.c5);
        c6= findViewById(R.id.c6);
        c7=findViewById(R.id.c7);
        c8=findViewById(R.id.c8);

        Intent intent = new Intent(Service_50.this, BookingPage.class);
        c1.setOnClickListener(v -> {
            String orderSummary = "(women)Fruit Facial \n (women)Normal full arm wax\n" +
                    "(women)Normal full leg wax\n" +
                    "(women)Under arm wax \n" +
                    "(women)Haircut (u,v)\n" +
                    "(women)Threading \n" +
                    "(women)Blowdry\n";
            intent.putExtra("Booking Amount",699);
            intent.putExtra("BookingType","direct");
            intent.putExtra("Order Summary",orderSummary);
            intent.putExtra("Time",150);
            startActivity(intent);
        });
        c2.setOnClickListener(v -> {
            String orderSummary = "(women)Fruit facial \n" +
                    "(women)Rica full leg wax\n" +
                    "(women)Rica full arm wax\n" +
                    "(women)Under arm\n" +
                    "(women)Threading \n" +
                    "(women)Haircut (u,v)\n";
            intent.putExtra("Booking Amount",999);
            intent.putExtra("BookingType","direct");
            intent.putExtra("Order Summary",orderSummary);
            intent.putExtra("Time",150);
            startActivity(intent);
        });
        c3.setOnClickListener(v -> {
            String orderSummary = "(women)Fruit facial\n" +
                    "(women)Haircut(u,v)\n" +
                    "(women)Threading\n";
            intent.putExtra("Booking Amount",399);
            intent.putExtra("BookingType","direct");
            intent.putExtra("Order Summary",orderSummary);
            intent.putExtra("Time",90);
            startActivity(intent);
        });
        c4.setOnClickListener(v -> {
            String orderSummary ="(women)Full leg wax normal\n" +
                    "(women)Full arm wax normal\n" +
                    "(women)Haircut (u,v)\n" +
                    "(women)Blow dry\n";
            intent.putExtra("Booking Amount",399);
            intent.putExtra("BookingType","direct");
            intent.putExtra("Order Summary",orderSummary);
            intent.putExtra("Time",90);
            startActivity(intent);
        });
        c5.setOnClickListener(v -> {
            String orderSummary="Full leg wax normal\n" +
                    "Full arm wax normal\n" +
                    "Haircut(u,v) \n" +
                    "Gold facial\n" +
                    "Blow dry\n" ;

            intent.putExtra("Booking Amount",899);
            intent.putExtra("BookingType","direct");
            intent.putExtra("Order Summary",orderSummary);
            intent.putExtra("Time",120);
            startActivity(intent);
        });
        c6.setOnClickListener(v -> {
            String orderSummary="(women)Full body milk wax\n" +
                    "(women)Blow dry\n" +
                    "(women)Haircut(any)\n" +
                    "(women)Gold facial\n" +
                    "(women)Threading\n";
            intent.putExtra("Booking Amount",1799);
            intent.putExtra("BookingType","direct");
            intent.putExtra("Order Summary",orderSummary);
            intent.putExtra("Time",200);
            startActivity(intent);
        });
        c7.setOnClickListener(v -> {
            String orderSummary ="(men)Kid haircut(upto 5 year)\n(men)haircut(adult)";
            intent.putExtra("Booking Amount",114);
            intent.putExtra("BookingType","direct");
            intent.putExtra("Order Summary",orderSummary);
            intent.putExtra("Time",60);
            startActivity(intent);
        });
        c8.setOnClickListener(v -> {
            String orderSummary ="(men)Gold facial \n(men) garnier hair-color ";
            intent.putExtra("Booking Amount",599);
            intent.putExtra("BookingType","direct");
            intent.putExtra("Order Summary",orderSummary);
            intent.putExtra("Time",60);
            startActivity(intent);
        });


    }
}