package com.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class Handler implements RequestHandler<String, String> {
    @Override
    public String handleRequest(String s, Context context) {
        return "";
    }
}
