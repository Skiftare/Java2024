package edu.java.bot.memory;

@SuppressWarnings("HideUtilityClassConstructor")
public class WeakLinkChecker {
    public static boolean checkLinkWithoutConnecting(String url) {
        return (url.startsWith("https") && (url.contains("github") || url.contains("stackoverflow")));
    }
    //TODO:Суть в чём
    // a. Все данные, которые пользователь присылает - хотят нам навредить.
    // b. Все данные, которые мы отправляем пользователю - будут использованы против нас.
    // Поэтому
    //    1. Надо переработать чекер и сделать его более жестким. а то текущий никуда не годится.
    //      (чтобы он проверял, а можем ли мы сейчас вообще обрабатывать такие ссылки)
    //    2. Надо сделать так, чтобы при подключении делалась пробная заявка.
    //      (а реальна ли ссылка, условная регулярка не даёт нам 100% гарантию)
}
