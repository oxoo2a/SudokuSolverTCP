package blog.syssoft;

import java.io.*;
import java.net.*;
import java.util.Map;

public class ClientProxy {

    public ClientProxy (Socket c, Map<String,ClientProxy> boxMap) {
        this.c = c;
        this.boxMap = boxMap;
        try {
                in = new BufferedReader(new InputStreamReader(c.getInputStream()));
        }
        catch (Exception e) {
            fatal(e,"Unable to bind BufferedReader to input stream of socket");
        }
        try {
            out = new PrintWriter(c.getOutputStream());
        }
        catch (Exception e) {
            fatal(e,"Unable to bind PrinterWriter to output stream of socket");
        }
        String initialMessage = readLine();
        String[] config = initialMessage.split(",");
        Name = config[0];
        Address = config[1];
        Port = config[2];
    }

    private Map<String,ClientProxy> boxMap;
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

    private String Name;
    private String Address;
    private String Port;

    public String readLine () {
        String message = null;
        try {
            message = in.readLine();
        }
        catch (Exception e) {
            fatal(e,"Error while reading");
        }
        return message;
    }

    private Socket c;

    private BufferedReader in;
    private PrintWriter out;

    private static void fatal(Exception e, String comment ) {
        System.out.println("Exception caught: " + e.getMessage());
        System.out.println(comment);
        e.printStackTrace();
        System.exit(-1);
    }

    public void start () {
        Thread t = new Thread(() -> { handleClient();});
        t.start();
    }

    private void handleClient () {
        while (true) {
            String message = readLine();
            System.out.printf("ClientProxy for box %s received \"%s\"\n",Name,message);
            ClientProxy cp = boxMap.get(message);
            String answer = "Request box not found";
            if (cp != null)
                answer = cp.getAddress()+','+cp.getPort();
            System.out.printf("ClientProxy for box %s sends \"%s\"\n",Name,answer);
            out.println(answer);
        }
    }

}
