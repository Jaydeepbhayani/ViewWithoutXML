package com.example.jaydeepbhayani.customviewwithoutxml.activity;

import android.content.Intent;
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

public class SignUPActivity extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout ll;
    private EditText etUserName, etPassword, etConfirmPassword;
    private Button btnCreateAccount;
    private TextView tvTitle, tvSignIn;
    public static final int iSignUp = 4, iSignIn = 5;
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get Instance  of Database Adapter
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();

        initViews();
        setViews();
        setProperties();

        btnCreateAccount.setId(iSignUp);
        tvSignIn.setId(iSignIn);

        btnCreateAccount.setOnClickListener(this);
        tvSignIn.setOnClickListener(this);

        setContentView(ll);
    }

    private void setProperties() {
        tvTitle.setText("MoBack Task");
        tvTitle.setTextSize(30);
        tvTitle.setGravity(Gravity.CENTER);

        tvSignIn.setText("Already Registered ? Login Now !");
        tvSignIn.setTextColor(Color.rgb(255, 64, 129));
        tvSignIn.setGravity(Gravity.CENTER);

        etUserName.setHint("User Name");
        etUserName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_user), null);

        etPassword.setHint("Password");
        etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_key), null);

        etConfirmPassword.setHint("Confirm Password");
        etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        etConfirmPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.ic_key), null);

        btnCreateAccount.setText("Register");
        btnCreateAccount.setTextColor(Color.WHITE);
        btnCreateAccount.setBackgroundColor(Color.rgb(63, 81, 181));
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
        ll.addView(etConfirmPassword, viewDimensions);
        ll.addView(btnCreateAccount, viewDimensions);
        ll.addView(tvSignIn);
    }

    private void initViews() {
        ll = new LinearLayout(this);
        tvTitle = new TextView(this);
        tvSignIn = new TextView(this);
        etPassword = new EditText(this);
        etConfirmPassword = new EditText(this);
        etUserName = new EditText(this);
        btnCreateAccount = new Button(this);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        loginDataBaseAdapter.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case iSignIn:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case iSignUp:
                registerProcess();
                break;
        }
    }

    private void registerProcess() {
        String userName = etUserName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // check if any of the fields are vaccant
        if (userName.equals("") || password.equals("") || confirmPassword.equals("")) {
            Toast.makeText(this, "Field Vaccant", Toast.LENGTH_LONG).show();
            return;
        }
        // check if both password matches
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password does not match", Toast.LENGTH_LONG).show();
            return;
        } else {
            // Save the Data in Database
            loginDataBaseAdapter.insertEntry(userName, password);
            Toast.makeText(getApplicationContext(), "Account Successfully Created ", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, LoginActivity.class);
            i.putExtra("username", etUserName.getText().toString().trim());
            i.putExtra("password", etPassword.getText().toString().trim());
            finish();
            startActivity(i);
        }
    }
}
