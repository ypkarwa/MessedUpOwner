package owner.messedup.com.messedupowner.data.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeekMenu {

    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("day")
    @Expose
    private String day;
    @SerializedName("menu")
    @Expose
    private List<MessMenu> menu = null;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<MessMenu> getMenu() {
        return menu;
    }

    public void setMenu(List<MessMenu> menu) {
        this.menu = menu;
    }

    @Override
    public String toString() {
        return "WeekMenu{" +
                "date='" + date + '\'' +
                ", day='" + day + '\'' +
                ", menu=" + menu +
                '}';
    }
}