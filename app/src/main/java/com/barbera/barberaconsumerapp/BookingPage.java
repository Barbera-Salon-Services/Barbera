package com.barbera.barberaconsumerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class BookingPage extends AppCompatActivity implements DatePickerDialog.OnDateSetListener , TimePickerDialog.OnTimeSetListener {
    private CardView chooseDate;
    private TextView date;
    private CardView chooseTime;
    private TextView time;
    private CardView usecurrentAddress;
    private TextView address;
    private String userAddress;//Address string to be stored in database
    private EditText houseAddress;
    private CardView ConfirmBooking;
    private TextView totalAmount;
    private CardView changeLocation;
    private TextView or;
    private SharedPreferences sharedPreferences;
    public static String OrderSummary="";
    public static String finalDate;
    public static String finalTime;
    private String Username;
    private String UserPhone;
    private String isCovidWarrior;
    private String bookingType="";
    private int listPosition;
    public static String BookingTotalAmount="";
    private int selectedDay;
    private int selectedMonth;
    private int selectedYear;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_page);

        Intent intent=getIntent();
        or =(TextView)findViewById(R.id.or);
        bookingType+=intent.getStringExtra("BookingType");
        listPosition=intent.getIntExtra("Position",-1);
        chooseDate=(CardView) findViewById(R.id.chooseDate);
        address = (TextView) findViewById(R.id.address);
        date=(TextView)findViewById(R.id.date);
        changeLocation =(CardView) findViewById(R.id.changeAddress);
        chooseTime=(CardView)findViewById(R.id.chooseTime);
        time=(TextView)findViewById(R.id.time);
        usecurrentAddress=(CardView)findViewById(R.id.currentAddress);
        houseAddress=(EditText)findViewById(R.id.HouseAddress);
        ConfirmBooking=(CardView)findViewById(R.id.confirmBooking);
        totalAmount=(TextView)findViewById(R.id.booking_amount);
        sharedPreferences =getSharedPreferences("UserInfo",MODE_PRIVATE);

        String addres=sharedPreferences.getString("Address","");

        if(addres.equals("NA") || addres.equals("")){
            finish();
        }
        //Toast.makeText(getApplicationContext(),bookingType,Toast.LENGTH_LONG).show();

        or.setText("OR");
        totalAmount.setText("Total Amount Rs " +BookingTotalAmount);


        chooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker=new com.barbera.barberaconsumerapp.DatePicker();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });


        chooseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment timePicker=new TimePicker();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });

        usecurrentAddress.setOnClickListener(new View.OnClickListener() {
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
        });

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
                    ConfirmBooking.setEnabled(false);
                    finalDate=date.getText().toString();
                    finalTime=time.getText().toString();
                    userAddress="Building Address: "+houseAddress.getText().toString()+" location: "+ address.getText().toString();
                    Toast.makeText(getApplicationContext(),userAddress,Toast.LENGTH_SHORT).show();
                    addTosheet();
                    addtoDatabase();
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
                                            startActivity(new Intent(BookingPage.this, CongratulationsPage.class));
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
                                            startActivity(new Intent(BookingPage.this, CongratulationsPage.class));
                                            finish();
                                        }
                                    }
                                });
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("New_Address","");
                    editor.commit();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences sharedPreferences =getSharedPreferences("UserInfo",MODE_PRIVATE);
        if(!sharedPreferences.getString("New_Address","").equals("")) {
            address.setText(sharedPreferences.getString("New_Address", ""));
        }
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
                parmas.put("total",BookingTotalAmount);
                parmas.put("address",userAddress);
                parmas.put("phone", UserPhone);
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
        else if(time.getText().toString().isEmpty()){
            Toast.makeText(getApplicationContext(),"Please Choose A Time",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(houseAddress.getText().toString().isEmpty()){
            houseAddress.setError("Please Enter an Address");
            houseAddress.requestFocus();
            Toast.makeText(getApplicationContext(),"Please Choose An Address",Toast.LENGTH_SHORT).show();
            return false;
        }else if(address.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Select Address",Toast.LENGTH_SHORT).show();
        }
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
        if(bookingType.equals("Cart")) {
            OrderSummary="";
            for (int i = 0; i < dbQueries.cartItemModelList.size(); i++) {
                OrderSummary += "(" + dbQueries.cartItemModelList.get(i).getType() + ")" + dbQueries.cartItemModelList.get(i).getServiceName()
                        + "(" + dbQueries.cartItemModelList.get(i).getQuantity() + ")" + "\t\t\t\t" + "Rs" + dbQueries.cartItemModelList.get(i).getServicePrice() + "\n";
            }
        }

        extractDataFromUser();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BookingTotalAmount="";

    }
}