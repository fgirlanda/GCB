package matibbajava.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import matibbajava.classi.Database;
import matibbajava.classi.ExcelReader;
import matibbajava.classi.GestioneEccezioni;
import matibbajava.classi.SceneManager;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GeneraDBController extends BasicController {
    @FXML
    public Label lblInputDir;
    @FXML
    public Label lblOutputDir;
    @FXML
    public TextField txtNomeDB;
    @FXML
    public ProgressBar pBar;
    @FXML
    public Label pLabel;
    public File selectedInputDir;
    public File selectedOutputDir;

    public void initialize() {
        lblInputDir.setText("Nessuna cartella selezionata");
    }

    @FXML
    public void scegliCartellaInput() {
        DirectoryChooser inputDir = new DirectoryChooser();
        inputDir.setTitle("Seleziona una cartella contenente file Excel da processare");

        // (Opzionale) imposta una cartella di partenza
        inputDir.setInitialDirectory(new File(System.getProperty("user.home")));

        // Mostra la finestra di dialogo
        selectedInputDir = inputDir.showDialog(stage);

        if (selectedInputDir != null) {
            lblInputDir.setText(selectedInputDir.getAbsolutePath());
        }
    }

    @FXML
    public void scegliCartellaOutput() {
        DirectoryChooser outputDir = new DirectoryChooser();
        outputDir.setTitle("Seleziona la cartella in cui salvare il database");

        // (Opzionale) imposta una cartella di partenza
        outputDir.setInitialDirectory(new File(System.getProperty("user.home")));

        // Mostra la finestra di dialogo
        selectedOutputDir = outputDir.showDialog(stage);

        if (selectedInputDir != null) {
            lblOutputDir.setText(selectedOutputDir.getAbsolutePath());
        }
    }

    @FXML
    public void genera() throws SQLException, IOException {
        String nomeDB = txtNomeDB.getText();
        if (nomeDB.isBlank()) {
            GestioneEccezioni.errore("Nome database non valido", null, false, null);
            return;
        }
        if (selectedOutputDir == null) {
            GestioneEccezioni.errore("Nessuna cartella selezionara per l'output.",  null, false, null);
        }

        Database db = new Database(nomeDB, selectedOutputDir);
        ExcelReader exr = new ExcelReader(db, selectedInputDir);
        pBar.progressProperty().bind(exr.progressProperty());
        pLabel.textProperty().bind(exr.messageProperty());
        new Thread(exr).start();
    }

    @FXML
    public void indietro(){
        SceneManager.indietro(stage);
    }
}
