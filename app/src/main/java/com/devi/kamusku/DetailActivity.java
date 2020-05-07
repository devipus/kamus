package com.devi.kamusku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    public static final String DETAIL_KATA = "kata";
    public static final String DETAIL_ARTI = "arti";

    TextView tvKata, tvArti;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tvKata = findViewById(R.id.tv_kata);
        tvArti = findViewById(R.id.tv_arti);

        tvKata.setText(getIntent().getStringExtra(DETAIL_KATA));
        tvArti.setText(getIntent().getStringExtra(DETAIL_ARTI));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;

                default:
                    return super.onOptionsItemSelected(item);
        }
    }
}
