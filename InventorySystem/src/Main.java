import database.Database;
import models.Inventory;
import models.Section;
import products.Game;
import products.Movie;

class Main {
    public static void main(String[] args) {

        Database database = Database.getInstance();

        Inventory inventory = database.getInventory(1);
        inventory.viewListOfSections();

        Section<Movie> movieSection = (Section<Movie>) inventory.getSection("My Movies");
        movieSection.viewItems();

//        Section<Movie> movieSection = new Section<>("My Movies");
//        movieSection.addItem(new Movie("Interstellar", "Chirstopher Nolan", 2014, "Sci-Fi"));
//        inventory.addSection(movieSection);

        database.closeConnection();
    }
}