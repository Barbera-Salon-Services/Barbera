package com.barbera.barberaconsumerapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceAdapter extends BaseAdapter {
    private List<Service> serviceList;

    public ServiceAdapter(List<Service> serviceList) {
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
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newservice_design, null);
        else
            view = (View) convertView;

        ImageView logo = view.findViewById(R.id.service_image);
        TextView title = view.findViewById(R.id.service_title);
        TextView price = view.findViewById(R.id.servicePrice);
        TextView cutPrice=view.findViewById(R.id.Service_cutPrice);
        final Button addToCart = view.findViewById(R.id.add_to_cart);

          Glide.with(view.getContext()).load(serviceList.get(position).getImageId())
           .apply(new RequestOptions().placeholder(R.drawable.logo)).into(logo);
        String amount = "Rs " + serviceList.get(position).getPrice();
        String CutAmount="Rs " +serviceList.get(position).getCutPrice();
        title.setText(serviceList.get(position).getServiceName());
        price.setText(amount);
        cutPrice.setText(CutAmount);
        //logo.setImageResource(R.drawable.logo);
        final Service adapterList=serviceList.get(position);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser()==null){
                    Toast.makeText(view.getContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
                    view.getContext().startActivity(new Intent(view.getContext(),SecondScreen.class));
                }
                else{
                    if(!dbQueries.cartList.contains(adapterList.getServiceId())){
                        final ProgressDialog progressDialog=new ProgressDialog(view.getContext());
                        progressDialog.show();
                       // SplashActivity.progressText.setText("Adding Service To Cart");
                        progressDialog.setContentView(R.layout.progress_dialog);
                        progressDialog.setCancelable(false);
                    addToCart.setEnabled(false);
                    DocumentReference documentReference=   FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .collection("UserData").document("MyCart");
                    Map<String,Object> cartData=new HashMap<>();
                    cartData.put("service_id_"+String.valueOf(dbQueries.cartList.size()+1),adapterList.getServiceId());
                    cartData.put("service_id_"+String.valueOf(dbQueries.cartList.size()+1)+"_type",adapterList.getServiceType());
                    cartData.put("cart_list_size",(long)(dbQueries.cartList.size()+1));
                    documentReference.update(cartData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        dbQueries.cartList.add(serviceList.get(position).getServiceId());
                                        dbQueries.cartItemModelList.clear();
                                        CartActivity.updateCartItemModelList();
                                        Toast.makeText(view.getContext(),"Service Added to Cart",Toast.LENGTH_SHORT).show();
                                        addToCart.setEnabled(true);
                                        progressDialog.dismiss();
                                    }
                                    else {
                                        Toast.makeText(view.getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        addToCart.setEnabled(true);
                                    }
                                }
                            });
                }
                else
                    Toast.makeText(view.getContext(),"Already Added to Cart",Toast.LENGTH_SHORT).show();
            }}
        });
        return view;
    }



}
