package com.example.bricksgame;

import com.example.bricksgame.data.AttachPoint;
import com.example.bricksgame.enums.BrickType;
import com.example.bricksgame.data.GameState;

import java.util.List;

public class Utils {

    public static boolean gameStatesAreEqual(GameState gameState1, GameState gameState2){
        List<AttachPoint> list1 = gameState1.getAttachPointList();
        List<AttachPoint> list2 = gameState2.getAttachPointList();

        BrickType type1 = gameState1.getUsedBrickRectangle().getBrickType();
        BrickType type2 = gameState2.getUsedBrickRectangle().getBrickType();

        int pointIndex1 = gameState1.getUsedAttachPoint().getIndex();
        int pointIndex2 = gameState2.getUsedAttachPoint().getIndex();

        boolean listsAreEqual = listsAreEqual(list1, list2);
        boolean typesAreEqual = type1 == type2;
        boolean pointsIndexesAreEqual = pointIndex1 == pointIndex2;

        if (typesAreEqual && listsAreEqual && pointsIndexesAreEqual){
            return true;
        }
        return false;
    }

    private static boolean listsAreEqual(List<AttachPoint> list1, List<AttachPoint> list2) {
        for (int i = 0; i < list1.size(); i++){
            if (!list1.get(i).isVerticalBrickPlacable() && list2.get(i).isVerticalBrickPlacable()) return false;
            if (!list1.get(i).isHorizontalBrickPlacable() && list2.get(i).isHorizontalBrickPlacable()) return false;
        }
        return true;
    }
}
