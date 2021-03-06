package model.beans.proxies;

import model.DAO.CampagnaDAO;
import model.DAO.DAO;
import model.DAO.UtenteDAO;
import model.beans.Campagna;
import model.beans.Donazione;
import model.beans.Utente;
import model.beans.proxyInterfaces.DonazioneInterface;

public final class DonazioneProxy implements DonazioneInterface {
    /**
     * donazione.
     */
    private Donazione donazione;


    /**
     * Costruttore.
     */
    public DonazioneProxy() {
        this.donazione = new Donazione();
    }

    /**
     * Costruttore.
     *
     * @param newDonazione donazione.
     */
    public DonazioneProxy(final Donazione newDonazione) {
        this.donazione = newDonazione;
    }


    @Override
    public Campagna getCampagna() {
        if (donazione.getCampagna() == null) {
            throw new IllegalArgumentException("Campagna must be not null");
        }
        if (donazione.getCampagna().getTitolo() == null) {
            DAO<Campagna> campagnaDAO = new CampagnaDAO();
            Campagna campagna = campagnaDAO
                    .getById(
                            donazione.getCampagna()
                                    .getIdCampagna());

            donazione.setCampagna(campagna);
            return donazione.getCampagna();
        } else {
            return donazione.getCampagna();
        }
    }

    @Override
    public Utente getUtente() {
        if (donazione.getUtente() == null) {
            throw new IllegalArgumentException("Utente must be not null");
        }
        if (donazione.getUtente().getCf() == null) {
            DAO<Utente> utenteDAO = new UtenteDAO();
            Utente u = utenteDAO.getById(donazione.getUtente().getIdUtente());
            if (u != null) {
                Utente utenteNuovo = donazione.getUtente();
                utenteNuovo.setIdUtente(u.getIdUtente());
                utenteNuovo.setNome(u.getNome());
                utenteNuovo.setCognome(u.getCognome());
                donazione.setUtente(utenteNuovo);
                return donazione.getUtente();
            } else {
                return null;
            }
        } else {
            return donazione.getUtente();
        }
    }

    /**
     * @param nuovaDonazione istanza di Donazione
     */
    public void setDonazione(final Donazione nuovaDonazione) {
        this.donazione = nuovaDonazione;
    }
}
