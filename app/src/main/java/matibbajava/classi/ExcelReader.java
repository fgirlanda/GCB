package matibbajava.classi;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

public class ExcelReader extends Task<Void> {
    private final Connection conn;
    private final File root;


    public ExcelReader(Database db, File root) {
        this.conn = db.getConn();
        this.root = root;
    }

    @Override
    public Void call() throws SQLException, IOException {
        creaTabella();
        percorriCartella(root);
        updateProgress(1, 1);
        updateMessage("Completato");
        return null;
    }


    private void creaTabella() throws SQLException {
        String cQuery = """
                    CREATE TABLE IF NOT EXISTS codici_barre (
                    nome_cartella TEXT,
                    nome_file TEXT,
                    codice_a_barre DECIMAL(13) PRIMARY KEY
                )""";
        try (Statement st = conn.createStatement()) {
            st.execute(cQuery);
        }
    }


    private void percorriCartella(File current) throws IOException, SQLException {
        File[] subFiles = current.listFiles();
        if (subFiles == null) return;
        String nomeCartella = current.getName();
        int tot = subFiles.length;
        int count = 0;
        for (File f : subFiles) {
            if (f.isDirectory()) {
                percorriCartella(f);
            } else if (f.getName().endsWith(".xlsx") || f.getName().endsWith(".xls")) {
                try {
                    processaExcel(f, nomeCartella);
                } catch (Exception e) {
                    Platform.runLater(() ->
                            GestioneEccezioni.errore("Errore nella lettura del file: " + f.getName(), e, false, null));
                }
                count++;
                updateProgress(count, tot);
                updateMessage("Processato " + f.getName());
            }
        }
    }


    private void processaExcel(File fileExcel, String nomeCartella) throws IOException, SQLException {
        FileInputStream fis = new FileInputStream(fileExcel);
        Workbook workbook = fileExcel.getName().endsWith(".xlsx")
                ? new XSSFWorkbook(fis)
                : new HSSFWorkbook(fis);

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);

            for (Row row : sheet) {
                for (Cell cell : row) {
                    Long valore = getValore(cell);

                    if (valore != null && valore > 830000000) {
                        inserisciCodice(nomeCartella, fileExcel.getName(), valore);
                    }
                }
            }
        }
    }


    private Long getValore(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> null;
            case NUMERIC -> {
                yield ((Double) cell.getNumericCellValue()).longValue();
            }
            case FORMULA -> null;
            case BOOLEAN -> null;
            case BLANK, _NONE, ERROR -> null;
        };
    }


    private void inserisciCodice(String nomeCartella, String fileExcel, Long codice) throws SQLException {
        String query = "INSERT INTO codici_barre (nome_cartella, nome_file, codice_a_barre) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nomeCartella);
            ps.setString(2, fileExcel);
            ps.setLong(3, codice);
            ps.executeUpdate();
        }
    }
}
