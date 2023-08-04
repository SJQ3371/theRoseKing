package commons.api;

import commons.model.PowerCard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface GameApi extends Remote {

    /**
     * Gets the crown's position X
     *
     * @return x
     */
    int getCrownX() throws RemoteException;

    /**
     * Gets the crown's position Y
     *
     * @return y
     */
    int getCrownY() throws RemoteException;

    /**
     * Get board layout
     *
     * @return Checkerboard layout two-dimensional array
     */
    int[][] getGameGrids() throws RemoteException;

    /**
     * Get a character's card
     *
     * @param color Player Color
     * @return PowerCard Map
     */
    Map<PowerCard, Integer> getPowerCard(String color) throws RemoteException;

    /**
     * Draw Card
     *
     * @param color Player Color
     */
    void drawCard(String color) throws RemoteException;

    /**
     * Play the PowerCard
     *
     * @param color  Player Color
     * @param card   PowerCard
     * @param isHero Whether to use the hero card
     * @return Success or not
     */
    boolean playCard(String color, PowerCard card, boolean isHero) throws RemoteException;

    /**
     * Get the color of the client, the first is the red square, the last is the white square
     *
     * @param clientId client id
     * @return Play Color
     */
    String getColor(String clientId) throws RemoteException;

    /**
     * Gets the current action of the player
     *
     * @return Current player color
     * @throws RemoteException rmi
     */
    String getPlayColor() throws RemoteException;

    /**
     * Determine if the game is over
     *
     * @return Whether game is over
     * @throws RemoteException RMI
     */
    boolean isGameOver() throws RemoteException;

    /**
     * Gets the number of heroes on a party
     *
     * @param color Player Color
     * @return Number of heroes
     * @throws RemoteException RMI
     */
    int getHeroCount(String color) throws RemoteException;

    /**
     * Switch current player
     *
     * @throws RemoteException RMI
     */
    void swap() throws RemoteException;

    /**
     * Disconnect and destroy the game!
     *
     * @throws RemoteException RMI
     */
    void closeLink() throws RemoteException;

    /**
     * Score a game
     *
     * @param color Player Color
     * @return score
     */
    int getScore(String color) throws RemoteException;

    /**
     * Gets the number of pieces for a side
     *
     * @param color Player Color
     * @return Coin count
     */
    int getCoinCount(String color) throws RemoteException;
}
