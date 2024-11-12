import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

Clerk.markdown("""
# Nachbau der Turing-Maschine
## Beispiel: Dekrementierung einer Binärzahl
### Folgendes Programm ist für die Turing-Maschine gegeben: 
| Aktueller Zustand | Gelesenes Zeichen | Geschriebenes Zeichen | Bewegung | Neuer Zustand |
|-------------------|-------------------|-----------------------|----------|---------------|
| S                 | #                 | #                     | LEFT     | S             |
| S                 | 1                 | 0                     | RIGHT    | R             |
| S                 | 0                 | 1                     | LEFT     | L             |
| R                 | 0                 | 0                     | RIGHT    | R             |
| R                 | 1                 | 1                     | RIGHT    | R             |
| R                 | #                 | #                     | LEFT     | W             |
| W                 | 1                 | 1                     | RIGHT    | HALT          |
| W                 | 0                 | 0                     | RIGHT    | HALT          |
| W                 | #                 | #                     | RIGHT    | HALT          |
| L                 | 0                 | 1                     | LEFT     | L             |
| L                 | 1                 | 0                     | RIGHT    | R             |
| L                 | #                 | #                     | RIGHT    | R             |
##
## Die ersten drei Einzelschritte erklärt:
**Das Band wird initialisiert mit:** 
```logo 
# 1 1 0 0 0 {#} 
```
**Das Vorbelegungszeichen auf dem Band ist # und die Maschine befindet sich im Zustand S.**
###
**Schritt 1**: Die TM liest das Zeichen **#** an der Position des S/L-Kopfes vom Band. Der S/L-Kopf schreibt **keine Änderung**, bewegt sich nach **links** und die TM bleibt im Zustand **S**.
####
**Schritt 2**: Die TM liest das Zeichen **0** an der Position des S/L-Kopfes vom Band. Der S/L-Kopf schreibt das Zeichen **1**, bewegt sich nach **links** und die TM wechselt in Zustand **L**.
####
**Schritt 3**: Die TM liest das Zeichen **0** an der Position des S/L-Kopfes vom Band. Der S/L-Kopf schreibt das Zeichen **1**, bewegt sich nach **links** und die TM wechselt in Zustand **L**.
### Alle Schritte in der jshell: 
```logo 
jshell> turingMachine.run()
0:  # 1 1 0 0 0 {#}  -- S
1:  # 1 1 0 0 {0} #  -- S
2:  # 1 1 0 {0} 1 #  -- L
3:  # 1 1 {0} 1 1 #  -- L
4:  # 1 {1} 1 1 1 #  -- L
5:  # 1 0 {1} 1 1 #  -- R
6:  # 1 0 1 {1} 1 #  -- R
7:  # 1 0 1 1 {1} #  -- R
8:  # 1 0 1 1 1 {#}  -- R
9:  # 1 0 1 1 {1} #  -- W
10: # 1 0 1 1 1 {#}  -- HALT
```
#### Beispiel: Einsen nach rechts schieben
```logo
jshell> turingMachine.run()
0:  S 0 1 0 1 0 {1} S  -- S
1:  S 0 1 0 1 {0} 1 S  -- S
2:  S 0 1 0 {1} 0 1 S  -- 0
3:  S 0 1 0 0 {0} 1 S  -- 1
4:  S 0 1 0 0 0 {1} S  -- 1
5:  S 0 1 0 0 {0} 1 S  -- D
6:  S 0 1 0 {0} 1 1 S  -- S
7:  S 0 1 {0} 0 1 1 S  -- 0
8:  S 0 {1} 0 0 1 1 S  -- 0
9:  S 0 0 {0} 0 1 1 S  -- 1
10: S 0 0 0 {0} 1 1 S  -- 1
11: S 0 0 0 0 {1} 1 S  -- 1
12: S 0 0 0 {0} 1 1 S  -- D
13: S 0 0 {0} 1 1 1 S  -- S
14: S 0 {0} 0 1 1 1 S  -- 0
15: S {0} 0 0 1 1 1 S  -- 0
16: {S} 0 0 0 1 1 1 S  -- 0
17: S {0} 0 0 1 1 1 S  -- HALT
```

### Um diese Ausgaben auch in der LiveView zu erzeugen, führe diese Kommandos in der jshell aus: 
**Initialisiert das Band:**
```logo 
String initialContent = "#11000#"
```
oder 2. Beispiel:
```logo 
String initialContent = "S010101S"
```

**Vorbelegungszeichen setzten:**
```logo 
int startPosition = initialContent.length() - 1
```
oder 2. Beispiel:
```logo 
int startPosition = initialContent.length() - 2
```

**Der Turing-Maschine das Band, den Zustand, das Vorbelegungszeichen und die Tabelle übergeben:**
```logo 
TM turingMachine = new TM(initialContent, "S", startPosition, "decrement");
```
oder 2. Beispiel:
```logo 
TM turingMachine = new TM(initialContent, "S", startPosition, "moveOnes");
```

**Turing-Maschine starten:**
```logo 
turingMachine.run()
```
## Hier die Ausgabe: 
""");

// Bewegungsrichtung des Schreib-/Lesekopfes
enum Move {
    LEFT,
    RIGHT
}

// record Klassen sind geeignet, um nur Daten zu speichern und sie immutable zu machen. Generiert auch automatisch equals(), hashCode() und toString()
// Trigger-Klasse, die den Zustand und das gelesene Zeichen repräsentiert
record Trigger(String fromState, char read) { }
// Action-Klasse, die beschreibt, was die Turing-Maschine ausführen soll
record Action(char write, Move move, String toState) { }

// Klasse zur Repräsentation des Bandes der TM
class Tape {
    List<Character> tape; // Liste zur Speicherung der Zeichen auf dem Band
    int headPosition; // Position des Schreib-/Lesekopfes
    // Standardzeichen für leere Stellen
    char defaultChar1 = '#';
    char defaultChar2 = 'S';

    // Konstruktor zur Initialisierung des Bandes und der Kopfposition
    public Tape(String initialContent, int startPosition) {
        this.tape = new ArrayList<>();
        for (char c : initialContent.toCharArray()) {
            tape.add(c);
        }
        // Überprüfung der Startposition
        if (startPosition >= 0 && startPosition < tape.size()) {
            this.headPosition = startPosition;
        }
    }
    
    // Methode zum Lesen des Zeichens unter dem Kopf
    char read() {
        return tape.get(headPosition);
    }

    // Methode zum Schreiben eines Zeichens unter dem Kopf
    void write(char c) {
        tape.set(headPosition, c);
    }

    // Methode zum Bewegen des Schreib-/Lesekopfes
    void move(Move direction) {
        if (direction == Move.RIGHT) {
            headPosition++;
            // Falls der Kopf über das Ende des Bandes hinausgeht, füge ein Standardzeichen hinzu
            if (headPosition >= tape.size()) {
                if (tape.get(headPosition) == defaultChar1) tape.add(defaultChar1);
                if (tape.get(headPosition) == defaultChar2) tape.add(defaultChar2);
            } 
        } else if (direction == Move.LEFT) {
            if (headPosition == 0) {
                // Falls der Kopf sich am Anfang befindet, füge ein Standardzeichen am Anfang hinzu
                if (tape.get(headPosition) == defaultChar1) tape.add(0, defaultChar1);
                if (tape.get(headPosition) == defaultChar2) tape.add(0, defaultChar2);
            } else {
                headPosition--;
            }
        }     
    }
}

// Klasse zur Darstellung der Transitionstabelle
class Table {
    Map<Trigger, Action> map; // Map zur Speicherung von Übergängen (Trigger -> Aktion)

    // Konstruktor zur Initialisierung der Transitionstabelle
    public Table() {
        this.map = new HashMap<>();
    }
    
    // Methode zum Hinzufügen eines Übergangs
    public void addTransition(String fromState, char read, char write, Move move, String toState) {
        Trigger trigger = new Trigger(fromState, read);
        Action action = new Action(write, move, toState);
        map.put(trigger, action); 
    }

    // Methode zur Initialisierung der Übergänge für die Dekrementierung einer Binärzahl
    public void initializeTransitionsDecrement() {
        addTransition("S", '#', '#', Move.LEFT, "S");
        addTransition("S", '1', '0', Move.RIGHT, "R");
        addTransition("S", '0', '1', Move.LEFT, "L");
        addTransition("R", '0', '0', Move.RIGHT, "R");
        addTransition("R", '1', '1', Move.RIGHT, "R");
        addTransition("R", '#', '#', Move.LEFT, "W");
        addTransition("W", '1', '1', Move.RIGHT, "HALT");
        addTransition("W", '0', '0', Move.RIGHT, "HALT");
        addTransition("W", '#', '#', Move.RIGHT, "HALT");
        addTransition("L", '0', '1', Move.LEFT, "L");
        addTransition("L", '1', '0', Move.RIGHT, "R");
        addTransition("L", '#', '#', Move.RIGHT, "R");
    }
    
    // Methode zur Initialisierung der Übergänge für das Bewegen von Einsen nach rechts
    public void initializeTransitionsMoveOnes() {
        addTransition("S", '1', '1', Move.LEFT, "S");
        addTransition("S", 'S', 'S', Move.RIGHT, "HALT");
        addTransition("S", '0', '0', Move.LEFT, "0");
        addTransition("0", '0', '0', Move.LEFT, "0");
        addTransition("0", '1', '0', Move.RIGHT, "1");
        addTransition("0", 'S', 'S', Move.RIGHT, "HALT");
        addTransition("1", '0', '0', Move.RIGHT, "1");
        addTransition("1", '1', '1', Move.LEFT, "D");
        addTransition("1", 'S', 'S', Move.LEFT, "D");
        addTransition("D", '0', '1', Move.LEFT, "S");
    }

    // Methode zum Abrufen der Aktion basierend auf Zustand und gelesenem Zeichen
    public Action getAction(String state, char read) {
        return map.get(new Trigger(state, read)); 
    }
}

// Hauptklasse der Turing-Maschine
class TM {
    private Tape tapeTM; // Das Band der Turing-Maschine
    private Table table; // Die Transitionstabelle
    private String currentState; // Der aktuelle Zustand der Turing-Maschine
    private int stepCount = 0; // Zähler für die Anzahl der Schritte
    private Turtle turtle; // Turtle-Objekt für die Live-View
    private int cellCounter = 0;
    
     // Konstruktor zur Initialisierung der Turing-Maschine
    public TM(String initialContent, String startState, int startPosition, String tableType) {
        this.tapeTM = new Tape(initialContent, startPosition);
        this.table = new Table();
        // Initialisieren der Turtle für die Live-View
        this.turtle = new Turtle(800, 2000); // Initialisieren der Turtle-Grafik
        turtle.penUp().backward(350).left(90).forward(950); // Startposition der Turtle setzen
        
        // Auswahl der zu initialisierenden Transitionstabelle basierend auf dem Tabellentyp
        switch (tableType) {
            case "decrement":
                this.table.initializeTransitionsDecrement();
                break;
            case "moveOnes":
                this.table.initializeTransitionsMoveOnes();
                break;
            default:
                throw new IllegalArgumentException("Invalid table type specified.");
        }
        
        this.currentState = startState; // Setzen des Startzustands
    }

    // Methode zur Ausführung eines Schrittes der TM
    public void step() {
        char currentChar = tapeTM.read(); // Lesen des aktuellen Zeichens unter dem Kopf
        Action action = table.getAction(currentState, currentChar); // Abrufen der Aktion basierend auf dem aktuellen Zustand und Zeichen
        if (action != null) {
            tapeTM.write(action.write()); // Schreiben des Zeichens auf das Band
            tapeTM.move(action.move()); // Bewegen des Kopfes in die angegebene Richtung
            currentState = action.toState(); // Wechseln in den neuen Zustand
        } else {
            currentState = "HALT"; // Anhalten, wenn keine Aktion gefunden wird
        }
    }

    // Methode zur vollständigen Ausführung der Turing-Maschine
    public void run() {
        System.out.println(this.toString()); // Ausgabe des Anfangszustands
        
        while (!currentState.equals("HALT")) {
            step(); // Ausführen eines Schrittes
            stepCount++; // Schrittzähler erhöhen
            System.out.println(this.toString()); // Ausgabe des aktuellen Bandzustands
            try {
                Thread.sleep(800); // Verzögerung für bessere Sichtbarkeit
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Wiederherstellung des unterbrochenen Status
            }
        }
    }

    // Methode zur Darstellung des aktuellen Bandzustands in der jshell
    @Override
    public String toString() {
        String result = "";  
        // Schrittzähler ausgeben
        if (stepCount >= 10) result += stepCount + ": "; // ein Leerzeichen weniger (Optik)
        else result += stepCount + ":  ";
    
        // Darstellung des Bandes mit Hervorhebung der Kopfposition
        for (int i = 0; i < tapeTM.tape.size(); i++) {
            if (i == tapeTM.headPosition) {
                result += "{" + tapeTM.tape.get(i) + "}";
            } else {
                result += tapeTM.tape.get(i);
            }
            result += " ";
        }
        result += " -- " + currentState; // Ausgabe des aktuellen Zustands
        updateTurtleView(); // Aktualisieren der Live-View mit der Turtle
        turtle.backward(50).left(90).forward(cellCounter * 70).right(90); // in eine neue Zeile rutschen
        cellCounter = 0; // wieder zurücksetzen
        return result;
    }
    
    // Methode zur Aktualisierung der Live-View
    private void updateTurtleView() {
        int headPosition = tapeTM.headPosition; // Abrufen der Kopfposition
        // Zeichne das Band mit dem Schreib/Lese-Kopf und den Nachbarzellen
        for (int i = 0; i < tapeTM.tape.size(); i++) {
            if (i == headPosition) {
                turtle.color(255, 0, 0); // Rot für die Zelle unter dem S/L-Kopf
                turtle.text("{" + tapeTM.tape.get(i) + "}", null, 40, null);
            } else {
                turtle.color(0, 0, 0); // Schwarz für alle anderen Zellen
                turtle.text(String.valueOf(tapeTM.tape.get(i)), null, 40, null);
            }
            turtle.left(90).backward(70).right(90); // Bewege die Turtle, um Platz für die nächste Zelle zu schaffen
            cellCounter++; // cellCounter wird bis zur maximalen Anzahl an Zeichen pro Zeile hochgezählt
        }
        turtle.color(0, 0, 0); // Zustände in schwarz anzeigen
        turtle.text(String.valueOf("-- " + currentState), null, 40, null); // Zustände dazu ausgeben
    }
}

/* 
Für Bespiel 1:
String initialContent = "#11000#"
int startPosition = initialContent.length() - 1
TM turingMachine = new TM(initialContent, "S", startPosition, "decrement");
turingMachine.run()

Für Bespiel 2:
String initialContent = "S010101S"
int startPosition = initialContent.length() - 2
TM turingMachine = new TM(initialContent, "S", startPosition, "moveOnes");
turingMachine.run()
*/