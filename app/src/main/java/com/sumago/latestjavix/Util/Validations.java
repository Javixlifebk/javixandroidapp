package com.sumago.latestjavix.Util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.EditText;

/**
 * Created by Rakesh on 12/04/2017.
 */

public class Validations {

    public final boolean isInternetOn(Context context) {


        // get Connectivity Manager object to check connection
        ConnectivityManager connec =
                (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {

            // if connected with internet

            //Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {

            //Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

   public boolean Text_Validator(String _type, EditText _text)
   {
       boolean _flag=false;

       switch (_type)
       {
           case "Mobile":
               if(_text.length()==10 ) {
                   String regexStr = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$";
                   if(_text.getText().toString().matches(regexStr)) {
                       if(!(_text.getText().toString().equalsIgnoreCase("9999999999")|| _text.getText().toString().equalsIgnoreCase("9999999999") || _text.getText().toString().equalsIgnoreCase("8888888888") || _text.getText().toString().equalsIgnoreCase("7777777777") || _text.getText().toString().equalsIgnoreCase("6666666666")))
                       _flag = true;
                   }
               }
               break;
           default:

       }

       return _flag;

   }

    public boolean Match_Validator(String _type, EditText _text1, EditText _text2)
    {
        boolean _flag=false;

        switch (_type)
        {
            case "KWH":
                if(_text1.getText().toString().equals(_text2.getText().toString()))
                    _flag=true;
                break;
            default:

        }

        return _flag;

    }
}
