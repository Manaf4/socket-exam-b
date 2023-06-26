package sn.groupeisi.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DB {
    //Pour la connexion
    private Connection cnx;
    //Pour les requetes préparées
    private PreparedStatement pstm;
    //Pour les resultats de requetes de type SELECT
    private ResultSet rs;
    //Pour les resultats de requetes de type mise á jour (INSERT INTO, UPDATE, DELETE)
    private int ok;

    //Méthode d'ouverture de la connexion á la base de données
    private Connection getConnection() {
        //Paramétres de connexion
        String host = "localhost";
        String user = "root";
        String password = "";
        String database = "db_socket_bilal";
        String url = "jdbc:mysql://"+host+":3306/"+database;
        try {
            //Chargement du Driver
            //Class.forName("com.mysql.jdbc.Driver");
            //Ouverture de la connexion
            cnx = DriverManager.getConnection(url, user, password);
            //System.out.println("yess");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnx;
    }

    //Méthode de préparation de requete
    public void initPrepar(String sql) {
        try {
            getConnection();
            pstm = cnx.prepareStatement(sql);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Méthode pour les requetes de type SELECT
    public ResultSet executeSelect() {
        rs = null;
        try {
            rs = pstm.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    //Méthode pour les requetes de type mise á jour
    public int executeMaj() {
        try {
            ok = pstm.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    //Méthode de fermeture de la connexion
    public void closeConnection() {
        try {
            if(cnx != null) {
                cnx.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Getter de l'attribut pstm
    public PreparedStatement getPstm() {
        return pstm;
    }
}
