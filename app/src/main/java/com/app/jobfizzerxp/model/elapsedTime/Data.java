package com.app.jobfizzerxp.model.elapsedTime;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("y")
    @Expose
    private Integer y;
    @SerializedName("m")
    @Expose
    private Integer m;
    @SerializedName("d")
    @Expose
    private Integer d;
    @SerializedName("h")
    @Expose
    private Integer h;
    @SerializedName("i")
    @Expose
    private Integer i;
    @SerializedName("s")
    @Expose
    private Integer s;
    @SerializedName("weekday")
    @Expose
    private Integer weekday;
    @SerializedName("weekday_behavior")
    @Expose
    private Integer weekdayBehavior;
    @SerializedName("first_last_day_of")
    @Expose
    private Integer firstLastDayOf;
    @SerializedName("invert")
    @Expose
    private Integer invert;
    @SerializedName("days")
    @Expose
    private Integer days;
    @SerializedName("special_type")
    @Expose
    private Integer specialType;
    @SerializedName("special_amount")
    @Expose
    private Integer specialAmount;
    @SerializedName("have_weekday_relative")
    @Expose
    private Integer haveWeekdayRelative;
    @SerializedName("have_special_relative")
    @Expose
    private Integer haveSpecialRelative;
    private final static long serialVersionUID = 952484581539514568L;

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getM() {
        return m;
    }

    public void setM(Integer m) {
        this.m = m;
    }

    public Integer getD() {
        return d;
    }

    public void setD(Integer d) {
        this.d = d;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public Integer getS() {
        return s;
    }

    public void setS(Integer s) {
        this.s = s;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Integer getWeekdayBehavior() {
        return weekdayBehavior;
    }

    public void setWeekdayBehavior(Integer weekdayBehavior) {
        this.weekdayBehavior = weekdayBehavior;
    }

    public Integer getFirstLastDayOf() {
        return firstLastDayOf;
    }

    public void setFirstLastDayOf(Integer firstLastDayOf) {
        this.firstLastDayOf = firstLastDayOf;
    }

    public Integer getInvert() {
        return invert;
    }

    public void setInvert(Integer invert) {
        this.invert = invert;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public Integer getSpecialType() {
        return specialType;
    }

    public void setSpecialType(Integer specialType) {
        this.specialType = specialType;
    }

    public Integer getSpecialAmount() {
        return specialAmount;
    }

    public void setSpecialAmount(Integer specialAmount) {
        this.specialAmount = specialAmount;
    }

    public Integer getHaveWeekdayRelative() {
        return haveWeekdayRelative;
    }

    public void setHaveWeekdayRelative(Integer haveWeekdayRelative) {
        this.haveWeekdayRelative = haveWeekdayRelative;
    }

    public Integer getHaveSpecialRelative() {
        return haveSpecialRelative;
    }

    public void setHaveSpecialRelative(Integer haveSpecialRelative) {
        this.haveSpecialRelative = haveSpecialRelative;
    }

}