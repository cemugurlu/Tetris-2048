import java.awt.*;
import java.util.Random;

/**
 * Block class: Class in order the create an object named block in order to store colors, x and y positions and the value of the blocks in Tetris 2048 game.
 */
public class Block {
    final Color[] colors = {new Color(238, 228, 218, 255), new Color(237, 224, 200), new Color(242, 177, 121, 255), new Color(245, 149, 99),
            new Color(246, 124, 96), new Color(246, 94, 59), new Color(237, 207, 115), new Color(237, 207, 114)
            , new Color(237, 204, 97), new Color(249, 202, 86), new Color(237, 197, 63)}; // Initializing the color of the blocks with rgb values
    
    
    boolean A = false;
    public int x;
    public int y;
    public int value;
    
    public Color getColor() {
        return colors[value - 1];
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
    
    public void setValue(int value) {
        this.value = value;
    }
    
    // Constructor with inputs x, y and value
    public Block(int x, int y, int value) {
        this.x = x;
        this.y = y;
        this.value = value;
    }
    // Constructor with inputs x and y
    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.value = 1;
    }
    
    
}
