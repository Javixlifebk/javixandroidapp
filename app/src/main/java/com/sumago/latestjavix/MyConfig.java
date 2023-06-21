package com.sumago.latestjavix;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyConfig {
    public static Context CONTEXT = null;
    //public static String URL_SERVER="http://143.244.136.145:3001";
    public static String URL_SERVER="http://143.244.136.145:3010"; // recent live base URL


//    public static String URL_SERVER = "http://192.168.1.195:3010"; // Local IP for Testing
//    public static String URL_SERVER="http://159.65.148.197:3001"; // Staging URL for Testing


    //    public static String URL_local_server ="http://159.65.148.197:3001/api/";    // staging url
    public static String URL_local_server = "http://143.244.136.145:3010/api/";    // staging url
//    public static String URL_local_server ="http://192.168.1.56:3001/api/";    // Local server IP


    public static int USER_ROLE = 0;
    public static String URL_LOGIN = URL_SERVER + "/api/auth/login";
    public static String URL_NGO_LIST = URL_SERVER + "/api/ngo/ngoList";
    public static String URL_NGO_ById = URL_SERVER + "/api/ngo/ngoById";
    public static String URL_SCREENER_LIST = URL_SERVER + "/api/ngo/screenerList";
    public static String URL_DOCTOR_LIST = URL_SERVER + "/api/doctor/doctorList";
    public static String URL_PHARMACY_LIST = URL_SERVER + "/api/pharmacy/pharmacyList";
    // http://139.59.59.3.1:3001/api/auth/login
    // add form
    public static String URL_ADD_NGO = URL_SERVER + "/api/ngo/addprofile";
    public static String URL_ADD_SCREENER = URL_SERVER + "/api/ngo/screener/addprofile";
    public static String URL_ADD_DOCTOR = URL_SERVER + "/api/doctor/addprofile";
    public static String URL_ADD_CITIZEN = URL_SERVER + "/api/citizen/addprofile";
    public static String URL_LIST_CITIZEN = URL_SERVER + "/api/citizen/citizenList";
    public static String URL_LIST_CITIZEN_LAST_100 = URL_SERVER + "/api/citizen/citizenList100";
    public static String URL_ADD_CASE = URL_SERVER + "/api/screening/addCase";
    public static String URL_LIST_CASE = URL_SERVER + "/api/screening/getCaseDetails"; // color code
    public static String URL_PICK_CASE = URL_SERVER + "/api/screening/updateCase";

    public static String URL_ADD_PRESCRIPTION = URL_SERVER + "/api/doctor/addprescription";
    public static String URL_VIEW_PRESCRIPTION = URL_SERVER + "/api/doctor/prescriptionList";

    public static String URL_USER_LIST = URL_SERVER + "/api/auth/authlist";
    public static String URL_USER_APROVING = URL_SERVER + "/api/auth/approve";

    public static String URL_DOCTOR_PROFILE = URL_SERVER + "/api/doctor/doctorById";
    public static String URL_NGO_PROFILE = URL_SERVER + "/api/ngo/ngoById";
    public static String URL_SCREENER_PROFILE = URL_SERVER + "/api/ngo/screenerById";
    public static String URL_GETLIST = URL_SERVER + "/api/graph/getlist";

    public static String URL_PARENTCHILD = URL_SERVER + "/api/stub/getstub";
    public static String URL_ADDISSUE = URL_SERVER + "/api/issues/addIssue";
    public static String URL_LISTISSUE = URL_SERVER + "/api/issues/issuesByUser";
    public static String URL_ALLISSUE = URL_SERVER + "/api/issues/issuesAll";
    public static String URL_DETAILSCREENING = URL_SERVER + "/api/screening/addDetailCase";
    public static String URL_GETDETAILSCREENING = URL_SERVER + "/api/screening/getCaseDetailsList";
    public static String URL_UPDATEPHOTO = URL_SERVER + "/api/misc/updatePhoto";
    public static String URL_UPDATESIGNATURE = URL_SERVER + "/api/doctor/updateSignature";
    public static String URL_GLUCOSETEST = URL_SERVER + "/api/labtest/addBloodGlucoseTest";
    public static String URL_LIPIDTEST = URL_SERVER + "/api/labtest/addLipidPanelTest";
    public static String URL_ADDEYETEST = URL_SERVER + "/api/labtest/addEyeTest";
    public static String URL_ADDHEMOGLOBIN = URL_SERVER + "/api/labtest/addHemoglobinTest";
    public static String URL_ADDSICKLECELL = URL_SERVER + "/api/labtest/addSickleCell";
    public static String URL_ADDTHALASSEMIA = URL_SERVER + "/api/labtest/addThalassemia";
    public static String URL_ADDLUNGTEST = URL_SERVER + "/api/labtest/addLungTest";
    public static String URL_ENCOUNTERS = URL_SERVER + "/api/screening/getEncounters";
    public static String URL_VISUALEXAM = URL_SERVER + "/api/labtest/addVisualExam";
    public static String URL_ADDURINE = URL_SERVER + "/api/labtest/addUrineTest";
    public static String URL_ADDHEART = URL_SERVER + "/api/labtest/addHeartTest";
    public static String URL_SYMPTOMS = URL_SERVER + "/api/screening/addsymptoms";
    public static String URL_GENERALSURVEY = URL_SERVER + "/api/generalsurvey/addGeneralSurvey";
    public static String URL_GENERALSURVEYLIST = URL_SERVER + "/api/generalsurvey/GeneralSurveyList";
    public static String URL_HEALTHSURVEY = URL_SERVER + "/api/healthsurvey/addHealthSurvey";
    public static String URL_SOCIOECONOMIC = URL_SERVER + "/api/socioeconomicsurvey/addSocioEconomicSurvey";
    public static String URL_ADDACTORS = URL_SERVER + "/api/auth/register";


//    public static String URL_PRESCRIBED_LIST = URL_SERVER + "/api/citizen/CitizenPrescribe";
    public static String URL_PRESCRIBED_LIST = URL_SERVER + "/api/screening/getCaseDetails";


    public static String URL_CITIZEN_LIST_PAGINATION = URL_SERVER + "/api/citizen/citizenListPagination";


    public static String abc;


    public static Hashtable<String, String> roleHash = new Hashtable<String, String>();
    public static String SCREENER_CODE = "2";
    public static String DOCTOR_CODE = "1";


    public static String URL_Refer_by_Sevika = URL_local_server + "citizen/citizenReferList";


    public static void loadRoleHash() {
        roleHash.put("1", "Doctor");
        roleHash.put("2", "Screener");
        roleHash.put("3", "NGO");
        roleHash.put("4", "Pharmacy");
        roleHash.put("5", "Doctor");
        roleHash.put("6", "");
        roleHash.put("7", "");
        roleHash.put("8", "");
        roleHash.put("9", "");
        roleHash.put("91", "Javix Admin");
        roleHash.put("92", "Doctor Admin");
        roleHash.put("93", "Super Admin");
        roleHash.put("21", "Sevika");

    }

    public static String getRoleName(String id) {
        try {
            return roleHash.get(id);
        } catch (Exception ee) {
            return "";
        }
    }


}

//  MenuItem
class JMenuItem {
    String menuName = "";
    String href = "#";
    String icon = "";
    Class activity = null;
    public static Hashtable<String, ArrayList<JMenuItem>> menuList = new Hashtable<String, ArrayList<JMenuItem>>();

    public static void clearAll() {
        JMenuItem.clearAll();
    }

    public static void addMenu(String roleName, JMenuItem item) {
        ArrayList<JMenuItem> arrayList = JMenuItem.menuList.get(roleName);
        if (arrayList == null) {
            arrayList = new ArrayList<JMenuItem>();
            menuList.put(roleName, arrayList);
        }
        arrayList.add(item);
    }

    public static ArrayList<JMenuItem> getMyMenus(String roleName) {
        ArrayList<JMenuItem> arrayList = JMenuItem.menuList.get(roleName);
        return (arrayList);
    }

    public static JMenuItem getItems() {
        return (new JMenuItem());
    }
}

// Input Data Validation

class ErrBox {
    private View v;
    private String type;
    private String err;
    private boolean required = false;
    private static ArrayList<ErrBox> arrayList = new ArrayList<ErrBox>();
    static String FIRST_ERR_MSG = "";

    static void clearAll() {
        arrayList.clear();
    }

    static void add(EditText v, String type, String err, boolean required) {
        ErrBox e = new ErrBox();
        e.v = v;
        e.type = type;
        e.err = err;
        e.required = required;
        ErrBox.arrayList.add(e);
    }

    static void add(Spinner v, String type, String err, boolean required) {
        ErrBox e = new ErrBox();
        e.v = v;
        e.type = type;
        e.err = err;
        e.required = required;
        ErrBox.arrayList.add(e);
    }

    public static int errorsStatus() {
        ErrBox.FIRST_ERR_MSG = "";
        int size = ErrBox.arrayList.size();
        int FR = 0;
        for (int i = 0; i < size; i++) {
            ErrBox e = ErrBox.arrayList.get(i);
            int R = e.getStatus(e);
            if (R == 1) ErrBox.FIRST_ERR_MSG = e.err;
            FR = FR + R;
        }
        if (FR == 0) FIRST_ERR_MSG = "Something went wrong !.";
        return (FR);
    }

    private static int getStatus(ErrBox e) {
        try {
            String value = "";
            if (e.v instanceof EditText)
                value = ((EditText) e.v).getText().toString().trim();
            else if (e.v instanceof Spinner)
                value = String.valueOf(((Spinner) e.v).getSelectedItem());

            if (e.required == true && value.length() == 0) {
                return (1);
            }
            if (value.length() >= 1) {
                if (e.type.equals("TEXT")) {
                    //Toast.makeText(MyConfig.CONTEXT,"TEXT=",Toast.LENGTH_SHORT).show();
                    Pattern mPattern = Pattern.compile("^[a-zA-Z0-9\\.-_ ]*$");
                    Matcher matcher = mPattern.matcher(value);
                    if (!matcher.find()) {
                        return (1);
                        //Toast.makeText(MyConfig.CONTEXT,"Not MAtches TEXT",Toast.LENGTH_SHORT).show();return(1);
                    } else {
                        return (0);
                    }
                } else if (e.type.equals("TEXTONLY")) {
                    Pattern mPattern = Pattern.compile("^[a-zA-Z ]*$");
                    Matcher matcher = mPattern.matcher(value);
                    if (!matcher.find()) {
                        return (1);
                    } else {
                        return (0);
                    }
                } else if (e.type.equals("NUMBER")) {
                    Pattern mPattern = Pattern.compile("^[0-9\\.]*$");
                    Matcher matcher = mPattern.matcher(value);
                    if (!matcher.find()) {
                        return (1);
                    } else {
                        return (0);
                    }
                } else if (e.type.equals("MIXED")) {
                    return (0);
                } else if (e.type.equals("MOBILE")) {
                    Pattern mPattern = Pattern.compile("[1-9][0-9]{9}");
                    Matcher matcher = mPattern.matcher(value);
                    if (!matcher.find()) {
                        return (1);
                    } else {
                        return (0);
                    }
                } else if (e.type.equals("EMAIL")) {
                    Pattern mPattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                    Matcher matcher = mPattern.matcher(value);
                    if (!matcher.find()) {
                        return (1);
                    } else {
                        return (0);
                    }
                }
            }
            return (0);
        } catch (Exception ee) {
            Toast.makeText(MyConfig.CONTEXT, "ERR=" + ee, Toast.LENGTH_SHORT).show();
            return (1);
        }
    }
}

class QuestionaryX {
    static boolean isLoaded = false;
    static Hashtable<String, ArrayList<QuestionaryY>> quest = new Hashtable<String, ArrayList<QuestionaryY>>();

    public static void clearAllSelectedList() {
        Enumeration<String> keys = QuestionaryX.quest.keys();
        while (keys.hasMoreElements()) {
            String keyName = keys.nextElement().toString();
            ArrayList<QuestionaryY> arrayList = quest.get(keyName);
            int size = arrayList.size();
            for (int i = 0; i < size; i++) {
                QuestionaryY questionaryY = arrayList.get(i);
                questionaryY.selectedValues.clear();
            }
        }
    }

    public static void add(String nameX, ArrayList<QuestionaryY> recsN) {
        QuestionaryX.quest.put(nameX, recsN);
    }

}

class QuestionaryY {
    String childText = "";
    ArrayList<String> selectedValues = new ArrayList<String>();
    ArrayList<String> hintList = new ArrayList<String>();
}

