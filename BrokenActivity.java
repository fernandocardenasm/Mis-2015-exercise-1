package com.example.usuario.mis_2015_exercise_1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class BrokenActivity extends Activity {

    private EditText mAuntEdith;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broken);
        mAuntEdith = (EditText)findViewById(R.id.editText);
        mButton = (Button) findViewById(R.id.firstButton);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.broken, menu);
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

    public void brokenFunction(View view){

        //I was once, perhaps, possibly a functioning function
        if (mAuntEdith.getText().toString().equals("Timmy")){
            System.out.println("Timmy fixed a bug!");
        }



        System.out.println("If this appears in your console, you fixed a bug.");
        Intent intent = new Intent(this,AnotherBrokenActivity.class);
        String message = mAuntEdith.getText().toString();
        intent.putExtra("EXTRA_MESSAGE",message);
        startActivity(intent);

    }
}

