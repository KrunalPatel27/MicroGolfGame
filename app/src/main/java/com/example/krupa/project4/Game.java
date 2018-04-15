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
        RANDOM, CLOSE_GROUP, SAME_GROUP
    }

    public final int numberOfGroups;
    public final int groupSize;
    public final int totatHoles;
    public static GameBoard gameBoard;
    public final int winnerHole;
    public final int winnerGroup;
    public boolean gameStatus = true;
    public Players p1;
    public Players p2;


    public Game(int numberOfGroups, int groupSize, int winnerHole){
        this.numberOfGroups = numberOfGroups;
        this.groupSize = groupSize;
        gameBoard = new GameBoard(5,10);
        totatHoles = numberOfGroups* groupSize;
        this.winnerHole = winnerHole;
        winnerGroup = winnerHole/groupSize;
        p1 = new Players(0, totatHoles);
        p2 = new Players(1, totatHoles);
    }
    public shotResponse pickAShot(int previousHole ,Shot_Options option, Players p){
        int holeChoice=0;
        if(option == Shot_Options.RANDOM){
            holeChoice = findValidHoleChoice(0, totatHoles, p);
        }else if(option == Shot_Options.SAME_GROUP) {
            int currGroup = previousHole/groupSize;
            holeChoice = findValidHoleChoice(currGroup*groupSize, (currGroup+1)*groupSize, p);
        }else if(option == Shot_Options.CLOSE_GROUP){
            int currGroup = previousHole/groupSize;
            holeChoice = findValidHoleChoice((currGroup-1)*groupSize, (currGroup+2)*groupSize, p);
        }else
            holeChoice = -1;

        return new shotResponse( holeChoice , makeMove(holeChoice, p));
    }

    private int findValidHoleChoice(int lowerBound, int upperBound, Players p){
        if(lowerBound <0)lowerBound =0;
        if(upperBound > totatHoles) upperBound = totatHoles;
        int retVal;
        while(true){
            retVal = new Random().nextInt(upperBound- lowerBound)+lowerBound;
            if(!p.pChoices[retVal]) break;
        }
        p.pChoices[retVal] = true;
        return retVal;
    }



    public Shot_Responses makeMove(int shootHole, Players p){
        if(!validateMove(shootHole))return Shot_Responses.INVALID_MOVE;
        if(isWinnerHole(shootHole)){
            gameStatus = false;
            return Shot_Responses.JACKPOT;
        }
        if(!isCatastrophe(shootHole, p))return Shot_Responses.CATASTROPHE;
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

    private boolean isCatastrophe(int move, Players p){
        if(p.ID == 0) return gameBoard.groups.get(move/groupSize).holes.get(move%groupSize).holeOpen("p1");
        return gameBoard.groups.get(move/groupSize).holes.get(move%groupSize).holeOpen("p2");
    }

    private boolean validateMove(int shootHole){
        if(shootHole < totatHoles && shootHole >= 0){
            return true;
        }
        return false;
    }
    public String [] getFlatNameArray(){
        String [] retVal = new String[totatHoles];
        for(int i = 0; i < gameBoard.numberOfGroups; i++){
            for(int j =0; j < gameBoard.GroupSize; j++){
                retVal[(i*groupSize)+j] = gameBoard.groups.get(i).holes.get(j).status;
            }
        }
        return retVal;
    }
    public  String getFlatName(int index){
        String value = gameBoard.groups.get(index/groupSize).holes.get(index%groupSize).status;
        if(index == winnerHole){
            return "Winner";
        }
        return value;
    }


    public class shotResponse{
        public int currentHole;
        public Shot_Responses response;
        shotResponse(int hole, Shot_Responses response){
            this.currentHole = hole;
            this.response = response;
        }
    }
    public class Players{
        public int ID;
        public boolean [] pChoices;
        Players(int id, int size){
            this.ID = id;
            this.pChoices = new boolean[size];
        }
    }

}
