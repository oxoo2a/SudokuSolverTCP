package blog.syssoft;

import java.net.*;

public class TCPServer {
    public TCPServer ( int port ) {
        this.port = port;
        try {
            s = new ServerSocket(port);
        } catch (Exception e) {
            fatal(e,"While trying to create server socket");
        }
    }

    public Socket acceptClient () {
        Socket client = null;
        try {
            client = s.accept();
        } catch (Exception e) {
            fatal(e,"While trying to accept client connection");
        }
        return client;
    }

    private static void fatal(Exception e, String comment ) {
        System.out.println("Exception caught: " + e.getMessage());
        System.out.println(comment);
        e.printStackTrace();
        System.exit(-1);
    }

    private ServerSocket s;
    private int port;
}
