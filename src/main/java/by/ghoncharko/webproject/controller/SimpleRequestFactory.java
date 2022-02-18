package by.ghoncharko.webproject.controller;


import by.ghoncharko.webproject.command.CommandRequest;
import by.ghoncharko.webproject.command.CommandResponse;

import javax.servlet.http.HttpServletRequest;

public class SimpleRequestFactory implements RequestFactory {

    @Override
    public CommandRequest createRequest(HttpServletRequest request) {
        return new WrappingCommandRequest(request);
    }

    @Override
    public CommandResponse createForwardResponse(String path) {
        return new PlainCommandResponse(path);
    }

    @Override
    public CommandResponse createRedirectResponse(String path) {
        return new PlainCommandResponse(true,path);
    }
}
