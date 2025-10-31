
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class that manages the GUI client
 */
public class Client {

    public static void main(String[] args) {

        GUI gui = new GUI();
        String returnValue;

        String hostName="localhost";
        int portNumber=35754;
        try (
                Socket conn=new Socket(hostName, portNumber);
                PrintWriter sockOut=new PrintWriter(conn.getOutputStream(),true);
                BufferedReader sockIn=new BufferedReader(new InputStreamReader(conn.getInputStream()));
        ) {
            String fromServer;
            char[][] gameboard = new char[19][40];

            game:
            while (true) {

                gui.updateButtons();
                if(gameboard[4][38] == '-' && gameboard[2][38] == '-') break;

                for(int i = 0; i < 19; i++) {
                    fromServer = sockIn.readLine();

                    // Incase if the other player leaves the game. Stop the GUI
                    if(fromServer == null) break game;

                    if(fromServer.equals("/--------------------------------------\\") && i != 0){
                        i = 0;
                        continue;
                    }

                    gameboard[i] = fromServer.toCharArray();
                }

                // If it's not our turn then wait for input by going back to the start of the while loop
                if(gameboard[4][38] != 'v') {
                    gui.turn = false;
                    gui.updateLayout(gameboard);
                    continue;
                }

                gui.turn = true;
                gui.updateButtons();
                gui.updateLayout(gameboard);

                while(true) {

                    sockOut.flush();

                    if((returnValue = gui.checkForMove()) != ""){
                        sockOut.println(returnValue);
                        break;
                    }
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("I think there's a problem with the host name.");
        } catch (IOException e) {
            System.out.println("Had an IO error for the connection.");
        }
    }
}
