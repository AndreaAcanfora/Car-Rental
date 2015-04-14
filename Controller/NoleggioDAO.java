/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Controller;

import java.util.List;
import model.Noleggi;
import org.hibernate.Query;
import org.hibernate.Session;
import util.NewHibernateUtil;

/**
 *
 * @author Acanfora
 */
public class NoleggioDAO implements NoleggioCRUD{

     private Session session;

        public NoleggioDAO(){
                session = NewHibernateUtil.getSessionFactory().openSession();

        }
    @Override
    public Integer aggiungi(Noleggi a) {
         session.beginTransaction();
        session.save(a);
        session.getTransaction().commit();
        return a.getCodiceNoleggio();
    }

    @Override
    public boolean elimina(int id) {
         session.beginTransaction();
        Noleggi l = (Noleggi)session.get(Noleggi.class,id);
        if ( l == null )
            return false;
        session.delete(l);
        session.getTransaction().commit();
        return true;
    }
    @Override
    public String getAuto(int id){
                Noleggi l = (Noleggi)session.get(Noleggi.class,id);
     
    return l.getAuto();
    }
    @Override
    public boolean aggiorna(Noleggi a, int id) {
         session.beginTransaction();
        Noleggi old_l = (Noleggi)session.get(Noleggi.class,id);
        if (old_l == null)
            return false;
        old_l.setAuto(a.getAuto());
        old_l.setInizio(a.getInizio());
        old_l.setFine(a.getFine());
        old_l.setSocio(a.getSocio());
        old_l.setAutoRestituita(a.isAutoRestituita());
        session.update(old_l);
        session.getTransaction().commit();
        return true;    
    }

    @Override
    public List<Noleggi> mostra() {
       
        Query q = session.createQuery("FROM Noleggi");
        return (List<Noleggi>)q.list();

    }
    public void close(){
    session.close();
    }
    @Override
    public Noleggi getNoleggioByID(Integer id) {
          Query q = session.createQuery("FROM Noleggi WHERE codiceNoleggio = "+id);
        return (Noleggi)q.uniqueResult();
    }
    @Override
    public void deleteRecordsbyCF(String CF){
       Query q = session.createQuery("FROM Noleggi n WHERE n.socio = \'"+CF+"\'");
List<Noleggi> l =(List<Noleggi>)q.list();

       System.out.println("List has n.records: "+l.size());
         for(int i=0;i<l.size();i++){
             System.out.println("Deleting "+l.get(i).getCodiceNoleggio());
           this.elimina(l.get(i).getCodiceNoleggio());
         }
   }
        @Override
   public void deleteRecordsbyTarga(String targa){
       System.out.println("Using targa: "+targa);
       Query q = session.createQuery("FROM Noleggi n WHERE n.auto = \'"+targa+"\'");
List<Noleggi> l =(List<Noleggi>)q.list();

       System.out.println("List has n.records: "+l.size());
         for(int i=0;i<l.size();i++){
             System.out.println("Deleting "+l.get(i).getCodiceNoleggio());
           this.elimina(l.get(i).getCodiceNoleggio());
         }
   }
    @Override
    public List<Noleggi> mostraNoleggiPeriodo(String CF,String date1,String date2) {
        Query q = session.createQuery("FROM Noleggi WHERE socio = \'"+CF+"\' and inizio >= \'"+date1+"\' and fine <= \'"+date2+"\'");
        return (List<Noleggi>)q.list();
    }
    
}
