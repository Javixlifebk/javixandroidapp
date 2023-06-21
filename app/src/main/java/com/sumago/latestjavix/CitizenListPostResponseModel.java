package com.sumago.latestjavix;

public class CitizenListPostResponseModel {

    String pageNo;
    String size;


    public CitizenListPostResponseModel() {

    }

    public CitizenListPostResponseModel(String pageNo, String size) {
        this.pageNo = pageNo;
        this.size = size;
    }


    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
}
