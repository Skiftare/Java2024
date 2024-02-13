package edu.java.bot.current_good_thing;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
public class MessageManager {

    private final UserRepository userRepository;

    @Autowired
    public MessageManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void processUpdate(Update update) {
        Long chatId = update.message().chat().id();
        String messageText = update.message().text();

        if (messageText.equals("/help")) {
            sendHelpMessage(chatId);
        } else {
            DialogManager dialogManager = new DialogManager(userRepository);
            dialogManager.processUpdate(update);
        }
    }

    private void sendHelpMessage(Long chatId) {
        // Send a help message to the user
        SendMessage request = new SendMessage(chatId, "This is the help message.");
        try{
            execute(request);
        } catch (Exception e){
            e.getMessage();
        }
    }
    private void execute(SendMessage request)
        throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
        InvocationTargetException {
        Class<?> botClass = Class.forName("src/main/java/edu/java/bot/current_good_thing/Bot");
        Object botObject = botClass.newInstance();

        // Получаем метод sendMessageToUser
        Method sendMessageToUserMethod = botClass.getDeclaredMethod("sendMessageToUser", SendMessage.class);
        sendMessageToUserMethod.invoke(botObject, request);

    }
}

