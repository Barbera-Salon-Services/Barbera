package com.barbera.barberaconsumerapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class CheckTermDialog extends AppCompatDialogFragment {
    private CheckBox checkBox;
    private CheckTerms checkTerms;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_terms,null);

        builder.setView(view).setTitle("BARBERA")
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(checkBox.isChecked()){
                             boolean selected = true;
                             checkTerms.extractBool(selected);
                        }
                    }
                });
        checkBox = view.findViewById(R.id.checkBox);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            checkTerms = (CheckTerms) context;
        }catch (ClassCastException w){
            throw new ClassCastException(context.toString()+"Must");
        }

    }

    public interface CheckTerms{
        void extractBool(Boolean selected);
    }
}
