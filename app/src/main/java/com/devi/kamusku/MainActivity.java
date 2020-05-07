package com.devi.kamusku;

import android.database.SQLException;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.devi.kamusku.cari.CariAdapter;
import com.devi.kamusku.helper.KamusHelper;
import com.devi.kamusku.model.KamusModel;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MaterialSearchBar.OnSearchActionListener {

    RecyclerView recyclerView;
    MaterialSearchBar cari;

    private CariAdapter cariAdapter;
    private KamusHelper kamusHelper;
    private ArrayList<KamusModel> list = new ArrayList<>();

    String options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        cari = (MaterialSearchBar) findViewById(R.id.cari);
        cari.setOnSearchActionListener(this);

        kamusHelper = new KamusHelper(this);
        cariAdapter = new CariAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cariAdapter);

        options = "indo";
        loadData();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_indonesia) {
            options = "indo";
            loadData("");
        } else if (id == R.id.nav_english) {
            options =  "eng";
            loadData( "");

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadData(String cari) {
        try{
            kamusHelper.open();
            if (cari.isEmpty()){
                list = kamusHelper.getAllData(options);
            } else {
                list = kamusHelper.getDataByName(cari, options);
            }

            if (options == "indo") {
                getSupportActionBar().setSubtitle(getResources().getString(R.string.ind_to_eng));
            } else {
                getSupportActionBar().setSubtitle(getResources().getString(R.string.eng_to_ind));
            }
        }catch (SQLException e){
            e.printStackTrace();
        } finally {
            kamusHelper.close();
        }
        cariAdapter.replaceAll(list);
    }

    private void loadData(){
        loadData("");
    }

    @Override
    public void onSearchStateChanged(boolean enabled) {

    }

    @Override
    public void onSearchConfirmed(CharSequence text) {
        loadData(String.valueOf(text));
    }

    @Override
    public void onButtonClicked(int buttonCode) {

    }
}
