import model.DAO.DAO;
import model.DAO.FaqDAO;
import model.beans.FAQ;
import model.beans.Utente;
import model.storage.ConPool;
import org.junit.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FaqDAOTest {

   DAO<FAQ> faqDAO;
   Utente utente;

   @BeforeClass
   public static void openConnection() throws SQLException {
      ConPool.getInstance().getConnection();
   }

   @Before
   public void setup() {
      faqDAO = new FaqDAO();
      utente = new Utente();
      utente.setIdUtente(1);
   }

   @Test
   public void saveNullObject() {
      assertThrows(IllegalArgumentException.class, () -> {
         faqDAO.save(null);
      });
   }

   @Test
   public void saveEmptyObject() {
      assertThrows(RuntimeException.class, () -> {
         faqDAO.save(new FAQ());
      });
   }

   @Test
   public void savePartialObject() {
      FAQ faq = new FAQ();
      faq.setDomanda("");
      faq.setRisposta(null);
      faq.setUtenteCreatore(utente);

      assertThrows(RuntimeException.class, () -> {
         faqDAO.save(faq);
      });
   }

   @Test
   public void saveObject() {
      FAQ faq = new FAQ();
      faq.setDomanda("");
      faq.setRisposta("");
      faq.setUtenteCreatore(utente);

      assertTrue(faqDAO.save(faq));

      faqDAO.delete(faq);
   }

   @Test
   public void getByIdInvalidId() {
      assertThrows(IllegalArgumentException.class, () -> {
         faqDAO.getById(-1);
      });
   }

   @Test
   public void getById() {
      FAQ faq = new FAQ();
      faq.setDomanda("Domanda");
      faq.setRisposta("Risposta");
      faq.setUtenteCreatore(utente);

      faqDAO.save(faq);
      FAQ faqDB = (FAQ) faqDAO.getById(faq.getIdFaq());

      assertAll(
              () -> assertNotNull(faqDB),
              () -> assertEquals(faq.getIdFaq(), faqDB.getIdFaq()),
              () -> assertEquals(faq.getDomanda(), faqDB.getDomanda()),
              () -> assertEquals(faq.getRisposta(), faqDB.getRisposta()),
              () -> assertEquals(faq.getUtenteCreatore().getIdUtente(),
                      faqDB.getUtenteCreatore().getIdUtente())
      );

      faqDAO.delete(faqDB);
   }

   @Test
   public void updateNullObject() {
      assertThrows(IllegalArgumentException.class, () -> {
         faqDAO.update(null);
      });
   }

   @Test
   public void updateEmptyObject() {
      assertThrows(RuntimeException.class, () -> {
         faqDAO.update(new FAQ());
      });
   }

   @Test
   public void update() {
      FAQ faq = new FAQ();
      faq.setDomanda("Domanda");
      faq.setRisposta("Risposta");
      faq.setUtenteCreatore(utente);

      faqDAO.save(faq);

      faq.setDomanda("DomandaCambiata");
      faq.setRisposta("RispostaCambiata");

      assertTrue(faqDAO.update(faq));

      faqDAO.delete(faq);
   }


   @Test
   public void updatePartialObject() {
      FAQ faq = new FAQ();
      faq.setDomanda("Domanda");
      faq.setRisposta("Risposta");
      faq.setUtenteCreatore(utente);

      faqDAO.save(faq);

      assertAll(
              () -> assertThrows(RuntimeException.class, () -> {
                 faq.setDomanda("DomandaCambiata");
                 faq.setRisposta(null);
                 faqDAO.update(faq);
              })
      );

      faqDAO.delete(faq);
   }

   @Test
   public void getAll() {
      FAQ faq = new FAQ();
      faq.setDomanda("Domanda");
      faq.setRisposta("Risposta");
      faq.setUtenteCreatore(utente);

      faqDAO.save(faq);

      List<FAQ> faqList = faqDAO.getAll();

      assertAll(
              () -> assertNotNull(faqList),
              () -> assertTrue(faqList.size() > 0)
      );

      faqDAO.delete(faq);
   }

   @Test
   public void deleteEntityNull() {
      assertThrows(IllegalArgumentException.class, () -> {
         faqDAO.delete(null);
      });
   }

   @Test
   public void delete() {
      FAQ faq = new FAQ();
      faq.setDomanda("Domanda");
      faq.setRisposta("Risposta");
      faq.setUtenteCreatore(utente);

      faqDAO.save(faq);

      assertTrue(faqDAO.delete(faq));
   }

   @Test
   public void extractNullResultSet() {
      assertThrows(IllegalArgumentException.class, () -> {
         faqDAO.extract(null);
      });
   }

   @AfterClass
   public static void closeConnection() {
      ConPool.getInstance().closeDataSource();
   }
}
