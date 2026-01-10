# Systeme de Gestion de Bibliotheque

Projet Java - BUT Informatique

## Description

Application console de gestion de bibliotheque developpee en Java. Ce systeme permet de gerer les livres, les membres et les emprunts d'une bibliotheque de maniere interactive.

## Fonctionnalites

- **Gestion des livres** : Ajout, suppression, recherche et consultation des livres
- **Gestion des membres** : Inscription, suppression et recherche de membres
- **Gestion des emprunts** : Emprunt, retour, suivi des retards
- **Persistance des donnees** : Sauvegarde et chargement via serialisation Java
- **Statistiques** : Livres les plus empruntes, membres les plus actifs, statistiques mensuelles
- **Recherche avancee** : Recherche multi-criteres avec filtres

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
+-------------------+       +-------------------+       +-------------------+
        |                           |                           |
        +---------------------------+---------------------------+
                                    |
                        +-----------+-----------+
                        |   LibraryService      |
                        +-----------------------+
                        | - books: List<Book>   |
                        | - members: List       |
                        | - loans: List<Loan>   |
                        +-----------------------+
                        | + addBook()           |
                        | + borrowBook()        |
                        | + returnBook()        |
                        | + searchBook()        |
                        +-----------------------+
```

## Comment executer

### Pre-requis
- Java JDK 17 ou superieur

### Compilation et execution

```bash
# Compiler tous les fichiers
javac -d out src/**/*.java src/Main.java

# Executer l'application
java -cp out Main
```

## Structure du projet

```
src/
  |-- model/
  |     |-- Book.java
  |     |-- Member.java
  |     |-- Loan.java
  |-- service/
  |     |-- LibraryService.java
  |     |-- DataManager.java
  |     |-- StatisticsService.java
  |     |-- SearchEngine.java
  |-- exception/
  |     |-- BookNotAvailableException.java
  |     |-- MemberNotFoundException.java
  |     |-- MaxLoansExceededException.java
  |-- util/
  |     |-- DateUtils.java
  |     |-- InputValidator.java
  |-- ui/
  |     |-- ConsoleMenu.java
  |-- Main.java
```

## Auteur

Saad LAGZIRI - BUT Informatique
