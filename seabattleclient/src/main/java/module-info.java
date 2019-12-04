module seabattleclient {
  requires slf4j.api;
  requires javafx.graphics;
  requires javafx.controls;
  requires SeaBattleLogin;
    requires seabattleCommunicatorShared;
    requires javax.websocket.client.api;
  requires gson;

  exports seabattlegui;
  exports seabattlecommunicator;
}