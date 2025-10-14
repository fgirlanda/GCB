package matibbajava.classi;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;
import java.util.function.Consumer;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

public class GestioneEccezioni {

    public static void errore(String header, Exception e, boolean bottone, Consumer<File> azione) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(header);

        if (e != null) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            TextArea textArea = new TextArea(sw.toString());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            alert.getDialogPane().setExpandableContent(textArea);
        }

        ButtonType scegliAltro = new ButtonType("Scegli un altro file");
        if (bottone)
            alert.getButtonTypes().add(scegliAltro);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == scegliAltro) {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Scegli un file CSV");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                azione.accept(file);
            }
        }
    }
}
