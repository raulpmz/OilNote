package com.example.raul.oilnote.Activitys;


import android.os.AsyncTask;
import android.os.Bundle;

import com.example.raul.oilnote.R;

import org.json.JSONArray;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_READ;

public class ListWorkerActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_list_worker);

    }

    class ListWorkersTask extends AsyncTask<Void, Void, JSONArray>{

        private JSONArray jsonArray;
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
                parametrosPost.put("ins_sql", "select * from workers where user_cod = '" + user + "'");
                jsonArray = connection.sendRequest(BASE_URL_READ, parametrosPost);

                if (jsonArray != null) {
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

            try {

                if(jsonArray != null){

                }

            } catch (Exception e) {
                e.getMessage();
            }
        }
    }
}
