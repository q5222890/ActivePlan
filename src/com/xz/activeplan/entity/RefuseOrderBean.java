package com.xz.activeplan.entity;

import java.io.Serializable;

/**退款
 * Created by Administrator on 2016/7/19.
 */
public class RefuseOrderBean implements Serializable {
    private long apply_time;
    private int inv_amount;
    private int inv_id;
    private String reason;
    private int ref_amount;
    private int ref_id;
    private String ref_num;
    private String ref_reason;
    private String remark;
    private int status;
    private int tea_id;
    private int usr_id;
    public long getApply_time() {
        return apply_time;
    }
    public void setApply_time(long apply_time) {
        this.apply_time = apply_time;
    }
    public int getInv_amount() {
        return inv_amount;
    }

    public void setInv_amount(int inv_amount) {
        this.inv_amount = inv_amount;
    }

    public int getInv_id() {
        return inv_id;
    }

    public void setInv_id(int inv_id) {
        this.inv_id = inv_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getRef_amount() {
        return ref_amount;
    }

    public void setRef_amount(int ref_amount) {
        this.ref_amount = ref_amount;
    }

    public int getRef_id() {
        return ref_id;
    }

    public void setRef_id(int ref_id) {
        this.ref_id = ref_id;
    }

    public String getRef_num() {
        return ref_num;
    }

    public void setRef_num(String ref_num) {
        this.ref_num = ref_num;
    }

    public String getRef_reason() {
        return ref_reason;
    }

    public void setRef_reason(String ref_reason) {
        this.ref_reason = ref_reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTea_id() {
        return tea_id;
    }

    public void setTea_id(int tea_id) {
        this.tea_id = tea_id;
    }

    public int getUsr_id() {
        return usr_id;
    }

    public void setUsr_id(int usr_id) {
        this.usr_id = usr_id;
    }
    @Override
    public String toString() {
        return "RefuseOrderBean{" +
                "apply_time=" + apply_time +
                ", inv_amount=" + inv_amount +
                ", inv_id=" + inv_id +
                ", reason='" + reason + '\'' +
                ", ref_amount=" + ref_amount +
                ", ref_id=" + ref_id +
                ", ref_num='" + ref_num + '\'' +
                ", ref_reason='" + ref_reason + '\'' +
                ", remark='" + remark + '\'' +
                ", status=" + status +
                ", tea_id=" + tea_id +
                ", usr_id=" + usr_id +
                '}';
    }
}
