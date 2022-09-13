package com.ssau.tpzrp.lab1.app;

import com.ssau.tpzrp.lab1.utils.ImageProcessor;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import static com.ssau.tpzrp.lab1.consts.Constants.FILTERED_IMAGE_PATH;
import static com.ssau.tpzrp.lab1.consts.Constants.SERVER_SOCKET_PORT;

public class Server {
    private static ServerSocket server; // серверсокет
    private static InputStream socketInputStream; // поток чтения из сокета

    public static void main(String[] args) {
        try {
            try {
                server = new ServerSocket(SERVER_SOCKET_PORT); // серверсокет прослушивает порт SERVER_SOCKET_PORT
                System.out.println("Server started!");

                //сокет для общения
                Socket clientSocket = server.accept(); // Запуск серверного сокета

                try {
                    // Принимаем сообщения с сокета
                    socketInputStream = clientSocket.getInputStream();

                    byte[] bytes = socketInputStream.readAllBytes();
                    BufferedImage receivedImage = ImageProcessor.getImageFromBytes(bytes);
                    BufferedImage filtered = ImageProcessor.doMedianFilter(receivedImage);

                    ImageProcessor.saveImageToFile(filtered, FILTERED_IMAGE_PATH);

                } finally { // Очистка. Закрытие потоков
                    clientSocket.close();
                    socketInputStream.close();
                }
            } finally {
                System.out.println("Server shutdown...");
                server.close();
            }
        } catch (IOException e) {
            System.out.println("Something wrong during server application execution");
            e.printStackTrace();
        }
    }
}
