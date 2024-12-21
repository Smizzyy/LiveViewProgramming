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
        // Anzahl Spalten und Reihen zeichnen
        turtle1.moveTo(x, 0);
        for (int i = 0; i < maxCols; i++) {
            if ((i + 1) > 1) turtle1.color(255, 0, 0).penUp().left(90).backward(35).text("     " + (i + 1), null, 25, null).forward(35).right(90).color(0, 0, 0); 
            else turtle1.color(255, 0, 0).penUp().left(90).backward(35).text("     " + (i + 1) + " (E)", null, 25, null).forward(35).right(90).color(0, 0, 0); // Eingang: Nur für Input-Objekte reserviert
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

    // Spalten hinzufügen
    void addColumn(int count) {
        if (count <= 0) throw new IllegalArgumentException("Die Anzahl der hinzuzufügenden Spalten muss positiv sein.");
    
        for (int c = 0; c < count; c++) { // füge die Spalten nacheinander hinzu
            maxCols++; // erhöhe die Anzahl der Spalten
            int x = 50 + (maxCols - 1) * 100; // X-Koordinate der neuen Spalte (am rechten Rand)
            int y = 50;
    
            // zeichne die Spaltennummer
            turtle1.moveTo(x, 0);
            turtle1.color(255, 0, 0).penUp().left(90).backward(35).text("     " + maxCols, null, 25, null).forward(35).right(90).color(0, 0, 0);
            drawPointHorizontal();
            turtle1.penUp().forward(100);
    
            // zeichne die neue Spalte
            turtle1.moveTo(x, y);
            for (int i = 0; i < maxRows; i++) {
                drawSquare();
                turtle1.moveTo(x, y += 100); // weiter zur nächsten Zelle in der Spalte
            }
        }
        System.out.println(count + " Spalte(n) wurde(n) hinzugefuegt. Gesamtspalten: " + maxCols);
    }

    // Reihen hinzufügen
    void addRow(int count) {
        if (count <= 0) throw new IllegalArgumentException("Die Anzahl der hinzuzufuegenden Spalten muss positiv sein.");
    
        for (int c = 0; c < count; c++) { // füge die Reihen nacheinander hinzu
            maxRows++; // erhöhe die Anzahl der Reihen
            int x = 50;
            int y = 50 + (maxRows - 1) * 100; // Y-Koordinate der neuen Spalte (am unteren Rand)
    
            // zeichne die Reihennummer
            turtle1.moveTo(0, y);
            turtle1.color(255, 0, 0).penUp().forward(10).left(90).backward(60).text("" + maxRows, null, 25, null).forward(60).right(90).backward(10).color(0, 0, 0);
            drawPointVertical();
            turtle1.penUp().forward(100);
    
            // zeichne die neue Reihe
            turtle1.moveTo(x, y);
            for (int i = 0; i < maxCols; i++) {
                drawSquare();
                turtle1.moveTo(x += 100, y); // weiter zur nächsten Zelle in der Reihe
            }
        }
        System.out.println(count + " Spalte(n) wurde(n) hinzugefuegt. Gesamtspalten: " + maxRows);
    }
    

    // prüft, ob man sich noch im Feld befindet
    boolean isValidPosition(int row, int col) {
        return row >= 0 && row < maxRows && col >= 0 && col < maxCols;
    }

    // Komponente hinzufügen
    void addComponent(int row, int col, T component) {
        // vorhandene Position prüfen
        if (!isValidPosition(row, col)) {
            throw new IllegalArgumentException("Ungültige Position: (" + row + ", " + col + "). Diese Position existiert nicht im Schaltungsfeld.");
        }

        // Spalte 1 auf Input-Objekte prüfen
        if (col == 1 && !(component instanceof Input)) throw new IllegalArgumentException("Spalte 1 ist nur für Input-Objekte reserviert.");

        // andere Spalten auf Input-Objekte prüfen
        if (col != 1 && (component instanceof Input)) throw new IllegalArgumentException("Input-Objekte duerfen nur in Spalte 1 hinzugefuegt werden.");
        
        Point position = new Point(col, row);

        // freie Position prüfen
        if (components.containsKey(position)) throw new IllegalArgumentException("An dieser Position existiert bereits eine Komponente!");

        // bereits existierendes Objekt prüfen
        if (components.containsValue(component)) throw new IllegalArgumentException("Das Objekt " + component + " existiert bereits im Schaltungsfeld und kann nicht erneut hinzugefügt werden.");

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
                case "AND" -> drawANDGate(gate);
                case "OR" -> drawORGate(gate);
                case "XOR" -> drawXORGate(gate);
                case "NAND" -> drawNANDGate(gate);
                case "NOR" -> drawNORGate(gate);
                case "XNOR" -> drawXNORGate(gate);
                case "NOT" -> drawNOTGate(gate);
                default -> throw new IllegalArgumentException("Unbekannter Gate-Typ: " + gate.getType());
            }
        } else if (component instanceof Input input) {
            drawInput(input);
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
                return component + " befindet sich an Position (" + position.y + ", " + position.x + ")."; // Position der Komponente gefunden
            }
        }
        return component + "wurde nicht gefunden."; // nicht gefunden
    }

    // Komponente einer Position herausfinden
    T getComponent(int row, int col) {
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

    // bei NOT, NAND, NOR und XNOR Gatter
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
    void drawANDGate(Gate gate) {
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("&", null, 13, null).backward(7).right(90);
        // Output
        turtle1.forward(31).penDown().forward(5);
        // 2 Inputs
        turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // OR-Gate zeichnen
    void drawORGate(Gate gate) {
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung
        turtle1.penUp().forward(17).left(90).backward(18).text("≥1", null, 13, null).backward(7).right(90);
        // Output
        turtle1.forward(33).penDown().forward(5);
        // 2 Inputs
        turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // XOR-Gate zeichnen
    void drawXORGate(Gate gate) {
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung
        turtle1.penUp().forward(17).left(90).backward(18).text("=1", null, 13, null).backward(7).right(90);
        // Output
        turtle1.forward(33).penDown().forward(5);
        // 2 Inputs
        turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // NAND-Gate zeichnen
    void drawNANDGate(Gate gate) {
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("&", null, 13, null).backward(7).right(90);
        // not-Output 
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).right(90);
        // 2 Inputs
        turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // NOR-Gate zeichnen
    void drawNORGate(Gate gate) {
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("≥1", null, 13, null).backward(7).right(90);
        // not-Output 
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).right(90);
        // 2 Inputs
        turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // XNOR-Gate zeichnen
    void drawXNORGate(Gate gate) {
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("=1", null, 13, null).backward(7).right(90);
        // not-Output 
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).right(90);
        // 2 Inputs
        turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
    }

    // NOT-Gate zeichnen
    void drawNOTGate(Gate gate) {
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("1", null, 13, null).backward(7).right(90);
        // not-Output 
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).right(90);
        // 1 Input
        turtle1.penUp().backward(68).penDown().backward(5).penUp();
    }

    // Input in der ersten Spalte zeichnen
    void drawInput(Input input) {
        // Benennung
        turtle1.penUp().forward(15).left(90).backward(20).text(input.getInputName(), null, 14, null).right(90).backward(15);
        // Input-Wert
        turtle1.left(90).backward(10).text("" + input.getInputValue(), null, 15, null).forward(3).right(90).forward(10);
        // Input-Objekt
        turtle1.penDown().forward(40).penUp();
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
    String getType() {
        return this.type;
    }

    // Namen abrufen
    String getName() {
        return this.name;
    }

    // Output-Wert abrufen
    int getOutputValue() {
        return this.output;
    }

    @Override
    public String toString() {
        return "Gate type: " + this.type + " named: " + this.name;
    }
}

class Input {
    String name;
    int input;

    // Konstruktor
    Input(String name, int input) {
        assert input == 1 || input == 0;
        this.name = name;
        this.input = input;
    }

    String getInputName() {
        return this.name;
    }
    int getInputValue() {
        return this.input;
    }
}

class Wire {

}

// Circuit<Object> c1 = new Circuit<>("Circ 1", 15, 10);
// Gate andGate1 = new Gate("and", "andGate1");
// c1.addComponent(2, 3, andGate1);
// c1.getPosition(andGate1);
// c1.getComponent(2, 3);
// Input input1 = new Input("x1", 1);