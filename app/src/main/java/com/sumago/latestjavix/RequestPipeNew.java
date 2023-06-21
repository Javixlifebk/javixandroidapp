package com.sumago.latestjavix;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class RequestPipeNew {

    private static final String TAG = "_msg";

    public RequestPipeNew() {


    }


    public String requestForm(String _url, HashMap<String, String> paramsHash)
    {   String responseText="";
        try {
            URL url = new URL(_url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            //conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            String params=getDataString(paramsHash);
            Log.e(TAG, "_targetUrl : " +_url);
            Log.e(TAG, "parameters : " +params);
            OutputStream out=conn.getOutputStream();
            out.write(params.getBytes(),0,params.length());
            int statusCode = conn.getResponseCode();
            responseText="ResCode:"+statusCode;
            Log.e(TAG, "else part : " +responseText);
            InputStream in=null;
            if(statusCode==HttpURLConnection.HTTP_OK)
            {in=conn.getInputStream();
            }
            else
            {in=conn.getErrorStream();}

            responseText=readStream(in);

            out.close();
            in.close();
            Log.e(TAG, "else part : " +responseText);
        }catch (Exception ee){responseText="{status:-1,message:'"+ee.toString()+"',data:[]}";}
        return(responseText);
    }
    private String getDataString(HashMap<String, String> params) throws  Exception{
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else result.append("&");
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        return result.toString();
    }
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            //e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

}
