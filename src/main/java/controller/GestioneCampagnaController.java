package controller;

import controller.utils.FileServlet;
import controller.utils.Validator;
import model.beans.Campagna;
import model.beans.Categoria;
import model.beans.Donazione;
import model.beans.Immagine;
import model.beans.StatoCampagna;
import model.beans.Utente;
import model.beans.proxies.CampagnaProxy;
import model.beans.proxies.DonazioneProxy;
import model.beans.proxyInterfaces.CampagnaInterface;
import model.services.CategoriaService;
import model.services.CampagnaService;
import model.services.CategoriaServiceImpl;
import model.services.CampagnaServiceImpl;
import model.services.ReportService;
import model.services.TipoReport;
import model.services.ImmagineService;
import model.services.ImmagineServiceImpl;
import model.storage.ConPool;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "GestioneCampagnaController",
        value = "/campagna/*",
        loadOnStartup = 0)
@MultipartConfig
public final class GestioneCampagnaController extends HttpServlet {
   /**
    * Variabile per il service di Campagna.
    */
   private static CampagnaService campagnaService;

   /**
    * Variabile per il service di Categoria.
    */
   private static CategoriaService categoriaService;

   /**
    * Costruttore per GestioneCampagnaController.
    *
    * @param service    il service di Campagna.
    * @param catService il service di Categoria.
    */
   public GestioneCampagnaController(final CampagnaService service,
                                     final CategoriaService catService) {
      campagnaService = service;
      categoriaService = catService;
   }

   /**
    * Costruttore per GestioneCampagnaController.
    */
   public GestioneCampagnaController() {
      campagnaService = new CampagnaServiceImpl();
      categoriaService = new CategoriaServiceImpl();
   }

   @Override
   public void doGet(final HttpServletRequest request,
                     final HttpServletResponse response)
           throws ServletException, IOException {
      String resource = "/";
      HttpSession session = request.getSession();
      String path = request.getPathInfo() == null
              ? "/" : request.getPathInfo();

      Utente userSession = (Utente) session.getAttribute("utente");

      switch (path) {
         case "/main":
            List<Campagna> lst = campagnaService.getActiveCampagne();
            lst.forEach(c -> {
               CampagnaInterface proxy = new CampagnaProxy(c);
               proxy.getUtente();
               proxy.getImmagini();
            });
            request.setAttribute("campagneList", lst);
            resource = "/WEB-INF/results/main_page.jsp";
            break;
         case "/creaCampagna":
            if (!new Validator(request).isValidBean(Utente.class,
                    userSession)) {
               response.sendRedirect(
                       request.getServletContext().getContextPath()
                               + "/autenticazione/login");
               return;
            } else {
               request.setAttribute("categorie",
                       categoriaService.visualizzaCategorie());
               resource = "/WEB-INF/results/form_campagna.jsp";
            }
            break;
         case "/modificaCampagna":
            visualizzaModificaCampagna(request, response);
            return;
         case "/campagna":
            String id = request.getParameter("idCampagna");
            int idCampagna = Integer.parseInt(id);
            Campagna c = campagnaService.trovaCampagna(idCampagna);
            if (c == null || c.getStato() != StatoCampagna.ATTIVA) {
               response.sendError(
                       HttpServletResponse.SC_NOT_FOUND,
                       "Campagna non trovata");
               return;
            } else {
               CampagnaInterface proxy = new CampagnaProxy(c);
               c.setUtente(proxy.getUtente());
               DonazioneProxy proxy2 = new DonazioneProxy();
               c.setImmagini(proxy.getImmagini());
               List<Donazione> donazioni = proxy.getDonazioni();
               donazioni.forEach(d -> {
                  proxy2.setDonazione(d);
                  d.setUtente(proxy2.getUtente());
               });
               c.setDonazioni(proxy.getDonazioni());
               if (campagnaService.modificaCampagna(c)) {
                  request.setAttribute("campagna", c);
                  condividiCampagna(request, c.getIdCampagna());
                  resource = "/WEB-INF/results/campagna.jsp";
               } else {
                  response.sendError(HttpServletResponse.SC_NOT_FOUND);
                  return;
               }
            }
            break;
         case "/ricerca":
            String searchText = request.getParameter("searchText");
            searchText = searchText.trim();
            List<Campagna> campagne = campagnaService.
                    ricercaCampagna(searchText);
            campagne = campagne.stream().
                    filter(campagna -> campagna.getStato()
                            == StatoCampagna.ATTIVA).
                    collect(Collectors.toList());

            if (campagne.size() > 0 && !searchText.isBlank()) {
               for (Campagna campagna : campagne) {
                  new CampagnaProxy(campagna).getImmagini();
               }
               request.setAttribute("campagneList", campagne);
               resource = "/WEB-INF/results/campagne.jsp";
            } else {
               request.setAttribute("errorSearch",
                       "Nessun risultato trovato");
               resource = "/WEB-INF/results/campagne.jsp";
            }
            break;
         case "/ricercaCategoria":
            String cat = request.getParameter("idCat");
            if (cat.isBlank()) {
               response.sendError(HttpServletResponse.SC_NOT_FOUND);
               return;
            }
            String idcattrimmed = cat.trim();
            Categoria categoria = new Categoria();
            try {
               categoria.setIdCategoria(Integer.parseInt(idcattrimmed));
            } catch (NumberFormatException e) {
               response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            categoria = categoriaService.visualizzaCategoria(categoria);

            if (categoria == null) {
               response.sendError(HttpServletResponse.SC_NOT_FOUND);
               return;
            }

            List<Campagna> campagneSearched = campagnaService.
                    ricercaCampagnaPerCategoria(categoria.getNome());

            campagne = campagneSearched.stream().
                    filter(campagna -> campagna.getStato()
                            == StatoCampagna.ATTIVA).
                    collect(Collectors.toList());

            if (campagne.size() > 0) {
               for (Campagna campagna : campagne) {
                  new CampagnaProxy(campagna).getImmagini();
               }
               request.setAttribute("campagneList", campagne);
            } else {
               request.setAttribute("errorSearch",
                       "Nessun risultato trovato");
            }
            resource = "/WEB-INF/results/campagne.jsp";
            break;
         default:
            response.sendError(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Risorsa non trovata");
            return;
      }

      RequestDispatcher dispatcher =
              request.getRequestDispatcher(resource);
      dispatcher.forward(request, response);
   }

   @Override
   public void doPost(final HttpServletRequest request,
                      final HttpServletResponse response)
           throws ServletException, IOException {

      HttpSession session = request.getSession();
      Utente userSession = (Utente) session.getAttribute("utente");
      String path = request.getPathInfo();
      if (!new Validator(request).isValidBean(Utente.class, userSession)) {
         response.sendRedirect(
                 request.getServletContext().getContextPath()
                         + "/autenticazione/login");
      } else {
         String idCampagna = request.getParameter("idCampagna");
         int id;

         switch (path) {
            case "/creaCampagna":
               creaCampagna(request, response, userSession);
               break;
            case "/modificaCampagna":
               if (idCampagna != null) {
                  modificaCampagna(request, response, campagnaService
                                  .trovaCampagna(
                                          Integer.parseInt(idCampagna)),
                          userSession);
               } else {
                  ReportService.creaReport(request, TipoReport.ERRORE,
                          "Operazione modifica campagna:",
                          "Non effettuata con successo");
               }
               break;
            case "/cancellaCampagna":
               id = Integer.parseInt(idCampagna);
               Campagna campagna = campagnaService.trovaCampagna(id);

               if (campagna.getUtente().getIdUtente()
                       != userSession.getIdUtente()) {
                  response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                  return;
               } else {
                  if (campagnaService.cancellaCampagna(campagna)) {
                     if (campagnaService.rimborsaDonazioni(campagna,
                             new CampagnaProxy(campagna))) {
                        ReportService.creaReport(request,
                                TipoReport.INFO,
                                "Operazione rimborso donazioni:",
                                "Effettuata con successo");
                     } else {
                        ReportService.creaReport(request,
                                TipoReport.ERRORE,
                                "Operazione rimborso donazioni:",
                                "Non effettuata con successo");
                     }
                     response.sendRedirect(request.getServletContext()
                             .getContextPath()
                             + "/GestioneUtenteController"
                             + "/visualizzaDashboard");
                     return;
                  } else {
                     ReportService.creaReport(request,
                             TipoReport.ERRORE,
                             "Operazione cancellazione campagna:",
                             "Non effettuata con successo");
                  }
               }
               break;
            case "/chiudiCampagna":
               id = Integer.parseInt(idCampagna);
               Campagna campagna1 = campagnaService.trovaCampagna(id);
               if (campagnaService.chiudiCampagna(campagna1)) {
                  ReportService.creaReport(request,
                          TipoReport.INFO,
                          "Chiusura campagna",
                          "Azione effettuata con successo");
                  response.sendRedirect(request.getServletContext()
                          .getContextPath()
                          + "/GestioneUtenteController"
                          + "/visualizzaDashboard");
               } else {
                  response.sendError(
                          HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
               }
               return;
            default:
               response.sendError(
                       HttpServletResponse.SC_NOT_FOUND);
               break;
         }
      }
   }

   private void condividiCampagna(final HttpServletRequest request,
                                  final int idCampagna) {

      Map<String, String> map =
              campagnaService.condividiCampagna(idCampagna, request);

      request.setAttribute("linkList", map);

   }

   private void visualizzaModificaCampagna(final HttpServletRequest request,
                                           final HttpServletResponse response)
           throws ServletException, IOException {
      HttpSession session = request.getSession();
      Utente userSession = (Utente) session.getAttribute("utente");

      if (!new Validator(request).isValidBean(Utente.class,
              session.getAttribute("utente"))) {
         response.sendRedirect(
                 request.getServletContext().getContextPath()
                         + "/autenticazione/login");
         return;
      }

      String idCampagna = request.getParameter("idCampagna");
      int id;
      try {
         id = Integer.parseInt(idCampagna);
      } catch (NumberFormatException e) {
         response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE,
                 "Input errato");
         e.printStackTrace();
         return;
      }
      Campagna c = campagnaService.trovaCampagna(id);
      if (c.getUtente().getIdUtente() != userSession.getIdUtente()) {
         response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                 "Non Autorizzato");
         return;
      }
      request.setAttribute("campagna", c);
      request.setAttribute("categorie",
              categoriaService.visualizzaCategorie());
      RequestDispatcher dispatcher =
              request.getRequestDispatcher(
                      "/WEB-INF/results/form_campagna.jsp");
      dispatcher.forward(request, response);
   }

   private void creaCampagna(final HttpServletRequest req,
                             final HttpServletResponse res,
                             final Utente utente)
           throws IOException, ServletException {

      Campagna c = extractCampagna(req);
      c.setSommaRaccolta(0d);
      c.setUtente(utente);

      if (campagnaService.creazioneCampagna(c)) {
         uploadFoto(req, c);
         ReportService.creaReport(req, TipoReport.INFO,
                 "Operazione creazione campagna:",
                 "Effettuata con successo");
         res.sendRedirect(
                 req.getServletContext().getContextPath() + "/index.jsp");
      } else {
         ReportService.creaReport(req, TipoReport.ERRORE,
                 "Operazione creazione campagna:",
                 "Non effettuata con successo");
         res.sendRedirect(
                 req.getServletContext().getContextPath()
                         + "/campagna/creaCampagna");
      }
   }

   private Campagna extractCampagna(final HttpServletRequest request) {
      Campagna c = new Campagna();

      c.setStato(StatoCampagna.ATTIVA);
      c.setTitolo(request.getParameter("titolo"));
      c.setDescrizione(request.getParameter("descrizione"));

      c.setSommaTarget(
              Double.parseDouble(request.getParameter("sommaTarget")));
      c.setUtente((Utente) request.getSession(false).getAttribute("utente"));

      Categoria categoria = new Categoria();
      categoria.setIdCategoria(Integer.parseInt(
              request.getParameter("idCategoria")));

      c.setCategoria(
              categoriaService.visualizzaCategoria(categoria));

      return c;
   }

   private void uploadFoto(final HttpServletRequest request,
                           final Campagna campagna) throws ServletException,
           IOException {
      List<String> fotoList = FileServlet.uploadFoto(request);
      ImmagineService immagineService =
              new ImmagineServiceImpl();
      Immagine immagine = new Immagine();
      immagine.setCampagna(campagna);

      if (!fotoList.isEmpty()) {
         immagineService.eliminaImmaginiCampagna(campagna.getIdCampagna());
      }

      for (String fotoPath : fotoList) {
         immagine.setPath(fotoPath);
         immagineService.salvaImmagine(immagine);
      }
   }

   private void modificaCampagna(final HttpServletRequest request,
                                 final HttpServletResponse response,
                                 final Campagna campagna,
                                 final Utente utente)
           throws IOException, ServletException {

      Campagna c = extractCampagna(request);
      c.setIdCampagna(campagna.getIdCampagna());
      c.setSommaRaccolta(campagna.getSommaRaccolta());

      if (campagnaService.modificaCampagna(c)) {
         uploadFoto(request, c);
         ReportService.creaReport(request, TipoReport.INFO,
                 "Operazione modifica campagna:",
                 "Effettuata con successo");
         response.sendRedirect(
                 request
                         .getServletContext()
                         .getContextPath() + "/index.jsp");
      } else {
         ReportService.creaReport(request, TipoReport.ERRORE,
                 "Operazione modifica campagna:",
                 "Non effettuata con successo");
         request.getRequestDispatcher("/campagna"
                 + "/modificaCampagna").forward(request, response);
      }
   }

   @Override
   public void destroy() {
      ConPool.getInstance().closeDataSource();
      super.destroy();
   }

}
