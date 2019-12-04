package seabattlecommunicator;

import seabattlecommunicatorshared.PlaceShipDTO;
import seabattlecommunicatorshared.Ship;
import seabattlecommunicatorshared.ShipType;

public class CommunicatorMessage {
    private PlaceShipDTO placeShipDTO;
    private ShipType shipType;
    private Ship ship;
    private int playerNr;
    private int posX;
    private int posY;
    private Boolean horizontal;

    public CommunicatorMessage() {

    }

    public void setPlaceShipDTO(int playerNr, ShipType shipType, int posX, int posY, Boolean horizontal){
        placeShipDTO = new PlaceShipDTO(playerNr,shipType,posX,posY,horizontal);
    }

    public PlaceShipDTO getPlaceShipDTO() {
        return placeShipDTO;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setPlayerNr(int playerNr) {
        this.playerNr = playerNr;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getPlayerNr() {
        return playerNr;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public Boolean getHorizontal(){
        return horizontal;
    }

    public void setHorizontal(Boolean horizontal) {
        this.horizontal = horizontal;
    }

}
