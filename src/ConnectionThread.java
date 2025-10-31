import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Thread which facilitates a game between 2 people
 */
public class ConnectionThread extends Thread{
    private Socket client1,client2;

    public ConnectionThread(Socket c1, Socket c2) {
        client1=c1; client2=c2;
    }

    public void run() {
        try (
                // Set up inputs and outputs for both clients
                PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);
                PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);
                BufferedReader in1 = new BufferedReader(new InputStreamReader(client1.getInputStream()));
                BufferedReader in2 = new BufferedReader(new InputStreamReader(client2.getInputStream()));
        ) {
            new Game(in1, in2, out1, out2);
        } catch (IOException e) {
            System.out.println("Ah nertz.");
        }
    }
}