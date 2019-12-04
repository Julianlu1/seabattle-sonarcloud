/*
 * Sea Battle Start project.
 */
package seabattlecommunicatorshared;

/**
 * Indicate the state of a square.
 * @author Nico Kuijpers
 */
public enum SquareState {
    WATER,        // No ship is positioned at this square
    SHIP,         // A ship is positioned at this square
    SHOTMISSED,   // A shot was fired at this square, but no hit
    SHOTHIT,      // A shot was fired at this square and a ship was hit
    SHIPSUNK;     // A shot was fired at this square and a ship is sunk
}
