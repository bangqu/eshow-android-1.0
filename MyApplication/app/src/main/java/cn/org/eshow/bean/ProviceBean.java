package cn.org.eshow.bean;

/**
 * Created by daikting on 16/3/31.
 */
public class ProviceBean {
    private int ProID;
    private String name;
    private String ProSort;
    private String ProRemark;

    public int getProID() {
        return ProID;
    }

    public void setProID(int proID) {
        ProID = proID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProSort() {
        return ProSort;
    }

    public void setProSort(String proSort) {
        ProSort = proSort;
    }

    public String getProRemark() {
        return ProRemark;
    }

    public void setProRemark(String proRemark) {
        ProRemark = proRemark;
    }
}
