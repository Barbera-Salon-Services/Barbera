package com.barbera.barberaconsumerapp;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.barbera.barberaconsumerapp.CartActivity.cartItemRecyclerView;
import static com.barbera.barberaconsumerapp.CartActivity.cartTotalAmtLayout;
import static com.barbera.barberaconsumerapp.CartActivity.emptyCart;

public class CartAdapter extends RecyclerView.Adapter {

    public CartAdapter() {
        super();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       /* if(viewType==0){
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_cart_layout,parent,false);
            return new EmptyCartViewHolder(view);
        }*/

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
            return new CartItemViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
       if(getItemCount()==0)
           return 0;
       return 1;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            String imageResource = dbQueries.cartItemModelList.get(position).getImageId();
            String title = dbQueries.cartItemModelList.get(position).getServiceName();
            String price = dbQueries.cartItemModelList.get(position).getServicePrice();
            int quantity = dbQueries.cartItemModelList.get(position).getQuantity();
            String type = dbQueries.cartItemModelList.get(position).getType();

            ((CartItemViewHolder) holder).setServiceDetails(imageResource, title, price, quantity, type, position);

    }

    @Override
    public int getItemCount() {
        return dbQueries.cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder{
        private ImageView logo;
        private TextView title;
        private TextView price;
        private TextView quantity;
        private TextView type;
        private Button increaseIncart;
        private Button decreaseIncart;

        public CartItemViewHolder(@NonNull View itemView) {

            super(itemView);
            logo=(ImageView) itemView.findViewById(R.id.cart_item_logo);
            title=(TextView) itemView.findViewById(R.id.cart_item_title);
            price=(TextView) itemView.findViewById(R.id.cart_item_price);
            quantity=(TextView) itemView.findViewById(R.id.cart_item_quantity);
            type=(TextView)itemView.findViewById(R.id.type_in_cart);
            increaseIncart=(Button)itemView.findViewById(R.id.increaseInCart);
            decreaseIncart=(Button)itemView.findViewById(R.id.decreaseInCart);
        }

        private void setServiceDetails(String resource, String Service, String amount, int Quantity, String Type, final int position){

            title.setText(Service);
            price.setText("Rs "+amount);
            quantity.setText(""+Quantity);
            type.setText(Type);
            Glide.with(itemView.getContext()).load(resource)
                    .apply(new RequestOptions().placeholder(R.drawable.logo)).into(logo);
            updateTotalAmount();

            increaseIncart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbQueries.cartItemModelList.get(position).setQuantity(dbQueries.cartItemModelList.get(position).getQuantity()+1);
                    updateQuantity(position);
                    updateTotalAmount();
                }
            });

            decreaseIncart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbQueries.cartItemModelList.get(position).setQuantity(dbQueries.cartItemModelList.get(position).getQuantity()-1);
                    updateQuantity(position);
                    updateTotalAmount();
                }
            });
            CartActivity.continueToBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dbQueries.cartItemModelList.size()>=1) {
                        BookingPage.BookingTotalAmount+=CartActivity.totalAmount;

                        Intent intent=new Intent(itemView.getContext(),BookingPage.class);
                        intent.putExtra("BookingType","Cart");
                        itemView.getContext().startActivity(intent);
                    }
                    else {
                        Toast.makeText(itemView.getContext(),"Please Add Something in Cart to Continue",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        private void updateQuantity(int position){
            if(dbQueries.cartItemModelList.get(position).getQuantity()==0)
                removeFromCart(position);
            else
            quantity.setText(""+dbQueries.cartItemModelList.get(position).getQuantity());
        }

        private void removeFromCart(final int position){
            CartActivity.progressBarMyCart.setVisibility(View.VISIBLE);
            CartActivity.continueToBooking.setEnabled(false);
            dbQueries.cartList.remove(dbQueries.cartItemModelList.get(position).getIndex()-1);
            dbQueries.cartItemModelList.remove(dbQueries.cartItemModelList.get(position));
            Map<String,Object> updateCartData=new HashMap<>();
            for(int i=0;i<dbQueries.cartItemModelList.size();i++) {
                updateCartData.put("service_id_" + (i+1), dbQueries.cartItemModelList.get(i).getServiceId());
                updateCartData.put("service_id_"+(i+1)+"_type",dbQueries.cartItemModelList.get(i).getType());
            }
            updateCartData.put("cart_list_size",(long)dbQueries.cartList.size());
            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .collection("UserData").document("MyCart")
                    .set(updateCartData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(itemView.getContext(), "Service Removed From Cart", Toast.LENGTH_SHORT).show();
                        CartActivity.progressBarMyCart.setVisibility(View.INVISIBLE);
                        CartActivity.continueToBooking.setEnabled(true);

                    }
                    else {
                        Toast.makeText(itemView.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        CartActivity.progressBarMyCart.setVisibility(View.INVISIBLE);
                        CartActivity.continueToBooking.setEnabled(true);
                    }
                }
            });
             CartActivity.updateCartItemModelList();

        }

        private void updateTotalAmount(){
            CartActivity.totalAmount=0;
            for(int i=0;i<dbQueries.cartItemModelList.size();i++) {
                  int price=Integer.parseInt(dbQueries.cartItemModelList.get(i).getServicePrice());
                CartActivity.totalAmount +=(price*dbQueries.cartItemModelList.get(i).getQuantity());
            }
            String result=String.valueOf(CartActivity.totalAmount);
            CartActivity.total_cart_amount.setText("Rs "+result);
        }

    }

}
