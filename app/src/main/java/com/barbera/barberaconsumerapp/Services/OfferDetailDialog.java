package com.barbera.barberaconsumerapp.Services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.barbera.barberaconsumerapp.Bookings.BookingPage;
import com.barbera.barberaconsumerapp.HomeActivity;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Utils.CartItemModel;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OfferDetailDialog extends AppCompatDialogFragment {
    private ImageView image;
    private TextView offer_name,offer_details,book;
    private String name,img,det,type,id;
    private int price,time;
    public OfferDetailDialog(String name,String details,String image,String serviceId,int price,String type,int time){
        this.name=name;
        det=details;
        img=image;
        this.price=price;
        this.time=time;
        this.type=type;
        id=serviceId;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.offer_details, null);
        builder.setView(view).setTitle("Offer details");

        offer_details=view.findViewById(R.id.offerDetails);
        //offer_name=view.findViewById(R.id.offerName);
        image=view.findViewById(R.id.offerImage);
        book=view.findViewById(R.id.offer_book);

        Glide.with(OfferDetailDialog.this).load(img+"?" + new Date().getTime()).into(image);
        //offer_name.setText(name);
        det=det.replaceAll("/n","\n");
        offer_details.setText(det);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ordersummary="";
                ordersummary+= "(" + type + ")" + name + "\t\t\tRs" + price + "\n";
                List<CartItemModel> list = new ArrayList<>();
                list.add(new CartItemModel(null, null, 0, null, 1, time, id, false, null));
                Intent intent = new Intent(getActivity(), BookingPage.class);
                intent.putExtra("Booking Amount", price);
                intent.putExtra("BookingType", "direct");
                intent.putExtra("Order Summary", ordersummary);
                intent.putExtra("Time", time);
                intent.putExtra("sidlist", (Serializable) list);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
            }
        });
        //Toast.makeText(getContext(), "HELLO", Toast.LENGTH_SHORT).show();

        return builder.create();
    }
}
