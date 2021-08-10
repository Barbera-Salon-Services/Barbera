package com.barbera.barberaconsumerapp.Bookings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.barbera.barberaconsumerapp.CheckTermDialog;
import com.barbera.barberaconsumerapp.MapSearchActivity;
import com.barbera.barberaconsumerapp.Profile.MyCoupons;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Utils.CartItemModel;
import com.barbera.barberaconsumerapp.Utils.CouponItem;
import com.barbera.barberaconsumerapp.Utils.InstItem;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceBooking;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceCoupon;
import com.barbera.barberaconsumerapp.network_email.Emailer;
import com.barbera.barberaconsumerapp.network_email.JsonPlaceHolderApi;
import com.barbera.barberaconsumerapp.network_email.RetrofitClientInstance;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class BookingPage extends AppCompatActivity implements CheckTermDialog.CheckTerms {
    private String userAddress;//Address string to be stored in database
    public static EditText houseAddress;
    public static boolean inMap=false;
    private Button ConfirmBooking;
    private String email,token;
    private TextView totalAmount,couponInfo;
    private TextView changeLocation;
    private int region,typesel=0;
    private Boolean checkterms = false;
    private boolean isBarberFound=false;
    private double lat, lon;
    private List<CartItemModel> sidlist;
    private TextView btype1,btype2,btype3;
    private TextView day1, day2, day3, day4, day5, day6, day7;
    private CardView tim1,tim2,tim3,tim4,tim5,tim6;
    private CardView slot1, slot2, slot3, slot4, slot5, slot6, slot7, slot8, slot9, slot10,slot11,slot12,slot13;
    private String mon1, mon2, mon3, mon4, mon5, mon6, mon7, mon, day;
    private SharedPreferences sharedPreferences;
    private int array[];
    private String OrderSummary = "",time="";
    public static String finalDate;
    public static String finalTime;
    private String barberIdRet,slotRet;
    private ProgressDialog progressDialog;
    public static boolean isCouponApplied;
    public static boolean isReferApplied;
    private String bookingType = "";
    private int listPosition;
    public int BookingTotalAmount;
    private int selectedYear;
    private static EditText couponcodeEditText;
    private List<String> users;
    private TextView BookingOrders;
    private long limit;
    private CardView couponApl;
    private TextView text;
    private boolean men = false, women = false;
    private int serviceTime, slotsBooked;
    private String randomId = "Barbera" + (int) (Math.random() * 9000000);
    private LinearLayout male_slots, female_slots, mf_slots,time_ll;
    private RelativeLayout rl;
    private CheckBox checkBox;
    private Button bookInst;
    private TextView InstText;
    private String couponServiceId="";
    private int upper=-1,lower=-1,curAmount;
    private String couponName="";
//    private ImageView drop;
    private RelativeLayout calendar;
    private LinearLayout slotBtn;

    @Override
    public void extractBool(Boolean selected) {
        checkterms = selected;
        if (checkterms) {
//            Toast.makeText(getApplicationContext(), "Checked", Toast.LENGTH_SHORT).show();
            if (checkUserData()) {

//                final ProgressDialog progressDialog = new ProgressDialog(BookingPage.this);
//                progressDialog.setMessage("Hold on for a moment...");
//                progressDialog.show();
//                progressDialog.setCancelable(false);
                //Toast.makeText(getApplicationContext(),"sd",Toast.LENGTH_SHORT).show();
                if (day.equals("1")) {
                    day = "01";
                }
                if (day.equals("2")) {
                    day = "02";
                }
                if (day.equals("3")) {
                    day = "03";
                }
                if (day.equals("4")) {
                    day = "04";
                }
                if (day.equals("5")) {
                    day = "05";
                }
                if (day.equals("6")) {
                    day = "06";
                }
                if (day.equals("7")) {
                    day = "07";
                }
                if (day.equals("8")) {
                    day = "08";
                }
                if (day.equals("9")) {
                    day = "09";
                }

                String dt = mon + " " + day + ", " + selectedYear;
                finalDate = dt;
                String mn = "";

                if (mon.equals("Jul")) {
                    mn = "07";
                } else if (mon.equals("Jan")) {
                    mn = "01";
                } else if (mon.equals("Feb")) {
                    mn = "02";
                } else if (mon.equals("Mar")) {
                    mn = "03";
                } else if (mon.equals("Apr")) {
                    mn = "04";
                } else if (mon.equals("May")) {
                    mn = "05";
                } else if (mon.equals("Jun")) {
                    mn = "06";
                } else if (mon.equals("Aug")) {
                    mn = "08";
                } else if (mon.equals("Sep")) {
                    mn = "09";
                } else if (mon.equals("Oct")) {
                    mn = "10";
                } else if (mon.equals("Nov")) {
                    mn = "11";
                } else if (mon.equals("Dec")) {
                    mn = "12";
                }
                String dat = day + "-" + mn + "-" + selectedYear;
                String slot = array[1] + time;
                finalTime = array[1] + ":00";
                userAddress = houseAddress.getText().toString();
                // Toast.makeText(getApplicationContext(),userAddress,Toast.LENGTH_SHORT).show();


                //addtoDatabase();
                //addTosheet();
//                    progressDialog.dismiss();
                //Toast.makeText(getApplicationContext(), "sd", Toast.LENGTH_SHORT).show();
                BookingsActivity.checked = false;
                Retrofit retrofit1= RetrofitClientInstanceBooking.getRetrofitInstance();
                JsonPlaceHolderApi2 jsonPlaceHolderApi21=retrofit1.create(JsonPlaceHolderApi2.class);

                ProgressDialog progressDialog = new ProgressDialog(BookingPage.this);
                progressDialog.setMessage("Loading");
                progressDialog.setCancelable(true);
                progressDialog.show();
                Log.d("priceLast",curAmount+"");
                Call<InstItem> call = jsonPlaceHolderApi21.bookSlot(new ServiceIdList(sidlist, null, null, curAmount,couponName), dat, array[1] + "", "Bearer " + token);
                call.enqueue(new Callback<InstItem>() {
                    @Override
                    public void onResponse(Call<InstItem> call, retrofit2.Response<InstItem> response) {
                        if (response.code() == 200) {
                            if (response.body().isSuccess()) {
                                if (bookingType.equals("Cart")) {
                                    Call<Void> call1 = jsonPlaceHolderApi21.deleteCart(new ServiceIdList(sidlist, null, null, curAmount,couponName), "Bearer " + token);
                                    call1.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                            if (response.code() == 200) {
                                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("Count", MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                editor.putInt("count", 0);
                                                editor.apply();
                                            } else {

                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call, Throwable t) {

                                        }
                                    });
                                }
                                if(isBarberFound){
                                    Toast.makeText(BookingPage.this, "Barber found!", Toast.LENGTH_SHORT).show();
                                    Call<Void> call1=jsonPlaceHolderApi21.revertBooking(new ServiceIdList(sidlist,barberIdRet,slotRet,curAmount,couponName),"Bearer "+token);
                                    call1.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                        if(response.code()==200){

                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Could not book slot",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                }

                                progressDialog.dismiss();
                                Intent intent1 = new Intent(BookingPage.this, CongratulationsPage.class);
                                intent1.putExtra("Booking Amount", curAmount);
                                intent1.putExtra("Order Summary", OrderSummary);
                                intent1.putExtra("date", dat);
                                intent1.putExtra("slot", slot);
                                intent1.putExtra("sidlist", (Serializable) sidlist);
                                startActivity(intent1);
                                finish();

                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "This slot has already been booked", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Could not book slot", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<InstItem> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
//                        BookingsActivity.bookingActivityList.clear();

                });


            }
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_booking_page);
        int count = 0;
        Retrofit retrofit= RetrofitClientInstanceCoupon.getRetrofitInstance();
        Retrofit retrofit1= RetrofitClientInstanceBooking.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
        JsonPlaceHolderApi2 jsonPlaceHolderApi21=retrofit1.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = getSharedPreferences("Token", MODE_PRIVATE);
        token = preferences.getString("token", "no");

        bookInst=findViewById(R.id.book_inst);
        InstText=findViewById(R.id.inst_book_text);
        houseAddress = findViewById(R.id.booking_edit_address);
        ConfirmBooking = findViewById(R.id.booking_confirm_booking);
        totalAmount = findViewById(R.id.booking_total_amount);
        sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE);
        couponcodeEditText = findViewById(R.id.booking_couponCode_editText);
        couponInfo =findViewById(R.id.couponInfo);

        couponcodeEditText.setSelection(couponcodeEditText.getText().length());

        couponcodeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    couponcodeEditText.setHint("");
                }
                else{
                    couponcodeEditText.setHint("Enter Coupon Code");
                }
            }
        });

        Button couponApply = findViewById(R.id.booking_coupon_apply_button);
        slotBtn=findViewById(R.id.book_a_slot);
        isCouponApplied = false;
        BookingOrders = findViewById(R.id.booking_order_summary);
        //drop=findViewById(R.id.drop_down_arrow);
        calendar=findViewById(R.id.Calender);
//        final int[] a = {0};
//        drop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(a[0] ==0){
//                    couponcodeEditText.setVisibility(View.VISIBLE);
//                    couponApply.setVisibility(View.VISIBLE);
//
//                    a[0]++;
//                }
//                else{
//                    couponcodeEditText.setVisibility(View.GONE);
//                    couponApply.setVisibility(View.GONE);
//
//                    a[0] =0;
//                }
//            }
//        });
        final int[] b = {0};
        slotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b[0] ==0){
                    calendar.setVisibility(View.VISIBLE);
                    ConfirmBooking.setVisibility(View.VISIBLE);
                    b[0] =1;
                }
                else{
                    calendar.setVisibility(View.GONE);
                    ConfirmBooking.setVisibility(View.GONE);
                    b[0]=0;
                }
            }
        });
        male_slots = (LinearLayout) findViewById(R.id.dt);
//        time_ll=findViewById(R.id.llt);
//        tim1=findViewById(R.id.t6);
//        tim2=findViewById(R.id.t1);
//        tim3=findViewById(R.id.t2);
//        tim4=findViewById(R.id.t3);
//        tim5=findViewById(R.id.t4);
//        tim6=findViewById(R.id.t5);
//        female_slots = (LinearLayout) findViewById(R.id.ll2);
//        mf_slots = (LinearLayout) findViewById(R.id.ll3);
        Intent intent = getIntent();
        couponApl = findViewById(R.id.viscoup);
        text = findViewById(R.id.line_coupon_text);
        //or =(TextView)findViewById(R.id.or);
        rl=findViewById(R.id.dispose);
        checkBox = findViewById(R.id.apron);
        bookingType += intent.getStringExtra("BookingType");
        listPosition = intent.getIntExtra("Position", -1);
        BookingTotalAmount = intent.getIntExtra("Booking Amount", 0);
        OrderSummary = intent.getStringExtra("Order Summary");
        serviceTime = intent.getIntExtra("Time",0);
        sidlist = (List<CartItemModel>) intent.getSerializableExtra("sidlist");
        Log.d("Order", OrderSummary + "  " + serviceTime);
        curAmount=BookingTotalAmount;

        Call<InstItem> call= jsonPlaceHolderApi21.bookInst(new ServiceIdList(sidlist,null,null,curAmount,couponName),"Bearer "+token);
        call.enqueue(new Callback<InstItem>() {
            @Override
            public void onResponse(Call<InstItem> call, retrofit2.Response<InstItem> response) {
                if(response.code()==200){
                    InstItem instItem=response.body();
                    barberIdRet=instItem.getId();
                    slotRet=instItem.getSlot();
                    if(!instItem.isSuccess()){
                        isBarberFound=false;
                        InstText.setText("No barber nearby");
                        bookInst.setVisibility(View.GONE);
                    }
                    else{
                        isBarberFound=true;
                        bookInst.setVisibility(View.VISIBLE);
                        InstText.setText("Nearest barber is "+instItem.getTime()+"min away.");
                    }
                }
                else{
                    InstText.setText("No bookings available now");
                    bookInst.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(),"Could not get instant booking",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InstItem> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        if(bookingType.equals("trend")){
            if(OrderSummary.equals("(men)Simple Hair Cut  Rs79") || OrderSummary.equals("(men)Stylish Hair Cut  Rs99")
                || OrderSummary.equals("(women)Hair Cut(U , V , straight)  Rs99")
                    || OrderSummary.equals("(women)Stylish Hair Cut(StepCut,LayerCut)  Rs199")){
                rl.setVisibility(View.GONE);
                count =1;
            }
        }
        if(bookingType.equals("direct")){
            String[] Orders = OrderSummary.split("\n", 5);
            for(String Order: Orders) {
                if (Order.equals("(men)Simple Hair Cut\t\t\tRs79") || Order.equals("(men)Stylish Hair Cut\t\t\tRs99")
                        || Order.equals("(women)Hair Cut(U , V , straight)\t\t\tRs99") || Order.equals("(women)Stylish Hair Cut(StepCut, LayerCut)\t\t\tRs199")) {
                    if(count == 0){
                        rl.setVisibility(View.GONE);
                    }
                    count++;
                }
            }
        }
        if(bookingType.equals("Cart")){
            String[] Orders = OrderSummary.split("\n",10);
            for(String Order: Orders){
                if(Order.startsWith("(men)Simple Hair Cut") || Order.startsWith("(men)Stylish Hair Cut") || Order.startsWith("(women)Hair Cut(U , V , straight)") ||
                Order.startsWith("(women)Stylish Hair Cut(StepCut, LayerCut)")){
                    if(count == 0){
                        rl.setVisibility(View.GONE);
                    }
                    count+= Integer.parseInt(Order.substring(Order.lastIndexOf('(')+1,Order.lastIndexOf(')')));
//                    Toast.makeText(getApplicationContext(), "test 1 pass"+count, Toast.LENGTH_SHORT).show();
                }
            }
        }
        int finalCount = count;
        checkBox.setOnClickListener(v -> {
            if(checkBox.isChecked())
                BookingTotalAmount+= 10* finalCount;
            else
                BookingTotalAmount-=10* finalCount;
            totalAmount.setText("Total Amount Rs " + BookingTotalAmount);
        });

        String[] lines = OrderSummary.split("\n");
        for (String line : lines) {
            String sub = line.substring(line.indexOf("(") + 1, line.indexOf(")"));
            Log.d("sub", sub);
            Log.d("line", line);
            if (sub.equals("men")) {
                men = true;
            }
            if (sub.equals("women")) {
                women = true;
            }
        }
        men=true;
        women=false;
        if (men && !women) {
            slot1 = findViewById(R.id.slot1);
            slot2 = findViewById(R.id.slot2);
            slot3 = findViewById(R.id.slot3);
            slot4 = findViewById(R.id.slot4);
            slot5 = findViewById(R.id.slot5);
            slot6 = findViewById(R.id.slot6);
            slot7 = findViewById(R.id.slot7);
            slot8 = findViewById(R.id.slot8);
            slot9 = findViewById(R.id.slot9);
            slot10 = findViewById(R.id.slot10);
            slot11=findViewById(R.id.slot11);
            slot12=findViewById(R.id.slot12);
            slot13=findViewById(R.id.slot13);
        }
        array = new int[2];
        array[0] = 100;
        array[1] = 100;
        //    private TextView date;
        //    private TextView chooseTime;
        //    private TextView time;
        //private CardView usecurrentAddress;
        TextView month = (TextView) findViewById(R.id.mon);

        changeLocation = findViewById(R.id.booking_choose_address);
        day1 = findViewById(R.id.day1);
        day2 = findViewById(R.id.day2);
        day3 = findViewById(R.id.day3);
        day4 = findViewById(R.id.day4);
        day5 = findViewById(R.id.day5);
        day6 = findViewById(R.id.day6);
        day7 = findViewById(R.id.day7);

        btype1=findViewById(R.id.daytoday);
        btype2=findViewById(R.id.weekly);
        btype3=findViewById(R.id.monthly);

//        String addres = sharedPreferences.getString("Address", "");
//
//        if (addres.equals("NA") || addres.equals("")) {
//            finish();
//        }
        totalAmount.setText("Total Amount Rs " + BookingTotalAmount);
        BookingOrders.setText(OrderSummary);

        bookInst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(BookingPage.this);
                builder.setMessage("Are you sure to book?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call1=jsonPlaceHolderApi21.confirmBooking(new ServiceIdList(sidlist,barberIdRet,slotRet,curAmount,couponName),"Bearer "+token);
                        call1.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                if(response.code()==200){
                                    Date c = Calendar.getInstance().getTime();
                                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    String formattedDate = df.format(c);

                                    Intent intent1 = new Intent(BookingPage.this, CongratulationsPage.class);
                                    intent1.putExtra("Booking Amount", BookingTotalAmount);
                                    intent1.putExtra("Order Summary", OrderSummary);
                                    finalTime=slotRet;
                                    finalDate=formattedDate;
                                    startActivity(intent1);
                                    finish();
                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Could not confirm booking",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Call<Void> call1=jsonPlaceHolderApi21.revertBooking(new ServiceIdList(sidlist,barberIdRet,slotRet,curAmount,couponName),"Bearer "+token);
                        call1.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                                if(response.code()==200){

                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Could not cancel booking",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });

        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String cur_month = month_date.format(calendar.getTime());
        mon1 = cur_month;
        month.setText(cur_month + ", " + selectedYear);

        int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day1.setText(String.valueOf(selectedDay));

        int flag = 0;
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day2.setText(String.valueOf(selectedDay));
        String compMonth = month_date.format(calendar.getTime());
        mon2 = compMonth;
        if (!compMonth.equals(cur_month)) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day3.setText(String.valueOf(selectedDay));
        compMonth = month_date.format(calendar.getTime());
        mon3 = compMonth;
        if (!compMonth.equals(cur_month) && flag == 0) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day4.setText(String.valueOf(selectedDay));
        compMonth = month_date.format(calendar.getTime());
        mon4 = compMonth;
        if (!compMonth.equals(cur_month) && flag == 0) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day5.setText(String.valueOf(selectedDay));
        mon5 = compMonth;
        compMonth = month_date.format(calendar.getTime());
        if (!compMonth.equals(cur_month) && flag == 0) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day6.setText(String.valueOf(selectedDay));
        compMonth = month_date.format(calendar.getTime());
        mon6 = compMonth;
        if (!compMonth.equals(cur_month) && flag == 0) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
        day7.setText(String.valueOf(selectedDay));
        compMonth = month_date.format(calendar.getTime());
        mon7 = compMonth;
        if (!compMonth.equals(cur_month) && flag == 0) {
            month.setText(cur_month + "/" + compMonth + ", " + selectedYear);
            flag = 1;
        }
        progressDialog = new ProgressDialog(BookingPage.this);
        progressDialog.setMessage("Loading...");

        //fetchRegion();
        typesel=1;
//        btype1.setOnClickListener(v -> {
//            btype1.setTextColor(getResources().getColor(R.color.white));
//            btype1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//            btype2.setTextColor(getResources().getColor(R.color.colorAccent));
//            btype2.setBackgroundColor(getResources().getColor(R.color.white));
//            btype3.setTextColor(getResources().getColor(R.color.colorAccent));
//            btype3.setBackgroundColor(getResources().getColor(R.color.white));
//            typesel=1;
//        });
//        btype2.setOnClickListener(v -> {
//            btype2.setTextColor(getResources().getColor(R.color.white));
//            btype2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//            btype1.setTextColor(getResources().getColor(R.color.colorAccent));
//            btype1.setBackgroundColor(getResources().getColor(R.color.white));
//            btype3.setTextColor(getResources().getColor(R.color.colorAccent));
//            btype3.setBackgroundColor(getResources().getColor(R.color.white));
//            typesel=2;
//        });
//        btype3.setOnClickListener(v -> {
//            btype3.setTextColor(getResources().getColor(R.color.white));
//            btype3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
//            btype2.setTextColor(getResources().getColor(R.color.colorAccent));
//            btype2.setBackgroundColor(getResources().getColor(R.color.white));
//            btype1.setTextColor(getResources().getColor(R.color.colorAccent));
//            btype1.setBackgroundColor(getResources().getColor(R.color.white));
//            typesel=3;
//        });

        day1.setOnClickListener(v -> {
            if(typesel==0){
                Toast.makeText(this,"Please select the type of booking you want to make",Toast.LENGTH_SHORT).show();
            }
            else {
                day1.setTextColor(getResources().getColor(R.color.white));
                day1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                mon = mon1;
                day = day1.getText().toString();
                male_slots.setVisibility(View.VISIBLE);
                //time_ll.setVisibility(View.VISIBLE);
                array[0] = 1;
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
            }
                disableUnavialableSlots();

        });
        day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typesel==0){
                Toast.makeText(getApplicationContext(),"Please select the type of booking you want to make",Toast.LENGTH_SHORT).show();
            }
            else {
                    day2.setTextColor(getResources().getColor(R.color.white));
                    day2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mon = mon2;
                    day = day2.getText().toString();

                    male_slots.setVisibility(View.VISIBLE);
                    //time_ll.setVisibility(View.VISIBLE);
                    //progressDialog.show();
//                setDefault();
                    array[0] = 2;
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

                }
                enableAvialableSlots();
            }
        });
        day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typesel==0){
                Toast.makeText(getApplicationContext(),"Please select the type of booking you want to make",Toast.LENGTH_SHORT).show();
            }
            else {
                    male_slots.setVisibility(View.VISIBLE);
                    //time_ll.setVisibility(View.VISIBLE);

                    day3.setTextColor(getResources().getColor(R.color.white));
                    day3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mon = mon3;
                    day = day3.getText().toString();
                    //progressDialog.show();
//                setDefault();
                    array[0] = 3;
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

                }
                enableAvialableSlots();
            }
        });
        day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typesel==0){
                Toast.makeText(getApplicationContext(),"Please select the type of booking you want to make",Toast.LENGTH_SHORT).show();
            }
            else {
                    male_slots.setVisibility(View.VISIBLE);
                    //time_ll.setVisibility(View.VISIBLE);

                    day4.setTextColor(getResources().getColor(R.color.white));
                    day4.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mon = mon4;
                    day = day4.getText().toString();
                    //progressDialog.show();
//                setDefault();
                    array[0] = 4;
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

                }
                enableAvialableSlots();
            }
        });
        day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typesel==0){
                Toast.makeText(getApplicationContext(),"Please select the type of booking you want to make",Toast.LENGTH_SHORT).show();
            }
            else {
                    male_slots.setVisibility(View.VISIBLE);
                    //time_ll.setVisibility(View.VISIBLE);

                    day5.setTextColor(getResources().getColor(R.color.white));
                    day5.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mon = mon5;
                    day = day5.getText().toString();
                    //progressDialog.show();
//                setDefault();
                    array[0] = 5;
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

                }
                enableAvialableSlots();
            }
        });
        day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(typesel==0){
                Toast.makeText(getApplicationContext(),"Please select the type of booking you want to make",Toast.LENGTH_SHORT).show();
            }
            else {
                    male_slots.setVisibility(View.VISIBLE);
                    //time_ll.setVisibility(View.VISIBLE);

                    day6.setTextColor(getResources().getColor(R.color.white));
                    day6.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mon = mon6;
                    day = day6.getText().toString();

                    //progressDialog.show();
//                setDefault();
                    array[0] = 6;
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

                }
                enableAvialableSlots();
            }
        });
        day7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (typesel == 0) {
                    Toast.makeText(getApplicationContext(), "Please select the type of booking you want to make", Toast.LENGTH_SHORT).show();
                } else {
                    male_slots.setVisibility(View.VISIBLE);
                    //time_ll.setVisibility(View.VISIBLE);

                    day7.setTextColor(getResources().getColor(R.color.white));
                    day7.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    mon = mon7;
                    day = day7.getText().toString();
                    //progressDialog.show();

                    array[0] = 7;
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

                }
                enableAvialableSlots();
            }
        });
        useCurrentAddress();

        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inMap=true;
                startActivity(new Intent(BookingPage.this, MapSearchActivity.class));
            }
        });

        slot1.setOnClickListener(v -> {
            slot1.setCardBackgroundColor(Color.parseColor("#27AE60"));
            slot2.setCardBackgroundColor(Color.BLACK);
            slot3.setCardBackgroundColor(Color.BLACK);
            slot4.setCardBackgroundColor(Color.BLACK);
            slot5.setCardBackgroundColor(Color.BLACK);
            slot6.setCardBackgroundColor(Color.BLACK);
            slot7.setCardBackgroundColor(Color.BLACK);
            slot8.setCardBackgroundColor(Color.BLACK);
            slot9.setCardBackgroundColor(Color.BLACK);
            slot10.setCardBackgroundColor(Color.BLACK);
            slot11.setCardBackgroundColor(Color.BLACK);
            slot12.setCardBackgroundColor(Color.BLACK);
            slot13.setCardBackgroundColor(Color.BLACK);
            if (men && !women) {
                array[1] = 6;
            } else {
                array[1] = 9;
            }
            if(array[0]==1){
                disableUnavialableSlots();
            }
        });
//        tim1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tim1.setCardBackgroundColor(Color.parseColor("#27AE60"));
//                tim2.setCardBackgroundColor(Color.BLACK);
//                tim3.setCardBackgroundColor(Color.BLACK);
//                tim4.setCardBackgroundColor(Color.BLACK);
//                tim5.setCardBackgroundColor(Color.BLACK);
//                tim6.setCardBackgroundColor(Color.BLACK);
//                time="00";
//
//            }
//        });
        slot2.setOnClickListener(v -> {
            slot2.setCardBackgroundColor(Color.parseColor("#27AE60"));
            slot1.setCardBackgroundColor(Color.BLACK);
            slot3.setCardBackgroundColor(Color.BLACK);
            slot4.setCardBackgroundColor(Color.BLACK);
            slot5.setCardBackgroundColor(Color.BLACK);
            slot6.setCardBackgroundColor(Color.BLACK);
            slot7.setCardBackgroundColor(Color.BLACK);
            slot8.setCardBackgroundColor(Color.BLACK);
            slot9.setCardBackgroundColor(Color.BLACK);
            slot10.setCardBackgroundColor(Color.BLACK);
            slot11.setCardBackgroundColor(Color.BLACK);
            slot12.setCardBackgroundColor(Color.BLACK);
            slot13.setCardBackgroundColor(Color.BLACK);
            if (men && !women) {
                array[1] = 7;
            } else {
                array[1] = 10;
            }
            if(array[0]==1){
                disableUnavialableSlots();
            }
        });
//        tim2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tim2.setCardBackgroundColor(Color.parseColor("#27AE60"));
//                tim1.setCardBackgroundColor(Color.BLACK);
//                tim3.setCardBackgroundColor(Color.BLACK);
//                tim4.setCardBackgroundColor(Color.BLACK);
//                tim5.setCardBackgroundColor(Color.BLACK);
//                tim6.setCardBackgroundColor(Color.BLACK);
//                time="10";
//
//            }
//        });
        slot3.setOnClickListener(v -> {
            slot3.setCardBackgroundColor(Color.parseColor("#27AE60"));
            slot2.setCardBackgroundColor(Color.BLACK);
            slot1.setCardBackgroundColor(Color.BLACK);
            slot4.setCardBackgroundColor(Color.BLACK);
            slot5.setCardBackgroundColor(Color.BLACK);
            slot6.setCardBackgroundColor(Color.BLACK);
            slot7.setCardBackgroundColor(Color.BLACK);
            slot8.setCardBackgroundColor(Color.BLACK);
            slot9.setCardBackgroundColor(Color.BLACK);
            slot10.setCardBackgroundColor(Color.BLACK);
            slot11.setCardBackgroundColor(Color.BLACK);
            slot12.setCardBackgroundColor(Color.BLACK);
            slot13.setCardBackgroundColor(Color.BLACK);
            if (men && !women) {
                array[1] = 8;
            } else {
                array[1] = 11;
            }
            if(array[0]==1){
                disableUnavialableSlots();
            }
        });
//        tim3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tim3.setCardBackgroundColor(Color.parseColor("#27AE60"));
//                tim2.setCardBackgroundColor(Color.BLACK);
//                tim1.setCardBackgroundColor(Color.BLACK);
//                tim4.setCardBackgroundColor(Color.BLACK);
//                tim5.setCardBackgroundColor(Color.BLACK);
//                tim6.setCardBackgroundColor(Color.BLACK);
//                time="20";
//
//            }
//        });
        slot4.setOnClickListener(v -> {
            slot4.setCardBackgroundColor(Color.parseColor("#27AE60"));
            slot2.setCardBackgroundColor(Color.BLACK);
            slot3.setCardBackgroundColor(Color.BLACK);
            slot1.setCardBackgroundColor(Color.BLACK);
            slot5.setCardBackgroundColor(Color.BLACK);
            slot6.setCardBackgroundColor(Color.BLACK);
            slot7.setCardBackgroundColor(Color.BLACK);
            slot8.setCardBackgroundColor(Color.BLACK);
            slot9.setCardBackgroundColor(Color.BLACK);
            slot10.setCardBackgroundColor(Color.BLACK);
            slot11.setCardBackgroundColor(Color.BLACK);
            slot12.setCardBackgroundColor(Color.BLACK);
            slot13.setCardBackgroundColor(Color.BLACK);
            if (men && !women) {
                array[1] = 9;
            } else {
                array[1] = 12;
            }
            if(array[0]==1){
                disableUnavialableSlots();
            }
        });
//        tim5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tim5.setCardBackgroundColor(Color.parseColor("#27AE60"));
//                tim2.setCardBackgroundColor(Color.BLACK);
//                tim3.setCardBackgroundColor(Color.BLACK);
//                tim4.setCardBackgroundColor(Color.BLACK);
//                tim1.setCardBackgroundColor(Color.BLACK);
//                tim6.setCardBackgroundColor(Color.BLACK);
//                time="40";
//
//            }
//        });
        slot5.setOnClickListener(v -> {
            slot5.setCardBackgroundColor(Color.parseColor("#27AE60"));
            slot2.setCardBackgroundColor(Color.BLACK);
            slot3.setCardBackgroundColor(Color.BLACK);
            slot4.setCardBackgroundColor(Color.BLACK);
            slot1.setCardBackgroundColor(Color.BLACK);
            slot6.setCardBackgroundColor(Color.BLACK);
            slot7.setCardBackgroundColor(Color.BLACK);
            slot8.setCardBackgroundColor(Color.BLACK);
            slot9.setCardBackgroundColor(Color.BLACK);
            slot10.setCardBackgroundColor(Color.BLACK);
            slot11.setCardBackgroundColor(Color.BLACK);
            slot12.setCardBackgroundColor(Color.BLACK);
            slot13.setCardBackgroundColor(Color.BLACK);
            if (men && !women) {
                array[1] = 10;
            } else if (women && !men) {
                array[1] = 13;
            } else {
                array[1] = 16;
            }
            if(array[0]==1){
                disableUnavialableSlots();
            }
        });
//        tim4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tim4.setCardBackgroundColor(Color.parseColor("#27AE60"));
//                tim2.setCardBackgroundColor(Color.BLACK);
//                tim3.setCardBackgroundColor(Color.BLACK);
//                tim1.setCardBackgroundColor(Color.BLACK);
//                tim5.setCardBackgroundColor(Color.BLACK);
//                tim6.setCardBackgroundColor(Color.BLACK);
//                time="30";
//
//            }
//        });
        slot6.setOnClickListener(v -> {
            slot6.setCardBackgroundColor(Color.parseColor("#27AE60"));
            slot2.setCardBackgroundColor(Color.BLACK);
            slot3.setCardBackgroundColor(Color.BLACK);
            slot4.setCardBackgroundColor(Color.BLACK);
            slot5.setCardBackgroundColor(Color.BLACK);
            slot1.setCardBackgroundColor(Color.BLACK);
            slot7.setCardBackgroundColor(Color.BLACK);
            slot8.setCardBackgroundColor(Color.BLACK);
            slot9.setCardBackgroundColor(Color.BLACK);
            slot10.setCardBackgroundColor(Color.BLACK);
            slot11.setCardBackgroundColor(Color.BLACK);
            slot12.setCardBackgroundColor(Color.BLACK);
            slot13.setCardBackgroundColor(Color.BLACK);
            if (men && !women) {
                array[1] = 11;
            } else if (women && !men) {
                array[1] = 14;
            } else {
                array[1] = 17;
            }
            if(array[0]==1){
                disableUnavialableSlots();
            }
        });
//        tim6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tim6.setCardBackgroundColor(Color.parseColor("#27AE60"));
//                tim2.setCardBackgroundColor(Color.BLACK);
//                tim3.setCardBackgroundColor(Color.BLACK);
//                tim4.setCardBackgroundColor(Color.BLACK);
//                tim5.setCardBackgroundColor(Color.BLACK);
//                tim1.setCardBackgroundColor(Color.BLACK);
//                time="50";
//
//            }
//        });
        if (!(men && women)) {
            slot7.setOnClickListener(v -> {
                slot7.setCardBackgroundColor(Color.parseColor("#27AE60"));
                slot2.setCardBackgroundColor(Color.BLACK);
                slot3.setCardBackgroundColor(Color.BLACK);
                slot4.setCardBackgroundColor(Color.BLACK);
                slot5.setCardBackgroundColor(Color.BLACK);
                slot6.setCardBackgroundColor(Color.BLACK);
                slot1.setCardBackgroundColor(Color.BLACK);
                slot8.setCardBackgroundColor(Color.BLACK);
                slot9.setCardBackgroundColor(Color.BLACK);
                slot10.setCardBackgroundColor(Color.BLACK);
                slot11.setCardBackgroundColor(Color.BLACK);
                slot12.setCardBackgroundColor(Color.BLACK);
                slot13.setCardBackgroundColor(Color.BLACK);
                if (men && !women) {
                    array[1] = 12;
                } else {
                    array[1] = 15;
                }
                if(array[0]==1){
                disableUnavialableSlots();
            }
            });
            slot8.setOnClickListener(v -> {
                slot8.setCardBackgroundColor(Color.parseColor("#27AE60"));slot2.setCardBackgroundColor(Color.BLACK);
                slot3.setCardBackgroundColor(Color.BLACK);
                slot2.setCardBackgroundColor(Color.BLACK);
                slot4.setCardBackgroundColor(Color.BLACK);
                slot5.setCardBackgroundColor(Color.BLACK);
                slot6.setCardBackgroundColor(Color.BLACK);
                slot7.setCardBackgroundColor(Color.BLACK);
                slot1.setCardBackgroundColor(Color.BLACK);
                slot9.setCardBackgroundColor(Color.BLACK);
                slot10.setCardBackgroundColor(Color.BLACK);
                slot11.setCardBackgroundColor(Color.BLACK);
                slot12.setCardBackgroundColor(Color.BLACK);
                slot13.setCardBackgroundColor(Color.BLACK);
                if (men && !women) {
                    array[1] = 13;
                } else {
                    array[1] = 16;
                }
                if(array[0]==1){
                disableUnavialableSlots();
            }
            });
            slot9.setOnClickListener(v -> {
                slot9.setCardBackgroundColor(Color.parseColor("#27AE60"));
                slot2.setCardBackgroundColor(Color.BLACK);
                slot3.setCardBackgroundColor(Color.BLACK);
                slot4.setCardBackgroundColor(Color.BLACK);
                slot5.setCardBackgroundColor(Color.BLACK);
                slot6.setCardBackgroundColor(Color.BLACK);
                slot7.setCardBackgroundColor(Color.BLACK);
                slot8.setCardBackgroundColor(Color.BLACK);
                slot1.setCardBackgroundColor(Color.BLACK);
                slot10.setCardBackgroundColor(Color.BLACK);
                slot11.setCardBackgroundColor(Color.BLACK);
                slot12.setCardBackgroundColor(Color.BLACK);
                slot13.setCardBackgroundColor(Color.BLACK);
                if (men && !women) {
                    array[1] = 14;
                } else {
                    array[1] = 17;
                }
                if(array[0]==1){
                disableUnavialableSlots();
            }
            });
            if (men && !women) {
                slot10.setOnClickListener(v -> {
                    slot10.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    slot2.setCardBackgroundColor(Color.BLACK);
                    slot3.setCardBackgroundColor(Color.BLACK);
                    slot4.setCardBackgroundColor(Color.BLACK);
                    slot5.setCardBackgroundColor(Color.BLACK);
                    slot6.setCardBackgroundColor(Color.BLACK);
                    slot7.setCardBackgroundColor(Color.BLACK);
                    slot8.setCardBackgroundColor(Color.BLACK);
                    slot1.setCardBackgroundColor(Color.BLACK);
                    slot9.setCardBackgroundColor(Color.BLACK);
                    slot11.setCardBackgroundColor(Color.BLACK);
                    slot12.setCardBackgroundColor(Color.BLACK);
                    slot13.setCardBackgroundColor(Color.BLACK);
                    array[1] = 15;
                    if(array[0]==1){
                disableUnavialableSlots();
            }
                });
                slot11.setOnClickListener(v -> {
                    slot11.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    slot2.setCardBackgroundColor(Color.BLACK);
                    slot3.setCardBackgroundColor(Color.BLACK);
                    slot4.setCardBackgroundColor(Color.BLACK);
                    slot5.setCardBackgroundColor(Color.BLACK);
                    slot6.setCardBackgroundColor(Color.BLACK);
                    slot7.setCardBackgroundColor(Color.BLACK);
                    slot8.setCardBackgroundColor(Color.BLACK);
                    slot1.setCardBackgroundColor(Color.BLACK);
                    slot9.setCardBackgroundColor(Color.BLACK);
                    slot10.setCardBackgroundColor(Color.BLACK);
                    slot12.setCardBackgroundColor(Color.BLACK);
                    slot13.setCardBackgroundColor(Color.BLACK);
                    array[1] = 16;
                    if(array[0]==1){
                disableUnavialableSlots();
            }
                });
                slot12.setOnClickListener(v -> {
                    slot12.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    slot2.setCardBackgroundColor(Color.BLACK);
                    slot3.setCardBackgroundColor(Color.BLACK);
                    slot4.setCardBackgroundColor(Color.BLACK);
                    slot5.setCardBackgroundColor(Color.BLACK);
                    slot6.setCardBackgroundColor(Color.BLACK);
                    slot7.setCardBackgroundColor(Color.BLACK);
                    slot8.setCardBackgroundColor(Color.BLACK);
                    slot1.setCardBackgroundColor(Color.BLACK);
                    slot9.setCardBackgroundColor(Color.BLACK);
                    slot11.setCardBackgroundColor(Color.BLACK);
                    slot10.setCardBackgroundColor(Color.BLACK);
                    slot13.setCardBackgroundColor(Color.BLACK);
                    array[1] = 17;
                    if(array[0]==1){
                disableUnavialableSlots();
            }
                });
                slot13.setOnClickListener(v -> {
                    slot13.setCardBackgroundColor(Color.parseColor("#27AE60"));
                    slot2.setCardBackgroundColor(Color.BLACK);
                    slot3.setCardBackgroundColor(Color.BLACK);
                    slot4.setCardBackgroundColor(Color.BLACK);
                    slot5.setCardBackgroundColor(Color.BLACK);
                    slot6.setCardBackgroundColor(Color.BLACK);
                    slot7.setCardBackgroundColor(Color.BLACK);
                    slot8.setCardBackgroundColor(Color.BLACK);
                    slot1.setCardBackgroundColor(Color.BLACK);
                    slot9.setCardBackgroundColor(Color.BLACK);
                    slot11.setCardBackgroundColor(Color.BLACK);
                    slot10.setCardBackgroundColor(Color.BLACK);
                    slot12.setCardBackgroundColor(Color.BLACK);
                    array[1] = 18;
                    if(array[0]==1){
                        disableUnavialableSlots();
                    }
                });
            }
        }


        ConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkDialog();
            }
        });

        couponApply.setOnClickListener(v -> {
            if(!TextUtils.isEmpty(couponcodeEditText.getText())){
                Call<CouponItem> couponItemCall=jsonPlaceHolderApi2.applyCoupon(new CartItemModel(null,couponcodeEditText.getText().toString(),0,null,0,0,null,false,null),"Bearer "+token);
                couponItemCall.enqueue(new Callback<CouponItem>() {
                    @Override
                    public void onResponse(Call<CouponItem> call, retrofit2.Response<CouponItem> response) {
                        if(response.code()==200){
                            CouponItem item=response.body();
                            couponServiceId=item.getServiceId();
                            upper=item.getUpperLimit();
                            lower=item.getLowerLimit();
                            couponName=couponcodeEditText.getText().toString();
                            Toast.makeText(getApplicationContext(),"Coupon applied!",Toast.LENGTH_LONG).show();
                            if(couponServiceId.equals("all")){
                                for(CartItemModel model:sidlist){
                                    if(upper!=-1){
                                        if(model.getServicePrice()<=upper && model.getServicePrice()>=lower) {
                                            curAmount = BookingTotalAmount - item.getDiscount();
                                            Log.d("price1",curAmount+"");
                                            break;
                                        }
                                    }
                                    else{
                                        if(model.getServicePrice()>=lower) {
                                            curAmount = BookingTotalAmount - item.getDiscount();
                                            Log.d("price2",curAmount+"");
                                            break;
                                        }
                                    }
                                }
                            }
                            else{
                                for(CartItemModel model:sidlist){
                                    if(upper!=-1){
                                        if(sidlist.get(sidlist.indexOf(model)).getId().equals(couponServiceId)){
                                            if(model.getServicePrice()<=upper && model.getServicePrice()>=lower){
                                                curAmount=BookingTotalAmount-item.getDiscount();
                                            }
                                            Log.d("price3",curAmount+"");
                                        }
                                    }
                                    else{
                                        if(sidlist.get(sidlist.indexOf(model)).getId().equals(couponServiceId)){
                                            if(model.getServicePrice()>=lower){
                                                curAmount=BookingTotalAmount-item.getDiscount();
                                            }
                                            Log.d("price4",curAmount+"");
                                        }
                                    }
                                }
                            }
                            Log.d("price",curAmount+"");
                            couponInfo.setVisibility(View.VISIBLE);
                            couponInfo.setText("You have received a discount of Rs:"+item.getDiscount());
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Could not apply coupon",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CouponItem> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            } else{
                couponcodeEditText.setError("Please enter a coupon code first");
                couponcodeEditText.requestFocus();
            }
        });
    }

    private void checkDialog () {
        CheckTermDialog checkTermDialog = new CheckTermDialog();
        checkTermDialog.show(getSupportFragmentManager(), "test");
    }

    private void sendemailconfirmation () {
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceholderApi = retrofit.create(JsonPlaceHolderApi.class);
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    email = task.getResult().get("Email Address").toString();
                    //Toast.makeText(getApplicationContext(), email +"cds",Toast.LENGTH_SHORT).show();
                    Emailer emailer = new Emailer(email, OrderSummary, finalTime + "  " + finalDate, BookingTotalAmount + "");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Toast.makeText(getApplicationContext(),"Destroyed",Toast.LENGTH_SHORT).show();
//        Retrofit retrofit1= RetrofitClientInstanceBooking.getRetrofitInstance();
//        JsonPlaceHolderApi2 jsonPlaceHolderApi21=retrofit1.create(JsonPlaceHolderApi2.class);
//        Call<Void> call1=jsonPlaceHolderApi21.revertBooking(new ServiceIdList(sidlist,barberIdRet,slotRet,0,couponName),"Bearer "+token);
//        call1.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
//                if(response.code()==200){
//                    //Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT).show();
//                }
//                else{
////                    Toast.makeText(getApplicationContext(),"Could not cancel booking",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Toast.makeText(getApplicationContext(),"On pause",Toast.LENGTH_SHORT).show();
        if(!inMap){
            //Toast.makeText(getApplicationContext(),"Destroyed",Toast.LENGTH_SHORT).show();
        Retrofit retrofit1= RetrofitClientInstanceBooking.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi21=retrofit1.create(JsonPlaceHolderApi2.class);
        Call<Void> call1=jsonPlaceHolderApi21.revertBooking(new ServiceIdList(sidlist,barberIdRet,slotRet,0,couponName),"Bearer "+token);
        call1.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if(response.code()==200){
                    //Toast.makeText(getApplicationContext(),"Cancelled",Toast.LENGTH_SHORT).show();
                }
                else{
//                    Toast.makeText(getApplicationContext(),"Could not cancel booking",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        inMap=false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(),"On resume",Toast.LENGTH_SHORT).show();
        inMap=false;
    }
    //    private void fetchRegion () {
////        ProgressDialog p =new ProgressDialog(BookingPage.this);
////        p.setMessage("Please Wait...");
////        p.show();
//        FirebaseFirestore.getInstance().collection("Users")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .get().addOnCompleteListener(task -> {
//            String[] x = new String[2];
//            try {
//                String coord = task.getResult().get("Address1").toString();
//                x = coord.split(",");
//                lat = Double.parseDouble(x[0]);
//                lon = Double.parseDouble(x[1]);
//                getRegion();
//            } catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Address fields not saved!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(BookingPage.this, ChangeLocation.class));
//            }
////                    p.dismiss();
//        });
//    }

//    private void getRegion () {
//        double radius3 = 1685.09;
//        double radius4 = 1361.44;
//        double radius5 = 2351.31;
//        double radius6 = 2080.72;
//        double radius7 = 1854.92;
//        double radius8 = 2448.73;
//        double radius9 = 1655.59;
//        double radius10 = 1399.92;
//        double radius11 = 2227.10;
//        double radius12 = 1881.67;
//
//        if (getdistanceinkm(new LatLng(26.956962, 75.77664)) * 1000 <= radius3) {
//            region = 1;
//        } else if (getdistanceinkm(new LatLng(26.939211, 75.795793)) * 1000 <= radius4) {
//            region = 2;
//        } else if (getdistanceinkm(new LatLng(26.896277, 75.783537)) * 1000 <= radius5) {
//            region = 3;
//        } else if (getdistanceinkm(new LatLng(26.858152, 75.765343)) * 1000 <= radius6) {
//            region = 4;
//        } else if (getdistanceinkm(new LatLng(26.822310, 75.769312)) * 1000 <= radius7) {
//            region = 5;
//        } else if (getdistanceinkm(new LatLng(26.823396, 75.862217)) * 1000 <= radius8) {
//            region = 6;
//        } else if (getdistanceinkm(new LatLng(26.900915, 75.829059)) * 1000 <= radius9) {
//            region = 7;
//        } else if (getdistanceinkm(new LatLng(26.880131, 75.812279)) * 1000 <= radius10) {
//            region = 8;
//        } else if (getdistanceinkm(new LatLng(26.814549, 75.820629)) * 1000 <= radius11) {
//            region = 9;
//        } else if (getdistanceinkm(new LatLng(26.850078, 75.804790)) * 1000 <= radius12) {
//            region = 10;
//        } else if (getdistanceinkm(new LatLng(26.930256, 75.875947)) * 1000 <= 8101.33 || getdistanceinkm(new LatLng(26.943649, 75.748845)) * 1000 <= 1718.21 || getdistanceinkm(new LatLng(26.949311, 75.714512)) * 1000 <= 1764.76) {
//            region = 11;
////            Toast.makeText(getApplicationContext(),"Special"+region,Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getApplicationContext(), "You are Out of Zone Please change your location!" + region, Toast.LENGTH_SHORT).show();
//        }
//
////        Toast.makeText(getApplicationContext(),"dcs"+region,Toast.LENGTH_SHORT).show();
//    }
//
//    private double getdistanceinkm (LatLng latLng){
//        double lat1 = latLng.latitude;
//        double lon1 = latLng.longitude;
//        double lat2 = lat;
//        double lon2 = lon;
//        lon1 = Math.toRadians(lon1);
//        lon2 = Math.toRadians(lon2);
//        lat1 = Math.toRadians(lat1);
//        lat2 = Math.toRadians(lat2);
//
//        // Haversine formula
//        double dlon = lon2 - lon1;
//        double dlat = lat2 - lat1;
//        double a = Math.pow(Math.sin(dlat / 2), 2)
//                + Math.cos(lat1) * Math.cos(lat2)
//                * Math.pow(Math.sin(dlon / 2), 2);
//
//        double c = 2 * Math.asin(Math.sqrt(a));
//
//        // Radius of earth in kilometers. Use 3956
//        // for miles
//        double r = 6371;
//
//        // calculate the result
//        return (c * r);
//    }

    @Override
    protected void onRestart () {
        super.onRestart();
        Intent intent=getIntent();
        finish();
        startActivity(intent);
    }

    private void addtoDatabase () {
        Map<String, Object> bookingData = new HashMap<>();
        bookingData.put("service", OrderSummary);
        bookingData.put("date", finalDate);
        bookingData.put("time", finalTime);
        bookingData.put("address", userAddress);
        bookingData.put("total_amount", BookingTotalAmount);
        bookingData.put("status", "pending");
        bookingData.put("total_time", serviceTime);
        bookingData.put("randomId", randomId);
        if(typesel==1){
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Bookings")
                    .document().set(bookingData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
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
                        if (men && !women) {
                            Map<String, Object> date = new HashMap<>();
                            if (array[1] >= 16) {
                                for (int i = array[1]; i < array[1] + slotsBooked && i < 20; i++) {
                                    date.put(i + "_m", "B");
                                }
                            } else {
                                for (int i = array[1]; i < array[1] + slotsBooked && i < 13; i++) {
                                    date.put(i + "_m", "B");
                                }
                            }
                            date.put("date", mon + " " + day + ", " + selectedYear);
                            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day" + array[0])
                                    .collection("Region").document("Region" + region).update(date)
                                    .addOnCompleteListener(task1 -> {

                                    });
                        } else if (women && !men) {
                            Map<String, Object> date = new HashMap<>();
                            for (int i = array[1]; i < array[1] + slotsBooked && i < 18; i++) {
                                date.put(i + "_f", "B");
                            }
                            date.put("date", mon + " " + day + ", " + selectedYear);
                            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day" + array[0])
                                    .collection("Region").document("Region" + region).update(date)
                                    .addOnCompleteListener(task1 -> {

                                    });
                        } else {
                            Map<String, Object> date = new HashMap<>();
                            if (array[1] >= 16) {
                                for (int i = array[1]; i < array[1] + slotsBooked && i < 18; i++) {
                                    date.put(i + "_m", "B");
                                }
                                for (int i = array[1]; i < array[1] + slotsBooked && i < 18; i++) {
                                    date.put(i + "_f", "B");
                                }
                            } else {
                                for (int i = array[1]; i < array[1] + slotsBooked && i < 13; i++) {
                                    date.put(i + "_m", "B");
                                }
                                for (int i = array[1]; i < array[1] + slotsBooked && i < 13; i++) {
                                    date.put(i + "_f", "B");
                                }
                            }
                            date.put("date", mon + " " + day + ", " + selectedYear);
                            FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day" + array[0])
                                    .collection("Region").document("Region" + region).update(date)
                                    .addOnCompleteListener(task1 -> {

                                    });
                        }
                    } else
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else if(typesel==2){
            FirebaseFirestore.getInstance().collection("Manual booking").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("Weekly booking").document().set(bookingData)
                    .addOnCompleteListener(task -> {

                    });
        }
        else{
            FirebaseFirestore.getInstance().collection("Manual booking").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("Monthly booking").document().set(bookingData)
                    .addOnCompleteListener(task -> {

                    });
        }

    }

//    private void extractDataFromUser () {
//        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            Username = task.getResult().get("Name").toString();
//                            UserPhone = task.getResult().get("Phone").toString();
//                        }
//                    }
//                });
//
//    }

    private void addTosheet () {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbynnJCsAja8_NPhqBVhc9wB2vsrw2lHRpIQIgoqCiw1_d5geLuUDzm-ibTVN1pSzrQ-oA/exec"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(),"Entered on REsponse",Toast.LENGTH_SHORT).show();
                //Toast.makeText(BookingPage.this,response,Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> parmas = new HashMap<>();
                //here we pass params
                parmas.put("action", "addItem");
                parmas.put("randomId", randomId);
                //parmas.put("userName", Username);
                parmas.put("services", OrderSummary);
                parmas.put("servicedate", finalDate);
                parmas.put("servicetime", finalTime);
                parmas.put("total", String.valueOf(BookingTotalAmount));
                parmas.put("address", userAddress);
                //parmas.put("phone", UserPhone);
                parmas.put("region", String.valueOf(region));
                parmas.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                if (isCouponApplied)
                    parmas.put("covid_warrior", "Yes");
                else
                    parmas.put("covid_warrior", "No");

                return parmas;
            }
        };
        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    private boolean checkUserData () {
        if (houseAddress.getText().toString().isEmpty()) {
            houseAddress.setError("Please Enter an Address");
            houseAddress.requestFocus();
            Toast.makeText(getApplicationContext(), "Please Choose An Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (array[0] == 100 || array[1] == 100) {
            Toast.makeText(getApplicationContext(), "Date/Time Not selected!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onStart () {
        super.onStart();
        //extractDataFromUser();
    }

//    private void addCouponUsage () {
//        HashMap<String, Object> data = new HashMap<>();
//        users.add(FirebaseAuth.getInstance().getUid());
//        data.put("CouponLimit", --limit);
//        data.put("users", FieldValue.arrayUnion(FirebaseAuth.getInstance().getUid()));
//        FirebaseFirestore.getInstance().collection("AppData").document("Coupons")
//                .update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()) {
//                    //Toast.makeText(getApplicationContext(),"Coupon Applied Successfully. Don't Revert this Booking, you will lose the couopon",Toast.LENGTH_LONG).show();
//                    //isCouponApplied=true;
//                    // progressDialog.dismiss();
//                }
//            }
//        });
//
//        if(isReferApplied) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("used", "Y");
//            FirebaseFirestore.getInstance().collection("AppData").document("Earn&Refer")
//                    .collection("EligibleCustomers")
//                    .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).update(map).addOnCompleteListener(task -> {
//
//            });
//        }
//    }

    private void useCurrentAddress () {
        SharedPreferences preferences=getSharedPreferences("Profile",MODE_PRIVATE);
        String address=preferences.getString("address",null);
        //Toast.makeText(getApplicationContext(),address,Toast.LENGTH_SHORT).show();
        houseAddress.setText(address);
    }


    private void slots (Task < DocumentSnapshot > task) {
        if (men && !women) {
            male_slots.setVisibility(View.VISIBLE);
            female_slots.setVisibility(View.INVISIBLE);
            mf_slots.setVisibility(View.INVISIBLE);
        } else if (women && !men) {
            male_slots.setVisibility(View.INVISIBLE);
            female_slots.setVisibility(View.VISIBLE);
            mf_slots.setVisibility(View.INVISIBLE);
        } else {
            male_slots.setVisibility(View.INVISIBLE);
            female_slots.setVisibility(View.INVISIBLE);
            mf_slots.setVisibility(View.VISIBLE);
        }
        if (men && !women) {
            if(task.getResult().get("7_m").toString().equals("B")) {
                slot1.setEnabled(false);
                slot1.setCardBackgroundColor(Color.GRAY);
            } else {
                slot1.setEnabled(true);
                slot1.setCardBackgroundColor(Color.BLACK);
            }
            if (task.getResult().get("8_m").toString().equals("B")) {
                slot2.setEnabled(false);
                slot2.setCardBackgroundColor(Color.GRAY);
            } else {
                slot2.setEnabled(true);
                slot2.setCardBackgroundColor(Color.BLACK);
            }
            if (task.getResult().get("9_m").toString().equals("B")) {
                slot3.setEnabled(false);
                slot3.setCardBackgroundColor(Color.GRAY);
            } else {
                slot3.setEnabled(true);
                slot3.setCardBackgroundColor(Color.BLACK);
            }
            if (task.getResult().get("10_m").toString().equals("B")) {
                slot4.setEnabled(false);
                slot4.setCardBackgroundColor(Color.GRAY);
            } else {
                slot4.setEnabled(true);
                slot4.setCardBackgroundColor(Color.BLACK);
            }
            if (task.getResult().get("11_m").toString().equals("B")) {
                slot5.setEnabled(false);
                slot5.setCardBackgroundColor(Color.GRAY);
            } else {
                slot5.setEnabled(true);
                slot5.setCardBackgroundColor(Color.BLACK);
            }
            if (task.getResult().get("12_m").toString().equals("B")) {
                slot6.setEnabled(false);
                slot6.setCardBackgroundColor(Color.GRAY);
            } else {
                slot6.setEnabled(true);
                slot6.setCardBackgroundColor(Color.BLACK);
            }
            if (task.getResult().get("16_m").toString().equals("B")) {
                slot7.setEnabled(false);
                slot7.setCardBackgroundColor(Color.GRAY);
            } else {
                slot7.setEnabled(true);
                slot7.setCardBackgroundColor(Color.BLACK);
            }
            if (task.getResult().get("17_m").toString().equals("B")) {
                slot8.setEnabled(false);
                slot8.setCardBackgroundColor(Color.GRAY);
            } else {
                slot8.setEnabled(true);
                slot8.setCardBackgroundColor(Color.BLACK);
            }
            if (task.getResult().get("18_m").toString().equals("B")) {
                slot9.setEnabled(false);
                slot9.setCardBackgroundColor(Color.GRAY);
            } else {
                slot9.setEnabled(true);
                slot9.setCardBackgroundColor(Color.BLACK);
            }
            if (task.getResult().get("19_m").toString().equals("B")) {
                slot10.setEnabled(false);
                slot10.setCardBackgroundColor(Color.GRAY);
            } else {
                slot10.setEnabled(true);
                slot10.setCardBackgroundColor(Color.BLACK);
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
            if (task.getResult().get("16_m").toString().equals("B") || task.getResult().get("16_f").toString().equals("B")) {
                slot5.setEnabled(false);
                slot5.setCardBackgroundColor(Color.GRAY);
            } else {
                slot5.setEnabled(true);
                slot5.setCardBackgroundColor(Color.BLACK);
            }
            if (task.getResult().get("17_m").toString().equals("B") || task.getResult().get("17_f").toString().equals("B")) {
                slot6.setEnabled(false);
                slot6.setCardBackgroundColor(Color.GRAY);
            } else {
                slot6.setEnabled(true);
                slot6.setCardBackgroundColor(Color.BLACK);
            }
        }
        progressDialog.dismiss();
    }

    @SuppressLint("ResourceAsColor")
    private void disableUnavialableSlots(){
        long currMilliSec = System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date(currMilliSec);
        String hourString = dateFormat.format(date);
        int hour = Integer.parseInt(hourString);

        if(hour>=6){
            slot1.setClickable(false);
            slot1.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=7){
            slot2.setClickable(false);
            slot2.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=8){
            slot3.setClickable(false);
            slot3.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=9){
            slot4.setClickable(false);
            slot4.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=10){
            slot5.setClickable(false);
            slot5.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=11){
            slot6.setClickable(false);
            slot6.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=12){
            slot7.setClickable(false);
            slot7.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=13){
            slot8.setClickable(false);
            slot8.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=14){
            slot9.setClickable(false);
            slot9.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=15){
            slot10.setClickable(false);
            slot10.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=16){
            slot11.setClickable(false);
            slot11.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=17){
            slot12.setClickable(false);
            slot12.setCardBackgroundColor(Color.GRAY);
        }
        if(hour>=18){
            slot13.setClickable(false);
            slot13.setCardBackgroundColor(Color.GRAY);
        }
    }

    @SuppressLint("ResourceAsColor")
    private void enableAvialableSlots(){
        long currMilliSec = System.currentTimeMillis();
        DateFormat dateFormat = new SimpleDateFormat("HH");
        Date date = new Date(currMilliSec);
        String hourString = dateFormat.format(date);

        int hour = Integer.parseInt(hourString);

        if(hour>=6){
            slot1.setClickable(true);
            slot1.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=7){
            slot2.setClickable(true);
            slot2.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=8){
            slot3.setClickable(true);
            slot3.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=9){
            slot4.setClickable(true);
            slot4.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=10){
            slot5.setClickable(true);
            slot5.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=11){
            slot6.setClickable(true);
            slot6.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=12){
            slot7.setClickable(true);
            slot7.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=13){
            slot8.setClickable(true);
            slot8.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=14){
            slot9.setClickable(true);
            slot9.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=15){
            slot10.setClickable(true);
            slot10.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=16){
            slot11.setClickable(true);
            slot11.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=17){
            slot12.setClickable(true);
            slot12.setCardBackgroundColor(Color.BLACK);
        }
        if(hour>=18){
            slot13.setClickable(true);
            slot13.setCardBackgroundColor(Color.BLACK);
        }
    }
}