package seabattlecommunicatorshared;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private boolean playerReady;

    private Grid grid;
    private List<Ship> ships = new ArrayList<>();

    public Player(String name, boolean playerReady){
        this.name = name;
        this.playerReady = playerReady;

        // Zet alle schepen van de Enum in de lijst met schepen van speler
        for(ShipType shipType : ShipType.values()){
            ships.add(new Ship(shipType.name(),shipType));
        }
    }

    public Ship getShipByType(ShipType shipType){
        for(Ship ship : ships){
            if(ship.getType().equals(shipType)){
                return ship;
            }
        }
        return null;
    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    public Grid getGrid() {
        return grid;
    }

    public String getName() {
        return name;
    }

    public boolean getPlayerReady(){
        return playerReady;
    }
}
