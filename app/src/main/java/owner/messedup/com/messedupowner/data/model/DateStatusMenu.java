package owner.messedup.com.messedupowner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DateStatusMenu {
    @SerializedName("date")
    @Expose
    public isSuccess Date;
    @SerializedName("VegieOne")
    @Expose
    public WeekMenu Menu;
    @SerializedName("VegieOne")
    @Expose
    public WeekStatus Status;

    public DateStatusMenu(isSuccess date, WeekMenu menu, WeekStatus status) {
        Date = date;
        Menu = menu;
        Status = status;
    }
}
