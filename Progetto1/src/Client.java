import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable{
    public static void main(String[] args){
        try {
            Thread[] threads = new Thread[1];
            for(int i = 0; i < threads.length; i++){
                String name = "t" + i;
                threads[i] = new Thread(new Client(), name);
                threads[i].start();
            }
            for(int i = 0; i < threads.length; i++){
                threads[i].join();
            }
            System.out.println("Thread main terminato");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run(){
        try {
            Socket client = new Socket("localhost", 5353);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter out = new PrintWriter(client.getOutputStream());
            System.out.println("Collegato all'host " + client.getInetAddress());
            String response = "";
            while(true){
                response = in.readLine();
                if(response.equals("EOF"))
                    break;
                System.out.println(response);
            }
            out.println("lookup: contoCorrenteMiaBanca");
            out.flush();
            response = in.readLine();
            System.out.println(response);
            out.close();
            in.close();
            client.close();
            System.out.println("Connessione chiusa");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
