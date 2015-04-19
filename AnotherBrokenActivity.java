package com.example.usuario.mis_2015_exercise_1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.MalformedURLException;

public class AnotherBrokenActivity extends Activity {

    private TextView mHttpText;
    private ProgressBar mProgressBar;
    private EditText mBrokenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_broken);

        Intent intent = getIntent();

        if (intent != null){
            String message = intent.getStringExtra("EXTRA_MESSAGE");
            Toast.makeText(this, "Welcome " + message, Toast.LENGTH_SHORT).show();
        }

        mBrokenText = (EditText) findViewById(R.id.brokenTextView);
        mHttpText = (TextView) findViewById(R.id.httpText);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.another_broken, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fetchHTML(View view) throws IOException {



        if (URLUtil.isValidUrl(mBrokenText.getText().toString())) {

            mProgressBar.setVisibility(View.VISIBLE);
            mHttpText.setText("Loading...");

            if (isNetworkAvailable()) {
                //Toast.makeText(this, "The network is available!",Toast.LENGTH_SHORT).show();

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(mBrokenText.getText().toString())
                        .build();

                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AnotherBrokenActivity.this, "The HTTP was NOT successful", Toast.LENGTH_SHORT).show();
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        try {
                            final String httpString = response.body().string();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mHttpText.setText(httpString);
                                    Toast.makeText(AnotherBrokenActivity.this, "The HTTP was successful", Toast.LENGTH_SHORT).show();
                                    mProgressBar.setVisibility(View.INVISIBLE);
                                }
                            });
                        } catch (IOException e) {
                            Toast.makeText(AnotherBrokenActivity.this, "Please try to send the URL again.", Toast.LENGTH_SHORT).show();
                            Log.e("AnotherBrokenActivity", "Exception caught: ", e);
                        }
                        //
                    }
                });


            } else {
                Toast.makeText(this, "We are sorry. The network is NOT available!", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
                mHttpText.setText("The response will appear here");
            }

        }
        else{
            Toast.makeText(AnotherBrokenActivity.this, "The URL is not valid. Please type a new one.", Toast.LENGTH_SHORT).show();
        }
    }

    //Determines when the network connection is available

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()){
            isAvailable = true;
        }
        return isAvailable;
    }

    private boolean isURLReal(String urlString) throws MalformedURLException {
        boolean validator = URLUtil.isValidUrl(urlString);
        return validator;
    }
}
