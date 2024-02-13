package edu.java.bot.url_handlers;

import java.net.URI;


public abstract class UrlHandler  {
    protected UrlHandler nextUrlHandler;

    public UrlHandler(UrlHandler processor) {
        this.nextUrlHandler = processor;
    }


    protected abstract String getValidHostName();


    public final boolean checkForValidationURL(URI url) {
        if (url.getHost().equals(getValidHostName())) {
            return true;
        }
        if (this.nextUrlHandler != null) {
            return this.nextUrlHandler.checkForValidationURL(url);
        }
        return false;
    }
}
