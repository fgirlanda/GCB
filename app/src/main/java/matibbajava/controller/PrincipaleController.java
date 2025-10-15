package matibbajava.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import matibbajava.classi.Database;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PrincipaleController extends BasicController{
    @FXML
    public GridPane grigliaDB;
    @FXML
    public RadioButton radioSelTutti;
    @FXML
    public TextField txtPaese;
    @FXML
    public TextField txtAzienda;
    @FXML
    public TextField txtNuovo;
    @FXML
    public TextField txtNum;
    @FXML
    public Text txtOutput;

    private List<Database> dbs = new ArrayList<>();
    private List<RadioButton> radios = new ArrayList<>();


    @FXML
    public void initialize(){
        radioSelTutti.setOnAction(e -> {
            if(radios == null) return;
            for(RadioButton r : radios) {
                if(radioSelTutti.isSelected()) {
                    r.setSelected(true);
                }else{
                    r.setSelected(false);
                }
            }
        });
    }
    @FXML
    private void scegliCartella() throws SQLException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Seleziona cartella con file .db");
        File folder = chooser.showDialog(grigliaDB.getScene().getWindow());
        if (folder != null && folder.isDirectory()) {
            loadDatabases(folder);
        }
    }

    private void loadDatabases(File folder) throws SQLException {
        grigliaDB.getChildren().clear();
        dbs.clear();
        radios.clear();
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".db"));
        if (files == null) return;

        for (File f : files) {
            dbs.add(new Database(f.getName(), folder));
        }

        for (int i = 0; i < dbs.size(); i++) {
            Database db = dbs.get(i);
            RadioButton radio = new RadioButton(db.getNome());
            radio.setUserData(db);
            radios.add(radio);
            grigliaDB.add(radio, i%3, i/3);
        }
    }

}
