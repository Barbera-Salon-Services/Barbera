package com.barbera.barberaconsumerapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceCart;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceUser;
import com.barbera.barberaconsumerapp.network_aws.SuccessReturn;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.Integer.parseInt;

public class CartAdapter extends RecyclerView.Adapter {
    private Context context;
    public CartAdapter(Context context) {
        super();
        this.context=context;
    }
    private int q,totalCount;

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
            int price = dbQueries.cartItemModelList.get(position).getServicePrice();
            int quantity = dbQueries.cartItemModelList.get(position).getQuantity();
            String type = dbQueries.cartItemModelList.get(position).getType();
            String id=dbQueries.cartItemModelList.get(position).getId();

            ((CartItemViewHolder) holder).setServiceDetails(imageResource, title, price, quantity, type, position,id);

    }

    @Override
    public int getItemCount() {
        return dbQueries.cartItemModelList.size();
    }

    class CartItemViewHolder extends RecyclerView.ViewHolder {
        private ImageView logo;
        private TextView title;
        private TextView price;
        private TextView quantity;
        private TextView type;
        private Button increaseIncart;
        private Button decreaseIncart;

        public CartItemViewHolder(@NonNull View itemView) {

            super(itemView);
            logo = (ImageView) itemView.findViewById(R.id.cart_item_logo);
            title = (TextView) itemView.findViewById(R.id.cart_item_title);
            price = (TextView) itemView.findViewById(R.id.cart_item_price);
            quantity = (TextView) itemView.findViewById(R.id.cart_item_quantity);
            type = (TextView) itemView.findViewById(R.id.type_in_cart);
            increaseIncart = (Button) itemView.findViewById(R.id.increaseInCart);
            decreaseIncart = (Button) itemView.findViewById(R.id.decreaseInCart);
        }

        private void setServiceDetails(String resource, String Service, int amount, int Quantity, String Type, final int position,String id) {
            title.setText(Service);
            price.setText("Rs " + amount);
            quantity.setText(""+Quantity);
            type.setText(Type);
            q = Quantity;
//            Glide.with(itemView.getContext()).load(resource)
//                    .apply(new RequestOptions().placeholder(R.drawable.logo)).into(logo);
            //updateTotalAmount();
            Retrofit retrofit = RetrofitClientInstanceCart.getRetrofitInstance();
            JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
            SharedPreferences preferences = context.getSharedPreferences("Token", context.MODE_PRIVATE);
            String token = preferences.getString("token", "no");
            increaseIncart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    q++;
                    quantity.setText(""+q);
                }
            });
//            Call<Void> call=jsonPlaceHolderApi2.updateQuantity(new CartItemModel(null,null,null,null,null,0,null,true,id),"Bearer "+token);
//            call.enqueue(new Callback<Void>() {
//                @Override
//                public void onResponse(Call<Void> call, Response<Void> response) {
//                    if(response.code()==200){
//                        q++;
//                        quantity.setText(""+ q);
//                    }
//                    else{
//                        Toast.makeText(context,"Could not increase cart",Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Void> call, Throwable t) {
//                    Toast.makeText(context,t.getMessage(),Toast.LENGTH_SHORT).show();
//                }
//            });

            decreaseIncart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    dbQueries.cartItemModelList.get(position).setQuantity(dbQueries.cartItemModelList.get(position).getQuantity()-1);
//                    updateQuantity(position);
//                    updateTotalAmount();
                    if (q == 1) {
                        Call<SuccessReturn> call = jsonPlaceHolderApi2.deleteFromCart(id, token);
                        call.enqueue(new Callback<SuccessReturn>() {
                            @Override
                            public void onResponse(Call<SuccessReturn> call, Response<SuccessReturn> response) {
                                if (response.code() == 200) {
                                    SuccessReturn successReturn = response.body();
                                    totalCount = successReturn.getCount();
                                    SharedPreferences sharedPreferences= context.getSharedPreferences("Count",context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putInt("count",totalCount);
                                    editor.apply();
                                    Toast.makeText(context, "Service deleted from cart", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Could not decrease cart", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<SuccessReturn> call, Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        q--;
                        quantity.setText("" + q);
                    }
                }
            });
//            CartActivity.continueToBooking.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(dbQueries.cartItemModelList.size()>=1) {
//                       // BookingPage.BookingTotalAmount=CartActivity.totalAmount;
//                        String OrderSummary="";
//                        int time=0;
//                        for (int i = 0; i < dbQueries.cartItemModelList.size(); i++) {
//                            OrderSummary += "(" + dbQueries.cartItemModelList.get(i).getType() + ")" +
//                                    dbQueries.cartItemModelList.get(i).getServiceName()
//                                    + "(" + dbQueries.cartItemModelList.get(i).getQuantity() + ")" + "\t\t\t\t" + "Rs" +
//                                    dbQueries.cartItemModelList.get(i).getServicePrice() + "\n";
//                            time+=parseInt(dbQueries.cartItemModelList.get(i).getTime());
//                        }
//                        Intent intent=new Intent(itemView.getContext(),BookingPage.class);
//                        intent.putExtra("Time",time);
//                        intent.putExtra("BookingType","Cart");
//                        intent.putExtra("Booking Amount",CartActivity.totalAmount);
//                        intent.putExtra("Order Summary",OrderSummary);
//                        itemView.getContext().startActivity(intent);
//                    }
//                    else {
//                        Toast.makeText(itemView.getContext(),"Please Add Something in Cart to Continue",Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
//
//        private void updateQuantity(int position){
//            if(dbQueries.cartItemModelList.get(position).getQuantity()==0)
//                removeFromCart(position);
//            else
//            quantity.setText(""+dbQueries.cartItemModelList.get(position).getQuantity());
//        }
//
//        private void removeFromCart(final int position){
//            CartActivity.progressBarMyCart.setVisibility(View.VISIBLE);
//            CartActivity.continueToBooking.setEnabled(false);
//            dbQueries.cartList.remove(dbQueries.cartItemModelList.get(position).getIndex()-1);
//            dbQueries.cartItemModelList.remove(dbQueries.cartItemModelList.get(position));
//            Map<String,Object> updateCartData=new HashMap<>();
//            for(int i=0;i<dbQueries.cartItemModelList.size();i++) {
//                updateCartData.put("service_id_" + (i+1), dbQueries.cartItemModelList.get(i).getServiceId());
//                updateCartData.put("service_id_"+(i+1)+"_type",dbQueries.cartItemModelList.get(i).getType());
//            }
//            updateCartData.put("cart_list_size",(long)dbQueries.cartList.size());
//            FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                    .collection("UserData").document("MyCart")
//                    .set(updateCartData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if(task.isSuccessful()) {
//                        Toast.makeText(itemView.getContext(), "Service Removed From Cart", Toast.LENGTH_SHORT).show();
//                        CartActivity.progressBarMyCart.setVisibility(View.INVISIBLE);
//                        CartActivity.continueToBooking.setEnabled(true);
//
//                    }
//                    else {
//                        Toast.makeText(itemView.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                        CartActivity.progressBarMyCart.setVisibility(View.INVISIBLE);
//                        CartActivity.continueToBooking.setEnabled(true);
//                    }
//                }
//            });
//             CartActivity.updateCartItemModelList();
//
//        }
//
//        private void updateTotalAmount(){
//            CartActivity.totalAmount=0;
//            for(int i=0;i<dbQueries.cartItemModelList.size();i++) {
//                  int price= parseInt(dbQueries.cartItemModelList.get(i).getServicePrice());
//                CartActivity.totalAmount +=(price*dbQueries.cartItemModelList.get(i).getQuantity());
//            }
//            String result=String.valueOf(CartActivity.totalAmount);
//            CartActivity.total_cart_amount.setText("Rs "+result);
//        }

        }
    }
}
