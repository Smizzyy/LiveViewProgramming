// Den Code bespreche ich ausführlich im Podcast "Herzbergs Hörsaal" in der Episode
// https://anchor.fm/dominikusherzberg/episodes/PiS-Das-Nim-Spiel-in-Java-programmiert-edks2t

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

Clerk.markdown(
    Text.fillOut(   
"""
# NimMC
## Vereinfachtes Monte-Carlo-Verfahren mit mcMove für das Nim-Spiel

### Die Klasse _MoveResult_
Die Klasse _MoveResult_ speichert einen Spielzug (_move_) sowie die Anzahl der Gewinne (_g_) und Verluste (_v_), 
die bei den Simulationen für diesen Zug erzielt wurden. Der Konstruktor initialisiert _g_ und _v_ jeweils mit 0, 
um mit einer leeren Zählung zu beginnen. Diese Struktur hilft uns später bei der Bewertung des besten Zugs, 
da _g_ und _v_ die Erfolgsbilanz jedes Zugs enthalten.
```java
${MoveResult}
```

### Die Methode _nimMc_:
### Die Liste _possibleMoves_
In der _mcMove_-Methode wird _possibleMoves_ als ArrayList genutzt, 
um alle möglichen Züge im aktuellen Spielzustand zu speichern. Diese Liste enthält für jeden Zug ein _MoveResult_-Objekt. 
Ich habe eine ArrayList gewählt, da sie eine schnelle (O(1)) Einfügeoperation bietet 
und die Anzahl der möglichen Züge in Nim relativ klein ist. Die Laufzeit bleibt daher effizient, 
selbst bei mehreren Durchläufen. Jeder Eintrag in der Liste repräsentiert einen möglichen Zug 
mit einem Ergebniszähler (_g_ und _v_), der in den Simulationen gefüllt wird.
```java
${possibleMoves}
```

### Die Simulation
Für jeden Zug in _possibleMoves_ werden _N_ zufällige Spiele simuliert. Der Zug wird auf den aktuellen Zustand angewendet, 
und das Spiel wird fortgesetzt, bis keine Züge mehr möglich sind. 
Eine innere Schleife erstellt _possibleMovesSim_ und fügt alle möglichen Züge für den aktuellen Spielzustand hinzu. 
Falls _possibleMovesSim_ leer ist, endet das Spiel. Bei jedem Zugwechsel wird _currentPlayerWins_ umgeschaltet, 
um den Spielerwechsel zu simulieren. Nach jeder Simulation wird _g_ erhöht, wenn der Spieler, der begann, gewann, oder _v_, falls nicht.
```java
${sim}
```

### Auswerten des besten Zugs (_bestMove_)
Am Ende durchläuft die Methode _possibleMoves_, um den Zug mit der besten Erfolgsquote (_score = g / (g + v)_) zu finden. 
_bestMove_ speichert den Zug mit dem höchsten Score; bei Gleichstand wählt ein zufälliger Boolean _nextBoolean()_ einen der gleich guten Züge. 
In den Tests habe ich _N = 40000_ gewählt, 
da dies auf einem MacBook M1 circa 2 Sekunden für die Berechnung des besten Zugs benötigt.
```java
${bestScore}
```

### Beispiel in der jshell:
```logo
jshell> Nim game = new Nim(3, 5, 4, 2, 6)
game ==> 
I I I 
I I I I I 
I I I I 
I I 
I I I I I I 

jshell> Move bestMove = game.mcMove(40000)
bestMove ==> (4, 3)
```

""", Map.of("MoveResult", Text.cutOut("./NimMC.java", "// MoveResult"),
            "possibleMoves", Text.cutOut("./NimMC.java", "// possibleMoves"),
            "sim", Text.cutOut("./NimMC.java", "// sim"),
            "bestScore", Text.cutOut("./NimMC.java", "// bestScore"))));

class Move {
    final int row, number;
    static Move of(int row, int number) {
        return new Move(row, number);
    }
    private Move(int row, int number) {
        if (row < 0 || number < 1) throw new IllegalArgumentException();
        this.row = row;
        this.number = number;
    }
    public String toString() {
        return "(" + row + ", " + number + ")";
    }
}

// MoveResult
class MoveResult {
    Move move;
    int g; // Gewinne 
    int v; // Verluste

    // Konstruktor 
    MoveResult(Move move) {
        this.move = move;
        this.g = 0;
        this.v = 0;
    }
}
// MoveResult

interface NimGame {
    static boolean isWinning(int... numbers) {
        return Arrays.stream(numbers).reduce(0, (i,j) -> i ^ j) != 0;
        // klassische Variante:
        // int res = 0;
        // for(int number : numbers) res ^= number;
        // return res != 0;
    }
    NimGame play(Move... moves);
    Move randomMove();
    Move bestMove();
    boolean isGameOver();
    String toString();
}

class Nim implements NimGame {
    private Random r = new Random();
    int[] rows;
    public static Nim of(int... rows) {
        return new Nim(rows);
    }
    protected Nim(int... rows) { // hier musste ich von private auf protected wechseln, damit die NimView-Klasse die globalen Variablen der Nim-Klasse sehen kann.
        assert rows.length >= 1;
        assert Arrays.stream(rows).allMatch(n -> n >= 0);
        // Aufbau
        if (rows.length > 5) throw new IllegalArgumentException("Es sind maximal 5 Reihen erlaubt.");
        for(int i = 0; i < rows.length; i++) {
            if (rows[i] < 0 || rows[i] > 7) throw new IllegalArgumentException("Jede Reihe darf maximal 7 Hölzchen enthalten."); 
        }
        this.rows = Arrays.copyOf(rows, rows.length);
    }
    private Nim play(Move m) {
        assert !isGameOver();
        assert m.row < rows.length && m.number <= rows[m.row];
        Nim nim = Nim.of(rows);
        nim.rows[m.row] -= m.number;
        return nim;
    }
    public Nim play(Move... moves) {
        Nim nim = this;
        for(Move m : moves) nim = nim.play(m);
        return nim;
    }
    public Move randomMove() {
        assert !isGameOver();
        int row;
        do {
            row = r.nextInt(rows.length);
        } while (rows[row] == 0);
        int number = r.nextInt(rows[row]) + 1;
        return Move.of(row, number);
    }
    public Move bestMove() {
        assert !isGameOver();
        if (!NimGame.isWinning(rows)) return randomMove();
        Move m;
        do {
            m = randomMove();
        } while(NimGame.isWinning(play(m).rows));
        return m;
    }
    public boolean isGameOver() {
        return Arrays.stream(rows).allMatch(n -> n == 0);
    }
    public String toString() {
        String s = "";
        for(int n : rows) s += "\n" + "I ".repeat(n);
        return s;
    }

    // equals-Methode
    @Override
    public boolean equals(Object other) {
        if (other == null) return false; // Null abwehren
        if (other == this) return true; // Bin ich's selbst?
        if (other.getClass() != getClass()) return false; // Andere Klasse?
        Nim that = (Nim)other; // Casting
        // Was definiert die Gleichheit zweier Nim-Objekte?
        // 1. Reihenanzahl muss gleich sein:
        if (this.rows.length != that.rows.length) return false; 
        // 2. Werte müssen gleich sein, aber nicht unbedingt in der selben Reihenfolge:
        for (int i = 0; i < this.rows.length; i++) {
            boolean found = false;
            for (int j = 0; j < that.rows.length; j++) {
                if (this.rows[i] == that.rows[j]) {
                    found = true;
                    break;
                }
            }
            if (!found) return false;
        }
        return true;
    }

    // hashCode-Methode
    @Override
    public int hashCode() {
        int[] sortedRows = Arrays.copyOf(rows, rows.length);
        Arrays.sort(sortedRows);  
        int prime = 31;
        int hash = 0;
        for (int i = 0; i < sortedRows.length; i++) {
            hash = hash * prime + sortedRows[i];
        }
        return hash;
    }

    // mcMove-Methode
    // possibleMoves
    public Move mcMove(int N) {
        List<MoveResult> possibleMoves = new ArrayList<>(); // Liste mit allen möglichen Zügen
        // alle Züge nacheinander durchgehen und in der Liste abspeichern 
        for (int row = 0; row < rows.length; row++) {
            for (int number = 1; number <= rows[row]; number++) {
                Move move = Move.of(row, number); // Spielzug erstellen und abspeichern 
                MoveResult result = new MoveResult(move); // g und v werden für den neuen Zug auf 0 gesetzt
                possibleMoves.add(result); // Zug zur Liste hinzufügen
            }      
        }
        // possibleMoves

        // sim
        for (MoveResult result : possibleMoves) { // durch die Liste über jeden Spielzug iterieren 
            for (int i = 0; i < N; i++) { // N-Simulationen für jeden Zug ausführen (N = Anzahl der Wiederholungen)
                Nim simulatedGame = this.play(result.move); // führt auf den aktuellen Zustand einen Zug aus und speichert den neuen Zustand ab
                boolean currentPlayerWins = false; // verfolgt den aktuellen Spieler während der Simulation

                // Simulation
                while (true) { // solange keine weiteren Züge mehr möglich sind
                    List<Move> possibleMovesSim = new ArrayList<>(); // Liste mit möglichen Spielzügen für den aktuellen Zustand
                    for (int ro = 0; ro < simulatedGame.rows.length; ro++) {
                        for (int no = 1; no <= simulatedGame.rows[ro]; no++) {
                            possibleMovesSim.add(Move.of(ro, no));
                        }
                    }
                    if (possibleMovesSim.isEmpty()) { // wenn keine weiteren Züge möglich sind (alle Reihen leer)
                        break;  // Spielende
                    }   
                    // wenn es noch Spielzüge gibt
                    Move randomMove = possibleMovesSim.get(r.nextInt(possibleMovesSim.size())); // zufälligen Spielzug aus der Liste auswählen und abspeichern 
                    simulatedGame = simulatedGame.play(randomMove); // zufälliger Spielzug auf den aktuellen Spielzustand anwenden und als neuen Zustand abspeichern
                    currentPlayerWins = !currentPlayerWins; // Spieler wechseln 
                }
                // gewonnen oder verloren  
                if (currentPlayerWins) {
                    result.g++; 
                } else {
                    result.v++; // (der Spieler, der begonnen hat)
                }
            }
        }
        // sim

        // bestScore
        // Besten Zug anhand der Erfolgsrate auswählen
        Move bestMove = null; // noch nicht bekannt 
        double bestScore = -1; // Startwert sorgt dafür, dass erster Score als bester Score gilt

        for (MoveResult result : possibleMoves) { // über alle möglichen Züge iterieren 
            double score = (double) result.g / (result.g + result.v); // Ergebnis liegt immer zwischen 0 und 1
            if (score > bestScore) {
                bestScore = score;
                bestMove = result.move;
            } else if (score == bestScore && r.nextBoolean()) { // zufällig einen von mehreren gleich guten Zügen auswählen
                bestMove = result.move; // zufällig bestimmt, falls true
            }
        }

        return bestMove;
        // bestScore
    }
}

// NimView
class NimView {
    static Turtle turtle = new Turtle(500, 500);
    Nim nimGame;
    NimView(Nim nimGame) {
        this.nimGame = nimGame;
    }

    void show() {
        turtle.reset();
        int[] rows = nimGame.rows;
        // Startposition
        int start = 200;
        turtle.penUp().backward(start).left(90).forward(start).right(180).penDown();

        for (int r = 0; r < rows.length; r++) {
            for(int i = 0; i < rows[r]; i++) {
                // Hölzchen zeichnen
                turtle.forward(40).penUp().left(90).forward(30).left(90).forward(40).right(180).penDown();
            }
            // in eine neue Reihe gehen
            turtle.penUp().left(90).backward(30 * rows[r]).right(90).forward(70).penDown();
        }
    }

    // Objekt aktualisieren
    public NimView play(Move move) {
        Nim updatedNim = nimGame.play(move); 
        NimView newView = new NimView(updatedNim);  
        return newView; // Neues NimView-Objekt zurückgeben
    }

    // Liefert den besten Spielzug aus der aktuellen Nim-Instanz
    public Move bestMove() {
        return nimGame.bestMove();
    }

    // Liefert einen zufälligen Spielzug aus der aktuellen Nim-Instanz
    public Move randomMove() {
        return nimGame.randomMove();
    }

    // Prüft, ob das Spiel beendet ist
    public boolean isGameOver() {
        return nimGame.isGameOver();
    }

    @Override
    public String toString() {
        show();
        return nimGame.toString();
    }
}

Nim nim = Nim.of(2,3,4);
assert nim != nim.play(Move.of(1,2)) : "Return a new Nim instance";

int[] randomSetup(int... maxN) {
    Random r = new Random();
    int[] rows = new int[maxN.length];
    for(int i = 0; i < maxN.length; i++) {
        rows[i] = Math.min(r.nextInt(maxN[i]) + 1, 7); // geändert: maximal 7 Hölzchen pro Reihe
    }
    return rows;
}

ArrayList<Move> autoplay(NimGame nim) {
    ArrayList<Move> moves = new ArrayList<>();
    while (!nim.isGameOver()) {
        Move m = nim.bestMove();
        moves.add(m);
        nim = nim.play(m);
    }
    return moves;
}

boolean simulateGame(int... maxN) {
    Nim nim = Nim.of(randomSetup(maxN));
    // System.out.println(nim);
    // System.out.println((NimGame.isWinning(nim.rows) ? "first" : "second") + " to win"); 
    ArrayList<Move> moves = autoplay(nim);
    // System.out.println(moves);
    return (NimGame.isWinning(nim.rows) && (moves.size() % 2) == 1) ||
           (!NimGame.isWinning(nim.rows) && (moves.size() % 2) == 0); 
}

assert IntStream.range(0,100).allMatch(i -> simulateGame(3,4,5));
assert IntStream.range(0,100).allMatch(i -> simulateGame(3,4,6,8));

/* // Beispielhaftes Spiel über JShell

jshell> Nim n = Nim.of(2,3,4)
n ==>
I I
I I I
I I I I

jshell> n = n.play(n.bestMove())
n ==>
I I
I I I
I

jshell> n = n.play(Move.of(2,1))
n ==>
I I
I I I


jshell> n = n.play(n.bestMove())
n ==>
I I
I I


jshell> n = n.play(Move.of(1,1))
n ==>
I I
I


jshell> n = n.play(n.bestMove())
n ==>
I
I


jshell> n = n.play(Move.of(1,1))
n ==>
I



jshell> n = n.play(n.bestMove())
n ==>




jshell> n.isGameOver()
$25 ==> true
*/