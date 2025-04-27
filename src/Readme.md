Adam mougharbel L2 info 
Reina Al Masri L2 DDIM

# L'Île Interdite

Ceci est une implémentation en Java du jeu de société "L'Île Interdite" (Forbidden Island).

## Description

L'Île Interdite est un jeu de société coopératif où les joueurs travaillent ensemble 
pour récupérer des trésors sur une île en train de sombrer avant qu’elle ne soit complètement submergée.
Cette implémentation propose une interface graphique pour jouer au jeu.

Le jeu comprend :

*Une grille d’île dynamique qui évolue à mesure que les zones sont inondées et submergées.
*Plusieurs joueurs avec différents rôles (même si les rôles ne sont pas explicitement définis dans le code fourni, la classe Joueur.java montre que plusieurs joueurs sont pris en charge).
*Des mécaniques pour se déplacer, assécher des zones, récupérer des clés et collecter des artefacts.
*Des cartes d’action spéciales comme les sacs de sable et les hélicoptères.
*Des conditions de victoire et de défaite basées sur l’état de l’île et les actions des joueurs.

## Comment exécuter

Pour compiler et exécuter ce jeu:

1. Enregistrez tous les fichiers .java dans un même répertoire.
2. Ouvrez un terminal ou une invite de commandes et naviguez jusqu'à ce répertoire.
3. Compilez les fichiers Java avec la commande : javac *.java
4. Lancez la classe principale avec :java Main

NB: Si on utilise Intellij il suffit de clicker sur le boutton run et le projet devra se compiler et s'executer.
NB: une personne a utiliser VS code et a ensuite push le code sur intellij, et l'autre a toujours utilise ce dernier.

5. La fenêtre du jeu devrait apparaître, débutant par le menu principal.

## Mécaniques de jeu

Le jeu se joue sur une grille représentant l'île. Chaque case de la grille est une Zone, qui peut être dans l'un des trois états : Normal, Inondée (INONDEE), ou Submergée (SUBMERGEE). 
Certaines zones sont des ZoneElementaire et contiennent des éléments (AIR, FEU, EAU, TERRE), tandis qu'une zone spéciale est l'Heliport.
Les joueurs (Joueur) naviguent sur l'île. À chaque tour, un joueur dispose d'un nombre limité d'actions. 
Les actions possibles incluent se déplacer sur une zone adjacente, assécher une zone inondée, ou tenter de récupérer un artefact s'il est sur une ZoneElementaire et possède la clé correspondante (CarteTirage de type CLE).
Après le tour d'un joueur, l'île commence à sombrer. Un certain nombre de zones sont inondées aléatoirement. Si une zone déjà inondée est tirée de nouveau, elle devient submergée.
Les joueurs peuvent aussi tirer des cartes (CarteTirage) qui peuvent être des clés pour récupérer les artefacts
ou des cartes d'action spéciales comme SAC_SABLE (sac de sable) pour assécher n'importe quelle zone, ou HELICOPTERE pour se déplacer vers n'importe quelle zone.

## Conditions de victoire et de défaite

Le jeu est gagné si les joueurs récupèrent les quatre artefacts et que tous les joueurs sont réunis sur l'Héliport.

Le jeu est perdu si:

*L'Héliport est submergé.
*Une zone nécessaire pour récupérer un artefact est submergée avant que l'artefact ne soit collecté.
*Il devient impossible pour les joueurs de rejoindre l'Héliport.

## Description des fichiers

CarteTirage.java: Représente les différents types de cartes que les joueurs peuvent tirer (Clés, Montée des eaux, Sac de sable, Hélicoptère).
Controlleur.java: Gère le déroulement du jeu, les actions des joueurs, la progression des tours et vérifie les conditions de victoire/défaite.
Element.java: Enumération définissant les quatre éléments liés aux artefacts et aux clés (AIR, FEU, EAU, TERRE).
Heliport.java: Représente la zone spéciale Héliport.
Ile.java: Représente la grille de l'île, incluant les zones, les joueurs et les paquets de cartes.
Joueur.java: Représente un joueur, gère sa position, ses clés et artefacts collectés ainsi que ses actions spéciales.
Main.java: Classe principale pour lancer le jeu et initialiser le menu principal.
MenuPrincipal.java: Crée l'interface du menu principal.
Message.java: Gère l'affichage de messages transitoires à l'écran.
Observable.java: Implémente le patron de conception Observable pour la mise à jour des vues.
Observer.java: Interface pour les classes devant être notifiées de changements.
PaquetCartes.java: Classe générique pour gérer les paquets de cartes (tirage, défausse, mélange).
Vue.java: Classe principale de l'affichage, gère la structure globale de l'interface graphique.
VueCommandes.java: Interface pour les commandes des joueurs (déplacements, assèchements, actions).
VueIle.java: Affiche visuellement la grille de l'île.
VueJoueurs.java: Affiche les informations des joueurs (clés, artefacts, objets spéciaux).
Zone.java: Classe de base pour toutes les zones de l'île, gère leur état (normal, inondé, submergé).
ZoneElementaire.java: Représente une zone contenant un élément/artefact.

