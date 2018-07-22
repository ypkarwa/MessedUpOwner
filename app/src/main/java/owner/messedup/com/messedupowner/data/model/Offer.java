package owner.messedup.com.messedupowner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Offer {
    @SerializedName("dayname")
    @Expose
    String messid;
    @SerializedName("date")
    @Expose
    String date;
    @SerializedName("offer")
    @Expose
    String offer;

    public String getMessid() {
        return messid;
    }

    public Offer(String messid, String date, String offer) {
        this.messid = messid;
        this.date = date;
        this.offer = offer;
    }

    public void setMessid(String messid) {
        this.messid = messid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }
}
