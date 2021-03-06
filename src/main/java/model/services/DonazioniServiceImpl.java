package model.services;

import model.DAO.DAO;
import model.DAO.DonazioneDAO;
import model.beans.Donazione;
import model.beans.Utente;

import java.util.List;

public final class DonazioniServiceImpl implements DonazioniService {
    /**
     * Wrapper di DonazioneDAO.
     */
    private final DAO<Donazione> dao;

    /**
     * Costruttore Donazione Service.
     * @param donazioneDAO istanza di DonazioneDAO
     */
    public DonazioniServiceImpl(final DAO<Donazione> donazioneDAO) {
        this.dao = donazioneDAO;
    }

    /**
     * Costruttore Donazione Service.
     */
    public DonazioniServiceImpl() {
        this.dao = new DonazioneDAO();
    }

    @Override
    public boolean effettuaDonazione(final Donazione d) {
        if (d == null) {
            throw new IllegalArgumentException("Argument invalid");
        }
        return dao.save(d);
    }

    @Override
    public List<Donazione> visualizzaDonazioni(final Utente u) {
        if (u == null) {
            throw new IllegalArgumentException("Argument must be not null");
        } else {
            DonazioneDAO donazioneDAO = (DonazioneDAO) dao;
            return donazioneDAO.getAllByUtente(u.getIdUtente());
        }
    }

    @Override
    public boolean commenta(final Donazione d) {
        if (d == null) {
            throw new IllegalArgumentException("Argument must be not null");
        } else {
            return dao.update(d);
        }
    }

    /**
     * Trova tutte le donazioni effettuate sul sistema.
     *
     * @return lista delle donazioni attualmente fatte sul sistema
     */
    @Override
    public List<Donazione> visualizzaDonazioni() {
        return dao.getAll();
    }

}
