package com.mabao.util.express;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mabao.util.CustomJsonDateDeserializer;

import java.util.Date;
import java.util.List;

public class PackInfo {
    private String message;
    private String nu;
    private String ischeck;
    private String com;
    private Date updatetime;
    @JsonIgnore
    private String status;
    private String state;
    private String condition;
    private List<PackDetails> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public List<PackDetails> getData() {
        return data;
    }

    public void setData(List<PackDetails> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PackInfo{" +
                "message='" + message + '\'' +
                ", nu='" + nu + '\'' +
                ", ischeck='" + ischeck + '\'' +
                ", com='" + com + '\'' +
                ", updatetime=" + updatetime +
                ", status='" + status + '\'' +
                ", state='" + state + '\'' +
                ", condition='" + condition + '\'' +
                ", data=" + data +
                '}';
    }
}
