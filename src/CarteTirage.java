public class CarteTirage {
    public enum Type { CLE, MONTEE_DES_EAUX, SAC_SABLE, HELICOPTERE }

    private Type type;
    private Element element; // null sauf si type == CLE

    public CarteTirage(Type type, Element element) {
        this.type = type;
        this.element = element;
    }

    public Type getType() { return type; }
    public Element getElement() { return element; }

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
