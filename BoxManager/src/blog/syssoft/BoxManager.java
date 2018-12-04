package blog.syssoft;

import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BoxManager {

    public static void main(String[] args) {
        System.out.println("Sudoku Solver - blog.syssoft.BoxManager");

        if (args.length != 1) {
            System.err.println("Usage: blog.syssoft.BoxManager <port>");
            System.exit(-1);
        }

        int port = Integer.parseInt(args[0]);

        TCPServer server = new TCPServer(port);

        int nBoxesConnected = 0;

        while (nBoxesConnected < 9) {
            Socket c = server.acceptClient();
            ClientProxy cp = new ClientProxy(c,boxMap);
            clients.put(c,cp);
            if (cp.isGoodclient()) {
                System.out.printf("Box %s at %s and port %s\n",
                        cp.getName(), cp.getAddress(), cp.getPort());
                boxMap.put(cp.getName(), cp);
                nBoxesConnected++;
            }
            else {
                System.out.println("Client with inappropriate initial message tried to connect");
                cp.close();
            }
        }

        System.out.print("All 9 boxes are running ... start answering queries");

        for (Map.Entry<String,ClientProxy> b : boxMap.entrySet()) {
            b.getValue().start();
        }
    }

    private static Map<Socket,ClientProxy> clients = new HashMap<Socket,ClientProxy>();
    private static Map<String,ClientProxy> boxMap = new HashMap<String,ClientProxy>();
}
