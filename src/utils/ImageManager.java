package utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import java.util.HashMap;
import java.util.Objects;

public class ImageManager {

    private static final String pathToSpriteSheet = "/entities/pacman_spritesheet.png";
    private static final int spriteSheetTextureSize = 32;
    private final BufferedImage spriteSheet;


    public ImageManager() {
        this.spriteSheet = loadSpriteSheet();
    }

    private BufferedImage loadSpriteSheet() {
        BufferedImage spriteSheet;
        try {
            spriteSheet = ImageIO.read(Objects.requireNonNull(ImageManager.class.getResource(pathToSpriteSheet)));
        } catch (IOException e) {
            System.out.println("Problem while loading Sprite sheet");
            return null;
        }
        return spriteSheet;
    }

    public BufferedImage resize(BufferedImage image, int width, int height) {
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    public BufferedImage resize(BufferedImage image, int size) {
        return resize(image, size, size);
    }

    private BufferedImage[] getPacmanImages(int width, int height) {
        if (spriteSheet == null) return null;


        BufferedImage[] images = new BufferedImage[]{
                spriteSheet.getSubimage(2 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
                spriteSheet.getSubimage(1 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
                spriteSheet.getSubimage(0 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
                spriteSheet.getSubimage(3 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
                spriteSheet.getSubimage(4 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
                spriteSheet.getSubimage(5 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
                spriteSheet.getSubimage(6 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
                spriteSheet.getSubimage(7 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
                spriteSheet.getSubimage(8 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
                spriteSheet.getSubimage(2 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
                spriteSheet.getSubimage(2 * spriteSheetTextureSize, 0, spriteSheetTextureSize, spriteSheetTextureSize),
        };

        for (int i = 0; i < images.length; i++) {
            images[i] = resize(images[i], width, height);
        }

        return images;
    }
    private BufferedImage[] getPacmanImages(int size) {
        return getPacmanImages(size, size);
    }


    public BufferedImage getPelletImage(int width, int height) {
        BufferedImage pelletImage = spriteSheet.getSubimage(spriteSheetTextureSize, 9 * spriteSheetTextureSize, spriteSheetTextureSize, spriteSheetTextureSize);
        pelletImage = resize(pelletImage, width, height);
        return pelletImage;
    };
    public BufferedImage getPelletImage(int imageSize) {
        return getPelletImage(imageSize, imageSize);
    }


    public BufferedImage getPowerPelletImage(int width, int height){
        BufferedImage pelletImage = spriteSheet.getSubimage(0, 9 * spriteSheetTextureSize, spriteSheetTextureSize, spriteSheetTextureSize);
        pelletImage = this.resize(pelletImage, width, height);
        return pelletImage;
    }
    public BufferedImage getPowerPelletImage(int imageSize) {
        return getPowerPelletImage(imageSize, imageSize);
    }

    private BufferedImage[] getGhostImages(EntityType type, int width, int height){
        if (spriteSheet == null) return null;

        int row = 2;
        int startCol = 0;
        int frames = 8;

        switch (type) {
            case RED_GHOST -> row = 2;
            case PINK_GHOST -> row = 3;
            case BLUE_GHOST -> row = 4;
            case YELLOW_GHOST -> row = 5;
            default -> {
                return null;
            }
        }

        BufferedImage[] images = new BufferedImage[frames];
        for (int i = 0; i < frames; i++) {
            images[i] = spriteSheet.getSubimage((startCol + i) * spriteSheetTextureSize, row*spriteSheetTextureSize, spriteSheetTextureSize, spriteSheetTextureSize);
        }

        for (int i = 0; i < images.length; i++) {
            images[i] = resize(images[i], width, height);
        }

        return images;
    }
    private BufferedImage[] getGhostImages(EntityType type, int size){
        return getGhostImages(type, size, size);
    }


    public static ImageIcon getButtonIcon() {
        return new ImageIcon(Objects.requireNonNull(ImageManager.class.getResource("/ui/button.png")));
    }


    public BufferedImage getWallImage(int width, int height) {
        try {
            return resize(ImageIO.read(Objects.requireNonNull(ImageManager.class.getResource("/board/wall.png"))), width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public BufferedImage getWallImage(int size) {
        return getWallImage(size, size);
    }

    public BufferedImage getBoostImage(EntityType type, int width, int height) {
        int col = 0;
        switch (type) {
            case SCORE_BOOST -> col = 0;
            case SPEED_BOOST -> col = 1;
            case LIVES_BOOST -> col = 2;
        }
        BufferedImage boostImage = spriteSheet.getSubimage(col * spriteSheetTextureSize, 8 * spriteSheetTextureSize, spriteSheetTextureSize, spriteSheetTextureSize);
        boostImage = this.resize(boostImage, width, height);
        return boostImage;
    }
    public BufferedImage getBoostImage(EntityType type, int size) {
        return getBoostImage(type, size);
    }


    public HashMap<MovementDirection, BufferedImage[]> getPacmanFrames(int width, int height){
        BufferedImage[] images = getPacmanImages(width, height);

        HashMap<MovementDirection, BufferedImage[]> pacmanFrames = new HashMap<>();

        pacmanFrames.put(MovementDirection.RIGHT, new BufferedImage[]
                {
                        images[0], images[1], images[2]
                });

        pacmanFrames.put(MovementDirection.LEFT, new BufferedImage[]
                {
                        images[0], images[3], images[4]
                });

        pacmanFrames.put(MovementDirection.UP, new BufferedImage[]
                {
                        images[0], images[5], images[6]
                });

        pacmanFrames.put(MovementDirection.DOWN, new BufferedImage[]
                {
                        images[0], images[7], images[8]
                });

        pacmanFrames.put(MovementDirection.NONE, new BufferedImage[]{
                images[0]
        });

        return pacmanFrames;
    }

    public HashMap<MovementDirection, BufferedImage[]> getPacmanFrames(int size) {
        BufferedImage[] images = getPacmanImages(size);

        HashMap<MovementDirection, BufferedImage[]> pacmanFrames = new HashMap<>();

        pacmanFrames.put(MovementDirection.RIGHT, new BufferedImage[]
                {
                        images[0], images[1], images[2]
                });

        pacmanFrames.put(MovementDirection.LEFT, new BufferedImage[]
                {
                        images[0], images[3], images[4]
                });

        pacmanFrames.put(MovementDirection.UP, new BufferedImage[]
                {
                        images[0], images[5], images[6]
                });

        pacmanFrames.put(MovementDirection.DOWN, new BufferedImage[]
                {
                        images[0], images[7], images[8]
                });

        pacmanFrames.put(MovementDirection.NONE, new BufferedImage[]{
                images[0]
        });

        return pacmanFrames;
    }

    public HashMap<MovementDirection, BufferedImage[]> getGhostFrames(EntityType type, int width, int height) {
        BufferedImage[] images = getGhostImages(type, width, height);
        HashMap<MovementDirection, BufferedImage[]> ghostFrames = new HashMap<>();

        assert images != null;

        ghostFrames.put(MovementDirection.RIGHT, new BufferedImage[]{
                images[0], images[1]
        });

        ghostFrames.put(MovementDirection.LEFT, new BufferedImage[]{
                images[4], images[5]
        });

        ghostFrames.put(MovementDirection.UP, new BufferedImage[]{
                images[6], images[7]
        });

        ghostFrames.put(MovementDirection.DOWN, new BufferedImage[]{
                images[2], images[3]
        });

        ghostFrames.put(MovementDirection.NONE, new BufferedImage[]{
                images[0]
        });

        return ghostFrames;
    }
    public HashMap<MovementDirection, BufferedImage[]> getGhostFrames(EntityType type, int size){
        return getGhostFrames(type, size, size);
    }
}
