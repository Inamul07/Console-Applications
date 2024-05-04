import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Map<Integer, Listing> listingMap = new HashMap<>();
        List<Guest> guestList = new ArrayList<>();
        List<Host> hostList = new ArrayList<>();

        int userType = 0;

        while(userType < 4) {
            System.out.println("1. Admin\n2. Guest\n3. Host\nEnter: ");
            userType = Integer.parseInt(sc.nextLine());

            if(userType == 1) {
                System.out.print("Enter Email: ");
                String email = sc.nextLine();

                Admin admin = new Admin((int) (Math.random() * 10), email);
                int choice = 0;
                while(choice < 7) {
                    System.out.println("1. Create 2. Read 3. Update 4. Delete 5. Get Users 6. Get Hosts");
                    choice = Integer.parseInt(sc.nextLine());
                    if(choice == 1) {
                        String propertyName, description, type;
                        int host_id;
                        float price;
                        propertyName = sc.nextLine();
                        price = Float.parseFloat(sc.nextLine());
                        description = sc.nextLine();
                        type = sc.nextLine();
                        host_id = admin.adminId;
                        Listing listing = new Listing((int) (Math.random() * 100), host_id, propertyName, description, type, price);
                        admin.createListing(listingMap, listing);
                    } else if(choice == 2) {
                        admin.getAllListings(listingMap);
                    } else if(choice == 3) {
                        String propertyName, description, type;
                        int host_id, listingId;
                        float price;
                        listingId = Integer.parseInt(sc.nextLine());
                        propertyName = sc.nextLine();
                        price = Float.parseFloat(sc.nextLine());
                        description = sc.nextLine();
                        type = sc.nextLine();
                        host_id = admin.adminId;
                        admin.updateListing(listingId, host_id, propertyName, description, type, price, listingMap);
                    } else if(choice == 4) {
                        int listing_id = Integer.parseInt(sc.nextLine());
                        admin.deleteListing(listing_id, listingMap);
                    } else if(choice == 5) {
                        admin.getGuests(guestList);
                    } else if(choice == 6) {
                        admin.getHosts(hostList);
                    }
                }
            } else if(userType == 2) {
                String name = sc.nextLine(), phone = sc.nextLine();
                Guest guest = new Guest((int) (Math.random() * 1000), name, phone);
                guestList.add(guest);
                int choice = 0;
                while(choice < 3) {
                    System.out.println("1. Read 2. Book");
                    choice = Integer.parseInt(sc.nextLine());
                    if(choice == 1) {
                        guest.getAllListings(listingMap);
                    } else if(choice == 2) {
                        int listing_id = Integer.parseInt(sc.nextLine());
                        Listing listing = listingMap.get(listing_id);
                        listing.initiateBooking();
                    }
                }
            } else if(userType == 3) {
                String name = sc.nextLine(), phone = sc.nextLine();
                Host host = new Host((int) (Math.random() * 10000), name, phone);
                hostList.add(host);
                int choice = 0;
                while(choice < 4) {
                    System.out.println("1. Create 2. Update 3. Delete");
                    choice = Integer.parseInt(sc.nextLine());
                    if(choice == 1) {
                        String propertyName, description, type;
                        int host_id;
                        float price;
                        propertyName = sc.nextLine();
                        price = Float.parseFloat(sc.nextLine());
                        description = sc.nextLine();
                        type = sc.nextLine();
                        host_id = host.user_id;
                        Listing listing = new Listing((int) (Math.random() * 100), host_id, propertyName, description, type, price);
                        host.createListing(listingMap, listing);
                    } else if(choice == 2) {
                        String propertyName, description, type;
                        int host_id, listingId;
                        float price;
                        listingId = Integer.parseInt(sc.nextLine());
                        propertyName = sc.nextLine();
                        price = Float.parseFloat(sc.nextLine());
                        description = sc.nextLine();
                        type = sc.nextLine();
                        host_id = host.user_id;
                        host.updateListing(listingId, host_id, propertyName, description, type, price, listingMap);
                    } else if(choice == 3) {
                        int listing_id = Integer.parseInt(sc.nextLine());
                        host.deleteListing(listing_id, listingMap);
                    }
                }
            }
        }
    }
}