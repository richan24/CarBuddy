package com.trackerbuddy.carassist;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class EntryPoint extends Activity {

    private TextView mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_point);
        mText = (TextView) findViewById(R.id.text);
    }

    public void sayHi(View view){
        mText.setText("Hello world");
    }
}
