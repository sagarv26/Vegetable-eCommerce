package sweinc.com.buyvegitables.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import sweinc.com.buyvegitables.R;


public class Show_Web extends AppCompatActivity {

    ActionBar toolbar;
    WebView SimpleWebID;
    static String title = null;
    static String ex = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_webview);
        toolbar = getSupportActionBar();

        Intent intent = getIntent();

        title = String.valueOf(intent.getStringExtra("keyTitle"));
        ex = String.valueOf(intent.getStringExtra("keyFile"));


        SimpleWebID = findViewById(R.id.SimpleWebID);
        SimpleWebID.getSettings().setBuiltInZoomControls(true);
        SimpleWebID.getSettings().setDisplayZoomControls(false);
        SimpleWebID.loadUrl(ex);
        toolbar.setTitle(title);


    }


}
