package sn.groupeisi.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
@Entity
public class Commentaire implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idC;
    @Column(name = "message")
    private String message;
    @Column(name = "dateCom")
    private Date dateC;
    @ManyToOne
    @JoinColumn(name = "membre_id")
    private Membre membre;

    public Commentaire() {
    }

    public Commentaire(int idC, String message, Date dateC) {
        this.idC = idC;
        this.message = message;
        this.dateC = dateC;
    }

    public int getIdC() {
        return idC;
    }

    public void setIdC(int idC) {
        this.idC = idC;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDateC() {
        return dateC;
    }

    public void setDateC(Date dateC) {
        this.dateC = dateC;
    }
}
