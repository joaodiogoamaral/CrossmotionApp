package com.example.joao.crossmotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class LoginActivity extends AppCompatActivity {

    private static final String PASS_KEY = "PASSWORD";
    private static final String USER_KEY = "USERNAME";
    private static final String EXTRA_PASSWORD = "PASS";
    private static final String EXTRA_USERNAME = "USERNAME";
    private static final String TAG = "LoginActivity";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static TextView passText,userText;
    private static TextView passBox,userBox;
    private Button loginButton;
    private static  String username,password;
    private static final int STATUS_OK=200,STATUS_NOK=401;


    private static String user,pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {











        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        username = sharedPreferences.getString(USER_KEY,"");
        password = sharedPreferences.getString(PASS_KEY,"");
        Log.e(TAG,username);
        Log.e(TAG,password);

        if((username != "" || password !="" ))
        {


            loginServer(username,password);
            Intent intent = new Intent();
            setResult(RESULT_OK);

            finish();
        }


        passText = findViewById(R.id.userText);
        userText = findViewById(R.id.passText);
        userBox = findViewById(R.id.userTextBox);
        passBox = findViewById(R.id.passTextBox);

        passText.setVisibility(View.VISIBLE);
        userText.setVisibility(View.VISIBLE);
        userBox.setVisibility(View.VISIBLE);
        passBox.setVisibility(View.VISIBLE);

        loginButton = findViewById(R.id.buttonLogin);
        loginButton.setVisibility(View.VISIBLE);


    }




    public  void checkLogin(View view)
    {



        user = userBox.getText().toString();
        pass = passBox.getText().toString();















        if(loginServer(user,pass)) {


            Intent intent = new Intent(this, MainActivity.class);
            setResult(RESULT_OK, intent);
            sharedPreferences = this.getPreferences(Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();

            editor.putString(PASS_KEY,pass);
            editor.putString(USER_KEY,user);
            editor.apply();

            finish();
        }else {

            TextView errorMsg = findViewById(R.id.LoginErrorMsg);
            errorMsg.setVisibility(View.VISIBLE);
        }





    }

    public static boolean loginServer(final String user, final String pass)
    {

        boolean loginResult;
        class PostLogin extends AsyncTask<String,Void,Integer>
        {


            @Override
            protected Integer doInBackground(String...params)
            {
                return post2Server(user,pass);








            }

            @Override
            protected void onPostExecute(Integer result)
            {
                super.onPostExecute(result);




                Log.e("UPLOAD: ",Integer.toString(result));
            }


            public int post2Server(String user,String pass) {


                String upLoadServerUri = Constants.URLCheckUsers; //Check which django server ip address


                //Log.e(TAG,"Uploading"+videoName);

                int serverResponseCode = -1;
                HttpsURLConnection conn = null;
                OutputStream dos = null;
                DataInputStream inStream = null;
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";




                try { // open a URL connection to the Server

                    URL url = new URL(upLoadServerUri);


                    conn = (HttpsURLConnection) url.openConnection(); // Open a HTTP  connection to  the URL
                    conn.setHostnameVerifier(new HostnameVerifier() {
                                                 @Override
                                                 public boolean verify(String hostname, SSLSession session) {
                                                     return hostname.equals(Constants.serverIP);
                                                 }
                                             }

                    );
                    conn.setDoInput(true); // Allow Inputs
                    conn.setDoOutput(true); // Allow Outputs
                    conn.setUseCaches(false); // Don't use a Cached Copy
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "form-data");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded" + boundary);
                    conn.setRequestProperty("username",user);
                    conn.setRequestProperty("pass",pass);





                    serverResponseCode = conn.getResponseCode();
                    String serverResponseMessage = conn.getResponseMessage();

                    Log.i("LOGIN to server", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);
                    // close streams
                   // dos.flush();
                    //dos.close();
                } catch (MalformedURLException ex) {
                    ex.printStackTrace();
                    Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
                } catch (Exception e) {
                    e.printStackTrace();
                }
//this block will give the response of upload link



                Log.e("HTTP_RESPONSE:",Integer.toString( serverResponseCode));



                return serverResponseCode;  // like 200 (Ok)


            // end upLoad2Server
        }




    }


        PostLogin pl=new PostLogin();
        pl.execute (user,pass);
        try {
            return pl.get() == STATUS_OK;
        }catch (InterruptedException e)
        {
            e.printStackTrace();
        }catch (ExecutionException e)
        {
            e.printStackTrace();
        }

        Log.e(TAG,"ERROR GETTING RESULT");

        return false;



}






}
