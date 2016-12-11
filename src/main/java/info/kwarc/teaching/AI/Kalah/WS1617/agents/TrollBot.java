package info.kwarc.teaching.AI.Kalah.WS1617.agents;


import info.kwarc.teaching.AI.Kalah.Agent;
import info.kwarc.teaching.AI.Kalah.Board;
import info.kwarc.teaching.AI.Kalah.Converter;
import info.kwarc.teaching.AI.Kalah.RandomPlayer;
import scala.collection.mutable.HashMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by vvalter on 11.12.16.
 */
public class TrollBot extends RandomPlayer {

    public TrollBot() {
        super("asdf");
    }

    @Override
    public String name() {
        return "Never gonna give you up...";
    }

    @Override
    public void init(Board board, boolean playerOne) {
        super.init(board, playerOne);
        Class boardClass = board.getClass();

        Field[] fields = boardClass.getDeclaredFields();
        Field storeP1 = fields[2];
        Field storeP2 = fields[3];

        Field ourStore;
        if (playerOne) {
            ourStore = storeP1;
        } else {
            ourStore = storeP2;
        }


        try {
            ourStore.setAccessible(true);
            ourStore.setInt(board, 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
