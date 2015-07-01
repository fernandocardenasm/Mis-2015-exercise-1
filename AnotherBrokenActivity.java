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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class AnotherBrokenActivity extends Activity {

    private TextView mHttpText;
    private ProgressBar mProgressBar;
    private EditText mBrokenText;
    private RadioGroup mRadioGroup;
    private Button mButtonConnect;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_broken);

        Intent intent = getIntent();

        if (intent != null) {
            String message = intent.getStringExtra("EXTRA_MESSAGE");
            Toast.makeText(this, "Welcome " + message, Toast.LENGTH_SHORT).show();
        }

        mBrokenText = (EditText) findViewById(R.id.brokenTextView);
        mHttpText = (TextView) findViewById(R.id.httpText);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mButtonConnect = (Button) findViewById(R.id.buttonConnect);
        mImageView = (ImageView) findViewById(R.id.imageView);

        mProgressBar.setVisibility(View.INVISIBLE);

        //enable Javascript

        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = mRadioGroup.getCheckedRadioButtonId();
                if (radioId == R.id.radioButtonPlainText){
                    try {
                        fetchHTML(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (radioId == R.id.radioButtonImage){
                    fetchImage();
                }
                else if (radioId == R.id.radioButtonWebView){
                    fetchWebView();
                }
            }
        });
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

        mHttpText.setVisibility(View.VISIBLE);
        mImageView.setVisibility(View.INVISIBLE);

        if (URLUtil.isValidUrl(mBrokenText.getText().toString())) {

            mProgressBar.setVisibility(View.VISIBLE);
            mHttpText.setText("Loading...");

            if (isNetworkAvailable()) {
                //Toast.makeText(this, "The network is available!",Toast.LENGTH_SHORT).show();

                //Taken from okhttp open source library
                //http://square.github.io/okhttp/

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
                                    Toast.makeText(AnotherBrokenActivity.this, "The HTTP was successful", Toast.LENGTH_LONG).show();
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

        } else {
            Toast.makeText(AnotherBrokenActivity.this, "The URL is not valid. Please type a new one.", Toast.LENGTH_SHORT).show();
            mHttpText.setText("The response will appear here");
        }
    }

    public void fetchImage() {

        mHttpText.setVisibility(View.INVISIBLE);
        mImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        if (URLUtil.isValidUrl(mBrokenText.getText().toString())) {
            if (isNetworkAvailable()) {

                //Picasso documentation
                //http://square.github.io/picasso/

                Picasso.with(AnotherBrokenActivity.this).load(mBrokenText.getText().toString()).into(mImageView
                        ,new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AnotherBrokenActivity.this, "The HTTP was successful", Toast.LENGTH_LONG).show();
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });

                    }

                    @Override
                    public void onError() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AnotherBrokenActivity.this, "The HTTP request is not an image.", Toast.LENGTH_LONG).show();
                                mProgressBar.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
                });
            }
            else{
                Toast.makeText(this, "We are sorry. The network is NOT available!", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
        else{
            Toast.makeText(AnotherBrokenActivity.this, "The URL is not valid. Please type a new one.", Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.INVISIBLE);
        }

    }

    private void fetchWebView(){
        mProgressBar.setVisibility(View.VISIBLE);
        if (URLUtil.isValidUrl(mBrokenText.getText().toString())) {
            if(isNetworkAvailable()){
                mProgressBar.setVisibility(View.INVISIBLE);
                Intent intent1 = new Intent(AnotherBrokenActivity.this, WebViewActivity.class);
                intent1.putExtra("URL",mBrokenText.getText().toString());
                startActivity(intent1);
            }
            else{
                Toast.makeText(this, "We are sorry. The network is NOT available!", Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        }
        else{
            Toast.makeText(AnotherBrokenActivity.this, "The URL is not valid. Please type a new one.", Toast.LENGTH_SHORT).show();
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    //Determines when the network connection is available

    //Mainly taken from a project of teamtreehouse.com named "Storm"

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }



}
