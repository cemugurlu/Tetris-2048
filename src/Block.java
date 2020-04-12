import java.awt.*;
import java.util.Random;

/** Block class: Class in order the create an object named block in order to store colors, x and y positions and the value of the blocks in Tetris 2048 game.
 *
 *
 */
public class Block {
    final Color[] colors = {new Color(238, 228, 218), new Color(236, 224, 200), new Color(243, 177, 121),
            new Color(246,124 ,95),new Color(253, 91, 66), new Color(246, 94, 69),new Color(237, 207, 114)
    ,new Color(237,204,97),new Color(249,202,86),new Color(237,197,63) };
    
    // new Color(243, 177, 121),// new Color(245, 149, 99), //new Color(249, 123, 98),new Color(238, 228, 218), new Color(236, 224, 200), new Color(243, 177, 121), new Color(245, 149, 99), new Color(249, 123, 98)};
    
    boolean A = false;
    int x;
    int y;
    int value;
    
    public Color getColor() {
        return colors[value];
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getValue() {
        return value;
    }
    
    public void setValue(int value)
    {
        this.value = value;
    }
    
    // Constructor with three input
    public Block(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }
    
    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 1;
    }
    
    
}
