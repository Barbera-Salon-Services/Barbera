package com.barbera.barberaconsumerapp.Services;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.barbera.barberaconsumerapp.R;
import com.barbera.barberaconsumerapp.Utils.ServiceItem;
import com.barbera.barberaconsumerapp.network_aws.JsonPlaceHolderApi2;
import com.barbera.barberaconsumerapp.network_aws.RetrofitClientInstanceCart;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Retrofit;

import static java.lang.Integer.parseInt;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private List<ServiceItem> serviceList;
    private Context con;

    public ServiceAdapter(Context context,List<ServiceItem> serviceList) {
        con=context;
        this.serviceList = serviceList;
    }


    @NonNull
    @NotNull
    @Override
    public ServiceAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.new_service_piece, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServiceAdapter.ViewHolder holder, int position) {
        Retrofit retrofit = RetrofitClientInstanceCart.getRetrofitInstance();
        JsonPlaceHolderApi2 jsonPlaceHolderApi2 = retrofit.create(JsonPlaceHolderApi2.class);
        SharedPreferences preferences = con.getSharedPreferences("Token", con.MODE_PRIVATE);
        String token = preferences.getString("token", "no");
        //  final Button addToCart = view.findViewById(R.id.new_service_add_to_cart);
        //  Button bookNow=view.findViewById(R.id.new_service_book_now_button);

        /*  Glide.with(view.getContext()).load(serviceList.get(position).getImageId())
           .apply(new RequestOptions().placeholder(R.drawable.logo)).into(logo);*/

        final String amount = "Rs " + serviceList.get(position).getPrice();
        String CutAmount="Rs " +serviceList.get(position).getCutprice();
        holder.title.setText(serviceList.get(position).getName());
        holder.price.setText(amount);
        holder.cutPrice.setText(CutAmount);
        if(serviceList.get(position).getDetail()!=null){
            String x= serviceList.get(position).getDetail().replaceAll("/n","\n");
            holder.details.setText(x);
        }
        holder.time.setText(serviceList.get(position).getTime()+" Min");
        final ServiceItem adapterList=serviceList.get(position);

//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(holder.checkBox.isChecked()){
//                    // Toast.makeText(view.getContext(),"Checked",Toast.LENGTH_LONG).show();
//                    ParlourActivity.checkeditemList.add(new CheckedModel(serviceList.get(position).getId(),serviceList.get(position).getName(),
//                            serviceList.get(position).getPrice(),serviceList.get(position).getTime()));
//                    holder.checkBox.setChecked(true);
//                }
//                else{
//                    //Toast.makeText(view.getContext(),"UnChecked",Toast.LENGTH_LONG).show();
//                    //  CheckedModel model=new CheckedModel(serviceList.get(position).getServiceId(),serviceList.get(position).getServiceName()
//                    //   ,serviceList.get(position).getPrice());
//                    //  ParlourActivity.checkeditemList.remove(serviceList.get(position).getServiceId());
//                    for (int i=0;i<ParlourActivity.checkeditemList.size();i++)
//                        if(ParlourActivity.checkeditemList.get(i).getId().equals(serviceList.get(position).getId())){
//                            ParlourActivity.checkeditemList.remove(i);
//                            break;
//                        }
//                    holder.checkBox.setChecked(false);
//                }
//            }
//        });
//
//        ParlourActivity.addToCart.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceType")
//            @Override
//            public void onClick(View v) {
//                if(token.equals("no")){
//                    Toast.makeText(view.getContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
//                    view.getContext().startActivity(new Intent(view.getContext(),SecondScreen.class));
//                }
//                else {
//                    if(ParlourActivity.checkeditemList.size()!=0){
//                        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
//                        progressDialog.show();
//                        progressDialog.setContentView(R.layout.progress_dialog);
//                        progressDialog.setCancelable(false);
//                        List<String> idList = new ArrayList<>();
//                        for (int i = 0; i < ParlourActivity.checkeditemList.size(); i++) {
//                            idList.add(ParlourActivity.checkeditemList.get(i).getId());
//                            Log.d("Item",ParlourActivity.checkeditemList.get(i).getId());
//                            ParlourActivity.checkeditemList.remove(i);
//                            i--;
//                        }
//                        Call<SuccessReturn> call=jsonPlaceHolderApi2.addToCart(new Success(idList),"Bearer "+token);
//                        call.enqueue(new Callback<SuccessReturn>() {
//                            @Override
//                            public void onResponse(Call<SuccessReturn> call, Response<SuccessReturn> response) {
//                                if(response.code()==200){
//                                    SuccessReturn success=response.body();
//                                    if(success.isSuccess()){
//                                        progressDialog.dismiss();
//                                        Toast.makeText(con,"Added to cart",Toast.LENGTH_SHORT).show();
//                                    }
//                                    else{
//                                        progressDialog.dismiss();
//                                        Toast.makeText(con,success.getMessage(),Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                                else{
//                                    progressDialog.dismiss();
//                                    Toast.makeText(con,"Could not add to cart",Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<SuccessReturn> call, Throwable t) {
//                                progressDialog.dismiss();
//                                Toast.makeText(con,t.getMessage(),Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                    else
//                        Toast.makeText(view.getContext(),"Select Something First",Toast.LENGTH_LONG).show();
//                }}
//        });
//
//        ParlourActivity.bookNow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(token.equals("no")){
//                    Toast.makeText(view.getContext(),"You Must Log In to continue",Toast.LENGTH_LONG).show();
//                    view.getContext().startActivity(new Intent(view.getContext(),SecondScreen.class));
//                }
//                else {
//                    if(ParlourActivity.checkeditemList.size()!=0) {
//                        int amount = 0,Time=0;
//                        String ordersummary="";
//                        //Toast.makeText(view.getContext(),"scascsnsvni", Toast.LENGTH_SHORT).show();
//                        for (int i = 0; i < ParlourActivity.checkeditemList.size(); i++) {
//                            ordersummary += "(" + ParlourActivity.salontype + ")" + ParlourActivity.checkeditemList.get(i).getName()
//                                    + "\t\t\tRs" + ParlourActivity.checkeditemList.get(i).getPrice() + "\n";
//                            amount += ParlourActivity.checkeditemList.get(i).getPrice();
//                            //Time+=ParlourActivity.checkeditemList.get(i).getTime();
//                        }
//                        //BookingPage.BookingTotalAmount = amount;
//                        Intent intent = new Intent(view.getContext(), BookingPage.class);
//                        intent.putExtra("Booking Amount",amount);
//                        intent.putExtra("BookingType","direct");
//                        intent.putExtra("Order Summary",ordersummary);
//                        intent.putExtra("Time",Time);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        view.getContext().startActivity(intent);
//                    }
//                    else
//                        Toast.makeText(view.getContext(),"Please Select Something First",Toast.LENGTH_LONG).show();
//
//                }
//            }
//        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView logo,timeImage;
        private TextView title,price,cutPrice,time,details;
        private CheckBox checkBox;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            logo = itemView.findViewById(R.id.service_image);
            title = itemView.findViewById(R.id.service_fragement_title);
            price = itemView.findViewById(R.id.service_fragement_price);
            cutPrice=itemView.findViewById(R.id.service_fragement_cut_price);
            checkBox=itemView.findViewById(R.id.service_fragement_check_box);
            time=itemView.findViewById(R.id.service_fragement_time);
            timeImage=itemView.findViewById(R.id.timer);
            details = itemView.findViewById(R.id.details);
        }
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

}
