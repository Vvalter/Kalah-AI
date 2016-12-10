package info.kwarc.teaching.AI.Kalah.WS1617.evaluation;

import java.util.ArrayList;

/**
 * Created by Simon Rainer on 12/10/16.
 */
public class Test {
    public static void main(String[] args) {
ArrayList<Integer> a = new ArrayList<>();
        a.add(2);
        a.add(4);
        a.add(0);
        a.add(2);
        a.sort((i,j) -> j - i);
        System.out.println(a);
    }
}
