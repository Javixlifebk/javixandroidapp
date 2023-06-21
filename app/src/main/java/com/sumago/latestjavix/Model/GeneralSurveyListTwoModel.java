package com.sumago.latestjavix.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class GeneralSurveyListTwoModel {

    String familyId;
    String noOfFamilyMembers;
    String nameHead;
    String ageHead;
    String NoOfAdultMales;
    String NoOfAdultFemales;
    String NoOfChildrenMales;
    String NoOfChildrenFemales;
    String CitizeID;


    public GeneralSurveyListTwoModel(JSONObject jsonObject) {
        try {
            this.familyId = jsonObject.getString("familyId");
            this.noOfFamilyMembers = jsonObject.getString("noOfFamilyMembers");
            this.nameHead = jsonObject.getString("nameHead");
            this.ageHead = jsonObject.getString("ageHead");
            NoOfAdultMales = jsonObject.getString("NoOfAdultMales");
            NoOfAdultFemales = jsonObject.getString("NoOfAdultFemales");
            NoOfChildrenMales = jsonObject.getString("NoOfChildrenMales");
            NoOfChildrenFemales = jsonObject.getString("NoOfChildrenFemales");
            CitizeID = jsonObject.getString("citizeID");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getNoOfFamilyMembers() {
        return noOfFamilyMembers;
    }

    public void setNoOfFamilyMembers(String noOfFamilyMembers) {
        this.noOfFamilyMembers = noOfFamilyMembers;
    }

    public String getNameHead() {
        return nameHead;
    }

    public void setNameHead(String nameHead) {
        this.nameHead = nameHead;
    }

    public String getAgeHead() {
        return ageHead;
    }

    public void setAgeHead(String ageHead) {
        this.ageHead = ageHead;
    }

    public String getNoOfAdultMales() {
        return NoOfAdultMales;
    }

    public void setNoOfAdultMales(String noOfAdultMales) {
        NoOfAdultMales = noOfAdultMales;
    }

    public String getNoOfAdultFemales() {
        return NoOfAdultFemales;
    }

    public void setNoOfAdultFemales(String noOfAdultFemales) {
        NoOfAdultFemales = noOfAdultFemales;
    }

    public String getNoOfChildrenMales() {
        return NoOfChildrenMales;
    }

    public void setNoOfChildrenMales(String noOfChildrenMales) {
        NoOfChildrenMales = noOfChildrenMales;
    }

    public String getNoOfChildrenFemales() {
        return NoOfChildrenFemales;
    }

    public void setNoOfChildrenFemales(String noOfChildrenFemales) {
        NoOfChildrenFemales = noOfChildrenFemales;
    }

    public String getCitizeID() {
        return CitizeID;
    }

    public void setCitizeID(String citizeID) {
        CitizeID = citizeID;
    }
}
