package com.mabao.util.alibaba.message;

import com.mabao.util.alibaba.message.smsBean.TextMessage;

public class PhoneBindMes extends TextMessage {

    public PhoneBindMes(){
        super.setTemplate("phoneBind");
    }

    private String code;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getContent() {
        return "{code:\'"+ this.getCode()+"\',name:\'"+ this.getName()+"\'}";
    }
}
