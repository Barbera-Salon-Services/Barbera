package com.barbera.barberaconsumerapp.Bookings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.CongratulationsPage;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceBooking;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class BarberAdapter extends RecyclerView.Adapter<BarberAdapter.BarberItemViewHolder> {
    private List<BarberItem> barberItems=new ArrayList<>();
    private Context context;
    private String date,amount,summary;
    private int slot;
    private ArrayList<String> sidlist=new ArrayList<>();

    public BarberAdapter(List<BarberItem> list, Context context, String dat, int slot, String amount, String summary, ArrayList<String> sidlist) {
        this.barberItems=list;
        this.context=context;
        this.date=dat;
        this.slot=slot;
        this.amount=amount;
        this.summary=summary;
        this.sidlist=sidlist;
    }

    @NotNull
    @Override
    public BarberAdapter.BarberItemViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.barber_item,parent,false);

        return new BarberAdapter.BarberItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull BarberAdapter.BarberItemViewHolder holder, int position) {
        BarberItem item=barberItems.get(position);
        holder.distance.setText(item.getDistance());
        holder.address.setText(item.getAddress());
        holder.phone.setText(item.getPhone());

        Retrofit retrofit = RetrofitClientInstanceBooking.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2=retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = context.getSharedPreferences("Token", context.MODE_PRIVATE);
        String token = preferences.getString("token", "no");

        holder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Call<Void> call= jsonPlaceHolderApi2.bookBarber(new ServiceIdList(sidlist,item.getBarberid()),date,slot,"Bearer "+token);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            Intent intent1 = new Intent(context, CongratulationsPage.class);
                            intent1.putExtra("Booking Amount", amount);
                            intent1.putExtra("Order Summary", summary);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent1);
                        }
                        else{
                            Toast.makeText(context,"Could not book barber",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    @Override
    public int getItemCount() {
        return barberItems.size();
    }
    public static class BarberItemViewHolder extends RecyclerView.ViewHolder{
        private TextView phone,address,distance;
        private Button book;

        public BarberItemViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            phone=itemView.findViewById(R.id.barber_phone);
            address=itemView.findViewById(R.id.barber_add);
            distance=itemView.findViewById(R.id.barber_distance);
            book=itemView.findViewById(R.id.barber_book);
        }
    }
}
