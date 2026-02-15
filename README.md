# Systeme de Gestion de Bibliotheque

Projet Java - BUT Informatique

## Description

Application console de gestion de bibliotheque developpee en Java. Ce systeme permet de gerer les livres, les membres et les emprunts d'une bibliotheque de maniere interactive. Le projet illustre les principes de la programmation orientee objet : encapsulation, heritage, polymorphisme et abstraction.

## Fonctionnalites

### Gestion des livres
- Ajout de nouveaux livres avec validation ISBN
- Suppression de livres (uniquement si non empruntes)
- Recherche par titre, auteur, genre ou ISBN
- Affichage des livres disponibles

### Gestion des membres
- Inscription de nouveaux membres avec validation email
- Suppression de membres (uniquement si aucun emprunt en cours)
- Recherche par nom, email ou identifiant
- Limite de 5 emprunts simultanes par membre

### Gestion des emprunts
- Emprunt de livres avec verification automatique des regles
- Retour de livres avec detection des retards
- Duree par defaut : 21 jours
- Affichage des emprunts en cours et en retard
- Historique d'emprunts par membre

### Persistance des donnees
- Sauvegarde et chargement via serialisation Java
- Export au format CSV (livres, membres, emprunts)
- Chargement automatique au demarrage

### Statistiques et analyses
- Livres les plus empruntes (top N)
- Membres les plus actifs (top N)
- Statistiques mensuelles des emprunts
- Distribution des livres par genre
- Taux d'occupation de la bibliotheque
- Duree moyenne des emprunts
- Taux de retard

### Recherche avancee
- Recherche multi-criteres combinable (ET logique)
- Filtres : titre, auteur, genre, plage d'annees, disponibilite
- Recherche globale (livres + membres)
- Liste des genres et auteurs disponibles

## Diagramme UML (simplifie)

```
+-------------------+       +-------------------+       +-------------------+
|      Book         |       |      Member       |       |       Loan        |
+-------------------+       +-------------------+       +-------------------+
| - title: String   |       | - id: String      |       | - book: Book      |
| - author: String  |       | - name: String    |       | - member: Member  |
| - isbn: String    |       | - email: String   |       | - loanDate: Date  |
| - year: int       |       | - borrowedBooks   |       | - dueDate: Date   |
| - available: bool |       | - registrationDate|       | - returnDate: Date|
| - genre: String   |       +-------------------+       | - isReturned: bool|
+-------------------+       | + getId()         |       +-------------------+
| + getTitle()      |       | + getName()       |       | + getBook()       |
| + isAvailable()   |       | + addBook()       |       | + isOverdue()     |
| + setAvailable()  |       | + removeBook()    |       | + returnBook()    |
| + equals()        |       | + canBorrow()     |       | + getOverdueDays()|
+-------------------+       +-------------------+       +-------------------+
                                    |
        +---------------------------+---------------------------+
        |                           |                           |
+-------+-------+       +----------+----------+       +--------+---------+
| LibraryService|       | StatisticsService   |       |   SearchEngine   |
+---------------+       +---------------------+       +------------------+
| - books       |       | - libraryService    |       | - libraryService |
| - members     |       +---------------------+       +------------------+
| - loans       |       | + getMostBorrowed() |       | + searchBooks()  |
+---------------+       | + getMostActive()   |       | + searchMembers()|
| + addBook()   |       | + getMonthlyStats() |       | + globalSearch() |
| + borrowBook()|       | + getGenreDistrib() |       | + searchByGenre()|
| + returnBook()|       | + generateReport()  |       | + searchByAuthor()|
| + searchBook()|       +---------------------+       +------------------+
+---------------+
        |
+-------+--------+       +-------------------+
|   DataManager   |       |   ConsoleMenu     |
+----------------+       +-------------------+
| + saveLibrary()|       | - libraryService  |
| + loadLibrary()|       | - scanner         |
| + exportToCSV()|       +-------------------+
+----------------+       | + start()         |
                         | + booksMenu()     |
                         | + membersMenu()   |
                         | + loansMenu()     |
                         +-------------------+
```

## Exceptions personnalisees

| Exception                    | Description                                      |
|------------------------------|--------------------------------------------------|
| `BookNotAvailableException`  | Livre non disponible a l'emprunt                 |
| `MemberNotFoundException`    | Membre non trouve dans le systeme                |
| `MaxLoansExceededException`  | Nombre maximum d'emprunts atteint (5)            |

## Classes utilitaires

| Classe            | Description                                          |
|-------------------|------------------------------------------------------|
| `DateUtils`       | Formatage dates (FR, ISO), calculs de differences    |
| `InputValidator`  | Validation ISBN, email, nom, annee de publication    |

## Comment executer

### Pre-requis
- Java JDK 17 ou superieur

### Compilation et execution

```bash
# Compiler tous les fichiers
javac -d out src/model/*.java src/exception/*.java src/util/*.java src/service/*.java src/ui/*.java src/Main.java

# Executer l'application
java -cp out Main
```

### Utilisation

Au lancement, l'application charge automatiquement les donnees sauvegardees (si elles existent) ou initialise des donnees d'exemple. Le menu principal offre 5 options :

1. **Gestion des livres** : ajouter, supprimer, rechercher, afficher
2. **Gestion des membres** : inscrire, supprimer, rechercher, afficher
3. **Gestion des emprunts** : emprunter, retourner, voir les retards
4. **Statistiques** : resume des donnees de la bibliotheque
5. **Sauvegarder / Charger** : persistance et export CSV

## Structure du projet

```
java-library-system/
  |-- src/
  |     |-- model/
  |     |     |-- Book.java           # Modele de livre
  |     |     |-- Member.java         # Modele de membre
  |     |     |-- Loan.java           # Modele d'emprunt
  |     |-- service/
  |     |     |-- LibraryService.java      # Service principal
  |     |     |-- DataManager.java         # Persistance des donnees
  |     |     |-- StatisticsService.java   # Statistiques et rapports
  |     |     |-- SearchEngine.java        # Recherche avancee
  |     |-- exception/
  |     |     |-- BookNotAvailableException.java
  |     |     |-- MemberNotFoundException.java
  |     |     |-- MaxLoansExceededException.java
  |     |-- util/
  |     |     |-- DateUtils.java           # Utilitaires de dates
  |     |     |-- InputValidator.java      # Validation des saisies
  |     |-- ui/
  |     |     |-- ConsoleMenu.java         # Interface console
  |     |-- Main.java                      # Point d'entree
  |-- .gitignore
  |-- README.md
```

## Technologies utilisees

- **Langage** : Java 17
- **Collections** : ArrayList, LinkedHashMap
- **API** : Stream API, java.time (LocalDate, YearMonth)
- **Persistance** : Serialisation Java (ObjectOutputStream / ObjectInputStream)
- **Export** : Format CSV avec separateur point-virgule

## Principes OOP appliques

- **Encapsulation** : attributs prives, getters/setters, copie defensive
- **Responsabilite unique** : chaque classe a un role precis
- **Gestion des exceptions** : exceptions personnalisees pour les erreurs metier
- **Validation** : verification des donnees en entree
- **Documentation** : Javadoc complete pour toutes les classes et methodes

## Auteur

Saad LAGZIRI - BUT Informatique
