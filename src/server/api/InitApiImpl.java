package server.api;

import commons.api.GameApi;
import commons.api.InitApi;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class InitApiImpl extends UnicastRemoteObject implements InitApi {
    Map<String, GameApi> map = new HashMap<>();

    public InitApiImpl() throws RemoteException {
    }

    @Override
    public void createGame(String name) throws RemoteException {
        try {
            if (map.containsKey(name)) {
                if (map.get(name).isGameOver()) {
                    GameApi gameApi = new GameApiImpl();
                    map.put(name, gameApi);
                    Naming.rebind("rmi://localhost:1099/" + name, gameApi);
                }
                return;
            }
            GameApi gameApi = new GameApiImpl();
            map.put(name, gameApi);
            Naming.bind("rmi://localhost:1099/" + name, gameApi);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
