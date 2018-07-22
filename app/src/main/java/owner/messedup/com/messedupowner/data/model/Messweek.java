package owner.messedup.com.messedupowner.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Messweek {

    @SerializedName("messid")
    @Expose
    private String messid;
    @SerializedName("lunsun")
    @Expose
    private String lunsun;
    @SerializedName("lunmon")
    @Expose
    private String lunmon;
    @SerializedName("luntue")
    @Expose
    private String luntue;
    @SerializedName("lunwed")
    @Expose
    private String lunwed;
    @SerializedName("lunthu")
    @Expose
    private String lunthu;
    @SerializedName("lunfri")
    @Expose
    private String lunfri;
    @SerializedName("lunsat")
    @Expose
    private String lunsat;

    @Override
    public String toString() {
        return "Messweek{" +
                "messid='" + messid + '\'' +
                ", lunsun='" + lunsun + '\'' +
                ", lunmon='" + lunmon + '\'' +
                ", luntue='" + luntue + '\'' +
                ", lunwed='" + lunwed + '\'' +
                ", lunthu='" + lunthu + '\'' +
                ", lunfri='" + lunfri + '\'' +
                ", lunsat='" + lunsat + '\'' +
                ", dinsun='" + dinsun + '\'' +
                ", dinmon='" + dinmon + '\'' +
                ", dintue='" + dintue + '\'' +
                ", dinwed='" + dinwed + '\'' +
                ", dinthu='" + dinthu + '\'' +
                ", dinfri='" + dinfri + '\'' +
                ", dinsat='" + dinsat + '\'' +
                '}';
    }

    @SerializedName("dinsun")
    @Expose
    private String dinsun;
    @SerializedName("dinmon")
    @Expose
    private String dinmon;
    @SerializedName("dintue")
    @Expose
    private String dintue;
    @SerializedName("dinwed")
    @Expose
    private String dinwed;
    @SerializedName("dinthu")
    @Expose
    private String dinthu;
    @SerializedName("dinfri")
    @Expose
    private String dinfri;
    @SerializedName("dinsat")
    @Expose
    private String dinsat;

    public String getMessid() {
        return messid;
    }

    public void setMessid(String messid) {
        this.messid = messid;
    }

    public String getLunsun() {
        return lunsun;
    }

    public void setLunsun(String lunsun) {
        this.lunsun = lunsun;
    }

    public String getLunmon() {
        return lunmon;
    }

    public void setLunmon(String lunmon) {
        this.lunmon = lunmon;
    }

    public String getLuntue() {
        return luntue;
    }

    public void setLuntue(String luntue) {
        this.luntue = luntue;
    }

    public String getLunwed() {
        return lunwed;
    }

    public void setLunwed(String lunwed) {
        this.lunwed = lunwed;
    }

    public String getLunthu() {
        return lunthu;
    }

    public void setLunthu(String lunthu) {
        this.lunthu = lunthu;
    }

    public String getLunfri() {
        return lunfri;
    }

    public void setLunfri(String lunfri) {
        this.lunfri = lunfri;
    }

    public String getLunsat() {
        return lunsat;
    }

    public void setLunsat(String lunsat) {
        this.lunsat = lunsat;
    }

    public String getDinsun() {
        return dinsun;
    }

    public void setDinsun(String dinsun) {
        this.dinsun = dinsun;
    }

    public String getDinmon() {
        return dinmon;
    }

    public void setDinmon(String dinmon) {
        this.dinmon = dinmon;
    }

    public String getDintue() {
        return dintue;
    }

    public void setDintue(String dintue) {
        this.dintue = dintue;
    }

    public String getDinwed() {
        return dinwed;
    }

    public void setDinwed(String dinwed) {
        this.dinwed = dinwed;
    }

    public String getDinthu() {
        return dinthu;
    }

    public void setDinthu(String dinthu) {
        this.dinthu = dinthu;
    }

    public String getDinfri() {
        return dinfri;
    }

    public void setDinfri(String dinfri) {
        this.dinfri = dinfri;
    }

    public String getDinsat() {
        return dinsat;
    }

    public void setDinsat(String dinsat) {
        this.dinsat = dinsat;
    }

}