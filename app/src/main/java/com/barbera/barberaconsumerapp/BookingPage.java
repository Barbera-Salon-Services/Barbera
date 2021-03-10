package com.barbera.barberaconsumerapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.barbera.barberaconsumerapp.network.Emailer;
import com.barbera.barberaconsumerapp.network.JsonPlaceHolderApi;
import com.barbera.barberaconsumerapp.network.RetrofitClientInstance;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class BookingPage extends AppCompatActivity  {
    private String userAddress;//Address string to be stored in database
    public static EditText houseAddress;
    private Button ConfirmBooking;
    private String email;
    private TextView totalAmount;
    private TextView changeLocation;
    private int region;
    private double lat,lon;
    private TextView day1, day2, day3, day4, day5, day6, day7;
    private CardView slot1,slot2,slot3,slot4,slot5,slot6,slot7,slot8,slot9;
    private String mon1,mon2,mon3,mon4,mon5,mon6,mon7,mon,day;
    private SharedPreferences sharedPreferences;
    private int array[];
    private String OrderSummary = "";
    public static String finalDate;
    public static String finalTime;
    private String Username;
    private String UserPhone;
    private ProgressDialog progressDialog;
    public static boolean isCouponApplied;
    private String bookingType = "";
    private int listPosition;
    public int BookingTotalAmount;
    private LinearLayout linearLayout;
    private int selectedYear;
    private static EditText couponcodeEditText;
    private List<String> users;
    private TextView BookingOrders;
    private long limit;
    private boolean men=false,women=false;
    private int serviceTime,slotsBooked;
    private String bookingID;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_booking_page);

        Intent intent=getIntent();
        //or =(TextView)findViewById(R.id.or);
        bookingType+=intent.getStringExtra("BookingType");
        listPosition=intent.getIntExtra("Position",-1);
        BookingTotalAmount=intent.getIntExtra("Booking Amount",0);
        OrderSummary=intent.getStringExtra("Order Summary");
        serviceTime=intent.getIntExtra("Time",0);
        Log.d("Order",OrderSummary+"  "+serviceTime);

        String [] lines = OrderSummary.split("\n");
        for (String line : lines) {
            String sub=line.substring(line.indexOf("(") + 1, line.indexOf(")"));
            Log.d("sub", sub);
            Log.d("line", line);
            if (sub.equals("men")) {
                men = true;
            }
            if (sub.equals("women")) {
                women = true;
            }
        }
//        Toast.makeText(this, men+" "+women, Toast.LENGTH_SHORT).show();
        array = new int[2];
        array[0]= 100;
        array[1]= 100;
        //    private TextView date;
        //    private TextView chooseTime;
        //    private TextView time;
        //private CardView usecurrentAddress;
        TextView month = (TextView) findViewById(R.id.mon);

        changeLocation = findViewById(R.id.booking_choose_address);
        day1 = findViewById(R.id.day1);
        day2 =  findViewById(R.id.day2);
        day3 =  findViewById(R.id.day3);
        day4 =  findViewById(R.id.day4);
        day5 =  findViewById(R.id.day5);
        day6 =  findViewById(R.id.day6);
        day7 = findViewById(R.id.day7);

        slot1= findViewById(R.id.slot1);
        slot2= findViewById(R.id.slot2);
        slot3= findViewById(R.id.slot3);
        slot4= findViewById(R.id.slot4);
        slot5= findViewById(R.id.slot5);
        slot6= findViewById(R.id.slot6);
        slot7= findViewById(R.id.slot7);
        slot8= findViewById(R.id.slot8);
        slot9= findViewById(R.id.slot9);

        houseAddress=findViewById(R.id.booking_edit_address);
        ConfirmBooking=findViewById(R.id.booking_confirm_booking);
        totalAmount=findViewById(R.id.booking_total_amount);
        linearLayout = findViewById(R.id.ll);
        sharedPreferences =getSharedPreferences("UserInfo",MODE_PRIVATE);
        couponcodeEditText = findViewById(R.id.booking_couponCode_editText);
        Button couponApply =  findViewById(R.id.booking_coupon_apply_button);
        isCouponApplied=false;
        BookingOrders=findViewById(R.id.booking_order_summary);

        String addres=sharedPreferences.getString("Address","");

        if(addres.equals("NA") || addres.equals("")){
            finish();
        }
            totalAmount.setText("Total Amount Rs " +BookingTotalAmount);
            BookingOrders.setText(OrderSummary);


        //autodateandtime();
        /* Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,Calendar.YEAR);
        calendar.set(Calendar.MONTH,Calendar.MONTH);
        calendar.set(Calendar.DAY_OF_MONTH,Calendar.DAY_OF_MONTH);
        String currentday= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date.setText(currentday);*/
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String cur_month = month_date.format(calendar.getTime());
        mon1=cur_month;
        month.setText(cur_month + ", " + selectedYear);

        int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
//        calendar.add(Calendar.DAY_OF_MONTH,20);
//        selectedDay=calendar.get(Calendar.DAY_OF_MONTH);
        day1.setText(String.valueOf(selectedDay));

        int flag = 0;
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day2.setText(String.valueOf(selectedDay));
        String compMonth = month_date.format(calendar.getTime());
        mon2=compMonth;
        if (!compMonth.equals(cur_month)) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day3.setText(String.valueOf(selectedDay));
        compMonth = month_date.format(calendar.getTime());
        mon3=compMonth;
        if (!compMonth.equals(cur_month) && flag == 0) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day4.setText(String.valueOf(selectedDay));
        compMonth = month_date.format(calendar.getTime());
        mon4=compMonth;
        if (!compMonth.equals(cur_month) && flag == 0) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day5.setText(String.valueOf(selectedDay));
        mon5=compMonth;
        compMonth = month_date.format(calendar.getTime());
        if (!compMonth.equals(cur_month) && flag == 0) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day6.setText(String.valueOf(selectedDay));
        compMonth = month_date.format(calendar.getTime());
        mon6=compMonth;
        if (!compMonth.equals(cur_month) && flag == 0) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day7.setText(String.valueOf(selectedDay));
        compMonth = month_date.format(calendar.getTime());
        mon7=compMonth;
        if (!compMonth.equals(cur_month) && flag == 0) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }
        progressDialog=new ProgressDialog(BookingPage.this);
        progressDialog.setMessage("Loading...");

        fetchRegion();

        day1.setOnClickListener(v -> {
            day1.setTextColor(getResources().getColor(R.color.white));
            day1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            mon=mon1;
            day=day1.getText().toString();
            progressDialog.show();
//            setDefault();

            array[0]=1;
            day2.setTextColor(getResources().getColor(R.color.colorAccent));
            day2.setBackgroundColor(getResources().getColor(R.color.white));
            day3.setTextColor(getResources().getColor(R.color.colorAccent));
            day3.setBackgroundColor(getResources().getColor(R.color.white));
            day4.setTextColor(getResources().getColor(R.color.colorAccent));
            day4.setBackgroundColor(getResources().getColor(R.color.white));
            day5.setTextColor(getResources().getColor(R.color.colorAccent));
            day5.setBackgroundColor(getResources().getColor(R.color.white));
            day6.setTextColor(getResources().getColor(R.color.colorAccent));
            day6.setBackgroundColor(getResources().getColor(R.color.white));
            day7.setTextColor(getResources().getColor(R.color.colorAccent));
            day7.setBackgroundColor(getResources().getColor(R.color.white));

            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day1")
                    .collection("Region").document("Region"+region)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        slots(task);
                    }
                }
            });
        });
        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day2.setTextColor(getResources().getColor(R.color.white));
                day2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mon=mon2;
                day=day2.getText().toString();

                progressDialog.show();
//                setDefault();
                array[0]=2;
                day1.setTextColor(getResources().getColor(R.color.colorAccent));
                day1.setBackgroundColor(getResources().getColor(R.color.white));
                day3.setTextColor(getResources().getColor(R.color.colorAccent));
                day3.setBackgroundColor(getResources().getColor(R.color.white));
                day4.setTextColor(getResources().getColor(R.color.colorAccent));
                day4.setBackgroundColor(getResources().getColor(R.color.white));
                day5.setTextColor(getResources().getColor(R.color.colorAccent));
                day5.setBackgroundColor(getResources().getColor(R.color.white));
                day6.setTextColor(getResources().getColor(R.color.colorAccent));
                day6.setBackgroundColor(getResources().getColor(R.color.white));
                day7.setTextColor(getResources().getColor(R.color.colorAccent));
                day7.setBackgroundColor(getResources().getColor(R.color.white));

                FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day2")
                        .collection("Region").document("Region"+region)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            slots(task);
                        }
                    }
                });
            }
        });
        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day3.setTextColor(getResources().getColor(R.color.white));
                day3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mon=mon3;
                day=day3.getText().toString();

                progressDialog.show();
//                setDefault();
                array[0]=3;
                day1.setTextColor(getResources().getColor(R.color.colorAccent));
                day1.setBackgroundColor(getResources().getColor(R.color.white));
                day2.setTextColor(getResources().getColor(R.color.colorAccent));
                day2.setBackgroundColor(getResources().getColor(R.color.white));
                day4.setTextColor(getResources().getColor(R.color.colorAccent));
                day4.setBackgroundColor(getResources().getColor(R.color.white));
                day5.setTextColor(getResources().getColor(R.color.colorAccent));
                day5.setBackgroundColor(getResources().getColor(R.color.white));
                day6.setTextColor(getResources().getColor(R.color.colorAccent));
                day6.setBackgroundColor(getResources().getColor(R.color.white));
                day7.setTextColor(getResources().getColor(R.color.colorAccent));
                day7.setBackgroundColor(getResources().getColor(R.color.white));

                FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day3")
                        .collection("Region").document("Region"+region)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            slots(task);
                        }
                    }
                });
            }
        });
        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day4.setTextColor(getResources().getColor(R.color.white));
                day4.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mon=mon4;
                day=day4.getText().toString();

                progressDialog.show();
//                setDefault();
                array[0]=4;
                day1.setTextColor(getResources().getColor(R.color.colorAccent));
                day1.setBackgroundColor(getResources().getColor(R.color.white));
                day2.setTextColor(getResources().getColor(R.color.colorAccent));
                day2.setBackgroundColor(getResources().getColor(R.color.white));
                day3.setTextColor(getResources().getColor(R.color.colorAccent));
                day3.setBackgroundColor(getResources().getColor(R.color.white));
                day5.setTextColor(getResources().getColor(R.color.colorAccent));
                day5.setBackgroundColor(getResources().getColor(R.color.white));
                day6.setTextColor(getResources().getColor(R.color.colorAccent));
                day6.setBackgroundColor(getResources().getColor(R.color.white));
                day7.setTextColor(getResources().getColor(R.color.colorAccent));
                day7.setBackgroundColor(getResources().getColor(R.color.white));

                FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day4")
                        .collection("Region").document("Region"+region)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            slots(task);
                        }
                    }
                });
            }
        });
        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day5.setTextColor(getResources().getColor(R.color.white));
                day5.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mon=mon5;
                day=day5.getText().toString();

                progressDialog.show();
//                setDefault();
                array[0]=5;
                day1.setTextColor(getResources().getColor(R.color.colorAccent));
                day1.setBackgroundColor(getResources().getColor(R.color.white));
                day2.setTextColor(getResources().getColor(R.color.colorAccent));
                day2.setBackgroundColor(getResources().getColor(R.color.white));
                day3.setTextColor(getResources().getColor(R.color.colorAccent));
                day3.setBackgroundColor(getResources().getColor(R.color.white));
                day4.setTextColor(getResources().getColor(R.color.colorAccent));
                day4.setBackgroundColor(getResources().getColor(R.color.white));
                day6.setTextColor(getResources().getColor(R.color.colorAccent));
                day6.setBackgroundColor(getResources().getColor(R.color.white));
                day7.setTextColor(getResources().getColor(R.color.colorAccent));
                day7.setBackgroundColor(getResources().getColor(R.color.white));

                FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day5")
                        .collection("Region").document("Region"+region)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            slots(task);
                        }
                    }
                });
            }
        });
        day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day6.setTextColor(getResources().getColor(R.color.white));
                day6.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mon=mon6;
                day=day6.getText().toString();

                progressDialog.show();
//                setDefault();
                array[0]=6;
                day1.setTextColor(getResources().getColor(R.color.colorAccent));
                day1.setBackgroundColor(getResources().getColor(R.color.white));
                day2.setTextColor(getResources().getColor(R.color.colorAccent));
                day2.setBackgroundColor(getResources().getColor(R.color.white));
                day3.setTextColor(getResources().getColor(R.color.colorAccent));
                day3.setBackgroundColor(getResources().getColor(R.color.white));
                day5.setTextColor(getResources().getColor(R.color.colorAccent));
                day5.setBackgroundColor(getResources().getColor(R.color.white));
                day4.setTextColor(getResources().getColor(R.color.colorAccent));
                day4.setBackgroundColor(getResources().getColor(R.color.white));
                day7.setTextColor(getResources().getColor(R.color.colorAccent));
                day7.setBackgroundColor(getResources().getColor(R.color.white));

                FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day6")
                        .collection("Region").document("Region"+region)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            slots(task);
                        }
                    }
                });
            }
        });
        day7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                day7.setTextColor(getResources().getColor(R.color.white));
                day7.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mon=mon7;
                day=day7.getText().toString();
                progressDialog.show();

                array[0]=7;
                day1.setTextColor(getResources().getColor(R.color.colorAccent));
                day1.setBackgroundColor(getResources().getColor(R.color.white));
                day2.setTextColor(getResources().getColor(R.color.colorAccent));
                day2.setBackgroundColor(getResources().getColor(R.color.white));
                day3.setTextColor(getResources().getColor(R.color.colorAccent));
                day3.setBackgroundColor(getResources().getColor(R.color.white));
                day4.setTextColor(getResources().getColor(R.color.colorAccent));
                day4.setBackgroundColor(getResources().getColor(R.color.white));
                day5.setTextColor(getResources().getColor(R.color.colorAccent));
                day5.setBackgroundColor(getResources().getColor(R.color.white));
                day6.setTextColor(getResources().getColor(R.color.colorAccent));
                day6.setBackgroundColor(getResources().getColor(R.color.white));

                FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day7")
                        .collection("Region").document("Region"+region)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            slots(task);
                        }
                    }
                });
            }
        });
        useCurrentAddress();


        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingPage.this,MapSearchActivity.class));
            }
        });

        slot1.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day"+array[0])
                    .collection("Region").document("Region"+region)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Inside task "+array[0],Toast.LENGTH_SHORT).show();
                        slots(task);
                        slot1.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    }
                }
            });
            array[1]=9;

        });
        slot2.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day"+array[0])
                    .collection("Region").document("Region"+region)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        slots(task);
                        slot2.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    }
                }
            });
            array[1]=10;
        });
        slot3.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day"+array[0])
                    .collection("Region").document("Region"+region)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        slots(task);
                        slot3.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    }
                }
            });
            array[1]=11;
        });
        slot4.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day"+array[0])
                    .collection("Region").document("Region"+region)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        slots(task);
                        slot4.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    }
                }
            });
            array[1]=12;
        });
        slot5.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day"+array[0])
                    .collection("Region").document("Region"+region)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        slots(task);
                        slot5.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    }
                }
            });
            array[1]=13;
        });
        slot6.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day"+array[0])
                    .collection("Region").document("Region"+region)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        slots(task);
                        slot6.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    }
                }
            });
            array[1]=14;
        });
        slot7.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day"+array[0])
                    .collection("Region").document("Region"+region)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        slots(task);
                        slot7.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    }
                }
            });
            array[1]=15;
        });
        slot8.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day"+array[0])
                    .collection("Region").document("Region"+region)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        slots(task);
                        slot8.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    }
                }
            });
            array[1]=16;
        });
        slot9.setOnClickListener(v -> {
            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day"+array[0])
                    .collection("Region").document("Region"+region)
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        slots(task);
                        slot9.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    }
                }
            });
            array[1]=17;
        });

        ConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUserData()) {
                    final ProgressDialog progressDialog=new ProgressDialog(BookingPage.this);
                    progressDialog.setMessage("Hold on for a moment...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    String dt=mon+" "+day+", "+selectedYear;
                    finalDate=dt;
                    finalTime=array[1]+":00";
                    userAddress=houseAddress.getText().toString();
                   // Toast.makeText(getApplicationContext(),userAddress,Toast.LENGTH_SHORT).show();

                    sendemailconfirmation();
                    addtoDatabase();
                    addTosheet();
                    if(isCouponApplied)
                        addCouponUsage();
                    if(bookingType.equals("Cart")) {
                        Map<String, Object> updateCart = new HashMap<>();
                        updateCart.put("cart_list_size", (long) 0);
                        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("UserData").document("MyCart").set(updateCart)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            progressDialog.dismiss();
                                            dbQueries.cartList.clear();
                                            dbQueries.cartItemModelList.clear();
                                            BookingsActivity.checked=false;
//                                            BookingsActivity.bookingActivityList.clear();
                                            MainActivity.cartAdapter.notifyDataSetChanged();
                                            Intent intent1=new Intent(BookingPage.this, CongratulationsPage.class);
                                            intent1.putExtra("Booking Amount",BookingTotalAmount);
                                            intent1.putExtra("Order Summary",OrderSummary);
                                            startActivity(intent1);
                                            finish();
                                        } else
                                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                    else if(bookingType.equals("Coupon")){
                        MyCoupons.couponItemModelList.remove(MyCoupons.couponItemModelList.get(listPosition));
                        Map<String,Object> updateCouponData=new HashMap<>();
                        for(int i=0;i<MyCoupons.couponItemModelList.size();i++){
                            updateCouponData.put("service_"+i+1,MyCoupons.couponItemModelList.get(i).getServiceName());
                            updateCouponData.put("service_"+i+1+"_type",MyCoupons.couponItemModelList.get(i).getType());
                            updateCouponData.put("service_"+i+1+"_price",MyCoupons.couponItemModelList.get(i).getServicePrice());
                            updateCouponData.put("service_"+i+1+"_icon",MyCoupons.couponItemModelList.get(i).getImageId());
                        }
                        updateCouponData.put("coupons",(long)MyCoupons.couponItemModelList.size());
                        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("UserData").document("MyCoupons").set(updateCouponData)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressDialog.dismiss();
                                            BookingsActivity.checked=false;
//                                            BookingsActivity.bookingActivityList.clear();
                                            MyCoupons.couponsChecked=false;
                                            MyCoupons.couponItemModelList.clear();
                                            Intent intent1=new Intent(BookingPage.this, CongratulationsPage.class);
                                            intent1.putExtra("Booking Amount",BookingTotalAmount);
                                            intent1.putExtra("Order Summary",OrderSummary);
                                            startActivity(intent1);
                                            finish();
                                        }
                                    }
                                });
                    }
                    else {
                        progressDialog.dismiss();
                        BookingsActivity.checked=false;
//                        BookingsActivity.bookingActivityList.clear();
                        Intent intent1=new Intent(BookingPage.this, CongratulationsPage.class);
                        intent1.putExtra("Booking Amount",BookingTotalAmount);
                        intent1.putExtra("Order Summary",OrderSummary);
                        startActivity(intent1);
                        finish();
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("New_Address","");
                    editor.commit();
                }
            }
        });

        couponApply.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(couponcodeEditText.getText())){
                final ProgressDialog progressDialog=new ProgressDialog(BookingPage.this);
                progressDialog.setMessage("Please wait for a while...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                FirebaseFirestore.getInstance().collection("AppData").document("Coupons").get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    String couponCode=task.getResult().get("CouponName").toString();
                                    limit=task.getResult().getLong("CouponLimit");
                                    users=(List<String>)task.getResult().get("users");
                                    if(!couponcodeEditText.getText().toString().equals(couponCode)){
                                        couponcodeEditText.setError("No Such Coupon Exists");
                                        couponcodeEditText.requestFocus();
                                        progressDialog.dismiss();
                                    }
                                    else if(users.contains(FirebaseAuth.getInstance().getUid())){
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"You have already used this coupon",Toast.LENGTH_LONG).show();
                                    }
                                    else if(limit==0){
                                        progressDialog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Sorry, it has reached its limit!!",Toast.LENGTH_LONG).show();
                                    }
                                    else if(BookingTotalAmount<69){
                                        progressDialog.dismiss();
                                        couponcodeEditText.setError("Total Amount should be greater than or equal to 69");
                                        couponcodeEditText.requestFocus();
                                    }
                                    else{
                                        BookingTotalAmount=(BookingTotalAmount>=200?BookingTotalAmount-100:BookingTotalAmount/2);
                                        totalAmount.setText("Total Amount Rs" +BookingTotalAmount+"(Coupon Applied)");
                                        isCouponApplied=true;
                                        Toast.makeText(getApplicationContext(),"Coupon Applied Successfully.",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }
                                }
                            }
                        });
            }
            else {
                couponcodeEditText.setError("Please enter a coupon code first");
                couponcodeEditText.requestFocus();
            }
        });
    }

    private void sendemailconfirmation() {
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceholderApi =retrofit.create(JsonPlaceHolderApi.class);
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    email = task.getResult().get("Email Address").toString();
                    //Toast.makeText(getApplicationContext(), email +"cds",Toast.LENGTH_SHORT).show();
                    Emailer emailer = new Emailer(email,OrderSummary);
                    Call<Emailer> call = jsonPlaceholderApi.sendEmail(emailer);
                    call.enqueue(new Callback<Emailer>() {
                        @Override
                        public void onResponse(Call<Emailer> call, retrofit2.Response<Emailer> response) {

                        }

                        @Override
                        public void onFailure(Call<Emailer> call, Throwable t) {

                        }
                    });
                });
    }

    private void fetchRegion() {
        ProgressDialog p =new ProgressDialog(BookingPage.this);
        p.setMessage("Please Wait...");
        p.show();
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get().addOnCompleteListener(task -> {
                    String[] x = new String[2];
                    try {
                        String coord = task.getResult().get("Address1").toString();
                        x = coord.split(",");
                        lat = Double.parseDouble(x[0]);
                        lon = Double.parseDouble(x[1]);
                        getRegion();
                    }catch(Exception e){
                        p.dismiss();
                        Toast.makeText(getApplicationContext(),"Address fields not saved!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BookingPage.this,ChangeLocation.class));
                    }
                    p.dismiss();
                });
    }

    private void getRegion() {
        double radius = 8101.33;
        double radius1 =1718.21;
        double radius2 =1764.76;

        if(getdistanceinkm(new LatLng(26.930256,75.875947))*1000<=radius){
            region =1 ;
        }
        if(getdistanceinkm(new LatLng(26.949311,75.714512))*1000<=radius1 || getdistanceinkm(new LatLng(26.943649,75.748845))*1000<=radius2){
            region =2;
        }
//        Toast.makeText(getApplicationContext(),"dcs"+region,Toast.LENGTH_SHORT).show();
    }

    private double getdistanceinkm( LatLng latLng) {
        double lat1= latLng.latitude;
        double lon1= latLng.longitude;
        double lat2= lat;
        double lon2 = lon;
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       /* SharedPreferences sharedPreferences =getSharedPreferences("UserInfo",MODE_PRIVATE);
        if(!sharedPreferences.getString("New_Address","").equals("")) {
            address.setText(sharedPreferences.getString("New_Address", ""));
        }*/
    }

    private void  addtoDatabase() {
        Map<String,Object> bookingData=new HashMap<>();
        bookingData.put("service",OrderSummary);
        bookingData.put("date",finalDate);
        bookingData.put("time",finalTime);
        bookingData.put("address",userAddress);
        bookingData.put("total_amount",BookingTotalAmount);
        bookingData.put("status","pending");
        bookingData.put("total_time",serviceTime);
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Bookings")
                .document().set(bookingData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Map<String, Object> addressMap = new HashMap<>();
                    addressMap.put("house_address", houseAddress.getText().toString());
                    FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .update(addressMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });

                    if (serviceTime % 60 == 0) {
                        slotsBooked = serviceTime / 60;
                    } else {
                        slotsBooked = (serviceTime / 60) + 1;
                    }
                    if (men) {
                        Map<String, Object> date = new HashMap<>();
                        for (int i = array[1]; i < array[1] + slotsBooked && i < 18; i++) {
                            date.put(i + "_m", "B");
                        }
                        date.put("date", mon + " " + day + ", " + selectedYear);
                        FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day" + array[0])
                                .collection("Region").document("Region" + region).update(date)
                                .addOnCompleteListener(task1 ->  {

                                });
                    }
                    if (women) {
                        Map<String, Object> date = new HashMap<>();
                        for (int i = array[1]; i < array[1] + slotsBooked && i < 18; i++) {
                            date.put(i + "_f", "B");
                        }
                        date.put("date", mon + " " + day + ", " + selectedYear);
                        FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day" + array[0])
                                .collection("Region").document("Region" + region).update(date)
                                .addOnCompleteListener(task1 -> {

                                });
                    }
                }
                else
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void extractDataFromUser() {
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Username=task.getResult().get("Name").toString();
                            UserPhone=task.getResult().get("Phone").toString();
                        }
                    }
                });

    }

    private void addTosheet(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwtLJ3Ts_ObuVoM0iuPOj1aOvf2wIy0E0Q6J56VtdMExqUWPvn4jAOURwxfHHE8zJIUIA/exec"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"Entered on REsponse",Toast.LENGTH_SHORT).show();
                //Toast.makeText(BookingPage.this,response,Toast.LENGTH_LONG).show();
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parmas = new HashMap<>();
                //here we pass params
                if(region==1){
                    parmas.put("action","addItem1");
                }
                else if(region==2){
                    parmas.put("action","addItem2");
                }

                parmas.put("userName",Username);
                parmas.put("services",OrderSummary);
                parmas.put("servicedate",finalDate);
                parmas.put("servicetime",finalTime);
                parmas.put("total",String.valueOf(BookingTotalAmount));
                parmas.put("address",userAddress);
                parmas.put("phone", UserPhone);
                if(isCouponApplied)
                 parmas.put("covid_warrior","Yes");
                else
                    parmas.put("covid_warrior","No");


                return parmas;
            }
        };
        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private boolean checkUserData() {
       if(houseAddress.getText().toString().isEmpty()){
            houseAddress.setError("Please Enter an Address");
            houseAddress.requestFocus();
            Toast.makeText(getApplicationContext(),"Please Choose An Address",Toast.LENGTH_SHORT).show();
            return false;
        }
       if(array[0] == 100 || array[1] ==100){
           Toast.makeText(getApplicationContext(),"Date/Time Not selected!",Toast.LENGTH_SHORT).show();
           return false;
       }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        extractDataFromUser();
        FirebaseFirestore.getInstance().collection("AppData").document("CoOrdinates").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            MapSearchActivity.radius=task.getResult().getDouble("ag_radius");
                            MapSearchActivity.radius1=task.getResult().getDouble("kal_1_radius");
                            MapSearchActivity.radius2=task.getResult().getDouble("kal_2_radius");
                            MapSearchActivity.center=new LatLng(task.getResult().getDouble("c1_lat "), task.getResult().getDouble("c1_lon"));
                            MapSearchActivity.center1=new LatLng(task.getResult().getDouble("c2_lat"), task.getResult().getDouble("c2_lon"));
                            MapSearchActivity.center2=new LatLng(task.getResult().getDouble("c3_lat"), task.getResult().getDouble("c3_lon"));

                        }
                    }
                });
    }

    private void addCouponUsage(){
        HashMap<String, Object> data=new HashMap<>();
        users.add(FirebaseAuth.getInstance().getUid());
        data.put("CouponLimit",--limit);
        data.put("users", FieldValue.arrayUnion(FirebaseAuth.getInstance().getUid()));
        FirebaseFirestore.getInstance().collection("AppData").document("Coupons")
                .update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getApplicationContext(),"Coupon Applied Successfully. Don't Revert this Booking, you will lose the couopon",Toast.LENGTH_LONG).show();
                    //isCouponApplied=true;
                   // progressDialog.dismiss();
                }
            }
        });

    }

    private void useCurrentAddress(){
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().get("house_address")!=null) {
                                String location = task.getResult().get("house_address").toString();
                                houseAddress.setText(location);

                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void slots(Task<DocumentSnapshot> task) {
        linearLayout.setVisibility(View.VISIBLE);
            if (men && !women) {
                if (task.getResult().get("9_m").toString().equals("B")) {
                    slot1.setEnabled(false);
                    slot1.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot1.setEnabled(true);
                    slot1.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("10_m").toString().equals("B")) {
                    slot2.setEnabled(false);
                    slot2.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot2.setEnabled(true);
                    slot2.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("11_m").toString().equals("B")) {
                    slot3.setEnabled(false);
                    slot3.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot3.setEnabled(true);
                    slot3.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("12_m").toString().equals("B")) {
                    slot4.setEnabled(false);
                    slot4.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot4.setEnabled(true);
                    slot4.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("13_m").toString().equals("B")) {
                    slot5.setEnabled(false);
                    slot5.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot5.setEnabled(true);
                    slot5.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("14_m").toString().equals("B")) {
                    slot6.setEnabled(false);
                    slot6.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot6.setEnabled(true);
                    slot6.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("15_m").toString().equals("B")) {
                    slot7.setEnabled(false);
                    slot7.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot7.setEnabled(true);
                    slot7.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("16_m").toString().equals("B")) {
                    slot8.setEnabled(false);
                    slot8.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot8.setEnabled(true);
                    slot8.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("17_m").toString().equals("B")) {
                    slot9.setEnabled(false);
                    slot9.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot9.setEnabled(true);
                    slot9.setCardBackgroundColor(Color.BLACK);
                }
            } else if (women && !men) {
                if (task.getResult().get("9_f").toString().equals("B")) {
                    slot1.setEnabled(false);
                    slot1.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot1.setEnabled(true);
                    slot1.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("10_f").toString().equals("B")) {
                    slot2.setEnabled(false);
                    slot2.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot2.setEnabled(true);
                    slot2.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("11_f").toString().equals("B")) {
                    slot3.setEnabled(false);
                    slot3.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot3.setEnabled(true);
                    slot3.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("12_f").toString().equals("B")) {
                    slot4.setEnabled(false);
                    slot4.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot4.setEnabled(true);
                    slot4.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("13_f").toString().equals("B")) {
                    slot5.setEnabled(false);
                    slot5.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot5.setEnabled(true);
                    slot5.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("14_f").toString().equals("B")) {
                    slot6.setEnabled(false);
                    slot6.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot6.setEnabled(true);
                    slot6.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("15_f").toString().equals("B")) {
                    slot7.setEnabled(false);
                    slot7.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot7.setEnabled(true);
                    slot7.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("16_f").toString().equals("B")) {
                    slot8.setEnabled(false);
                    slot8.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot8.setEnabled(true);
                    slot8.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("17_f").toString().equals("B")) {
                    slot9.setEnabled(false);
                    slot9.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot9.setEnabled(true);
                    slot9.setCardBackgroundColor(Color.BLACK);
                }
            } else {
                if (task.getResult().get("9_m").toString().equals("B") || task.getResult().get("9_f").toString().equals("B")) {
                    slot1.setEnabled(false);
                    slot1.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot1.setEnabled(true);
                    slot1.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("10_m").toString().equals("B") || task.getResult().get("10_f").toString().equals("B")) {
                    slot2.setEnabled(false);
                    slot2.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot2.setEnabled(true);
                    slot2.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("11_m").toString().equals("B") || task.getResult().get("11_f").toString().equals("B")) {
                    slot3.setEnabled(false);
                    slot3.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot3.setEnabled(true);
                    slot3.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("12_m").toString().equals("B") || task.getResult().get("12_f").toString().equals("B")) {
                    slot4.setEnabled(false);
                    slot4.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot4.setEnabled(true);
                    slot4.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("13_m").toString().equals("B") || task.getResult().get("13_f").toString().equals("B")) {
                    slot5.setEnabled(false);
                    slot5.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot5.setEnabled(true);
                    slot5.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("14_m").toString().equals("B") || task.getResult().get("14_f").toString().equals("B")) {
                    slot6.setEnabled(false);
                    slot6.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot6.setEnabled(true);
                    slot6.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("15_m").toString().equals("B") || task.getResult().get("15_f").toString().equals("B")) {
                    slot7.setEnabled(false);
                    slot7.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot7.setEnabled(true);
                    slot7.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("16_m").toString().equals("B") || task.getResult().get("16_f").toString().equals("B")) {
                    slot8.setEnabled(false);
                    slot8.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot8.setEnabled(true);
                    slot8.setCardBackgroundColor(Color.BLACK);
                }
                if (task.getResult().get("17_m").toString().equals("B") || task.getResult().get("17_f").toString().equals("B")) {
                    slot9.setEnabled(false);
                    slot9.setCardBackgroundColor(Color.GRAY);
                } else {
                    slot9.setEnabled(true);
                    slot9.setCardBackgroundColor(Color.BLACK);
                }
        }
        progressDialog.dismiss();
    }
}