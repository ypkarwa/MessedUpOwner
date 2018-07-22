package owner.messedup.com.messedupowner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OwnerData {

    @SerializedName("messname")
    @Expose
    private String messname;
    @SerializedName("ownername")
    @Expose
    private String ownername;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;
    @SerializedName("area")
    @Expose
    private String area;
    @SerializedName("messid")
    @Expose
    private String messid;


    public OwnerData(String messid, String messname, String ownername, String phonenumber, String area) {
        this.messid = messid;
        this.messname = messname;
        this.ownername = ownername;
        this.phonenumber = phonenumber;
        this.area = area;
    }

    @Override
    public String toString() {
        return "OwnerData{" +
                "messname='" + messname + '\'' +
                ", ownername='" + ownername + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", area='" + area + '\'' +
                ", messid='" + messid + '\'' +
                '}';
    }

    public String getMessid() {
        return messid;
    }


    public String getMessname() {
        return messname;
    }

    public String getOwnername() {
        return ownername;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getArea() {
        return area;
    }
}
