// Den Code bespreche ich ausführlich im Podcast "Herzbergs Hörsaal" in der Episode
// https://anchor.fm/dominikusherzberg/episodes/PiS-Das-Nim-Spiel-in-Java-programmiert-edks2t

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

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
    public Move mcMove(int N) {
        List<MoveResult> possibleMoves = new ArrayList<>(); // Liste mit allen möglichen Zügen
        // alle Züge nacheinander durchgehen und in der Liste abspeichern 
        for (int row = 0; row < rows.length; row++) {
            for (int number = 1; number <= rows[row]; number++) {
                Move move = Move.of(row, number); // Spielzug ausführen und abspeichern 
                MoveResult result = new MoveResult(move); // im aktuellen Spielzug g und v auf 0 setzten und abspeichern
                possibleMoves.add(result); // in die Liste hinzufügen
            }      
        }

        for (MoveResult result : possibleMoves) { // durch die Liste über jeden Spielzug iterieren 
            for (int i = 0; i < N; i++) { // N-Simulationen für jeden Zug ausführen 
                Nim simulatedGame = this.play(result.move); // führt auf den aktuellen Zustand einen Zug aus und speichert den neuen Zustand ab
                boolean currentPlayerWins = false; // verfolgt den aktiven Spieler während der Simulation

                // Simulation
                while (true) { // solange keine weiteren Züge mehr möglich sind
                    List<Move> possibleMovesSim = new ArrayList<>(); // Liste mit möglichen Spielzügen
                    for (int ro = 0; ro < simulatedGame.rows.length; ro++) {
                        for (int no = 1; no <= simulatedGame.rows[ro]; no++) {
                            possibleMovesSim.add(Move.of(ro, no));
                        }
                    }
                    if (possibleMovesSim.isEmpty()) { // wenn kein Move-Objekt hinzugefügt wurde, also wenn bei einem Spiel alle Reihen leer sind 
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

        // Besten Zug anhand der Erfolgsrate auswählen
        Move bestMove = null; // noch nicht bekannt 
        double bestScore = -1; // Ergebnis immer zwischen 0 und 1. So ist der Start-score immer der beste score

        for (MoveResult result : possibleMoves) { // über alle möglichen Züge iterieren 
            double score = (double) result.g / (result.g + result.v);
            if (score > bestScore) {
                bestScore = score;
                bestMove = result.move;
            } else if (score == bestScore && r.nextBoolean()) { // wenn mehrere Ergebnisse gleich sind und boolean true ist
                bestMove = result.move; // zufällig bestimmt, falls true
            }
        }

        return bestMove;
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