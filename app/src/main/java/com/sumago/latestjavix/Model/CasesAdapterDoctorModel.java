package com.sumago.latestjavix.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class CasesAdapterDoctorModel {


   String _id = "";
   String status = "";
   String severity_bp = "";
   String severity_spo2 = "";
   String severity_temperature = "";
   String severity_pulse = "";
   String severity_bmi = "";
   String severity_respiratory_rate = "";
   String severity = "";
   String citizenId = "";
   String notes = "";
   String doctorId = "";
   String screenerId = "";
   String height = "";
   String weight = "";
   String bmi = "";
   String bpsys = "";
   String bpdia = "";
   String arm = "";
   String spo2 = "";
   String caseId = "";
   String pulse = "";
   String respiratory_rate = "";
   String temperature = "";
   String referDocId = "";
   String createdAt = "";
   String updatedAt = "";
   String __v = "";


   public CasesAdapterDoctorModel(JSONObject jsonObject) {
      try {
         this._id = jsonObject.getString("_id");
         this.status = jsonObject.getString("status");
         this.severity_bp = jsonObject.getString("severity_bp");
         this.severity_spo2 = jsonObject.getString("severity_spo2");
         this.severity_temperature = jsonObject.getString("severity_temperature");
         this.severity_pulse = jsonObject.getString("severity_pulse");
         this.severity_bmi = jsonObject.getString("severity_bmi");
         this.severity_respiratory_rate = jsonObject.getString("severity_respiratory_rate");
         this.severity = jsonObject.getString("severity");
         this.citizenId = jsonObject.getString("citizenId");
         this.notes = jsonObject.getString("notes");
         this.doctorId = jsonObject.getString("doctorId");
         this.screenerId = jsonObject.getString("screenerId");
         this.height = jsonObject.getString("height");
         this.weight = jsonObject.getString("weight");
         this.bmi = jsonObject.getString("bmi");
         this.bpsys = jsonObject.getString("bpsys");
         this.bpdia = jsonObject.getString("bpdia");
         this.arm = jsonObject.getString("arm");
         this.spo2 = jsonObject.getString("spo2");
         this.caseId = jsonObject.getString("caseId");
         this.pulse = jsonObject.getString("pulse");
         this.respiratory_rate = jsonObject.getString("respiratory_rate");
         this.temperature = jsonObject.getString("temperature");
         this.referDocId = jsonObject.getString("referDocId");
         this.createdAt = jsonObject.getString("createdAt");
         this.updatedAt = jsonObject.getString("updatedAt");
         this.__v = jsonObject.getString("__v");

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

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public String getSeverity_bp() {
      return severity_bp;
   }

   public void setSeverity_bp(String severity_bp) {
      this.severity_bp = severity_bp;
   }

   public String getSeverity_spo2() {
      return severity_spo2;
   }

   public void setSeverity_spo2(String severity_spo2) {
      this.severity_spo2 = severity_spo2;
   }

   public String getSeverity_temperature() {
      return severity_temperature;
   }

   public void setSeverity_temperature(String severity_temperature) {
      this.severity_temperature = severity_temperature;
   }

   public String getSeverity_pulse() {
      return severity_pulse;
   }

   public void setSeverity_pulse(String severity_pulse) {
      this.severity_pulse = severity_pulse;
   }

   public String getSeverity_bmi() {
      return severity_bmi;
   }

   public void setSeverity_bmi(String severity_bmi) {
      this.severity_bmi = severity_bmi;
   }

   public String getSeverity_respiratory_rate() {
      return severity_respiratory_rate;
   }

   public void setSeverity_respiratory_rate(String severity_respiratory_rate) {
      this.severity_respiratory_rate = severity_respiratory_rate;
   }

   public String getSeverity() {
      return severity;
   }

   public void setSeverity(String severity) {
      this.severity = severity;
   }

   public String getCitizenId() {
      return citizenId;
   }

   public void setCitizenId(String citizenId) {
      this.citizenId = citizenId;
   }

   public String getNotes() {
      return notes;
   }

   public void setNotes(String notes) {
      this.notes = notes;
   }

   public String getDoctorId() {
      return doctorId;
   }

   public void setDoctorId(String doctorId) {
      this.doctorId = doctorId;
   }

   public String getScreenerId() {
      return screenerId;
   }

   public void setScreenerId(String screenerId) {
      this.screenerId = screenerId;
   }

   public String getHeight() {
      return height;
   }

   public void setHeight(String height) {
      this.height = height;
   }

   public String getWeight() {
      return weight;
   }

   public void setWeight(String weight) {
      this.weight = weight;
   }

   public String getBmi() {
      return bmi;
   }

   public void setBmi(String bmi) {
      this.bmi = bmi;
   }

   public String getBpsys() {
      return bpsys;
   }

   public void setBpsys(String bpsys) {
      this.bpsys = bpsys;
   }

   public String getBpdia() {
      return bpdia;
   }

   public void setBpdia(String bpdia) {
      this.bpdia = bpdia;
   }

   public String getArm() {
      return arm;
   }

   public void setArm(String arm) {
      this.arm = arm;
   }

   public String getSpo2() {
      return spo2;
   }

   public void setSpo2(String spo2) {
      this.spo2 = spo2;
   }

   public String getCaseId() {
      return caseId;
   }

   public void setCaseId(String caseId) {
      this.caseId = caseId;
   }

   public String getPulse() {
      return pulse;
   }

   public void setPulse(String pulse) {
      this.pulse = pulse;
   }

   public String getRespiratory_rate() {
      return respiratory_rate;
   }

   public void setRespiratory_rate(String respiratory_rate) {
      this.respiratory_rate = respiratory_rate;
   }

   public String getTemperature() {
      return temperature;
   }

   public void setTemperature(String temperature) {
      this.temperature = temperature;
   }

   public String getReferDocId() {
      return referDocId;
   }

   public void setReferDocId(String referDocId) {
      this.referDocId = referDocId;
   }

   public String getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(String createdAt) {
      this.createdAt = createdAt;
   }

   public String getUpdatedAt() {
      return updatedAt;
   }

   public void setUpdatedAt(String updatedAt) {
      this.updatedAt = updatedAt;
   }

   public String get__v() {
      return __v;
   }

   public void set__v(String __v) {
      this.__v = __v;
   }
}
