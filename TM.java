import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

Clerk.markdown(
    Text.fillOut(
"""
# Nachbau der Turing-Maschine

""", Map.of("turtle_tree", Text.cutOut("./TM.java", "// turtle tree"),
            "tree", Text.cutOut("./TM.java", "// tree"))));

enum Move {
    LEFT,
    RIGHT
}

record Trigger(String fromState, char read) { }

record Action(char write, Move move, String toState) { }

class Tape {

    List<Character> tape;
    int headPosition;
    char defaultChar1 = '#';
    char defaultChar2 = 'S';

    public Tape(String initialContent, int startPosition) {
        this.tape = new ArrayList<>();
        for (char c : initialContent.toCharArray()) {
            tape.add(c);
        }
        if (startPosition >= 0 && startPosition < tape.size()) {
            this.headPosition = startPosition;
        }
    }
    
    char read() {
        return tape.get(headPosition);
    }

    void write(char c) {
        tape.set(headPosition, c);
    }

    void move(Move direction) {
        if (direction == Move.RIGHT) {
            headPosition++;
            if (headPosition >= tape.size()) {
                if (tape.get(headPosition) == defaultChar1) tape.add(defaultChar1);
                if (tape.get(headPosition) == defaultChar2) tape.add(defaultChar2);
            } 
        } else if (direction == Move.LEFT) {
            if (headPosition == 0) {
                if (tape.get(headPosition) == defaultChar1) tape.add(0, defaultChar1);
                if (tape.get(headPosition) == defaultChar2) tape.add(0, defaultChar2);
            } else {
                headPosition--;
            }
        }     
    }
}

class Table {
    Map<Trigger, Action> map;

    public Table() {
        this.map = new HashMap<>();
    }
    
    public void addTransition(String fromState, char read, char write, Move move, String toState) {
        Trigger trigger = new Trigger(fromState, read);
        Action action = new Action(write, move, toState);
        map.put(trigger, action); 
    }

    // Dekrementierung einer Binärzahl
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
    
    // Einsen nach rechts bewegen
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

    public Action getAction(String state, char read) {
        return map.get(new Trigger(state, read)); 
    }
}

class TM {
    private Tape tapeTM;
    private Table table;
    private String currentState;
    int stepCount = 0;
    Turtle turtle;
    

    public TM(String initialContent, String startState, int startPosition, String tableType) {
        this.tapeTM = new Tape(initialContent, startPosition);
        this.table = new Table();
        // Initialisieren der Turtle für die Live-View
        this.turtle = new Turtle(600, 100);
        
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
        
        this.currentState = startState;
    }

    public void step() {
        char currentChar = tapeTM.read();
        Action action = table.getAction(currentState, currentChar);
        if (action != null) {
            tapeTM.write(action.write());
            tapeTM.move(action.move());
            currentState = action.toState();
        } else {
            currentState = "HALT";
        }
    }

    public void run() {
        System.out.println(this.toString());
        
        while (!currentState.equals("HALT")) {
            step();
            stepCount++;
            System.out.println(this.toString());
            try {
                Thread.sleep(800); // Verzögerung für bessere Sichtbarkeit
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public String toString() {
        String result = "";  
        if (stepCount >= 10) result += stepCount + ": "; // ein Leerzeichen weniger (Optik)
        else result += stepCount + ":  ";
    
        // Darstellung des Bandes mit der Kopfposition
        for (int i = 0; i < tapeTM.tape.size(); i++) {
            if (i == tapeTM.headPosition) {
                result += "{" + tapeTM.tape.get(i) + "}";
            } else {
                result += tapeTM.tape.get(i);
            }
            result += " ";
        }
        result += " -- " + currentState;
        updateTurtleView();
        return result;
    }
    
    private void updateTurtleView() {
        turtle.reset(); // Löscht die vorherige Zeichnung und setzt die Turtle in die Mitte zurück
        int headPosition = tapeTM.headPosition; // Direktzugriff auf das Feld, falls es öffentlich ist
        turtle.penUp().backward(250).left(90);
        // Zeichne das Band mit dem Schreib/Lese-Kopf und den Nachbarzellen
        for (int i = 0; i < tapeTM.tape.size(); i++) {
            if (i == headPosition) {
                turtle.color(255, 0, 0); // rot für gelesene Zelle
                turtle.text("{" + tapeTM.tape.get(i) + "}", null, 40, null);
            } else {
                turtle.color(0, 0, 0); // schwarz
                turtle.text(String.valueOf(tapeTM.tape.get(i)), null, 40, null);
            }
            turtle.left(90).backward(70).right(90); // Bewege die Turtle, um Platz für die nächste Zelle zu schaffen
        }
    }
}

/* 
Für Bespiel 1:
String initialContent = "#11000#"
int startPosition = initialContent.length() - 1
TM turingMachine = new TM(initialContent, "S", startPosition, "decrement");

Für Bespiel 2:
String initialContent = "S010101S"
int startPosition = initialContent.length() - 2
TM turingMachine = new TM(initialContent, "S", startPosition, "moveOnes");
*/