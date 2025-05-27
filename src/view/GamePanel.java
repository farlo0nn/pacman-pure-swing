package view;

import model.entity.*;
import utils.*;
import utils.game.*;
import utils.game.Tile;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.function.Consumer;

public class GamePanel extends JPanel implements ActionListener {

    private final Consumer<GameStatus> statusConsumer;

    private final int width = 28 * 24;
    private final int height = 31 * 24;

    private int tileSize;
    private HashSet<Block> walls;
    private RedGhost redGhost;
    private ArrayList<RandomlyMovingGhost> ghosts;
    private HashSet<Block> pellets;
    private HashSet<Block> powerPellets;
    private ArrayList<Portal> portals;
    private Pacman player;
    private Thread gameThread;
    private int score;
    private int lives;
    private int pelletsLeft;
    private HashMap<Tile, ArrayList<MovementDirection>> turnPoints;

    private final Image wallImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/board/wall.png"))).getImage();


    private char[][] map;
    private GameTimer gameTimer;

    public GamePanel(BoardSizes boardSize, Consumer<GameStatus> statusConsumer) {
        setFocusable(true);
        setBackground(Color.BLACK);
        this.statusConsumer = statusConsumer;
        setPreferredSize(new Dimension(UIConstants.WINDOW_WIDTH, UIConstants.GAME_PANEL_HEIGHT));
        initSize(boardSize);
        initGame();
        initInput();
    }


    private void initSize(BoardSizes boardSize) {
        switch (boardSize) {
            case SMALL -> {
                tileSize = getPreferredSize().height/31;
                map = BoardLoader.loadBoard("/board/boards/small.csv");
                break;
            }
            case MEDIUM -> {
                tileSize = width/42;
                map = BoardLoader.loadBoard("/board/boards/medium.csv");
                break;
            }
            case LARGE -> {
                tileSize = width/56;
                map = BoardLoader.loadBoard("/board/boards/large.csv");
                break;
            }
        }
    }

    private void initGame() {

        lives = 1;
        score = 0;

        // 60 fps

        gameTimer = new GameTimer(60, () -> {
            updateGame();
            repaint();
        });
        gameThread = new Thread(gameTimer);

        nextRound();

        gameThread.start();
    }

    public void nextRound() {
        ghosts = new ArrayList<RandomlyMovingGhost>();
        pellets = new HashSet<>();
        powerPellets = new HashSet<>();
        portals = new ArrayList<>();
        walls = new HashSet<>();

        BufferedImage pelletImage = ImageManager.getPelletImage(tileSize);
        BufferedImage powerPelletImage = ImageManager.getPowerPelletImage(tileSize);
        turnPoints = GhostPathBuilder.getTurnPoints(map);

        pelletsLeft = 0;

        for (int rowId=0; rowId<map.length; rowId++) {
            char[] row = map[rowId];
            for (int colId=0; colId<map[0].length; colId++) {
                char value = row[colId];
                int x = colId*tileSize;
                int y = rowId*tileSize;
                if (value == 'X') {
                    walls.add(new Block(wallImage, x, y, tileSize));
                } else if (value == 'P') {
                    player = new Pacman(x,y,tileSize);
                } else if (value == '.') {
                    pellets.add(new Block(pelletImage, x, y, tileSize));
                    pelletsLeft++;
                } else if (value == 'o') {
                    powerPellets.add(new Block(powerPelletImage, x, y, tileSize));
                    pelletsLeft++;
                } else if (value == 'r') {
                    redGhost = new RedGhost(x, y, tileSize);
                } else if (value == 'p') {
                    addGhost(colId, rowId, GhostColor.PINK);
                } else if (value == 'b') {
                    addGhost(colId, rowId, GhostColor.BLUE);
                } else if (value == 'y') {
                    addGhost(colId, rowId, GhostColor.YELLOW);
                } else if (value == 'O') {
                    portals.add(new Portal(x, y, 1, tileSize));
                }
            }
        }

        if (portals.size() % 2 != 0){
            //TODO LOG

        }
        else {
            for (int i = 0; i < portals.size() - 1; i+=2) {
                portals.get(i).setOther(portals.get(i+1));
                portals.get(i+1).setOther(portals.get(i));
            }
        }
    }

    public void addGhost(int colId, int rowId, GhostColor color ){
        Tile tile = new Tile(colId, rowId);
        ArrayList<Tile> path = GhostPathBuilder.getPath(map, tile, GhostPathBuilder.getClosestTurnPoint(new ArrayList<Tile>(turnPoints.keySet()), tile));
        ghosts.add(new RandomlyMovingGhost(colId*tileSize, rowId*tileSize, tileSize, color, path));
    }

    @Override
    public void addNotify() {
        super.addNotify();
        requestFocusInWindow();
    }


    private void initInput() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT ->
                    player.setRequestedDirection(MovementDirection.RIGHT);
                case KeyEvent.VK_LEFT ->
                    player.setRequestedDirection(MovementDirection.LEFT);
                case KeyEvent.VK_UP ->
                    player.setRequestedDirection(MovementDirection.UP);
                case KeyEvent.VK_DOWN ->
                    player.setRequestedDirection(MovementDirection.DOWN);
                case KeyEvent.VK_ESCAPE -> stopGame();
            }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawWalls(g);
        drawPellets(g);
        drawPowerPellets(g);
        drawPlayer(g);
        drawGhosts(g);
        drawHUD(g);
    }

    private void drawPellets(Graphics g) {
        for (Block pellet: pellets) {
            pellet.render(g);
        }
    }

    private void drawGhosts(Graphics g) {
        for (RandomlyMovingGhost ghost : ghosts) {
            ghost.render(g);
        }
        redGhost.render(g);
    }

    private void drawPowerPellets(Graphics g) {
        for (Block powerPellet: powerPellets) {
            powerPellet.render(g);
        }
    }

    private void drawWalls(Graphics g) {
        for (Block wall : walls) {
            wall.render(g);
        }
    }

    private void drawPlayer(Graphics g) {
        player.render(g);
    }

    private void drawHUD(Graphics g) {
        int fontSize = 16;
        g.setColor(Color.WHITE);
        g.setFont(new Font("Sans", Font.BOLD, fontSize));
        g.drawString("Score: " + score, 100  , UIConstants.GAME_PANEL_HEIGHT + UIConstants.HUD_HEIGHT / 2 - fontSize);
        g.drawString("Lives: " + lives, 200, UIConstants.GAME_PANEL_HEIGHT + UIConstants.HUD_HEIGHT / 2 - fontSize);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    public void wallCollisions() {
        boolean collisionInRequestedDirection = false;

        for (Block wall : walls) {

            if (UIUtils.collides(redGhost.getBounds(), wall.getBounds())) {
                redGhost.stepBack();
            }

            if (UIUtils.collides(player.getBounds(), wall.getBounds())) {
                player.stepBack();
                player.stop();
            }

            else if (UIUtils.collides(player.getFutureBounds(), wall.getBounds())) {
                collisionInRequestedDirection = true;
            }

            for (RandomlyMovingGhost ghost : ghosts) {
                if (UIUtils.collides(ghost.getBounds(), wall.getBounds())) {
                    ghost.stepBack();
                }
            }
        }

        if (!collisionInRequestedDirection) {
            player.changeDirection();
        }
    }

    private void portalCollisions() {
        for (Portal portal : portals) {
            if (UIUtils.collides(player.getBounds(), portal.getBounds())) {
                player.teleport(portal.getOther().getXCoord(), portal.getOther().getYCoord());
            }
            for (RandomlyMovingGhost ghost : ghosts) {
                if (UIUtils.collides(ghost.getBounds(), portal.getBounds())) {
                    ghost.changeDirection(MovementDirection.opposite(ghost.getDirection()));
                    ghost.lastTurnTile = new Tile(ghost.getTile().x, ghost.getTile().y);
                }
            }
        }
    }

    private void updateGhosts() {

        //randomly moving ghosts update
        for (RandomlyMovingGhost ghost : ghosts) {
            if (!ghost.exitingBox() && !ghost.getTile().equals(ghost.lastTurnTile) && turnPoints.containsKey(ghost.getTile())){
                ghost.changeDirection(turnPoints.get(ghost.getTile()));
                ghost.lastTurnTile = new Tile(ghost.getTile().x, ghost.getTile().y);
            }

            ghost.update();

        }

        // red ghost update
        if (redGhost.reachedTarget()) {
            ArrayList<Tile> path = GhostPathBuilder.getPath(
                    map,
                    redGhost.getTile(),
                    player.getTile()
            );

            if (!path.isEmpty()){
                Tile target = path.getFirst();
                redGhost.setTarget(target);
            }
        }

        redGhost.update();
    }

    private void updatePellets() {

        ArrayList<Block> pelletsToRemove = new ArrayList<>();
        for (Block pellet : pellets) {
            if (UIUtils.collides(player.getBounds(), pellet.getBounds())){
                pelletsToRemove.add(pellet);
                score+=10;
            }
        }

        pelletsToRemove.forEach(pellets::remove);
        pelletsLeft-=pelletsToRemove.size();

        ArrayList<Block> powerPelletsToRemove = new ArrayList<>();
        for (Block powerPellet : powerPellets) {
            if (UIUtils.collides(player.getBounds(), powerPellet.getBounds())){
                powerPelletsToRemove.add(powerPellet);
                score+=50;
            }
        }

        powerPelletsToRemove.forEach(powerPellets::remove);
        pelletsLeft-=powerPelletsToRemove.size();
    }

    private void updateGame() {

        if (lives == 0) {
            stopGame();
            return;
        }

        if (pelletsLeft == 0) {
            nextRound();
        }

        updateGhosts();

        wallCollisions();
        portalCollisions();

        updatePellets();
        player.update();

        for (RandomlyMovingGhost ghost : ghosts) {
            if (UIUtils.collides(player.getBounds(), ghost.getBounds())) {
                // TODO
                resetAfterCollision();
            }
        }

        if (UIUtils.collides(player.getBounds(), redGhost.getBounds())) {
            // TODO
            resetAfterCollision();
        }
    }

    public void resetAfterCollision(){
        player.reset();
        ghosts.forEach(Entity::reset);
        redGhost.reset();
        lives--;

    }

    public void stopGame() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (gameThread != null && gameThread.isAlive() && Thread.currentThread() != gameThread) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                // TODO - add logging logic
                e.printStackTrace();
            }
        }
        this.statusConsumer.accept(GameStatus.GAME_OVER);
    }
}