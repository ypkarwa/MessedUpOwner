package owner.messedup.com.messedupowner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Verification{

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("Verified")
    @Expose
    private String verified;
    @SerializedName("OwnerName")
    @Expose
    private String ownername;
    @SerializedName("MessName")
    @Expose
    private String messname;
    @SerializedName("Contact")
    @Expose
    private String phonenumber;
    @SerializedName("Address")
    @Expose
    private String area;

    @Override
    public String toString() {
        return "Verification{" +
                "success='" + success + '\'' +
                ", verified='" + verified + '\'' +
                ", ownername='" + ownername + '\'' +
                ", messname='" + messname + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", area='" + area + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @SerializedName("message")
    @Expose
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getOwnername() {
        return ownername;
    }

    public void setOwnername(String ownername) {
        this.ownername = ownername;
    }

    public String getMessname() {
        return messname;
    }

    public void setMessname(String messname) {
        this.messname = messname;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

}