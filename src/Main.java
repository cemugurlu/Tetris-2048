import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * @author Cem Uğurlu, Uras Felamur
 * @date 13.04.2020
 * Project Name: Tetris 2048
 * Description: A game that mergence of Tetris and 2048 with using StdDraw.
 */
public class Main {
    public static void main(String[] args) {
        // set the size of the drawing canvas
        final Color backgroundColor = new Color(206, 195, 181);
        final Color gridColor = new Color(185, 171, 158);
        final Color menuColor = new Color(167, 160, 151);
        final Color seperatorColor = new Color(132, 122, 113);
        int heightScale = 18;
        int widthScale = 12;
        Block[][] incomingTetrominoAllRotations = new Block[][]{};
        List<Block> occupiedBlocks = new ArrayList<>();
        boolean doSendNewBlock = true;
        int score = 0;
        int rotationIndex = 0;
        int shapeIndex;
        
        MinoeGenerator minoeGenerator = new MinoeGenerator();
        initCanvas(heightScale, widthScale);
        
        
        while (true) {  // The loop that executes all the methods and the drawings
            if (StdDraw.hasNextKeyTyped()) {
                char charA = StdDraw.nextKeyTyped();
                rotationIndex = handleUserInput(incomingTetrominoAllRotations, occupiedBlocks, rotationIndex, charA, widthScale);
                
            }
            if (doSendNewBlock) {
                rotationIndex = minoeGenerator.getRotationIndex();
                shapeIndex = minoeGenerator.getShapeIndex();
                incomingTetrominoAllRotations = Tetrominoes.tetrominoes[shapeIndex];
                
                normalizeIncomingTetrominoHeightAndXPosition(incomingTetrominoAllRotations, rotationIndex, heightScale);
                doSendNewBlock = false;
            }
            
            StdDraw.clear(backgroundColor);   // clear the background (double buffering)
            
            
            drawOccupiedBlocks(occupiedBlocks);
            
            drawIncomingTetromino(incomingTetrominoAllRotations[rotationIndex]);
            
            drawGrid(heightScale, widthScale, gridColor);
            drawMenuAndSeperator(score, menuColor, seperatorColor, widthScale, heightScale);
            drawNextTetromino(widthScale, Tetrominoes.tetrominoes[minoeGenerator.getIncomingShapeIndex()][minoeGenerator.getIncomingRotationIndex()]);
            
            if (willTetrominoPassBottomBorder(incomingTetrominoAllRotations[rotationIndex]) || hasBlockToBottomOf(incomingTetrominoAllRotations[rotationIndex], occupiedBlocks)) {
                saveOccupiedBlocks(occupiedBlocks, incomingTetrominoAllRotations[rotationIndex]);
                score = mergeAppropriateBlocksAndCalculateScore(occupiedBlocks, widthScale, score);
                
                // System.out.println("y when saving: " + incomingTetrominoAllRotations[rotationIndex][0].y);
                
                
                System.out.println("x when saving: " + incomingTetrominoAllRotations[rotationIndex][0].x);
               
               
                /*for (Block occupiedBlock : occupiedBlocks) {
                    if (occupiedBlock.y >= heightScale) {
                        System.out.println("heights " +heightScale);
                        System.out.println("game over");
                    }
               
               
               
                }*/
                
                
                doSendNewBlock = true;
            } else
                for (Block[] blocks : incomingTetrominoAllRotations)
                    for (Block block : blocks)
                        block.y--;
            
            
            StdDraw.show();
            
            StdDraw.pause(500); // pause for 200 ms (double buffering)
            
            
        }
    }
    
    /**
     * drawNextTetromino: Draws the incoming tetromino on the canvas
     *
     * @param width:     Width Scale
     * @param tetromino: The tetromino array
     */
    private static void drawNextTetromino(int width, Block[] tetromino) {
        Block leftmostBlock = new Block(500, 50);
        Block uppermostBlock = new Block(0, 0);
        
        for (Block block : tetromino) {
            if (leftmostBlock.x > block.x)
                leftmostBlock = block;
            if (uppermostBlock.y < block.y)
                uppermostBlock = block;
        }
        double diffXToAdd = (width + 1) - leftmostBlock.x;
        double diffYToAdd = 4 - uppermostBlock.y;
        for (Block block : tetromino) {
            block.x += diffXToAdd;
            block.y += diffYToAdd;
        }
        for (Block block : tetromino) {
            StdDraw.setPenColor(block.getColor());
            StdDraw.filledRectangle(block.x, block.y, 0.5, 0.5);
            Font font = new Font("Arial",Font.BOLD, 30);
            StdDraw.setFont(font);
            StdDraw.text(block.x,block.y,"A");
           // StdDraw.text();
            //TODO:StdDraw.text(sayılar);
        }
    }
    
    /**
     * mergeAppropriateBlocksAndCalculateScore: The method that decides the merge operation will continue or not. And calculates the score of the game.
     *
     * @param occupiedBlocks: The blocks after the tetrominoes has landed on the bottom or above of some block
     * @param widthScale:     Width scale
     * @param score:          Score that the user gains.
     * @return score
     */
    
    private static int mergeAppropriateBlocksAndCalculateScore(List<Block> occupiedBlocks, int widthScale, int score) {
        boolean canMergeFurthermore; // boolean will detect some merge operations if exist
        for (int i = 0; i < widthScale; i++) {
            canMergeFurthermore = true;
            List<Block> blocksInXPosition;
            int counter = 0;
            while (canMergeFurthermore) {
                canMergeFurthermore = false;
                
                blocksInXPosition = new ArrayList<>();
                for (Block occupiedBlock : occupiedBlocks)
                    if (occupiedBlock.x == i)
                        blocksInXPosition.add(occupiedBlock);
                blocksInXPosition.sort(Comparator.comparingInt(block -> block.y)); // sort list by property of it's objects (Y position) ascending
                
                for (int i1 = 0; i1 < blocksInXPosition.size() - 1; i1++) {
                    if (blocksInXPosition.get(i1).value == blocksInXPosition.get(i1 + 1).value) {
                        canMergeFurthermore = true;
                        break;
                    }
                }
                
                if (blocksInXPosition.size() < 2)
                    break;
                
                
                if (!canMergeFurthermore)
                    break;
                
                Block blockToRemove = null;
                for (int counterInLoop = counter; counterInLoop < blocksInXPosition.size() - 1; counterInLoop++) {
                    Block blockBelow = blocksInXPosition.get(counterInLoop);
                    counter = counterInLoop;
                    Block blockAbove = blocksInXPosition.get(counterInLoop + 1);
                    if (blockAbove.y - blockBelow.y == 1 && blockBelow.value == blockAbove.value) {
                        for (Block occupiedBlock : occupiedBlocks) {
                            if (occupiedBlock.y == blockBelow.y && occupiedBlock.x == blockBelow.x) {
                                occupiedBlock.value++;
                                break;
                            }
                        }
                        blockToRemove = blockAbove;
                        counter = 0;
                        break;
                    }
                }
                if (blockToRemove != null) {
                    occupiedBlocks.remove(blockToRemove);
                    score += Math.pow(2, blockToRemove.value + 1);
                }
                fallDown(occupiedBlocks);
                
                canMergeFurthermore = false;
                blocksInXPosition = new ArrayList<>();
                for (Block occupiedBlock : occupiedBlocks)
                    if (occupiedBlock.x == i)
                        blocksInXPosition.add(occupiedBlock);
                blocksInXPosition.sort(Comparator.comparingInt(block -> block.y)); // sort list by property of it's objects( Y position ) ascending
                
                for (int i1 = 0; i1 < blocksInXPosition.size() - 1; i1++)
                    if (blocksInXPosition.get(i1).value == blocksInXPosition.get(i1 + 1).value) {
                        canMergeFurthermore = true;
                        break;
                    }
            }
            
        }
        
        return score;
    }
    
    /**
     * fallDown: The method that checks the fall down situation with boolean return.
     *
     * @param occupiedBlocks: The blocks after the tetrominoes has landed on the bottom or above of some block
     * @return didIWork: Boolean that returns true or false the did the method
     */
    private static void fallDown(List<Block> occupiedBlocks) {
        for (Block occupiedBlock : occupiedBlocks) {
            while (occupiedBlock.y != 0 && !isThereBlockBelowOnX(occupiedBlock, occupiedBlocks, occupiedBlock.x))
                occupiedBlock.y--;
            
        }
    }
    
    /**
     * isThereBlockBelowOnX: The method that returns true if there is any blocks below on each X axis column.
     *
     * @param block:          Each block on the canvas
     * @param occupiedBlocks: The blocks after the tetrominoes has landed on the bottom or above of some block
     * @param value:          Value that to compare if it is equal to x axis
     * @return boolean: false
     */
    private static boolean isThereBlockBelowOnX(Block block, List<Block> occupiedBlocks, int value) {
        for (Block occupiedBlock : occupiedBlocks)
            if (occupiedBlock.x == value && occupiedBlock.y == block.y - 1)
                return true;
        return false;
    }
    
    /**
     * drawMenuAndSeparator: Draws the menu and the separator.
     *
     * @param score:          Score that the user gains
     * @param menuColor:      Menu color
     * @param seperatorColor: The separator color
     * @param startX:         Start position x-axis
     * @param height:         y-axis
     */
    @SuppressWarnings("IntegerDivisionInFloatingPointContext")
    private static void drawMenuAndSeperator(int score, Color menuColor, Color seperatorColor, int startX, int height) {
        StdDraw.setPenColor(menuColor);
        StdDraw.filledRectangle(startX + 2, height / 2, 2.5, height / 2 + 1);
        StdDraw.setPenColor(Color.WHITE);
        Font font = new Font("Clear Sans", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.text(startX + 2, height - 2, "SCORE");
        StdDraw.text(startX + 2, height - 3, String.valueOf(score));
        StdDraw.text(startX + 2, 5, "NEXT ");
    }
    
    /**
     * drawIncomingTetromino: The method that draws the incoming tetromino to the canvas with StdDraw.
     *
     * @param incomingTetrominoAllRotation: Tetromino array
     */
    
    private static void drawIncomingTetromino(Block[] incomingTetrominoAllRotation) {
        for (Block block : incomingTetrominoAllRotation) {
            StdDraw.setPenColor(block.getColor());
            StdDraw.filledRectangle(block.x, block.y, 0.5, 0.5);
            Color numberWhite = new Color(249, 246, 242);
            Color numberDark = new Color(127, 111, 98);
            int valueOnTheBlocks = (int) Math.pow(2, block.value);
            colorOfNumberDecider(block, valueOnTheBlocks, numberWhite, numberDark);
        }
    }
    
    /**
     * drawOccupiedBlocks: The method that draws the occupied blocks to the canvas with StdDraw.
     *
     * @param occupiedBlocks: The blocks after the tetrominoes has landed on the bottom or above of some block
     */
    private static void drawOccupiedBlocks(List<Block> occupiedBlocks) {
        for (Block occupiedBlock : occupiedBlocks) {
            StdDraw.setPenColor(occupiedBlock.getColor());
            StdDraw.filledRectangle(occupiedBlock.x, occupiedBlock.y, 0.5, 0.5);
            int valueOnTheBlocks = (int) Math.pow(2, occupiedBlock.value);
            Color numberWhite = new Color(249, 246, 242);
            Color numberDark = new Color(127, 111, 98);
            colorOfNumberDecider(occupiedBlock, valueOnTheBlocks, numberWhite, numberDark);
            /*Color numberWhite = new Color(249,246,242);
            StdDraw.setPenColor(numberWhite);
            Font numberFont = new Font("Times New Roman", Font.BOLD, 30);
            StdDraw.setFont(numberFont);
            StdDraw.text(occupiedBlock.x, occupiedBlock.y, String.valueOf((int) Math.pow(2, occupiedBlock.value)));*/
            
            
        }
    }
    
    /**
     * colorOfNumberDecider: The method decides the color of the value on the block.
     *
     * @param occupiedBlock:    The blocks after the tetrominoes has landed on the bottom or above of some block
     * @param valueOnTheBlocks: The integer value on the blocks
     * @param numberWhite:      White color of the appropriate numbers on canvas
     * @param numberDark:       Dark gray color of the appropriate numbers on canvas
     */
    private static void colorOfNumberDecider(Block occupiedBlock, int valueOnTheBlocks, Color numberWhite, Color numberDark) {
        Font clearSansFont = null;
        if (valueOnTheBlocks == 2 || valueOnTheBlocks == 4) {  // Dark colors of the number two and four
            StdDraw.setPenColor(numberDark);
            
            try {
                clearSansFont = Font.createFont(Font.TRUETYPE_FONT,new File("ClearSans-Bold.ttf")).deriveFont(30f);
            } catch (FontFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(clearSansFont);
            StdDraw.setFont(clearSansFont);
            
            
        } else {
            StdDraw.setPenColor(numberWhite);
          Font numberFont = new Font("Calibre", Font.BOLD, 30);
            
            StdDraw.setFont(numberFont);
        }
        StdDraw.text(occupiedBlock.x, occupiedBlock.y, String.valueOf(valueOnTheBlocks));
    }
    
    /**
     * drawGrid: The method that draws the grid.
     *
     * @param heightScale: Y-axis scale
     * @param widthScale:  X-axis scale
     * @param gridColor:   Grid color
     */
    
    private static void drawGrid(int heightScale, int widthScale, Color gridColor) {
        StdDraw.setPenColor(gridColor); // Draws the grid
        for (double x = -0.5; x <= widthScale - 0.5; x++)    // vertical lines
            StdDraw.line(x, -0.5, x, heightScale - 0.5);
        for (double y = -0.5; y <= heightScale - 0.5; y++)     // horizontal lines
            StdDraw.line(-0.5, y, widthScale - 0.5, y);
        
    }
    
    /**
     * normalizeIncomingTetrominoHeightAndXPosition: Normalizes the position of the incoming tetromino.
     *
     * @param incomingTetrominoeAllRotations: Incoming tetrominoe array
     * @param rotationIndex:                  The index that the all rotations of an each tetromino in order to easily implement the rotation
     * @param heightScale:                    Y-axis scale
     */
    private static void normalizeIncomingTetrominoHeightAndXPosition(Block[][] incomingTetrominoeAllRotations, int rotationIndex, int heightScale) {
        Block lowestPoint = new Block(100, 100);
        for (Block block : incomingTetrominoeAllRotations[rotationIndex]) {
            if (lowestPoint.y > block.y)
                lowestPoint = block;
        }
        // Normalizing the y position
        int yAmountToAdd = (heightScale - lowestPoint.y);
        for (Block[] blocks : incomingTetrominoeAllRotations) {
            for (Block block : blocks) {
                if(!block.A)
                    block.x += 2;
                block.A = true;
                block.y += yAmountToAdd;
            }
        }
    }
    
    /**
     * handleUserInput: The method that handles the user input with considering the wall kick conditions
     *
     * @param incomingTetrominoeAllRotations: Incoming tetromino array
     * @param occupiedBlocks:                 Occupied blocks list
     * @param rotationIndex:                  The index that the all rotations of an each tetromino in order to easily implement the rotation
     * @param ch:                             The character that will entered by the user
     * @param widthScale:                     x-axis scale
     * @return: int value of rotationIndex
     */
    private static int handleUserInput(Block[][] incomingTetrominoeAllRotations, List<Block> occupiedBlocks,
                                       int rotationIndex, char ch, int widthScale) {
        Block[] incomingTetrominoe = incomingTetrominoeAllRotations[rotationIndex];
        switch (ch) {
            case 'a': {
                if (!hasBlockToLeftOf(incomingTetrominoe, occupiedBlocks) & !willPassLeftBorder(incomingTetrominoe))
                    moveLeft(incomingTetrominoeAllRotations);
                break;
            }
            case 'd': {
                if (!hasBlockToRightOf(incomingTetrominoe, occupiedBlocks) & !willPassRightBorder(incomingTetrominoe, widthScale))
                    moveRight(incomingTetrominoeAllRotations);
                break;
            }
            case 's': {
                while (!hasBlockToBottomOf(incomingTetrominoe, occupiedBlocks) & !willTetrominoPassBottomBorder(incomingTetrominoe)) {
                    moveDown(incomingTetrominoeAllRotations);
                }
                break;
            }
            case 'q': {
                Block leftmostBlock = new Block(100, 100), rightmostBlock = new Block(0, 0);
                for (Block block : incomingTetrominoeAllRotations[rotationIndex]) {
                    if (leftmostBlock.x > block.x)
                        leftmostBlock = block;
                    else if (rightmostBlock.x < block.x)
                        rightmostBlock = block;
                }
                if (rotationIndex == 0) {
                    for (Block block : incomingTetrominoeAllRotations[3])
                        for (Block occupiedBlock : occupiedBlocks)
                            if (block.x == occupiedBlock.x & block.y == occupiedBlock.y)
                                return 0;
                    return (leftmostBlock.x >= 0) & (rightmostBlock.x <= widthScale) ? 3 : 0;
                } else {
                    for (Block block : incomingTetrominoeAllRotations[rotationIndex - 1])
                        for (Block occupiedBlock : occupiedBlocks)
                            if (block.x == occupiedBlock.x & block.y == occupiedBlock.y)
                                return rotationIndex;
                    return (leftmostBlock.x >= 0) & (rightmostBlock.x <= widthScale) ? rotationIndex - 1 : rotationIndex;
                }
            }
            case 'e': {
                Block leftmostBlock = new Block(100, 100), rightmostBlock = new Block(0, 0);
                for (Block block : incomingTetrominoeAllRotations[rotationIndex]) {
                    if (leftmostBlock.x > block.x)
                        leftmostBlock = block;
                    else if (rightmostBlock.x < block.x)
                        rightmostBlock = block;
                }
                if (rotationIndex == 3) {
                    for (Block block : incomingTetrominoeAllRotations[0])
                        for (Block occupiedBlock : occupiedBlocks)
                            if (block.x == occupiedBlock.x & block.y == occupiedBlock.y)
                                return 3;
                    return (leftmostBlock.x >= 0) & (rightmostBlock.x <= widthScale) ? 0 : 3;
                } else {
                    for (Block block : incomingTetrominoeAllRotations[rotationIndex + 1])
                        for (Block occupiedBlock : occupiedBlocks)
                            if (block.x == occupiedBlock.x & block.y == occupiedBlock.y)
                                return rotationIndex;
                    return (leftmostBlock.x >= 0) & (rightmostBlock.x <= widthScale) ? rotationIndex + 1 : rotationIndex;
                }
            }
        }
        return rotationIndex;
    }
    
    /**
     * hasBlockToBottomOf: The method that returns true if there is any block underneath of the current block.
     *
     * @param incomingTetrominoe: Incoming tetromino array
     * @param occupiedBlocks:     Occupied blocks list
     * @return : True or false depends on the conditions
     */
    private static boolean hasBlockToBottomOf(Block[] incomingTetrominoe, List<Block> occupiedBlocks) {
        for (Block occupiedBlock : occupiedBlocks)
            for (Block block : incomingTetrominoe) {
                if (occupiedBlock.x == block.x && occupiedBlock.y == block.y - 1)
                    return true;
            }
        return false;
        
    }
    
    /**
     * moveDown: The method that decreases the y-axis.
     *
     * @param incomingTetrominoeAllRotations: Incoming tetromino
     */
    private static void moveDown(Block[][] incomingTetrominoeAllRotations) {
        for (Block[] incomingTetrominoeAllRotation : incomingTetrominoeAllRotations)
            for (Block block : incomingTetrominoeAllRotation)
                block.y -= 1;
        
        
    }
    
    /**
     * moveRight: The method that increases the y-axis.
     *
     * @param incomingTetrominoAllRotations: Incoming tetromino
     */
    private static void moveRight(Block[][] incomingTetrominoAllRotations) {
        for (Block[] incomingTetrominoeAllRotation : incomingTetrominoAllRotations)
            for (Block block : incomingTetrominoeAllRotation)
                block.x += 1;
    }
    
    /**
     * moveLeft: The method that decreases the x-axis.
     *
     * @param incomingTetrominoAllRotations: Incoming tetromino
     */
    private static void moveLeft(Block[][] incomingTetrominoAllRotations) {
        for (Block[] incomingTetrominoeAllRotation : incomingTetrominoAllRotations)
            for (Block block : incomingTetrominoeAllRotation)
                block.x -= 1;
        
        
    }
    
    /**
     * hasBlockToLeftOf: The method that checks are there any block on the left of the current block.
     *
     * @param incomingTetromino: Incoming tetromino
     * @param occupiedBlocks:    Occupied Blocks
     * @return: true or false depends on there is any block on the left
     */
    private static boolean hasBlockToLeftOf(Block[] incomingTetromino, List<Block> occupiedBlocks) {
        for (Block occupiedBlock : occupiedBlocks)
            for (Block block : incomingTetromino) {
                if (occupiedBlock.x == block.x - 1 && occupiedBlock.y == block.y)
                    return true;
            }
        
        
        return false;
    }
    
    /**
     * hasBlockToRightOf: The method that checks are there any block on the left of the current block.
     *
     * @param incomingTetromino: Incoming tetromino
     * @param occupiedBlocks:    Occupied Blocks
     * @return: true or false depends on there is any block on the right
     */
    private static boolean hasBlockToRightOf(Block[] incomingTetromino, List<Block> occupiedBlocks) {
        for (Block occupiedBlock : occupiedBlocks)
            for (Block block : incomingTetromino) {
                if (occupiedBlock.x == block.x + 1 && occupiedBlock.y == block.y)
                    return true;
            }
        
        
        return false;
    }
    
    /**
     * saveOccupiedBlocks: The method that saves the occupied blocks in order to treat it as blocks after the tetromino landed.
     *
     * @param occupiedBlocks:               Occupied Blocks
     * @param incomingTetrominoAllRotation: Incoming tetromino
     */
    private static void saveOccupiedBlocks(List<Block> occupiedBlocks, Block[] incomingTetrominoAllRotation) {
        for (Block block : incomingTetrominoAllRotation) {
            Block pos = new Block(block.x, block.y, block.value);
            occupiedBlocks.add(pos);
        }
        
    }
    
    /**
     * willPassRightBorder: The method that checks the tetromino pass the right border or not.
     *
     * @param tetrominoe:
     * @param widthScale: X scale
     * @return boolean true or false depends on the situation
     */
    //    being called from rotation action, checks if rightmost block is outside bounds
//    if returns false, check for collisions and apply rotation command
    private static boolean willPassRightBorder(Block[] tetrominoe, int widthScale) {
        Block rightmostPoint = new Block(-50, -50);
        for (Block block : tetrominoe) {
            if (rightmostPoint.x < block.x)
                rightmostPoint = block;
        }
        return rightmostPoint.x >= widthScale - 1;
    }
    
    /**
     * willPassLeftBorder: The method that checks the tetromino pass the left border or not.
     *
     * @param tetrominoe:
     * @return boolean true or false depends on the condition
     */
    private static boolean willPassLeftBorder(Block[] tetrominoe) {
        Block leftmostPoint = new Block(50, 50);
        for (Block block : tetrominoe) {
            if (leftmostPoint.x > block.x)
                leftmostPoint = block;
        }
        return leftmostPoint.x - 1 < 0;
    }
    
    /**
     * willTetrominoPassBottomBorder: The method that checks will tetromino pass the bottom border.
     *
     * @param tetrominoe: //
     * @return boolean true or false depends on the condition
     */
    
    private static boolean willTetrominoPassBottomBorder(Block[] tetrominoe) {
        Block lowestPoint = new Block(50, 50);
        for (Block block : tetrominoe) {
            if (lowestPoint.y > block.y)
                lowestPoint = block;
        }
        return lowestPoint.y <= 0; // TODO: ???????
    }
    
    
    /**
     * initCanvas: Initializes the canvas.
     *
     * @param heightScale: Y-axis
     * @param widthScale:  X-axis
     */
    private static void initCanvas(int heightScale, int widthScale) {
        StdDraw.setCanvasSize(700, 750);
        // set the scale of the coordinate system
        StdDraw.setXscale(-0.5, widthScale + 4.5);
        StdDraw.setYscale(-0.5, heightScale - 0.5);
        // double buffering is used for speeding up drawing needed to enable computer animations
        // check https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html for details
        StdDraw.enableDoubleBuffering();
        
        
    }
    
}

