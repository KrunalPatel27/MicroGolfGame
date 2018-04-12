package com.example.krupa.project4;

/**
 * Created by krupa on 4/10/2018.
 */

public class GameBoard {
    int numberOfGroups;
    int GroupSize;

    GameBoard(int numberOfGroups, int groupSize){
        this.numberOfGroups = numberOfGroups;
        this.GroupSize = groupSize;
    }

    class hole{
        Player control = null;
        int wholeNumber;
        int wholeGroup;
        hole(int wholeNumber, int wholeGroup){
            this.wholeNumber = wholeNumber;
            this.wholeGroup = wholeGroup;
        }

        public boolean holeOpen(Player player){
            if (control == null){
                this.control = player;
                return true;
            }
            return false;
        }
    }
    class group{
        private hole [] holes = new hole[10];

    }
}
