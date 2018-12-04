package blog.syssoft;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Map;

public class ClientProxy {

    public ClientProxy(Socket c, Map<String, ClientProxy> boxMap) {
        this.c = c;
        this.boxMap = boxMap;
        try {
            in = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"));
        } catch (Exception e) {
            fatal(e, "Unable to bind BufferedReader to input stream of socket");
        }
        try {
            out = new PrintWriter(c.getOutputStream());
        } catch (Exception e) {
            fatal(e, "Unable to bind PrinterWriter to output stream of socket");
        }
        String initialMessage = readLine();
        System.out.printf("ClientProxy (%s): receiving initial message \"%s\"\n",
                c.getRemoteSocketAddress().toString(), initialMessage);
        if (initialMessage != null) {
            String[] config = initialMessage.split(",");
            if (config.length == 3) {
                Name = config[0];
                Address = config[1];
                Port = config[2];
                goodclient = true;
            }
        }
    }

    public SocketAddress getRemoteSocketAddress () {
        return c.getRemoteSocketAddress();
    }

    private Map<String, ClientProxy> boxMap;
    private Thread t;

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getPort() {
        return Port;
    }

    private String Name = "NONE";
    private String Address = "NONE";
    private String Port = "NONE";

    public boolean isGoodclient() {
        return goodclient;
    }

    private boolean goodclient = false;

    public String readLine() {
        String message = null;
        try {
            message = in.readLine();
            System.out.printf("ClientProxy receiving \"%s\"\n", message);
        } catch (Exception e) {
            // fatal(e,"Error while reading");
        }
        return message;
    }

    private Socket c;

    private BufferedReader in;
    private PrintWriter out;

    private static void fatal(Exception e, String comment) {
        System.out.println("Exception caught: " + e.getMessage());
        System.out.println(comment);
        e.printStackTrace();
        System.exit(-1);
    }

    public void start() {
        Thread t = new Thread(() -> {
            handleClient();
        });
        t.start();
    }

    private void handleClient() {
        while (true) {
            String message = readLine();
            System.out.printf("ClientProxy for box %s received \"%s\"\n", Name, message);
            ClientProxy cp = boxMap.get(message);
            String answer = "Request box not found";
            if (cp != null)
                answer = cp.getAddress() + ',' + cp.getPort();
            System.out.printf("ClientProxy for box %s sends \"%s\"\n", Name, answer);
            out.println(answer);
        }
    }

}
