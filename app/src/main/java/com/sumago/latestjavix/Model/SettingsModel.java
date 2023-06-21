package com.sumago.latestjavix.Model;

public class SettingsModel {

    Integer iv_logo;
    String tv_settings;

    public SettingsModel(Integer iv_logo, String tv_settings) {
        this.iv_logo = iv_logo;
        this.tv_settings = tv_settings;
    }

    public Integer getIv_logo() {
        return iv_logo;
    }

    public void setIv_logo(Integer iv_logo) {
        this.iv_logo = iv_logo;
    }

    public String getTv_settings() {
        return tv_settings;
    }

    public void setTv_settings(String tv_settings) {
        this.tv_settings = tv_settings;
    }
}
