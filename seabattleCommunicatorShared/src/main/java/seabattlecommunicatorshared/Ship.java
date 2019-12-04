package seabattlecommunicatorshared;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private String name;
    private ShipType type;
    List<Square> squares = new ArrayList<Square>();

    public String getName() {
        return name;
    }

    public void addSquare(Square square) {
        this.squares.add(square);
    }

    public ShipType getType() {
        return type;
    }

    public Ship(String name, ShipType type){
        this.name = name;
        this.type = type;
    }

    public List<Square> getSquares() {
        return squares;
    }

    public boolean isDestroyed() {
        return true;
    }
}
