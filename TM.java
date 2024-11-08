import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;

enum Move {
    LEFT,
    RIGHT
}

record Trigger(String fromState, char read) { }

record Action(char write, Move move, String toState) { }

class Tape {

    List<Character> tape;
    int headPosition;
    char defaultChar = '#';

    public Tape(String initialContent) {
        this.tape = new ArrayList<>();
        for (char c : initialContent.toCharArray()) {
            tape.add(c);
        }
        this.headPosition = 0;
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
                tape.add(defaultChar);
            } 
        } else if (direction == Move.LEFT) {
            if (headPosition == 0) {
                tape.add(0, defaultChar);
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

    public Action getAction(String state, char read) {
        return map.get(new Trigger(state, read)); 
    }
}

class TM {
    private Tape tape;
    private Table table;
    private String currentState;
    int stepCount = 0;

    public TM(String initialContent, Table table, String startState) {
        this.tape = new Tape(initialContent);
        this.table = table;
        this.currentState = startState;
    }

    public void step() {
        char currentChar = tape.read();
        Action action = table.getAction(currentState, currentChar);
        if (action != null) {
            tape.write(action.write());
            tape.move(action.move());
            currentState = action.toState();
            System.out.println(this); // Gibt den aktuellen Zustand der Maschine aus
        } else {
            currentState = "HALT";
        }
    }

    public void run() {
        while (!currentState.equals("HALT")) {
            System.out.print(stepCount + ": "); // Schrittzähler anzeigen
            System.out.println(this.toString());
            step();
            stepCount++;
            try {
                Thread.sleep(500); // Verzögerung für bessere Sichtbarkeit
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public String toString() {
        String result = "";
        // Darstellung des Bandes mit der Kopfposition direkt aus dem Tape-Objekt
        for (int i = 0; i < tape.tape.size(); i++) { // Direktzugriff auf das 'tape'-Feld
            if (i == tape.headPosition) { // Direktzugriff auf 'headPosition'
                result += "{" + tape.tape.get(i) + "}";
            } else {
                result += tape.tape.get(i);
            }
            result += " ";
        }

        // Entfernt das letzte Leerzeichen und gibt den Zustand der Maschine an
        result = result.trim() + " -- " + currentState;

        return result;
    }
}