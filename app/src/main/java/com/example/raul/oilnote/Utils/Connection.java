package com.example.raul.oilnote.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by raul on 6/5/17.
 *
 * Clase Conexión.
 */

public class Connection {

    private HttpURLConnection conn;
    public static final int CONNECTION_TIMEOUT = 10 * 20000;

    public JSONObject sendRequest(String link) throws JSONException {
        JSONArray jArray = null;
        JSONObject jobject=null;

        try {
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(CONNECTION_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.connect();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    result.append(line);
                }

                try {
                    jobject = new JSONObject(result.toString());
                    return jobject;
                } catch (JSONException e) {
                    //Log.e("ERROR => ", "Error convirtiendo los datos a JSON : " + e.toString());
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (MalformedURLException e) {
        } catch (IOException e) {
            e.getMessage();
        }
        return jobject;
    }

    public static boolean compruebaConexion(Context context) {
        boolean connected = false;

        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }
}
