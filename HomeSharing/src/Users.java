import java.util.Map;

public class Users {
    int user_id;
    String name, phone;

    public Users(int user_id, String name, String phone) {
        this.user_id = user_id;
        this.name = name;
        this.phone = phone;
    }

    public void getAllListings(Map<Integer, Listing> listingMap) {
        for(int i: listingMap.keySet()) {
            System.out.println(listingMap.get(i).toString());
        }
    }
}
