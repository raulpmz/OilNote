package com.example.raul.oilnote.Utils;

/**
 * Created by ptmarketing02 on 12/05/2017.
 */

public class HttpQuery {

    public static final String BASE_URL_READ   = "http://iesayala.ddns.net/raulpmz/imprime.php?ins_sq=";
    public static final String BASE_URL_WRITE  = "http://iesayala.ddns.net/raulpmz/escribe.php?ins_sq=";

    public static String getLoginQuery(String user, String password){
        return BASE_URL_READ + "select * from users where user_name ='" + user + "' and user_password = '"+password+"'";
    }
}
