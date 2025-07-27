package sodal.pacman.gui;

import sodal.pacman.entity.enemy.Enemy;
import sodal.pacman.entity.food.Food;
import sodal.pacman.entity.player.Player;
import sodal.pacman.ui.UIManager;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class GamePanel extends JPanel implements Runnable, KeyListener {

    // SETTINGS / CONFIGURATION
    private static final int TILE_SIZE = 30;
    private static final int NUM_TILES_WIDTH = 17;
    private static final int NUM_TILES_HEIGHT = 22;
    private static final int WIDTH = NUM_TILES_WIDTH * TILE_SIZE;
    private static final int HEIGHT = NUM_TILES_HEIGHT * TILE_SIZE;


    // GAME STATE FLAGS
    private static boolean isRunning = false;
    private static boolean gameOver = false;
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

    // GAME LOOP
    private int FPS = 60;
    private Thread gameLoop;


    // UI COMPONENTS / GRAPHICS
    private BufferedImage gameOverBuffer;
    private BufferedImage playBuffer;
    private BufferedImage menuBuffer;
    private BufferedImage boardBuffer;
    private static Point gameOverHover;

    private Menu menu;


    // SCORE / UI ELEMENTS
    private ScoreBoard scoreBoard;


    private static Clip backgroundClip;
    private boolean startBackgroundSound = false;


    private static List<Food> allFood = new ArrayList<>();
    private static Font gameOverFont;

    private static Point playButtonPos;
    private static Point menuButtonPos;
    private static boolean checkHighScore = false;
    private static boolean  showHighScoreText = false;


    public GamePanel(JFrame frame, Menu menu) {

        this.menu = menu;
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
        setUpPButtonPos();
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
                        addFruit(x, y);
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
        gameLoop.start();
    }

    private void setUpPButtonPos() {
        playButtonPos = new Point(TILE_SIZE * 7, TILE_SIZE * 14);
        menuButtonPos = new Point(TILE_SIZE * 7, TILE_SIZE * 16);
    }

    private void setUpBuffer() {
        this.gameOverBuffer = UIManager.createBuffer(TILE_SIZE * 8, TILE_SIZE * 2, "./res/image/gameover/gameOver.png");
        this.playBuffer = UIManager.createBuffer(TILE_SIZE * 3, TILE_SIZE * 1, "./res/image/menu/play.png");
        this.menuBuffer = UIManager.createBuffer(TILE_SIZE * 3, TILE_SIZE * 1, "./res/image/menu/menu.png");
        this.boardBuffer = UIManager.createBuffer(TILE_SIZE * 7, TILE_SIZE * 7, "./res/image/gameover/board.png");
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
        gameOverFont = UIManager.getFont("./res/font/pixel.otf", 15f);
    }


    private void update() {
        if (!gameOver) {
            if (startGame) {
              //  playBackgroundSound();
                scoreBoard.update();
                updatePlayerAndEnemies();
                checkCollision();
            }
        } else {
            handleGameOverDelay();
            handleRestart();
            checkHighScore();
        }
    }

    /**
     * check highscore file ONCE when game over.
     */
    private void checkHighScore() {
        if (!checkHighScore) {
            readHighScoreFile();
        }
        checkHighScore = true;
    }


    private void readHighScoreFile() {
        try {
            File myObj = new File("src/sodal/pacman/highscore/highscore.txt");
            Scanner myReader = new Scanner(myObj);
            String score = myReader.nextLine();
            highScoreCheck(score, myReader);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    private void highScoreCheck(String score, Scanner myReader) {
        if (score.equals("No high score yet.")) {
            updateHighScore();
            return;
        }
        //check if score is higher than highscore
        if (Integer.parseInt(score) < player.getScore()) {
            //update highscore
            updateHighScore();
            return;
        }
        if (Integer.parseInt(score) == player.getScore()) {
            String highScoreTime = myReader.nextLine();
            int convertToSec = convertTimeToSeconds(highScoreTime);
            if (scoreBoard.getPassedTime() < convertToSec) {
                updateHighScore();
                return;
            }
        }
        showHighScoreText = false;
    }


    public int convertTimeToSeconds(String data) {
        String[] parts = data.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds;
    }


    private void updateHighScore() {
        try {
            FileWriter myWriter = new FileWriter("src/sodal/pacman/highscore/highscore.txt");
            myWriter.write(player.getScore() + System.lineSeparator() );
            myWriter.write(scoreBoard.getTime());
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            showHighScoreText = true;
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
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
            initializeGameState();
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

    public static void stopBackgroundSound() {
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
                   // replayBackgroundSound();
                }
            }
        }

    }

    public static void handleGameOverState() {
        gameOver = true;
        startGame = false;
        checkHighScore = false;
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


    public void initializeGameState() {
        respawn();
        scoreBoard.resetTimer();
        player.resetHealth();
        player.setScore(0);
        gameOverHover = playButtonPos;
        //make all food
        resetAllFoodSize();
        drawGameOver = false;
        restart = false;
        startBackgroundSound = false;
        gameOverTimeStamp = -1;
        gameOver = false;
    }

    private void resetAllFoodSize() {
        for (Food food : allFood) {
            food.setSize(Food.getSize(), Food.getSize());
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
            playSound("./res/sound/dead.wav", 0, 0);
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
        player.render(g2);
        renderEnemies(g2);
        scoreBoard.render(g2);
        renderWorld(g2);
        //wait for GAME_OVER_DELAY_MS until displaying gameover state
        if (drawGameOver) {
            renderGameOver(g2);
        }
        g2.dispose();
    }

    private void renderFood(Graphics2D g2) {
        for (Food food : getAllFood()) {
            if (!food.isEaten()) {
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
        renderGameOverBackground(g2);
        renderGameOverBuffers(g2);
        if(showHighScoreText) {
            renderText(g2, "NEW HIGH SCORE", TILE_SIZE * 7, Color.GREEN);
        }
        renderText(g2, scoreBoard.getGameOverTimeMessage(), TILE_SIZE * 9, ScoreBoard.getTextColor());
        if(player.getScore() == 285) {
            renderText(g2, " MAX SCORE: " + player.getScore(), TILE_SIZE * 8, ScoreBoard.getTextColor());
        }
        else {
            renderText(g2, "SCORE: " + player.getScore(), TILE_SIZE * 8, ScoreBoard.getTextColor());
        }
    }

    private void renderScore(Graphics2D g2) {

    }

    private void renderGameOverBackground(Graphics2D g2) {
        g2.setColor(new Color(0, 0, 0, 200)); // Black with 50% transparency
        g2.fillRect(0, 0, WIDTH, HEIGHT);
    }

    private void renderHover(Graphics2D g2) {
        g2.setColor(Color.green);
        g2.drawRect(gameOverHover.x, gameOverHover.y, playBuffer.getWidth(), playBuffer.getHeight());

    }

    private void renderGameOverBuffers(Graphics2D g2) {
        g2.drawImage(gameOverBuffer, (WIDTH - gameOverBuffer.getWidth()) / 2, TILE_SIZE, null);
        g2.drawImage(playBuffer, playButtonPos.x, playButtonPos.y, null);
        g2.drawImage(menuBuffer, menuButtonPos.x, menuButtonPos.y, null);
        g2.drawImage(boardBuffer, (WIDTH - boardBuffer.getWidth()) / 2, TILE_SIZE * 5, null);
        renderHover(g2);
    }

    private void renderText(Graphics2D g2, String text, int yPos, Color color) {
        g2.setFont(gameOverFont);
        g2.setColor(color);
        // String time =   scoreBoard.getTime();
        g2.drawString(text, scoreBoard.alignX(g2, gameOverFont, text), yPos);
    }


    public void renderWorld(Graphics2D g2) {
        g2.setColor(Color.blue);
        for (Rectangle r : world) {
            g2.drawRect(r.x, r.y, r.width, r.height);
        }
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
        if (!gameOver) {
            handleGameInput(k);
        } else {
            handleGameOverInput(k);
        }
    }


    private void handleGameOverInput(int k) {
        if (k == KeyEvent.VK_UP) {
            System.out.println("up!!!");
            gameOverHover = playButtonPos;
        } else if (k == KeyEvent.VK_DOWN) {
            System.out.println("down!!!!");
            gameOverHover = menuButtonPos;
        } else if (k == KeyEvent.VK_ENTER) {
            //replay game (hover same pos as playButton).
            System.out.println("hover" + gameOverHover.getLocation());
            System.out.println("playbutton" + playButtonPos.getLocation());
            if (gameOverHover.getLocation().equals(playButtonPos.getLocation())) {
                restart = true;
                System.out.println("enter");
            }
            //menu button
            if (gameOverHover.getLocation().equals(menuButtonPos.getLocation())) {
                UIManager.switchTo(TheFrame.getFrame(), this, menu);
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
        if (!startGame && (k == KeyEvent.VK_UP || k == KeyEvent.VK_DOWN || k == KeyEvent.VK_LEFT || k == KeyEvent.VK_RIGHT)) {
            startGame = true;
        }

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
    }


    public static void playSound(String filePath, int count, float volume) {
        File file = new File(filePath);
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(volume); // Reduce volume by 10 decibels.
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


    public static List<Food> getAllFood() {
        return allFood;
    }


    public static boolean getStartGame() {
        return startGame;
    }


    public static boolean getPlayerEnemyCollisionState() {
        return playerEnemyCollision;
    }


}
