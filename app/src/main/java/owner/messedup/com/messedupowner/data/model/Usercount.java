package owner.messedup.com.messedupowner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Usercount {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("count")
    @Expose
    private String count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "Usercount{" +
                "date='" + date + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}

