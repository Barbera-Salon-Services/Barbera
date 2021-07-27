package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class SpecialOffers extends AppCompatActivity {

    RecyclerView recyclerView;
    SpecialOfferAdapter specialOfferAdapter;
    List<SpecialOfferData> specialOfferDataList;

    public static final String DummyImgUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQh6igBH1UkC4nE8qESGQobUF8n1iiIurPmog&usqp=CAU";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_offers);

        // Setting up own toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Special Offers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.special_offers_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        specialOfferDataList = new ArrayList<>();

        specialOfferDataList.add(new SpecialOfferData("Classic Package", "1. Golden Facial\n2.Garnier Hair-color", "@Rs. 900\n@Rs. 100", "@Rs. 1000", "@Rs. 559",  DummyImgUrl));
        specialOfferDataList.add(new SpecialOfferData("Classic Package", "1. Golden Facial\n2.Garnier Hair-color", "@Rs. 900\n@Rs. 100", "@Rs. 1000", "@Rs. 559",  DummyImgUrl));
        specialOfferDataList.add(new SpecialOfferData("Classic Package", "1. Golden Facial\n2.Garnier Hair-color", "@Rs. 900\n@Rs. 100", "@Rs. 1000", "@Rs. 559",  DummyImgUrl));
        specialOfferDataList.add(new SpecialOfferData("Classic Package", "1. Golden Facial\n2.Garnier Hair-color", "@Rs. 900\n@Rs. 100", "@Rs. 1000", "@Rs. 559",  DummyImgUrl));
        specialOfferDataList.add(new SpecialOfferData("Classic Package", "1. Golden Facial\n2.Garnier Hair-color", "@Rs. 900\n@Rs. 100", "@Rs. 1000", "@Rs. 559",  DummyImgUrl));
        specialOfferDataList.add(new SpecialOfferData("Classic Package", "1. Golden Facial\n2.Garnier Hair-color", "@Rs. 900\n@Rs. 100", "@Rs. 1000", "@Rs. 559",  DummyImgUrl));
        specialOfferDataList.add(new SpecialOfferData("Classic Package", "1. Golden Facial\n2.Garnier Hair-color", "@Rs. 900\n@Rs. 100", "@Rs. 1000", "@Rs. 559",  DummyImgUrl));

        specialOfferAdapter = new SpecialOfferAdapter(specialOfferDataList, this);
        recyclerView.setAdapter(specialOfferAdapter);
    }
}