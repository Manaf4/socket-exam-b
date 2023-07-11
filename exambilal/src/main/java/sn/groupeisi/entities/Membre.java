package sn.groupeisi.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
public class Membre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idM;
    @Column(name="username", unique=true, length=255, nullable=false)
    private String username;
    @OneToMany(mappedBy = "membre")
    private List<Commentaire> commentaires;

    public Membre() {
    }

    public Membre(int idM, String username, List<Commentaire> commentaires) {
        this.idM = idM;
        this.username = username;
        this.commentaires = commentaires;
    }

    public Membre(int idM, String username) {
        this.idM = idM;
        this.username = username;
    }

    public int getIdM() {
        return idM;
    }

    public void setIdM(int idM) {
        this.idM = idM;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Commentaire> getCommentaires() {
        return commentaires;
    }

    public void setCommentaires(List<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }
}
