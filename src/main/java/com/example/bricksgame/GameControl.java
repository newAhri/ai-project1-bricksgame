package com.example.bricksgame;

import com.example.bricksgame.data.AttachPoint;
import com.example.bricksgame.data.BrickRectangle;
import com.example.bricksgame.data.BrickType;
import com.example.bricksgame.data.GameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameControl {

    public AttachPoint checkIsBrickOnAttachPoint(BrickRectangle brickRectangle, List<AttachPoint> attachPointList) throws Exception {

        double recX;
        if (brickRectangle.getBrickType() == BrickType.HORIZONTAL) {
            recX = brickRectangle.getTranslateX() - 100;
        } else {
            recX = brickRectangle.getTranslateX();
        }
        double recY = brickRectangle.getTranslateY();
        double pointX, pointY;
        boolean attachPointIsFound = false;
        AttachPoint currentAttachPoint = new AttachPoint();

        for (int i = 0; i < attachPointList.size(); i++) {
            currentAttachPoint = attachPointList.get(i);
            pointX = currentAttachPoint.getX();
            pointY = currentAttachPoint.getY();
            boolean rectangleInAttachArea = (recX - pointX) * (recX - pointX) + (recY - pointY) * (recY - pointY) < 50 * 50;
            if (rectangleInAttachArea) {
                attachPointIsFound = true;
                break;
            }
        }
        if (!attachPointIsFound) throw new Exception();
        return currentAttachPoint;
    }

    public void checkIsBrickPlacable(BrickRectangle brickRectangle, AttachPoint attachPoint) throws Exception {

        boolean isHorizontalBrickPlacable = brickRectangle.getBrickType() == BrickType.HORIZONTAL
                && attachPoint.isHorizontalBrickPlacable();
        boolean isVerticalBrickPlacable = brickRectangle.getBrickType() == BrickType.VERTICAL
                && attachPoint.isVerticalBrickPlacable();
        if (isHorizontalBrickPlacable || isVerticalBrickPlacable) return;
        throw new Exception();
    }

    public List<AttachPoint> updateAttachPointList(AttachPoint busyAttachPoint
            , BrickRectangle brickRectangle
            , List<AttachPoint> attachPointList) {

        busyAttachPoint.setHorizontalBrickPlacable(false);
        busyAttachPoint.setVerticalBrickPlacable(false);
        attachPointList.set(busyAttachPoint.getIndex(), busyAttachPoint);
        AttachPoint neighbourBusyAttachPoint = new AttachPoint();

        if (brickRectangle.getBrickType() != BrickType.SINGLE) {
            neighbourBusyAttachPoint = new AttachPoint();

            if (brickRectangle.getBrickType() == BrickType.HORIZONTAL) {
                neighbourBusyAttachPoint = attachPointList.get(busyAttachPoint.getIndex() + 1);
            } else if (brickRectangle.getBrickType() == BrickType.VERTICAL) {
                neighbourBusyAttachPoint = attachPointList.get(busyAttachPoint.getIndex() + 4);
            }
            neighbourBusyAttachPoint.setHorizontalBrickPlacable(false);
            neighbourBusyAttachPoint.setVerticalBrickPlacable(false);
            attachPointList.set(neighbourBusyAttachPoint.getIndex(), neighbourBusyAttachPoint);
        }
        attachPointList = updateSurroundingAttachPoints(busyAttachPoint
                , neighbourBusyAttachPoint
                , brickRectangle
                , attachPointList);

        return attachPointList;

    }

    public List<AttachPoint> updateSurroundingAttachPoints(AttachPoint busyAttachPoint
            , AttachPoint neighbourBusyAttachPoint
            , BrickRectangle brickRectangle
            , List<AttachPoint> attachPointList) {
        List<Integer> firstColumnIndexes = Arrays.asList(0, 4, 8, 12);
        if (brickRectangle.getBrickType() == BrickType.HORIZONTAL) {
            if (busyAttachPoint.getIndex() >= 4) {
                AttachPoint attachPoint = attachPointList.get(busyAttachPoint.getIndex() - 4);
                attachPoint.setVerticalBrickPlacable(false);
                attachPointList.set(attachPoint.getIndex(), attachPoint);

                attachPoint = attachPointList.get(neighbourBusyAttachPoint.getIndex() - 4);
                attachPoint.setVerticalBrickPlacable(false);
                attachPointList.set(attachPoint.getIndex(), attachPoint);
            }
            if (!firstColumnIndexes.contains(busyAttachPoint.getIndex())) {
                AttachPoint attachPoint = attachPointList.get(busyAttachPoint.getIndex() - 1);
                attachPoint.setHorizontalBrickPlacable(false);
                attachPointList.set(attachPoint.getIndex(), attachPoint);
            }
        } else if (brickRectangle.getBrickType() == BrickType.VERTICAL) {
            if (busyAttachPoint.getIndex() >= 4) {
                AttachPoint attachPoint = attachPointList.get(busyAttachPoint.getIndex() - 4);
                attachPoint.setVerticalBrickPlacable(false);
                attachPointList.set(attachPoint.getIndex(), attachPoint);
            }
            if (!firstColumnIndexes.contains(busyAttachPoint.getIndex())) {
                AttachPoint attachPoint = attachPointList.get(busyAttachPoint.getIndex() - 1);
                attachPoint.setHorizontalBrickPlacable(false);
                attachPointList.set(attachPoint.getIndex(), attachPoint);

                attachPoint = attachPointList.get(neighbourBusyAttachPoint.getIndex() - 1);
                attachPoint.setHorizontalBrickPlacable(false);
                attachPointList.set(attachPoint.getIndex(), attachPoint);
            }
        } else if (brickRectangle.getBrickType() == BrickType.SINGLE) {
            if (busyAttachPoint.getIndex() >= 4) {
                AttachPoint attachPoint = attachPointList.get(busyAttachPoint.getIndex() - 4);
                attachPoint.setVerticalBrickPlacable(false);
                attachPointList.set(attachPoint.getIndex(), attachPoint);
            }
            if (!firstColumnIndexes.contains(busyAttachPoint.getIndex())) {
                AttachPoint attachPoint = attachPointList.get(busyAttachPoint.getIndex() - 1);
                attachPoint.setHorizontalBrickPlacable(false);
                attachPointList.set(attachPoint.getIndex(), attachPoint);
            }
        }
        return attachPointList;
    }

    public List<GameState> getListOfPossibleGameStates(GameState gameState) {

        List<GameState> gameStateList = new ArrayList<>();
        List<AttachPoint> attachPointList = gameState.getAttachPointList();
        List<BrickRectangle> brickRectangleList = gameState.getBrickRectangleList();
        List<AttachPoint> possibleAttachPointList;
        List<BrickRectangle> possibleBrickRectangleList;

        List<BrickRectangle> leftBrickRectangleList = brickRectangleList
                .stream()
                .map(BrickRectangle::getBrickType)
                .distinct()
                .map(BrickRectangle::new)
                .collect(Collectors.toList());

        if (leftBrickRectangleList.isEmpty()) return gameStateList;

        for (AttachPoint attachPoint : attachPointList) {
            for (BrickRectangle brickRectangle : leftBrickRectangleList) {
                try {
                    checkIsBrickPlacable(brickRectangle, attachPoint);

                    AttachPoint attachPointCopy = attachPoint.clone();
                    List<AttachPoint> attachPointListCopy = new ArrayList<>();

                    for (AttachPoint point:attachPointList){
                        attachPointListCopy.add(point.clone());
                    }

                    attachPointListCopy = updateAttachPointList(attachPointCopy, brickRectangle, attachPointListCopy);

                    possibleAttachPointList = new ArrayList<>();

                    for (AttachPoint point:attachPointListCopy){
                        possibleAttachPointList.add(point.clone());
                    }

                    possibleBrickRectangleList = new ArrayList<>();

                    for (BrickRectangle brick:brickRectangleList){
                        possibleBrickRectangleList.add(brick.clone());
                    }

                    List<BrickRectangle> finalPossibleBrickRectangleList = possibleBrickRectangleList;

                    possibleBrickRectangleList.stream()
                            .filter(brick -> brick.getBrickType() == brickRectangle.getBrickType())
                            .findFirst()
                            .ifPresent(finalPossibleBrickRectangleList::remove);

                    GameState possibleGameState = new GameState(possibleAttachPointList, possibleBrickRectangleList);
                    possibleGameState.setUsedBrickRectangle(brickRectangle);
                    possibleGameState.setUsedAttachPoint(attachPoint);

                    gameStateList.add(possibleGameState);
                } catch (Exception ignored) {
                }
            }
        }
        return gameStateList;
    }
}
