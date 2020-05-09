package sweinc.com.buyvegitables.model;

public class FavItem {
    private String favName;
    private String favCost;
    private String favURL;

    public FavItem(String favName,  String favURL, String favCost) {
        this.favName = favName;
        this.favCost = favCost;
        this.favURL = favURL;

    }

    public String getFavName() {
        return favName;
    }

    public void setFavName(String favName) {
        this.favName = favName;
    }

    public String getFavCost() {
        return favCost;
    }

    public void setFavCost(String favCost) {
        this.favCost = favCost;
    }

    public String getFavURL() {
        return favURL;
    }

    public void setFavURL(String cartURL) {
        this.favURL = cartURL;
    }
}

