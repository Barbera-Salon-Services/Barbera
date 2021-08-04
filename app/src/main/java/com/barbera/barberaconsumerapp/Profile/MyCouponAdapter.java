package com.barbera.barberaconsumerapp.Profile;

import android.content.ClipData;
import android.content.ClipboardManager;
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

import com.barbera.barberaconsumerapp.Bookings.BookingPage;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Utils.GetCouponItem;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyCouponAdapter extends RecyclerView.Adapter<MyCouponAdapter.CouponViewHolder> {
    private List<GetCouponItem> couponItemModels;
    private Context context;

    public MyCouponAdapter(List<GetCouponItem> couponItemModels, Context context) {
        this.couponItemModels = couponItemModels;
        this.context=context;
    }

    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.newservice_design,parent,false);
        return new CouponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyCouponAdapter.CouponViewHolder holder, int position) {
        GetCouponItem item=couponItemModels.get(position);
//        holder.lower.setText("Lower price for coupon: "+item.getLowerLimit());
//        holder.upper.setText("Upper price for coupon: "+item.getUpperLimit());
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", holder.name.getText().subSequence(13,holder.name.getText().toString().length()));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context,"Copied!",Toast.LENGTH_SHORT).show();
            }
        });
        holder.discount.setText("Coupon discount:Rs "+item.getDiscount());
        holder.name.setText("Coupon name: "+item.getName());
    }


    @Override
    public int getItemCount() {
        return couponItemModels.size();
    }

    public class CouponViewHolder extends RecyclerView.ViewHolder {
        private TextView name,discount,upper,lower;
        private ImageView copy;

        public CouponViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.couponName);
            discount=itemView.findViewById(R.id.couponDiscount);
            copy=itemView.findViewById(R.id.copy_coupon);
//            upper=itemView.findViewById(R.id.couponUpperPrice);
//            lower=itemView.findViewById(R.id.couponLowerPrice);
        }
    }

}
