package seabattlecommunicator;

import seabattlegame.iObserver;

public interface iSubject {
    void attach(iObserver o);
    void detach(iObserver o);
}
