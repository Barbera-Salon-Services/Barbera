package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener {
    private TextView chooseDate;
    private TextView date;
    private TextView chooseTime;
    private TextView time;
    //private CardView usecurrentAddress;
    //private TextView address;
    private String userAddress;//Address string to be stored in database
    public static EditText houseAddress;
    private Button ConfirmBooking;
    private TextView totalAmount;
    private TextView changeLocation;
    //private TextView or;
    private SharedPreferences sharedPreferences;
    private String OrderSummary="";
    public static String finalDate;
    public static String finalTime;
    private String Username;
    private String UserPhone;
    public static boolean isCouponApplied;
    private String bookingType="";
    private int listPosition;
    public int BookingTotalAmount;
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;
    private static EditText couponcodeEditText;
    private Button couponApply;
    private List<String> users;
    private TextView BookingOrders;
    private long limit;

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
        chooseDate=(TextView) findViewById(R.id.booking_choose_a_date);
        //address = (TextView) findViewById(R.id.address);
        date=(TextView)findViewById(R.id.booking_date_text);
        changeLocation =(TextView) findViewById(R.id.booking_choose_address);
        chooseTime=(TextView)findViewById(R.id.booking_choose_a_time);
        time=(TextView)findViewById(R.id.booking_time_text);
        //usecurrentAddress=(CardView)findViewById(R.id.currentAddress);
        houseAddress=(EditText)findViewById(R.id.booking_edit_address);
        ConfirmBooking=(Button)findViewById(R.id.booking_confirm_booking);
        totalAmount=(TextView)findViewById(R.id.booking_total_amount);
        sharedPreferences =getSharedPreferences("UserInfo",MODE_PRIVATE);
        couponcodeEditText =(EditText) findViewById(R.id.booking_couponCode_editText);
        couponApply=(Button)findViewById(R.id.booking_coupon_apply_button);
        isCouponApplied=false;
        BookingOrders=(TextView)findViewById(R.id.booking_order_summary);

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
        Calendar calendar= Calendar.getInstance();
        selectedYear=calendar.get(Calendar.YEAR);
        selectedMonth=calendar.get(Calendar.MONTH);
        selectedDay=calendar.get(Calendar.DAY_OF_MONTH);

        String currentday= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date.setText(currentday);


        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new com.barbera.barberaconsumerapp.DatePicker();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        useCurrentAddress();


        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment timePicker=new TimePicker();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });

      /*  usecurrentAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(BookingPage.this);
                progressDialog.setMessage("Fetching Current Address...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                    if(task.getResult().get("Address")!=null) {
                                        String location = task.getResult().get("Address").toString();
                                        address.setText(location);
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(),"You don't have an address stored",Toast.LENGTH_LONG).show();
                                    }

                                }
                                else
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });*/

        changeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BookingPage.this,MapSearchActivity.class));
            }
        });

        ConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkUserData()) {
                    final ProgressDialog progressDialog=new ProgressDialog(BookingPage.this);
                    progressDialog.setMessage("Hold on for a moment...");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    finalDate=date.getText().toString();
                    finalTime=time.getText().toString();
                    userAddress=houseAddress.getText().toString();
                   // Toast.makeText(getApplicationContext(),userAddress,Toast.LENGTH_SHORT).show();
                    addTosheet();
                    addtoDatabase();
                    if(isCouponApplied==true)
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
                                            BookingsActivity.bookingActivityList.clear();
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
                                            BookingsActivity.bookingActivityList.clear();
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
                        BookingsActivity.bookingActivityList.clear();
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

        couponApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                                           /* HashMap<String, Object> data=new HashMap<>();
                                            users.add(FirebaseAuth.getInstance().getUid());
                                            data.put("CouponLimit",--limit);
                                            data.put("users", FieldValue.arrayUnion(FirebaseAuth.getInstance().getUid()));
                                            FirebaseFirestore.getInstance().collection("AppData").document("Coupons")
                                                    .update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(getApplicationContext(),"Coupon Applied Successfully. Don't Revert this Booking, you will lose the couopon",Toast.LENGTH_LONG).show();
                                                        isCouponApplied=true;
                                                        progressDialog.dismiss();
                                                    }
                                                }
                                            });*/
                                        }
                                    }
                                }
                            });
                }
                else {
                    couponcodeEditText.setError("Please enter a coupon code first");
                    couponcodeEditText.requestFocus();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       /* SharedPreferences sharedPreferences =getSharedPreferences("UserInfo",MODE_PRIVATE);
        if(!sharedPreferences.getString("New_Address","").equals("")) {
            address.setText(sharedPreferences.getString("New_Address", ""));
        }*/
    }

    private void addtoDatabase() {
        Map<String,Object> bookingData=new HashMap<>();
        bookingData.put("service",OrderSummary);
        bookingData.put("date",finalDate);
        bookingData.put("time",finalTime);
        bookingData.put("address",userAddress);
        bookingData.put("total_amount",BookingTotalAmount);
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Bookings")
                .document().set(bookingData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Map<String,Object> addressMap=new HashMap<>();
                    addressMap.put("House_address",houseAddress.getText().toString());
                    FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .update(addressMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        }
                    });
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
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbzlHWNBT0F-28qDbkNKqDezI8JmPts1jQVZIj5ClOY_YvEQNxY/exec"
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
                parmas.put("action","addItem");
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
        if(date.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Choose A Date",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(time.getText().toString().isEmpty()||time.getText().toString().equals("HH:MM AM/PM")){
            Toast.makeText(getApplicationContext(),"Please Choose A Time",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(houseAddress.getText().toString().isEmpty()){
            houseAddress.setError("Please Enter an Address");
            houseAddress.requestFocus();
            Toast.makeText(getApplicationContext(),"Please Choose An Address",Toast.LENGTH_SHORT).show();
            return false;
        }
       /* else if(address.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Select Address",Toast.LENGTH_SHORT).show();
        }*/
        return true;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        selectedDay= dayOfMonth;
        selectedMonth=month;
        selectedYear=year;

            String currentday= DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
            date.setText(currentday);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        Calendar calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,selectedYear);
        calendar.set(Calendar.MONTH,selectedMonth);
        calendar.set(Calendar.DAY_OF_MONTH,selectedDay);
        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
        calendar.set(Calendar.MINUTE,minute);
       if((calendar.getTimeInMillis()>Calendar.getInstance().getTimeInMillis()&&calendar.getTimeInMillis()-Calendar.getInstance().getTimeInMillis()<3600000)
           ||(calendar.getTimeInMillis()<Calendar.getInstance().getTimeInMillis())) {
           Toast.makeText(getApplicationContext(), "Please Select a Time with 1 hour Gap", Toast.LENGTH_LONG).show();
       }
       else{
           if(hourOfDay>=19||hourOfDay<9){
               Toast.makeText(getApplicationContext(), "Sorry, Our services are only Active between 9 AM to 7 PM", Toast.LENGTH_LONG).show();
           }
           else {
               if (hourOfDay >= 12) {
                   hourOfDay = (hourOfDay >= 13 ? hourOfDay - 12 : hourOfDay);
                   time.setText(hourOfDay + ":" + minute + " PM");
               }
               else
                   time.setText(hourOfDay + ":" + minute + " AM");
           }
       }
      /* else {
           if (hourOfDay >= 12) {
               hourOfDay = (hourOfDay >= 13 ? hourOfDay - 12 : hourOfDay);
              // if (hourOfDay > 7 || (hourOfDay == 7 && minute > 0))
                 //  Toast.makeText(getApplicationContext(), "Sorry, Our services are only Active between 9 AM to 7 PM", Toast.LENGTH_LONG).show();
              // else
                   time.setText(hourOfDay + ":" + minute + " PM");
           } else {
               if (hourOfDay < 9)
                   Toast.makeText(getApplicationContext(), "Sorry, Our services are only Active between 9 AM to 7 PM", Toast.LENGTH_LONG).show();
               else
                   time.setText(hourOfDay + ":" + minute + " AM");
           }
       }*/
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
                            GeoPoint geoPoint1=task.getResult().getGeoPoint("kal_1");
                            GeoPoint geoPoint2=task.getResult().getGeoPoint("kal_2");
                            GeoPoint geoPoint=task.getResult().getGeoPoint("ag");
                            MapSearchActivity.radius=task.getResult().getDouble("ag_radius");
                            MapSearchActivity.radius1=task.getResult().getDouble("kal_1_radius");
                            MapSearchActivity.radius2=task.getResult().getDouble("kal_2_radius");
                            MapSearchActivity.center=new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());
                            MapSearchActivity.center1=new LatLng(geoPoint1.getLatitude(),geoPoint.getLongitude());
                            MapSearchActivity.center2=new LatLng(geoPoint2.getLatitude(),geoPoint.getLongitude());

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
                            if(task.getResult().get("Address")!=null) {
                                String location = task.getResult().get("Address").toString();
                                houseAddress.setText(location);
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }

}