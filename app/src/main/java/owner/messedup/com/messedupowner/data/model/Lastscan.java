package owner.messedup.com.messedupowner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lastscan {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("plate")
    @Expose
    private String plate;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Lastscan{" +
                "name='" + name + '\'' +
                ", plate='" + plate + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
