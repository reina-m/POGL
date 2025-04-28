# L'Île Interdite

**Par Adam Mougharbel (L2 Info) et Reina Al Masri (L2 DDIM)**

---

## Description

Cette application est une implémentation en Java du jeu de société *L'Île Interdite*.

**Le principe** : Les joueurs doivent coopérer pour récupérer quatre artefacts dispersés sur une île qui s'enfonce progressivement sous l'eau. Il faudra réussir à tous se réunir sur l'héliport pour s'échapper avant que l'île ne disparaisse complètement.

Le projet propose :

- Une grille d'île dynamique avec évolution de l'état des zones (normal, inondé, submergé).
- Un système multi-joueurs avec gestion individuelle des clés, artefacts et déplacements.
- Des cartes spéciales : sacs de sable, hélicoptères.
- Des conditions de victoire et de défaite suivant l'état de l'île et les actions des joueurs.
- Une interface graphique simple respectant l'architecture **Modèle-Vue-Contrôleur (MVC)**.

---

## Comment exécuter

1. Récupérer tous les fichiers `.java` dans un même dossier.
2. Ouvrir un terminal et naviguer dans ce dossier.
3. Compiler avec :

   ```bash
   javac *.java
   ```

4. Lancer le jeu avec :

   ```bash
   java Main
   ```

**Notes** :
- Le projet peut être ouvert et exécuté directement sous IntelliJ (bouton *Run*).
- Certains travaux initiaux ont été faits sur VS Code avant un passage définitif sous IntelliJ.

---

## Mécaniques de jeu

- Chaque case de la grille est une `Zone`, pouvant être :
  - **Normale**
  - **Inondée**
  - **Submergée**
- Certaines zones sont des `ZoneElementaire` (associées à un élément : Air, Feu, Eau, Terre).
- Les joueurs peuvent :
  - Se déplacer.
  - Assécher une zone.
  - Récupérer des artefacts s'ils possèdent la clé correspondante.
  - Utiliser des cartes spéciales.

Après chaque tour :
- De nouvelles zones sont aléatoirement inondées.
- Les joueurs reçoivent parfois des cartes.

---

## Conditions de victoire

- Tous les artefacts ont été récupérés.
- Tous les joueurs sont réunis sur l'héliport.
- Tous embarquent ensemble.

## Conditions de défaite

- L'héliport est submergé.
- Une zone nécessaire à un artefact est perdue avant sa récupération.
- Aucun chemin possible vers l'héliport pour un joueur.

---

## Structure du projet

- **`Main.java`** : Point d'entrée du programme, création de l'île, de la vue et du contrôleur.
- **`Ile.java`** : Représente la grille de jeu, initialise l'île et ses zones spéciales.
- **`Zone.java`** : Zone de l'île (normal, inondé, submergé).
- **`ZoneElementaire.java`** : Zone contenant un artefact élémentaire.
- **`Heliport.java`** : Zone spéciale, lieu d'évacuation.
- **`Joueur.java`** : Gère la position, les clés et artefacts des joueurs.
- **`Controlleur.java`** : Gère les actions des joueurs et la progression du jeu.
- **`Vue.java`** : Fenêtre principale qui assemble toutes les vues secondaires.
- **`VueIle.java`** : Affiche visuellement l'île et son état.
- **`VueJoueurs.java`** : Affiche les informations individuelles des joueurs.
- **`VueCommandes.java`** : Interface pour réaliser les actions du joueur.
- **`Observer.java`** et **`Observable.java`** : Implémentation du pattern Observateur.
- **`Element.java`** : Enumération des quatre éléments à récupérer.

---

## Remarques personnelles

Nous avons pris soin de :

- Respecter l'architecture MVC imposée.
- Proposer une interface graphique claire et simple.
- Tester progressivement toutes les mécaniques avant d'assembler le projet complet.
- Travailler à deux efficacement en utilisant GitLab et IntelliJ.

Ce projet nous a permis de renforcer nos compétences en programmation orientée objet, en Java, et en travail collaboratif.


# Ressources 
tuiles : https://brysiaa.itch.io/pixel-tileset-grass-island
joueurs : https://agusstt.itch.io/tiny-ghost-animated
clés : https://drxwat.itch.io/pixel-art-key
artefacts : https://gamedevshlok.itch.io/heartpack
