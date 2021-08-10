package com.barbera.barberaconsumerapp.Bookings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.barbera.barberaconsumerapp.R;

public class BarberDetailDialog extends AppCompatDialogFragment {

    public TextView yes, no,dist;
    private final String one;
    private final String two;
    private final double three;

    public BarberDetailDialog(String name, String phone,double distance) {
        this.one = name;
        this.two = phone;
        this.three=distance;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.barber_detail_dialog, null);

        builder.setView(view).setTitle("Contact Info");
        yes = view.findViewById(R.id.tnam);
        no = view.findViewById(R.id.ph);
        dist=view.findViewById(R.id.tdist);
        ImageView call = view.findViewById(R.id.callsss);

//        Toast.makeText(getContext(),one+two+"dvs",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+two));

        yes.setText("Name: " +one);
        no.setText("Call:  "+two);
        dist.setText("Time for arrival: "+String.format("%.2f",three/30));

        call.setOnClickListener(v -> {
            startActivity(intent);
        });


        return builder.create();

    }
}
