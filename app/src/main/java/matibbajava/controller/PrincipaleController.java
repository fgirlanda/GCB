package matibbajava.controller;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import matibbajava.classi.Database;
import matibbajava.classi.GestioneEccezioni;
import matibbajava.classi.OutputManager;
import matibbajava.classi.SceneManager;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PrincipaleController extends BasicController {
    @FXML
    public GridPane grigliaDB;
    @FXML
    public Label lblDBDir;
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

    private File dbFolder;
    public List<Long> listaCodici = new ArrayList<>();
    HashMap<Long, List<String>> codiciPresenti = new HashMap<Long, List<String>>();

    private List<Database> dbs = new ArrayList<>();
    private List<Database> dbsSelezionati = new ArrayList<>();
    private List<RadioButton> radios = new ArrayList<>();


    @FXML
    public void initialize() {
        radioSelTutti.setOnAction(e -> {
            if (radios == null) return;
            dbsSelezionati.clear();
            for (RadioButton r : radios) {
                if (radioSelTutti.isSelected()) {
                    Database temp = (Database) r.getUserData();
                    dbsSelezionati.add(temp);
                    r.setSelected(true);
                } else {
                    r.setSelected(false);
                }
            }
        });
    }


    @FXML
    private void scegliCartella() throws SQLException {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Seleziona cartella con file .db");
        dbFolder = chooser.showDialog(grigliaDB.getScene().getWindow());
        if (dbFolder != null && dbFolder.isDirectory()) {
            lblDBDir.setText(dbFolder.getAbsolutePath());
            loadDatabases();
        }
    }


    private void loadDatabases() throws SQLException {
        grigliaDB.getChildren().clear();
        dbs.clear();
        radios.clear();
        dbsSelezionati.clear();
        listaCodici.clear();
        codiciPresenti.clear();
        txtOutput.setText("");
        File[] files = dbFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".db"));
        if (files == null) return;

        for (File f : files) {
            dbs.add(new Database(f.getName(), dbFolder));
        }

        for (int i = 0; i < dbs.size(); i++) {
            Database db = dbs.get(i);
            RadioButton radio = new RadioButton(db.getNome());
            radio.setUserData(db);
            radio.setOnAction(e -> {
               Database dbTemp = (Database) radio.getUserData();
               if(radio.isSelected()) {
                   dbsSelezionati.add(dbTemp);
               }else{
                   dbsSelezionati.remove(dbTemp);
               }
            });
            radios.add(radio);
            grigliaDB.add(radio, i % 3, i / 3);
        }
    }


    @FXML
    public void verifica() throws SQLException {
        if(dbFolder == null || txtNuovo.getText().isBlank() || dbsSelezionati.isEmpty()) {
            GestioneEccezioni.errore("Errore: dati mancanti.\n\n" +
                    "Procedimento corretto:\n\n" +
                    "1) Seleziona una cartella che contenga database (.db)\n\n" +
                    "2) Seleziona uno o più database\n\n" +
                    "3) Inserisci il nuovo codice di partenza e la quantità di etichette desiderate", null, false, null);
            return;
        }
        dbsSelezionati.clear();
        listaCodici.clear();
        codiciPresenti.clear();
        txtOutput.setText("");
        int spazi = 0;
        creaCodici();
        boolean presente = false;

        for (RadioButton r : radios) {
            if (r.isSelected()) {
                dbsSelezionati.add((Database) r.getUserData());
            }
        }
        for (Database db : dbsSelezionati) {
            for (Long codice : listaCodici) {
                if (db.checkDB(codice)) {
                    presente = true;
                    if (codiciPresenti.containsKey(codice)) {
                        codiciPresenti.get(codice).add(db.getNome());
                    } else {
                        List<String> listaDB = new ArrayList<>();
                        listaDB.add(db.getNome());
                        codiciPresenti.put(codice, listaDB);
                    }
                } else {
                    spazi++;
                }
            }
        }
        String output = "";
        if (presente) {
            output = OutputManager.presente(codiciPresenti);
        } else {
            int spaziPerDB = spazi / dbsSelezionati.size();
            output = OutputManager.nonPresente(listaCodici, spazi, spaziPerDB);
        }
        txtOutput.setText(output);
    }


    private void creaCodici() {
        String codice12 = txtPaese.getText() + txtAzienda.getText() + txtNuovo.getText();
        Long base = Long.parseLong(codice12);
        int num = Integer.parseInt(txtNum.getText());

        for (int i = 0; i < num; i++) {
            String codiceTemp = String.format("%012d", base + i);
            int check = calcolaCheck(codiceTemp);
            String codiceCompleto = codiceTemp + check;
            listaCodici.add(Long.parseLong(codiceCompleto));
        }
    }

    private int calcolaCheck(String codice12) {
        int somma = 0;
        int check = 0;
        for (int i = 0; i < codice12.length(); i++) {
            int cifra = Character.getNumericValue(codice12.charAt(i));
            if (i % 2 == 0) {
                somma += cifra;
            } else {
                somma += 3 * cifra;
            }
        }
        int resto = somma % 10;
        return check = (resto == 0) ? 0 : 10 - resto;
    }

    @FXML
    public void indietro() {
        SceneManager.indietro(stage);
    }

    @FXML
    public void copiaCodici() {
        String testo = "";
        for(Long c: listaCodici){
            testo += c+"\n";
        }
        if (!testo.isEmpty()) {
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(testo);
            clipboard.setContent(content);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Eseguito");
            alert.setHeaderText("\tCodici copiati negli appunti");
            alert.showAndWait();
        }
    }

    @FXML
    public void inserisci() throws SQLException {
        if (dbsSelezionati.size() > 1) {
            GestioneEccezioni.errore("Seleziona un singolo database per l'inserimento", null, false, null);
            return;
        }
        Database dbSelezionato = dbsSelezionati.get(0);
        for (Long codice : listaCodici) {
            dbSelezionato.inserisciCodice(codice, null, null);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Eseguito");
        alert.setHeaderText("Codici inseriti nel database "+dbSelezionato.getNome());
        alert.showAndWait();
    }
}


