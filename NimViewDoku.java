// Den Code bespreche ich ausführlich im Podcast "Herzbergs Hörsaal" in der Episode
// https://anchor.fm/dominikusherzberg/episodes/PiS-Das-Nim-Spiel-in-Java-programmiert-edks2t
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.IntStream;

Clerk.markdown(
    Text.fillOut(   
"""
# NimView
## In dieser Dokumentation gehe ich wesentlich nur auf meinen Code ein, den ich selber geschrieben habe. Diese wären in der Nim-Klasse die Regel, dass man nur _maximal fünf Reihen und maximal sieben "Hölzchen"_ haben darf, die _equals- und hashCode-Methode_ und die gesamte _NimView-Klasse_. 

""", Text.cutOut("./NimViewDoku.java", "// all")));

Clerk.markdown(
    Text.fillOut(
"""
## Aufbau des Nim-Spiels
Hier werden die Eingabedaten eines Arrays _rows_ im Konstruktor geprüft, um sicherzustellen, 
dass sie bestimmten Anforderungen entsprechen.

**1. Maximale Reihenanzahl**: Falls das Array _rows_ mehr als 5 Elemente enthält, 
wird eine _IllegalArgumentException_ mit der Nachricht "Es sind maximal 5 Reihen erlaubt." ausgelöst. 
Dies beschränkt die Anzahl der erlaubten Reihen auf 5.

**2. Wertebereich jeder Reihe**: Der Code iteriert durch jedes Element von _rows_. 
Wenn ein Element kleiner als 0 oder größer als 7 ist, wird eine weitere _IllegalArgumentException_ ausgelöst, 
diesmal mit der Nachricht "Jede Reihe darf maximal 7 Hölzchen enthalten." Damit wird sichergestellt, 
dass jede Reihe nur zwischen 0 und 7 Hölzchen enthält.

```java
${rule}
```
## equals-Methode: Sind beide Nim-Spiele gleich? 
Die equals-Methode in der Klasse Nim prüft, wann zwei Nim-Objekte als gleich betrachtet werden. 
Die Methode überprüft mehrere Bedingungen, um die Gleichheit sicherzustellen:

**Null-Check**: Falls das übergebene Objekt _other null_ ist, wird _false_ zurückgegeben.

**Selbstvergleich**: Wenn _other_ dasselbe Objekt wie _this_ ist, wird _true_ zurückgegeben, 
da ein Objekt immer gleich zu sich selbst ist.

**Typprüfung**: Wenn _other_ eine andere Klasse hat als _this_, wird _false_ zurückgegeben, 
da sie nicht vergleichbar sind.

**Casting**: _other_ wird als Objekt der Klasse _Nim_ gecastet, um auf dessen Eigenschaften zugreifen zu können.

**Reihenanzahl**: Es wird überprüft, ob beide Objekte dieselbe Anzahl an Reihen (_rows.length_) haben. 
Wenn nicht, wird _false_ zurückgegeben.

**Gleichheit der Werte**: Es wird iterativ geprüft, ob jede Zahl in _this.rows_ auch in _that.rows_ vorkommt. 
Die Reihenfolge der Zahlen spielt dabei keine Rolle. Falls ein Wert in _this.rows_ nicht in _that.rows_ vorhanden ist, 
wird _false_ zurückgegeben.

```java
${equals}
```
## hashCode-Methode: Gleicher Hashcode, wenn zwei oder mehrere Nim-Spiele "equal" sind
Die hashCode-Methode liefert einen eindeutigen Hash-Wert für ein _Nim-Objekt_. 
Der Hash-Wert hilft, _Nim_-Objekte effizient in Hash-Tabellen wie _HashMap_ oder _HashSet_ zu speichern.

**Kopieren und Sortieren**: Eine Kopie des Arrays rows wird erstellt und in sortedRows gespeichert, die dann sortiert wird. Dies stellt sicher, dass zwei Nim-Objekte mit denselben Werten, aber in unterschiedlicher Reihenfolge, denselben Hash-Wert haben.

**Prime-Faktor und Initialisierung**: Der Wert _prime = 31_ wird als Multiplikator verwendet, 
da Primzahlen eine gute Streuung für Hash-Werte fördern. Die Variable _hash_ wird initial auf 0 gesetzt.

**Berechnung des Hash-Werts**: Für jedes Element in _sortedRows_ wird hash mit dem Wert von _prime_ multipliziert und 
der aktuelle Wert des Arrays hinzugefügt. So entsteht ein eindeutiger Hash, der von den Werten in _rows_ abhängt.

**Rückgabe des Hash-Werts**: Der berechnete hash-Wert wird zurückgegeben.

```java
${hashCode}
```
""", Map.of("rule", Text.cutOut("./NimViewDoku.java", "// Aufbau"),
            "equals", Text.cutOut("./NimViewDoku.java", "// equals-Methode"),
            "hashCode", Text.cutOut("./NimViewDoku.java", "// hashCode-Methode"))));
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
        // Aufbau
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
    // equals-Methode

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
    // hashCode-Methode
}

Clerk.markdown(
    Text.fillOut(
"""
## NimView-Klasse: LiveViewProgramming
Die NimView-Klasse dient zur visuellen Darstellung und Verwaltung eines Nim-Spiels. 
Sie kombiniert die Spiellogik der _Nim_-Instanz mit der grafischen Ausgabe mittels einer Turtle-Grafik. 
Jede _NimView_-Instanz zeigt den aktuellen Spielzustand an und 
ermöglicht die Durchführung von Zügen sowie das Abrufen möglicher Züge.

### Attribute
**static Turtle turtle**: Eine statische _Turtle_-Instanz, die verwendet wird, um das Spielfeld anzuzeigen. 
Durch die statische Deklaration wird verhindert, dass mehrere Turtle-Grafiken gleichzeitig gezeichnet werden.

**Nim nimGame**: Eine Instanz der _Nim_-Klasse, die den aktuellen Zustand des Spiels speichert und 
die Spiellogik bereitstellt.

### Konstruktor
**NimView(Nim nimGame)**: Initialisiert eine neue _NimView_-Instanz mit einer _Nim_-Instanz, 
die den Anfangszustand des Spiels enthält.

### Methoden
**void show()**: Zeichnet das aktuelle Spielfeld mithilfe der Turtle-Grafik. 
Die Turtle wird an die Startposition versetzt und 
zeichnet vertikale Linien ("Hölzchen") entsprechend dem Zustand der _nimGame_-Instanz. 
Nach jedem  _turtle.reset()_ wird das Spielfeld komplett neu gezeichnet, um den aktuellen Zustand darzustellen.

**NimView play(Move move)**: Führt einen Spielzug aus und gibt ein neues _NimView-Objekt_ zurück, 
das den aktualisierten Zustand des Spiels enthält. Der Move wird über die _Nim_-Instanz ausgeführt, 
wodurch eine neue _Nim_-Instanz mit dem modifizierten Zustand erzeugt wird. 
Anschließend wird das neue Spielfeld über die _show()_-Methode dargestellt.

**Move bestMove()**: Gibt den besten Spielzug für den aktuellen Zustand des Spiels zurück. 
Diese Methode delegiert den Aufruf an die _bestMove()_-Methode der _nimGame_-Instanz.

**Move randomMove()**: Gibt einen zufälligen, gültigen Spielzug für den aktuellen Zustand des Spiels zurück. 
Der Aufruf wird an die _randomMove()_-Methode der _nimGame_-Instanz weitergeleitet.

**boolean isGameOver()**: Prüft, ob das Spiel beendet ist. Wenn alle Reihen leer sind, 
gibt die Methode _true_ zurück. Ansonsten gibt sie _false_ zurück. 
Auch hier wird der Aufruf an die _nimGame_-Instanz delegiert.

**String toString()**: Gibt eine textuelle Darstellung des aktuellen Spiels zurück, 
indem die _toString()_-Methode der _nimGame_-Instanz aufgerufen wird. Zusätzlich ruft _toString()_ die _show()_-Methode auf, 
um das Spielfeld in der aktuellen Grafik darzustellen.
```java
${0}
```
""", Text.cutOut("./NimViewDoku.java", "// NimView")));
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
// NimView

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