package owner.messedup.com.messedupowner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;



public class MessMenu implements Serializable{


    @SerializedName("messid")
    @Expose
    private String messid;

    @SerializedName("dayname")
    @Expose
    private String dayname;

    @SerializedName("Meal")
    @Expose
    private String meal;
    @SerializedName("Rice")
    @Expose
    private String rice;
    @SerializedName("VegieOne")
    @Expose
    private String vegieOne;
    @SerializedName("VegieTwo")
    @Expose
    private String vegieTwo;
    @SerializedName("VegieThree")
    @Expose
    private String vegieThree;
    @SerializedName("Roti")
    @Expose
    private String roti;
    @SerializedName("Special")
    @Expose
    private String special;
    @SerializedName("SpecialExtra")
    @Expose
    private String specialExtra;
    @SerializedName("Other")
    @Expose
    private String other;

    public String getMessid() {
        return messid;
    }

    public void setMessid(String messid) {
        this.messid = messid;
    }

    public String getDayname() {
        return dayname;
    }

    public void setDayname(String dayname) {
        this.dayname = dayname;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public String getRice() {
        return rice;
    }

    public void setRice(String rice) {
        this.rice = rice;
    }

    public String getVegieOne() {
        return vegieOne;
    }

    public void setVegieOne(String vegieOne) {
        this.vegieOne = vegieOne;
    }

    public String getVegieTwo() {
        return vegieTwo;
    }

    public void setVegieTwo(String vegieTwo) {
        this.vegieTwo = vegieTwo;
    }

    public String getVegieThree() {
        return vegieThree;
    }

    public void setVegieThree(String vegieThree) {
        this.vegieThree = vegieThree;
    }

    public String getRoti() {
        return roti;
    }

    public void setRoti(String roti) {
        this.roti = roti;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getSpecialExtra() {
        return specialExtra;
    }

    @Override
    public String toString() {
        String menu = "";
        if (special != null && !special.equals("null") && special.length()>1) {
            menu = special + ", ";
        }
        if (specialExtra != null && !specialExtra.equals("null") && specialExtra.length()>1) {
            menu = menu + specialExtra + ", ";
        }
        if (vegieOne != null && !vegieOne.equals("null") && vegieOne.length()>1) {
            menu = menu + vegieOne + ", ";
        }
        if (vegieTwo != null && !vegieTwo.equals("null") && vegieTwo.length()>1) {
            menu = menu + vegieTwo + ", ";
        }
        if (vegieThree != null && !vegieThree.equals("null") && vegieThree.length()>1) {
            menu = menu + vegieThree + ", ";
        }
        if (rice != null && !rice.equals("null") && rice.length()>1) {
            menu = menu + rice + ", ";
        }
        if ((roti != null && !roti.equals("null")) && roti.length()>1) {
            menu = menu + roti + ", ";
        }

        if (other != null && !other.equals("null") && other.length()>1)  {
            menu = menu + other;
        }

        menu = menu + ".";

        return  menu;
    }

    public void setSpecialExtra(String specialExtra) {
        this.specialExtra = specialExtra;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public MessMenu(){

    }

}