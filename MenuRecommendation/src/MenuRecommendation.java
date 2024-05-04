import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class MenuRecommendation implements IMenuRecommendation {

    Map<Integer, MenuItem> map;
    int currDeal;

    public MenuRecommendation() {
        map = new HashMap<>();
        currDeal = 0;
    }

    @Override
    public void addItem(int itemId, String displayName) {
        map.put(itemId, new MenuItem(itemId, displayName));
    }

    @Override
    public MenuItem getRecommendedItem() {
        if(currDeal != 0 && map.get(currDeal).isStockAvailable) {
            return map.get(currDeal);
        }
        PriorityQueue<MenuItem> pq = new PriorityQueue<>((a, b) -> {
            double rating1 = a.getAverageRating(), rating2 = b.getAverageRating();
            if(rating1 != rating2) {
                return Double.compare(rating2, rating1);
            }
            if(a.numOfRatings != b.numOfRatings) {
                return Integer.compare(b.numOfRatings, a.numOfRatings);
            }
            int nameComparison = a.displayName.compareTo(b.displayName);
            if(nameComparison != 0) {
                return nameComparison;
            }
            return Integer.compare(a.itemId, b.itemId);
        });
        for(MenuItem item: map.values()) {
            if(item.isStockAvailable) pq.add(item);
        }
        return pq.remove();
    }

    @Override
    public void outOfStockItem(int itemId) {
        MenuItem item = map.get(itemId);
        item.isStockAvailable = false;
    }

    @Override
    public void restockItem(int itemId) {
        MenuItem item = map.get(itemId);
        item.isStockAvailable = true;
    }

    @Override
    public void makeDealOfTheDayItem(int itemId) {
        if(currDeal != 0) {
            MenuItem prevDeal = map.get(currDeal);
            prevDeal.isDealOfTheDay = false;
        }
        MenuItem item = map.get(itemId);
        item.isDealOfTheDay = true;
        currDeal = itemId;
    }

    @Override
    public void rateItem(int itemId, int rating) {
        MenuItem item = map.get(itemId);
        item.numOfRatings += 1;
        item.totalRatings += rating;
    }
}
