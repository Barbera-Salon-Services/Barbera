package com.barbera.barberaconsumerapp;

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

public class BarberDetailDialog extends AppCompatDialogFragment {

    public TextView yes, no;
    private final String one;
    private final String two;

    public BarberDetailDialog(String name, String phone) {
        this.one = name;
        this.two = phone;
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
        ImageView call = view.findViewById(R.id.callsss);

//        Toast.makeText(getContext(),one+two+"dvs",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+two));

        yes.setText("Name: " +one);
        no.setText("Call:  "+two);

        call.setOnClickListener(v -> {
            startActivity(intent);
        });


        return builder.create();

    }
}
