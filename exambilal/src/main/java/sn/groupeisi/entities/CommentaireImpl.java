package sn.groupeisi.entities;

import sn.groupeisi.dao.DB;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CommentaireImpl implements ICommentaire {
    private DB db = new DB();
    private ResultSet rs;
    private int ok;

    @Override
    public int addCommentaire(Commentaire commentaire) {
        String sql = "INSERT INTO commentaire VALUES (null, ?, ?, ?)";

        try {
            db.initPrepar(sql);
            db.getPstm().setString(2, commentaire.getMessage());
            db.getPstm().setTimestamp(1, new Timestamp(commentaire.getDateC().getTime()));


            Membre membre = commentaire.getMembre();
            int membreId = membre.getIdM();
            db.getPstm().setInt(3, membreId);

            ok = db.executeMaj();
            db.closeConnection();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return ok;
    }

    public List<Commentaire> getAllCommentaires() {
        List<Commentaire> commentaires = new ArrayList<>();

        String sql = "SELECT * FROM commentaire";
        try {
            db.initPrepar(sql);
            rs = db.executeSelect();

            IMember memberImpl = new MembreImpl(); // Instancier MembreImpl une fois

            while (rs.next()) {
                int idC = rs.getInt("idC");
                String message = rs.getString("message");
                Timestamp dateC = rs.getTimestamp("dateCom");
                int membreId = rs.getInt("membre_id");

                // Get the corresponding Membre object
                Membre membre = memberImpl.getMembreById(membreId);

                // Create Commentaire object and add it to the list
                Commentaire commentaire = new Commentaire(idC, message, dateC);
                commentaire.setMembre(membre);
                commentaires.add(commentaire);
            }

            db.closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return commentaires;
    }


}
