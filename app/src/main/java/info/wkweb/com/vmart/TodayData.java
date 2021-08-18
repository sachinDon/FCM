package info.wkweb.com.vmart;

/**
 * Created by learntodrill on 11/02/20.
 */

public class TodayData {
    private String description,custprice1,custprice2,custprice3,custprice4;
    private int imgId;
    public TodayData(String description, int imgId,String custprice1,String custprice2,String custprice3,String custprice4)
    {
        this.description = description;
        this.imgId = imgId;
        this.custprice1 = custprice1;
        this.custprice2 = custprice2;
        this.custprice3 = custprice3;
        this.custprice4 = custprice4;
    }

    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getcustprice1() {
        return custprice1;
    }
    public void setcustprice1(String custprice1) {
        this.custprice1 = custprice1;
    }

    public String getcustprice2() {
        return custprice2;
    }
    public void setcustprice2(String custprice2) {
        this.custprice2 = custprice2;
    }

    public String getcustprice3() {
        return custprice3;
    }
    public void setcustprice3(String custprice3) {
        this.custprice3 = custprice3;
    }

    public String getcustprice4() {
        return custprice4;
    }
    public void setcustprice4(String custprice4) {
        this.custprice4 = custprice4;
    }
}



