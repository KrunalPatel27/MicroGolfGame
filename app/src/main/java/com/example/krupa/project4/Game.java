package com.example.krupa.project4;

import java.util.Random;

/**
 * Created by krupa on 4/12/2018.
 */

public class Game {

    public enum Shot_Responses{
        JACKPOT, NEAR_MISS, NEAR_GROUP, BIG_MISS, CATASTROPHE, INVALID_MOVE
    }
    public enum Shot_Options{
        RANDOM, CLOSE_GROUP, SAME_GROUP, TARGET_HOLE
    }

    public final int numberOfGroups;
    public final int groupSize;
    public final int totatHoles;
    public static GameBoard gameBoard;
    public final int winnerHole;
    public final int winnerGroup;

    public Game(int numberOfGroups, int groupSize){
        this.numberOfGroups = numberOfGroups;
        this.groupSize = groupSize;
        gameBoard = new GameBoard(5,10);
        totatHoles = numberOfGroups* groupSize;
        winnerHole = new Random().nextInt(totatHoles);
        winnerGroup = winnerHole/groupSize;
    }

    public Shot_Responses makeMove(int shootHole, Player player){
        if(!validateMove(shootHole))return Shot_Responses.INVALID_MOVE;
        if(isWinnerHole(shootHole))return Shot_Responses.JACKPOT;
        if(!isCatastrophe(shootHole))return Shot_Responses.CATASTROPHE;
        //what kind of Miss
        int shotGroup = shootHole/groupSize;
        if ( shotGroup == winnerGroup) return Shot_Responses.NEAR_MISS;
        else if(shotGroup-1 == winnerGroup || shotGroup +1 == winnerGroup)return Shot_Responses.NEAR_GROUP;
        else return Shot_Responses.BIG_MISS;
    }

    private boolean isWinnerHole(int move){
        if(move == winnerHole)return true;
        return false;
    }
    private boolean isCatastrophe(int move){
        return gameBoard.groups.get(move/groupSize).holes.get(move%groupSize).holeOpen();
    }

    private boolean validateMove(int shootHole){
        if(shootHole < totatHoles && shootHole >= 0){
            return true;
        }
        return false;
    }

}
