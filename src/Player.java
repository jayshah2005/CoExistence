import java.util.ArrayList;

/**
 * Player class to keep information about a player in one unit
 */
public class Player {
    int points;
    ArrayList<String> cards;
    boolean pass;

    Player(){
        points = 0;
        cards = new ArrayList<>(5);
        pass = false;
    }
}
