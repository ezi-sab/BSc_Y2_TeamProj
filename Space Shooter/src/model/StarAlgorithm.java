package model;

import javafx.util.Pair;
import view.ShipModel;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Random;

public class StarAlgorithm extends ShipModel {

    private static final int ROW =20;
    private static final int COLUMN = 36;
    private static final double CELL_WIDTH = 34.0;

    CellValue shipCell;
    CellValue enemyCell;
    CellValue enemyNextCell;
    CellValue enemyNewCell;

    CellValue[][] shipLocation;
    CellValue[][] enemyLocation;
    CellValue[][] enemyNextLocation;
    CellValue[][] enemyNewLocation;

    int shipLocationX, shipLocationY;
    int enemyLocationX, enemyLocationY;
    int enemyNextLocationX, EnemyNextLocationY;
    int enemyNewLocationX, enemyNewLocationY;

    int gn = 0;
    int hn = 0;


    public StarAlgorithm(CellValue[][] grid) {
        super(grid);
    }

    private ArrayList<Pair<Integer, ArrayList<Direction>>> starSearch(CellValue[][] enemyGrid, int step, Direction preDirection){
        int enemyX = enemyGrid.length;
        int enemyY = enemyGrid[0].length;

        ArrayList<Direction> newDirections = new ArrayList<>();

        if(enemyX==shipLocationX && enemyY==shipLocationY){
            return null;
        }

        if(shipLocationX > enemyX){
            hn = hn + (shipLocationX - enemyX);
        } else {
            hn = hn + (enemyX - shipLocationX);
        }

        if(shipLocationY > enemyY){
            hn = hn + (shipLocationY - enemyY);
        } else {
            hn = hn + (enemyY - shipLocationY);
        }

        hn = (int)(hn/CELL_WIDTH);

        ArrayList<Pair<Integer, ArrayList<Direction>>> enableLists = new ArrayList<>();
        for(Direction directionVal: Direction.values()){
            if(directionVal != preDirection){
                CellValue[][] location = cellCoordinate(enemyGrid, directionVal);
                CellValue status = cellStatus(location);

                if(status==CellValue.BLOCK){
                    continue;
                } else {
                    newDirections.add(directionVal);

                    Pair<Integer, ArrayList<Direction>> fn = new Pair<>(gnhn(enemyGrid, shipLocationX, shipLocationY, hn, directionVal), newDirections);
                    enableLists.add(fn);
                }
            } else {
                continue;
            }
        }
        int enablePaths = enableLists.size();

        if(enablePaths==0){
            newDirections.add(preDirection);

            Pair<Integer, ArrayList<Direction>> fn = new Pair<>(gnhn(enemyGrid, shipLocationX, shipLocationY, hn, preDirection), newDirections);
            enableLists.add(fn);
        }

        enablePaths = enableLists.size();

        Pair<Integer, ArrayList<Direction>> firstPath = enableLists.get(0);
        //Still working for here

        return enableLists;
    }

    public CellValue[][] cellCoordinate(CellValue[][] cell, Direction nextDirection){
        int x_coordinate = cell.length;
        int y_coordinate = cell[0].length;

        if(nextDirection==Direction.UP){
            if(0<x_coordinate && x_coordinate<=ROW){
                x_coordinate = x_coordinate - 1;
            }
        } else if(nextDirection==Direction.DOWN){
            if(0<=x_coordinate && x_coordinate<ROW){
                x_coordinate = x_coordinate + 1;
            }
        } else if(nextDirection==Direction.LEFT){
            if(0<y_coordinate && y_coordinate<=COLUMN){
                y_coordinate = y_coordinate - 1;
            }
        } else if(nextDirection==Direction.RIGHT){
            if(0<=y_coordinate && y_coordinate<COLUMN){
                y_coordinate = y_coordinate + 1;
            }
        }
        CellValue[][] newCell = new CellValue[x_coordinate][y_coordinate];

        return newCell;
    }

    public CellValue cellStatus(CellValue[][] cell){ //Check CellValue of CellValue[][]
        CellValue cellVal = gameGrid[cell.length][cell[0].length];

        return cellVal;
    }
    private int gnhn(CellValue[][] gridLocation, int shipX, int shipY, int h, Direction dir){
        int gridX = gridLocation.length;
        int gridY = gridLocation[0].length;

        CellValue[][] checkLocation = cellCoordinate(gridLocation, dir);
        CellValue checkStatus = cellStatus(checkLocation);

        int newhn = 0;

        if(checkStatus!=CellValue.BLOCK){
            if(dir==Direction.UP){
                gridX = gridX - 1;

                if(shipLocationX > gridX){
                    newhn = newhn + (shipLocationX - gridX);
                } else {
                    newhn = newhn + (gridX - shipLocationX);
                }

                if(shipLocationY > gridY){
                    newhn = newhn + (shipLocationY - gridY);
                } else {
                    newhn = newhn + (gridY - shipLocationY);
                }

                newhn = (int)(newhn/CELL_WIDTH);

                if(h>newhn){
                    gn = gn + 1;
                } else {
                    gn = gn + 10;
                }
            } else if (dir==Direction.DOWN){
                gridX = gridX + 1;

                if(shipLocationX > gridX){
                    newhn = newhn + (shipLocationX - gridX);
                } else {
                    newhn = newhn + (gridX - shipLocationX);
                }

                if(shipLocationY > gridY){
                    newhn = newhn + (shipLocationY - gridY);
                } else {
                    newhn = newhn + (gridY - shipLocationY);
                }

                newhn = (int)(newhn/CELL_WIDTH);

                if(h>newhn){
                    gn = gn + 1;
                } else {
                    gn = gn + 10;
                }

            } else if (dir==Direction.LEFT){
                gridY = gridY - 1;

                if(shipLocationX > gridX){
                    newhn = newhn + (shipLocationX - gridX);
                } else {
                    newhn = newhn + (gridX - shipLocationX);
                }

                if(shipLocationY > gridY){
                    newhn = newhn + (shipLocationY - gridY);
                } else {
                    newhn = newhn + (gridY - shipLocationY);
                }

                newhn = (int)(newhn/CELL_WIDTH);

                if(h>newhn){
                    gn = gn + 1;
                } else {
                    gn = gn + 10;
                }

            } else if (dir==Direction.RIGHT){
                gridY = gridY + 1;

                if(shipLocationX > gridX){
                    newhn = newhn + (shipLocationX - gridX);
                } else {
                    newhn = newhn + (gridX - shipLocationX);
                }

                if(shipLocationY > gridY){
                    newhn = newhn + (shipLocationY - gridY);
                } else {
                    newhn = newhn + (gridY - shipLocationY);
                }

                newhn = (int)(newhn/CELL_WIDTH);

                if(h>newhn){
                    gn = gn + 1;
                } else {
                    gn = gn + 10;
                }
            }
        } else { //checkStatus==CellValue.BLOCK
            gn = gn + 10000;
        }
        return (gn+h);
    }



}
