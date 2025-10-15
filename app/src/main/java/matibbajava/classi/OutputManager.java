package matibbajava.classi;

import java.util.HashMap;

public class OutputManager {

    public static String dbPresente(HashMap<Long, String> codici_presenti){
        String output = "";
        if(codici_presenti.size() == 1){
            output += "Il codice richiesto è già presente:\n\n";
        }else{
            output += "I codici richiesti sono già presenti:\n\n";
        }
        output += "Codice\t\t" + "File\n\n";
        for(Long codice : codici_presenti.keySet()){
            output += codice + "\t\t" + codici_presenti.get(codice) + "\n";
        }
        return output;
    }
}
