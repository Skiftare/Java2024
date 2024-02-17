package edu.java.bot.memory;

@SuppressWarnings("HideUtilityClassConstructor")
public class WeakLinkChecker {
    public static boolean checkLinkWithoutConnecting(String url) {
        return (url.startsWith("https") && (url.contains("github") || url.contains("stackoverflow")));
    }
}
