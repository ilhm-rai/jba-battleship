package battleship;

public enum ShipEnum {

    AIRCRAFT_CARRIER("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    SUBMARINE("Submarine", 3),
    CRUISER("Cruiser", 3),
    DESTROYER("Destroyer", 2);

    private String name;
    private int unit;

    ShipEnum(String name, int unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public int getUnit() {
        return unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }
}
