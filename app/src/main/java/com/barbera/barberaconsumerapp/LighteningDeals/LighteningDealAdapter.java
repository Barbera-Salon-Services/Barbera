package com.barbera.barberaconsumerapp.LighteningDeals;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.BookingActivityAdapter;
import com.barbera.barberaconsumerapp.BookingPage;
import com.barbera.barberaconsumerapp.MenHorizontalAdapter;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Service;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import static java.lang.Integer.parseInt;

public class LighteningDealAdapter extends RecyclerView.Adapter<LighteningDealAdapter.DealItemHolder> {
    private List<LightenDealItem> list;
    private Context activity;
    private int flag;

    public LighteningDealAdapter(List<LightenDealItem> list, Context activity, int flag) {
        this.list = list;
        this.activity = activity;
        this.flag = flag;
    }


    @NonNull
    @Override
    public DealItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item,parent,false);
        return new DealItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DealItemHolder holder, int position) {
        LightenDealItem lightenDealItem = list.get(position);
        holder.name.setText(lightenDealItem.getTitle());
        holder.time.setText("Duration: "+lightenDealItem.getTime());
        holder.price.setText("Original Price: "+lightenDealItem.getPrice());
        holder.discount.setText("Discounted Price: "+(lightenDealItem.getPrice()- lightenDealItem.getDiscount()));

        Glide.with(activity).load(lightenDealItem.getImage_url())
                .apply(new RequestOptions().placeholder(R.drawable.logo)).into(holder.img);

        String ordersummary;
        if(flag==0){
            ordersummary="(men)"+lightenDealItem.getTitle()+"  Rs"+lightenDealItem.getPrice();
        }
        else{
            ordersummary="(women)"+lightenDealItem.getTitle()+"  Rs"+lightenDealItem.getPrice();
        }
        holder.book.setOnClickListener(v -> {
            Intent intent=new Intent(activity, BookingPage.class);
            intent.putExtra("BookingType","Cart");
            intent.putExtra("Booking Amount",lightenDealItem.getPrice()- lightenDealItem.getDiscount());
            intent.putExtra("Order Summary",ordersummary);
            intent.putExtra("Time",lightenDealItem.getTime());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent );
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class DealItemHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView time;
        private ImageView img;
        private TextView price;
        private TextView discount;
        private Button book;

        public DealItemHolder(@NonNull View itemView) {
            super(itemView);

            name= itemView.findViewById(R.id.service_name);
            time  = itemView.findViewById(R.id.time_dur);
            img=itemView.findViewById(R.id.icon_img);
            price = itemView.findViewById(R.id.price);
            discount =itemView.findViewById(R.id.discount);
            book =itemView.findViewById(R.id.books);
        }
    }
}
