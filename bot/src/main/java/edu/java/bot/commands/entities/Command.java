package edu.java.bot.commands.entities;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public interface Command {
    String getCommandName();

    String description();

    SendMessage handle(Update update);

    boolean supportsMessageProcessing(Update update);
}
