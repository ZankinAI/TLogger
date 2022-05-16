package com.project.tlogger;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return true;
    }
}