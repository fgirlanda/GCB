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
        String query = "SELECT codice_a_barre FROM codici_barre WHERE codice_a_barre = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setLong(1, codice);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}
