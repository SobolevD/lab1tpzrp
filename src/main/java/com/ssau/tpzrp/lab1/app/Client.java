package com.ssau.tpzrp.lab1.app;

import com.ssau.tpzrp.lab1.utils.ImageProcessor;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;

import static com.ssau.tpzrp.lab1.consts.Constants.*;

public class Client {

    private static Socket clientSocket; //сокет для общения
    private static OutputStream socketOutputStream; // Стрим для записи байтов в сокет

    public static void main(String[] args) {
        try {
            try {

                BufferedImage sourceImage = ImageProcessor.getImageFromFile(IMAGE_PATH);
                BufferedImage noisedImage = ImageProcessor.addSaltAndPepper(sourceImage);

                // адрес - локальный хост, порт - SERVER_SOCKET_PORT, такой же как у сервера
                clientSocket = new Socket(HOST_NAME, SERVER_SOCKET_PORT);

                // Отправка изображения на сервер
                socketOutputStream = clientSocket.getOutputStream();
                byte[] imageBytes = ImageProcessor.getImageBytes(noisedImage);
                socketOutputStream.write(imageBytes);

                // Проверка наложения импульсного шума на изображение
                ImageProcessor.saveImageToFile(noisedImage, NOISED_IMAGE_PATH);

            } finally { // Очистка (закрытие потоков)
                System.out.println("Client's work completed...");
                socketOutputStream.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Something wrong during client application execution");
            e.printStackTrace();
        }

    }
}
