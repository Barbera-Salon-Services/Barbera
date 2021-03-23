package com.barbera.barberaconsumerapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class BookingActivityAdapter extends RecyclerView.Adapter<BookingActivityAdapter.BookingItemViewHolder> {
    private List<BookingModel> bookingAdapterList;
    private String UserName;
    private String UserPhone;
    private ProgressDialog progressDialog;
    private Context context;
    private int region;
    private double lat,lon;
    private boolean men=false,women=false;

    public BookingActivityAdapter(List<BookingModel> bookingAdapterList, Context context) {
        this.bookingAdapterList = bookingAdapterList;
        this.context = context;
    }



    @NonNull
    @Override
    public BookingActivityAdapter.BookingItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.new_booking_fragement,parent,false);
        return new BookingItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingActivityAdapter.BookingItemViewHolder holder, int position) {
        BookingModel bookingModel = bookingAdapterList.get(position);

        holder.serviceSummary.setText(bookingModel.getSummary());
        holder.totalAmount.setText("Total Amount Rs "+bookingModel.getAmount());
        holder.dateTime.setText(bookingModel.getDate()+"\n"+bookingModel.getTime());

        extractNameAndContact();

        if(bookingModel.getStatus().equals("done")){
            holder.start.setVisibility(View.INVISIBLE);
            holder.end.setVisibility(View.INVISIBLE);
            holder.cancelBooking.setVisibility(View.INVISIBLE);
            holder.status.setVisibility(View.VISIBLE);
        }
        if(bookingModel.getStatus().equals("ongoing")){
            holder.start.setVisibility(View.INVISIBLE);
            holder.end.setVisibility(View.VISIBLE);
            holder.cancelBooking.setVisibility(View.INVISIBLE);
        }
        if(bookingModel.getStatus().equals("pending")){
            holder.start.setVisibility(View.VISIBLE);
            holder.end.setVisibility(View.VISIBLE);
            holder.cancelBooking.setVisibility(View.INVISIBLE);
            holder.status.setVisibility(View.INVISIBLE);
        }
       /* else if(bookingModel.getStatus().equals("")){
            holder.start.setVisibility(View.INVISIBLE);
            holder.end.setVisibility(View.VISIBLE);
            holder.cancelBooking.setVisibility(View.INVISIBLE);
            holder.status.setVisibility(View.INVISIBLE);
        }*/


        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateStartOtp(v,holder,position);
            }
        });
        holder.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateEndOtp(v,position,holder);
            }
        });

        holder.cancelBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Really!!You Want to Cancel..");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        progressDialog = new ProgressDialog(v.getContext());
                        progressDialog.setMessage("Hold On for a moment...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        fetchRegion(position);
                        sendEmailCancelationMail(position);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return  bookingAdapterList.size();
    }

    public class BookingItemViewHolder extends RecyclerView.ViewHolder {
        private TextView dateTime;
        private TextView totalAmount;
        private TextView serviceSummary;
        private Button end;
        private Button cancelBooking;
        private Button start;
        private TextView status;
        public BookingItemViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceSummary= itemView.findViewById(R.id.booking_service_title);
            totalAmount=itemView.findViewById(R.id.booking_service_total);
            dateTime=itemView.findViewById(R.id.booking_date_time);
            cancelBooking=itemView.findViewById(R.id.cancel_button);
            start = itemView.findViewById(R.id.startOtp);
            end = itemView.findViewById(R.id.endtOtp);
            status =itemView.findViewById(R.id.status);
        }
    }


    private void generateEndOtp(View v,int pos, BookingItemViewHolder holder) {
        final int otp = (int)(Math.random()*9000)+1000;
        Map<String,Object> user=new HashMap<>();
        user.put("endOtp",otp);
        progressDialog = new ProgressDialog(v.getContext());
        progressDialog.setMessage("generating otp...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).update(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setTitle("End Otp is "+otp);
                        builder.setMessage("Please use this otp to end service");
                        builder.setCancelable(true);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.end.setVisibility(View.INVISIBLE);
                                holder.start.setVisibility(View.INVISIBLE);
                                holder.cancelBooking.setVisibility(View.INVISIBLE);
                                updateStatus(pos,"done");
                                rateService(pos);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
    }

    private void updateStatus(int pos,String status) {
        Map<String,Object> user=new HashMap<>();
        user.put("status",status);
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid())
                .collection("Bookings").document( bookingAdapterList.get(pos).getDocId()).update(user)
                .addOnCompleteListener(task -> {
                    bookingAdapterList.get(pos).setStatus(status);
                    BookingsActivity.bookingActivityAdapter.notifyDataSetChanged();

                });
    }

    private void rateService(int pos) {
        context.startActivity(new Intent(context,Rating.class)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .putExtra("docId",bookingAdapterList.get(pos).getDocId()));
    }

    private void generateStartOtp(View view, BookingItemViewHolder holder,int pos) {
        final int otp = (int)(Math.random()*9000)+1000;
        Map<String,Object> user=new HashMap<>();
        user.put("startOtp",otp);
        progressDialog = new ProgressDialog(view.getContext());
        progressDialog.setMessage("generating otp...");
        progressDialog.show();
        progressDialog.setCancelable(false);
        updateStatus(pos,"ongoing");
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).update(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Start Otp is "+otp);
                        builder.setMessage("Please use this otp to start service");
                        builder.setCancelable(true);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                holder.end.setVisibility(View.VISIBLE);
                                holder.start.setVisibility(View.INVISIBLE);
                                holder.cancelBooking.setVisibility(View.INVISIBLE);
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
    }

    private void extractNameAndContact() {
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        UserName=task.getResult().get("Name").toString();
                        UserPhone=task.getResult().get("Phone").toString();
                    }
                });
    }

    private void dropBooking(final int position) {
        Map<String,Object> CancelData=new HashMap<>();
        CancelData.put("status","Cancelled");
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Bookings")
                .document(bookingAdapterList.get(position).getDocId()).delete().
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            removeBookings(position);
                            bookingAdapterList.remove(bookingAdapterList.get(position));
                            BookingsActivity.bookingActivityAdapter.notifyDataSetChanged();

                        }
                        else
                            Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });

    }
    private void removeBookings(int position){
        Calendar calendar = Calendar.getInstance();
        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
        String date = bookingAdapterList.get(position).getDate();
        int totalTime =Integer.parseInt(bookingAdapterList.get(position).getTotalTime());
        if(totalTime%60==0){
            totalTime=totalTime/60;
        }
        else{
            totalTime=(totalTime/60)+1;
        }
        int slot =Integer.parseInt(bookingAdapterList.get(position).getTime().substring(0,2));
        Log.d("slot no", String.valueOf(slot));
        Log.d("Total time", String.valueOf(totalTime));

        String summary = bookingAdapterList.get(position).getSummary();
        Log.d("summary",summary);
        String [] lines = summary.split("\n");
        for (String line : lines) {
            String sub=line.substring(line.indexOf("(") + 1, line.indexOf(")"));
            Log.d("sub",sub);
            if (sub.equals("men")) {
                men = true;
            }
            if (sub.equals("women")) {
                women = true;
            }
        }
        Log.d("men,women", String.valueOf(men)+" "+ String.valueOf(women));
        String dateNo = date.substring(4,6);
        int day = Integer.parseInt(dateNo) - curDay+1;
        Log.d("day",String.valueOf(day));
        Map<String,Object> map = new HashMap<>();
        if(men && !women){
            if(slot>=16){
                for(int i=slot;i<slot+totalTime && i<20;i++){
                    map.put(i + "_m","NB");
                }
            }
            else{
                for(int i=slot;i<slot+totalTime && i<13;i++){
                    map.put(i + "_m","NB");
                }
            }
        }
        else if(women && !men){
            for(int i=slot;i<slot+totalTime && i<18;i++){
                map.put(i + "_f","NB");
            }
        }
        else{
            if(slot>=16){
                for(int i=slot;i<slot+totalTime && i<18;i++){
                    map.put(i + "_m","NB");
                }
            }
            else{
                for(int i=slot;i<slot+totalTime && i<13;i++){
                    map.put(i + "_m","NB");
                }
            }
        }
        //Toast.makeText(context,"MAP"+map.get("11_m")+"day:"+day+"region: "+region,Toast.LENGTH_LONG).show();
        FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day" + day)
                .collection("Region").document("Region" + region).update(map)
                .addOnCompleteListener(task1 ->  {
                    if(task1.isSuccessful()){
                        Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addtoSheet(final int position) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbwtLJ3Ts_ObuVoM0iuPOj1aOvf2wIy0E0Q6J56VtdMExqUWPvn4jAOURwxfHHE8zJIUIA/exec"
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(context,"Booking Cancelled",Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        },  new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parmas = new HashMap<>();
                //here we pass params

                if(region==1){
                    parmas.put("action","cancelItem1");
                }
                else{
                    parmas.put("action","cancelItem2");
                }
                Log.d("region", String.valueOf(region));
                parmas.put("randomId",bookingAdapterList.get(position).getRandomId());
                Log.d("id",bookingAdapterList.get(position).getRandomId());
//                parmas.put("userName",UserName);
//                parmas.put("services",bookingAdapterList.get(position).getSummary());
//                parmas.put("servicedate",bookingAdapterList.get(position).getDate());
//                parmas.put("servicetime",bookingAdapterList.get(position).getTime());
//                parmas.put("total",bookingAdapterList.get(position).getAmount());
//                parmas.put("address",bookingAdapterList.get(position).getAddress());
//                parmas.put("phone", UserPhone);

                return parmas;
            }
        };
        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
    private void fetchRegion(int position) {
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
                addtoSheet(position);
                dropBooking(position);
            }catch(Exception e) {
            }
        });
    }

    private void getRegion() {
        double radius3 =1685.09;
        double radius4 =1361.44;
        double radius5 =2351.31;
        double radius6 =2080.72;
        double radius7 =1854.92;
        double radius8 =2448.73;
        double radius9 =1655.59;
        double radius10 =1399.92;
        double radius11=2227.10;
        double radius12 =1881.67;

        if(getdistanceinkm(new LatLng(26.956962,75.77664))*1000<=radius3){
            region =1 ;
        }
        if(getdistanceinkm(new LatLng(26.939211,75.795793))*1000<=radius4){
            region =2 ;
        }
        if(getdistanceinkm(new LatLng(26.896277,75.783537))*1000<=radius5){
            region =3 ;
        }
        if(getdistanceinkm(new LatLng(26.858152,75.765343))*1000<=radius6){
            region =4 ;
        }
        if(getdistanceinkm(new LatLng(26.822310,75.769312))*1000<=radius7){
            region =5 ;
        }
        if(getdistanceinkm(new LatLng(26.823396,75.862217))*1000<=radius8){
            region =6 ;
        }
        if(getdistanceinkm(new LatLng(26.900915,75.829059))*1000<=radius9){
            region =7 ;
        }
        if(getdistanceinkm(new LatLng(26.880131,75.812279))*1000<=radius10){
            region =8 ;
        }
        if(getdistanceinkm(new LatLng(26.814549,75.820629))*1000<=radius11){
            region =9 ;
        }
        if(getdistanceinkm(new LatLng(26.850078,75.804790))*1000<=radius12){
            region =10 ;
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
    private void sendEmailCancelationMail(int position){
        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
        JsonPlaceHolderApi jsonPlaceholderApi =retrofit.create(JsonPlaceHolderApi.class);
        FirebaseFirestore.getInstance().collection("Users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    String email = task.getResult().get("Email Address").toString();
                    //Toast.makeText(getApplicationContext(), email +"cds",Toast.LENGTH_SHORT).show();
                    Emailer emailer = new Emailer(email,bookingAdapterList.get(position).getSummary());
                    Call<Emailer> call = jsonPlaceholderApi.cancelEmail(emailer);
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
}