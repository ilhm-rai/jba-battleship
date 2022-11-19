package battleship;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {

    private static final char[][] secretBoard = new char[10][10];
    private final Player player1 = new Player();
    private final Player player2 = new Player();
    private final List<Player> players = List.of(player1, player2);

    private List<Ship> loadShips() {
        List<Ship> ships = new ArrayList<>();
        ships.add(new Ship("Aircraft Carrier", 5));
        ships.add(new Ship("Battleship", 4));
        ships.add(new Ship("Submarine", 3));
        ships.add(new Ship("Cruiser", 3));
        ships.add(new Ship("Destroyer", 2));
        return ships;
    }

    private void prepareBoard() {
        char[][] board1 = new char[10][10];
        char[][] board2 = new char[10][10];
        for (int i = 0; i < 10; i++) {
            Arrays.fill(board1[i], '~');
            Arrays.fill(secretBoard[i], '~');
            Arrays.fill(board2[i], '~');
        }
        this.player1.setBoard(board1);
        this.player2.setBoard(board2);
    }

    private void viewBoard(char[][] board) {
        int alpha = 65;
        System.out.println("  1 2 3 4 5 6 7 8 9 10");
        for (char[] ship : board) {
            System.out.print((char) alpha + " ");
            for (int j = 0; j < ship.length; j++) {
                System.out.print(ship[j]);
                if (j < ship.length - 1) {
                    System.out.print(" ");
                }
            }
            alpha++;
            System.out.println();
        }
    }

    private boolean placeShip(char[][] board, String cord1, String cord2, Ship ship) {
        int alphaStart = cord1.charAt(0) % 65;
        int alphaEnd = cord2.charAt(0) % 65;
        int indexStart = Integer.parseInt(cord1.substring(1)) - 1;
        int indexEnd = Integer.parseInt(cord2.substring(1)) - 1;

        int alphaMin = Math.min(alphaStart, alphaEnd);
        int alphaMax = Math.max(alphaStart, alphaEnd);
        int indexMin = Math.min(indexStart, indexEnd);
        int indexMax = Math.max(indexStart, indexEnd);

        int alpha = alphaMax - alphaMin + 1;
        int index = indexMax - indexMin + 1;

        boolean isError = false;
        boolean isTooClose = false;

        if (alphaStart != alphaEnd && indexStart != indexEnd) {
            System.out.println("Error! Wrong ship location! Try again:");
            isError = true;
        } else if (alpha != ship.getUnit() && index != ship.getUnit()) {
            System.out.printf("Error! Wrong length of the %s! Try again:%n", ship.getName());
            isError = true;
        }

        if (!isError) {
            if (alphaStart == alphaEnd) {
                if (board[alphaMin][indexOrZero(indexMin)] == 'O' || board[alphaMin][indexOrLength(indexMax, board.length)] == 'O') {
                    isTooClose = true;
                }
                if (!isTooClose) {
                    for (int i = indexMin; i <= indexMax; i++) {
                        if (board[indexOrZero(alphaMin)][i] == 'O' || board[indexOrLength(alphaMin, board.length)][i] == 'O') {
                            isTooClose = true;
                            break;
                        }
                        board[alphaMin][i] = 'O';
                    }
                }
            } else {
                if (board[indexOrZero(alphaMin)][indexMin] == 'O' || board[indexOrLength(alphaMax, board.length)][indexMin] == 'O') {
                    isTooClose = true;
                }
                if (!isTooClose) {
                    for (int i = alphaMin; i <= alphaMax; i++) {
                        if (board[i][indexOrZero(indexMin)] == 'O' || board[i][indexOrLength(indexMin, board.length)] == 'O') {
                            isTooClose = true;
                            break;
                        }
                        board[i][indexMin] = 'O';
                    }
                }
            }
        }

        if (isTooClose) {
            System.out.println("Error! You placed it too close to another one. Try again:");
            isError = true;
        }

        return !isError;
    }

    private int indexOrZero(int index) {
        return Math.max(index - 1, 0);
    }

    private int indexOrLength(int index, int length) {
        return Math.min(index + 1, length - 1);
    }

    private void prepareGame() {
        prepareBoard();
        List<Ship> ships = loadShips();
        for (var player : players) {
            System.out.printf("Player %d, place your ships on the game field%n", player.getId());
            viewBoard(player.getBoard());
            Scanner scanner = new Scanner(System.in);
            for (var ship : ships) {
                System.out.printf("Enter the coordinates of the %s (%d cells):\n", ship.getName(), ship.getUnit());
                boolean hasPlaced = false;
                while (!hasPlaced) {
                    String cord1 = scanner.next();
                    String cord2 = scanner.next();
                    hasPlaced = placeShip(player.getBoard(), cord1, cord2, ship);
                }
                viewBoard(player.getBoard());
            }
            promptEnterKey();
        }
    }

    public void start() {
        prepareGame();
        Scanner scanner = new Scanner(System.in);
        int playerOnePoint = 0;
        int playerTwoPoint = 0;
        while (playerOnePoint < Ship.totalUnit && playerTwoPoint < Ship.totalUnit) {
            for (var player : players) {
                char[][] board = player.getBoard();
                char[][] opponentBoard;
                if (player.getId() == 1) {
                    opponentBoard = player2.getBoard();
                } else {
                    opponentBoard = player1.getBoard();
                }
                viewBoard(secretBoard);
                System.out.println("---------------------");
                viewBoard(board);
                System.out.printf("Player %d, it's your turn:%n", player.getId());
                String target = scanner.next();
                int alpha = target.charAt(0) % 65;
                int index = Integer.parseInt(target.substring(1)) - 1;
                boolean isRightCoordinate = alpha < 10 && index >= 0 && index < 10;
                boolean isSunk = false;
                if (!isRightCoordinate) {
                    System.out.println("Error! You entered the wrong coordinates! Try again:");
                } else {
                    boolean isMissed;
                    int length = opponentBoard.length;
                    if (opponentBoard[alpha][index] == 'O' || opponentBoard[alpha][index] == 'X') {
                        if (opponentBoard[alpha][index] == 'O') {
                            if (player.getId() == 1) {
                                playerOnePoint++;
                            } else {
                                playerTwoPoint++;
                            }
                        }

                        opponentBoard[alpha][index] = 'X';
                        isMissed = false;

                        if (index + 1 < length) {
                            int i = 0;
                            while (index + i < length) {
                                isSunk = opponentBoard[alpha][Math.min(index + i, length - 1)] == 'X';
                                i++;
                            }
                        }

                        if (index - 1 >= 0) {
                            int i = 0;
                            while (index - i >= 0) {
                                isSunk = opponentBoard[alpha][Math.max(index - i, 0)] == 'X';
                                i++;
                            }
                        }

                        if (alpha + 1 < length) {
                            int i = 0;
                            while (alpha + i < length) {
                                isSunk = opponentBoard[Math.min(alpha + i, length - 1)][index] == 'X';
                                i++;
                            }
                        }

                        if (alpha - 1 >= 0) {
                            int i = 0;
                            while (alpha - i >= 0) {
                                isSunk = opponentBoard[Math.max(alpha - i, 0)][index] == 'X';
                                i++;
                            }
                        }
                    } else {
                        opponentBoard[alpha][index] = 'M';
                        isMissed = true;
                    }

                    if (isMissed) {
                        System.out.println("You missed! Try again:");
                    } else {
                        if (isSunk) {
                            System.out.println("You sank a ship!");
                        } else {
                            if (playerOnePoint == Ship.totalUnit || playerTwoPoint == Ship.totalUnit) {
                                System.out.println("You sank the last ship. You won. Congratulations!");
                            } else {
                                System.out.println("You hit a ship! Try again:");
                            }
                        }
                    }
                    promptEnterKey();
                }
            }
        }
    }

    private void promptEnterKey() {
        System.out.println("Press Enter and pass the move to another player");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
