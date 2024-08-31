package com.elasticBeanstalk.service;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArrayList<String> test = new ArrayList<>(3);
        test.add("test");
        System.out.println(test);
        System.out.println(String.join(",",test ));
    }
}
