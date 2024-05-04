public class Listing {
    int listing_id, host_id;
    float price;
    String propertyName, description, type;

    Listing(int listing_id, int host_id, String propertyName, String description, String type, float price) {
        this.listing_id = listing_id;
        this.host_id = host_id;
        this.price = price;
        this.propertyName = propertyName;
        this.description = description;
        this.type = type;
    }

    public void initiateBooking() {
        System.out.println("Booking Done");
    }

    @Override
    public String toString() {
        return "Listing{" +
                "listing_id=" + listing_id +
                ", host_id=" + host_id +
                ", price=" + price +
                ", propertyName='" + propertyName + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
