public class CarteTirage {

    //Enum representant les differents types de cartes
    public enum Type { CLE, MONTEE_DES_EAUX, SAC_SABLE, HELICOPTERE }

    private Type type; //Type de carte
    private Element element; // null sauf si type == CLE

    //Constructeur pour initialiser le type de carte et l'element associe
    public CarteTirage(Type type, Element element) {
        this.type = type;
        this.element = element;
    }
    //Getter pour obtenir le type de la carte
    public Type getType() { return type; }

    //Getter pour obtenir l'element associe
    public Element getElement() { return element; }

    //toString qui retourne une representation textuelle de la carte
    @Override
    public String toString() {
        return switch (type) {
            case CLE -> "CLE(" + element + ")";
            case MONTEE_DES_EAUX -> "MONTEE_DES_EAUX";
            case SAC_SABLE -> "SAC_SABLE";
            case HELICOPTERE -> "HELICOPTERE";
        };
    }
}
