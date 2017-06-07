package com.example.jaydeepbhayani.customviewwithoutxml.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaydeepbhayani.customviewwithoutxml.R;
import com.example.jaydeepbhayani.customviewwithoutxml.services.LoginDataBaseAdapter;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    LoginDataBaseAdapter loginDataBaseAdapter;
    private Button btnSignIn;
    private LinearLayout ll;
    private TextView tvTitle, tvSignUp;
    private EditText etPassword, etUserName;
    private SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final int iSignUp = 4, iSignIn = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create a instance of SQLite Database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        initViews();
        setViews();

        Intent i = getIntent();
        String saUsername = i.getStringExtra("username");
        String saPassword = i.getStringExtra("password");

        etUserName.setText(saUsername);
        etPassword.setText(saPassword);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        setProperties();

        btnSignIn.setId(iSignIn);
        tvSignUp.setId(iSignUp);

        btnSignIn.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);

        setContentView(ll);
    }

    private void setProperties() {
        tvTitle.setText(getString(R.string.title));
        tvTitle.setTextSize(30);
        tvTitle.setGravity(Gravity.CENTER);

        tvSignUp.setText(getString(R.string.str_signup));
        tvSignUp.setTextColor(Color.rgb(255, 64, 129));
        tvSignUp.setGravity(Gravity.CENTER);

        etUserName.setHint("User Name");
        etUserName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_user), null);

        etPassword.setHint("Password");
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_key), null);

        btnSignIn.setText(getString(R.string.str_submit));
        btnSignIn.setTextColor(Color.WHITE);
        btnSignIn.setBackgroundColor(Color.rgb(63, 81, 181));
    }

    private void setViews() {
        LinearLayout.LayoutParams dimesions = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.setLayoutParams(dimesions);
        ll.setGravity(Gravity.CENTER);
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams viewDimensions = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        viewDimensions.setMargins(80, 0, 80, 40);

        ll.addView(tvTitle, viewDimensions);
        ll.addView(etUserName, viewDimensions);
        ll.addView(etPassword, viewDimensions);
        ll.addView(btnSignIn, viewDimensions);
        ll.addView(tvSignUp);
    }

    private void initViews() {
        ll = new LinearLayout(this);
        tvTitle = new TextView(this);
        tvSignUp = new TextView(this);
        etPassword = new EditText(this);
        etUserName = new EditText(this);
        btnSignIn = new Button(this);
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
            case iSignUp:
                /// Create Intent for SignUpActivity  and Start The Activity
                Intent intentSignUP = new Intent(getApplicationContext(), SignUPActivity.class);
                startActivity(intentSignUP);
                break;
            case iSignIn:
                singInProcess();
                break;
        }
    }

    private void singInProcess() {
        SharedPreferences.Editor editor = sharedpreferences.edit();

        // get The User name and Password
        String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // fetch the Password form database for respective user name
        String storedPassword = loginDataBaseAdapter.getSinlgeEntry(userName);

        // check if the Stored password matches with  Password entered by user
        if (password.equals(storedPassword)) {
            Toast.makeText(this, "Congrats: Login Successfull", Toast.LENGTH_SHORT).show();
            editor.putString("username", userName);
            editor.putString("password", password);
            editor.commit();
            Intent intent = new Intent(this, ProfileActivity.class);
            intent.putExtra("username", etUserName.getText().toString().trim());
            intent.putExtra("password", etPassword.getText().toString().trim());
            finish();
            startActivity(intent);
        } else {
            Toast.makeText(this, "User Name or Password does not match", Toast.LENGTH_SHORT).show();
        }
    }
}