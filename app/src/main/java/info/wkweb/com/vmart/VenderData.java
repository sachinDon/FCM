package info.wkweb.com.vmart;

/**
 * Created by learntodrill on 10/02/20.
 */

public class VenderData{
    private String description;
    private int imgId;
    public VenderData(String description, int imgId)
    {
        this.description = description;
        this.imgId = imgId;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

}

