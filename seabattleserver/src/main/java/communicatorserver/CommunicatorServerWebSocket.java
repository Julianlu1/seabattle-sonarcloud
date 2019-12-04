package communicatorserver;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import seabattlecommunicatorshared.*;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ServerEndpoint(value="/communicator/")
public class CommunicatorServerWebSocket {
    private int newPlayerNr = 0;

    private Game game = new Game();

    private static final Map<Session, Integer> sessions = new HashMap<>();

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
    public Square getChosenSquare(List<Square> squares, int x, int y) {
        // Loop door alle squares
        for (Square s : squares) {
            if (x == s.getPositionX() && y == s.getPositionY()) {
                return s;
            }
        }
        return null;
    }
    private boolean checkForMultipleShips(List<Ship> ships, ShipType shiptype){
        for(Ship s : ships){
            if(s.getType().equals(shiptype)){
                return true;
            }
        }
        return false;
    }

    // Toevoegen aan sessions lijst
    @OnOpen
    public void onConnect(Session session){

        System.out.println("[WebSocket Connected] SessionID: " + session.getId());
        String message = String.format("[New client with client side session ID]: %s",session.getId());
        sessions.put(session, newPlayerNr);
        this.newPlayerNr++;
        System.out.println("[#sessions]: " + sessions.size());
    }

    // Wanneer een bericht naar de server wordt gestuurd komt de code hier
    @OnMessage
    public void onText(String message, Session session){
        System.out.println("[WebSocket Session ID] : " + session.getId() + " [Received] : " + message);
        handleMessageFromClient(message, session);
    }
    private void handleMessageFromClient(String jsonMessage, Session session) {
        Gson gson = new Gson();

        CommunicatorWebSocketMessage wbMessage = null;


        try {
            wbMessage = gson.fromJson(jsonMessage,seabattlecommunicatorshared.CommunicatorWebSocketMessage.class);
        }
        catch (JsonSyntaxException ex) {
            System.out.println("[WebSocket ERROR: cannot parse Json message " + jsonMessage);
            return;
        }

        // Operation defined in message
        seabattlecommunicatorshared.CommunicatorWebSocketMessageOperation operation;
        operation = wbMessage.getOperation();

        switch(operation){
            case ADDPLAYER:
                Player newPlayer = new Player(wbMessage.getName(),false);
                this.game.addPlayer(newPlayer);
                break;
            case PLACESHIP:
                    int playerNr = this.sessions.get(session);

                    ShipType shipType = wbMessage.getShipType();
                    Boolean horizontal = wbMessage.getHorizontal();
                    int bowX = wbMessage.getPosX();
                    int bowY = wbMessage.getPosY();


                    boolean safeToAdd = false;
                    // Get all squares
                    Player player = game.getCurrentPlayerByNumber(playerNr);

                    Grid grid = player.getGrid();
                    List<Square> squares = grid.getSquares();

                    int shipLength = 0;

                    switch(shipType){
                        case AIRCRAFTCARRIER:
                            shipLength = 5;
                            break;
                        case BATTLESHIP:
                            shipLength = 4;
                            break;
                        case CRUISER:
                        case SUBMARINE:
                            shipLength = 3;
                            break;
                        case MINESWEEPER:
                            shipLength = 2;
                            break;
                    }

                    Square chosenSquare = getChosenSquare(squares,bowX,bowY);
                    Ship chosenShip = player.getShipByType(shipType);

                    List<Ship> playerShips = player.getShips();
                    // Als het veilig is om een schip te plaatsen en er geen duplicaten zijn van schepen
                    if(checkSafeToAdd(shipLength,horizontal,chosenSquare,squares) && checkForMultipleShips(playerShips,shipType)){
                        for(int i = 0; i < shipLength ; i++){
                            assert chosenSquare != null;
                            if(horizontal){
                                int x = chosenSquare.getPositionX() + i; // Steeds 1 erbij optellen
                                Square sss = getChosenSquare(squares,x,chosenSquare.getPositionY()); // De volgende square pakken || getPositionY is steeds dezelfde locatie
                                // Als de Square al een ship heeft, is er overlapping
                                playerShips.removeIf(ship-> ship.getType().equals(shipType));
                                sss.setSquareState(SquareState.SHIP); // Set state naar SHIP
                                chosenShip.addSquare(sss);
                            }else{
                                int y = chosenSquare.getPositionY() + i; // Steeds 1 erbij optellen
                                Square ss = getChosenSquare(squares,chosenSquare.getPositionX(),y); // De volgende square pakken
                                ss.setSquareState(SquareState.SHIP); // Set state naar SHIP
                                playerShips.removeIf(ship-> ship.getType().equals(shipType));
                                chosenShip.addSquare(ss);
                            }
                        }
                    }else{
                    }

                    // PlaceShipDTO is null
                    PlaceShipDTO placeShipDTO = wbMessage.getPlaceShipDTO();
                    seabattlecommunicatorshared.CommunicatorWebSocketMessage communicatorWebSocketMessage = new seabattlecommunicatorshared.CommunicatorWebSocketMessage(CommunicatorWebSocketMessageOperation.PLACESHIP,placeShipDTO);
                    communicatorWebSocketMessage.setShip(chosenShip);
                        String message = gson.toJson(communicatorWebSocketMessage);

                    session.getAsyncRemote().sendText(message);
                communicatorWebSocketMessage.setOperation(CommunicatorWebSocketMessageOperation.PLACESHIPENEMY);
                CommunicatorWebSocketMessage finalWbMessage = communicatorWebSocketMessage;
                this.sessions.forEach((s, p) -> {
                    if(!s.equals(session)){
                        String jsonStringMessage = gson.toJson(finalWbMessage);
                        s.getAsyncRemote().sendText(jsonStringMessage);
                    }
                });
        }
    }
}
