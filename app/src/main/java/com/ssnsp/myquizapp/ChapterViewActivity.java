package com.ssnsp.myquizapp;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ChapterViewActivity extends AppCompatActivity {
    ListView listview;
    Toolbar toolbar;
    String tutorials[]
            = { "1. ઈતિહાસ જાણવાના સ્ત્રોત",
            "2. આદિમાનવના સ્થાયી જીવનની શરૂઆત",
            "3. પ્રાચીન નગરો અને ગ્રંથો",
            "4. ભારતની પ્રારંભિક રાજ્યવ્યવસ્થા"};
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chapter_view_activity);
        listview = findViewById(R.id.chapterlist);
        toolbar = findViewById(R.id.toolbar);

        Intent i = getIntent();
        String toolbarstd = i.getStringExtra("std");
        toolbar.setTitle(toolbarstd);

        ArrayAdapter<String> arr;
        arr = new ArrayAdapter<String>(
                this, R.layout.chapter_list_row,R.id.chapter_row, tutorials);
        listview.setAdapter(arr);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(ChapterViewActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });
    }
}
