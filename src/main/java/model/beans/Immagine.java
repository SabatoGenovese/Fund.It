package model.beans;

public final class Immagine {
    /**
     * Riferimento alla campagna.
     */
    private Campagna campagna;
    /**
     * Path immagine.
     */
    private String path;
    /**
     * Id univoco immagine.
     */
    private int id;
    /**
     * Getter Campagna.
     * @return la campagna relativa
     */
    public Campagna getCampagna() {
        return campagna;
    }

    /**
     * Getter Path.
     * @return la stringa del path
     */
    public String getPath() {
        return path;
    }

    /**
     * Setter Campagna.
     * @param newCampagna riferimento alla campagna
     */
    public void setCampagna(final Campagna newCampagna) {
        this.campagna = newCampagna;
    }

    /**
     * Setter path.
     * @param imgPath specifica il path dell'immagine
     */
    public void setPath(final String imgPath) {
        this.path = imgPath;
    }

    /**
     * @return id dell'immagine.
     */
    public int getId() {
        return id;
    }

    /**
     * @param ident identificativo dell'immagine da recuperare.
     */
    public void setId(final int ident) {
        this.id = ident;
    }
    /**
     * @return la stringa dell'oggetto
     */
    @Override
    public String toString() {
        return "Immagine{"
                + "campagna=" + campagna.getIdCampagna()
                + ", path='" + path + '\''
                + ", id=" + id
                + '}';
    }
}
