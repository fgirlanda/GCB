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
import java.util.List;

public class PrincipaleController extends BasicController{
    @FXML
    public TableView<List<Database>> grigliaDB;
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
    @FXML
    public TableColumn<List<Database>, Database> column1, column2, column3;

    private final ObservableList<List<Database>> databases = FXCollections.observableArrayList();



//    public File databaseDir;


//    public void initialize(){
//        // Impostazioni TableView
//        grigliaDB.setItems(databases);
//        grigliaDB.getSelectionModel().setCellSelectionEnabled(true); // selezione di celle
//        grigliaDB.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // selezione multipla
//
//        setupColonna(column1, 0);
//    }
//
//    private void setupColonna(TableColumn<ArrayList<Database>, Database> column, int index){
//        // Colonna che contiene Database come oggetto
//        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
//
//        // CellFactory che crea una Label per ogni Database
//        column.setCellFactory(col -> new TableCell<>() {
//            @Override
//            protected void updateItem(Database item, boolean empty) {
//                super.updateItem(item, empty);
//
//                if (empty || item == null) {
//                    setGraphic(null);
//                } else {
//                    Label label = new Label(item.getNome());
//                    label.setUserData(item); // salva Database come userdata
//                    setGraphic(label);
//
//                    // Gestione click su cella per selezione/deselezione
//                    label.setOnMouseClicked(event -> {
//                        if (event.getButton() == MouseButton.PRIMARY) {
//                            TablePosition<Database, ?> pos =
//                                    new TablePosition<>(grigliaDB, getIndex(), col);
//
//                            TableView.TableViewSelectionModel<Database> sm = grigliaDB.getSelectionModel();
//                            if (sm.getSelectedCells().contains(pos)) {
//                                sm.clearSelection(pos.getRow(), col); // deseleziona
//                            } else {
//                                sm.select(pos.getRow(), col); // seleziona
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }
//    @FXML
//    public void scegliCartella() throws SQLException {
//        DirectoryChooser directoryChooser = new DirectoryChooser();
//        databaseDir = directoryChooser.showDialog(stage);
//        if(databaseDir == null) return;
//        File[] files = databaseDir.listFiles();
//        if(files == null) return;
//        for (File file : files){
//            Database nuovoDB = new Database(file.getName(), databaseDir);
//            databases.add(nuovoDB);
//        }
//    }


    @FXML
    public void initialize() {
        grigliaDB.setItems(databases);

        grigliaDB.getSelectionModel().setCellSelectionEnabled(true); // selezione di celle
        grigliaDB.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); // selezione multipla

        setupColumn(column1, 0);
        setupColumn(column2, 1);
        setupColumn(column3, 2);
    }

    private void setupColumn(TableColumn<List<Database>, Database> column, int index) {
        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(index)));

        column.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Database item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    Label label = new Label(item.getNome());
                    label.setUserData(item);
                    setGraphic(label);

                    label.setOnMouseClicked(event -> {
                        if (event.getButton() == MouseButton.PRIMARY) {
                            TablePosition<List<Database>, ?> pos =
                                    new TablePosition<>(grigliaDB, getIndex(), col);

                            TableView.TableViewSelectionModel<List<Database>> sm = grigliaDB.getSelectionModel();
                            if (sm.getSelectedCells().contains(pos)) {
                                sm.clearSelection(pos.getRow(), col);
                            } else {
                                sm.select(pos.getRow(), col);
                            }
                        }
                    });
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
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".db"));
        if (files == null) return;

        List<Database> allDBs = new ArrayList<>();
        for (File f : files) {
            allDBs.add(new Database(f.getName(), folder));
        }

        // Dividi i Database in righe da 3 elementi
        databases.clear();
        for (int i = 0; i < allDBs.size(); i += 3) {
            List<Database> row = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                if (i + j < allDBs.size())
                    row.add(allDBs.get(i + j));
                else
                    row.add(null); // celle vuote
            }
            databases.add(row);
        }
    }

}
