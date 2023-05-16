import java.util.ArrayList;
import java.util.List;

public class Registry {

    public static int port = 5353;
    public static String ip = "localhost";
    private List<Service> services = new ArrayList<Service>();

    public Boolean bind(String nomeServizio, String ip, int porta, String protocollo){
        for(Service service : services){
            if(service.getName().equals(nomeServizio)){
                return false;
            }
            else if(service.getPort() == porta){
                return false;
            }
        }
        services.add(new Service(nomeServizio, ip, porta, protocollo));
        return true;
    }

    public Boolean unbind(String nomeServizio){
        for(Service service : services){
            if(service.getName().equals(nomeServizio)){
                services.remove(service);
                return true;
            }
        }
        return false;
    }

    public Boolean rebind(String nomeServizio, String ip, int porta, String protocollo){
        for(Service service : services){
            if(service.getName().equals(nomeServizio)){
                services.remove(service);
                services.add(new Service(nomeServizio, ip, porta, protocollo));
                return true;
            }
        }
        return false;
    }

    public Service lookup(String nomeServizio){
        for(Service service : services){
            if(service.getName().equals(nomeServizio)){
                return service;
            }
        }
        return null;
    }

    public String protocol(){
        return "Servizi offerti:\n"
                + "bind: permette di registrare un servizio nel registro.\n Protocollo: bind: nomeServizio, ip, porta, protocollo\n"
                + "unbind: permette di rimuovere un servizio dal registro.\n Protocollo: unbind: nomeServizio\n"
                + "rebind: permette di modificare un servizio già registrato.\n Protocollo: rebind: nomeServizio, ip, porta, protocollo\n"
                + "lookup: permette di cercare un servizio nel registro.\n Protocollo: lookup: nomeServizio\n"
                + "EOF";
    }

}
