package com.sumago.latestjavix.Model;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.json.JSONException;
import org.json.JSONObject;


public class SevikaReferredToDrModel {


    String _id = "";
    String pstatus = "";
    String isInstant = "";
    String isUnrefer = "";
    String citizenId = "";
    String sex = "";
    String email = "";
    String javixId = "";
    String citizenLoginId = "";
    String aadhaar = "";
    String screenerId = "";
    String createdAt = "";
    String fullname = "";
    String screenerfullname = "";
    String mobile = "";

//String _id = "";
//String _id = "";
//String _id = "";
//String _id = "";
//String _id = "";
//String _id = "";
//String _id = "";
//String _id = "";
//String _id = "";
//String _id = "";


    public SevikaReferredToDrModel() {
    }

    public SevikaReferredToDrModel(JSONObject jsonObject) {
        try {
            this._id = jsonObject.getString("_id");
            this.pstatus = jsonObject.getString("pstatus");
            this.isInstant = jsonObject.getString("isInstant");
            this.isUnrefer = jsonObject.getString("isUnrefer");
            this.citizenId = jsonObject.getString("citizenId");
            this.sex = jsonObject.getString("sex");
            // this.email = jsonObject.getString("email");
            this.javixId = jsonObject.getString("javixId");
            this.citizenLoginId = jsonObject.getString("citizenLoginId");
            this.aadhaar = jsonObject.getString("aadhaar");
            this.screenerId = jsonObject.getString("screenerId");
            this.createdAt = jsonObject.getString("createdAt");
            this.fullname = jsonObject.getString("fullname");
            this.screenerfullname = jsonObject.getString("screenerfullname");
            this.mobile = jsonObject.getString("mobile");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    public String getIsInstant() {
        return isInstant;
    }

    public void setIsInstant(String isInstant) {
        this.isInstant = isInstant;
    }

    public String getIsUnrefer() {
        return isUnrefer;
    }

    public void setIsUnrefer(String isUnrefer) {
        this.isUnrefer = isUnrefer;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJavixId() {
        return javixId;
    }

    public void setJavixId(String javixId) {
        this.javixId = javixId;
    }

    public String getCitizenLoginId() {
        return citizenLoginId;
    }

    public void setCitizenLoginId(String citizenLoginId) {
        this.citizenLoginId = citizenLoginId;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getScreenerId() {
        return screenerId;
    }

    public void setScreenerId(String screenerId) {
        this.screenerId = screenerId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public String getScreenerfullname() {
        return screenerfullname;
    }

    public void setScreenerfullname(String screenerfullname) {
        this.screenerfullname = screenerfullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
