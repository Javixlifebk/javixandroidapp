package com.sumago.latestjavix.Util;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

public class WriteOfflineFile {
    public static final String TAG="SRIT_HCL";
    public static void createLog_Login(String vendor,String varData){
        Log.e(TAG,vendor +"=>"+ varData);
        WritetoLog( vendor +"=>"+ varData);

    }
    public static void WritetoLog( String LogMessage){
        Log.e(TAG,"Directory Creating");
        if (!LogMessage.isEmpty()) {
            File umlogroot = new File(Environment.getExternalStorageDirectory(), "javix_logs");
            if (!umlogroot.exists()) {
                umlogroot.mkdir();
                Log.e(TAG,"Directory Created");
            }
            try {
                File umlogfile = new File(umlogroot, "javix_logs_citizenlist");
                FileWriter writer=null;
                if(!umlogfile.exists()) {
                    umlogfile.createNewFile();

                }
                writer = new FileWriter(umlogfile,true);
                writer.append("LINE=="+LogMessage+"\r\n");
                writer.flush();
                writer.close();
            }
            catch (Exception e) { }
        }
    }

}

