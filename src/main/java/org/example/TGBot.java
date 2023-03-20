package org.example;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
        boolean isAnswer = true;
        if (message.hasText() && message.hasEntities()) {
            String[] commands = message.getText().split(" ");
            if (commands.length > 1) {
                switch (commands[0]) {
                    case "/port":
                        String ip;
                        try {
                            ip = String.valueOf(InetAddress.getByName(commands[1]).getHostAddress());
                        } catch (UnknownHostException e) {
                            response = "Вы ввели несуществующий адрес к интернет-ресурсу.";
                            break;
                        }
                        if (commands.length == 2) {
                            checkMainPorts(message, ip);
                            isAnswer = false;
                        } else if (commands.length == 3) {
                            response = checkPort(commands, ip);
                        }
                        break;
                    case "/domain":
                        response = checkDomain(commands[1]);
                        break;
                    case "/tracerout":
                        traceRout(message, commands[1]);
                        isAnswer = false;
                        break;
                    case "/vpn":
                        response = checkVPN(commands[1]);
                        break;
                }
            }
        }

        if (isAnswer) {
            try {
                execute(SendMessage.builder()
                        .text(response)
                        .chatId(message.getChatId().toString())
                        .build());
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void traceRout(Message message, final String address) {
        String ip = "";
        boolean isException = false;
        try {
            ip = String.valueOf(InetAddress.getByName(address).getHostAddress());
        } catch (UnknownHostException e) {
            isException = true;
        }

        StringBuilder response = new StringBuilder("Трассировка маршрута к " + ip + "\nс максимальным числом прыжков 30:\n");
        try{
            if (!isException) {
                Process proc = Runtime.getRuntime().exec("tracert " + ip);
                BufferedReader InputStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                SendMessage sendMessage = SendMessage.builder()
                        .text(response.toString())
                        .chatId(message.getChatId().toString()).build();
                int id = execute(sendMessage).getMessageId();
                String str;
                int index = 1;
                int counter = 0;
                while ((str = InputStream.readLine()) != null) {
                    String[] arr = str.split("\\s+");
                    if (arr.length > 8 && arr[1].matches("\\d+")) {
                        if (str.indexOf('*') == -1) {
                            str = index + ".    " + arr[2] + arr[3] + "\t" + arr[4] +
                                    arr[5] + "\t" + arr[6] + arr[7] + "    ";

                            if (arr[8].matches("\\d+\\.\\d+\\.\\d+\\.\\d+"))
                                str += arr[8];
                            else
                                str += arr[9].substring(1, arr[9].length() - 1);
                            index++;
                            counter = 0;
                        } else {
                            str = "";
                            counter++;
                        }

                        if (!str.isEmpty()) {
                            response.append("\n").append(str);
                            execute(EditMessageText.builder().messageId(id)
                                    .text(response.toString())
                                    .chatId(message.getChatId().toString()).build());
                        } else if (counter > 4) {
                            execute(EditMessageText.builder().messageId(id)
                                    .text("Трассировка маршрута к заданному ресурсу через ICMP-протокол невозможна из-за настроек безопасности.")
                                    .chatId(message.getChatId().toString()).build());
                            return;
                        }
                    }
                }
                response.append("\n\n").append("Трассировка завершена.");
                execute(EditMessageText.builder().messageId(id)
                        .text(response.toString())
                        .chatId(message.getChatId().toString()).build());
            } else {
                execute(SendMessage.builder()
                        .text("Вы ввели несуществующий адрес к интернет-ресурсу.")
                        .chatId(message.getChatId().toString())
                        .build());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private final String[] domains = {".by", ".ru", ".com", ".org", ".net", ".info", ".xyz"};
    private String checkDomain(final String name) {
        StringBuilder response = new StringBuilder();
        response.append("Список свободных доменов:").append("\n");
        String temp;
        for (String dom : domains) {
            temp = name.concat(dom);
            if (isAvailableDomain(temp)) {
                response.append("Домен ").append(temp).append(" свободен").append("\n");
            }
        }


        return response.toString();
    }

    private boolean isAvailableDomain(final String domain) {
        try {
            //Whois.main(new String[] {"skytouch.com"});
            InetAddress.getByName(domain);
        } catch (UnknownHostException e) {
            return true;
        }
        return false;
    }

    private String checkPort(final String[] commands, final String ip) {
        StringBuilder result = new StringBuilder();

        boolean isIncorrect = false;
        int port = 0;
        try {
            port = Integer.parseInt(commands[2]);
        } catch (NumberFormatException e) {
            isIncorrect = true;
        }
        if (isIncorrect || port < 0 || port > 65535) {
            return "Вы ввели несуществующий порт.";
        } else {
            result.append(checkPort(ip, port));
        }

        return result.toString();
    }

    private void checkMainPorts(Message message, final String ip) {
        StringBuilder response = new StringBuilder("Список портов:\n\n");

        int[] ports = new int[]{17, 20, 21, 22, 25, 42, 53, 67, 80, 110, 123, 135, 143, 443, 514,
                1701, 3389, 7102, 7105, 8080, 8081, 8082, 8088};

        SendMessage sendMessage = SendMessage.builder()
                .text(response.toString())
                .chatId(message.getChatId().toString()).build();
        try {
            int id = execute(sendMessage).getMessageId();

            for (int port : ports) {
                response.append(checkPort(ip, port)).append('\n');
                execute(EditMessageText.builder().messageId(id)
                        .text(response.toString())
                        .chatId(message.getChatId().toString()).build());
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private String checkVPN(final String address) {
        StringBuilder response = new StringBuilder();

        boolean isCorrect = true;
        try {
            InetAddress.getByName(address);
        } catch (UnknownHostException e) {
            isCorrect = false;
        }

        if (isCorrect) {
            JSONRequest jsonRequest = new JSONRequest();
            String jsonPage = jsonRequest.getJSON(address);
            Gson gson = new Gson();
            Type collectionType = new TypeToken<Collection<GeopingJSON>>() {}.getType();
            ArrayList<GeopingJSON> info = gson.fromJson(jsonPage, collectionType);

            for (GeopingJSON geopingJSON : info) {
                response.append("Сервис ").append(address);
                if (geopingJSON.isIs_alive())
                    response.append(" доступен");
                else
                    response.append(" не доступен");
                response.append(" из ");
                response.append(geopingJSON.getFrom_loc().getCity()).append("\n");
            }
        } else {
            response.append("Вы ввели несуществующий адрес к интернет-ресурсу.");
        }

        return response.toString();
    }

    private String checkPort(final String ip, final int port) {
        StringBuilder stringBuilder = new StringBuilder("Порт " + port);
        int timeout = 1000;
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(ip, port), timeout);
            socket.close();
            stringBuilder.append(" доступен");
        } catch (IOException e) {
            stringBuilder.append(" недоступен");
        }

        return stringBuilder.toString();
    }

    private String botFunctionsInfo() {
        return "Бот выполняет следующие функции: \n\n" +
                "/port {ip_address} — Проверка доступности основных портов по заданному интернет-ресурсу. \n" +
                "/port {ip_address} {port} — Проверка доступности заданного порта по заданному интернет-ресурсу. \n" +
                "/vpn {ip_address} — Проверка доступности ресурса с различных точек мира. \n" +
                "/tracerout {ip_address} — Проверка маршрута до заданного интернет-ресурса. \n" +
                "/domain {domain} — Проверка свободности и доступности доменного имени для ресурса. ";
    }
}