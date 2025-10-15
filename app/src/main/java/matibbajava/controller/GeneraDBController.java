package matibbajava.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import matibbajava.classi.Database;
import matibbajava.classi.ExcelReader;
import matibbajava.classi.GestioneEccezioni;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GeneraDBController extends BasicController {
    @FXML
    public Label labelDirScelta;
    @FXML
    public TextField txtNomeDB;
    @FXML
    public ProgressBar pBar;
    @FXML
    public Label pLabel;
    public File selectedInputDir;
    public File selectedOutputDir;

    public void initialize() {
        labelDirScelta.setText("Nessuna cartella selezionata");
    }

    @FXML
    public void scegliCartella() {
        DirectoryChooser inputDir = new DirectoryChooser();
        inputDir.setTitle("Seleziona una cartella");

        // (Opzionale) imposta una cartella di partenza
        inputDir.setInitialDirectory(new File(System.getProperty("user.home")));

        // Mostra la finestra di dialogo
        selectedInputDir = inputDir.showDialog(stage);

        if (selectedInputDir != null) {
            labelDirScelta.setText(selectedInputDir.getAbsolutePath());
        }
    }

    @FXML
    public void genera() throws SQLException, IOException {
        String nomeDB = txtNomeDB.getText();
        if (nomeDB.isBlank()) {
            GestioneEccezioni.errore("Nome database non valido", null, false, null);
            return;
        }
        DirectoryChooser outputDir = new DirectoryChooser();
        outputDir.setTitle("Seleziona la cartella dove salvare il database");

        selectedOutputDir = outputDir.showDialog(stage);
        if (selectedOutputDir == null) return;
        Database db = new Database(nomeDB, selectedOutputDir);
//        File fileDB = new File(selectedOutputDir, nomeDB + ".db");
//        String url = "jdbc:sqlite:" + fileDB.getAbsolutePath();
//        Connection conn = DriverManager.getConnection(url);
        ExcelReader exr = new ExcelReader(db, selectedInputDir);
        pBar.progressProperty().bind(exr.progressProperty());
        pLabel.textProperty().bind(exr.messageProperty());
        new Thread(exr).start();


    }
}
