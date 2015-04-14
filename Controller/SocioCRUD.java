/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.util.List;
import model.Soci;

/**
 *
 * @author Acanfora
 */
public interface SocioCRUD {
     public void aggiungi(Soci s);
    public boolean elimina(String CF);
    public boolean aggiorna(Soci s, String CF);
    public List<Soci> mostra();
    public Soci getSocioByCF(String CF);
        public String getCFbyCognomeNome(String nominativo);

}
