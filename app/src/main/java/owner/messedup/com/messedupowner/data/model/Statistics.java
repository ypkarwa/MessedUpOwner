 package owner.messedup.com.messedupowner.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Statistics {

    @SerializedName("probableusers")
    @Expose
    private String probableusers;
    @SerializedName("usercount")
    @Expose
    private List<Usercount> usercount = null;
    @SerializedName("lastscans")
    @Expose
    private List<Lastscan> lastscans = null;

    public String getProbableusers() {
        return probableusers;
    }

    public void setProbableusers(String probableusers) {
        this.probableusers = probableusers;
    }

    public List<Usercount> getUsercount() {
        return usercount;
    }

    public void setUsercount(List<Usercount> usercount) {
        this.usercount = usercount;
    }

    public List<Lastscan> getLastscans() {
        return lastscans;
    }

    public void setLastscans(List<Lastscan> lastscans) {
        this.lastscans = lastscans;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "probableusers='" + probableusers + '\'' +
                ", usercount=" + usercount +
                ", lastscans=" + lastscans +
                '}';
    }
}
