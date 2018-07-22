package owner.messedup.com.messedupowner.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeekStatus {

    @SerializedName("messweek")
    @Expose
    private List<Messweek> messweek = null;
    @SerializedName("success")
    @Expose
    private Integer success;

    public List<Messweek> getMessweek() {
        return messweek;
    }

    public void setMessweek(List<Messweek> messweek) {
        this.messweek = messweek;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "WeekStatus{" +
                "messweek=" + messweek +
                ", success=" + success +
                '}';
    }
}