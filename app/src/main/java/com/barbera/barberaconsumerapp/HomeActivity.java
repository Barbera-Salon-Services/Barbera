package com.barbera.barberaconsumerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView menRecyclerView;
    RecyclerView womenRecyclerView;
    RecyclerView weddingRecyclerView;
    List<String> imgUrl, imgName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addMenGrid();
        addWomenGrid();
        addWeddingGrid();
    }

    private void addWeddingGrid() {

        weddingRecyclerView = findViewById(R.id.wedding_recycler_view);

        imgName = new ArrayList<>();
        imgUrl = new ArrayList<>();
        GridAdapter gridAdapter;

        imgName.add("Bridal Packages\nStarting@ Rs. 5599");
        imgName.add("Groom's Packages\nStarting@ Rs. 2599");

        imgUrl.add("https://i.ibb.co/8rtx241/image.png");
        imgUrl.add("https://i.ibb.co/840KRbC/image.png");

        gridAdapter = new GridAdapter(imgUrl, imgName, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        weddingRecyclerView.setLayoutManager(gridLayoutManager);
        weddingRecyclerView.setAdapter(gridAdapter);
    }

    private void addWomenGrid() {
        womenRecyclerView = findViewById(R.id.women_recycler_view);

        imgName = new ArrayList<>();
        imgUrl = new ArrayList<>();
        GridAdapter gridAdapter;

        imgName.add("Hair Cut");
        imgName.add("Hair Care");
        imgName.add("Hair Color");
        imgName.add("Facial");
        imgName.add("Head Massage");
        imgName.add("Bleech");

        imgUrl.add("https://i.ibb.co/MVmwpZH/image.png");
        imgUrl.add("https://i.ibb.co/6tn2gFk/image.png");
        imgUrl.add("https://i.ibb.co/SXLk7XW/image.png");
        imgUrl.add("https://i.ibb.co/f0MFcHx/image.png");
        imgUrl.add("https://i.ibb.co/j9K4bDL/image.png");
        imgUrl.add("https://i.ibb.co/HDyvXxp/image.png");

        gridAdapter = new GridAdapter(imgUrl, imgName, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        womenRecyclerView.setLayoutManager(gridLayoutManager);
        womenRecyclerView.setAdapter(gridAdapter);

    }

    private void addMenGrid() {
        imgName = new ArrayList<>();
        imgUrl = new ArrayList<>();
        GridAdapter gridAdapter;

        imgName.add("Hair Cut");
        imgName.add("Shaving");
        imgName.add("Hair Color");
        imgName.add("Facial");
        imgName.add("Head Massage");
        imgName.add("Bleech");

        imgUrl.add("https://i.ibb.co/t4z5Vqp/image.png");
        imgUrl.add("https://i.ibb.co/7zYTRbs/image.png");
        imgUrl.add("https://i.ibb.co/mGqJLmJ/image.png");
        imgUrl.add("https://i.ibb.co/PFWzrPb/image.png");
        imgUrl.add("https://i.ibb.co/smdS7FT/image.png");
        imgUrl.add("https://i.ibb.co/Ht4gYjf/image.png");

        gridAdapter = new GridAdapter(imgUrl, imgName, this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        menRecyclerView = findViewById(R.id.men_recycler_view);
        menRecyclerView.setLayoutManager(gridLayoutManager);
        menRecyclerView.setAdapter(gridAdapter);

    }
}