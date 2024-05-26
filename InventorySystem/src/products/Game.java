package products;

public class Game {

    private final String gameName;
    private final int releaseYear;
    private final boolean isOpenWorld;

    Game(String gameName, int releaseYear, boolean isOpenWorld) {
        this.gameName = gameName;
        this.releaseYear = releaseYear;
        this.isOpenWorld = isOpenWorld;
    }

    @Override
    public String toString() {
        return "{Game: " + gameName + ", " + releaseYear + ", " + (isOpenWorld? "Open World": "Online Multiplayer") + "}";
    }

}
