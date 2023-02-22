package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Optional;


public class TGBot extends TelegramLongPollingBot {

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            handleMessage(update.getMessage());
        }

    }

    @Override
    public String getBotUsername() {
        return "@Jarold13Bot";
    }

    @Override
    public String getBotToken() {
        return "6255032911:AAGgzYovPJwPJW91IZdM1JRJnfvWeJMJQYg";
    }

    public static void main(String[] args) throws TelegramApiException {
        TGBot bot = new TGBot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }

    private void handleMessage(Message message) {
        String response = botFunctionsInfo();
        if (message.hasText() && message.hasEntities()) {
            String[] words = message.getText().split(" ");
            if (words.length > 1) {
                String command = words[0];

                switch (command) {
                    case "/port":
                        String ip = words[1];
                        response = checkMainPorts(ip);

                        break;
                }

            }
        }

        try {
            execute(SendMessage.builder()
                    .text(response)
                    .chatId(message.getChatId().toString())
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String checkMainPorts(final String ip) {
        StringBuilder result = new StringBuilder();

        result.append(checkPort(ip, 80)).append('\n');
        //result.append(checkPort(ip, 22)).append('\n');
        //result.append(checkPort(ip, 21)).append('\n');
        //result.append(checkPort(ip, 53)).append('\n');
        result.append(checkPort(ip, 443)).append('\n');

        return result.toString();
    }

    private String checkPort(final String ip, final int port) {
        StringBuilder stringBuilder = new StringBuilder("Порт " + port);
        try {
            Socket socket = new Socket(ip, port);
            stringBuilder.append(" доступен");
        } catch (IOException e) {
            stringBuilder.append(" недоступен");
        }

        return stringBuilder.toString();
    }

    private String botFunctionsInfo() {
        return "Бот выполняет следующие функции: \n\n" +
                "/port {ip_address} — Проверка доступности основных портов по заданному интернет-ресурсу.";
    }
}