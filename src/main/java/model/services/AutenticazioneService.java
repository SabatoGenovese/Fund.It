package model.services;

import model.beans.Utente;

/**
 *
 */
public interface AutenticazioneService {

    /**
     * @param utente Istanza di Utente che desidera fare il login
     * @return true se l'operazione è andata a buon fine, false altrimenti
     */
    boolean login(Utente utente);

    /**
     * @param utente Istanza di Utente che desidera fare la registrazione
     * @return true se l'operazione va a buon fine, false altrimenti
     */
    boolean registrazione(Utente utente);

    /**
     *
     * @param utente Istanza di Utente che desidera fare il logout
     * @return true se l'operazione va a buon fine, false altrimenti
     */
    boolean logout(Utente utente);
}
