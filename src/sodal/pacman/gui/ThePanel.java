package sodal.pacman.gui;

import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.entity.food.Food;
import sodal.pacman.entity.player.Player;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;


public class ThePanel extends JPanel implements Runnable, KeyListener {

    // SETTINGS / CONFIGURATION
    private static final int TILE_SIZE = 30;
    private static final int NUM_TILES_WIDTH = 17;
    private static final int NUM_TILES_HEIGHT = 22;
    private static final int WIDTH = NUM_TILES_WIDTH * TILE_SIZE;
    private static final int HEIGHT = NUM_TILES_HEIGHT * TILE_SIZE;


    // GAME STATE FLAGS
    private static boolean isRunning = false;
    private boolean gameOver = false;
    private boolean restart = false;
    private volatile boolean drawGameOver = false;
    private static volatile boolean playerEnemyCollision = false;
    private static boolean startGame = false;


    // TIMERS / DELAYS
    private final long FREEZE_DURATION_MS = 2000;
    private final long GAME_OVER_DELAY_MS = 3000;
    //player and enemy have collided with each other.
    private long collisionTimeStamp = 0;
    private long gameOverTimeStamp = -1;

    //DEAD ANIMATION
    private final long DEATH_ANIMATION_DELAY_MS = 120;
    private long deathAnimationStartTime = 0;
    private boolean startDeadAnimation = false;


    // CORE ENTITIES
    private static Player player;
    private Enemy[] enemies = new Enemy[4];
    private static Rectangle[] world = new Rectangle[15];
    private byte[][] worldData = new byte[NUM_TILES_HEIGHT][NUM_TILES_WIDTH];


    // GAME LOOP
    private int FPS = 60;
    private Thread gameLoop;


    // UI COMPONENTS / GRAPHICS
    private BufferedImage gameOverBuffer;
    private BufferedImage playBuffer;
    private BufferedImage menuBuffer;
    // private Rectangle buttonRect;
    private BufferedImage[] worldImages = new BufferedImage[6];
    private static Point gameOverHover = new Point(TILE_SIZE * 7, TILE_SIZE * 10);


    // SCORE / UI ELEMENTS
    private ScoreBoard scoreBoard;


    private Clip backgroundClip;
    private boolean startBackgroundSound = false;


    ///FOOD
    private Food food;

    private  static  List<Food> allFood = new ArrayList<>();


    public ThePanel() {
        setupPanel();
        setUpEnemies();
        setUpPlayer();
        setUpScoreBoard();
        setUpBuffer();
        setUpWorldRectangles();
        backgroundClip = getClip("./res/sound/siren.wav");
        //FOOD
        loadWorldMap();
        setUpGameLoop();
    }


    private void addAllFood() {
        int foodSize = Food.getSize();
        food = new Food(0 + TILE_SIZE / 2 - foodSize / 2, 0 + TILE_SIZE / 2 - foodSize / 2);
    }

    private void createWorldBuffer() {
        try {
            for (int i = 0; i < worldImages.length; i++) {
                worldImages[i] = ImageIO.read(new File("./res/image/world/" + (i + 1) + ".png"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadWorldMap() {
        String filePath = "./res/worldMap/world.txt"; // Replace with your actual file path
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            addAllFood(br);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void addAllFood(BufferedReader br) {
        try {
            String line = br.readLine();
            int x = 0;
            int y = 0;
            while (line != null) {
                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    if (c == '0') {
                        //add fruit
                       addFruit(x,y);
                    }
                    x += TILE_SIZE;
                }
                x = 0;
                y += TILE_SIZE;

                line = br.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFruit(int x, int y) {
        int foodSize = Food.getSize();
        Food food = new Food(x + TILE_SIZE / 2 - foodSize / 2, y + TILE_SIZE / 2 - foodSize / 2);
        allFood.add(food);
    }



    private void setUpGameLoop() {
        //game loop
        gameLoop = new Thread(this);
        // gameLoop.start();
    }


    private void setUpBuffer() {
        this.gameOverBuffer = createBuffer(TILE_SIZE * 3, TILE_SIZE * 3, "./res/image/gameover/gameOver.png");
        this.playBuffer = createBuffer(TILE_SIZE * 3, TILE_SIZE * 1, "./res/image/menu/play.png");
        this.menuBuffer = createBuffer(TILE_SIZE * 3, TILE_SIZE * 1, "./res/image/menu/menu.png");
    }

    private void setUpScoreBoard() {
        scoreBoard = new ScoreBoard(player);
    }

    private void setUpPlayer() {
        int radius = TILE_SIZE / 2;
        player = new Player(TILE_SIZE * 8 + radius, (HEIGHT - 3 * TILE_SIZE) + radius, radius, 3, this);
    }

    private void setUpEnemies() {
        //enemies
        enemies[0] = new Enemy(TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE, 1, "./src/sodal/pacman/entity/enemy/image/blinky.png", 1000);
        enemies[1] = new Enemy(TILE_SIZE, HEIGHT - 4 * TILE_SIZE, TILE_SIZE, TILE_SIZE, 1, "./src/sodal/pacman/entity/enemy/image/clyde.png", 2000);
        enemies[2] = new Enemy(WIDTH - 2 * TILE_SIZE, HEIGHT - 4 * TILE_SIZE, TILE_SIZE, TILE_SIZE, 1, "./src/sodal/pacman/entity/enemy/image/inky.png", 3000);
        enemies[3] = new Enemy(WIDTH - 2 * TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE, 1, "./src/sodal/pacman/entity/enemy/image/pinky.png", 4000);
    }

    private void setupPanel() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setOpaque(true);
        this.setDoubleBuffered(true);
        this.setLayout(null);
        this.setDoubleBuffered(true);
        //lister
        this.addKeyListener(this);
        this.setFocusable(true);
        this.requestFocusInWindow();

    }


    private void update() {
        if (!gameOver) {
            if (startGame) {
               // playBackgroundSound();
                scoreBoard.update();
                updatePlayerAndEnemies();
                checkCollision();
            }
        } else {
            handleGameOverDelay();
            handleRestart();
        }
    }

    private void handleGameOverDelay() {
        if (gameOverTimeStamp == -1) {
            gameOverTimeStamp = System.currentTimeMillis();
        }
        //  GAME_OVER_DELAY_MS delay before gameover state is displayed on screen.
        if (!drawGameOver && System.currentTimeMillis() - gameOverTimeStamp >= GAME_OVER_DELAY_MS) {
            drawGameOver = true;
        }

    }

    private void handleRestart() {
        //delay game over time has passed
        if (drawGameOver && restart) {
            restart();
        }
    }


    private void updatePlayerAndEnemies() {
        if (!playerEnemyCollision) {
            for (Enemy enemy : enemies) {
                enemy.update();
            }
            player.update();
        }
    }


    /**
     * collision between player and enemy
     */
    public void checkCollision() {
        checkPlayerEnemyCollision();
    }

    public void checkPlayerEnemyCollision() {
        if (!playerEnemyCollision) {
            for (Enemy enemy : enemies) {
                if (player.getRect().intersects(enemy.getEnemyRect())) {
                    for (Rectangle rect : enemy.getRect()) {
                        //collision!!!
                        if (circleRectCollision(rect)) {
                            processPlayerEnemyCollision();
                            return;
                        }
                    }
                }
            }
        } else {
            collisionDelay();
        }
    }

    private void processPlayerEnemyCollision() {
        //stop background sound
        stopBackgroundSound();
        System.out.println(" collided!!!");
        playerEnemyCollision = true;
        player.decrementHealth();
        stopPlayerEnemiesMovement();
        collisionTimeStamp = System.currentTimeMillis();

    }

    private void stopBackgroundSound() {
        backgroundClip.stop();
        backgroundClip.close();
    }


    private void stopPlayerEnemiesMovement() {
        player.setSpeed(0);
        for (Enemy enemy : enemies) {
            enemy.setSpeed(0);
        }
    }


    private void hideEnemies() {
        for (Enemy enemy : enemies) {
            enemy.setSize(0, 0);
        }
    }

    /**
     * the time in which pacman and enemies freeze when pacman hots enemies
     *
     * @return
     */

    private boolean freezeCollision() {
        if (System.currentTimeMillis() - collisionTimeStamp >= FREEZE_DURATION_MS) {
            return true;
        }
        return false;
    }

    private void collisionDelay() {
        //The player enemy collision "freeze"
        if (freezeCollision()) {
            //make enemies invisible
            hideEnemies();
            if (deadAnimation()) {
                if (player.getHealth() == 0) {
                    handleGameOverState();
                } else {
                    respawn();
                    replayBackgroundSound();
                }
            }
        }

    }

    private void handleGameOverState() {
        //animation done
        player.setSize(0, 0);
        //display gameover
        gameOver = true;
        startGame = false;
    }

    private void respawn() {
        //display transparent image for 1 extra second
        //Dead animation has finished
        player.resetToInitialState();
        //put enemies in initial position
        for (Enemy enemy : enemies) {
            enemy.resetToInitialState();
        }
        playerEnemyCollision = false;
    }


    private void restart() {
        respawn();
        player.resetHealth();
        player.setScore(0);
        //make all food
        resetAllFoodSize();
        drawGameOver = false;
        restart = false;
        startBackgroundSound = false;
        gameOverTimeStamp = -1;
        gameOver = false;
    }

    private void resetAllFoodSize() {
        for(Food food : allFood) {
            food.setSize(Food.getSize(),Food.getSize());
        }
    }

    private boolean deadAnimation() {
        beginDeathAnimation();
        long deltaTime = System.currentTimeMillis() - deathAnimationStartTime;
        updateDeadAnimationFrame(deltaTime);
        long extra_time_ms = player.getHealth() == 0 ? 0 : 1000;
        long timeDuration = player.getDeadBuffer().length * DEATH_ANIMATION_DELAY_MS + extra_time_ms;
        if (deltaTime >= timeDuration) {
            startDeadAnimation = false;
            return true;
        }
        return false;
    }

    private void beginDeathAnimation() {
        if (!startDeadAnimation) {
            startDeadAnimation = true;
            playSound("./res/sound/dead.wav", 0);
            deathAnimationStartTime = System.currentTimeMillis();
        }
    }

    private void updateDeadAnimationFrame(long deltaTime) {
        for (int i = 0; i < player.getDeadBuffer().length; i++) {
            if (deltaTime >= i * DEATH_ANIMATION_DELAY_MS && deltaTime < (i + 1) * DEATH_ANIMATION_DELAY_MS) {
                player.setImage(player.getDeadBuffer()[i]);
            }
        }
    }


    private BufferedImage createBuffer(int width, int height, String path) {
        try {
            // Load the original image
            BufferedImage original = ImageIO.read(new File(path));

            // Create a new resized image
            BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resized.createGraphics();

            // Apply rendering hints for better quality
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(original, 0, 0, width, height, null);
            g2d.dispose();

            return resized;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // file could not be found!
        return null;
    }


    public boolean circleRectCollision(Rectangle rect) {
        double distance = getDistance(rect);
        //collision!!!
        if (distance < player.getRadius()) {
            return true;
        }
        return false;
    }


    public double getDistance(Rectangle rect) {
        double closestRectX = clamp(rect.x, rect.x + rect.width, player.getxCenter());
        double closestRectY = clamp(rect.y, rect.y + rect.height, player.getyCenter());
        return distance(closestRectX, closestRectY);
    }


    public void backtrack(Rectangle rect) {
        double playerRadius = player.getRadius();
        double distance = getDistance(rect);
        while (distance < playerRadius) {
            player.moveInOppositeDirection();
            distance = getDistance(rect);
        }
    }


    private double distance(double closestRectX, double closestRectY) {
        double deltaX = Math.abs(player.getxCenter() - closestRectX);
        double deltaY = Math.abs(player.getyCenter() - closestRectY);
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }


    public double clamp(double min, double max, double value) {
        if (value < min) {
            return min;
        } else if (value > max) {
            return max;
        }
        return value;
    }


    public Thread getGameLoop() {
        return gameLoop;
    }


    private void renderGrid(Graphics2D g2) {
        g2.setColor(Color.gray);
        int x = 0;
        int y = 0;
        for (int i = 0; i < NUM_TILES_HEIGHT; i++) {
            for (int j = 0; j < NUM_TILES_WIDTH; j++) {
                g2.drawRect(x, y, TILE_SIZE, TILE_SIZE);
                x += TILE_SIZE;
            }
            x = 0;
            y += TILE_SIZE;
        }
    }


    public static int getTileSize() {
        return TILE_SIZE;
    }


    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        //paint
        renderBackground(g2);
        renderGrid(g2);
        //render all food
        renderFood(g2);
        renderEnemies(g2);
        player.render(g2);
        scoreBoard.render(g2);
        renderWorld(g2);
        //gameOver = true
        //wait for GAME_OVER_DELAY_MS until displaying gameover state
        if (drawGameOver) {
            renderGameOver(g2);
        }



        g2.dispose();
    }

    private void renderFood(Graphics2D g2) {
        for(Food food :  getAllFood()) {
            if(!food.isEaten()) {
                food.render(g2);
            }
        }
    }


    private void renderBackground(Graphics2D g2) {
        g2.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void renderEnemies(Graphics2D g2) {
        for (Enemy enemy : enemies) {
            enemy.render(g2);
        }
    }

    private void renderGameOver(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 200)); // Black with 50% transparency
        g2.fillRect(0, 0, WIDTH, HEIGHT);
        g2.drawImage(gameOverBuffer, TILE_SIZE * 7, TILE_SIZE * 5, null);
        g2.drawImage(playBuffer, TILE_SIZE * 7, TILE_SIZE * 10, null);
        g2.drawImage(menuBuffer, TILE_SIZE * 7, TILE_SIZE * 12, null);
        g2.setColor(Color.green);
        g2.drawRect(gameOverHover.x, gameOverHover.y, playBuffer.getWidth(), playBuffer.getHeight());
    }


    public void renderWorld(Graphics2D g2) {
        g2.setColor(Color.blue);
        for (Rectangle r : world) {
            g2.drawRect(r.x, r.y, r.width, r.height);
        }
        //cover some part of the world
        //  renderLines(g2);
    }

    //cover some part of the world
    private void renderLines(Graphics2D g2) {
        //draw over the lines in world
        g2.setColor(Color.BLACK);
        //L
        g2.drawLine(TILE_SIZE, 12 * TILE_SIZE + 1, TILE_SIZE, 13 * TILE_SIZE - 1);
        //house
        g2.drawLine(21 * TILE_SIZE + 1, 13 * TILE_SIZE, 22 * TILE_SIZE - 1, 13 * TILE_SIZE);
        g2.drawLine(23 * TILE_SIZE + 1, 13 * TILE_SIZE, 24 * TILE_SIZE - 1, 13 * TILE_SIZE);
        //cross
        //left
        g2.drawLine(12 * TILE_SIZE, 7 * TILE_SIZE + 1, 12 * TILE_SIZE, 8 * TILE_SIZE - 1);
        //top
        g2.drawLine(12 * TILE_SIZE + 1, 7 * TILE_SIZE, 13 * TILE_SIZE - 1, 7 * TILE_SIZE);
        //right
        g2.drawLine(13 * TILE_SIZE, 7 * TILE_SIZE + 1, 13 * TILE_SIZE, 8 * TILE_SIZE - 1);
        //bottom
        g2.drawLine(12 * TILE_SIZE + 1, 8 * TILE_SIZE, 13 * TILE_SIZE - 1, 8 * TILE_SIZE);

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    private void switchDirection(int index) {
        for (int i = 0; i < player.getDirectionArray().length; i++) {
            if (i == index) {
                player.getDirectionArray()[index] = 1;
                continue;
            }
            player.getDirectionArray()[i] = 0;
        }
    }


    /**
     * 0 -> up, 1 -> down, 2 -> left, 3 -> right
     *
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        // System.out.println("pressed!!");
        if (!gameOver) {
            //System.out.println("hello");
            handleGameInput(k);
        } else {
            handleGameOverInput(k);
        }

    }


    private void handleGameOverInput(int k) {
        if (k == KeyEvent.VK_UP) {
            System.out.println("up!!!");
            gameOverHover.setLocation(TILE_SIZE * 7, TILE_SIZE * 10);
        } else if (k == KeyEvent.VK_DOWN) {
            System.out.println("down!!!!");
            gameOverHover.setLocation(TILE_SIZE * 7, TILE_SIZE * 12);
        } else if (k == KeyEvent.VK_ENTER) {
            //replay game (hover same pos as playButton).
            if (gameOverHover.getY() == TILE_SIZE * 10) {
                restart = true;
                System.out.println("enter");
            }
        }


    }

    private void handleGameInput(int k) {
        if (!playerEnemyCollision) {
            if (k == KeyEvent.VK_UP) {
                switchDirection(0);
            } else if (k == KeyEvent.VK_DOWN) {
                switchDirection(1);
            } else if (k == KeyEvent.VK_LEFT) {
                switchDirection(2);
            } else if (k == KeyEvent.VK_RIGHT) {
                switchDirection(3);
            }
        }
        startGame = true;
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }


    //game loop.
    @Override
    public void run() {

        isRunning = true;
        double drawInterval = 1000_000_000.0 / FPS; // 0.01666 seconds.
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        //display fps variables.
        long timer = 0;
        int drawCount = 0;

        while (isRunning) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            //displays FPS.
            if (timer >= 1_000_000_000) {
                // System.out.println("FPS: " + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }


    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }


    public static Rectangle[] getWorld() {
        return world;
    }

    private void setUpWorldRectangles() {
        //left portal
        world[0] = new Rectangle(0, 2 * TILE_SIZE, TILE_SIZE, 7 * TILE_SIZE);
        world[1] = new Rectangle(0, 9 * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE);
        world[2] = new Rectangle(0, 11 * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE);
        world[3] = new Rectangle(0, 12 * TILE_SIZE, TILE_SIZE, 7 * TILE_SIZE);
        //right portals
        world[4] = new Rectangle(WIDTH - TILE_SIZE, 2 * TILE_SIZE, TILE_SIZE, 7 * TILE_SIZE);
        world[5] = new Rectangle(WIDTH - 3 * TILE_SIZE, 9 * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE);
        world[6] = new Rectangle(WIDTH - 3 * TILE_SIZE, 11 * TILE_SIZE, 3 * TILE_SIZE, TILE_SIZE);
        world[7] = new Rectangle(WIDTH - TILE_SIZE, 12 * TILE_SIZE, TILE_SIZE, 7 * TILE_SIZE);

        //pacman house
        //left
        world[8] = new Rectangle(7 * TILE_SIZE, HEIGHT - 3 * TILE_SIZE, TILE_SIZE, 2 * TILE_SIZE);
        //middle
        world[9] = new Rectangle(8 * TILE_SIZE, HEIGHT - 2 * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        //right
        world[10] = new Rectangle(9 * TILE_SIZE, HEIGHT - 3 * TILE_SIZE, TILE_SIZE, 2 * TILE_SIZE);


        //top "house
        world[11] = new Rectangle(7 * TILE_SIZE, 0, TILE_SIZE, 2 * TILE_SIZE);
        world[12] = new Rectangle(8 * TILE_SIZE, 0, TILE_SIZE, TILE_SIZE);
        world[13] = new Rectangle(9 * TILE_SIZE, 0, TILE_SIZE, 2 * TILE_SIZE);

        //block in middle
        world[14] = new Rectangle(7 * TILE_SIZE, 7 * TILE_SIZE, 3 * TILE_SIZE, 7 * TILE_SIZE);


        //ghost house
        //top
       /* world[11] = new Rectangle(5*TILE_SIZE + TILE_SIZE/2, 0 ,6* TILE_SIZE ,   TILE_SIZE);
        //left
        world[12] = new Rectangle(5*TILE_SIZE + TILE_SIZE/2, TILE_SIZE , TILE_SIZE ,   3*  TILE_SIZE);
        //right
        world[13] = new Rectangle(10*TILE_SIZE + TILE_SIZE/2, TILE_SIZE ,  TILE_SIZE ,   3*  TILE_SIZE);*/


    }


    public static void playSound(String filePath, int count) {
        File file = new File(filePath);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(count);
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public static Clip getClip(String filePath) {
        File file = new File(filePath);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            return clip;
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            e.printStackTrace();
        }
        //something wrong... clip should not be null
        return null;
    }


    /**
     * play background at the start of the game
     */
    private void playBackgroundSound() {
        if (!startBackgroundSound) {
            backgroundClip = getClip("./res/sound/siren.wav");
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            startBackgroundSound = true;
        }
    }


    private void replayBackgroundSound() {
        backgroundClip = getClip("./res/sound/siren.wav");
        backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
    }


    public  static List<Food> getAllFood() {
        return allFood;
    }


   public static boolean getStartGame() {
        return startGame;
   }


   public  static boolean getPlayerEnemyCollisionState() {
        return playerEnemyCollision;
   }




}
