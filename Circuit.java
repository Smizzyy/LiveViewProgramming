import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

class Circuit {
    private Turtle turtle1;
    private String name;
    private Map<Point, Gate> gates;
    private Map<Point, Wire> wires;
    private Map<Point, Input> inputs;
    int width = 1700;
    int height = 1700;
    

    Circuit(String name) {
        this.turtle1 = new Turtle(this.width, this.height);
        this.gates = new HashMap<>();
        this.wires = new HashMap<>();
        this.inputs = new HashMap<>();
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
    
    // Schaltungfeld zeichnen
    void drawCircuitField(int rows, int cols) {
        int x = 50;
        int y = 50;
        turtle1.moveTo(75, 40).left(90).penUp().forward(10).text("Only inputs for the first column").right(90);
        turtle1.moveTo(x, y);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                turtle1.penDown();
                drawSquare();
                turtle1.penUp().forward(15).right(90).forward(10).left(180).text((i + 1) + ", " + (j + 1)).forward(10).left(90).forward(15).right(180); // jede Zelle mit der Koordinate markieren
                turtle1.penUp().forward(100); // zur Position der nächsten Zelle bewegen
            }
            turtle1.moveTo(x, y += 100);
        }
        turtle1.penUp();
    }

    // Komponente hinzufügen
    void addGate(int row, int col, Gate gate) {
        
    }

    void addWire(int row, int col, Wire wire) {
        
    }

    void addInput(int row, int col, Input input) {
        
    }
}



// Gatter mit verschiedener Logik
class Gate {
    private String type;
    private int input1;
    private int input2;
    private int output;

    Gate(String type) {
        this.type = type.toUpperCase();
    }

   


/*
    switch (this.type = type.toUpperCase()) {
        case "AND" -> input1 & input2;
        case "OR" -> input1 | input2;
        case "XOR" -> input1 ^ input2;
        case "NAND" -> ~(input1 & input2);
        case "NOR" -> ~(input1 | input2);
        case "XNOR" -> ~(input1 ^ input2);
        case "NOT" -> ~input1;
        default -> throw new IllegalArgumentException("Unbekannter Gate-Typ: " + type);
    };
    */
}

class Wire {

}

class Input {

}