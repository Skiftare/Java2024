package edu.java.bot.utility;

public class UtilityStatusClass {
    public static final String HELP_COMMAND_DESCRIPTION = "Вывести окно с командами";
    public static final String LIST_COMMAND_DESCRIPTION = "Показать список отслеживаемых ссылок";
    public static final String START_COMMAND_DESCRIPTION = "Зарегистрировать пользователя";
    public static final String TRACK_COMMAND_DESCRIPTION = "Начать отслеживание ссылки";
    public static final String UNTRACK_COMMAND_DESCRIPTION = "Прекратить отслеживание ссылки";

    public static final String SUCCESS_TRACK_INFO = "Отслеживание ссылки начато!";
    public static final String UNSUCCESSFUL_TRACK_INFO = "Ссылка невалидна";

    public static final String SUCCESS_UNTRACK_INFO = "Отслеживание ссылки прекращено!";
    public static final String UNSUCCESSFUL_UNTRACK_INFO ="Ссылка невалидна или отсутсвует в отслеживаемых";
    public static final String UNKNOWN_COMMAND_INFO = "Команда неизвестна";
    public static final String WAIT_FOR_URL_TRACK_INFO = "Жду ссылку на отслеживание";
    public static final String WAIT_FOR_URL_UNTRACK_INFO = "Жду ссылку на удаление";
}
