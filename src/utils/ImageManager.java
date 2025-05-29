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

    public static Image resize(Image image, int width, int height) {
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_FAST);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }

    public BufferedImage resize(BufferedImage image, int size) {
        return resize(image, size, size);
    }

    private BufferedImage[] getPacmanImages(int size) {
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
            images[i] = resize(images[i], size);
        }

        return images;
    }

    public BufferedImage getPelletImage(int imageSize) {
        BufferedImage pelletImage = spriteSheet.getSubimage(spriteSheetTextureSize, 9 * spriteSheetTextureSize, spriteSheetTextureSize, spriteSheetTextureSize);
        pelletImage = resize(pelletImage, imageSize);
        return pelletImage;
    };

    public BufferedImage getPowerPelletImage(int imageSize) {
        BufferedImage pelletImage = spriteSheet.getSubimage(0, 9 * spriteSheetTextureSize, spriteSheetTextureSize, spriteSheetTextureSize);
        pelletImage = this.resize(pelletImage, imageSize);
        return pelletImage;
    };

    private BufferedImage[] getGhostImages(EntityType type, int size){
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
            images[i] = resize(images[i], size);
        }

        return images;
    }


    public static ImageIcon getButtonIcon() {
        return new ImageIcon(Objects.requireNonNull(ImageManager.class.getResource("/ui/button.png")));
    }


    public Image getWallImage(int size) {
        try {
            return resize(ImageIO.read(Objects.requireNonNull(ImageManager.class.getResource("/board/wall.png"))), size);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public HashMap<MovementDirection, Image[]> getPacmanFrames(int size) {
        BufferedImage[] images = getPacmanImages(size);

        HashMap<MovementDirection, Image[]> pacmanFrames = new HashMap<>();

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

    public HashMap<MovementDirection, Image[]> getGhostFrames(EntityType type, int size) {
        BufferedImage[] images = getGhostImages(type, size);
        HashMap<MovementDirection, Image[]> ghostFrames = new HashMap<>();

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
}
