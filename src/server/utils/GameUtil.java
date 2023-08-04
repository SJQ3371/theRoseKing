package server.utils;

import commons.model.PowerCard;

import java.util.ArrayList;
import java.util.List;

public class GameUtil {


    /**
     * Create power cards and shuffle them
     *
     * @return Power card after shuffling
     */
    public List<PowerCard> createPowerCards() {
        List<PowerCard> powerCardList = new ArrayList<>();
        int[] _x = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] _y = {-1, 0, 1, -1, 1, -1, 0, 1};
        String[] label = {"Move up left", "Move left", "Move down left", "Move up", "Move down", "Move up right", "Move Right", "Move Right Down"};
        for (int i = 0; i < 8; i++) {
            powerCardList.add(new PowerCard(1, _x[i], _y[i], "I." + label[i]));
            powerCardList.add(new PowerCard(2, _x[i], _y[i], "II." + label[i]));
            powerCardList.add(new PowerCard(3, _x[i], _y[i], "III." + label[i]));
        }
        // 洗牌
        powerCardList.sort((o1, o2) -> Double.compare(Math.random(), 0.5));
        return powerCardList;
    }


}
