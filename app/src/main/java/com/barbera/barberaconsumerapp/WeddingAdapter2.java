package com.barbera.barberaconsumerapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WeddingAdapter2 extends BaseAdapter {
    private List<Service> serviceList;

    public WeddingAdapter2(List<Service> serviceList) {
        this.serviceList = serviceList;
    }

    @Override
    public int getCount() {
        return serviceList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final View view;
        if (convertView == null)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_service_piece, null);
        else
            view = (View) convertView;

        // ImageView logo = view.findViewById(R.id.service_image);
        TextView title = view.findViewById(R.id.service_fragement_title);
        TextView price = view.findViewById(R.id.service_fragement_price);
        TextView cutPrice=view.findViewById(R.id.service_fragement_cut_price);
        final CheckBox checkBox=view.findViewById(R.id.service_fragement_check_box);
        //TextView time=view.findViewById(R.id.service_fragement_time);
        //  final Button addToCart = view.findViewById(R.id.new_service_add_to_cart);
        //  Button bookNow=view.findViewById(R.id.new_service_book_now_button);

        /*  Glide.with(view.getContext()).load(serviceList.get(position).getImageId())
           .apply(new RequestOptions().placeholder(R.drawable.logo)).into(logo);*/
        final String amount = "Rs " + serviceList.get(position).getPrice();
        String CutAmount="Rs " +serviceList.get(position).getCutPrice();
        title.setText(serviceList.get(position).getServiceName());
        price.setText(amount);
        cutPrice.setText(CutAmount);
        //time.setText(serviceList.get(position).getTime()+" Min");
        final Service adapterList=serviceList.get(position);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked()){
                    // Toast.makeText(view.getContext(),"Checked",Toast.LENGTH_LONG).show();
                    WeddingActivity2.checkeditemList.add(new CheckedModel(serviceList.get(position).getServiceId(),serviceList.get(position).getServiceName(),
                            serviceList.get(position).getPrice()));
                    checkBox.setChecked(true);
                }
                else{
                    //Toast.makeText(view.getContext(),"UnChecked",Toast.LENGTH_LONG).show();
                    //  CheckedModel model=new CheckedModel(serviceList.get(position).getServiceId(),serviceList.get(position).getServiceName()
                    //   ,serviceList.get(position).getPrice());
                    //  ParlourActivity.checkeditemList.remove(serviceList.get(position).getServiceId());
                    for (int i=0;i<ParlourActivity.checkeditemList.size();i++)
                        if(WeddingActivity2.checkeditemList.get(i).getId().equals(serviceList.get(position).getServiceId())){
                            WeddingActivity2.checkeditemList.remove(i);
                            break;
                        }
                    checkBox.setChecked(false);
                }
            }
        });
        WeddingActivity2.addToCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(view.getContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    view.getContext().startActivity(new Intent(view.getContext(),SecondScreen.class));
                }
                else {
                    if(ParlourActivity.checkeditemList.size()!=0){
                        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
                        progressDialog.show();
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.setCancelable(false);
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .collection("UserData").document("MyCart");
                        Map<String, Object> cartData = new HashMap<>();
                        for (int i = 0; i < ParlourActivity.checkeditemList.size(); i++) {
                            if(!dbQueries.cartList.contains(WeddingActivity2.checkeditemList.get(i).getId())) {
                                cartData.put("service_id_" + String.valueOf(dbQueries.cartList.size() + i + 1), WeddingActivity2.checkeditemList.get(i).getId());
                                cartData.put("service_id_" + String.valueOf(dbQueries.cartList.size() + i + 1) + "_type", WeddingActivity2.salontype);
                            }
                            else{
                                ParlourActivity.checkeditemList.remove(i);
                                --i;
                            }
                        }
                        cartData.put("cart_list_size", (long) (dbQueries.cartList.size() + WeddingActivity2.checkeditemList.size()));
                        documentReference.update(cartData)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            for(int i = 0; i< WeddingActivity2.checkeditemList.size(); i++)
                                                dbQueries.cartList.add(WeddingActivity2.checkeditemList.get(i).getId());
                                            dbQueries.cartItemModelList.clear();
                                            CartActivity.updateCartItemModelList();
                                            WeddingActivity2.loadNumberOnCartParlour();
                                            Toast.makeText(view.getContext(), "Service Added to Cart", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        } else {
                                            Toast.makeText(view.getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    }
                                });
                    }
                    else
                        Toast.makeText(view.getContext(),"Select Something First",Toast.LENGTH_LONG).show();
                }}
        });
        WeddingActivity2.bookNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(view.getContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    view.getContext().startActivity(new Intent(view.getContext(),SecondScreen.class));
                }
                else {
                    if(WeddingActivity2.checkeditemList.size()!=0) {
                        int amount = 0;
                        String ordersummary="";
                        for (int i = 0; i < WeddingActivity2.checkeditemList.size(); i++) {
                            ordersummary += "(" + WeddingActivity2.salontype + ")" + WeddingActivity2.checkeditemList.get(i).getName()
                                    + "\t\t\tRs " + WeddingActivity2.checkeditemList.get(i).getPrice() + "\n";
                            amount += Integer.parseInt(WeddingActivity2.checkeditemList.get(i).getPrice());
                        }
                        //BookingPage.BookingTotalAmount = amount;
                        Intent intent = new Intent(view.getContext(), BookingPage.class);
                        intent.putExtra("Booking Amount",amount);
                        intent.putExtra("Order Summary",ordersummary);
                        view.getContext().startActivity(intent);
                    }
                    else
                        Toast.makeText(view.getContext(),"Please Select Something First",Toast.LENGTH_LONG).show();

                }
            }
        });
        return view;
    }


}
