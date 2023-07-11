package sn.groupeisi.entities;

import java.util.List;

public interface ICommentaire {
    public int addCommentaire(Commentaire commentaire);
    public List<Commentaire> getAllCommentaires();
}
