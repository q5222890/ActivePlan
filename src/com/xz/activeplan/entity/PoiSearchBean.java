package com.xz.activeplan.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/7/18.
 */
public class PoiSearchBean implements Serializable{

    private String  addresss;

    public PoiSearchBean(String addresss, int imageid) {
        this.addresss = addresss;
    }

    public PoiSearchBean() {
    }


    public String getAddresss() {
        return addresss;
    }

    public void setAddresss(String addresss) {
        this.addresss = addresss;
    }


}
