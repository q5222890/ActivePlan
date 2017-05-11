package com.xz.activeplan.utils.observer;

import java.io.Serializable;

/**
 * 事件bean
 * 
 */
public class EventBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private int type; // 事件类型
    private int clickId; // 点击的view的id
    private Object obj;

    public EventBean(int type) {
        this.type = type;
    }

    public EventBean(int type, int clickId) {
        this.type = type;
        this.clickId = clickId;
    }

    public EventBean(int type, int clickId, Object obj) {
        this.type = type;
        this.clickId = clickId;
        this.obj = obj;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getClickId() {
        return clickId;
    }

    public void setClickId(int clickId) {
        this.clickId = clickId;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

}
