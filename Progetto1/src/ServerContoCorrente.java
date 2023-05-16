import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerContoCorrente implements Runnable{

    private List<ContoCorrente> conti = new ArrayList<ContoCorrente>();
    private Socket threadClientSocket;
    private BufferedReader threadIn;
    private PrintWriter threadOut;
    private static final String protocol = "Servizi offerti: "
                        + "nuovoCliente: crea un nuovo conto corrente. Protocollo: nuovoCliente: codiceCliente1, codiceCliente2, ..."
                        + "versa: permette di versare un importo sul conto. Protocollo: versa: idConto, codiceCliente, importo."
                        + "preleva: permette di prelevare un importo dal conto. Protocollo: preleva: idConto, codiceCliente, importo."
                        + "bonifico: permette di effettuare un bonifico da un conto all'altro. Protocollo: bonifico: idContoPrelievo, codiceClientePrelievo, idContoVersamento, importo."
                        + "EOF";

    public ServerContoCorrente(Socket clientSocket){
        this.threadClientSocket = clientSocket;
    }
    
    public static void main(String[] args) throws IOException{
        ServerSocket serverSocker = new ServerSocket(5656);
        Thread[] threads = new Thread[4];
        Socket clientSocket;
        BufferedReader in;
        PrintWriter out;
        Boolean flag = false;
        Socket registry = new Socket("localhost", Registry.port);
        in = new BufferedReader(new InputStreamReader(registry.getInputStream()));
        out = new PrintWriter(registry.getOutputStream(), true);
        String response = "";
        while(true){
            response = in.readLine();
            if(response.equals("EOF"))
                break;
            System.out.println(response);
        } // read the protocol
        out.println("bind: contoCorrenteMiaBanca, localhost, 5656, " + protocol);
        out.flush();
        response = in.readLine(); // read the response
        System.out.println(response);
        out.close();
        in.close();
        registry.close();

        while(true){
            clientSocket = serverSocker.accept();
            System.out.println("Client connesso");
            while(!flag){
                for(int i = 0; i < threads.length; i++){
                    if(threads[i] == null || !threads[i].isAlive()){
                        threads[i] = new Thread(new ServerContoCorrente(clientSocket));
                        threads[i].start();
                        flag = true;
                        break;
                    }
                }
            }
            flag = false;            
        }
    }

    public int nuovoCliente(int[] codiciClienti){
        ContoCorrente conto = new ContoCorrente(codiciClienti);
        conti.add(conto);
        return conto.getCodiceConto();
    }

    public boolean versa(int idConto, int codiceCliente, double quantita){
        for(ContoCorrente conto : conti){
            if(conto.getCodiceConto() == idConto){
                if(conto.getCliente(codiceCliente)){
                    return conto.versa(quantita);
                }
            }
        }
        return false;
    }

    public boolean preleva(int idConto, int codiceCliente, double quantita){
        for(ContoCorrente conto : conti){
            if(conto.getCodiceConto() == idConto){
                if(conto.getCliente(codiceCliente)){
                    return conto.preleva(quantita);
                }
            }
        }
        return false;
    }

    public boolean bonifico(int idContoPrelievo, int codiceClientePrelievo, int idContoVersamento, double quantita){
        if(preleva(idContoPrelievo, codiceClientePrelievo, quantita)){
            return versa(idContoPrelievo, codiceClientePrelievo, quantita);
        }
        return false;
    }

    @Override
    public void run() {
        try {
            threadOut = new PrintWriter(threadClientSocket.getOutputStream(), true);
            threadIn = new BufferedReader(new InputStreamReader(threadClientSocket.getInputStream()));
            threadOut.println(protocol);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
