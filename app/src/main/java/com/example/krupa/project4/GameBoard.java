package com.example.krupa.project4;

import java.security.acl.Group;
import java.util.ArrayList;

/**
 * Created by krupa on 4/10/2018.
 */

public class GameBoard {
    int numberOfGroups;
    int GroupSize;
    ArrayList<group> groups;

    GameBoard(int numberOfGroups, int groupSize){
        this.numberOfGroups = numberOfGroups;
        this.GroupSize = groupSize;

        groups = new ArrayList<group>(numberOfGroups);
        for(int i = 0; i < numberOfGroups; i++){
            groups.add(new group(i, groupSize));
        }
    }

    class hole{
        Boolean isTaken = false;
        int wholeNumber;
        hole(int wholeNumber){
            System.out.println(wholeNumber);
            this.wholeNumber = wholeNumber;
        }

        public boolean holeOpen(){
            if (!isTaken){
                isTaken = true;
                return true;
            }
            return false;
        }
    }
    class group{
        int groupNumber;
        ArrayList <hole> holes;

        group(int groupNumber, int size){
            this.groupNumber = groupNumber*size;
            holes = new ArrayList<hole>(size);
            for(int i = 0; i< size; i++){
                holes.add(new hole(this.groupNumber+i));
            }
        }
    }
}

