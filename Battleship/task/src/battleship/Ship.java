package battleship;

public class Ship {

    private String name;
    private int unit;
    static int totalUnit = 0;

    public Ship(String name, int unit) {
        this.name = name;
        this.unit = unit;
        totalUnit += unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
