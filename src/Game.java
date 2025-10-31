import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private char[][] gameboard;
    int round;
    Player p1;
    Player p2;
    int currPlayer;

    /**
     * Game class which simulates a game between 2 players
     * @param in1 input stream for player 1
     * @param in2 input stream for player 2
     * @param out1 output stream for player 1
     * @param out2 output stream for player 2
     */
    Game(BufferedReader in1, BufferedReader in2, PrintWriter out1, PrintWriter out2) throws IOException {
        currPlayer = (int) Math.floor(Math.random() * 2);
        p1 = new Player();
        p2 = new Player();
        boolean properInp;
        String inputLine;

        // Initialize a default game board to simulate all the round on
        initializeBoard();
        printMsg("NEW GAME");

        // Simulate 5 round
        game:
        for(round = 1; round < 5; round++){
            dealCards(p1, p2);
            printGameBoards(out1, out2);

            // Simulate a round
            round:
            while (true) {

                // The if condition is to check if the starting player is player1
                if (currPlayer == 0) {
                    // Player 1 Turn
                    do {
                        inputLine = in1.readLine().toUpperCase();
                        if (inputLine == null) break game;
                        // Make changes to the board based on p1
                        properInp = updateState(inputLine);

                        if(properInp){
                            // Change the player, print the new board for both parties and check if the round is over
                            updatePlayer();
                            if(roundEnd(p1, p2)) break round;
                        }
                        printGameBoards(out1, out2);
                    } while (!properInp);
                }

                // Player 2 Turn
                do{
                    inputLine=in2.readLine().toUpperCase();
                    if (inputLine==null) break game;
                    // Make changes to the board based on p2
                    properInp = updateState(inputLine);

                    if(properInp){
                        // Change the player, print the new board for both parties and check if the round is over
                        updatePlayer();
                        if(roundEnd(p1, p2)) break round;
                    }
                    printGameBoards(out1, out2);
                } while(!properInp);
            }

            // Check if the game is over and reset the passes from both variables
            if(p1.points == 9 || p2.points == 9) break game;
            p1.pass = false;
            p2.pass = false;
            printMsg("NEW ROUND");
        }
        printGameBoards(out1, out2);    // Print a gameboard indicating the game is over for both players
    }

    /**
     * Changes the arrows to reflect that the game is over
     */
    public void updateArrowsForGameOver(){
        gameboard[2][38] = '-';
        gameboard[4][38] = '-';
        gameboard[12][38] = '-';
        gameboard[14][38] = '-';
    }

    /**
     * Prints a loss screen in a relative 5*5 array starting from (i, j)
     */
    public void lossScreen(int i, int j){

        clear5By5(i, j);

        gameboard[i][j] = '[';
        gameboard[i][j+1] = '=';
        gameboard[i][j+2] = 'X';
        gameboard[i][j+3] = '=';
        gameboard[i][j+4] = ']';

        gameboard[i+1][j+1] = 'o';
        gameboard[i+1][j+3] = 'O';
        gameboard[i+2][j+2] = 'v';

        gameboard[i+3][j+1] = '-';
        gameboard[i+3][j+2] = '-';
        gameboard[i+3][j+3] = '-';

        gameboard[i + 4][j] = '[';
        gameboard[i + 4][j+1] = '=';
        gameboard[i + 4][j+2] = 'X';
        gameboard[i + 4][j+3] = '=';
        gameboard[i + 4][j+4] = ']';
    }

    /**
     * Prints a win screen in a relative 5*5 array starting from (i, j)
     */
    public void winScreen(int i, int j){
        char[] arr = "Player Won".toCharArray();
        clear5By5(i, j);

        int x = i + 2;
        int y = j;

        for(char c : arr){
            gameboard[x][y] = c;
            y++;
            if(y > j + 5){
                y = j;
                x++;
            }
        }
    }

    /**
     * Clear a 5*5 relative array starting from (i, j)
     */
    public void clear5By5(int i, int j){
        int x = i;
        int y = j;

        // Reset the space first
        for(x=i; x < i + 5; x++){
            for(y=j; y < j + 5; y++) gameboard[x][y] = ' ';
        }
    }

    /**
     * @return true if the round is over, false otherwise
     */
    public boolean roundEnd(Player p1, Player p2){
        if(p1.points == 9){
            return true;
        } else if(p2.points == 9){
            return true;
        } else if(p1.pass && p2.pass){
            return true;
        }
        return false;
    }

    public void updatePlayer(){
        currPlayer = 1 - currPlayer;
    }

    /**
     * Update Player state based on a player move
     * @param inputLine the player input
     * @return true if the move was valid, false otherwise
     */
    public boolean updateState(String inputLine){
        char[] inpArray = inputLine.toCharArray();
        Player p1;
        Player p2;

        // Decide who's turn it is and assign p1 to the attacking player and p2 to the defending player
        if(currPlayer == 0){
            p1 = this.p1;
            p2 = this.p2;
        } else{
            p1 = this.p2;
            p2 = this.p1;
        }

        // Check is the player passed
        if(inputLine.equals("PS")) {
            p1.pass = true;
            printMsg("PLAYER PASSED");
            return true;
        }

        // Check if the input is of correct length
        if(inpArray.length != 2) {
            printMsg("SYNTAX ERROR");
            return false;
        }

        // Check if the selection is out of bounds or not allowed and Assign the cards corresponding to the attacking and defending player
        if (!(inpArray[0] >= 'A' && inpArray[0] < 'G' && inpArray[1] >= 'A' && inpArray[1] < 'G')) {
            printMsg("SYNTAX ERROR");
            return false;
        }

        String attacker = p1.cards.get(((int)inpArray[0] - 65));
        String receiver = p2.cards.get(((int)inpArray[1] - 65));

        // Check if the move is valid
        if(!validateMove(attacker, receiver)){
            printMsg("INVALID MOVE");
            return false;
        }

        p2.cards.set(((int)inpArray[1] - 65), " ");

        // Only give points if the move does not involve arrows as attacking/receiving card
        if(!(attacker.equals("arrow") || receiver.equals("arrow"))) p1.points++;

        if(p1.points == 9) printMsg(attacker.toUpperCase() + " takes " + receiver.toUpperCase() + "! Game Over");
        else printMsg(attacker.toUpperCase() + " takes " + receiver.toUpperCase());

        return true;
    }

    /**
     * Check if a move is valid
     * @param attacker attacking card
     * @param receiver defending card
     * @return true if it valid, false otherwise
     */
    public boolean validateMove(String attacker, String receiver){
        boolean valide = false;

        if (attacker.equals("axe") && receiver.equals("hammer")) valide = true;
        else if (attacker.equals("hammer") && receiver.equals("sword")) valide = true;
        else if (attacker.equals("sword") && receiver.equals("axe")) valide = true;
        else if (attacker.equals("arrow") || receiver.equals("arrow")) valide = true;

        if (attacker.equals(" ") || receiver.equals(" ")) valide = false;

        return valide;
    }

    /**
     * Print gameboards for both players
     * @param out1 output stream for player 1
     * @param out2 output stream for player 2
     */
    public void printGameBoards(PrintWriter out1, PrintWriter out2){
        String printable;

        // Print for the player 1
        printable = "";
        char[][] p1Board = getGameboard(p1, p2);

        for(int i = 0; i < 19; i++){
            printable += new String(p1Board[i]) + "\n";
        }
        out1.println(printable.substring(0, printable.length() - 1));

        // print for player 2
        printable = "";
        char[][] p2Board = getGameboard(p2, p1);

        for(int i = 0; i < 19; i++){
            printable += new String(p2Board[i]) + "\n";
        }
        out2.println(printable.substring(0, printable.length() - 1));
    }

    /**
     * Print a message in the message log
     * @param msg the message to be printed
     */
    public void printMsg(String msg){

        char[] text = msg.toCharArray();

        if(text.length > 38) throw new Error("The message is too long");

        int i = 17;
        int j = 1;

        for(int x = 1; x < 39; x++){
            gameboard[i][x] = ' ';
        }

        for(; j < msg.length() + 1; j++){
            gameboard[i][j] = text[j - 1];
        }
    }

    /**
     * Reads the cards both players have and, populates the board from player p1's prespective
     * @param p1 relative player 1 whose board we are printing
     * @param p2 the opponent player
     * @return a populated gameboard
     */
    public char[][] getGameboard(Player p1, Player p2) {

        resetCards();

        // Set all the cards for p1 and p2
        setCard(3, 2, p2.cards.get(0));
        setCard(3, 7, p2.cards.get(1));
        setCard(3, 12, p2.cards.get(2));
        setCard(3, 17, p2.cards.get(3));
        setCard(3, 22, p2.cards.get(4));
        setCard(3, 27, p2.cards.get(5));

        setCard(11, 2, p1.cards.get(0));
        setCard(11, 7, p1.cards.get(1));
        setCard(11, 12, p1.cards.get(2));
        setCard(11, 17, p1.cards.get(3));
        setCard(11, 22, p1.cards.get(4));
        setCard(11, 27, p1.cards.get(5));

        // Check whose turn it is, whose's prespective it is and initialize the arrows
        if(p1.equals(this.p1)){
            if(currPlayer != 0){
                gameboard[2][38] = '^';
                gameboard[4][38] = '-';

                gameboard[12][38] = '^';
                gameboard[14][38] = '-';
            } else {
                gameboard[2][38] = '-';
                gameboard[4][38] = 'v';

                gameboard[12][38] = '-';
                gameboard[14][38] = 'v';
            }
        }else{
            if(currPlayer != 0 && !p1.equals(this.p1)){
                gameboard[2][38] = '-';
                gameboard[4][38] = 'v';

                gameboard[12][38] = '-';
                gameboard[14][38] = 'v';
            } else{
                gameboard[2][38] = '^';
                gameboard[4][38] = '-';

                gameboard[12][38] = '^';
                gameboard[14][38] = '-';
            }
        }

        // Print the points and the current round
        gameboard[7][38] = (char) (p2.points + '0');
        gameboard[8][38] = (char) (round + '0');
        gameboard[9][38] = (char) (p1.points + '0');

        // Print a win/loss message if needed
        if(p1.points == 9){
            updateArrowsForGameOver();
            printMsg("GAME OVER. You Win");
            winScreen(10, 31);
            lossScreen(2, 31);
        } else if(p2.points == 9){
            updateArrowsForGameOver();
            printMsg("GAME OVER. You Lose");
            winScreen(2, 31);
            lossScreen(10, 31);
        } else if(round > 4){

            // When we reach round 6 we exit the for loop but it's not really round 6. The game ends at round 5
            // so to reflect that we update the round counter in the returned board and reset the round back to 6
            round--;
            gameboard[8][38] = (char) (round + '0');
            round++;

            updateArrowsForGameOver();
            printMsg("BOTH PLAYERS LOSE");
            lossScreen(2, 31);
            lossScreen(10, 31);
        }

        return gameboard;
    }

    /**
     * Sets a card contents in a relative 3*3 relative array starting from (i, j)
     * @param card the card we are setting
     */
    public void setCard(int i, int j, String card) {
        if (card.equals("axe")) {
            setAxe(i, j);
        } else if (card.equals("hammer")) {
            setHammer(i, j);
        } else if (card.equals("arrow")) {
            setArrow(i, j);
        } else if (card.equals("sword")) {
            setSword(i, j);
        }
    }

    /**
     * Create a hammer in a 3x3 relative array starting from (i, j)
     */
    public void setHammer(int i, int j) {
        gameboard[i][j] = '[';
        gameboard[i][j + 1] = '=';
        gameboard[i][j + 2] = ']';
        gameboard[i + 1][j + 1] = '|';
        gameboard[i + 2][j + 1] = '|';
    }

    /**
     * Create an arrow in a 3x3 relative array starting from (i, j)
     */
    public void setArrow(int i, int j) {
        gameboard[i][j + 1] = '^';
        gameboard[i + 1][j + 1] = '|';
        gameboard[i + 2][j + 1] = '^';
        gameboard[i + 2][j] = '/';
        gameboard[i + 2][j + 2] = '\\';
    }

    /**
     * Create a sword in a 3x3 relative array starting from (i, j)
     */
    public void setSword(int i, int j) {
        gameboard[i][j + 2] = '/';
        gameboard[i + 1][j + 1] = '/';
        gameboard[i + 2][j] = 'X';
    }

    /**
     * Create an axe in a 3x3 relative array starting from (i, j)
     */
    public void setAxe(int i, int j) {
        gameboard[i][j] = '<';
        gameboard[i][j + 1] = '7';
        gameboard[i][j + 2] = '>';
        gameboard[i + 1][j + 1] = '|';
        gameboard[i + 2][j + 1] = 'L';
    }


    /**
     * Reset all the card spaces to empty
     */
    public void resetCards(){
        // Set empty inner cards for the top row
        emptyInnerCard(3, 2);
        emptyInnerCard(3, 7);
        emptyInnerCard(3, 12);
        emptyInnerCard(3, 17);
        emptyInnerCard(3, 22);
        emptyInnerCard(3, 27);

        // Set empty inner cards for the bottom row
        emptyInnerCard(11, 2);
        emptyInnerCard(11, 7);
        emptyInnerCard(11, 12);
        emptyInnerCard(11, 17);
        emptyInnerCard(11, 22);
        emptyInnerCard(11, 27);

    }

    /**
     * Empty the card in a 3*3 relative array space starting from (i, j)
     */
    public void emptyInnerCard(int i, int j){
        for(int x = i; x < i + 3; x++){
            for(int y = j; y < j + 3; y++){
                gameboard[x][y] = ' ';
            }
        }
    }

    /**
     * Initialize an empty 19 * 40 array and set up the gameboard on it
     */
    public void initializeBoard() {
        gameboard = new char[19][40];

        // Initialize the array
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 40; j++) {
                gameboard[i][j] = ' ';
            }
        }
        setBorders();
        initializeEmptyCards();
        initializeIdentifiers();

        // Set up some more miscellaneous constants.
        gameboard[3][38] = '|';
        gameboard[13][38] = '|';

        gameboard[7][37] = '[';
        gameboard[7][39] = ']';
        gameboard[8][37] = 'R';
        gameboard[8][39] = '<';
        gameboard[9][37] = '[';
        gameboard[9][39] = ']';
    }

    public void initializeIdentifiers(){
        int j = 0;
        char[] letters = new char[]{'A', 'B', 'C', 'D', 'E', 'F'};
        for(int i = 3; i < 31; i+=5){
            gameboard[1][i] = letters[j];
            gameboard[15][i] = letters[j];
            j++;
        }
    }

    public void initializeEmptyCards(){

        // Set empty cards for the top row
        createEmptyCard(2, 1);
        createEmptyCard(2, 6);
        createEmptyCard(2, 11);
        createEmptyCard(2, 16);
        createEmptyCard(2, 21);
        createEmptyCard(2, 26);

        // Set empty cards for the bottom row
        createEmptyCard(10, 1);
        createEmptyCard(10, 6);
        createEmptyCard(10, 11);
        createEmptyCard(10, 16);
        createEmptyCard(10, 21);
        createEmptyCard(10, 26);
    }

    /**
     * Creates an empty card in a relative 3*3 space starting from (i, j)
     */
    public void createEmptyCard(int i, int j){
        gameboard[i][j] = '/';
        gameboard[i][j + 4] = '\\';
        gameboard[i+4][j] = '\\';
        gameboard[i+4][j+4] = '/';
        for(int k = i + 1; k < i + 4; k++){
            gameboard[k][j] = '|';
            gameboard[k][j + 4] = '|';
        }
        for(int k = j + 1; k < j + 4; k++){
            gameboard[i][k] = '-';
            gameboard[i + 4][k] = '-';
        }
    }

    /**
     * Set the top and bottom borders. Also set the border for the message log and the player separator.
     */
    public void setBorders(){
        gameboard[0][0] = '/';
        gameboard[0][39] = '\\';
        for(int i = 1; i < 39; i++){
            gameboard[0][i] = '-';
            gameboard[18][i] = '-';
        }
        gameboard[18][0] = '\\';
        gameboard[18][39] = '/';

        gameboard[17][0] = '|';
        gameboard[17][39] = '|';

        gameboard[16][0] = '|';
        gameboard[16][39] = '|';
        for(int i = 1; i < 39; i++) gameboard[16][i] = '-';


        gameboard[8][0] = '<';
        gameboard[8][36] = '>';
        for(int i = 1; i < 36; i++) gameboard[8][i] = '=';
    }

    /**
     * Deals new cards to both players from a randomly shuffled deck
     */
    public void dealCards(Player p1, Player p2){
        ArrayList<String> deck = getDeck();
        p1.cards.clear();
        p2.cards.clear();

        for(int i = 0; i < 6; i++){
            p1.cards.add(deck.get(i));
            p2.cards.add(deck.get(i + 6));
        }
    }

    /**
     * @return a randomly shuffled deck of cards
     */
    public ArrayList<String> getDeck(){
        ArrayList<String> deck = new ArrayList<>();
        deck.add("axe");
        deck.add("axe");
        deck.add("axe");
        deck.add("hammer");
        deck.add("hammer");
        deck.add("hammer");
        deck.add("sword");
        deck.add("sword");
        deck.add("sword");
        deck.add("arrow");
        deck.add("arrow");
        deck.add("arrow");

        Collections.shuffle(deck);

        return deck;
    }
}
