package commons.api;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InitApi extends Remote {
    /**
     * Create a game and mount it to RMI
     *
     * @param name room name
     * @throws RemoteException RMI
     */
    void createGame(String name) throws RemoteException;
}
