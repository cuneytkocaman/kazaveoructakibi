package com.cuneyt.kazaveoructakibim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class InfoActivity extends AppCompatActivity {

    private Toolbar toolbarInfo;
    private ViewPager viewPager;

    public void visualObject(){
        toolbarInfo = findViewById(R.id.toolbarInfo);
        viewPager = findViewById(R.id.viewPager);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        visualObject();

        toolbarInfo.setTitle("Bilgilendirme");
        toolbarInfo.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbarInfo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Toolbar geri butonu.

        ImageAdapter adapter = new ImageAdapter(this);
        viewPager.setAdapter(adapter);
    }
}