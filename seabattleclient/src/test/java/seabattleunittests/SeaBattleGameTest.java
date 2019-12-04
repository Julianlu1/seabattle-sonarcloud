/*
 * Sea Battle Start project.
 */
package seabattleunittests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import seabattlecommunicatorshared.*;
import seabattlegame.ISeaBattleGame;
import seabattlegame.SeaBattleGame;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Unit tests for Sea Battle game.
 * @author Nico Kuijpers
 */
class SeaBattleGameTest {
    
    private ISeaBattleGame game;
    private Game game2;
    private MockSeaBattleApplication applicationPlayer;
    private MockSeaBattleApplication applicationOpponent;
    
    SeaBattleGameTest() {
    }

    @BeforeEach
    void setUp() {
        
        // Create the Sea Battle game
        game = new SeaBattleGame();
        game2 = new Game();
        Player player = new Player("Testplayer",false);
        Player player2 = new Player("Computer",false);

        game2.addPlayer(player);
        game2.addPlayer(player2);
        // Create mock Sea Battle GUI for player
        applicationPlayer = new MockSeaBattleApplication();
        
        // Create mock Sea Battle GUI for opponent
        applicationOpponent = new MockSeaBattleApplication();
    }
    
    @AfterEach
    void tearDown() {
    }

    /**
     * Example test for method registerPlayerName(). 
     * Test whether an IllegalArgumentException is thrown when parameter 
     * name is null.
     * @author Nico Kuijpers
     */
    @Test() // expected=IllegalArgumentException.class
    void testRegisterPlayerNameNull() {

        // Register player with parameter name null in single-player mode
        String name = null;
        String password = "password";
        boolean singlePlayerMode = true;
        assertThrows(IllegalArgumentException.class,()->{game.registerPlayer(name, password, applicationPlayer, singlePlayerMode);});
    }


    @Test() // expected=IllegalArgumentException.class
    void testRegisterPlayerPasswordNull() {

        // Register player with parameter password null in single-player mode
        String name = "Jan";
        String password = null;
        boolean singlePlayerMode = true;
        assertThrows(IllegalArgumentException.class,()->{game.registerPlayer(name, password, applicationPlayer, singlePlayerMode);});
    }

    @Test() // expected=IllegalArgumentException.class
    void testRegisterPlayerApplicationPlayerNull() {

        // Register player with parameter password null in single-player mode
        String name = "Jan";
        String password = "123";
        boolean singlePlayerMode = true;
        applicationPlayer = null;
        assertThrows(IllegalArgumentException.class,()->{game.registerPlayer(name, password, applicationPlayer, singlePlayerMode);});
    }
    
    /**
     * Example test for method placeShipsAutomatically().
     * Test whether the correct number of squares contain a ship in the
     * ocean area of the player's application.
     */
    @Test
    void testPlaceShipsAutomatically() {
        
        // Register player in single-player mode
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);
        
        // Place ships automatically
        int playerNr = applicationPlayer.getPlayerNumber();
        game.placeShipsAutomatically(playerNr);
        
        // Count number of squares where ships are placed in player's application
        int expectedResult = 5 + 4 + 3 + 3 + 2;
        int actualResult = applicationPlayer.numberSquaresPlayerWithSquareState(SquareState.SHIP);
        assertEquals(expectedResult,actualResult, "Wrong number of squares where ships are placed");
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test.

    @Test
    void testPlaceShipOutsideGrid() {
        assertThrows(IllegalArgumentException.class,()->{game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);});
    }

    @Test
    void testPlaceShipHorizontal() {
        // Register player in single-player mode
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);

        // Place ships automatically
        int playerNr = applicationPlayer.getPlayerNumber();
        Ship ship = game.placeShip(playerNr, ShipType.SUBMARINE,1,1,true);

        Player player = game2.getCurrentPlayerByNumber(playerNr);
        Grid grid = player.getGrid();
        List<Square> squares = grid.getSquares();

        Square chosenSquare = game.getChosenSquare(squares,1,1);

        List<SquareState> geplaatst = new ArrayList<>();

        for(int i= 0; i< 3; i++){
            int x = chosenSquare.getPositionX()+ i;
            Square s = game.getChosenSquare(squares,1,i);
            s.setState(SquareState.SHIP);

            if(s.getState().equals(SquareState.SHIP)){
                geplaatst.add(SquareState.SHIP);
            }
        }
        assertEquals(SquareState.SHIP,geplaatst.get(0));
        assertEquals(SquareState.SHIP,geplaatst.get(1));
        assertEquals(SquareState.SHIP,geplaatst.get(2));


    }

    @Test
    void testPlaceShipVertical() {
        // Register player in single-player mode
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);

        // Place ships automatically
        int playerNr = applicationPlayer.getPlayerNumber();
        Ship ship = game.placeShip(playerNr, ShipType.SUBMARINE,1,1,false);

        Player player = game2.getCurrentPlayerByNumber(playerNr);
        Grid grid = player.getGrid();
        List<Square> squares = grid.getSquares();

        Square chosenSquare = game.getChosenSquare(squares,1,1);

        List<SquareState> geplaatst = new ArrayList<>();

        for(int i= 0; i< 3; i++){
            int y = chosenSquare.getPositionY()+ i;
            Square s = game.getChosenSquare(squares,i,1);
            s.setState(SquareState.SHIP);

            if(s.getState().equals(SquareState.SHIP)){
                geplaatst.add(SquareState.SHIP);
            }
        }
        assertEquals(SquareState.SHIP,geplaatst.get(0));
        assertEquals(SquareState.SHIP,geplaatst.get(1));
        assertEquals(SquareState.SHIP,geplaatst.get(2));

    }

    @Test
    void testPlaceShipOverlay() {
        // Register player in single-player mode
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);

        int playerNr = applicationPlayer.getPlayerNumber();
        game.placeShip(playerNr, ShipType.SUBMARINE,1,1,true);

        SquareState posY1 = applicationPlayer.getPlayerSquareState(1,1);
        SquareState posY2 = applicationPlayer.getPlayerSquareState(1,2);
        SquareState posY3 = applicationPlayer.getPlayerSquareState(1,3);


        game.placeShip(playerNr,ShipType.MINESWEEPER,2,1,true);

        assertThrows(IllegalArgumentException.class,()->{game.placeShip(playerNr,ShipType.MINESWEEPER,2,1,true); });
    }


   /* @Test
    void testPlaceExistingShip() {
        List<Ship> ships = new ArrayList<>();
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);

        Ship submarineShip  = new Ship();
        Ship submarineShipNew = new Ship();
        int playerNr = applicationPlayer.getPlayerNumber();
        game.placeShip(playerNr, ShipType.SUBMARINE,1,1,true);

        game.placeShip(playerNr,ShipType.SUBMARINE,5,5,true);
        ships.add(submarineShip);
        ships.add(submarineShipNew);

        // SquareState ophalen van oude schip, dit zou WATER moeten zijn.
        // Het oude schip moet namelijk weggehaald worden
        SquareState posY1 = applicationPlayer.getPlayerSquareState(1,1);
        SquareState n = applicationPlayer.getPlayerSquareState(5,5);
        assertEquals(SquareState.WATER,posY1);
        assertEquals(SquareState.SHIP,n);
    }*/

    @Test
    void testRemoveShip(){
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);

        int playerNr = applicationPlayer.getPlayerNumber();
        game.placeShip(playerNr, ShipType.SUBMARINE,1,1,true);
        game.removeShip(playerNr,1,1);

        SquareState posY1 = applicationPlayer.getPlayerSquareState(1,1);
        assertEquals(SquareState.WATER,posY1);
    }

    @Test
    void testRemoveAllShips(){
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);

        int playerNr = applicationPlayer.getPlayerNumber();
        game.placeShip(playerNr, ShipType.SUBMARINE,1,1,true);
        game.placeShip(playerNr, ShipType.MINESWEEPER,5,3,true);
        game.removeAllShips(playerNr);

        SquareState posY1 = applicationPlayer.getPlayerSquareState(1,1);
        SquareState posY3 = applicationPlayer.getPlayerSquareState(1,3);

        assertEquals(SquareState.WATER,posY1);
        assertEquals(SquareState.WATER,posY3);
    }

    @Test
    void testNotifyWhenReady(){
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);

        int playerNr = applicationPlayer.getPlayerNumber();
        game.placeShip(playerNr, ShipType.SUBMARINE,1,1,true);
        game.placeShip(playerNr, ShipType.MINESWEEPER,1,1,true);
        game.placeShip(playerNr, ShipType.CRUISER,1,1,true);

        assertThrows(IllegalArgumentException.class,()->{game.notifyWhenReady(playerNr);});
    }

    @Test
    void testShotFiredHit(){
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);
        int playerNr = applicationPlayer.getPlayerNumber();
        int opponentNr = applicationOpponent.getPlayerNumber();
        game.placeShip(playerNr, ShipType.SUBMARINE,1,1,true);

        game.fireShot(opponentNr,1,1);

        assertEquals(ShotType.HIT,applicationPlayer.getLastShotOpponent());
    }

    @Test
    void testShotFiredMiss(){
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);
        int playerNr = applicationPlayer.getPlayerNumber();
        int opponentNr = applicationOpponent.getPlayerNumber();

        game.placeShip(playerNr, ShipType.SUBMARINE,1,1,true);

        game.fireShot(opponentNr,1,2);

        assertEquals(ShotType.MISSED,applicationPlayer.getLastShotOpponent());
    }

    @Test
    void testShotFiredSunk(){
        game.registerPlayer("Some Name", "Some Password", applicationPlayer, true);
        int playerNr = applicationPlayer.getPlayerNumber();
        int opponentNr = applicationOpponent.getPlayerNumber();

        game.placeShip(playerNr, ShipType.SUBMARINE,1,1,true);

        game.fireShot(opponentNr,1,1);
        game.fireShot(opponentNr,2,1);
        game.fireShot(opponentNr,3,1);

        assertEquals(ShotType.SUNK,applicationPlayer.getLastShotOpponent());
    }

    @Test
    void testNewGame(){
        int playerNr = applicationPlayer.getPlayerNumber();

        //
        game.placeShip(playerNr, ShipType.SUBMARINE,1,1,true);
        game.placeShip(playerNr, ShipType.MINESWEEPER,1,2,true);

        SquareState posX1 = applicationPlayer.getPlayerSquareState(1,1);
        SquareState posX2 = applicationPlayer.getPlayerSquareState(2,1);
        SquareState posX3 = applicationPlayer.getPlayerSquareState(3,1);

        SquareState s = applicationPlayer.getPlayerSquareState(1,2);
        SquareState s2 = applicationPlayer.getPlayerSquareState(2,2);

        game.removeAllShips(playerNr);

        assertEquals(SquareState.WATER,posX1);
        assertEquals(SquareState.WATER,posX2);
        assertEquals(SquareState.WATER,posX3);

        assertEquals(SquareState.WATER,s);
        assertEquals(SquareState.WATER,s2);
    }

}

