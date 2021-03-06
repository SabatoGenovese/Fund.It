package Services;

import model.DAO.DAO;
import model.DAO.UtenteDAO;
import model.beans.Utente;
import model.services.UtenteService;
import model.services.UtenteServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UtenteServiceTest {
   Utente utente;

   @Before
   public void setup() {
      utente = new Utente();

      utente.setAdmin(true);
      utente.setIdUtente(1);
      utente.setCap("cap");
      utente.setCf("cf");
      utente.setCitta("città");
      utente.setCognome("cognome");
      utente.setDataBan(null);
      utente.setDataDiNascita(LocalDate.now());
      utente.setEmail("email");
      utente.setFotoProfilo("fotoProfilo");
      utente.setNome("nome");
      utente.setPassword("passwordhash");
      utente.setStrada("strada");
      utente.setTelefono("telefono");
      utente.setCampagne(null);
      utente.setDonazioni(null);
      utente.setSegnalazioni(null);
   }

   @Test
   public void visualizzaDashboardUtente1() {
      UtenteService utenteService = new UtenteServiceImpl(Mockito.mock(UtenteDAO.class));

      assertThrows(IllegalArgumentException.class, () -> utenteService.visualizzaDashboardUtente(0));
   }

   @Test
   public void visualizzaDashboardUtente2() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.getById(utente.getIdUtente())).thenReturn(null);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertNull(utenteService.visualizzaDashboardUtente(utente.getIdUtente()));
   }

   @Test
   public void visualizzaDashboardUtente3() {
      utente.setDataBan(null);
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.getById(utente.getIdUtente())).thenReturn(utente);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);
      Utente utenteDB = utenteService.visualizzaDashboardUtente(utente.getIdUtente());

      assertAll(
              () -> assertNotNull(utenteDB),
              () -> assertEquals(utente.isAdmin(), utenteDB.isAdmin()),
              () -> assertEquals(utente.getCap(), utenteDB.getCap()),
              () -> assertEquals(utente.getCf(), utenteDB.getCf()),
              () -> assertEquals(utente.getCitta(), utenteDB.getCitta()),
              () -> assertEquals(utente.getCognome(), utenteDB.getCognome()),
              () -> assertEquals(utente.getDataBan(), utenteDB.getDataBan()),
              () -> assertEquals(utente.getDataDiNascita(), utenteDB.getDataDiNascita()),
              () -> assertEquals(utente.getEmail(), utenteDB.getEmail()),
              () -> assertEquals(utente.getFotoProfilo(), utenteDB.getFotoProfilo()),
              () -> assertEquals(utente.getNome(), utenteDB.getNome()),
              () -> assertEquals(utente.getPassword(), utenteDB.getPassword()),
              () -> assertEquals(utente.getStrada(), utenteDB.getStrada()),
              () -> assertEquals(utente.getTelefono(), utenteDB.getTelefono())
      );
   }

   @Test
   public void modificaProfilo1() {
      UtenteService utenteService = new UtenteServiceImpl(Mockito.mock(UtenteDAO.class));

      assertThrows(IllegalArgumentException.class, () -> utenteService.modificaProfilo(null));
   }

   @Test
   public void modificaProfilo2() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.update(utente)).thenReturn(true);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertTrue(utenteService.modificaProfilo(utente));
   }

   @Test
   public void modificaProfilo3() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.update(utente)).thenReturn(false);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertFalse(utenteService.modificaProfilo(utente));
   }

   @Test
   public void sospensioneUtente1() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertThrows(IllegalArgumentException.class, () -> utenteService.sospensioneUtente(null));
   }

   @Test
   public void sospensioneUtente2() {
      utente.setDataBan(null);
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.update(utente)).thenReturn(true);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertTrue(utenteService.sospensioneUtente(utente));
   }

   @Test
   public void sospensioneUtente3() {
      utente.setDataBan(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.update(utente)).thenReturn(true);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertTrue(utenteService.sospensioneUtente(utente));
   }

   @Test
   public void sospensioneUtente4() {
      utente.setDataBan(null);
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.update(utente)).thenReturn(false);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertFalse(utenteService.sospensioneUtente(utente));
   }

   @Test
   public void sospensioneUtente5() {
      utente.setDataBan(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.update(utente)).thenReturn(false);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertFalse(utenteService.sospensioneUtente(utente));
   }

   @Test
   public void promuoviDeclassaUtente1() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertThrows(IllegalArgumentException.class,
              () -> utenteService.promuoviDeclassaUtente(null, null));
   }

   @Test
   public void promuoviDeclassaUtente2() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertThrows(IllegalArgumentException.class,
              () -> utenteService.promuoviDeclassaUtente(utente, null));
   }

   @Test
   public void promuoviDeclassaUtente3() {
      utente.setAdmin(false);
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertThrows(IllegalCallerException.class,
              () -> utenteService.promuoviDeclassaUtente(utente, utente));
   }

   @Test
   public void promuoviDeclassaUtente4() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.update(utente)).thenReturn(false);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertFalse(utenteService.promuoviDeclassaUtente(utente, utente));
   }

   @Test
   public void promuoviDeclassaUtente5() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.update(utente)).thenReturn(true);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertTrue(utenteService.promuoviDeclassaUtente(utente, utente));
   }

   @Test
   public void visualizzaUtenti1() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertThrows(IllegalArgumentException.class, () -> utenteService.visualizzaUtenti(null));
   }

   @Test
   public void visualizzaUtenti2() {
      utente.setAdmin(false);
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);

      assertThrows(IllegalCallerException.class, () -> utenteService.visualizzaUtenti(utente));
   }

   @Test
   public void visualizzaUtenti3() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.getAll()).thenReturn(new ArrayList<>());
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);
      List<Utente> utenteList = utenteService.visualizzaUtenti(utente);

      assertAll(
              () -> assertNotNull(utenteList),
              () -> assertEquals(0, utenteList.size())
      );
   }

   @Test
   public void visualizzaUtenti4() {
      DAO<Utente> utenteDAO = Mockito.mock(UtenteDAO.class);
      Mockito.when(utenteDAO.getAll()).thenReturn(List.of(utente));
      UtenteService utenteService = new UtenteServiceImpl(utenteDAO);
      List<Utente> utenteList = utenteService.visualizzaUtenti(utente);

      assertAll(
              () -> assertNotNull(utenteList),
              () -> assertEquals(1, utenteList.size()),
              () -> assertEquals(utente.isAdmin(), utenteList.get(0).isAdmin()),
              () -> assertEquals(utente.getCap(), utenteList.get(0).getCap()),
              () -> assertEquals(utente.getCf(), utenteList.get(0).getCf()),
              () -> assertEquals(utente.getCitta(), utenteList.get(0).getCitta()),
              () -> assertEquals(utente.getCognome(), utenteList.get(0).getCognome()),
              () -> assertEquals(utente.getDataBan(), utenteList.get(0).getDataBan()),
              () -> assertEquals(utente.getDataDiNascita(), utenteList.get(0).getDataDiNascita()),
              () -> assertEquals(utente.getEmail(), utenteList.get(0).getEmail()),
              () -> assertEquals(utente.getFotoProfilo(), utenteList.get(0).getFotoProfilo()),
              () -> assertEquals(utente.getNome(), utenteList.get(0).getNome()),
              () -> assertEquals(utente.getPassword(), utenteList.get(0).getPassword()),
              () -> assertEquals(utente.getStrada(), utenteList.get(0).getStrada()),
              () -> assertEquals(utente.getTelefono(), utenteList.get(0).getTelefono())
      );
   }
}
