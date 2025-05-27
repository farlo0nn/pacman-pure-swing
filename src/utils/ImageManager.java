package utils;

import utils.game.GhostColor;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ImageManager {

    private static final String pathToSpriteSheet = "/entities/pacman_spritesheet.png";
    private static final int spriteSheetTextureSize = 32;


    private static BufferedImage loadSpriteSheet() {
        BufferedImage spriteSheet;
        try {
            spriteSheet = ImageIO.read(Objects.requireNonNull(ImageManager.class.getResource(pathToSpriteSheet)));
        } catch (IOException e) {
            System.out.println("Problem while loading Sprite sheet");
            return null;
        }
        return spriteSheet;
    }

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        Image tmp = image.getScaledInstance(width, height, Image.SCALE_FAST);
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

    public static BufferedImage resize(BufferedImage image, int size) {
        return resize(image, size, size);
    }

    public static BufferedImage[] getPacmanImages(int width, int height) {
        BufferedImage spriteSheet = loadSpriteSheet();
        if (spriteSheet == null) return null;


        BufferedImage[] images = {
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

        for (BufferedImage image : images) {
            resize(image, width, height);
        }

        return images;
    }

    public static BufferedImage[] getPacmanImages(int imageSize){
        return getPacmanImages(imageSize, imageSize);
    }

    public static BufferedImage[] getPacmanImages() {
        return getPacmanImages(32);
    }

    public static BufferedImage getPelletImage(int imageSize) {
        BufferedImage spriteSheet = loadSpriteSheet();
        BufferedImage pelletImage = spriteSheet.getSubimage(spriteSheetTextureSize, 9 * spriteSheetTextureSize, spriteSheetTextureSize, spriteSheetTextureSize);
        resize(pelletImage, imageSize);
        return pelletImage;
    };

    public static BufferedImage getPowerPelletImage(int imageSize) {
        BufferedImage spriteSheet = loadSpriteSheet();
        BufferedImage pelletImage = spriteSheet.getSubimage(0, 9 * spriteSheetTextureSize, spriteSheetTextureSize, spriteSheetTextureSize);
        resize(pelletImage, imageSize);
        return pelletImage;
    };

    public static BufferedImage[] getGhostImages(GhostColor color, int imageSize){
        BufferedImage spriteSheet = loadSpriteSheet();
        if (spriteSheet == null) return null;

        int row = 2;
        int startCol = 0;
        int frames = 8;

        switch (color) {
            case RED -> row = 2;
            case PINK -> row = 3;
            case BLUE -> row = 4;
            case YELLOW -> row = 5;
        }

        BufferedImage[] images = new BufferedImage[frames];
        for (int i = 0; i < frames; i++) {
            images[i] = spriteSheet.getSubimage((startCol + i) * spriteSheetTextureSize, row*spriteSheetTextureSize, spriteSheetTextureSize, spriteSheetTextureSize);
        }

        for (BufferedImage image : images) {
            resize(image, imageSize, imageSize);
        }

        return images;
    }

    public static ImageIcon getButtonIcon() {
        ImageIcon buttonIcon = new ImageIcon(Objects.requireNonNull(ImageManager.class.getResource("/ui/button.png")));
        return buttonIcon;
    }
}
