package sn.groupeisi.entities;

import java.util.List;

public interface IMember {
    public int addMember(Membre membre);
    public List<Membre> membreList();
    public Membre lastMembre();
}
