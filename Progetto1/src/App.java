import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
    public static void main(String[] args) throws IOException, InterruptedException {
        ServerSocket serverSocker = new ServerSocket(Registry.port);
        Socket clientSocket;
        BufferedReader in;
        PrintWriter out;
        Registry registry = new Registry();

        while(true){
            clientSocket = serverSocker.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out.println(registry.protocol());
            Thread.sleep(1000); // wait for the client to read the message
            String response = in.readLine(); // read the service name
            String[] service = response.split(": ");
            if(service[0].equals("bind")){
                String[] params = service[1].split(", ");
                if(registry.bind(params[0], params[1], Integer.parseInt(params[2]), params[3])){
                    out.println("Servizio registrato con successo");
                }
                else{
                    out.println("Servizio non registrato");
                }
            }
            else if(service[0].equals("unbind")){
                String[] params = service[1].split(", ");
                if(registry.unbind(params[0])){
                    out.println("Servizio rimosso con successo");
                }
                else{
                    out.println("Servizio non rimosso");
                }
            }
            else if(service[0].equals("rebind")){
                String[] params = service[1].split(", ");
                if(registry.rebind(params[0], params[1], Integer.parseInt(params[2]), params[3])){
                    out.println("Servizio modificato con successo");
                }
                else{
                    out.println("Servizio non modificato");
                }
            }
            else if(service[0].equals("lookup")){
                Service serviceFound = registry.lookup(service[1]);
                if(serviceFound != null){
                    out.println("Servizio trovato: " + serviceFound.getName() + ", " + serviceFound.getIp() + ", " + serviceFound.getPort() + ", " + serviceFound.getProtocol());
                }
                else{
                    out.println("Servizio non trovato");
                }
            }
            else{
                out.println("Servizio non trovato");
            }
            out.flush();
            Thread.sleep(1000); // wait for the client to read the message
            in.close();
            out.close();
            clientSocket.close();
        }
    }
}
