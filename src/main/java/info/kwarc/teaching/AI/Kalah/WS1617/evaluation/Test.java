package info.kwarc.teaching.AI.Kalah.WS1617.evaluation;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Simon Rainer on 12/10/16.
 */
public class Test {
    public static void main(String[] args) {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("[1, 2, 3]", 5);
        System.out.println(map.containsKey(Arrays.toString(new int[]{1,2,3})));
    }
}
