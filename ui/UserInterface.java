package minesweeper.ui;

import minesweeper.domain.MineField;

import java.util.Scanner;

public class UserInterface {
    private Scanner scanner;
    private final int DIM = 9;
    private final int TOTAL_CELL_COUNT = DIM * DIM;
    private int col = -1;
    private int row = -1;
    private String inputCommand = "";



    public UserInterface(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        int numberOfMines = -1;
        while (true) {
            try {
                System.out.print("How many mines do you want on the field? > ");
                numberOfMines = Integer.parseInt(scanner.nextLine());
                if (numberOfMines < 1 || numberOfMines > DIM * DIM) {
                    System.out.println("The number of mines must be between 0 and 81 (inclusive)");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: value must be of type int");
            }
        }


        MineField mineField = new MineField(DIM, numberOfMines);
        mineField.printField();

        // Loop until game ends
        while (mineField.isActive()) {



            getUserCommand();


            if (inputCommand.equals("free")) {
                // reveal cell
                boolean isFree = mineField.claimFree(col,  row);
                mineField.printField();
                if (!isFree) {
                    System.out.println("You stepped on a mine and failed!");
                    break;
                }
            } else if (inputCommand.equals("mine")) {
                mineField.setFlag(col, row);
                mineField.printField();

            }
            if (mineField.checkWin()) {
                System.out.println("Congratulations! You found all the mines!");
                mineField.setActive(false);
            }

        }

    }
    private void getUserCommand() {
        boolean validInput = false;

        while (!validInput) {
            System.out.println("Set/delete mine marks or claim a cell as free: > ");
            String input = scanner.nextLine();
            String[] parts = input.split(" ");

            if (parts.length != 3) {
                System.out.println("Invalid number of inputs: Input should be in form: x y \"command\"");
                continue;
            }

            try {
                row = Integer.parseInt(parts[0]) - 1;
                col = Integer.parseInt(parts[1]) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Input must be a valid int");
                continue;
            }

            if (col < 0 || col > DIM - 1 || row < 0 || row > DIM - 1) {
                System.out.println("Invalid input row and col must be in range 1 - 9 (inclusive)");
                continue;
            }

            inputCommand = parts[2];

            if (inputCommand.equals("free") || inputCommand.equals("mine")) {
                validInput = true;
            } else {
                System.out.println("Invalid command! Command must be: \"mine\" or \"free\"");
            }

        }
    }
}
