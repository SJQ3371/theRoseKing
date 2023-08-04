package server.api;

import commons.api.GameApi;
import commons.model.PowerCard;
import server.utils.GameUtil;
import server.utils.ScoreUtil;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class GameApiImpl extends UnicastRemoteObject implements GameApi, Serializable {
    private int whiteHeroCount, redHeroCount, crownX, crownY, whiteCoinCount = 26, redCoinCount = 26;
    private List<PowerCard> whitePowerCard, redPowerCard, foldCard;
    private Stack<PowerCard> powerCards;
    private String redClientId, playColor = "RED";
    private boolean gameOver;
    // 游戏布局
    // 0空1红2白
    private int[][] gameGrids;


    public GameApiImpl() throws RemoteException {
        GameUtil util = new GameUtil();
        whiteHeroCount = 4;
        redHeroCount = 4;
        // 最开始的皇冠居中 9行 索引0 所以4是居中的位置
        crownX = 4;
        crownY = 4;
        // 获取能量牌卡片
        powerCards = new Stack<>();
        powerCards.addAll(util.createPowerCards());
        foldCard = new ArrayList<>();
        whitePowerCard = new ArrayList<>();
        redPowerCard = new ArrayList<>();

        // 初始化红白两方分配随机的卡牌
        powerCards.removeIf(powerCard -> {
            if (whitePowerCard.size() < 5) {
                whitePowerCard.add(powerCard);
            } else if (redPowerCard.size() < 5) {
                redPowerCard.add(powerCard);
            } else {
                return false;
            }
            return true;
        });

        gameGrids = new int[9][];
        for (int i = 0; i < 9; i++) {
            gameGrids[i] = new int[9];
            for (int j = 0; j < 9; j++) {
                gameGrids[i][j] = 0;
            }
        }
    }

    @Override
    public int getCrownX() throws RemoteException {
        return crownX;
    }

    public int getCrownY() throws RemoteException {
        return crownY;
    }

    public int[][] getGameGrids() throws RemoteException {
        return gameGrids;
    }


    public Map<PowerCard, Integer> getPowerCard(String color) throws RemoteException {
        Map<PowerCard, Integer> map = new HashMap<>();

        for (PowerCard card : color.equals("WHITE") ? whitePowerCard : redPowerCard) {
            if (!map.containsKey(card)) {
                map.put(card, 0);
            }
            map.put(card, map.get(card) + 1);
        }
        return map;
    }

    // 抓牌
    public void drawCard(String color) throws RemoteException {
        if (this.powerCards.size() == 0) {
            // 如果没有卡牌
            this.powerCards.addAll(this.foldCard);
            this.foldCard.clear();
        }


        PowerCard card = this.powerCards.pop();
        if (color.equals("WHITE")) {
            this.whitePowerCard.add(card);
        } else {
            this.redPowerCard.add(card);
        }
    }


    public boolean playCard(String color, PowerCard card, boolean isHero) throws RemoteException {

        // 校验是否有英雄牌
        if (isHero && color.equals("RED") && redHeroCount == 0) return false;
        if (isHero && color.equals("WHITE") && whiteHeroCount == 0) return false;

        boolean isSuccess = false;
        int newX = crownX, newY = crownY;

        // 循环移动
        for (int i = 0; i < card.getLevel(); i++) {
            newX += card.getX();
            newY += card.getY();
        }

        // 越界
        if (newX < 0 || newX >= 9 || newY < 0 || newY >= 9) {
            return false;
        }

        if (isHero) {
            isSuccess = true;
            gameGrids[newY][newX] = color.equals("RED") ? 1 : 2;
        } else {
            if (gameGrids[newY][newX] == 0) {
                isSuccess = true;
                gameGrids[newY][newX] = color.equals("RED") ? 1 : 2;
            }
        }

        if (isSuccess) {
            crownX = newX;
            crownY = newY;
            // 删除卡牌
            (color.equals("RED") ? redPowerCard : whitePowerCard).removeIf(c -> c.equals(card));
            if (color.equals("RED")) redCoinCount--;
            if (color.equals("WHITE")) whiteCoinCount--;
            if (isHero && color.equals("RED")) redHeroCount--;
            if (isHero && color.equals("WHITE")) whiteHeroCount--;
            // 舍弃牌堆
            foldCard.add(card);
        }
        return isSuccess;
    }


    @Override
    public String getColor(String clientId) throws RemoteException {
        if (redClientId == null) {
            redClientId = clientId;
        }
        return redClientId.equals(clientId) ? "RED" : "WHITE";
    }

    @Override
    public String getPlayColor() throws RemoteException {
        return playColor;
    }

    @Override
    public boolean isGameOver() throws RemoteException {
        return checkOver(playColor) || gameOver;
    }

    @Override
    public int getHeroCount(String color) throws RemoteException {
        if (color.equals("WHITE"))
            return whiteHeroCount;
        return redHeroCount;
    }

    @Override
    public void swap() throws RemoteException {
        this.playColor = this.playColor.equals("WHITE") ? "RED" : "WHITE";
    }

    @Override
    public void closeLink() throws RemoteException {
        gameOver = true;
    }

    @Override
    public int getScore(String color) throws RemoteException {
        return ScoreUtil.getScore(gameGrids, color.equals("RED") ? 1 : 2);
    }

    @Override
    public int getCoinCount(String color) {
        int value = color.equals("RED") ? 1 : 2;
        int count = 0;
        for (int[] gameGrid : gameGrids) {
            for (int i : gameGrid) {
                if (i == value) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean checkMove(String color, PowerCard card, boolean isHero) {
        // 校验是否有英雄牌
        if (isHero && color.equals("RED") && redHeroCount == 0) return false;
        if (isHero && color.equals("WHITE") && whiteHeroCount == 0) return false;
        boolean isSuccess = false;
        int newX = crownX, newY = crownY;
        // 循环移动
        for (int i = 0; i < card.getLevel(); i++) {
            newX += card.getX();
            newY += card.getY();
        }
        // 越界
        if (newX < 0 || newX >= 9 || newY < 0 || newY >= 9) {
            return false;
        }
        if (isHero) {
            isSuccess = true;
        } else {
            if (gameGrids[newY][newX] == 0) {
                isSuccess = true;
            }
        }
        return isSuccess;
    }


    private boolean checkOver(String color) throws RemoteException{
        int count = color.equals("WHITE") ? whiteCoinCount : redCoinCount;
        if (count == 0) {
            return true;
        }
        List<PowerCard> powerCards = color.equals("WHITE") ? whitePowerCard : redPowerCard;
        // 如果可以抓牌的话
        if (powerCards.size() < 5) return false;
        int heroCount = color.equals("WHITE") ? whiteHeroCount : redHeroCount;
        for (PowerCard card : powerCards) {
            // 如果有可以移动的话
            if (checkMove(color, card, heroCount > 0)) {
                return false;
            }
        }
        return true;
    }
}
