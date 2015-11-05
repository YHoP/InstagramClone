/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtUsername;
    EditText txtPassword;
    TextView txtSubmit;
    Button btnSubmit;
    ImageView logo;
    RelativeLayout mRelativeLayout;

    Boolean signUpModeActive;

    TextView.OnEditorActionListener actionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_NULL
                    && event.getAction() == KeyEvent.ACTION_DOWN) {
                signUpOrLogin(v);
            }
            return true;
        }
    };


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.txtSubmit) {
            Log.i("AppInfo", "Change Signup Mode");
            if(signUpModeActive) {
                signUpModeActive = false;
                txtSubmit.setText("Sign Up");
                btnSubmit.setText("Log In");
            } else {
                signUpModeActive = true;
                txtSubmit.setText("Log In");
                btnSubmit.setText("Sign Up");
            }
        } else if (v.getId() == R.id.imgLogo || v.getId() == R.id.relativeLayout){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

    }

    public void signUpOrLogin(View view) {

        if (signUpModeActive) {
            ParseUser user = new ParseUser();
            user.setUsername(String.valueOf(txtUsername.getText()));
            user.setPassword(String.valueOf(txtPassword.getText()));
            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Log.i("AppInfo", "Signup Successful");
                        showUserList();
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();
                        Log.i("AppInfo", "Signup Failed");
                        e.printStackTrace();
                    }
                }
            });
        } else {

            ParseUser.logInInBackground(String.valueOf(txtUsername.getText()), String.valueOf(txtPassword.getText()), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null){
                        Log.i ("AppInfo", "Log in successful");
                        showUserList();
                    } else {
                        Toast.makeText(getApplicationContext(), e.getMessage().substring(e.getMessage().indexOf(" ")), Toast.LENGTH_LONG).show();
                    }

                }
            });
        }
    }

    public void showUserList(){
        Intent intent = new Intent(getApplicationContext(), UserList.class);
        startActivity(intent);
    }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

      if(ParseUser.getCurrentUser() != null){
          showUserList();
      }


      signUpModeActive = true;

      txtUsername = (EditText) findViewById(R.id.txtUsername);
      txtPassword = (EditText) findViewById(R.id.txtPassword);
      txtSubmit = (TextView) findViewById(R.id.txtSubmit);
      btnSubmit = (Button) findViewById(R.id.btnSubmit);
      logo = (ImageView) findViewById(R.id.imgLogo);
      mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

      txtSubmit.setOnClickListener(this);
      logo.setOnClickListener(this);
      mRelativeLayout.setOnClickListener(this);


      txtPassword.setOnEditorActionListener(actionListener);

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }



}
