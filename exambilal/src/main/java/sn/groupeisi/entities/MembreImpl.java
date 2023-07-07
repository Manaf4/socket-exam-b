package sn.groupeisi.entities;

import sn.groupeisi.dao.DB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MembreImpl implements IMember {
    private DB db = new DB();
    private ResultSet rs;
    private int ok;
    @Override
    public int addMember(Membre membre) {
        String sql = "INSERT INTO membre VALUES(null, ?)";
        try{
            db.initPrepar(sql);
            db.getPstm().setString(1, membre.getUsername());
            ok = db.executeMaj();


            db.closeConnection();

        }catch(Exception exception){
            exception.printStackTrace();
        }
        return ok;
    }

    public List<Membre> membreList(){
        List<Membre> membres = new ArrayList<>();
        String sql = "SELECT * FROM membre ORDER BY idM DESC LIMIT 1";

        try{
            db.initPrepar(sql);
            rs = db.executeSelect();
            while(rs.next()){
                Membre membre = new Membre();
                membre.setIdM(rs.getInt("idM"));
                membre.setUsername(rs.getString("username"));

                membres.add(membre);
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }

        return membres;
    }
    public Membre lastMembre(){
        Membre membre = new Membre();
        String sql = "SELECT * FROM membre ORDER BY idM DESC LIMIT 1";

        try{
            db.initPrepar(sql);
            rs = db.executeSelect();
            if (rs.next()){
                membre.setIdM(rs.getInt("idM"));
                membre.setUsername(rs.getString("username"));
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        //System.out.println(membre.getUsername());

        return membre;
    }


}
