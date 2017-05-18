package com.example.raul.oilnote.Activitys;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.example.raul.oilnote.Adapters.ListWorkerAdapter;
import com.example.raul.oilnote.Objects.Worker;
import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.raul.oilnote.Utils.Codification.decodeBase64;
import static com.example.raul.oilnote.Utils.GlobalVars.*;

public class ListWorkerActivity extends BaseActivity {

    protected ListView listViewWorkers;
    protected ListWorkerAdapter listWorkerAdapter;
    protected List<Worker> listWorkers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_worker);

        listViewWorkers = (ListView) findViewById(R.id.list_worker);
        listWorkers     = new ArrayList<>();

        new ListWorkersTask().execute();
    }

    class ListWorkersTask extends AsyncTask<Void, Void, JSONArray>{

        private HashMap<String, String> parametrosPost = new HashMap<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(ListWorkerActivity.this,getString(R.string.loading));
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            try {
                // Consulto los trabajadores que tiene el usuario:
                parametrosPost.put("ins_sql", "SELECT * FROM workers WHERE user_cod = '" + USER_COD + "' ORDER BY worker_name");
                jSONArray = connection.sendRequest(BASE_URL_READ, parametrosPost);
                if (jSONArray != null) {
                    return jSONArray;
                }
            } catch (Exception e) {
                e.getMessage();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);

            onStopProgressDialog();

            try {
                if(jsonArray != null){
                    System.out.println("Foto encode: "+mapWorkersList(jsonArray).get(0).getWorkerPhoto());
                    // Inicializo el adaptador:
                    listWorkerAdapter = new ListWorkerAdapter(ListWorkerActivity.this, mapWorkersList(jsonArray));
                    // Relacionando la lista con el adaptador:
                    listViewWorkers.setAdapter(listWorkerAdapter);


                }else{
                    // Poner una lista avisando de que no tiene trabajadores:
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Mapeo los dato del JSONArray que recivo en una lista de trabajadores para montar el adaptador:
    public List<Worker> mapWorkersList(JSONArray jsonArray) throws JSONException {

        List<Worker> lw = new ArrayList<>();

        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length() ; i++ ){

                Worker worker = new Worker();

                worker.setWorkerCod(jsonArray.getJSONObject(i).getString("worker_cod"));
                worker.setWorkerName(jsonArray.getJSONObject(i).getString("worker_name"));
                worker.setWorkerNick(jsonArray.getJSONObject(i).getString("worker_nick"));
                worker.setWorkerPhone(jsonArray.getJSONObject(i).getString("worker_phone"));
                worker.setWorkerJob(jsonArray.getJSONObject(i).getInt("worker_job"));
                worker.setWorkerPhoto(jsonArray.getJSONObject(i).getString("worker_photo"));

                lw.add(worker);
            }
        }

        return lw;
    }


}
