package blog.syssoft;

import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Set;

public class ClientProxy {

    public ClientProxy(Socket c, Map<String, ClientProxy> boxMap, Set<String> possibleBoxnames) {
        this.c = c;
        this.boxMap = boxMap;
        this.possibleBoxnames = possibleBoxnames;
        try {
            in = new BufferedReader(new InputStreamReader(c.getInputStream(), "UTF-8"));
        } catch (Exception e) {
            fatal(e, "Unable to bind BufferedReader to input stream of socket");
        }
        try {
            out = new PrintWriter(c.getOutputStream(),true,Charset.forName("UTF-8"));
        } catch (Exception e) {
            fatal(e, "Unable to bind PrinterWriter to output stream of socket");
        }
        String initialMessage = readLine();
        System.out.printf("ClientProxy (%s): receiving initial message \"%s\"\n",
                c.getRemoteSocketAddress().toString(), initialMessage);
        if (initialMessage != null) {
            String[] config = initialMessage.split(",");
            if (config.length == 3) {
                Name = config[0].toUpperCase();
                if (possibleBoxnames.contains(Name)) {
                    Address = config[1];
                    Port = config[2];
                    if (!boxMap.containsKey(Name)) {
                        goodclient = true;
                        out.println("OK");
                    }
                    else
                        out.println("Someone else is responsible for this box name");
                }
                else {
                    if (Name.equals("TEST"))
                            out.println("OK");
                    else
                        out.println("Box name <" + Name + " invalid");
                }
            }
            else
                out.println("Format Boxname,IP,Port expected");
        }
        else
            out.println("Empty string received");
    }

    public SocketAddress getRemoteSocketAddress () {
        return c.getRemoteSocketAddress();
    }

    private Map<String, ClientProxy> boxMap;
    private Set<String> possibleBoxnames;
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
        t = new Thread(() -> {
            handleClient();
        });
        t.start();
    }

    public void join () {
        try {
            t.join();
        } catch (Exception e) {
            fatal(e,"Unable to join a thread");
        }
    }
    
    private void handleClient() {
        while (true) {
            String message = readLine();
            System.out.printf("ClientProxy for box %s received \"%s\"\n", Name, message);
            message = message.toUpperCase();
            if (!message.startsWith("RESULT,")) {
                ClientProxy cp = boxMap.get(message);
                String answer = "Requested box not found";
                if (cp != null)
                    answer = cp.getAddress() + ',' + cp.getPort();
                System.out.printf("ClientProxy for box %s sends \"%s\"\n", Name, answer);
                out.println(answer);
            }
            else {
                result = message;
                break;
            }
        }
    }

    private String result;

    public String getResult() {
        return result;
    }

    public void close () {
        try {
            c.close();
        }
        catch (Exception e) {
            fatal(e,"Error while closing socket");
        }
    }
}
