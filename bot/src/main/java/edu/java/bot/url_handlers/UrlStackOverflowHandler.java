package edu.java.bot.url_handlers;

public class UrlStackOverflowHandler extends UrlHandler {
    public UrlStackOverflowHandler(UrlHandler processor) {
        super(processor);
    }

    @Override
    protected String getValidHostName() {
        return null;
    }
}
