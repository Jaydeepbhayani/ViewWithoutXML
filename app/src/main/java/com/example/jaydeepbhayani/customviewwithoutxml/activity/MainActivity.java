package com.example.jaydeepbhayani.customviewwithoutxml.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.LinearLayout;

/**
 * Created by Jaydeep Bhayani on 13-01-2017.
 */

public class MainActivity extends AppCompatActivity {
    private LinearLayout ll;
    private SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ll = new LinearLayout(this);

        LinearLayout.LayoutParams dimesions = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(dimesions);
        ll.setGravity(Gravity.CENTER);
        ll.setOrientation(LinearLayout.VERTICAL);

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        initActivity();

        setContentView(ll);
    }

    private void initActivity() {
        String Uname = sharedPreferences.getString("username", "");
        String Pwd = sharedPreferences.getString("password", "");

        if (Uname.equals("") && Pwd.equals("")) {
            Intent i = new Intent(this, LoginActivity.class);
            finish();
            startActivity(i);
        } else {
            Intent i = new Intent(this, ProfileActivity.class);
            finish();
            startActivity(i);
        }
    }
}
