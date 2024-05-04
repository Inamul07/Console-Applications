import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Admin {
    int adminId;
    String email;

    Admin(int adminId, String email) {
        this.adminId = adminId;
        this.email = email;
    }

    public void createListing(Map<Integer, Listing> listingMap, Listing listing) {
        listingMap.put(listing.listing_id, listing);
    }

    public void getAllListings(Map<Integer, Listing> listingMap) {
        for(int i: listingMap.keySet()) {
            System.out.println(listingMap.get(i).toString());
        }
    }

    public void updateListing(int listing_id, int host_id, String propertyName, String description, String type, float price, Map<Integer, Listing> listingMap) {
        if(!listingMap.containsKey(listing_id)) System.out.println("No Listing");
        else listingMap.put(listing_id, new Listing(listing_id, host_id, propertyName, description, type, price));
    }

    public void deleteListing(int listing_id, Map<Integer, Listing> listingMap) {
        listingMap.remove(listing_id);
    }

    public void getGuests(List<Guest> guests) {
        System.out.println(Arrays.toString(guests.toArray()));
    }

    public void getHosts(List<Host> hosts) {
        System.out.println(Arrays.toString(hosts.toArray()));
    }

}
