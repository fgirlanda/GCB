package matibbajava.classi;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public Connection getConn(){
        return conn;
    }

    public String getNome(){
        return nome;
    }
}
