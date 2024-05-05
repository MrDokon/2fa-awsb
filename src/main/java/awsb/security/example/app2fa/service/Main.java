package awsb.security.example.app2fa.service;

import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        String type =  null;
        Optional.ofNullable(type).ifPresent(e -> System.out.println("null jest w srodku"));
    }
}
