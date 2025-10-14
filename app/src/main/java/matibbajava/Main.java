package matibbajava;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        try {
            Application.launch(GCP.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
