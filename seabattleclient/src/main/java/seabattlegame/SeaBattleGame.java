/*
 * Sea Battle Start project.
 */
package seabattlegame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seaBattleLogin.SeaBattleLogin;
import seabattlecommunicator.CommunicatorClientWebSocket;
import seabattlecommunicator.CommunicatorMessage;
import seabattlecommunicator.iSubject;
import seabattlecommunicatorshared.*;
import seabattlegui.ISeaBattleGUI;


import java.util.*;

/**
 * The Sea Battle game. To be implemented.
 *
 * @author Nico Kuijpers
 */
public class SeaBattleGame implements ISeaBattleGame, iObserver , iSubject {

  private List<iObserver> observers = new ArrayList<>();
  private Game game = new Game();
  private CommunicatorClientWebSocket communicator = CommunicatorClientWebSocket.getInstance();

  private static final Logger log = LoggerFactory.getLogger(SeaBattleGame.class);

  @Override
  public void registerPlayer(String name, String password, ISeaBattleGUI application, boolean singlePlayerMode) {
    communicator.attach(this);
    communicator.start();

    log.debug("Register Player {} - password {}", name, password);
    //throw new UnsupportedOperationException("Method registerPlayer() not implemented.");
    if(name==null || password ==null || application == null){
      throw new IllegalArgumentException("Name is null");
    }
    //application.setOpponentName(1, "cpu");
    Player player = new Player(name,false);
    Player player2 = new Player("Computer",false);
    game.addPlayer(player);
    game.addPlayer(player2);
      CommunicatorMessage message = new CommunicatorMessage();
      message.setPlayerNr(0);
      communicator.register(message);
      SeaBattleLogin seaBattleLogin = new SeaBattleLogin();
      seaBattleLogin.registerPlayer(name,password);

  }

  @Override
  public List<Ship> placeShipsAutomatically(int playerNr) {

      Player player = game.getCurrentPlayerByNumber(playerNr);
    Grid grid = player.getGrid();
    List<Square> squares = grid.getSquares();
    List<Ship> ships = player.getShips();

    List<Ship> lstShipsFilled = new ArrayList<>();
    for(Ship ship : ships){
      ship.getSquares().clear();
    }
    Random random = new Random();

      for(Ship ship : ships){

          int shipLength = 0;

          switch (ship.getType()){
              case AIRCRAFTCARRIER:
                  shipLength=5;
                  break;
              case BATTLESHIP:
                  shipLength = 4;
                  break;
              case CRUISER:
              case SUBMARINE:
                  shipLength =3;
                  break;
              case MINESWEEPER:
                  shipLength=2;
                  break;
          }
          boolean shipPlaced = false;
          // While de schip niet geplaatst is, blijf het proberen
          while(!shipPlaced){
              int bowX = random.nextInt(8);
              int bowY = random.nextInt(8);
              boolean horizontal = Math.random() < 0.5;

              Square chosenSquare = getChosenSquare(squares,bowX,bowY);

              if(checkSafeToAdd(shipLength,horizontal,chosenSquare,squares)){
                  for(int i = 0; i <shipLength;i++){
                      if(horizontal){
                          int x = chosenSquare.getPositionX() +i;
                          Square s = getChosenSquare(squares,x,chosenSquare.getPositionY());
                          s.setSquareState(SquareState.SHIP);
                          ship.addSquare(s);
                          shipPlaced = true;
                      }else{
                          int y = chosenSquare.getPositionY() +i;
                          Square s = getChosenSquare(squares,chosenSquare.getPositionX(),y);
                          s.setSquareState(SquareState.SHIP);
                          ship.addSquare(s);
                          shipPlaced = true;
                      }
                  }
                  lstShipsFilled.add(ship);
              }
          }
      }

      //ships.add(chosenShip);
    return lstShipsFilled;

    //throw new UnsupportedOperationException("Method placeShipsAutomatically() not implemented.");
  }

 @Override
 public Ship placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {

      
     CommunicatorMessage message = new CommunicatorMessage();
     message.setPosX(bowX);
     message.setPosY(bowY);
     message.setPlayerNr(playerNr);
     message.setHorizontal(horizontal);
     message.setShipType(shipType);
     communicator.update(message);

  return null;
 }

    @Override
    public void update(CommunicatorWebSocketMessage message) {
        observers.stream().forEach(o->o.update(message));

    }

 private boolean checkSafeToAdd(int shiplength,boolean horizontal,Square chosensquare,List<Square> squares){
    for(int i = 0; i < shiplength; i++){
        if(horizontal){
            int x = chosensquare.getPositionX() +i; // De volgende X positie
            Square s = getChosenSquare(squares,x,chosensquare.getPositionY());
            if(s.getState().equals(SquareState.SHIP) || x < 0 || x > 9){
                return false;
            }
        }else{
            int y = chosensquare.getPositionY() +i; // De volgende Y positie
            Square s = getChosenSquare(squares,chosensquare.getPositionX(),y);
            if(s.getState().equals(SquareState.SHIP) || y < 0 || y > 9){
                return false;
            }
        }
    }
     return true;
 }

 private boolean checkForMultipleShips(List<Ship> ships, ShipType shiptype){
      for(Ship s : ships){
          if(s.getType().equals(shiptype)){
              return true;
          }
      }
      return false;
 }


  public Square getChosenSquare(List<Square> squares, int x, int y) {
    // Loop door alle squares
    for (Square s : squares) {
      if (x == s.getPositionX() && y == s.getPositionY()) {
        return s;
      }
    }
    return null;
  }

  @Override
  public void removeShip(int playerNr, int posX, int posY) {
    throw new UnsupportedOperationException("Method removeShip() not implemented.");
  }

  @Override
  public List<Ship> removeAllShips(int playerNr) {
    Player player = game.getCurrentPlayerByNumber(playerNr);
    Grid grid = player.getGrid();
    List<Square> squares = grid.getSquares();
    for(Square s : squares){
        s.setSquareState(SquareState.WATER);
    }
    for(Ship ship: player.getShips()){
        for(Square square : ship.getSquares()){
            square.setSquareState(SquareState.WATER);
        }
    }
    return player.getShips();
    //throw new UnsupportedOperationException("Method removeAllShips() not implemented.");
  }

  @Override
  public void notifyWhenReady(int playerNr) {

    throw new UnsupportedOperationException("Method notifyWhenReady() not implemented.");
  }

  @Override
  public Square fireShot(int playerNr, int posX, int posY) {
      Player player = game.getCurrentPlayerByNumber(playerNr);
      List<Ship> ships = player.getShips();

      Grid playerGrid = player.getGrid();
      List<Square> squares = playerGrid.getSquares();

      Square chosenSquare = getChosenSquare(squares,posX,posY);

      // Controleer wat de state is van de geschoten square
      // Loop door alle squares van de speler
      // Zoek naar de geschoten square en verander de state en shottype
      if(chosenSquare.getState() == SquareState.SHIP){
          for(Square s : playerGrid.getSquares()){
              if(s.getPositionY() == chosenSquare.getPositionY() && s.getPositionX() == chosenSquare.getPositionX()){
                  s.setSquareState(SquareState.SHOTHIT);
                  s.setShotType(ShotType.HIT);
              }
          }
      }else{
          for(Square s : playerGrid.getSquares()){
              if(s.getPositionY() == chosenSquare.getPositionY() && s.getPositionX() == chosenSquare.getPositionX()){
                  s.setSquareState(SquareState.SHOTMISSED);
                  s.setShotType(ShotType.MISSED);
              }
          }
      }

      // Loop door de lijst van schepen van de speler
      // Loop door de squares van elk schip
      // Als elke squarestate van een schip gelijk is aan "SHOTHIT", en dat komt door de laatst geschoten square
      // Verander state naar Sunk

      for(Ship ship : ships){

          int array[] = new int[ship.getSquares().size()];
          ArrayList arrayList = new ArrayList();  // Als de array allemaal 1 bevat, is het schip gezonken

          for (int i = 0; i < ship.getSquares().size(); i++) {
              arrayList.add(array[0]);
          }

          int counter =0;
          for(Square s : ship.getSquares()){
              if(s.getState() == SquareState.SHOTHIT){
                  arrayList.set(counter,1);
                  counter++;
              }else
              {
                  arrayList.set(counter,0);
                  counter++;
              }
              if(!arrayList.contains(0)){
                  chosenSquare.setSquareState(SquareState.SHIPSUNK);
                  chosenSquare.setShotType(ShotType.SUNK);
              }
          }
      }

      // De chosensquare returnen van de speler grid
      for(Square s : playerGrid.getSquares()){
          if(chosenSquare.getPositionX() == s.getPositionX() && chosenSquare.getPositionY() == s.getPositionY()){
              return s;
          }
      }

      return null;
  }

  @Override
  public void startNewGame(int playerNr) {
    throw new UnsupportedOperationException("Method startNewGame() not implemented.");
  }

    @Override
    public void attach(iObserver o) {
        observers.add(o);
        //communicator.attach(o);
    }

    @Override
    public void detach(iObserver o) {
        observers.remove(o);
    }

}

