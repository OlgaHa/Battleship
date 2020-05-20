package com.company;

public class Board {

    private Field myField;
    private Field opponentField;

    public Board() {
        myField = new Field();
        opponentField = new Field();
    }

    public Field getMyField() {
        return myField;
    }

    public Field getOpponentField() {
        return opponentField;
    }


    public void switchFields() {
        Field tmp = myField;
        myField = opponentField;
        opponentField = tmp;
    }

}
