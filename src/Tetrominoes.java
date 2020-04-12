import java.awt.*;

/** Tetrominoes class
 * The class stores the tetrominoes with three dimented block array
 */
public class Tetrominoes {
    public static final Block[][][] tetrominoes = {
            // I tetromino
            {
                    {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(3, 1)},
                    {new Block(1, 0), new Block(1, 1), new Block(1, 2), new Block(1, 3)},
                    {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(3, 1)},
                    {new Block(1, 0), new Block(1, 1), new Block(1, 2), new Block(1, 3)}
            },
            
            // J tetromino
            {
                    {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(2, 0)},
                    {new Block(1, 0), new Block(1, 1), new Block(1, 2), new Block(2, 2)},
                    {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(0, 2)},
                    {new Block(1, 0), new Block(1, 1), new Block(1, 2), new Block(0, 0)}
            },
            
            // L tetromino
            {
                    {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(2, 2)},
                    {new Block(1, 0), new Block(1, 1), new Block(1, 2), new Block(0, 2)},
                    {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(0, 0)},
                    {new Block(1, 0), new Block(1, 1), new Block(1, 2), new Block(2, 0)}
            },
            
            // Square tetromino
            {
                    {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)},
                    {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)},
                    {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)},
                    {new Block(0, 0), new Block(0, 1), new Block(1, 0), new Block(1, 1)}
            },
            
            // S tetromino
            {
                    {new Block(1, 0), new Block(2, 0), new Block(0, 1), new Block(1, 1)},
                    {new Block(0, 0), new Block(0, 1), new Block(1, 1), new Block(1, 2)},
                    {new Block(1, 0), new Block(2, 0), new Block(0, 1), new Block(1, 1)},
                    {new Block(0, 0), new Block(0, 1), new Block(1, 1), new Block(1, 2)}
            },
            
            // T tetromino
            {
                    {new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(2, 1)},
                    {new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(1, 2)},
                    {new Block(0, 1), new Block(1, 1), new Block(2, 1), new Block(1, 2)},
                    {new Block(1, 0), new Block(1, 1), new Block(2, 1), new Block(1, 2)}
            },
            
            // Z tetromino
            {
                    {new Block(0, 0), new Block(1, 0), new Block(1, 1), new Block(2, 1)},
                    {new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(0, 2)},
                    {new Block(0, 0), new Block(1, 0), new Block(1, 1), new Block(2, 1)},
                    {new Block(1, 0), new Block(0, 1), new Block(1, 1), new Block(0, 2)}
            }
    };
    
  
}
