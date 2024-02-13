package edu.java.bot.current_good_thing;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import edu.java.bot.configuration.ApplicationConfig;
import java.util.List;
import java.util.logging.LogManager;
import jakarta.annotation.PostConstruct;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(ApplicationConfig.class)
public class Bot implements BotInterface {
    //private final static Logger LOGGER = (Logger) LogManager.getLogManager();
    private TelegramBot bot;

    @Autowired
    private ApplicationConfig config;


    @PostConstruct
    public void setBot(){
        bot = new TelegramBot(config.telegramToken());
        setUpdatesManager();

    }

    private void setUpdatesManager() {
        bot.setUpdatesListener(UpdatesManager::createUpdatesManager);
    }

    @Override
    public <T extends BaseRequest<T, R>, R extends BaseResponse> void execute(BaseRequest<T, R> request) {

    }

    @Override
    public int process(List<Update> updates) {
        return 0;
    }

    @Override
    public void start() {

    }

    @Override
    public void close() {
        bot.shutdown();

    }
}
