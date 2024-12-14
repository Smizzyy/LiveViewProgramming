import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

class Circuit<T> {
    private String name;
    private Map<Point, T> components;
    private Turtle turtle1;
    private int width = 1700;
    private int height = 1700;
    
    // Konstruktor
    Circuit(String name) {
        this.turtle1 = new Turtle(this.width, this.height);
        this.components = new HashMap<>();
        this.name = name;
    }

    // einzelenes Quadrat im Feld
    void drawSquare() {
        for (int i = 0; i < 4; i++) {
            turtle1.penDown();
            turtle1.forward(100).right(90);
            turtle1.penUp();
        }
    }

    // horizontales Rechteck
    void drawPointHorizontal() {
        for (int i = 0; i < 4; i++) {
            turtle1.penDown();
            if (i % 2 == 0) turtle1.forward(100).right(90);
            else turtle1.forward(50).right(90);
        }
    }

    // vertikales Rechteck
    void drawPointVertical() {
        for (int i = 0; i < 4; i++) {
            turtle1.penDown();
            if (i % 2 == 0) turtle1.forward(50).right(90);
            else turtle1.forward(100).right(90);
        }
    }
    
    // Schaltungfeld zeichnen
    void drawCircuitField(int cols, int rows) {
        assert (rows > 0 && cols > 0); 
        int x = 50, y = 50;
        // Anzahl Reihen und Spalten zeichnen
        turtle1.moveTo(x, 0);
        for (int i = 0; i < rows; i++) { 
            turtle1.color(255, 0, 0).penUp().left(90).backward(35).text("     " + (i + 1), null, 25, null).forward(35).right(90).color(0, 0, 0);
            drawPointHorizontal();
            turtle1.penUp().forward(100);
        }
        turtle1.moveTo(0, y);
        for (int i = 0; i < cols; i++) {
            turtle1.color(255, 0, 0).penUp().forward(10).left(90).backward(60).text("" + (i + 1), null, 25, null).forward(60).right(90).backward(10).color(0, 0, 0);
            drawPointVertical();
            turtle1.penUp().right(90).forward(100).left(90);
        }
        // Feld zeichnen
        turtle1.moveTo(x, y);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                drawSquare();
                turtle1.penUp().forward(100); // zur Position der nächsten Zelle bewegen
            }
            turtle1.moveTo(x, y += 100);
        }
    }

    // Komponente hinzufügen
    void addComponent(int col, int row, T component) {
        Point position = new Point(col, row);
        if (components.containsKey(position)) throw new IllegalArgumentException("An dieser Position existiert bereits eine Komponente!");
        else components.put(position, component);
        System.out.println(component + " an Position " + col + ", " + row + " hinzugefuegt.");
    }

    // Position einer Komponente herausfinden
    String getPosition(T component) {
        for (Map.Entry<Point, T> entry : components.entrySet()) { // iteriert über alle Schlüssel-Wert-Paare
            if (entry.getValue().equals(component)) {
                Point position = entry.getKey(); 
                return component + " befindet sich an Position " + position.x + ", " + position.y + "."; // Position der Komponente gefunden
            }
        }
        return component + "wurde nicht gefunden."; // nicht gefunden
    }

    // Komponente einer Position herausfinden
    T getComponent(int col, int row) {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Zeile und Spalte müssen positiv sein.");
        }
        Point position = new Point(col, row);
        return components.get(position);
    }
    
}

// Gatter mit verschiedener Logik
class Gate {
    private String type;
    private String name;
    private int input1;
    private int input2;
    private int output;

    // Konstruktor
    Gate(String type, String name) {
        this.type = type.toUpperCase();
        this.name = name;
        this.output = inputToOutput();
    }

    // wandelt den input je nach Logik in output um
    int inputToOutput() {
        output = switch (type) {
            case "AND" -> input1 & input2;
            case "OR" -> input1 | input2;
            case "XOR" -> input1 ^ input2;
            case "NAND" -> ~(input1 & input2);
            case "NOR" -> ~(input1 | input2);
            case "XNOR" -> ~(input1 ^ input2);
            case "NOT" -> ~input1;
            default -> throw new IllegalArgumentException("Unbekannter Gate-Typ: " + type);
        };
        return output;
    }

    @Override
    public String toString() {
        return "Gate type: " + type + " named: " + name;
    }
}

class Wire {

}

class Input {

}

// Circuit<Object> c1 = new Circuit<>("Circ 1");
// c1.drawCircuitField(15, 15);
// Gate andGate1 = new Gate("and", "andGate1");
// c1.addComponent(2, 3, andGate1);
// c1.getPosition(andGate1);
// c1.getComponent(2, 3);