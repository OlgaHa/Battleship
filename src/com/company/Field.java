package com.company;

import java.util.ArrayList;
import java.util.List;

public class Field {
    public static final String X_COORD_LETTERS = "KARTUPELIS";
    private ShipState[][] field = new ShipState[10][10];
    private int shipDrowned;
    private int shipCount;


    public int getShipCount() {
        return shipCount;
    }

    public void createShip(String coord, String status) {
        int[] coords = parseCoordinates(coord);
        field[coords[0]][coords[1]] = ShipState.fromString(status);
    }

    public static int[] parseCoordinates(String coords) {
        return new int[]{
                Integer.parseInt("" + coords.charAt(3)),
                Integer.parseInt("" + coords.charAt(4))
        };
    }

    public Field() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                field[i][j] = ShipState.EMPTY;
            }
        }
    }


    public int getShipDrowned() {
        return shipDrowned;
    }

    // Checks if it is possible to place a ship at given coordinates

    public boolean checkForBuffer(List<int[]> coordinateList) {
        for (int[] coordinates : coordinateList) {
            if (!isFieldAroundCellEmpty(coordinates))
                return false;
        }
        return true;
    }

    public boolean areCoordinatesDifferent(List<int[]> coordinateList) {
        for (int[] firstCoordinates : coordinateList) {
            for (int[] secondCoordinates : coordinateList) {
                if (firstCoordinates != secondCoordinates &&
                        firstCoordinates[0] == secondCoordinates[0] &&
                        firstCoordinates[1] == secondCoordinates[1]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean areCoordinatesConnected(List<int[]> coordinateList) {
        int[] sampleCoord = coordinateList.get(0);

        boolean areConnected = true;
        boolean areAllOnX = true;
        for (int[] coordinates : coordinateList) {
            if (coordinates[0] != sampleCoord[0]) {
                areAllOnX = false;
                break;
            }
            if (Math.abs(coordinates[1] - sampleCoord[1]) >= coordinateList.size()) {
                areConnected = false;
            }
        }

        boolean areAllOnY = true;
        for (int[] coordinates : coordinateList) {
            if (coordinates[1] != sampleCoord[1]) {
                areAllOnY = false;
                break;
            }
            if (Math.abs(coordinates[0] - sampleCoord[0]) >= coordinateList.size()) {
                areConnected = false;
            }
        }

        return areConnected && (areAllOnX || areAllOnY);
    }

    private boolean isFieldAroundCellEmpty(int[] cell) {
        int i = cell[0];
        int j = cell[1];
        return (isCellEmpty(i - 1, j - 1) && isCellEmpty(i - 1, j) && isCellEmpty(i - 1, j + 1) &&
                isCellEmpty(i, j - 1) && isCellEmpty(i, j) && isCellEmpty(i, j + 1) &&
                isCellEmpty(i + 1, j - 1) && isCellEmpty(i + 1, j) && isCellEmpty(i + 1, j + 1));
    }



    private boolean isCellEmpty(int i, int j) {
        i = Math.min(9, Math.max(i, 0));
        j = Math.min(9, Math.max(j, 0));
        return field[i][j] == ShipState.EMPTY;
    }



    public void placeAShip(List<int[]> coordinateList) {
        for (int[] coordinates : coordinateList) {
            field[coordinates[0]][coordinates[1]] = ShipState.MARKED;
        }
        shipCount++;
    }


    public List<int[]> getShipFromCoordinate(int x, int y) {
        List<int[]> shipCoords = getShipFromCoordinateR(x, y);
        for (int[] cell : shipCoords) {
            if (field[cell[0]][cell[1]].equals(ShipState.CHECKED_MARKED)) {
                field[cell[0]][cell[1]] = ShipState.MARKED;
            }
            if (field[cell[0]][cell[1]].equals(ShipState.CHECKED_SHOT)) {
                field[cell[0]][cell[1]] = ShipState.SHOT;
            }
        }
        return shipCoords;
    }

    private List<int[]> getShipFromCoordinateR(int x, int y) {
        List<int[]> coordinates = new ArrayList<>();
        if (x >= 0 && y >= 0 && x <= 9 && y <= 9) {
            var state = getValueAt(x, y);
            if ((state.equals(ShipState.MARKED) || state.equals(ShipState.SHOT))) {
                if (state.equals(ShipState.MARKED)) {
                    field[x][y] = ShipState.CHECKED_MARKED;
                }
                if (state.equals(ShipState.SHOT)) {
                    field[x][y] = ShipState.CHECKED_SHOT;
                }
                int[] coord = new int[]{x, y};
                coordinates.add(coord);
                List<int[]> list = getShipFromCoordinate(x - 1, y);
                coordinates.addAll(list);
                list = getShipFromCoordinate(x + 1, y);
                coordinates.addAll(list);
                list = getShipFromCoordinate(x, y + 1);
                coordinates.addAll(list);
                list = getShipFromCoordinate(x, y - 1);
                coordinates.addAll(list);

            }
        }
        return coordinates;
    }


    public ShipState getValueAt(int i, int j) {
        return field[i][j];
    }

    private boolean checkIfAllDecksShot(List<int[]> ship, int x, int y) {
        for (int[] coord : ship) {
            if (coord[0] == x && coord[1] == y) {
                continue;
            } else {
                if (!field[coord[0]][coord[1]].equals(ShipState.SHOT)) {
                    return false;
                }
            }
        }
        return true;
    }

    public ShipState shootShip(int[] coordinates) {

        int a = coordinates[0];
        int b = coordinates[1];
        List<int[]> ship = getShipFromCoordinate(a, b);
        if (ship.size() == 0) {
            field[a][b] = ShipState.MISSED;
        } else if (ship.size() == 1) {
            field[a][b] = ShipState.DROWN;
            shipDrowned++;
        } else if (ship.size() > 1) {
            if(checkIfAllDecksShot(ship,a,b)) {
                for(int [] coord : ship) {
                    field[coord[0]][coord[1]] = ShipState.DROWN;
                }
                shipDrowned++;
            } else {
                field[a][b] = ShipState.SHOT;
            }
        }

        return field[a][b];
    }

    public void setShipDrowned(int shipDrowned) {
        this.shipDrowned = shipDrowned;
    }

    public void setShipCount(int shipCount) {
        this.shipCount = shipCount;
    }
}


