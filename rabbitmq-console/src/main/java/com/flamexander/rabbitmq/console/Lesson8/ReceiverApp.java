package com.flamexander.rabbitmq.console.Lesson8;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class ReceiverApp {

    private final static String QUEUE_NAME = "hello";
    private static final String EXCHANGE_NAME = "DoubleDirect";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        Scanner sc = new Scanner(System.in);
        System.out.println("Введите тему");
        String topic = sc.nextLine();
        String[] parts = topic.split(" ");
        String part2 = parts[1];
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, part2);

        System.out.println(" [*] Waiting for messages");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(Thread.currentThread().getName() + " [x] Received '" + message + "'");
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }

}
