/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.util.List;
import model.Auto;
import org.hibernate.Query;
import org.hibernate.Session;
import util.NewHibernateUtil;

/**
 *
 * @author Acanfora
 */
public class AutoDAO implements AutoCRUD{
        private Session session;

        public AutoDAO(){
                session = NewHibernateUtil.getSessionFactory().openSession();

        }
    @Override
    public void aggiungi(Auto a) throws Exception{
        try{
        session.beginTransaction();
        session.save(a);
        session.getTransaction().commit();
         } catch(Exception ex) {
            session.getTransaction().rollback();
            System.out.println(ex.getMessage());
            throw new Exception(ex.getMessage()); 
        }
    }

    @Override
    public boolean elimina(String targa) {
          session.beginTransaction();
        Auto a = (Auto)session.get(Auto.class, targa);
        if (a == null)
            return false;
        session.delete(a);
        session.getTransaction().commit();
        return true;
    }

    @Override
    public boolean aggiorna(Auto a, String targa) {
          session.beginTransaction();
        Auto old_a = (Auto)session.get(Auto.class, targa);
        if (old_a == null)
            return false;
        old_a.setCostoGiornallero(a.getCostoGiornallero());
        old_a.setMarca(a.getMarca());
        old_a.setModello(a.getModello());
        session.update(old_a);
        session.getTransaction().commit();
        return true;
    }

  

    public void close(){
    session.close();
    }
    @Override
    public Auto getAutoByTarga(String targa) {
          Query q = session.createQuery("FROM Auto WHERE targa = \'" + targa+"\'");
        return (Auto)q.uniqueResult();
    }
        @Override
    public String getMarcaModelloByTarga(String targa){
     Query q = session.createQuery("select concat(marca,' - ',modello) FROM Auto WHERE targa = " + targa);
     return (String)q.uniqueResult();
    }
    

 
  @Override
    public List<Auto> mostra() {
          Query q = session.createQuery("FROM Auto");
        List<Auto> lst = (List<Auto>)q.list();
        return lst;
        
    }
 
    @Override
    public List<Auto> getAutoDisp() {
                 Query q = session.createQuery("FROM Auto a WHERE a.targa not in ( select t.targa from Auto t , Noleggi n where t.targa=n.auto and n.autoRestituita=0 )");
        List<Auto> lst = (List<Auto>)q.list();
                 return lst;

    }
}
