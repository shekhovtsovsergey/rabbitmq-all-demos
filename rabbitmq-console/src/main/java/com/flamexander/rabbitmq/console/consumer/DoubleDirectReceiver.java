package com.flamexander.rabbitmq.console.consumer;

import com.rabbitmq.client.*;

public class DoubleDirectReceiver {
    private static final String EXCHANGE_NAME = "DoubleDirect";

    public static void main(String[] argv) throws Exception {

        //Создание соединения
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //Обьявление обменника
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        //Создание временной очереди
        String queueName = channel.queueDeclare().getQueue();
        System.out.println("My queue name: " + queueName);

        //Это подписка на несколько тем в сообщении
        //Если придет сообщение с темой php, то кидайте в него это сообщение
        channel.queueBind(queueName, EXCHANGE_NAME, "php");
        //Если придет сообщение с темой java, то кидайте в него это сообщение
        channel.queueBind(queueName, EXCHANGE_NAME, "java");

        System.out.println(" [*] Waiting for messages");

        //создается коллбэк и вешается на очередь
        //На стороне потребителя один поток
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
            System.out.println(Thread.currentThread().getName());
        };

        //создается консьюмер
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });
    }
}
