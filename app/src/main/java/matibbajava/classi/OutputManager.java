package matibbajava.classi;

import java.util.HashMap;
import java.util.List;

public class OutputManager {

    public static String presente(HashMap<Long, List<String>> codiciPresenti){
        String output = "";
        if(codiciPresenti.size() == 1){
            output += "Il codice richiesto è già presente:\n\n";
        }else{
            output += "I codici richiesti sono già presenti:\n\n";
        }
        output += "Codice\t\t\t\t" + "Database\n\n";
        for(Long codice : codiciPresenti.keySet()){
            output += codice + "\t\t" + codiciPresenti.get(codice) + "\n";
        }
        return output;
    }

    public static String nonPresente(List<Long> codiciRichiesti, int spazi){
        String output = "Codice/i non presente/i.\n\n" +
                "Spazi disponibili: "+spazi+"\n\n" +
                "Codici disponibili generati: \n\n";
        for(int i = 0; i < spazi;i++){
            output += codiciRichiesti.get(i) + "\n";
        }
        return output;
    }
}
