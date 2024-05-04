import java.util.Map;

public class Host extends Users{

    public Host(int user_id, String name, String phone) {
        super(user_id, name, phone);
    }

    public void createListing(Map<Integer, Listing> listingMap, Listing listing) {
        listingMap.put(listing.listing_id, listing);
    }

    public void updateListing(int listing_id, int host_id, String propertyName, String description, String type, float price, Map<Integer, Listing> listingMap) {
        if(!listingMap.containsKey(listing_id)) System.out.println("No Listing");
        else listingMap.put(listing_id, new Listing(listing_id, host_id, propertyName, description, type, price));
    }

    public void deleteListing(int listing_id, Map<Integer, Listing> listingMap) {
        listingMap.remove(listing_id);
    }

    @Override
    public String toString() {
        return "Host{" +
                "user_id=" + user_id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
