import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;

public class TestGeneral {
    @Test public void z1(){ Zone z=new Zone(); assertEquals(Zone.Etat.NORMALE,z.getEtat()); }
    @Test public void z2(){ Zone z=new Zone(); z.inonder(); assertEquals(Zone.Etat.INONDEE,z.getEtat()); }
    @Test public void z3(){ Zone z=new Zone(); z.inonder(); z.inonder(); assertEquals(Zone.Etat.SUBMERGEE,z.getEtat()); }
    @Test public void z4(){ Zone z=new Zone(); z.inonder(); z.assecher(); assertEquals(Zone.Etat.NORMALE,z.getEtat()); }

    @Test public void ze(){ ZoneElementaire ze=new ZoneElementaire(Element.EAU); assertTrue(ze.estElementaire()); }
    @Test public void ze2(){ assertEquals(Element.FEU,new ZoneElementaire(Element.FEU).getElement()); }

    @Test public void hp(){ Heliport h=new Heliport(); assertTrue(h.estHeliport()); }
    @Test public void hp2(){ assertFalse(new Heliport().estElementaire()); }

    @Test public void ct1(){ CarteTirage c=new CarteTirage(CarteTirage.Type.CLE,Element.AIR); assertEquals(CarteTirage.Type.CLE,c.getType()); }
    @Test public void ct2(){ assertEquals(Element.AIR,new CarteTirage(CarteTirage.Type.CLE,Element.AIR).getElement()); }
    @Test public void ct3(){ assertNull(new CarteTirage(CarteTirage.Type.MONTEE_DES_EAUX,null).getElement()); }
    @Test public void ct4(){ assertEquals("CLE(TERRE)",new CarteTirage(CarteTirage.Type.CLE,Element.TERRE).toString()); }

    @Test public void p1(){ PaquetCartes<String> p=new PaquetCartes<>(Arrays.asList("a","b")); assertNotNull(p.piocher()); assertNotNull(p.piocher()); }
    @Test public void p2(){ PaquetCartes<String> p=new PaquetCartes<>(Collections.singletonList("x")); p.defausser("x"); assertEquals("x",p.piocher()); }
    @Test public void p3(){ PaquetCartes<String> p=new PaquetCartes<>(Arrays.asList("a","b","c")); p.retirerCarte("b"); boolean ok=true; for(int i=0;i<3;i++){ String s=p.piocher(); if("b".equals(s)) ok=false;} assertTrue(ok); }
    @Test public void p4(){ PaquetCartes<String> p=new PaquetCartes<>(Collections.emptyList()); assertNull(p.piocher()); }
    @Test public void p5(){ PaquetCartes<String> p=new PaquetCartes<>(Collections.singletonList("y")); p.defausser("y"); p.piocher(); assertEquals("y",p.piocher()); }

    @Test public void j1(){ Joueur j=new Joueur(0,0); j.ajouterCle(Element.AIR); assertTrue(j.possedeCle(Element.AIR)); }
    @Test public void j2(){ assertFalse(new Joueur(0,0).possedeCle(Element.FEU)); }
    @Test public void j3(){ Joueur j=new Joueur(0,0); j.ajouterCle(Element.AIR); assertTrue(j.recupererArtefact(new ZoneElementaire(Element.AIR))); }
    @Test public void j4(){ assertFalse(new Joueur(0,0).recupererArtefact(new ZoneElementaire(Element.AIR))); }

    @Test public void d1(){ Ile ile=new Ile(); Joueur j=new Joueur(2,4); j.deplacer(0,1,ile); assertEquals(5,j.getY()); }
    @Test public void d2(){ Ile ile=new Ile(); Joueur j=new Joueur(2,4); j.deplacer(1,1,ile); assertEquals(2,j.getX()); assertEquals(4,j.getY()); }

    @Test public void a1(){ Ile ile=new Ile(); Joueur j=new Joueur(2,4); Zone z=ile.getZone(2,5); z.inonder(); j.assecher(ile,0,1); assertEquals(Zone.Etat.NORMALE,z.getEtat()); }

    @Test public void s1(){ Joueur j=new Joueur(0,0); j.ajouterSacDeSable(); assertEquals(1,j.getSacsDeSable()); }
    @Test public void s2(){ Ile ile=new Ile(); Joueur j=new Joueur(2,4); j.ajouterSacDeSable(); ile.getZone(2,5).inonder(); j.utiliserSacDeSable(ile,2,5); assertEquals(Zone.Etat.NORMALE,ile.getZone(2,5).getEtat()); assertEquals(0,j.getSacsDeSable()); }

    @Test public void h1(){ Joueur j=new Joueur(0,0); j.ajouterHelico(); assertEquals(1,j.getHelicos()); }
    @Test public void h2(){ Ile ile=new Ile(); Joueur j=new Joueur(2,4); j.ajouterHelico(); j.utiliserHelico(3,4,ile); assertEquals(3,j.getX()); }

    @Test public void ile1(){ Ile ile=new Ile(); assertEquals(10,ile.getRows()); }
    @Test public void ile2(){ assertTrue(new Ile().estIle(2,4)); }
    @Test public void ile3(){ assertFalse(new Ile().estIle(0,0)); }

    @Test public void ile4(){ Ile ile=new Ile(); assertNotNull(ile.piocherCarteJoueur()); }
    @Test public void ile5(){ Ile ile=new Ile(); for(int i=0;i<50;i++) ile.piocherCarteJoueur(); assertNull(ile.piocherCarteJoueur()); }

    @Test public void in1(){ Ile ile=new Ile(); Point p=ile.inonderAleatoireEtRetourne(); assertNotNull(p); assertTrue(ile.estIle(p.x,p.y)); }

    @Test public void obs(){ Ile ile=new Ile(); final int[] c={0}; ile.addObserver(()->c[0]++); ile.notifyObservers(); assertEquals(1,c[0]); }
}
