package seabattlecommunicatorshared;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private List<Player> currentPlayers = new ArrayList<>();

    public boolean startGame(){
        for(Player player : currentPlayers){
            if(!player.getPlayerReady()){
                return false;
            }
        }
        return true;
    }

    public void addPlayer(Player newPlayer){
        if(this.currentPlayers.size() < 2 ){
            currentPlayers.add(newPlayer);
            newPlayer.setGrid(new Grid("Grid: " + newPlayer.getName(),0));
        }
    }

    public void endGame(){

    }

    public Player getCurrentPlayerByNumber(int playerNr) {
        return currentPlayers.get(playerNr);
    }

    public void setupGame(Grid grid) {

    }
}
