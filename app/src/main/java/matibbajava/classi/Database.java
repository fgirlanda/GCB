package matibbajava.classi;

import java.io.File;
import java.sql.*;

public class Database {
    File dir;
    String nome;
    Connection conn;

    public Database(String nome, File dir) throws SQLException {
        if(!nome.endsWith("db")) this.nome = nome+".db";
        else this.nome = nome;
        this.dir = dir;
        this.conn = connetti();
    }

    private Connection connetti() throws SQLException {
        File fileDB = new File(dir, nome);
        String url = "jdbc:sqlite:" + fileDB.getAbsolutePath();
        return DriverManager.getConnection(url);
    }

    public void disconnetti(){
        try{
            conn.close();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public Connection getConn(){
        return conn;
    }

    public String getNome(){
        return nome;
    }

    public boolean checkDB(Long codice) throws SQLException {
        String queryCheck = "SELECT codice_a_barre FROM codici_barre WHERE codice_a_barre = ?";
        PreparedStatement psCheck = conn.prepareStatement(queryCheck);
        psCheck.setLong(1, codice);
        ResultSet rs = psCheck.executeQuery();
        return rs.next();
    }

    public void inserisciCodice(Long codice, String nomeCartella, String nomeFile) throws SQLException {
        String queryIns = "INSERT INTO codici_barre (nome_cartella, nome_file, codice_a_barre) VALUES (?, ?, ?)";
        PreparedStatement psIns = conn.prepareStatement(queryIns);
        psIns.setString(1, nomeCartella);
        psIns.setString(2, nomeFile);
        psIns.setLong(3, codice);
        psIns.executeUpdate();
    }
}
