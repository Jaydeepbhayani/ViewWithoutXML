package com.example.jaydeepbhayani.customviewwithoutxml.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaydeepbhayani.customviewwithoutxml.R;
import com.example.jaydeepbhayani.customviewwithoutxml.services.LoginDataBaseAdapter;

/**
 * Created by Jaydeep Bhayani on 12-01-2017.
 */

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    LoginDataBaseAdapter loginDataBaseAdapter;
    private ImageView ivImage;
    private LinearLayout ll, ll2;
    private TextView tvUserName, tvError;
    private Button btnSignOut, btnChangePassword;
    private EditText etOldPassword, etNewPassword;
    private AlertDialog dialog;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs", USERNAME = "username";
    public static final int iSignOut = 1, iChangePassword = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        initViews();
        setDimesions();
        setProperties();

        btnChangePassword.setId(iChangePassword);
        btnSignOut.setId(iSignOut);

        btnChangePassword.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);

        setContentView(ll);
    }

    private void setProperties() {
        ivImage.setImageResource(R.drawable.ic_action_face_unlock);

        sharedpreferences = getBaseContext().getSharedPreferences(MyPREFERENCES, 0);
        String loadUsername = sharedpreferences.getString(USERNAME, null);
        tvUserName.setText(getString(R.string.str_welcome) + loadUsername);
        tvUserName.setTextSize(21);
        tvUserName.setTextColor(Color.rgb(255, 64, 129));
        tvUserName.setGravity(Gravity.CENTER);

        btnChangePassword.setText(getString(R.string.str_change_pwd));
        btnChangePassword.setTextColor(Color.WHITE);
        btnChangePassword.setBackgroundColor(Color.rgb(63, 81, 181));

        btnSignOut.setText(getString(R.string.str_logout));
        btnSignOut.setTextColor(Color.WHITE);
        btnSignOut.setBackgroundColor(Color.rgb(63, 81, 181));
    }

    private void setDimesions() {
        LinearLayout.LayoutParams dimesions = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(dimesions);
        ll.setGravity(Gravity.CENTER);
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams viewDimensions = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewDimensions.setMargins(80, 30, 80, 0);

        ll.addView(ivImage, viewDimensions);
        ll.addView(tvUserName, viewDimensions);
        ll.addView(btnChangePassword, viewDimensions);
        ll.addView(btnSignOut, viewDimensions);
    }

    private void initViews() {
        ll = new LinearLayout(this);
        ivImage = new ImageView(this);
        tvUserName = new TextView(this);
        btnSignOut = new Button(this);
        btnChangePassword = new Button(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close The Database
        loginDataBaseAdapter.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case iSignOut:
                signOut();
                break;
            case iChangePassword:
                showDialog();
                break;
        }
    }

    private void signOut() {
        SharedPreferences settings = ProfileActivity.this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
        Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
        finish();
        startActivity(i);
    }

    private void showDialog() {
        ll2 = new LinearLayout(this);
        etOldPassword = new EditText(this);
        etNewPassword = new EditText(this);
        tvError = new TextView(this);

        ll2.setGravity(Gravity.CENTER);
        ll2.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams viewDimensions2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewDimensions2.setMargins(40, 30, 40, 0);

        etOldPassword.setHint("Old Password");
        etOldPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_key), null);
        etNewPassword.setHint("New Password");
        etNewPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_key), null);
        tvError.setGravity(Gravity.CENTER_VERTICAL);

        ll2.addView(etOldPassword, viewDimensions2);
        ll2.addView(etNewPassword, viewDimensions2);
        ll2.addView(tvError, viewDimensions2);

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

        builder.setView(ll2);
        builder.setTitle("Change Password");
        builder.setPositiveButton("Change Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = etOldPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                sharedpreferences = getBaseContext().getSharedPreferences(MyPREFERENCES, 0);
                String loadUsername = sharedpreferences.getString(USERNAME, null);
                // fetch the Password form database for respective user name
                String storedPassword = loginDataBaseAdapter.getSinlgeEntry(loadUsername);
                System.out.println("Value :::" + storedPassword);
                if (!(oldPassword.isEmpty() && newPassword.isEmpty())) {
                    if (oldPassword.matches(storedPassword)) {
                        // update the Password form database when user is loggedIn
                        loginDataBaseAdapter.updateEntry(loadUsername, newPassword);
                        Toast.makeText(ProfileActivity.this, "Successfully Changed", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        tvError.setText(getString(R.string.str_pwd_not_match));
                        tvError.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_error), null, null, null);
                    }
                } else {
                    tvError.setText(getString(R.string.str_empty));
                    tvError.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_error), null, null, null);
                }
            }
        });
    }
}
