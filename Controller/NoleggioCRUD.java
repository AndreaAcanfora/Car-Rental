/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.util.List;
import model.Noleggi;

/**
 *
 * @author Acanfora
 */
public interface NoleggioCRUD {
     public Integer aggiungi(Noleggi a);
    public boolean elimina(int id);
    public boolean aggiorna(Noleggi a,int id);
    public List<Noleggi> mostra();
        public String getAuto(int id);
    public void deleteRecordsbyCF(String CF);
       public void deleteRecordsbyTarga(String targa);
    public Noleggi getNoleggioByID(Integer id);
    public List<Noleggi> mostraNoleggiPeriodo(String CF,String date1,String date2);

}
