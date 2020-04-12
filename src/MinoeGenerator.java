import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** Minoe Generator class: Generates minoes and stores shapes indexes and rotation indexes as list
 *
 */
public class MinoeGenerator {
    private List<Integer> shapeIndexList, rotationIndexList;
    private int counter1 = 0, counter2 = 0;
    
    public MinoeGenerator() {
        shapeIndexList = new ArrayList();
        rotationIndexList = new ArrayList();
        Random random = new Random();
        for (int i = 0; i < 500; i++) {
            shapeIndexList.add(random.nextInt(7));
            rotationIndexList.add(random.nextInt(4));
        }
    }
    
    public int getShapeIndex() {
        counter1 += 1;
        return shapeIndexList.get(counter1);
    }
    
    public int getRotationIndex() {
        counter2 += 1;
        return rotationIndexList.get(counter2);
    }
    
    public int getIncomingShapeIndex() {
        return shapeIndexList.get(counter1 + 1);
    }
    
    public int getIncomingRotationIndex() {
        return rotationIndexList.get(counter2 + 1);
    }
}
