package seabattlecommunicator;

public interface ICommunicator {
    /**
     * Start the connection.
     */
    public void start();

    /**
     * Stop the connection.
     */
    public void stop();

    /**
     * Register a property.
     * @param property
     */

    /**
     * Update a property by sending a message to all clients
     * that are subscribed to the property of the message.
     * @param message the message to be sent
     */
    public void update(CommunicatorMessage message);
}
