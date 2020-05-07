package com.devi.kamusku;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.devi.kamusku.helper.KamusHelper;
import com.devi.kamusku.model.KamusModel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PreloadActivity extends AppCompatActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preload);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        new LoadData().execute();
    }

    // menjalankan preload

    private class LoadData extends AsyncTask<Void, Integer, Void>{

        final String TAG = LoadData.class.getSimpleName();

        KamusHelper kamusHelper;
        AppPreference appPreference;

        double progress;
        double maxprogress = 100;

        @Override
        protected void onPreExecute(){
            kamusHelper = new KamusHelper(PreloadActivity.this);
            appPreference = new AppPreference(PreloadActivity.this);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //panggil preference first run

            Boolean firstRun = appPreference.getFirstRun();

            //jika first run true maka melakukan proses pre load
            //jika first run false maka akan langsung menuju home

            if (firstRun) {
                //load raw data dari file txt ke dalam array model kamus

                ArrayList<KamusModel> kamusIndo = preLoadRaw("indo");
                ArrayList<KamusModel> kamusEng = preLoadRaw("eng");

                kamusHelper.open();

                progress = 30;
                publishProgress((int) progress);

                Double progressMaxInsert = 80.0;
                Double progressDiff = (progressMaxInsert - progress) / (kamusIndo.size() + kamusEng.size());

                //query insert yg transactional
                // indo to eng

                kamusHelper.beginTransaction();

                try {

                    for (KamusModel model : kamusIndo) {
                        kamusHelper.insertTransaction(model, "indo");
                    }
                    kamusHelper.setTransactionSuccess();

                } catch (Exception e){
                    //jika gagal maka do nothing
                    Log.e(TAG, "doInBackground: Exception");
                }
                //jika semua proses telah di set success maka akan di commit ke database

                kamusHelper.endTransaction();
                progress += progressDiff;
                publishProgress((int) progress);

                // eng to indo

                kamusHelper.beginTransaction();

                try {

                    for (KamusModel model : kamusEng) {
                        kamusHelper.insertTransaction(model, "eng");

                    }
                    kamusHelper.setTransactionSuccess();

                } catch (Exception e){
                    //jika gagal maka do nothing
                    Log.e(TAG, "doInBackground: Exception");
                }
                //jika semua proses telah di set success maka akan di commit ke database

                kamusHelper.endTransaction();
                progress += progressDiff;
                publishProgress((int) progress);

                kamusHelper.close();

                appPreference.setFirstRun(false);
                publishProgress((int) maxprogress);
            } else {
                try {
                    synchronized (this) {
                        this.wait(2000);
                        publishProgress(50);
                        this.wait(2000);
                        publishProgress((int) maxprogress);
                    }
                } catch (Exception e) {

                }

            }

            return null;
        }
        //update prosesnya
        @Override
        protected  void onProgressUpdate(Integer...values){
            progressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void result){
            Intent i = new Intent(PreloadActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public ArrayList<KamusModel> preLoadRaw(String options) {
        int data;

        if (options == "indo"){
            data = R.raw.indonesia_english;
        } else {
            data = R.raw.english_indonesia;
        }

        ArrayList<KamusModel> kamusModels = new ArrayList<>();
        String line;
        BufferedReader reader;

        try {
            Resources res = getResources();
            InputStream raw_dict = res.openRawResource(data);

            reader = new BufferedReader(new InputStreamReader(raw_dict));
            int count = 0;
            do {
                line = reader.readLine();
                String[] splitstr = line.split("\t");

                KamusModel kamusModel;

                kamusModel = new KamusModel(splitstr[0], splitstr[1]);
                kamusModels.add(kamusModel);
                count++;
            } while (line != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return kamusModels;
    }
}
