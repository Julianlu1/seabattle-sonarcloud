package seabattlecommunicatorshared;


public class Square {
    private SquareState state;
    private ShotType shotType;
    private int positionX;
    private int positionY;

    public Square(SquareState state, int positionX, int positionY){
       this.state = state;
       this.positionX = positionX;
       this.positionY = positionY;
        //reee = rectangle;
    }

    public void setSquareState(SquareState squareState) {
        this.state = squareState;
       /*
        switch (squareState) {
            case SHIP:
                reee.setFill(Color.DARKGRAY);
                break;
            case SHOTMISSED:
                reee.setFill(Color.BLUE);
                break;
            case SHOTHIT:
                reee.setFill(Color.RED);
                break;
            case SHIPSUNK:
                reee.setFill(Color.GREEN);
                break;
            default:
                reee.setFill(Color.LIGHTBLUE);
                break;
        }

        */
    }

    public SquareState getState() {
        return state;
    }

    public void setState(SquareState state) {
        this.state = state;
    }

    public ShotType getShotType() {
        return shotType;
    }

    public void setShotType(ShotType shotType) {
        this.shotType = shotType;
    }

    public int getPositionX() {
        //return (int)reee.getX();
        return positionX;
    }

    public int getPositionY() {
        //return (int)reee.getY();
        return positionY;
    }
}
