package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class CongratulationsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations_page);


        CardView backtohome=(CardView) findViewById(R.id.backtoHome);
        TextView finalSummary=(TextView)findViewById(R.id.finalSummary);
        TextView finalAmount=(TextView)findViewById(R.id.finalPageAmount);
        TextView finalDateTime=(TextView)findViewById(R.id.finalPagedate);

        finalDateTime.setText(BookingPage.finalDate+"\t\t\t\t\t"+BookingPage.finalTime);

        finalAmount.setText("Total   Rs"+BookingPage.BookingTotalAmount);

        finalSummary.setText(BookingPage.OrderSummary);

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CongratulationsPage.this,MainActivity.class));
                finish();
            }
        });

        BookingPage.BookingTotalAmount="";
    }
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        overridePendingTransition(0,0);
        finish();
        super.onBackPressed();
    }
}