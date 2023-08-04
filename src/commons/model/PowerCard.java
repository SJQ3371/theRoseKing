package commons.model;

import java.io.Serializable;
import java.util.Objects;

public class PowerCard implements Serializable {
    private int level;
    private int x;
    private int y;
    private String label;

    public PowerCard(int level, int x, int y, String label) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public int getLevel() {
        return level;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerCard powerCard = (PowerCard) o;
        return Objects.equals(label, powerCard.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        return "PowerCard{" +
                "level=" + level +
                ", x=" + x +
                ", y=" + y +
                ", label='" + label + '\'' +
                '}';
    }
}
