package com.example.raul.oilnote.Utils;

import android.os.AsyncTask;

import org.json.JSONObject;

/**
 * Created by ptmarketing02 on 12/05/2017.
 */

public class Async {

    class LoginTask extends AsyncTask<String, String, JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
    }
}
