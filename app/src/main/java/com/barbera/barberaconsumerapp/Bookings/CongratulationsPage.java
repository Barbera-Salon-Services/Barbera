package com.barbera.barberaconsumerapp.Bookings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.barbera.barberaconsumerapp.HomeActivity;
import com.barbera.barberaconsumerapp.R;

public class CongratulationsPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations_page);

        Intent intent=getIntent();
        int totalAmount=intent.getIntExtra("Booking Amount",0);
        String summary=intent.getStringExtra("Order Summary");
        String date=intent.getStringExtra("date");

        CardView backtohome=(CardView) findViewById(R.id.backtoHome);
        TextView finalSummary=(TextView)findViewById(R.id.finalSummary);
        TextView finalAmount=(TextView)findViewById(R.id.finalPageAmount);
        TextView finalDateTime=(TextView)findViewById(R.id.finalPagedate);
        TextView confirmation=findViewById(R.id.accepted);
        String x=BookingPage.finalTime;
        int y=Integer.parseInt(x.split(":")[0]);
        String a,b;
        if(y>=12){
            a="pm";
        }
        else{
            a="am";
        }
        y++;
        if(y>=12){
            b="pm";
        }
        else{
            b="am";
        }

        confirmation.setText("The service person will reach at your place on "+date+" between "+BookingPage.finalTime+a+" to "+y+":00"+b);
        finalDateTime.setText(BookingPage.finalDate+"\t\t\t\t\t"+BookingPage.finalTime);

        finalAmount.setText("Total Rs "+totalAmount);

        finalSummary.setText(summary);

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CongratulationsPage.this, HomeActivity.class));
                finish();
            }
        });

        //BookingPage.BookingTotalAmount="";
    }
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
        overridePendingTransition(0,0);
        finish();
    }
}