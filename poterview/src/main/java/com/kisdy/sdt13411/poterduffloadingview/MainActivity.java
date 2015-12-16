package com.kisdy.sdt13411.poterduffloadingview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kisdy.sdt13411.poterduffloadingview.view.PoterDuffLoadingView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG ="MainActivity" ;
    PoterDuffLoadingView loadingView;
    Button btnext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        btnext= (Button) findViewById(R.id.id_gonext);

        btnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ExpandListViewActivity.class);
                startActivity(intent);
            }
        });

    }


}
