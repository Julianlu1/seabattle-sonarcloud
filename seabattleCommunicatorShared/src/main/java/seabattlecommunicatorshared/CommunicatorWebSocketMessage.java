package seabattlecommunicatorshared;

public class CommunicatorWebSocketMessage {
    private CommunicatorWebSocketMessageOperation operation;
    private PlaceShipDTO placeShipDTO;
    private Ship ship;
    private ShipType shipType;
    private int playerNr;
    private int posX;
    private int posY;
    private Boolean horizontal;
    private String name = "Henk";

    public String getName() {
        return name;
    }

    public CommunicatorWebSocketMessage(CommunicatorWebSocketMessageOperation operation, PlaceShipDTO placeShipDTO){
        this.operation = operation;
        this.placeShipDTO = placeShipDTO;
    }

    public CommunicatorWebSocketMessage(int playerNr, ShipType shipType, int posX, int posY, Boolean horizontal){
        this.playerNr = playerNr;
        this.shipType = shipType;
        this.posX = posX;
        this.posY = posY;
        this.horizontal = horizontal;
    }

    public CommunicatorWebSocketMessageOperation getOperation() {
        return operation;
    }


    public PlaceShipDTO getPlaceShipDTO() {
        return placeShipDTO;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public void setPlaceShipDTO(PlaceShipDTO placeShipDTO) {
        this.placeShipDTO = placeShipDTO;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Boolean getHorizontal() {
        return horizontal;
    }

    public void setOperation(CommunicatorWebSocketMessageOperation operation) {
        this.operation = operation;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPlayerNr(int playerNr) {
        this.playerNr = playerNr;
    }

    public int getPosY() {
        return posY;
    }

    public int getPosX() {
        return posX;
    }

    public int getPlayerNr() {
        return playerNr;
    }
}
