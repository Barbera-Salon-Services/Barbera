package com.barbera.barberaconsumerapp.Services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.cardview.widget.CardView;

import com.barbera.barberaconsumerapp.ActivityPhoneVerification;
import com.barbera.barberaconsumerapp.Bookings.BookingPage;
import com.barbera.barberaconsumerapp.HomeActivity;
import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Utils.CartItemModel;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceCart;
import com.barbera.barberaconsumerapp.network_aws.Success;
import com.barbera.barberaconsumerapp.network_aws.SuccessReturn;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OfferDetailDialog extends AppCompatDialogFragment {
    private ImageView image;
    private CardView bookCard,cartCard;
    private TextView offer_name,offer_details,book,cart;
    private String name,img,det,type,id;
    private int price,time,discount,start,end;
    public OfferDetailDialog(String name,String details,String image,String serviceId,int price,String type,int time,int discount,int start,int end){
        this.name=name;
        det=details;
        img=image;
        this.price=price;
        this.time=time;
        this.type=type;
        id=serviceId;
        this.discount=discount;
        this.start=start;
        this.end=end;
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
        cart=view.findViewById(R.id.offer_addtocart);
        bookCard=view.findViewById(R.id.book_card);
        cartCard=view.findViewById(R.id.cart_card);

        Calendar cal=Calendar.getInstance();
        int dt= cal.get(Calendar.DAY_OF_WEEK);
        dt--;
        //Toast.makeText(getContext(), dt+" "+start+" "+end, Toast.LENGTH_SHORT).show();
        if(!(dt<=end && dt>=start)){
            //Toast.makeText(getContext(),"Start:"+start+" end:"+end+" cur:"+dt,Toast.LENGTH_SHORT).show();
            bookCard.setCardBackgroundColor(getResources().getColor(R.color.gray));
            cartCard.setCardBackgroundColor(getResources().getColor(R.color.gray));
            book.setEnabled(false);
            cart.setEnabled(false);
        }
        else{
            bookCard.setCardBackgroundColor(getResources().getColor(R.color.offer_color));
            cartCard.setCardBackgroundColor(getResources().getColor(R.color.offer_color));
            book.setEnabled(true);
            cart.setEnabled(true);
        }

        Retrofit retrofit = RetrofitClientInstanceCart.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = getActivity().getSharedPreferences("Token", getActivity().MODE_PRIVATE);
        String token = preferences.getString("token", "no");

        Glide.with(OfferDetailDialog.this).load(img+"?" + new Date().getTime()).into(image);
        //offer_name.setText(name);
        det=det.replaceAll("/n","\n");
        offer_details.setText(det);

        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (token.equals("no")) {
                    Toast.makeText(getContext(), "You Must Log In to continue", Toast.LENGTH_LONG).show();
                    getActivity().startActivity(new Intent(getActivity(), ActivityPhoneVerification.class));
                } else {
                    String ordersummary = "";
                    ordersummary += "(" + type + ")" + name + "\t\t\tRs" + price + "\n";
                    List<CartItemModel> list = new ArrayList<>();
                    list.add(new CartItemModel(null, null, 0, null, 1, time, id, false, null,name));
                    Intent intent = new Intent(getActivity(), BookingPage.class);
                    intent.putExtra("Booking Amount", (price*(100-discount))/100);
                    intent.putExtra("BookingType", "direct");
                    intent.putExtra("Order Summary", ordersummary);
                    intent.putExtra("Time", time);
                    intent.putExtra("sidlist", (Serializable) list);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(intent);
                }
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (token.equals("no")) {
                    Toast.makeText(getContext(), "You Must Log In to continue", Toast.LENGTH_LONG).show();
                    getActivity().startActivity(new Intent(getActivity(), ActivityPhoneVerification.class));
                } else {
                    ProgressDialog progressDialogView=new ProgressDialog(getActivity());
                    progressDialogView.show();
//                    progressDialog.show();
//                    progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//                    progressDialog.setContentView(R.layout.progress_dialog);
//                    progressDialog.setCancelable(false);
                    List<String> idList = new ArrayList<>();
                    idList.add(id+","+name);
                    Call<SuccessReturn> call = jsonPlaceHolderApi2.addToCart(new Success(idList), "Bearer " + token);
                    call.enqueue(new Callback<SuccessReturn>() {
                        @Override
                        public void onResponse(Call<SuccessReturn> call, Response<SuccessReturn> response) {
                            if (response.code() == 200) {
                                SuccessReturn success = response.body();
                                if (success.isSuccess()) {
                                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Count", getActivity().MODE_PRIVATE);
                                    int count = sharedPreferences.getInt("count", 0);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putInt("count", count + 1);
                                    editor.apply();
                                    //ServiceTypeAdapter.serviceAdapter.notifyDataSetChanged();
                                    progressDialogView.dismiss();
                                    Toast.makeText(getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                                } else {
                                    progressDialogView.dismiss();
                                    Toast.makeText(getContext(), success.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                progressDialogView.dismiss();
                                Toast.makeText(getContext(), "Already added to cart", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<SuccessReturn> call, Throwable t) {
                            progressDialogView.dismiss();
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        //Toast.makeText(getContext(), "HELLO", Toast.LENGTH_SHORT).show();

        return builder.create();
    }
}
