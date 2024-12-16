import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

class Circuit<T> {
    private String name;
    private int maxRows; 
    private int maxCols;
    private Map<Point, T> components;
    private Turtle turtle1;
    private int width = 1700;
    private int height = 1700;
    
    // Konstruktor
    Circuit(String name, int cols, int rows) {
        this.turtle1 = new Turtle(this.width, this.height);
        this.components = new HashMap<>();
        this.name = name;
        this.maxCols = cols;
        this.maxRows = rows;
        drawCircuitField();
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
    void drawCircuitField() {
        assert (maxRows > 0 && maxCols > 0); 
        int x = 50, y = 50;
        // Anzahl Spalten und zeichnen
        turtle1.moveTo(x, 0);
        for (int i = 0; i < maxCols; i++) { 
            turtle1.color(255, 0, 0).penUp().left(90).backward(35).text("     " + (i + 1), null, 25, null).forward(35).right(90).color(0, 0, 0);
            drawPointHorizontal();
            turtle1.penUp().forward(100);
        }
        turtle1.moveTo(0, y);
        for (int i = 0; i < maxRows; i++) {
            turtle1.color(255, 0, 0).penUp().forward(10).left(90).backward(60).text("" + (i + 1), null, 25, null).forward(60).right(90).backward(10).color(0, 0, 0);
            drawPointVertical();
            turtle1.penUp().right(90).forward(100).left(90);
        }
        // Feld zeichnen
        turtle1.moveTo(x, y);
        for (int i = 0; i < maxRows; i++) {
            for (int j = 0; j < maxCols; j++) {
                drawSquare();
                turtle1.penUp().forward(100); // zur Position der nächsten Zelle bewegen
            }
            turtle1.moveTo(x, y += 100);
        }
    }

    boolean isValidPosition(int row, int col) {
        return row >= 0 && row < maxRows && col >= 0 && col < maxCols;
    }

    // Komponente hinzufügen
    void addComponent(int row, int col, T component) {

        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Ungültige Position: (" + row + ", " + col + "). Diese Position existiert nicht im Schaltungsfeld.");
        }

        Point position = new Point(col, row);
        if (components.containsKey(position)) throw new IllegalArgumentException("An dieser Position existiert bereits eine Komponente!");

        // Komponente zur Map hinzufügen
        components.put(position, component);

        // Umrechnung in Pixelkoordinaten
        int cellWidth = 100, cellHeight = 100; // Breite und Höhe einer Zelle in Pixeln
        int xPixel = col * cellWidth;
        int yPixel = row * cellHeight;
        
        turtle1.moveTo(xPixel, yPixel).backward(25).left(90).forward(25).right(90); // zur Position gehen

        // Typprüfung und Zeichnen der Komponente
        if (component instanceof Gate gate) { 
            switch (gate.getType()) {
                case "AND" -> drawANDGate();
                case "OR" -> drawORGate();
                case "XOR" -> drawXORGate();
                case "NAND" -> drawNANDGate();
                case "NOR" -> drawNORGate();
                case "XNOR" -> drawXNORGate();
                case "NOT" -> drawNOTGate();
                default -> throw new IllegalArgumentException("Unbekannter Gate-Typ: " + gate.getType());
            }
        } 
        // else if Wire..., else if Input...
        else throw new IllegalArgumentException("Unbekannter Komponententyp: " + component.getClass().getSimpleName());

        System.out.println(component + " an Position (" + row + ", " + col + ") hinzugefuegt.");
    }

    // Position einer Komponente herausfinden
    String getPosition(T component) {
        for (Map.Entry<Point, T> entry : components.entrySet()) { // iteriert über alle Schlüssel-Wert-Paare
            if (entry.getValue().equals(component)) {
                Point position = entry.getKey(); 
                return component + " befindet sich an Position (" + position.x + ", " + position.y + ")."; // Position der Komponente gefunden
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

    // vertikal ausgerichtetes Rechteck
    void drawSmallSquare() {
        turtle1.penDown();
        for (int i = 0; i < 4; i++) {
            turtle1.forward(50).right(90);
        }
        turtle1.penUp();
    }

    void drawNotCircle() {
        double radius = 6;
        double stepSize = (2 * Math.PI * radius) / 360;
        turtle1.penDown();
        for (int i = 0; i < 360; i++) {
            turtle1.forward(stepSize).right(1);
        }
        turtle1.penUp();
    }

    // AND-Gate zeichnen
    void drawANDGate() {
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("&", null, 12, null).backward(7).right(90);
        // Output
        turtle1.forward(31).penDown().forward(5);
        // 2 Inputs
        turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // OR-Gate zeichnen
    void drawORGate() {
        drawSmallSquare();
        // Beschriftung
        turtle1.penUp().forward(17).left(90).backward(18).text("≥1", null, 12, null).backward(7).right(90);
        // Output
        turtle1.forward(33).penDown().forward(5);
        // 2 Inputs
        turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // XOR-Gate zeichnen
    void drawXORGate() {
        drawSmallSquare();
        // Beschriftung
        turtle1.penUp().forward(17).left(90).backward(18).text("=1", null, 12, null).backward(7).right(90);
        // Output
        turtle1.forward(33).penDown().forward(5);
        // 2 Inputs
        turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // NAND-Gate zeichnen
    void drawNANDGate() {
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("&", null, 12, null).backward(7).right(90);
        // not-Output 
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).right(90);
        // 2 Inputs
        turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // NOR-Gate zeichnen
    void drawNORGate() {
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("≥1", null, 12, null).backward(7).right(90);
        // not-Output 
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).right(90);
        // 2 Inputs
        turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // XNOR-Gate zeichnen
    void drawXNORGate() {
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("=1", null, 12, null).backward(7).right(90);
        // not-Output 
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).right(90);
        // 2 Inputs
        turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // NOT-Gate zeichnen
    void drawNOTGate() {
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("1", null, 12, null).backward(7).right(90);
        // not-Output 
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).right(90);
        // 1 Input
        turtle1.penUp().backward(68).penDown().backward(5).penUp();
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

    // Typ abrufen
    public String getType() {
        return type;
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