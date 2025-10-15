package matibbajava.classi;

import java.io.IOException;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import matibbajava.GCP;
import matibbajava.controller.GeneraDBController;
import matibbajava.controller.MainController;
import matibbajava.controller.PrincipaleController;

public class SceneManager {

    public static String guiPath = "/GUI/";


    public static void apriGeneraDB(Stage stage){
        cambioScena(stage, "genera_db.fxml", "GCB - Genera DB", (GeneraDBController controller) -> controller.setStage(stage));
    }


    public static void apriPrincipale(Stage stage) {
        cambioScena(stage, "principale.fxml", "GCB - Verifica codici", (PrincipaleController controller) -> controller.setStage(stage));
    }

    public static void indietro(Stage stage){
        cambioScena(stage, "main.fxml", "GCB", (MainController controller) -> controller.setStage(stage));
    }

    public static <T> void cambioScena(Stage stage, String fxmlFile, String title,
            Consumer<T> controllerConsumer) {
        try {
            FXMLLoader loader = new FXMLLoader(GCP.class.getResource(guiPath + fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

            T controller = loader.getController();
            if (controllerConsumer != null) {
                controllerConsumer.accept(controller);
            }

        } catch (IOException e) {
            GestioneEccezioni.errore("Errore caricamento file: " + fxmlFile, e, false, null);
        }
    }
}
