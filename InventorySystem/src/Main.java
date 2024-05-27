import database.Database;
import models.Inventory;
import models.Section;
import products.Game;
import products.Movie;

class Main {
    public static void main(String[] args) {

        Database database = Database.getInstance();

//        Inventory inventory = new Inventory();
//        Section<Game> gameSection = new Section<>("My Games");
//        gameSection.addItem(new Game("GTA V", 2014, true));
//        gameSection.addItem(new Game("Efootball 2024", 2024, false));
//        inventory.addSection(gameSection);
//
//        Section<Movie> movieSection = new Section<>("My Movies");
//        movieSection.addItem(new Movie("Interstellar", "Christopher Nolan", 2014, "Sci-Fi"));
//        movieSection.addItem(new Movie("Avengers Endgame", "Russo Brothers", 2018, "Super Hero"));
//        movieSection.addItem(new Movie("Revenge of the Sith", "George Lucas", 2005, "Space Opera"));
//        inventory.addSection(movieSection);
//
//        System.out.println(inventory.getLastModified());
//        inventory.viewListOfSections();
//        gameSection.viewItems();
//        movieSection.viewItems();

        Inventory inventory = database.getInventory(1);
        System.out.println(inventory.getLastModified());
        inventory.viewListOfSections();
        inventory.viewListOfContents();

        database.closeConnection();
    }
}