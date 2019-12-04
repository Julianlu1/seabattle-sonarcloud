package seabattlecommunicatorshared;


import java.util.ArrayList;
import java.util.List;

public class Grid {
    private String name;
    private int shipCounter;

    private List<Square> squares = new ArrayList<>();

    public void setSquares(List<Square> squares) {
        this.squares = squares;
    }

    public Grid(String name, int shipCounter){
        this.name = name;
        this.shipCounter = shipCounter;

        for(int i = 0 ; i < 100 ; i++){
            for(int x = 0 ; x < 100; x++){
                Square s = new Square(SquareState.WATER,i,x);
                squares.add(s);
            }
        }
    }

    public List<Square> getSquares() {
        return squares;
    }

    public String getName() {
        return name;
    }

    public int getShipCounter() {
        return shipCounter;
    }

    public void placeShip(Ship ship, int positionY, int positionX){

    }
    public void removeShip(Ship ship){

    }

    public SquareState shoot(int positionY, int positionX){
        return null;
    }
}
