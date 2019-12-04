package seabattlegame;

import seabattlecommunicatorshared.CommunicatorWebSocketMessage;

public interface iObserver {
    void update(CommunicatorWebSocketMessage message);
}
