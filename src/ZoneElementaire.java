public class ZoneElementaire extends Zone {
    enum Element {
        AIR, FEU, EAU, TERRE
    }
    private Element element;

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


