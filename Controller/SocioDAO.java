/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.util.List;
import model.Soci;
import org.hibernate.Query;
import org.hibernate.Session;
import util.NewHibernateUtil;

/**
 *
 * @author Acanfora
 */
public class SocioDAO implements SocioCRUD{
 private Session session;

        public SocioDAO(){
                session = NewHibernateUtil.getSessionFactory().openSession();

        }
    @Override
    public void aggiungi(Soci s) {
            try{
        session.beginTransaction();
        session.save(s);
        session.getTransaction().commit();
         } catch(Exception ex) {
            session.getTransaction().rollback();
        }
    }

    public void close(){
    session.close();
    }
    @Override
    public boolean elimina(String CF) {
            session.beginTransaction();
        Soci a = (Soci)session.get(Soci.class, CF);
        if (a == null)
            return false;
        session.delete(a);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public boolean aggiorna(Soci s, String CF) {
              session.beginTransaction();
        Soci old_a = (Soci)session.get(Soci.class, CF);
        if (old_a == null)
            return false;
        old_a.setCognome(s.getCognome());
        old_a.setIndirizzo(s.getIndirizzo());
        old_a.setNome(s.getNome());
        old_a.setTelefono(s.getTelefono());
        session.update(old_a);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public List<Soci> mostra() {
         Query q = session.createQuery("FROM Soci");
        List<Soci> lst = (List<Soci>)q.list();
        return lst;
    }

    @Override
    public Soci getSocioByCF(String CF) {
          Query q = session.createQuery("FROM Soci WHERE cf = \'" + CF+"\'");
        return (Soci)q.uniqueResult();
    }
        @Override
    public String getCFbyCognomeNome(String nominativo){
    
     Query q = session.createQuery("FROM Soci WHERE lower(concat(cognome,' ',nome))= \'"+nominativo.toLowerCase()+"\'");
        return ((Soci)q.uniqueResult()).getCf();
    }
    
}
