package com.barbera.barberaconsumerapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookingActivityAdapter extends RecyclerView.Adapter<BookingActivityAdapter.BookingItemViewHolder> {
    private List<BookingModel> bookingAdapterList;
    private String UserName;
    private String UserPhone;
    private ProgressDialog progressDialog;
    private Context context;

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
       /* else if(bookingModel.getStatus().equals("started")){
            holder.start.setVisibility(View.INVISIBLE);
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
                        addtoSheet(position);
                        dropBooking(position);
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
                .collection("Bookings").document(bookingAdapterList.get(pos).getDocId()).update(user)
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
                            bookingAdapterList.remove(bookingAdapterList.get(position));
                            BookingsActivity.bookingActivityAdapter.notifyDataSetChanged();
                        }
                        else
                            Toast.makeText(context,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addtoSheet(final int position) {
        StringRequest stringRequest=new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyn906_iser-mdKH73SAln_nrIQLv7X7ZDB5CVKfr706j9n2Ypl/exec"
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
                parmas.put("action","cancelItem");
                parmas.put("userName",UserName);
                parmas.put("services",bookingAdapterList.get(position).getSummary());
                parmas.put("servicedate",bookingAdapterList.get(position).getDate());
                parmas.put("servicetime",bookingAdapterList.get(position).getTime());
                parmas.put("total",bookingAdapterList.get(position).getAmount());
                parmas.put("address",bookingAdapterList.get(position).getAddress());
                parmas.put("phone", UserPhone);

                return parmas;
            }
        };
        int socketTimeOut = 50000;// u can change this .. here it is 50 seconds
        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
}