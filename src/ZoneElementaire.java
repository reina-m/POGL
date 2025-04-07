public class ZoneElementaire extends Zone {
    private Element element;

    //Constructeur
    public ZoneElementaire(Element element) {
        super();
        this.element = element;
    }

    @Override
    public boolean estElementaire() {
        return true;
    }

    public Element getElement() {
        return element;
    }
}


