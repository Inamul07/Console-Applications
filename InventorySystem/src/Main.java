import database.Database;
import models.Inventory;
import models.Section;
import products.Game;

class Main {
    public static void main(String[] args) {

        Inventory inventory = new Inventory();
        Section<Game> gameSection = new Section<>("My Games");
        gameSection.addItem(new Game("GTA V", 2014, true));
        gameSection.addItem(new Game("Efootball 2024", 2023, false));

        gameSection.viewItems();
        inventory.addSection(gameSection);

        Section<Game> gameSection1 = (Section<Game>) inventory.getSection("My Games");₹
        gameSection1.viewItems();
    }
}