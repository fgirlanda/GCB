package matibbajava.controller;
import javafx.fxml.FXML;
import matibbajava.classi.SceneManager;

public class MainController extends BasicController{
    @FXML
    public void generadb(){
        SceneManager.apriGeneraDB(stage);
    }

    @FXML
    public void verifica(){
        return;
    }
}
