package com.example.raul.oilnote.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase Conexión.
 */

public class Connection {

    // Variables:

    private HttpURLConnection conn;
    public static final int CONNECTION_TIMEOUT = 10 * 20000;

    // Método para conectar con el servidor y obetener los datos de la consulta en JSON:

    public JSONArray sendRequest(String link, HashMap<String, String> values) throws JSONException {
        JSONArray jArray = null;
        try {
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(CONNECTION_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            if (values != null) {
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osWriter = new OutputStreamWriter(os, "UTF-8");
                BufferedWriter writer = new BufferedWriter(osWriter);
                writer.write(getPostData(values));
                writer.flush();
                writer.close();
                os.close();
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                InputStreamReader isReader = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isReader);
                String result = "";
                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();

                try {

                    jArray = new JSONArray(result);
                    return jArray;

                } catch (JSONException e) {
                    Log.e("ERROR => ", "Error convirtiendo los datos a JSON : " + e.toString());
                    e.getMessage();
                    return null;
                }
            }
        }
        catch (MalformedURLException e) {}
        catch (IOException e) {}
        return jArray;

    }

    public JSONObject sendWrite(String link, HashMap<String, String> values) throws JSONException {

        JSONObject jobject = null;

        try {
            URL url = new URL(link);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(CONNECTION_TIMEOUT);
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.connect();

            if (values != null) {
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osWriter = new OutputStreamWriter(os, "UTF-8");
                BufferedWriter writer = new BufferedWriter(osWriter);
                writer.write(getPostData(values));
                writer.flush();
                writer.close();
                os.close();
            }

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                InputStreamReader isReader = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isReader);
                String result = "";
                String line = null;
                StringBuilder sb = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();

                try {
                    jobject = new JSONObject(result.toString());
                    return jobject;
                } catch (JSONException e) {
                    Log.e("JSONException", e.getMessage());
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

    public String getPostData(HashMap<String, String> values) {
        StringBuilder builder = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : values.entrySet()) {

            if (first) {
                first = false;
            }else{
                builder.append("&");
            }
            try {
                builder.append(URLEncoder.encode(entry.getKey(),"UTF-8"));
                builder.append("=");
                builder.append(URLEncoder.encode(entry.getValue(),"UTF-8"));
            }
            catch (UnsupportedEncodingException e) {}
        }
        return builder.toString();
    }

    // Comprueba el estado de las conexiones de nuestro dispositivo, con el fin de avisar al usuario para que las active :

    public static boolean checkConnection(Context context) {
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

    // Comprueba el estado del servidor al que nos conectamos para poder controlar los mensajes que se muestran al usuario:

    public static int stateConnection(String link){
        HttpURLConnection connection;

        try {
            URL url = new URL(link);
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(CONNECTION_TIMEOUT);
            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK ) return 1;
            else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) return 0;
            //else if()retu;

        } catch (MalformedURLException e) {
        } catch (IOException e) {
            e.getMessage();
        }
        return 0;

    }
}
