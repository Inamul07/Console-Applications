package products;

import java.io.Serializable;

public class Game implements Serializable {

    private static final long serialVersionUID = 2L;

    private final String gameName;
    private final int releaseYear;
    private final boolean isOpenWorld;

    public Game(String gameName, int releaseYear, boolean isOpenWorld) {
        this.gameName = gameName;
        this.releaseYear = releaseYear;
        this.isOpenWorld = isOpenWorld;
    }

    @Override
    public String toString() {
        return "{Game: " + gameName + ", " + releaseYear + ", " + (isOpenWorld? "Open World": "Online Multiplayer") + "}";
    }

}
