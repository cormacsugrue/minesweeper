package minesweeper.domain;


import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class MineField {
    private final Cell[][] cells;
    private final int numberOfMines;
    private final int fieldDimension;
    private final HashSet<List<Integer>> mineSet;
    private boolean isActive = true;

    public MineField(int dimension, int numberOfMines) {
        this.cells = new Cell[dimension][dimension];
        this.numberOfMines = numberOfMines;
        this.fieldDimension = dimension;
        this.mineSet = new HashSet<>();
        generateMineField();
    }


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    private void generateMineField() {
        generateEmptyField();
        generateMineSet();
        plantMines();
    }

    public boolean checkWin() {
        boolean allFlagsCorrect = true;
        boolean allSafeCellsRevealed = true;

        for (int i = 0; i < fieldDimension; i++) {
            for (Cell cell : cells[i]) {
                if (cell.isMine() && cell.getState() != Cell.State.FLAGGED) {
                    allFlagsCorrect = false;
                }
                if (!cell.isMine() && cell.getState() == Cell.State.FLAGGED) {
                    allFlagsCorrect = false;
                }
                if (!cell.isMine() && (cell.getState() == Cell.State.DEFAULT || cell.getState() == Cell.State.FLAGGED)) {
                    allSafeCellsRevealed = false;
                }
            }
        }


        return allFlagsCorrect || allSafeCellsRevealed;

    }

    private void plantMines() {
        Cell currentCell;
        for (int i = 0; i < this.fieldDimension; i++) {
            for (int j = 0; j < this.fieldDimension; j++) {
                currentCell = cells[i][j];
                for (List<Integer> coordinate : mineSet) {
                    if (coordinate.get(0) == i && coordinate.get(1) == j) {
                        currentCell.setValue('X');

                        updateCellState(currentCell, Cell.State.DEFAULT);
                        incrementSurroundingCells(i, j);
                    }
                }

            }
        }
    }

    private void generateEmptyField() {
        for (int i = 0; i < this.fieldDimension; i++) {
            for (int j = 0; j < this.fieldDimension; j++) {
                cells[i][j] = new Cell('/');
            }
        }
    }

    public void setFlag(int x, int y) {
        Cell currentCell = cells[x ][y];
        if (currentCell.getState() == Cell.State.DEFAULT) {
            currentCell.setState(Cell.State.FLAGGED);
        } else if (currentCell.getState() == Cell.State.FLAGGED) {
            currentCell.setState(Cell.State.DEFAULT);
            // Todo refactor cell to make logic clear in below statement
        } else if (currentCell.getState() == Cell.State.REVEALED && !currentCell.isMine() && currentCell.getValue() != '/') {
            System.out.println("There is a number here!");
        }
    }


    public void printField() {
        Cell currentCell = cells[0][0];
        printFieldHeader();
        for (int i = 0; i < this.fieldDimension; i++) {
            System.out.print(i + 1);
            System.out.print("|");
            for (int j = 0; j < this.fieldDimension; j++) {
                currentCell = cells[i][j];
                if (currentCell.getState() == Cell.State.DEFAULT) {
                    System.out.print('.');
                } else if (currentCell.getState() == Cell.State.REVEALED) {
                    System.out.print(currentCell.getValue());
                } else if (currentCell.getState() == Cell.State.FLAGGED) {
                    System.out.print('*');
                }
//                System.out.print(currentCell.getValue());
            }

            System.out.println("|");
        }
        printDashedLine();
    }

    private void revealAllMines(){
        for (int i = 0; i < fieldDimension; i++) {
            for (Cell cell : cells[i]) {
                if (cell.isMine()) {
                    cell.setState(Cell.State.REVEALED);
                }
            }
        }
//        printField();
    }

    private void printFieldHeader() {
        System.out.print("-|");
        for (int i = 0; i < this.fieldDimension; i++) {
            System.out.print(i + 1);
        }
        System.out.println("|");
        printDashedLine();
    }

    private void printDashedLine() {
        System.out.print("-|");
        for (int i = 0; i < this.fieldDimension; i++) {
            System.out.print("-");
        }
        System.out.println("|");
    }


    private void updateCellState(Cell cell, Cell.State state) {
        cell.setState(state);
    }

    public boolean claimFree(int row, int col) {
        // check if cell is a mine
        Cell selectedCell = cells[row][col];
        if (selectedCell.isMine()) {
            // set all mine states to revealed
            this.revealAllMines();
            isActive = false;
            return false;
        }
        else {
            selectedCell.setState(Cell.State.REVEALED);
            claimSurroundingCells(row, col);
            return true;
        }
    }

    private void generateMineSet() {
        Random random = new Random();
        while (mineSet.size() < numberOfMines) {
            List<Integer> nextMine = Arrays.asList(random.nextInt(this.fieldDimension), random.nextInt(this.fieldDimension));
            mineSet.add(nextMine);
        }

    }

    private void claimSurroundingCells(int xCor, int yCor) {
        Cell currentCell = cells[0][0];
        for (int i = xCor - 1; i <= xCor + 1; i++) {
            if (i >= 0 && i < this.fieldDimension) {
                for (int j = yCor - 1; j <= yCor + 1; j++) {
                    if (j >= 0 && j < this.fieldDimension && this.cells[i][j].getValue() != 'X') {
                        currentCell = this.cells[i][j];
                        if (currentCell.getValue() == '/' && currentCell.getState() != Cell.State.REVEALED) {
                            this.claimFree(i, j );
                        } else if (currentCell.getValue() != '/'  && !currentCell.isMine()) {
                            cells[i][j].setState(Cell.State.REVEALED);

                        }
                    }
                }
            }
        }
    }

    public void incrementSurroundingCells(int xCor, int yCor) {
        // Iterate over  3 x 3 matrix surrounding cells[x][y]
        Cell currentCell = cells[0][0];
        for (int i = xCor - 1; i <= xCor + 1; i++) {
            if (i >= 0 && i < this.fieldDimension) {
                for (int j = yCor - 1; j <= yCor + 1; j++) {
                    if (j >= 0 && j < this.fieldDimension && this.cells[i][j].getValue() != 'X') {
                        currentCell = this.cells[i][j];
                        if (currentCell.getValue() == '/') {
                            currentCell.setValue('1');
                        } else {
                            this.cells[i][j].setValue((char) (this.cells[i][j].getValue() + 1));
                        }
                    }
                }
            }
        }
    }
}
