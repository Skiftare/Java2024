package edu.java.bot.url_handlers;

public class UrlGithubHandler extends UrlHandler{
    public UrlGithubHandler(UrlHandler processor) {
        super(processor);
    }

    @Override
    protected String getValidHostName() {
        return null;
    }
}
