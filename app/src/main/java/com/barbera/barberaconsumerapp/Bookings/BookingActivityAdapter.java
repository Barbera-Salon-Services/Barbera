 package com.barbera.barberaconsumerapp.Bookings;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.barbera.barberaconsumerapp.BarberDetailDialog;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Rating;
import com.barbera.barberaconsumerapp.Utils.InstItem;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceBooking;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceUser;
import com.barbera.barberaconsumerapp.network_email.Emailer;
import com.barbera.barberaconsumerapp.network_email.JsonPlaceHolderApi;
import com.barbera.barberaconsumerapp.network_email.RetrofitClientInstance;
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
import retrofit2.Response;
import retrofit2.Retrofit;

public class BookingActivityAdapter extends RecyclerView.Adapter<BookingActivityAdapter.BookingItemViewHolder> {
    private List<BookingModel> bookingAdapterList;
    private String token;
    private String UserPhone;
    private ProgressDialog progressDialog;
    private Context context;
    private static int sotp;
    private int eotp;
    private int region;
    private double lat,lon;
    private FragmentManager fragmentManager;
    private boolean men=false,women=false;
    private JsonPlaceHolderApi2 jsonPlaceHolderApi2;

    public BookingActivityAdapter(List<BookingModel> bookingAdapterList, Context context, FragmentManager fragmentManager) {
        this.bookingAdapterList = bookingAdapterList;
        this.context = context;
        this.fragmentManager = fragmentManager;
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
        holder.dateTime.setText(bookingModel.getDate()+"\n"+bookingModel.getTime()+":00");

        //extractNameAndContact(holder);

//        if(bookingModel.getStatus().equals("done")){
//            holder.start.setVisibility(View.INVISIBLE);
//            holder.end.setVisibility(View.INVISIBLE);
//            holder.otp.setVisibility(View.INVISIBLE);
//            holder.cancelBooking.setVisibility(View.INVISIBLE);
//            holder.status.setVisibility(View.VISIBLE);
//        }
//        if(bookingModel.getStatus().equals("ongoing")){
//            holder.start.setVisibility(View.INVISIBLE);
//            holder.end.setVisibility(View.VISIBLE);
//            holder.cancelBooking.setVisibility(View.INVISIBLE);
//            holder.otp.setVisibility(View.VISIBLE);
//            holder.otp.setText("Start Otp:"+sotp);
//        }
//        if(bookingModel.getStatus().equals("pending")){
//            holder.start.setVisibility(View.VISIBLE);
//            holder.end.setVisibility(View.INVISIBLE);
//            holder.otp.setVisibility(View.INVISIBLE);
//            holder.cancelBooking.setVisibility(View.VISIBLE);
//            holder.status.setVisibility(View.INVISIBLE);
//        }

//        holder.barber.setOnClickListener(v -> { fetchAndShowContact(bookingModel.getDocId());});
        holder.start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setMessage("generating otp...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                Retrofit retrofit = RetrofitClientInstanceUser.getRetrofitInstance();
                jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
                SharedPreferences preferences = context.getSharedPreferences("Token",context.MODE_PRIVATE);
                token = preferences.getString("token", "no");
                Call<Void> call=jsonPlaceHolderApi2.startOtp(new InstItem(bookingModel.getBarberId(),0,null,false),"Bearer "+token);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            final Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences sharedPreferences=context.getSharedPreferences("Notification",context.MODE_PRIVATE);
                                    holder.otp.setText("Start otp: "+sharedPreferences.getString("notif",""));
                                    holder.end.setVisibility(View.VISIBLE);
                                    holder.start.setVisibility(View.INVISIBLE);
                                    holder.cancelBooking.setVisibility(View.INVISIBLE);
                                }
                            };
                            final Handler h = new Handler();
                            h.removeCallbacks(runnable);
                            h.postDelayed(runnable, 2000);
                            progressDialog.dismiss();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(context,"Could not get otp",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        holder.end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setMessage("generating otp...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                Call<Void> call=jsonPlaceHolderApi2.endOtp(new InstItem(bookingModel.getBarberId(),0,null,false),"Bearer "+token);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            final Runnable runnable = new Runnable() {
                                @Override
                                public void run() {
                                    SharedPreferences sharedPreferences=context.getSharedPreferences("Notification",context.MODE_PRIVATE);
                                    holder.otp.setText("End otp: "+sharedPreferences.getString("notif",""));
                                    holder.end.setVisibility(View.INVISIBLE);
                                    holder.start.setVisibility(View.INVISIBLE);
                                    holder.cancelBooking.setVisibility(View.INVISIBLE);
                                }
                            };
                            final Handler h = new Handler();
                            h.removeCallbacks(runnable);
                            h.postDelayed(runnable, 2000);
                            progressDialog.dismiss();
                        }
                        else{
                            progressDialog.dismiss();
                            Toast.makeText(context,"Could not get otp",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.cancelBooking.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
            builder.setMessage("Really!!You Want to Cancel..");
            builder.setPositiveButton("YES", (dialog, which) -> {
                progressDialog = new ProgressDialog(v.getContext());
                progressDialog.setMessage("Hold On for a moment...");
                progressDialog.show();
                progressDialog.setCancelable(false);
                //fetchRegion(position);
                //sendEmailCancelationMail(position);
            });
            builder.setNegativeButton("NO", (dialog, which) -> {

            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });

    }

    private void fetchAndShowContact(String docId) {
        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Bookings")
                .document(docId).get().addOnCompleteListener(task -> {
                    try {
                        String barberId = task.getResult().get("assignedTo").toString();
                        FirebaseFirestore.getInstance().collection("Service").document(barberId).get()
                                .addOnCompleteListener(task1 -> {
                                    String name = task1.getResult().get("name").toString();
                                    String phone = task1.getResult().get("phone").toString();
                                    BarberDetailDialog bb = new BarberDetailDialog(name, phone);
                                    bb.show(fragmentManager,"true");
                                    bb.setCancelable(true);

                                });
                    }catch (Exception e ){
                        Toast.makeText(context,"Barber Not asigned",Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return  bookingAdapterList.size();
    }

    public static class BookingItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTime;
        private final TextView totalAmount;
        private final TextView serviceSummary;
        private final Button end;
        private final Button cancelBooking;
        private final Button start;
        private TextView barber;
        private final TextView status;
        private final TextView otp;
        public BookingItemViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceSummary= itemView.findViewById(R.id.booking_service_title);
            totalAmount=itemView.findViewById(R.id.booking_service_total);
            dateTime=itemView.findViewById(R.id.booking_date_time);
            cancelBooking=itemView.findViewById(R.id.cancel_button);
            start = itemView.findViewById(R.id.startOtp);
            end = itemView.findViewById(R.id.endtOtp);
            status =itemView.findViewById(R.id.status);
            barber = itemView.findViewById(R.id.barberDetails);
            otp = itemView.findViewById(R.id.otp);
        }
    }



//    private void updateStatus(int pos,String status) {
//        Map<String,Object> user=new HashMap<>();
//        user.put("status",status);
//        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid())
//                .collection("Bookings").document( bookingAdapterList.get(pos).getDocId()).update(user)
//                .addOnCompleteListener(task -> {
//                    bookingAdapterList.get(pos).setStatus(status);
//                    BookingsActivity.bookingActivityAdapter.notifyDataSetChanged();
//
//                });
//    }
//
//    private void rateService(int pos) {
//        context.startActivity(new Intent(context, Rating.class)
//                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                .putExtra("docId",bookingAdapterList.get(pos).getDocId()));
//    }
//

//    private void extractNameAndContact(BookingItemViewHolder holder) {
//        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getUid()).get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        UserName=task.getResult().get("Name").toString();
//                        UserPhone=task.getResult().get("Phone").toString();
//                        try{
//                            sotp = Integer.parseInt(task.getResult().get("startOtp").toString());
//                            eotp = Integer.parseInt(task.getResult().get("endOtp").toString());
//                        }catch (Exception ignored){
//
//                        }
//                        holder.otp.setText("Start OTP:"+sotp);
//                    }
//                });
//    }
//
//    private void dropBooking(final int position) {
//        Map<String,Object> CancelData=new HashMap<>();
//        CancelData.put("status","Cancelled");
//        FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Bookings")
//                .document(bookingAdapterList.get(position).getDocId()).delete().
//                addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            removeBookings(position);
//                            bookingAdapterList.remove(bookingAdapterList.get(position));
//                            BookingsActivity.bookingActivityAdapter.notifyDataSetChanged();
//
//                        }
//                        else
//                            Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//    private void removeBookings(int position){
//        Calendar calendar = Calendar.getInstance();
//        int curDay = calendar.get(Calendar.DAY_OF_MONTH);
//        String date = bookingAdapterList.get(position).getDate();
//        int totalTime =Integer.parseInt(bookingAdapterList.get(position).getTotalTime());
//        if(totalTime%60==0){
//            totalTime=totalTime/60;
//        }
//        else{
//            totalTime=(totalTime/60)+1;
//        }
//        int slot =Integer.parseInt(bookingAdapterList.get(position).getTime().substring(0,2));
//        Log.d("slot no", String.valueOf(slot));
//        Log.d("Total time", String.valueOf(totalTime));
//
//        String summary = bookingAdapterList.get(position).getSummary();
//        Log.d("summary",summary);
//        String [] lines = summary.split("\n");
//        for (String line : lines) {
//            String sub=line.substring(line.indexOf("(") + 1, line.indexOf(")"));
//            Log.d("sub",sub);
//            if (sub.equals("men")) {
//                men = true;
//            }
//            if (sub.equals("women")) {
//                women = true;
//            }
//        }
//        Log.d("men,women", String.valueOf(men)+" "+ String.valueOf(women));
//        String dateNo = date.substring(4,6);
//        int day;
//        if(dateNo.charAt(1) != ','){
//           day = Integer.parseInt(dateNo) - curDay+1;
//        }else{
//            day = Integer.parseInt(dateNo.charAt(0)+"") - curDay+1;
//        }
//        Log.d("day",String.valueOf(day));
//        Map<String,Object> map = new HashMap<>();
//        if(men && !women){
//            if(slot>=16){
//                for(int i=slot;i<slot+totalTime && i<20;i++){
//                    map.put(i + "_m","NB");
//                }
//            }
//            else{
//                for(int i=slot;i<slot+totalTime && i<13;i++){
//                    map.put(i + "_m","NB");
//                }
//            }
//        }
//        else if(women && !men){
//            for(int i=slot;i<slot+totalTime && i<18;i++){
//                map.put(i + "_f","NB");
//            }
//        }
//        else{
//            if(slot>=16){
//                for(int i=slot;i<slot+totalTime && i<18;i++){
//                    map.put(i + "_m","NB");
//                }
//            }
//            else{
//                for(int i=slot;i<slot+totalTime && i<13;i++){
//                    map.put(i + "_m","NB");
//                }
//            }
//        }
//        //Toast.makeText(context,"MAP"+map.get("11_m")+"day:"+day+"region: "+region,Toast.LENGTH_LONG).show();
//        FirebaseFirestore.getInstance().collection("DaytoDayBooking").document("Day" + day)
//                .collection("Region").document("Region" + region).update(map)
//                .addOnCompleteListener(task1 ->  {
//                    if(task1.isSuccessful()){
//                        Toast.makeText(context,"Success",Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void addtoSheet(final int position) {
//        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbynnJCsAja8_NPhqBVhc9wB2vsrw2lHRpIQIgoqCiw1_d5geLuUDzm-ibTVN1pSzrQ-oA/exec"
//                , new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Toast.makeText(context,"Booking Cancelled",Toast.LENGTH_LONG).show();
//                progressDialog.dismiss();
//            }
//        },  new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show();
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> parmas = new HashMap<>();
//                //here we pass params
//                    parmas.put("action","cancelItem");
//                    parmas.put("region",String.valueOf(region));
//                //Log.d("region", String.valueOf(region));
//                    parmas.put("randomId",bookingAdapterList.get(position).getRandomId());
//                //Log.d("id",bookingAdapterList.get(position).getRandomId());
////                parmas.put("userName",UserName);
////                parmas.put("services",bookingAdapterList.get(position).getSummary());
////                parmas.put("servicedate",bookingAdapterList.get(position).getDate());
////                parmas.put("servicetime",bookingAdapterList.get(position).getTime());
////                parmas.put("total",bookingAdapterList.get(position).getAmount());
////                parmas.put("address",bookingAdapterList.get(position).getAddress());
////                parmas.put("phone", UserPhone);
//
//                return parmas;
//            }
//        };
//        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds
//        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(retryPolicy);
//        RequestQueue queue = Volley.newRequestQueue(context);
//        queue.add(stringRequest);
//    }
//    private void fetchRegion(int position) {
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
//                addtoSheet(position);
//                String summary=bookingAdapterList.get(position).getSummary();
//                String end=summary.substring(summary.length()-4);
//                String last=end.substring(end.length()-1);
//                String mid=end.substring(0,end.length()-1);
//                if(last.equals(")")){
//                    if(mid.equals("kly")){
//                        dropweekly(position);
//                    }
//                    else{
//                        dropmonthly(position);
//                    }
//                }
//                else{
//                    dropBooking(position);
//                }
//
//            }catch(Exception e) {
//            }
//        });
//    }
//    private void dropweekly(int position){
//        FirebaseFirestore.getInstance().collection("Manual booking").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Weekly booking")
//                .document(bookingAdapterList.get(position).getDocId()).delete().
//                addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            removeBookings(position);
//                            bookingAdapterList.remove(bookingAdapterList.get(position));
//                            BookingsActivity.bookingActivityAdapter.notifyDataSetChanged();
//
//                        }
//                        else
//                            Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void dropmonthly(int position){
//        FirebaseFirestore.getInstance().collection("Manual booking").document(FirebaseAuth.getInstance().getCurrentUser().getUid()).collection("Monthly booking")
//                .document(bookingAdapterList.get(position).getDocId()).delete().
//                addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if(task.isSuccessful()){
//                            removeBookings(position);
//                            bookingAdapterList.remove(bookingAdapterList.get(position));
//                            BookingsActivity.bookingActivityAdapter.notifyDataSetChanged();
//
//                        }
//                        else
//                            Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//
//    private void getRegion() {
//        double radius3 =1685.09;
//        double radius4 =1361.44;
//        double radius5 =2351.31;
//        double radius6 =2080.72;
//        double radius7 =1854.92;
//        double radius8 =2448.73;
//        double radius9 =1655.59;
//        double radius10 =1399.92;
//        double radius11=2227.10;
//        double radius12 =1881.67;
//
//        if(getdistanceinkm(new LatLng(26.956962,75.77664))*1000<=radius3){
//            region =1 ;
//        }
//        else if(getdistanceinkm(new LatLng(26.939211,75.795793))*1000<=radius4){
//            region =2 ;
//        }
//        else if(getdistanceinkm(new LatLng(26.896277,75.783537))*1000<=radius5){
//            region =3 ;
//        }
//        else if(getdistanceinkm(new LatLng(26.858152,75.765343))*1000<=radius6){
//            region =4 ;
//        }
//        else if(getdistanceinkm(new LatLng(26.822310,75.769312))*1000<=radius7){
//            region =5 ;
//        }
//        else if(getdistanceinkm(new LatLng(26.823396,75.862217))*1000<=radius8){
//            region =6 ;
//        }
//        else if(getdistanceinkm(new LatLng(26.900915,75.829059))*1000<=radius9){
//            region =7 ;
//        }
//        else if(getdistanceinkm(new LatLng(26.880131,75.812279))*1000<=radius10){
//            region =8 ;
//        }
//        else if(getdistanceinkm(new LatLng(26.814549,75.820629))*1000<=radius11){
//            region =9 ;
//        }
//        else if(getdistanceinkm(new LatLng(26.850078,75.804790))*1000<=radius12){
//            region =10 ;
//        }else if(getdistanceinkm(new LatLng(26.930256,75.875947))*1000<=8101.33 || getdistanceinkm(new LatLng(26.943649,75.748845))*1000<=1718.21 || getdistanceinkm(new LatLng(26.949311,75.714512 ))*1000<=1764.76 ){
//            region = 11;
////            Toast.makeText(getApplicationContext(),"Special"+region,Toast.LENGTH_SHORT).show();
//        }
////        Toast.makeText(getApplicationContext(),"dcs"+region,Toast.LENGTH_SHORT).show();
//    }
//    private double getdistanceinkm( LatLng latLng) {
//        double lat1= latLng.latitude;
//        double lon1= latLng.longitude;
//        double lat2= lat;
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
//                * Math.pow(Math.sin(dlon / 2),2);
//
//        double c = 2 * Math.asin(Math.sqrt(a));
//
//        // Radius of earth in kilometers. Use 3956
//        // for miles
//        double r = 6371;
//
//        // calculate the result
//        return(c * r);
//    }
//    private void sendEmailCancelationMail(int position){
//        Retrofit retrofit = RetrofitClientInstance.getRetrofitInstance();
//        JsonPlaceHolderApi jsonPlaceholderApi =retrofit.create(JsonPlaceHolderApi.class);
//        FirebaseFirestore.getInstance().collection("Users")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .get()
//                .addOnCompleteListener(task -> {
//                    String email = task.getResult().get("Email Address").toString();
//                    //Toast.makeText(getApplicationContext(), email +"cds",Toast.LENGTH_SHORT).show();
//                    Emailer emailer = new Emailer(email,bookingAdapterList.get(position).getSummary(),bookingAdapterList.get(position).getTime()+"  "+
//                            bookingAdapterList.get(position).getDate(),bookingAdapterList.get(position).getAmount());
//                    Call<Emailer> call = jsonPlaceholderApi.cancelEmail(emailer);
//                    call.enqueue(new Callback<Emailer>() {
//                        @Override
//                        public void onResponse(Call<Emailer> call, retrofit2.Response<Emailer> response) {
//
//                        }
//
//                        @Override
//                        public void onFailure(Call<Emailer> call, Throwable t) {
//
//                        }
//                    });
//                });
//    }
}