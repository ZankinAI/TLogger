package com.project.tlogger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.project.tlogger.ui.history.HistoryFragment.onSomeEventListener;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.project.tlogger.databinding.ActivityMainBinding;
import com.project.tlogger.ui.temperature.TemperatureFragment;

public class MainActivity extends AppCompatActivity implements onSomeEventListener{

    private ActivityMainBinding binding;
    private final static String TAG = "ContentFragment";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        LayoutInflater inflater = (LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.layout_action_bar,null);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        bar.setCustomView(v);
        /*//bar.setDisplayOptions(ActionBar.DISPLAY_USE_LOGO);
        bar.setIcon(getDrawable(R.drawable.logo));
        //bar.setDisplayShowHomeEnabled(true);
        bar.setHomeButtonEnabled(true);
        bar.setTitle("TempSense");
        bar.setDisplayShowTitleEnabled(true);
        bar.show();*/
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(

                R.id.navigation_temperature, R.id.navigation_history,
                R.id.navigation_settings, R.id.navigation_about)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }


    @Override
    public void someEvent(String s){
        Log.d(TAG, s);
        /*FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        TemperatureFragment temperatureFragment = new TemperatureFragment();
        ft.replace(R.id.nav_host_fragment_activity_main, temperatureFragment, "temperatureFragment");
        ft.addToBackStack(null);
        ft.commit();*/
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);

    }

}