package com.sumago.latestjavix.WebService;

import android.content.Context;
import android.content.SharedPreferences;

public class AppConfig
{
    private Context context;
    private SharedPreferences sharedPreferences;

    public AppConfig(Context context)
    {
        this.context=context;
        sharedPreferences=context.getSharedPreferences("pref_file_key",Context.MODE_PRIVATE);
    }
    //Check login or not
    public  boolean isUserLogin()
    {
        return sharedPreferences.getBoolean("is_user_login",false);
    }

    public void updateUserLogin(boolean status)
    {
        Shared_Preferences.getPrefs(context.getApplicationContext(), Constant.USER_ID);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("is_user_login",status);
        editor.apply();
    }

    public String getNameofUser()
    {
        return sharedPreferences.getString("name_of_user","Unknow");
    }
}
