package seabattlecommunicatorshared;

public class PlaceShipDTO {
    private int playerNr;
    private ShipType shipType;
    private int bowX;
    private int bowY;
    private Boolean horizontal;

    public PlaceShipDTO(int playerNr, ShipType shiptype, int bowX, int bowY, Boolean horizontal) {
        this.playerNr = playerNr;
        this.shipType = shiptype;
        this.bowX = bowX;
        this.bowY = bowY;
        this.horizontal = horizontal;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public int getPlayerNr() {
        return playerNr;
    }

    public Boolean getHorizontal() {
        return horizontal;
    }

    public int getBowX() {
        return bowX;
    }

    public int getBowY() {
        return bowY;
    }
}
