package com.company;


public class Game {

    private Board board;
    private boolean isPlayer1Active;
    private boolean isGameStarted;
    private boolean canSwitchActivePlayer;

    public void setCanSwitchActivePlayer(boolean canSwitchActivePlayer) {
        this.canSwitchActivePlayer = canSwitchActivePlayer;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isPlayer1Active() {
        return isPlayer1Active;
    }

    public Game() {
        restart();
    }

    public boolean canSwitchActivePlayer() {
        return canSwitchActivePlayer;
    }


    public void restart() {
        board = new Board();
        isPlayer1Active = true;
        isGameStarted = false;
        canSwitchActivePlayer = false;
        board.getMyField().setShipCount(0);
        board.getMyField().setShipDrowned(0);

            }

    public void changeActivePlayer() {
        isPlayer1Active = !isPlayer1Active;
        canSwitchActivePlayer = false;

        getBoard().switchFields();
    }

    public boolean isGameStarted() {
        return isGameStarted;
    }

    public void startGame() {
        isGameStarted = true;
        changeActivePlayer();
    }



    public boolean hasPlayer1PlacedAllShips() {
        if (getBoard().getMyField().getShipCount() == 10) {
            return true;
        }
        return false;
    }

    public boolean hasPlayer2PlacedAllShips() {
        if (getBoard().getMyField().getShipCount() == 10) {
            return true;
        }
        return false;
    }


}
