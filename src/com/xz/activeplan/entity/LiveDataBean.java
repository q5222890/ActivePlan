package com.xz.activeplan.entity;import java.io.Serializable;/** * 直播数据 * *    channelname":"test", *    "ifpublic":0, *    "deadline":1451491200, *    "ifrecord":1, *    "recordname":"test", *    "sizelimit":1048576, *    "recordposition":0, *    "callback":"http://tom.test.com"}' */public class LiveDataBean implements Serializable{    private  String channelname;    private  int ifpublic;    private  long deadline;    private  int ifrecord;    private  String recordname;    private  int sizelimit;    private  int recordposition;    private  String callback;    public LiveDataBean() {    }    public LiveDataBean(String channelname, int ifpublic, long deadline, int ifrecord, String recordname, int sizelimit, int recordposition, String callback) {        this.channelname = channelname;        this.ifpublic = ifpublic;        this.deadline = deadline;        this.ifrecord = ifrecord;        this.recordname = recordname;        this.sizelimit = sizelimit;        this.recordposition = recordposition;        this.callback = callback;    }    public String getChannelname() {        return channelname;    }    public void setChannelname(String channelname) {        this.channelname = channelname;    }    public int getIfpublic() {        return ifpublic;    }    public void setIfpublic(int ifpublic) {        this.ifpublic = ifpublic;    }    public long getDeadline() {        return deadline;    }    public void setDeadline(long deadline) {        this.deadline = deadline;    }    public int getIfrecord() {        return ifrecord;    }    public void setIfrecord(int ifrecord) {        this.ifrecord = ifrecord;    }    public int getSizelimit() {        return sizelimit;    }    public void setSizelimit(int sizelimit) {        this.sizelimit = sizelimit;    }    public String getRecordname() {        return recordname;    }    public void setRecordname(String recordname) {        this.recordname = recordname;    }    public int getRecordposition() {        return recordposition;    }    public void setRecordposition(int recordposition) {        this.recordposition = recordposition;    }    public String getCallback() {        return callback;    }    public void setCallback(String callback) {        this.callback = callback;    }}