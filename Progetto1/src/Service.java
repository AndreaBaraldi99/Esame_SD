import java.net.InetSocketAddress;

public class Service {

    private String name;
    private String ip;
    private int port;
    private InetSocketAddress address;
    private String protocol;

    public Service(String name, String ip, int port, String protocol) {
        this.name = name;
        this.ip = ip;
        this.port = port;
        this.protocol = protocol;
        this.address = new InetSocketAddress(ip, port);
    }

    public String getName() {
        return name;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }
}
