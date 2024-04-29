package osman.app.telegrambottest4.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import osman.app.telegrambottest4.exception.ServiceException;
import osman.app.telegrambottest4.servise.ExchangeRatesServise;

import javax.sql.rowset.serial.SerialException;
import java.time.LocalDate;

@Component
public class ExchangeRatesBot extends TelegramLongPollingBot {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesBot.class);

    private static final String START = "/start";
    private static final String USD = "/usd";
    private static final String EUR = "/eur";
    private static final String LIRA = "/TLira";
    private static final String UAH = "/uah";
    private static final String UZS = "/uzs";

    private static final String HELP = "/help";

    @Autowired
    private ExchangeRatesServise exchangeRatesServise;



    public ExchangeRatesBot(@Value("${bot.token}") String botToken){
        super(botToken);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()){
        return;
        }
        var message = update.getMessage().getText();
        var chatId = update.getMessage().getChatId();
        switch (message){
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                startCommand(chatId, userName);
            }
            case HELP -> helpCommand(chatId);
            case USD -> usdCommand(chatId);
            case EUR -> eurCommand(chatId);
            case LIRA -> liraCommand(chatId);
            case UAH -> uahCommand(chatId);
            case UZS -> uzsCommand(chatId);

            default -> errorCommand(chatId);

        }



    }

    @Override
    public String getBotUsername() {
        return "testOneOsmanBot";
    }

    private void startCommand(Long chatID, String userName){
        var text= """
                %s Добро пожаловать в бот.
                
                Здесь вы можете узнать официальный курс валют по отношению к рублю на сегодня.
                
                Для этого воспользуйтесь командами:
                /usd - курс доллара
                /eur - курс евро
                /TLira - курс турецкой лиры
                /uah - курс гривны
                /uzs - курс узбекского сума
                
                дополнительные команды:
                /help - получение справки.
                """;
        var formattedText = String.format(text, userName);
        sendMessage(chatID, formattedText);
    }
    private void helpCommand(Long chatId){
        var text= """
                Данный бот переводит следующие валюты на рубли:
                /usd - курс доллара
                /eur - курс евро
                /TLira - курс турецкой лиры
                /uah - курс гривны
                /uzs - курс узбекского сума
                """;
        sendMessage(chatId,text);
    }

    private void errorCommand(Long chatId){
        var text= """
                Ошибка, возвожно вы не правильно указали запрос
                Повторите попытку.
                """;
        sendMessage(chatId,text);
    }

    private void usdCommand(Long chatId){
        String formattedText;
        try{
            var usd = exchangeRatesServise.getUSDExchangeRate();
            var text = "Курс одного доллара на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);

        }catch (ServiceException e){
            LOG.error("Ошибка получения курса", e);
            formattedText = "Не удалось получить курс, попробуйте позже";

        }
        sendMessage(chatId, formattedText);
    }

    private void eurCommand(Long chatId){
        String formattedText;
        try{
            var usd = exchangeRatesServise.getEURExchangeRate();
            var text = "Курс одного евро на %s составляет %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);

        }catch (ServiceException e){
            LOG.error("Ошибка получения курса", e);
            formattedText = "Не удалось получить курс, попробуйте позже";

        }
        sendMessage(chatId, formattedText);
    }

    private void liraCommand(Long chatId){
        String formattedText;
        try{
            var usd = exchangeRatesServise.getLIRAExchangeRate();
            var text = "10 турецких лир на %s составляют %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);

        }catch (ServiceException e){
            LOG.error("Ошибка получения курса", e);
            formattedText = "Не удалось получить курс, попробуйте позже";

        }
        sendMessage(chatId, formattedText);
    }
    private void uahCommand(Long chatId){
        String formattedText;
        try{
            var usd = exchangeRatesServise.getUAHExchangeRate();
            var text = "10 гривень на %s составляют %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);

        }catch (ServiceException e){
            LOG.error("Ошибка получения курса", e);
            formattedText = "Не удалось получить курс, попробуйте позже";

        }
        sendMessage(chatId, formattedText);
    }

    private void uzsCommand(Long chatId){
        String formattedText;
        try{
            var usd = exchangeRatesServise.getUZSExchangeRate();
            var text = "10000 гривень на %s составляют %s рублей";
            formattedText = String.format(text, LocalDate.now(), usd);

        }catch (ServiceException e){
            LOG.error("Ошибка получения курса", e);
            formattedText = "Не удалось получить курс, попробуйте позже";

        }
        sendMessage(chatId, formattedText);
    }

    private void sendMessage(Long chatId, String text){
        var chatIdStr = String.valueOf(chatId);
        var sendMessage = new SendMessage(chatIdStr, text);
        try {
            execute(sendMessage);
        }catch (TelegramApiException e){
            LOG.error("Ошибка отправки сообщения", e);
        }
    }
}
