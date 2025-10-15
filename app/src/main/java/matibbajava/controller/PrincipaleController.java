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
import matibbajava.classi.OutputManager;
import matibbajava.classi.SceneManager;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    public List<Long> listaCodici = new ArrayList<>();

    private List<Database> dbs = new ArrayList<>();
    private List<Database> dbsSelezionati = new ArrayList<>();
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


    @FXML
    public void verifica() throws SQLException {
        dbsSelezionati.clear();
        listaCodici.clear();
        List<Long> listaCodici = creaCodici();
        boolean presente = false;
        HashMap<Long, String> codiciPresenti = new HashMap<Long, String>();
        for(RadioButton r : radios) {
            if(r.isSelected()) {
                dbsSelezionati.add((Database)r.getUserData());
            }
        }
        for(Database db : dbsSelezionati) {
            for(Long codice : listaCodici){
                if(db.checkDB(codice)){
                    presente = true;
                    codiciPresenti.put(codice, db.getNome());
                }
            }
        }
        String output = "";
        if(presente) {
            output = OutputManager.presente(codiciPresenti);
        }else{
//            int spazi = calcolaSpazi()
            output = OutputManager.nonPresente();
        }
        txtOutput.setText(output);
    }


    private List<Long> creaCodici(){
        String codice12 = txtPaese.getText() + txtAzienda.getText() + txtNuovo.getText();
        Long base = Long.parseLong(codice12);
        int num = Integer.parseInt(txtNum.getText());

        for(int i = 0; i < num; i++){
            String codiceBase = String.format("%012d", base + i); // mantiene 12 cifre
            int check = calcolaCheck(codiceBase);
            String codiceCompleto = codiceBase + check;
            listaCodici.add(Long.parseLong(codiceCompleto));
        }

        return listaCodici;
    }

    private int calcolaCheck(String codice12){
        int somma = 0;
        int check = 0;
        for(int i = 0; i < codice12.length(); i++){
            int cifra = Character.getNumericValue(codice12.charAt(i));
            if(i%2 == 0){
                somma += cifra;
            }else{
                somma += 3 * cifra;
            }
        }
        int resto = somma % 10;
        return check = (resto == 0) ? 0 : 10 - resto;
    }
    @FXML
    public void indietro(){
        SceneManager.indietro(stage);
    }

    @FXML
    public void copiaCodici(){

    }

    @FXML
    public void inserisci(){

    }
}
