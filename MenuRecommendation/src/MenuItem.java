import java.util.Objects;

public class MenuItem {
    int itemId;
    int numOfRatings;
    int totalRatings;
    boolean isStockAvailable;
    boolean isDealOfTheDay;
    String displayName;

    public MenuItem(int itemId, String displayName) {
        this.itemId = itemId;
        this.displayName = displayName;
        numOfRatings = 0;
        totalRatings = 0;
        isStockAvailable = true;
        isDealOfTheDay = false;
    }

    public double getAverageRating() {
        if(numOfRatings == 0) return 0;
        return (double) totalRatings / numOfRatings;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "itemId=" + itemId +
                ", numOfRatings=" + numOfRatings +
                ", totalRatings=" + totalRatings +
                ", isStockAvailable=" + isStockAvailable +
                ", isDealOfTheDay=" + isDealOfTheDay +
                ", displayName='" + displayName + '\'' +
                '}';
    }
}
