package Controller;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.util.List;
import model.Auto;

/**
 *
 * @author Acanfora
 */
public interface AutoCRUD {
      public void aggiungi(Auto a) throws Exception;
    public boolean elimina(String targa);
    public boolean aggiorna(Auto a, String targa);
    public List<Auto> mostra();
        public List<Auto> getAutoDisp();
    public String getMarcaModelloByTarga(String targa);

    public Auto getAutoByTarga(String targa);
}
