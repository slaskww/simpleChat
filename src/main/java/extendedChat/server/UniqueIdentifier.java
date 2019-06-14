package src.main.java.extendedChat.server;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UniqueIdentifier {


    private static List<Integer> ids  = new ArrayList<>();
    private static final int RANGE = 10000; //number of possible identifiers
    private static int currentIndex = 0;

    static { //static method that is executed without calling it
        for (int i = 0; i < RANGE; i++){
            ids.add(i);
        }
        Collections.shuffle(ids);
    }



    private UniqueIdentifier() {
    }

    public static Integer getIdentifier(){

        if (currentIndex == RANGE - 1){
            currentIndex = 0;
        }
        return ids.get(currentIndex++);
    }
}
