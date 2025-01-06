import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Circuit<T> {
    private String name;
    private int maxRows; 
    private int maxCols;
    private Map<Point, T> components; // key: Punkt, value: Komponente 
    private Map<T, Point> firstInputPositions; // key: Komponente, value: Punkt
    private Map<T, Point> secondInputPositions; // key: Komponente, value: Punkt
    private Map<T, Point> outputPositions; // key: Komponente, value: Punkt
    private List<Connection<T>> connections; // Liste der verbundenen Komponente 
    private Turtle turtle1;
    private int width = 1700;
    private int height = 1700;
    
    // Konstruktor
    Circuit(String name, int cols, int rows) {
        this.turtle1 = new Turtle(this.width, this.height);
        this.components = new HashMap<>();
        this.firstInputPositions = new HashMap<>();
        this.secondInputPositions = new HashMap<>();
        this.outputPositions = new HashMap<>();
        this.connections = new ArrayList<>();
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

     // Typ vom Gate auwerten und zeichnen
     void selectGateAndDraw(T component) {
        if (component instanceof Gate gate) {
            switch (gate.getType()) {
                case "AND" -> drawANDGate(component);
                case "OR" -> drawORGate(component);
                case "XOR" -> drawXORGate(component);
                case "NAND" -> drawNANDGate(component);
                case "NOR" -> drawNORGate(component);
                case "XNOR" -> drawXNORGate(component);
                case "NOT" -> drawNOTGate(component);
                default -> throw new IllegalArgumentException("Unbekannter Gate-Typ: " + gate.getType());
            }
        }
    }

    // Komponente hinzufügen
    void addComponent(int row, int col, T component) {
        // vorhandene Position prüfen
        if (!isValidPosition(row, col)) throw new IllegalArgumentException("Ungültige Position: (" + row + ", " + col + "). Diese Position existiert nicht im Schaltungsfeld.");
        
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
        
        turtle1.moveTo(getPixel(col), getPixel(row)).backward(25).left(90).forward(25).right(90); // zur Position gehen

        // Typprüfung und Zeichnen der Komponente
        if (component instanceof Gate) selectGateAndDraw(component);
        else if (component instanceof Input) drawInput(component);
        // else if Wire...
        else throw new IllegalArgumentException("Unbekannter Komponententyp: " + component.getClass().getSimpleName());

        System.out.println(component + " an Position (" + row + ", " + col + ") hinzugefuegt.");
    }

    // Komponente hinzufügen ohne Überprüfung, um dynamisch zeichnen zu können
    void addComponentWithoutChecks(int row, int col, T component) {
        Point position = new Point(col, row);

        // Komponente zur Map hinzufügen
        components.put(position, component);
        
        turtle1.moveTo(getPixel(col), getPixel(row)).backward(25).left(90).forward(25).right(90); // zur Position gehen

        // Typprüfung und Zeichnen der Komponente
        if (component instanceof Gate) selectGateAndDraw(component); 
        else if (component instanceof Input) drawInput(component);
        // else if Wire..., else if Input...
        else throw new IllegalArgumentException("Unbekannter Komponententyp: " + component.getClass().getSimpleName());

        System.out.println(component + " an Position (" + row + ", " + col + ") hinzugefuegt.");
    }

    // Position in Pixelgröße umrechnen
    int getPixel (int colOrRow) {
        int cellWidthOrHeight = 100;
        int pixel = colOrRow * cellWidthOrHeight;
        return pixel;
    }

    // Komponente verbinden
    void connectComponents(T sourceComponent, T destinationComponent, int inputNumber) {
        // Position prüfen
        if (!isValidConnection(sourceComponent, destinationComponent)) return;
        
        if (inputNumber != 1 && inputNumber != 2) throw new IllegalArgumentException("Es gibt nur zwei Eingänge. Bitte nur Eingang 1 oder 2 auswaehlen.");
        
        Point sourceOutput = outputPositions.get(sourceComponent);
        Point destInput = (inputNumber == 1) ? firstInputPositions.get(destinationComponent) : secondInputPositions.get(destinationComponent);

        if (sourceOutput == null || destInput == null) throw new IllegalArgumentException("Verbindung nicht möglich. Komponentenposition nicht gefunden.");

        // doppelte Verbindungen vermeiden
        if (isConnectionPresent(sourceComponent, destinationComponent, inputNumber)) throw new IllegalArgumentException("Verbindung existiert bereits: " + sourceComponent + " -> " + destinationComponent + " (Eingang " + inputNumber + ")");

        // Verbindung in die Liste eintragen
        connections.add(new Connection<>(sourceComponent, destinationComponent, inputNumber));
        System.out.println("Verbindung hinzugefügt: " + sourceComponent + " -> " + destinationComponent + " (Eingang " + inputNumber + ")");

        turtle1.moveTo(sourceOutput.x, sourceOutput.y);
        drawConnection(sourceOutput, destInput, inputNumber, destinationComponent);
    
    }

    void drawConnection(Point sourceOutput, Point destInput, T source1, T source2, T destinationComponent) {
        int startX = sourceOutput.x;
        int startY = sourceOutput.y;
        int endX = destInput.x;
        int endY = destInput.y;
        int offset = 5; // Versetzung

        // zähle die Anzahl der Verbindungen zu dieser Zielkomponente
        long existingConnections = connections.stream()
            .filter(conn -> conn.destination.equals(destinationComponent))
            .count();
        existingConnections = (int) existingConnections;


        // Startposition
        turtle1.moveTo(startX, startY).penDown();

        // Kabel nach rechts zeichnen
        while (startX != endX) {
            startX += 1; 
            turtle1.forward(1);
        }

        // Kabel nach oben oder unten zeichnen
        if (startY < endY) turtle1.right(90);
        else if (startY > endY) turtle1.left(90);
        
        while (startY != endY) {
            startY += 1; 
            turtle1.forward(1);
        }
    }

    // prüfen, ob beide Quellen oberhalb oder unterhalb sind 
    boolean checkSourcesPositions(T source1, T source2, T destination) {
        Point source1Position = outputPositions.get(source1);
        Point source2Position = outputPositions.get(source2);
        Point destinationPosition = null;

        for (Map.Entry<Point, T> entry : components.entrySet()) {
            if (entry.getValue().equals(destination)) {
                destinationPosition = entry.getKey();  // Position gefunden
                break;
            }
        }

        if (destinationPosition == null || source1Position == null || source2Position == null) throw new IllegalArgumentException("Komponentenposition nicht gefunden.");

        boolean bothAbove = source1Position.y < destinationPosition.y && source2Position.y < destinationPosition.y;
        boolean bothBelow = source1Position.y > destinationPosition.y && source2Position.y > destinationPosition.y;

        return bothAbove || bothBelow;
    }

    // alle Verbindungen ausgeben
    void printConnections() {
        if (connections.isEmpty()) {
            System.out.println("Keine Verbindungen vorhanden.");
        } else {
            System.out.println("Aktuelle Verbindungen:");
            for (Connection<T> connection : connections) {
                System.out.println(connection);
            }
        }
    }

    // prüft, ob schon eine Verbindung in der Liste vorhanden ist
    boolean isConnectionPresent(T source, T destination, int inputNumber) {
        return connections.stream()
        .anyMatch(conn -> conn.source.equals(source) &&
                          conn.destination.equals(destination) &&
                          conn.inputNumber == inputNumber);
    }

    // prüft, ob beide Eingänge von einem Gatter mit einem Kabel verbunden sind 
    boolean areBothInputsConnected(T destinationComponent) {
        // prüfe Input 1
        boolean input1connected = connections.stream().
            anyMatch(conn -> conn.destination.equals(destinationComponent) && conn.inputNumber == 1);
        // prüfe Input 2
        boolean input2connected = connections.stream().
            anyMatch(conn -> conn.destination.equals(destinationComponent) && conn.inputNumber == 2);
            
        return input1connected && input2connected;    
    } 
    
    // Position der Komponente prüfen
    boolean isValidConnection(T source, T destination) {
        Point sourcePosition = null, destPosition = null;
    
        // Positionen der Komponenten aus der Map holen
        for (Map.Entry<Point, T> entry : components.entrySet()) {
            if (entry.getValue().equals(source)) sourcePosition = entry.getKey();
            if (entry.getValue().equals(destination)) destPosition = entry.getKey();
        }
    
        // falls eine der Komponenten nicht gefunden wird
        if (sourcePosition == null || destPosition == null) throw new IllegalArgumentException("Komponente nicht gefunden.");
    
        int sourceCol = sourcePosition.x;
        int destCol = destPosition.x;
    
        // gleiche Spalte verhindern
        if (sourceCol == destCol) {
            System.out.println("Verbindung nicht möglich: Komponenten befinden sich in der gleichen Spalte.");
            return false;
        }
    
        // Ziel darf nicht links von der Quelle sein
        if (destCol < sourceCol) {
            System.out.println("Verbindung nicht möglich: Zielkomponente liegt links von der Quellkomponente.");
            return false;
        }
    
        // Verbindung ist gültig
        return true;
    }    

    // Eingänge schalten
    int setInput(T component, int value) {
        if (value != 0 && value != 1) throw new IllegalArgumentException("Bitte nur 1 oder 0 schalten.");
        if (component instanceof Input inputComponent) {
            if (inputComponent.input != value) {
                inputComponent.input = value; // Eingang wird umgeschaltet
                System.out.println(inputComponent.getInputName() + " wurde auf " + inputComponent.input + " geschaltet."); 
                drawNewCircuit();
                return inputComponent.getInputValue();
            } else throw new IllegalArgumentException("Dieser Einagng hat schon den Wert " + inputComponent.input);
            
        }
        throw new IllegalArgumentException("Fehler: Nicht kompatible Komponente. Bitte Input-Komponente angeben.");
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

    // Pixel von X einer Komponente abrufen
    int getPixelPositionX(T component) {
        for (Map.Entry<Point, T> entry : components.entrySet()) { 
            if (entry.getValue().equals(component)) {
                Point position = entry.getKey();
                int xPixel =  position.x * 100; // Umrechnung in Pixel
                return xPixel; 
            }
        }
        return -1; 
    }

    // Pixel von Y einer Komponente abrufen
    int getPixelPositionY(T component) {
        for (Map.Entry<Point, T> entry : components.entrySet()) { 
            if (entry.getValue().equals(component)) {
                Point position = entry.getKey();
                int yPixel =  position.y * 100; // Umrechnung in Pixel 
                return yPixel;
            }
        }
        return -1;
    }

    // Komponente einer Position herausfinden
    T getComponent(int row, int col) {
        if (row < 0 || col < 0) {
            throw new IllegalArgumentException("Zeile und Spalte müssen positiv sein.");
        }
        Point position = new Point(col, row);
        return components.get(position);
    }

    // alles wieder neu zeichnen 
    void drawNewCircuit() {
        turtle1.reset();
        drawCircuitField();
        for (Map.Entry<Point, T> entry : components.entrySet()) {
            Point position = entry.getKey();
            T component = entry.getValue(); 
            addComponentWithoutChecks(position.y, position.x, component);
        }
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

    // Koordinaten des Ausgangs und der Eingänge abspeichern
    void saveEndingsOfGate(int x, int y, T component) {
        x += 30;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).forward(30); // Test
        Point point = new Point(x, y);
        outputPositions.put(component, point); // die Koordinaten vom Ausgang gespeichert
        System.out.println("Koordinaten Ausgang: x = " + x + ", y = " + y);
        x -= 60;
        y -= 15;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).backward(30); // Test
        point = new Point(x, y);
        outputPositions.put(component, point); // die Koordinaten vom oberen Eingang gespeichert
        System.out.println("Koordinaten oberen Eingang: x = " + x + ", y = " + y);
        y += 30;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).backward(30); // Test
        point = new Point(x, y);
        outputPositions.put(component, point); // die Koordinaten vom unteren Eingang gespeichert
        System.out.println("Koordinaten unteren Eingang: x = " + x + ", y = " + y);
    }

    void saveEndingsOfnGate(int x, int y, T component) {
        x += 44;
        y += 1;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).forward(30); // Test
        Point point = new Point(x, y);
        outputPositions.put(component, point); // die Koordinaten vom Ausgang gespeichert
        System.out.println("Koordinaten Ausgang: x = " + x + ", y = " + y);
        x -= 74;
        y -= 16;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).backward(30); // Test
        point = new Point(x, y);
        firstInputPositions.put(component, point); // Koordinaten vom oberen Eingang gespeichert
        System.out.println("Koordinaten oberen Eingang: x = " + x + ", y = " + y);
        y += 30;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).backward(30); // Test
        point = new Point(x, y);
        secondInputPositions.put(component, point); // Koordinaten vom unteren Eingang gespeichert
        System.out.println("Koordinaten unteren Eingang: x = " + x + ", y = " + y);
    }

    void saveEndingsOfNot(int x, int y, T component) {
        x += 44;
        y += 1;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).forward(30); // Test
        Point point = new Point(x, y);
        outputPositions.put(component, point); // die Koordinaten vom Ausgang gespeichert
        System.out.println("Koordinaten Ausgang: x = " + x + ", y = " + y);
        x -= 74;
        y -= 1;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).backward(30); // Test
        point = new Point(x, y);
        firstInputPositions.put(component, point); // Koordinaten vom Eingang gespeichert
        System.out.println("Koordinaten Eingang: x = " + x + ", y = " + y);
    }

    void saveEndingOfInput(int x, int y, T component) {
        x += 25;
        y += 2;
        // turtle1.moveTo(x, y).penDown().forward(30); // Test
        Point point = new Point(x, y);
        outputPositions.put(component, point); // die Koordinaten vom Ende des Input-Objekts gespeichert
        System.out.println("Koordinaten Ausgang x = " + x + ", y = " + y);
    }

    // AND-Gate zeichnen
    void drawANDGate(T component) {
        if (!(component instanceof Gate gate)) {
            throw new IllegalArgumentException("Komponente ist kein Input.");
        }
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("&", null, 13, null).backward(7).right(90);
        // Output
        turtle1.forward(31).penDown().forward(5);
        // 2 Inputs
        turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfGate(x, y, component);
    }

    // OR-Gate zeichnen
    void drawORGate(T component) {
        if (!(component instanceof Gate gate)) {
            throw new IllegalArgumentException("Komponente ist kein Input.");
        }
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung
        turtle1.penUp().forward(17).left(90).backward(18).text("≥1", null, 13, null).backward(7).right(90);
        // Output
        turtle1.forward(33).penDown().forward(5);
        // 2 Inputs
        turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfGate(x, y, component);
    }

    // XOR-Gate zeichnen
    void drawXORGate(T component) {
        if (!(component instanceof Gate gate)) {
            throw new IllegalArgumentException("Komponente ist kein Input.");
        }
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung
        turtle1.penUp().forward(17).left(90).backward(18).text("=1", null, 13, null).backward(7).right(90);
        // Output
        turtle1.forward(33).penDown().forward(5);
        // 2 Inputs
        turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp().backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfGate(x, y, component);
    }

    // NAND-Gate zeichnen
    void drawNANDGate(T component) {
        if (!(component instanceof Gate gate)) {
            throw new IllegalArgumentException("Komponente ist kein Input.");
        }
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
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
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfnGate(x, y, component);
    }

    // NOR-Gate zeichnen
    void drawNORGate(T component) {
        if (!(component instanceof Gate gate)) {
            throw new IllegalArgumentException("Komponente ist kein Input.");
        }
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
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
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfnGate(x, y, component);
    }

    // XNOR-Gate zeichnen
    void drawXNORGate(T component) {
        if (!(component instanceof Gate gate)) {
            throw new IllegalArgumentException("Komponente ist kein Input.");
        }
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
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
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfnGate(x, y, component);
    }

    // NOT-Gate zeichnen
    void drawNOTGate(T component) {
        if (!(component instanceof Gate gate)) {
            throw new IllegalArgumentException("Komponente ist kein Input.");
        }
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text(" 1", null, 13, null).backward(7).right(90);
        // not-Output 
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).right(90);
        // 1 Input
        turtle1.penUp().backward(68).penDown().backward(5).penUp();
        // Koordinaten des Ein- und Ausgangs abspeichern
        saveEndingsOfNot(x, y, component);
    }

    // Input in der ersten Spalte zeichnen
    void drawInput(T component) { 
        if (!(component instanceof Input input)) {
            throw new IllegalArgumentException("Komponente ist kein Input.");
        }
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().forward(15).left(90).backward(20).text(input.getInputName(), null, 14, null).right(90).backward(15);
        if (input.getInputValue() == 1) {
            // Input-Wert bei 1 grün
            turtle1.left(90).backward(10).color(0, 255, 0).text("" + input.getInputValue(), null, 15, null).forward(3).right(90).forward(10);
            // Input-Objekt bei 1 grün
            turtle1.penDown().forward(40).penUp().color(0, 0, 0);
        } else {
            // Input-Wert
            turtle1.left(90).backward(10).text("" + input.getInputValue(), null, 15, null).forward(3).right(90).forward(10);
            // Input-Objekt
            turtle1.penDown().forward(40).penUp(); 
        }
        saveEndingOfInput(x, y, component);
    }
}

// Gatter mit verschiedener Logik
class Gate {
    private String type;
    private String name;
    private int input1;
    private int input2;
    private int output;
    Point input1Position;
    Point input2Position;
    Point outputPosition;

    // Konstruktor
    Gate(String type, String name) {
        this.type = type.toUpperCase();
        this.name = name;
        this.output = inputToOutput();
    }

    // wandelt den input je nach Logik in output um
    int inputToOutput() {
        output = switch (type) {
            case "AND" -> this.input1 & this.input2;
            case "OR" -> this.input1 | this.input2;
            case "XOR" -> this.input1 ^ this.input2;
            case "NAND" -> ~(this.input1 & this.input2);
            case "NOR" -> ~(this.input1 | this.input2);
            case "XNOR" -> ~(this.input1 ^ this.input2);
            case "NOT" -> ~this.input1;
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

    // ersten Input-Wert abrufen
    int getInputValue1() {
        return this.input1;
    }

    // zweiten Input-Wert abrufen
    int getInputValue2() {
        return this.input2;
    }

    @Override
    public String toString() {
        return "Gate type: " + this.type + " named: " + this.name;
    }
}

// Eingänge zum Schalten
class Input {
    String name;
    int input;

    // Konstruktor mit direkter Input-Eingabe
    Input(String name, int input) {
        assert input == 1 || input == 0;
        this.name = name;
        this.input = input;
    }

    // Konstruktor ohne Input-Eingabe
    Input (String name) {
        this.name = name;
        this.input = 0;
    }

    // Nanme für die Zuordnung
    String getInputName() {
        return this.name;
    }

    // geschaltenen Wert abrufen
    int getInputValue() {
        return this.input;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

// Verbindungen
class Connection<T> {
    T source;
    T destination;
    int inputNumber;

    Connection(T source, T destination, int inputNumber) {
        this.source = source;
        this.destination = destination;
        this.inputNumber = inputNumber;
    }

    @Override
    public String toString() {
        return source + " -> " + destination + " (Eingang " + inputNumber + ")";
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
// c1.addComponent(2, 1, input1);


/*
Circuit<Object> c1 = new Circuit<>("Circ 1", 15, 10);
Input input1 = new Input("x1", 1);
c1.addComponent(1, 1, input1);
Gate xnorGate1 = new Gate("xnor", "xnorGate1");
c1.addComponent(2, 3, xnorGate1);
c1.connectComponents(input1, xnorGate1, 1);
*/