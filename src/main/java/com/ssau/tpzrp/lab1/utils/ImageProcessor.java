package com.ssau.tpzrp.lab1.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class ImageProcessor {

    public static BufferedImage getImageFromFile(String imagePath) {
        try {
            return ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage addSaltAndPepper(BufferedImage resourceImage) {

        int width = resourceImage.getWidth();
        int height = resourceImage.getHeight();

        for(int i=0; i < height; i++){

            for(int j=0; j<width; j++){

                Color c = new Color(resourceImage.getRGB(j, i));
                int red = (int)(c.getRed() * 0.299);
                int green = (int)(c.getGreen() * 0.587);
                int blue = (int)(c.getBlue() * 0.114);
                int rgb = range(red+green+blue,8);
                Color newColor = new Color(rgb,rgb,rgb);

                resourceImage.setRGB(j,i,newColor.getRGB());
            }
        }
        return resourceImage;
    }

    public static void saveImageToFile(BufferedImage resourceImage, String imagePath) {
        try{
            File f = new File(imagePath);
            String fileExtension = imagePath.split("\\.")[1];
            ImageIO.write(resourceImage, fileExtension, f);
        } catch(IOException e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage doMedianFilter(BufferedImage image) {

        Color[] pixel=new Color[9];
        int[] R = new int[9];
        int[] B = new int[9];
        int[] G = new int[9];

        for(int i=1;i<image.getWidth()-1;i++)
            for(int j=1;j<image.getHeight()-1;j++)
            {
                pixel[0]=new Color(image.getRGB(i-1,j-1));
                pixel[1]=new Color(image.getRGB(i-1,j));
                pixel[2]=new Color(image.getRGB(i-1,j+1));
                pixel[3]=new Color(image.getRGB(i,j+1));
                pixel[4]=new Color(image.getRGB(i+1,j+1));
                pixel[5]=new Color(image.getRGB(i+1,j));
                pixel[6]=new Color(image.getRGB(i+1,j-1));
                pixel[7]=new Color(image.getRGB(i,j-1));
                pixel[8]=new Color(image.getRGB(i,j));
                for(int k=0;k<9;k++){
                    R[k]=pixel[k].getRed();
                    B[k]=pixel[k].getBlue();
                    G[k]=pixel[k].getGreen();
                }
                Arrays.sort(R);
                Arrays.sort(G);
                Arrays.sort(B);
                image.setRGB(i,j,new Color(R[4],B[4],G[4]).getRGB());
            }
        return image;
    }

    public static byte[] getImageBytes(BufferedImage image) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(image, "jpeg", os);
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage getImageFromBytes(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static int range(int n, double prob) {
        double res = ((100 * prob)/10);

        int[]array = new int[(int)res];
        array[0]= 1;
        array[1]=255;

        for (int i = 2 ; i <= res - 2; i++)
        {
            array[i] = n;
        }
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }
}
