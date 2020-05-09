package sweinc.com.buyvegitables.model;

public class RecyclerItem {

    private String index_name, index_price;
    private String index_thumbnail;

    public RecyclerItem(String index_name, String index_thumbnail, String index_price) {
        this.index_name = index_name;
        this.index_thumbnail = index_thumbnail;
        this.index_price = index_price;
    }

    public String getIndex_price() {
        return index_price;
    }

    public void setIndex_price(String index_price) {
        this.index_price = index_price;
    }

    public String getIndex_name() {
        return index_name;
    }

    public void setIndex_name(String index_name) {
        this.index_name = index_name;
    }

    public String getIndex_thumbnail() {
        return index_thumbnail;
    }

    public void setIndex_thumbnail(String index_thumbnail) {
        this.index_thumbnail = index_thumbnail;
    }


}