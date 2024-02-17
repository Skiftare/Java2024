package edu.java.bot.utility;

@SuppressWarnings("HideUtilityClassConstructor")
public class UtilityStatusClass {
    public static final String HELP_COMMAND_DESCRIPTION = "Вывести окно с командами";
    public static final String LIST_COMMAND_DESCRIPTION = "Показать список отслеживаемых ссылок";
    public static final String START_COMMAND_DESCRIPTION = "Зарегистрировать пользователя";
    public static final String TRACK_COMMAND_DESCRIPTION = "Начать отслеживание ссылки";
    public static final String UNTRACK_COMMAND_DESCRIPTION = "Прекратить отслеживание ссылки";

    public static final String SUCCESS_TRACK_INFO = "Отслеживание ссылки начато!";
    public static final String UNSUCCESSFUL_TRACK_INFO = "Ссылка невалидна";

    public static final String SUCCESS_UNTRACK_INFO = "Отслеживание ссылки прекращено!";
    public static final String UNSUCCESSFUL_UNTRACK_INFO = "Ссылка невалидна или отсутсвует в отслеживаемых";
    public static final String UNKNOWN_COMMAND_INFO = "Команда неизвестна";
    public static final String WAIT_FOR_URL_TRACK_INFO = "Жду ссылку на отслеживание";
    public static final String WAIT_FOR_URL_UNTRACK_INFO = "Жду ссылку на удаление";

    public static final String UNTRACK_COMMAND_COMMAND = "/untrack";
    public static final String TRACK_COMMAND_COMMAND = "/track";
    public static final String LIST_COMMAND_COMMAND = "/list";
    public static final String HELP_COMMAND_COMMAND = "/help";
    public static final String START_COMMAND_COMMAND = "/start";
    public static final String SUCCESS_START_INFO =
            "Бот будет хранить id диалога только если есть хотя бы 1 отслеживаемая ссылка";
    public static final String SPACE_AS_SPLIT_CHAR = " ";
    public static final String ENDL_CHAR = "\n";
    public static final String NO_LINKS_NOT_TRACKED = "Никаких ссылок не отслеживается";
    public static final String NAME_OF_HANDLE_METHOD_IN_CLASS_OF_BOT_COMMANDS = "handle";
    public static final String SEPARATOR_BETWEEN_COMMAND_AND_DESCRIPTION = " :\t";
    public static final String NAME_OF_COMMAND_METHOD_IN_CLASS_OF_BOT_COMMANDS = "command";
    public static final String NAME_OF_DESCRIPTION_METHOD_IN_CLASS_OF_BOT_COMMANDS = "description";
}
