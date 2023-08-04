package server;

import commons.api.InitApi;
import server.api.InitApiImpl;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ApiServer {
    public static void main(String[] args) {
        try {
            InitApi initApi = new InitApiImpl();
            LocateRegistry.createRegistry(1099);
            Naming.bind("rmi://localhost:1099/api", initApi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
