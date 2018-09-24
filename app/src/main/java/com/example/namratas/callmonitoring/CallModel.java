package com.example.namratas.callmonitoring;

import java.util.Date;

/**
 * Created by namrata.s on 21/02/2018.
 */

public class CallModel {

    private String contact_image;
    private String person_name;
    private String phone_No;
    private String inoutcoming;
    private String duration;
    private String day;
    private String forward_image;
    private String icon;
    private String sim_slot;

    public CallModel(String contact_image, String person_name, String phone_No, String inoutcoming, String duration, String day, String forward_image, String icon, String sim_slot) {
        this.contact_image = contact_image;
        this.person_name = person_name;
        this.phone_No = phone_No;
        this.inoutcoming = inoutcoming;
        this.duration = duration;
        this.day = day;
        this.forward_image = forward_image;
        this.icon = icon;
        this.sim_slot = sim_slot;
    }

    public CallModel() {
    }

    public String getContact_image() {
        return contact_image;
    }

    public void setContact_image(String contact_image) {
        this.contact_image = contact_image;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPhone_No() {
        return phone_No;
    }

    public void setPhone_No(String phone_No) {
        this.phone_No = phone_No;
    }

    public String getInoutcoming() {
        return inoutcoming;
    }

    public void setInoutcoming(String inoutcoming) {
        this.inoutcoming = inoutcoming;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getForward_image() {
        return forward_image;
    }

    public void setForward_image(String forward_image) {
        this.forward_image = forward_image;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSim_slot() {
        return sim_slot;
    }

    public void setSim_slot(String sim_slot) {
        this.sim_slot = sim_slot;
    }

    @Override
    public String toString() {
        return "CallModel{" +
                "contact_image='" + contact_image + '\'' +
                ", person_name='" + person_name + '\'' +
                ", phone_No='" + phone_No + '\'' +
                ", inoutcoming='" + inoutcoming + '\'' +
                ", duration='" + duration + '\'' +
                ", day='" + day + '\'' +
                ", forward_image='" + forward_image + '\'' +
                ", icon='" + icon + '\'' +
                ", sim_slot='" + sim_slot + '\'' +
                '}';
    }
}