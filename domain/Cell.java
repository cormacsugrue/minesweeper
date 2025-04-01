package minesweeper.domain;

public class Cell {
    private State state = State.DEFAULT;
    private boolean isMine;
    private char value;

    public enum State {
        DEFAULT, FLAGGED, REVEALED
    }

    public Cell(char value){
        this.value = value;
        this.isMine = value == 'X';
    }

    public void setValue(char value) {
        this.value = value;
        this.isMine = value == 'X';
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public void setState(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public boolean isMine() {
        return isMine;
    }

    public char getValue() {
        return value;
    }
}
