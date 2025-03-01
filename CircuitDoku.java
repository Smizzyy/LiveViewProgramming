import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.awt.Rectangle;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

Clerk.markdown(
    Text.fillOut(
"""
# Simulator für digitale Schaltungen
## *Vorname: Alexander*
## *Nachname: Schmidt*
## *Matrikelnummer: 5502046*

## Zweck der Anwendung
Der Simulator für digitale Schaltungen soll Nutzern ermöglichen, digitale Schaltkreise virtuell zu testen und zu analysieren. 
Die Anwendung zielt darauf ab, ein Werkzeug zur Verfügung zu stellen, mit dem man etwas komplexe Schaltungen ohne physische Bauteile mit Hilfe der Turtle konstruieren und verstehen kann.

## Wichitig: Das Porgramm wird nur mit der jshell gesteuert. Wenn beim ersten Versuch des Öffnens des Programmes ein Fehler auftritt, einfach noch mal öffnen, dann sollte es immer funktionieren.

# Wahrheitstabellen für Gattertypen

## AND-Gatter
| Input 1 | Input 2 | Output |
|---------|---------|--------|
|    0    |    0    |   0    |
|    0    |    1    |   0    |
|    1    |    0    |   0    |
|    1    |    1    |   1    |

## OR-Gatter
| Input 1 | Input 2 | Output |
|---------|---------|--------|
|    0    |    0    |   0    |
|    0    |    1    |   1    |
|    1    |    0    |   1    |
|    1    |    1    |   1    |

## XOR-Gatter
| Input 1 | Input 2 | Output |
|---------|---------|--------|
|    0    |    0    |   0    |
|    0    |    1    |   1    |
|    1    |    0    |   1    |
|    1    |    1    |   0    |

## NAND-Gatter
| Input 1 | Input 2 | Output |
|---------|---------|--------|
|    0    |    0    |   1    |
|    0    |    1    |   1    |
|    1    |    0    |   1    |
|    1    |    1    |   0    |

## NOR-Gatter
| Input 1 | Input 2 | Output |
|---------|---------|--------|
|    0    |    0    |   1    |
|    0    |    1    |   0    |
|    1    |    0    |   0    |
|    1    |    1    |   0    |

## XNOR-Gatter
| Input 1 | Input 2 | Output |
|---------|---------|--------|
|    0    |    0    |   1    |
|    0    |    1    |   0    |
|    1    |    0    |   0    |
|    1    |    1    |   1    |

## NOT-Gatter
| Input | Output |
|-------|--------|
|   0   |   1    |
|   1   |   0    |


# Dokumentation 
# Szenario 1 - Grundlegende Gatter

## Ziel
Ein Benutzer soll verschiedene grundlegende Gatter (NOT, AND, OR, NAND, NOR, XOR, XNOR) gemäß der IEC-Norm erstellen und deren Funktionsweise testen können. Zum Beispiel kann ein Benutzer ein AND-Gatter mit zwei Eingängen erstellen und die Logik überprüfen.

### 1. **Gatter und Eingänge erstellen**

#### Konstruktoren:
- **`Input`**: Erstellt einen Eingang mit einem bestimmten Namen und optional einem Anfangswert (0 oder 1).
  ```java
  Input(String name, int inputValue)
  Input(String name) // inputValue = 0
  ```
  **Beispiel:**
  ```java
  Input x1 = new Input("x1", 1);
  Input x2 = new Input("x2", 0);
  ```

- **`Gate`**: Erstellt ein Gatter mit einem Typ (z. B. AND, OR) und einem Namen. Jedes Gatter (außer NOT) hat zwei Eingänge.
  ```java
  Gate(String type, String name)
  ```
  **Beispiel:**
  ```java
  Gate andGate = new Gate("AND", "andGate1");
  Gate orGate = new Gate("OR", "orGate1");
  ```

### 2. **Komponenten hinzufügen**

Die Methode `addComponent` fügt eine Komponente (z. B. ein Gatter oder einen Eingang) an eine bestimmte Position im Schaltungsfeld hinzu. Dabei ist zu beachten, dass Eingänge in die erste Spalte und Gatter nicht in die erste Spalte hinzugefügt werden können.
```java
${add}
```
**Beispiel:**
```java
Circuit<Object> c1 = new Circuit<>("Schaltkreis 1", 10, 5); // 10 Zeilen und 5 Spalten 
c1.addComponent(2, 1, x1); // x1 in Zeile 2, Spalte 1 hinzufügen
c1.addComponent(3, 1, x2);
c1.addComponent(2, 3, andGate1);
```
#### Herausforderungen:
- **Überprüfung von Positionen:** Es muss gewährleistet sein, dass die Komponente in eine gültige Position eingefügt wird (z. B. Eingänge nur in Spalte 1).
- **Prüfung auf Überschneidungen:** Stellen Sie sicher, dass an einer Position keine andere Komponente oder Kabel bereits existiert.
- **Unterstützung unterschiedlicher Typen:** Die Methode muss sowohl mit `Input`- als auch mit `Gate`-Objekten umgehen können. Dies erfordert Typprüfungen und geeignete Zeichnungslogik für die Turtle.

### 3. **Komponenten verbinden**

Die Methode `connectComponents` verbindet die Ausgänge einer Quelle mit den Eingängen eines Ziels.
```java
${connect}
```
**Beispiel:**
```java
c1.connectComponents(x1, andGate, 1); // x1 mit Eingang 1 von andGate verbinden (1 = oberer Eingang des Gatters)
c1.connectComponents(x2, andGate, 2); // x2 mit Eingang 2 von andGate verbinden (2 = unterer Eingang des Gatters)
```
#### Herausforderungen:
- **Verbindung zwischen Quellen und Zielen:** Sicherstellen, dass die Verbindung nur dann erstellt wird, wenn die Quelle und das Ziel in der Schaltung vorhanden sind.
- **Gattertyp beachten:** Nur zulässige Verbindungen (z. B. zwei Eingänge bei `AND`-Gattern) dürfen hergestellt werden.
- **Fehlerhafte Verbindungen vermeiden:** Das Ziel darf nicht links von der Quelle liegen oder in derselben Spalte.
- **Offset bei Verbindung:** Wenn bereits eine Verbindung zu einer Zielkomponente besteht, muss beim Zeichnen der Verbindung ein "Offset" angewendet werden, um Überlappungen der Kabeln zu vermeiden. 
- **Umzeichnen bei Hindernissen:** Falls sich ein Gatter in derselben Zeile zwischen der Quelle und dem Ziel befindet, muss die Verbindung so angepasst werden, dass sie um das Hindernis herumgeführt wird (z. B. durch Umleitung oberhalb oder unterhalb des Hindernisses).
- **Farbprüfung der Verbindung:** Jede Verbindung muss abhängig vom Logikwert korrekt gezeichnet werden:
  - **Grün (HIGH / 1):** Wenn der Ausgang der Quelle den Wert 1 hat.
  - **Schwarz (LOW / 0):** Wenn der Ausgang der Quelle den Wert 0 hat.

### 4. **Eingang setzen**

Die Methode `setInput` ändert den Wert eines Eingangs und aktualisiert automatisch die gesamte Schaltung. Nach dem Setzen wird die Schaltung sofort neu ausgewertet und gezeichnet.
```java
${setInput}
```
**Beispiel:**
```java
c1.setInput(x1, 1); // x1 auf HIGH (1) setzen
c1.setInput(x2, 0); // x2 auf LOW (0) setzen
```
#### Herausforderungen:
- **Echtzeitaktualisierung der Schaltung:** Nach dem Schalten eines Eingangs muss die gesamte Schaltung neu ausgewertet und gezeichnet werden.
- **Logikfehler vermeiden:** Die Eingaben müssen korrekt auf 0 oder 1 begrenzt sein.

### 5. **Schaltung auswerten**

Die Methode `evaluateCircuit` berechnet die Logik für alle verbundenen Komponenten basierend auf den Eingabewerten. Diese Methode wird immer automatisch aufgerufen, sobald ein Eingang geschalten wird oder man Komponente verbindet. 
```java
${evaluate}
```
#### Herausforderungen:
- **Iterative Auswertung:** Jede Komponente muss in der richtigen Reihenfolge ausgewertet werden, abhängig davon, welche Verbindungen existieren.
- **Änderungen verfolgen:** Wenn sich ein Ausgang ändert, muss die Änderung korrekt in der Konsole angezeigt werden.
- **Rekursivität vermeiden:** Es muss verhindert werden, dass Schleifen in den Verbindungen zu endlosen Auswertungen führen.

### 6. **Schaltung dynamisch halten**

Die Methode `drawNewCircuit` zeichnet die gesamte Schaltung auf dem Feld neu. Diese Methode wird immer automatisch aufgerufen, sobald die Schaltung neu gezeichnet werden muss, um Veränderungen optisch in der Turtle zu sehen. 
```java
${drawNew}
```
#### Herausforderungen:
- **Vollständige Aktualisierung:** Alle Verbindungen und Komponenten müssen erneut gezeichnet werden.

## Beispielablauf: Mit einem AND-Gatter und zwei Eingängen 
### jshell:
```java
// Schaltkreis erstellen
Circuit<Object> c1 = new Circuit<>("Schaltkreis 1", 10, 5);

// Eingänge erstellen
Input x1 = new Input("x1", 1);
Input x2 = new Input("x2", 0);

// Gatter erstellen
Gate andGate = new Gate("AND", "andGate");

// Komponenten hinzufügen
c1.addComponent(2, 1, x1);
c1.addComponent(3, 1, x2);
c1.addComponent(2, 3, andGate);

// Komponenten verbinden
c1.connectComponents(x1, andGate, 1);
c1.connectComponents(x2, andGate, 2);

// Eingang schalten
c1.setInput(x1, 1); 
c1.setInput(x2, 1); 
```
### Turtle:
""", Map.of("add", Text.cutOut("./CircuitDoku.java", "// add"),
            "connect", Text.cutOut("./CircuitDoku.java", "// connect"),
            "setInput", Text.cutOut("./CircuitDoku.java", "// setInput"),
            "evaluate", Text.cutOut("./CircuitDoku.java", "// evaluate"),
            "drawNew", Text.cutOut("./CircuitDoku.java", "// drawNew"))));

turtle1.reset();
Circuit<Object> c1 = new Circuit<>("Schaltkreis 1", 10, 5);
c1.deleteCircuit();   
Input x1 = new Input("x1", 1);
Input x2 = new Input("x2", 0);
Gate andGate = new Gate("AND", "andGate");
c1.addComponent(2, 1, x1);
c1.addComponent(3, 1, x2);
c1.addComponent(2, 3, andGate);
c1.connectComponents(x1, andGate, 1);
c1.connectComponents(x2, andGate, 2);
c1.setInput(x1, 1);
c1.setInput(x2, 1);           

Clerk.markdown("""
---
# Szenario 2 - Schaltungsaufbau
## Ziel 
in Benutzer erstellt eine Schaltung, die verschiedene Gatter miteinander verschaltet.
Zum Beispiel: Der Benutzer kann aus NOT- und NAND-Gatter eine AND- oder
OR-Schaltung bauen und die Ausgabe auslesen.

## Beispielablauf: AND-Schaltung aus NOT und NAND
## jshell:
```java
// Schaltkreis erstellen
Circuit<Object> c1 = new Circuit<>("Schaltkreis 1", 10, 5);

// Eingänge erstellen
Input x1 = new Input("x1", 1);
Input x2 = new Input("x2", 1);

// Gatter erstellen
Gate notGate1 = new Gate("NOT", "notGate1");
Gate notGate2 = new Gate("NOT", "notGate2");
Gate nandGate = new Gate("NAND", "nandGate");

// Komponenten hinzufügen
c1.addComponent(2, 1, x1);
c1.addComponent(3, 1, x2);
c1.addComponent(2, 2, notGate1);
c1.addComponent(3, 2, notGate2);
c1.addComponent(3, 3, nandGate);

// Komponenten verbinden
c1.connectComponents(x1, notGate1, 1); 
c1.connectComponents(x2, notGate2, 1);
c1.connectComponents(notGate1, nandGate, 1);
c1.connectComponents(notGate2, nandGate, 2);

// Eingänge schalten
c1.setInput(x1, 1); 
c1.setInput(x2, 1);
```
### Turtle:
""");
turtle1.reset();
Circuit<Object> c1 = new Circuit<>("Schaltkreis 1", 10, 5);
c1.deleteCircuit(); 
Input x1 = new Input("x1", 1);
Input x2 = new Input("x2", 1);
Gate notGate1 = new Gate("NOT", "notGate1");
Gate notGate2 = new Gate("NOT", "notGate2");
Gate nandGate = new Gate("NAND", "nandGate");
c1.addComponent(2, 1, x1);
c1.addComponent(3, 1, x2);
c1.addComponent(2, 2, notGate1);
c1.addComponent(3, 2, notGate2);
c1.addComponent(3, 3, nandGate);
c1.connectComponents(x1, notGate1, 1); 
c1.connectComponents(x2, notGate2, 1);
c1.connectComponents(notGate1, nandGate, 1);
c1.connectComponents(notGate2, nandGate, 2);
c1.setInput(x1, 1); 
c1.setInput(x2, 1);

Clerk.markdown(
    Text.fillOut(
"""
---
# Szenario 3 - Schaltungsanalyse
## Ziel 
3.3. Szenario 3 - Schaltungsanalyse
Ein Benutzer erstellt eine komplexere Schaltung, die er daraufhin analysieren kann. Dies hilft einem, die einzelnen Zwischenschritte der Schaltung zu verstehen.
Zum Beispiel: Der Benutzer kann sich eine Tabelle erzeugen lassen, mit allen kombinierten HIGH/LOW Eingängen, Zwischenausgaben bzw. Zwischenschritte und Endausgaben.

## Wichtige Methoden

### 1. **Gesamte Wahrheitstabelle zeichnen**
Die Methode `drawTable` erstellt die gesamte Wahrheitstabelle der Schaltung, indem sie das Tabellenlayout in einer neuen Turtle zeichnet, die Eingabekombinationen und die Ergebnisse aller Gatter in die Tabelle einträgt.

```java
${drawTable}
```

#### Ablauf:
1. **Tabellenlayout zeichnen:** Die Methode `drawTableFrame` zeichnet das Gitter der Tabelle basierend auf der Anzahl der Eingänge und Ausgänge.
2. **Eingabekombinationen einfügen:** Die Methode `drawInputValuesInTable` generiert alle möglichen HIGH/LOW-Eingangskombinationen und trägt sie ein.
3. **Gatterausgaben einfügen:** Die Methode `drawOuputValuesInTable` analysiert die Ergebnisse der Gatter und fügt diese der Tabelle hinzu.

### 2. **Tabellenlayout zeichnen**

Die Methode `drawTableFrame` erstellt das Layout der Tabelle. Sie berechnet die benötigte Höhe basierend auf der Anzahl der Eingänge und Ausgänge und zeichnet die Gitterlinien.

```java
${tableFrame}
```

#### Herausforderungen:
- **Berechnung der Tabellenhöhe:** Die Höhe der Tabelle wird dynamisch anhand der Anzahl der Eingangskombinationen berechnet.
- **Begrenzung der Ausgänge:** Maximal sechs Ausgänge sind erlaubt. Bei mehr Ausgängen wird eine Ausnahme ausgelöst.
- **Einteilung in Eingangs- und Ausgangsbereich:** Die Tabelle wird in einen Bereich für Eingabekombinationen und einen Bereich für Gatterausgaben unterteilt.

### 3. **Eingabekombinationen schreiben**

Die Methode `drawInputValuesInTable` generiert alle möglichen Kombinationen von HIGH/LOW für die Eingänge und trägt sie in die Tabelle ein.

```java
${inputValues}
```

#### Herausforderungen:
- **Generierung aller Kombinationen:** Mithilfe der Methode `generateInputCombinations` wird eine Liste aller möglichen HIGH/LOW-Kombinationen erstellt.
- **Platzierung der Werte:** Die Werte werden korrekt in die Zellen der Tabelle geschrieben.
- **Flexibilität:** Die Methode funktioniert dynamisch für beliebige Eingangsanzahlen.

### 4. **Gatterausgaben schreiben**

Die Methode `drawOuputValuesInTable` berechnet die Logik der Gatter für alle Eingangskombinationen und trägt die Ergebnisse in die Tabelle ein.

```java
${outputValues}
```

#### Herausforderungen:
- **Logikauswertung:** Die Methode `evaluateLogicForAllInputs` berechnet die Ergebnisse aller Gatter basierend auf den Eingangskombinationen.
- **Sortierung der Gatter:** Mithilfe der Methode `getSortedOutputs` werden die Gatter so sortiert, dass sie in der richtigen Reihenfolge ausgewertet werden.
- **Platzierung der Ausgaben:** Die Ergebnisse der Gatter werden dynamisch in die Tabelle eingetragen.

### 5. **Generierung der Wahrheitstabelle für alle Eingänge**

Die Methode `generateInputCombinations` erstellt eine Liste aller möglichen HIGH/LOW-Kombinationen für die Eingänge.

```java
${evalInputs}
```

#### Ablauf:
1. **Berechnung der Kombinationen:** Es werden `2^numberOfInputs` Kombinationen generiert.
2. **Bitweise Generierung:** Für jede Kombination wird geprüft, ob ein Bit HIGH oder LOW ist.

### 6. **Logik aller Ausgänge bewerten**

Die Methode `evaluateLogicForAllInputs` berechnet für jede Eingabekombination die Ausgänge aller Gatter.

```java
${evalOutputs}
```

#### Ablauf:
1. **Setzen der Eingänge:** Die Werte aller Eingänge werden basierend auf der aktuellen Kombination gesetzt.
2. **Schaltung auswerten:** Die Methode `evaluateCircuit` berechnet die Ausgänge aller Gatter.
3. **Ergebnisse speichern:** Die Ausgänge aller Gatter werden in der Reihenfolge gespeichert, die durch `getSortedOutputs` bestimmt wird.

## Beispielablauf: Analyse einer komplexen Schaltung 
### jshell:
```java
// Schaltkreis erstellen
Circuit<Object> c1 = new Circuit<>("Schaltkreis 1", 10, 5);

// Eingänge erstellen
Input x1 = new Input("x1");
Input x2 = new Input("x2");

// Gatter erstellen
Gate nandGate1 = new Gate("NAND", "nandGate1");
Gate nandGate2 = new Gate("NAND", "nandGate2");
Gate nandGate3 = new Gate("NAND", "nandGate3");
Gate nandGate4 = new Gate("NAND", "nandGate4");
Gate notGate = new Gate("NOT", "notGate");

// Komponenten hinzufügen
c1.addComponent(2, 1, x1);
c1.addComponent(4, 1, x2);
c1.addComponent(3, 3, nandGate1);
c1.addComponent(2, 5, nandGate2);
c1.addComponent(4, 5, nandGate3);
c1.addComponent(3, 7, nandGate4);
c1.addComponent(3, 9, notGate);

// Komponenten verbinden
c1.connectComponents(x1, nandGate2, 1);
c1.connectComponents(x1, nandGate1, 1);
c1.connectComponents(x2, nandGate3, 2);
c1.connectComponents(x2, nandGate1, 2);
c1.connectComponents(nandGate1, nandGate2, 2);
c1.connectComponents(nandGate1, nandGate3, 1);
c1.connectComponents(nandGate2, nandGate4, 1);
c1.connectComponents(nandGate3, nandGate4, 2);
c1.connectComponents(nandGate4, notGate, 1);

// Schaltung analysieren und Wahrheitstabelle ausgeben 
c1.drawTable();
```
### Turtle:
""", Map.of("drawTable", Text.cutOut("./CircuitDoku.java", "// drawTable"),
            "tableFrame", Text.cutOut("./CircuitDoku.java", "// tableFrame"),
            "inputValues", Text.cutOut("./CircuitDoku.java", "// inputValues"),
            "outputValues", Text.cutOut("./CircuitDoku.java", "// outputValues"),
            "evalInputs", Text.cutOut("./CircuitDoku.java", "// evalInputs"),
            "evalOutputs", Text.cutOut("./CircuitDoku.java", "// evalOutputs"))));

turtle1.reset();
Circuit<Object> c1 = new Circuit<>("Schaltkreis 1", 10, 5);
c1.deleteCircuit();  
Input x1 = new Input("x1");
Input x2 = new Input("x2");
Gate nandGate1 = new Gate("NAND", "nandGate1");
Gate nandGate2 = new Gate("NAND", "nandGate2");
Gate nandGate3 = new Gate("NAND", "nandGate3");
Gate nandGate4 = new Gate("NAND", "nandGate4");
Gate notGate = new Gate("NOT", "notGate");
c1.addComponent(2, 1, x1);
c1.addComponent(4, 1, x2);
c1.addComponent(3, 3, nandGate1);
c1.addComponent(2, 5, nandGate2);
c1.addComponent(4, 5, nandGate3);
c1.addComponent(3, 7, nandGate4);
c1.addComponent(3, 9, notGate);
c1.connectComponents(x1, nandGate2, 1);
c1.connectComponents(x1, nandGate1, 1);
c1.connectComponents(x2, nandGate3, 2);
c1.connectComponents(x2, nandGate1, 2);
c1.connectComponents(nandGate1, nandGate2, 2);
c1.connectComponents(nandGate1, nandGate3, 1);
c1.connectComponents(nandGate2, nandGate4, 1);
c1.connectComponents(nandGate3, nandGate4, 2);
c1.connectComponents(nandGate4, notGate, 1);
c1.drawTable();

Clerk.markdown(
    Text.fillOut(
"""
---
# Szenario 4 - Halbaddierer
## Ziel
Ein Benutzer soll die Möglichkeit haben, einen Halbaddierer zu erstellen oder einen vorgefertigten Halbaddierer zu nutzen.  
Der Halbaddierer kann zwei Binärzahlen (Eingänge) addieren und gibt die **Summe** und den **Übertrag (Carry)** aus.

### 1. **Halbaddierer manuell erstellen**

Ein Halbaddierer besteht aus zwei Eingängen (`x1`, `x2`), einem XOR-Gatter für die Summe und einem AND-Gatter für den Übertrag.  
Die Komponenten können manuell erstellt und verbunden werden.

## Beispiel:
### jshell:
```java
// Schaltkreis erstellen
Circuit<Object> c1 = new Circuit<>("Halbaddierer", 10, 5);

// Eingänge erstellen
Input x1 = new Input("x1", 0); 
Input x2 = new Input("x2", 0);

// Gatter erstellen
Gate xorGate = new Gate("XOR", "Summe");
Gate andGate = new Gate("AND", "Übertrag");

// Komponenten hinzufügen
c1.addComponent(2, 1, x1);
c1.addComponent(3, 1, x2);
c1.addComponent(2, 3, xorGate);
c1.addComponent(4, 3, andGate);

// Komponenten verbinden
c1.connectComponents(x1, xorGate, 1); 
c1.connectComponents(x2, xorGate, 2);  
c1.connectComponents(x1, andGate, 1);  
c1.connectComponents(x2, andGate, 2); 

// Eingänge schalten
c1.setInput("x1", 1); 
c1.setInput("x2", 1);
```
### 2. **Vorgefertigten Halbaddierer verwenden**

Die Klasse `HalfAdder` ermöglicht es, einen Halbaddierer direkt zu erstellen und in den Schaltkreis einzufügen.

```java
${0}
```
## Beispiel:
### jshell:
```java
// Schaltkreis erstellen
Circuit<Object> c1 = new Circuit<>("Halbaddierer", 10, 5);

// Half-Adder hinzufügen
HalfAdder halfAdder = new HalfAdder(c1);

// Eingänge setzen
c1.setInput(halfAdder.x1, 1);
c1.setInput(halfAdder.x2, 1);
```
### Turtle bei beiden Beispielen gleich:
""", Text.cutOut("./CircuitDoku.java", "// drawHalfAdder")));
turtle1.reset();
Circuit<Object> c1 = new Circuit<>("Halbaddierer", 10, 5);
c1.deleteCircuit(); 
HalfAdder halfAdder = new HalfAdder(c1);
c1.setInput(halfAdder.x1, 1);
c1.setInput(halfAdder.x2, 1);

Clerk.markdown("""
---
# Szenario 5 – Speichern und Laden von Schaltungen

## Ziel
Das Ziel dieses Szenarios ist es, Nutzern zu ermöglichen, Schaltungen abzuspeichern und später wieder zu laden, um sie weiterzuverwenden und zu bearbeiten. Dies ist besonders praktisch, wenn komplexe Schaltungen wie ein Halbaddierer erstellt wurden und die Arbeit an der Schaltung nach einer Pause fortgesetzt werden soll.

## Herausforderung
- Beim Speichern und Laden wurde festgestellt, dass die Referenzen der geladenen Objekte häufig nicht korrekt aktualisiert wurden. Dies führte dazu, dass die Schaltungen zwar optisch korrekt geladen wurden, aber keine Interaktion mit den geladenen Komponenten möglich war.
- Man kann neue Gatter oder Eingänge in die geladene Schaltung einfügen und diese neuen Komponenten weiter bearbeiten, allerdings ist es nicht möglich, diese neuen Komponenten mit den bereits geladenen Komponenten zu verbinden.

## Vorgehensweise

### 1. **Speichern der Schaltung**
Die Schaltung wird in einer Datei im `.ser`-Format gespeichert, das die Serialisierung der Schaltung ermöglicht. Hierbei werden alle relevanten Informationen wie die Komponenten, Verbindungen und Positionen gespeichert. Die gespeicherte Datei wird im selben Verzeichnis wie die der Java-Datei hinterlegt.

```java
class CircuitHandler<T> {
    public void saveCircuit(Circuit<T> circuit, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(circuit);
            System.out.println("Schaltung erfolgreich in " + fileName + " gespeichert.");
        } catch (IOException e) {
            System.err.println("Fehler beim Speichern der Schaltung: " + fileName);
            e.printStackTrace();
        }
    }
}
```

### 2. Laden der Schaltung
Beim Laden einer Schaltung wird die gespeicherte Datei gelesen und die Schaltung in die Live-Ansicht eingefügt. Dabei wird die Methode copyFrom der Circuit-Klasse verwendet, um die geladenen Daten auf das existierende Schaltungsobjekt zu übertragen.

```java
class CircuitHandler<T> {
    public void loadCircuit(String fileName, Circuit<T> targetCircuit) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            Circuit<T> loadedCircuit = (Circuit<T>) ois.readObject();
            System.out.println("Schaltung erfolgreich aus " + fileName + " geladen.");
            targetCircuit.copyFrom(loadedCircuit);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Fehler beim Laden der Schaltung: " + fileName);
            e.printStackTrace();
        }
    }
}
```

### 3. Kopieren der geladenen Schaltung
Die Methode copyFrom stellt sicher, dass die geladenen Komponenten und Verbindungen korrekt in die Live-Ansicht übertragen werden. Dies beinhaltet das Neuzeichnen aller Elemente und das Aktualisieren der Verbindungen.

```java
void copyFrom(Circuit<T> loadedCircuit) { 
    this.width = loadedCircuit.width;
    this.height = loadedCircuit.height;
    this.maxCols = loadedCircuit.maxCols;
    this.maxRows = loadedCircuit.maxRows;
    this.turtle1 = new Turtle(this.width, this.height);
    this.turtle2 = new Turtle(1600, 1000);
    this.components.clear();

    for (Map.Entry<Point, T> entry : loadedCircuit.components.entrySet()) {
        Point position = entry.getKey();
        T component = entry.getValue(); 
        // falls das Objekt ein Gate oder Input ist, Referenz aktualisieren
        this.components.put(position, component);
        this.addComponentWithoutChecks(position.y, position.x, component);
        if (component instanceof Gate gate) component = this.getComponentByName(gate.getName());
        else if (component instanceof Input input) component = this.getComponentByName(input.getInputName());
        System.out.println(component + " an Position (" + position.y + ", " + position.x + ") hinzugefügt.");
    }

    this.connections.clear();
    this.firstInputPositions.clear();
    this.secondInputPositions.clear();
    this.outputPositions.clear();

    restoreConnections(loadedCircuit);
}
```

## Beispiel:
### jshell:
```java
// Schaltung erstellen
Circuit<Object> c1 = new Circuit<>("Half-Adder", 15, 6);
HalfAdder halfAdder = new HalfAdder(c1);

// Schaltung speichern und die Ansicht zurücksetzen
CircuitHandler<Object> handler = new CircuitHandler<>();
handler.saveCircuit(c1, "HalfAdder.ser");
Clerk.clear()

// Schaltung laden
Circuit<Object> c2 = new Circuit<>("Half-Adder", 15, 6);
CircuitHandler<Object> handler = new CircuitHandler<>();
handler.loadCircuit("HalfAdder.ser", c2);
```

## Fazit
Beim Speichern und Laden der Schaltung wurde festgestellt, dass die geladenen Schaltungen zwar optisch korrekt dargestellt werden, jedoch keine weitere Interaktion mit den geladenen Komponenten möglich ist. 
Dies bedeutet, dass Eingaben nicht geschaltet und keine neuen Verbindungen mit den geladenen Komponenten erstellt werden können. 
Allerdings ist es möglich, neue Gatter oder Eingänge in die geladene Schaltung einzubauen und mit diesen weiterzuarbeiten. 
Jedoch können diese neuen Komponenten nicht mit den bereits geladenen Komponenten verbunden werden. 
Dieses Verhalten ist auf das Problem zurückzuführen, dass die Referenzen der geladenen Objekte nicht korrekt aktualisiert werden. 
Dadurch bleibt die Schaltung nur teilweise nutzbar und stellt eine bekannte Herausforderung bei der Arbeit mit serialisierten Objekten und generischen Typen dar.

# Weitere nützliche Funktionen

## **1. deleteCircuit()**
Diese Funktion setzt die gesamte Schaltung zurück. Alle Komponenten, Verbindungen und Positionen werden gelöscht, und die Zeichenfläche wird vollständig geleert.

---

## **2. addColumn(int count)**
Mit dieser Funktion kann die Anzahl der Spalten in der Schaltung dynamisch erhöht werden. Der Parameter `count` gibt an, wie viele zusätzliche Spalten hinzugefügt werden sollen. 

---

## **3. addRow(int count)**
Ähnlich wie `addColumn()` ermöglicht diese Funktion das Hinzufügen zusätzlicher Reihen. Der Parameter `count` gibt an, wie viele Reihen zur aktuellen Schaltung hinzugefügt werden sollen. 

---

## **4. printConnections()**
Diese Funktion gibt alle derzeitigen Verbindungen der Schaltung in der Konsole aus. Sie zeigt an, welche Komponenten miteinander verbunden sind, inklusive der Details zu den Eingängen und Ausgängen. 
""");


class Circuit<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int maxRows; 
    private int maxCols;
    private Map<Point, T> components; // key: Punkt, value: Komponente 
    private Map<T, Point> firstInputPositions; // key: Komponente, value: Punkt
    private Map<T, Point> secondInputPositions; // key: Komponente, value: Punkt
    private Map<T, Point> outputPositions; // key: Komponente, value: Punkt
    private List<Connection<T>> connections; // Liste der verbundenen Komponente 
    private List<Point> wirePoints; // speichert die Punkte ab, wo sich ein Kabel befindet
    private transient Turtle turtle1  = new Turtle(1100, 600);
    private transient Turtle turtle2;
    private int width = 1600;
    private int height = 700;
    // Versetzung des Kabels 
    private int offsetX = 5; 
    private int offsetY = 5;
    private boolean isRedrawing = false;
    
    // Konstruktor
    Circuit(String name, int cols, int rows) {
        this.components = new HashMap<>();
        this.firstInputPositions = new HashMap<>();
        this.secondInputPositions = new HashMap<>();
        this.outputPositions = new HashMap<>();
        this.connections = new ArrayList<>();
        this.wirePoints = new ArrayList<>();
        this.maxCols = cols;
        this.maxRows = rows;
        drawCircuitField();
    }

    // Konstruktor zum Laden
    Circuit(String name, String fileName) {
        Circuit<T> loadedCircuit = loadCircuit(fileName);
        if (loadedCircuit == null) throw new IllegalArgumentException("Fehler beim Laden der Datei: " + fileName);
        System.out.println("Schaltkreis " + fileName + " wurde erfolgreich geladen.");
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
        turtle1.moveTo(x, 0).color(170, 170, 170);
        for (int i = 0; i < maxCols; i++) {
            if ((i + 1) > 1) turtle1.color(255, 0, 0).penUp().left(90).backward(35).text("     " + (i + 1), null, 25, null).forward(35).right(90).color(170, 170, 170); 
            else turtle1.color(255, 0, 0).penUp().left(90).backward(35).text("     " + (i + 1) + " (E)", null, 25, null).forward(35).right(90).color(170, 170, 170); // Eingang: Nur für Input-Objekte reserviert
            drawPointHorizontal();
            turtle1.penUp().forward(100);
        }
        turtle1.moveTo(0, y);
        for (int i = 0; i < maxRows; i++) {
            turtle1.color(255, 0, 0).penUp().forward(10).left(90).backward(60).text("" + (i + 1), null, 25, null).forward(60).right(90).backward(10).color(170, 170, 170);
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
        turtle1.color(0, 0, 0);
    }

    // gesamte Wahrheitstabelle zeichnen lassen
    // drawTable
    void drawTable() {
        turtle2 = new Turtle(1600, 400);
        turtle2.reset();
        // Tabellemgitter zeichnen
        drawTableFrame();
        
        // Inputs-Kombinationen schreiben
        drawInputValuesInTable();

        // Gate-Outputs-Kombinationen schreiben
        drawOuputValuesInTable();
    }
    // drawTable

    // Tabelle zeichnen
    // tableFrame
    void drawTableFrame() {
        // Anzahl der Verbindungen abspeichern
        long outputCount = connections.stream()
            .map(conn -> conn.destination) // Nur die Zielkomponenten betrachten
            .distinct() // Doppelte entfernen
            .count(); // Zählen
        outputCount = (int) outputCount; 
        if (outputCount > 6) {
            printConnections();
            throw new IllegalArgumentException("Eine Auswertung ist nur mit bis zu sechs Ausgängen möglich.");
        }  

        int rowHeight = 60;
        
        // Anzahl der Inputs abspeichern
        List<Input> inputs = new ArrayList<>();
        for (T component : components.values()) if (component instanceof Input input) inputs.add(input);
        int inputsAmount = inputs.size();
        int totalRows = (int) Math.pow(2, inputsAmount); // Anzahl der Spalten in Abhängigkeit von der Anzahl der Inputs berechnen
        int tableHeight = rowHeight * totalRows; // Höhe der Tabelle bestimmen in Abhängigkeit der Anzahl von Inputs 

        // Input-Teil der Tabelle zeichnen
        turtle2.moveTo(0, 0).penUp().right(90).forward(40).left(90).forward(10).penDown();
        for (int i = 0; i < inputsAmount; i++) 
            turtle2.forward(30).right(90).forward(tableHeight).penUp().backward(tableHeight).penDown().backward(30).penUp().forward(30).left(90).penDown();
        turtle2.forward(5);

        // Output-Teil der Tabelle zeichnen
        for (int i = 0; i < outputCount; i++) 
            turtle2.left(90).forward(30).penUp().backward(30).penDown().backward(tableHeight).penUp().forward(tableHeight).right(90).penDown().forward(240);      
    }
    // tableFrame

    // Inputs-Kombinationen schreiben
    // inputValues
    void drawInputValuesInTable() {
        // Anzahl der Verbindungen abspeichern
        List<Input> inputs = new ArrayList<>();
        for (T component : components.values()) if (component instanceof Input input) inputs.add(input);
        int inputsAmount = inputs.size();

        // Liste all der Kombinationen abspeichern
        List<List<Integer>> combinations = generateInputCombinations(inputsAmount);

        // Input-Namen
        turtle2.moveTo(20, 30).penUp(); // Start
        for (int i = 0; i < inputsAmount; i++)
            turtle2.left(90).text("" + inputs.get(i).getInputName(), null, 13, null).right(90).forward(30);

        // Input-Kombinationen
        turtle2.moveTo(20, 75).penUp(); // Start
        for (int i = 0; i < combinations.size(); i++) {
            List<Integer> combination = combinations.get(i);
            for (int j = 0; j < combination.size(); j++) {
                int value = combination.get(j);
                // Werte in die Tabelle schreiben
                turtle2.left(90).text("" + value, null, 14, null).right(90).forward(30);
                if ((j + 1) % inputsAmount == 0) turtle2.backward(inputsAmount * 30).right(90).moveTo(20, 75 + ((i + 1) * 60)).left(90);
            }
        }
    }
    // inputValues

    // Alle Outputs der Wahrheitstabelle
    // outputValues
    void drawOuputValuesInTable() {
        List<String> logicExpressions = getConnectionLogicStrings();
        List<List<Integer>> allOutputCombinations = evaluateLogicForAllInputs();

        List<Input> inputs = new ArrayList<>();
        for (T component : components.values()) if (component instanceof Input input) inputs.add(input);

        // Namen der Logik
        long outputCount = connections.stream()
            .map(conn -> conn.destination)
            .distinct()
            .count();
        outputCount = (int) outputCount;
        turtle2.moveTo(10, 30).penUp().forward(inputs.size() * 30 + 10);
        for (int i = 0; i < outputCount; i++) turtle2.left(90).text("" + logicExpressions.get(i), null, 13, null).right(90).forward(240);

        // alle kombinierten Outputs
        turtle2.moveTo(115 + (15 + 30 * inputs.size()), 75).penUp(); // Start
        for (int i = 0; i < allOutputCombinations.size(); i++) {
            List<Integer> outputCombination = allOutputCombinations.get(i);
            for (int j = 0; j < outputCombination.size(); j++) {
                int value = outputCombination.get(j);
                // Output-Werte in die Tabelle schreiben
                turtle2.left(90).text("" + value, null, 14, null).right(90).forward(240);
                if ((j + 1) % outputCount == 0) turtle2.backward(outputCount + 240).right(90).moveTo(115 + (15 + 30 * inputs.size()), 75 + ((i + 1) * 60)).left(90);
            }
        }
    }
    // outputValues

    // Generierung der Wahrheitstabelle für alle Inputs
    // evalInputs
    List<List<Integer>> generateInputCombinations(int numberOfInputs) {
        List<List<Integer>> combinations = new ArrayList<>();
        int totalCombinations = (int) Math.pow(2, numberOfInputs); // 2^numberOfInputs

        for (int i = 0; i < totalCombinations; i++) { 
            List<Integer> combination = new ArrayList<>(); 
            for (int j = numberOfInputs - 1; j >= 0; j--) { // Bits werden von links nach rechts betrachtet
                int bit = (i >> j) & 1; // Beispiel für i = 1 und zwei Inputs: (1 >> 1) & 1 = 0, (1 >> 0) & 1 = 1
                combination.add(bit);
            }
            combinations.add(combination);

        }
        return combinations;
    }
    // evalInputs

    // Name der Verbindungen abrufen
    List<String> getConnectionLogicStrings() {
        // Sortierte Outputs abrufen
        List<T> sortedOutputs = getSortedOutputs();

        List<String> logicExpressions = new ArrayList<>();

        for (T output : sortedOutputs) {
            if (output instanceof Gate gate) {
                String gateName = gate.getName(); // Name des Gates
                String gateType = gate.getType(); // Typ des Gates

                // beide Eingänge des Gates ermitteln
                String input1Name = getInputComponentName(gate, 1);
                String input2Name = getInputComponentName(gate, 2);

                // Logik-Ausdruck zusammensetzen
                String logicExpression = gateName + " = " + input1Name + " " + gateType + " " + input2Name;
                System.out.println(logicExpression);
                logicExpressions.add(logicExpression);
            }
        }
        return logicExpressions;
    }

    String getInputComponentName(Gate gate, int inputNumber) {
        for (Connection<T> conn : connections) {
            if (conn.destination.equals(gate) && conn.inputNumber == inputNumber)
                if (conn.source instanceof Input input) return input.getInputName(); // Name des Inputs
                else if (conn.source instanceof Gate sourcGate) return sourcGate.getName(); // Name als Quelle kommende Gate
        }
        return "";
    }

    // Generierung der Wahrheitstabelle für alle Outputs
    // evalOutputs
    List<List<Integer>> evaluateLogicForAllInputs() {
        // alle Input-Komponenten sammeln
        List<T> inputList = components.values().stream()
            .filter(component -> component instanceof Input) // filter nur Input-Objekte
            .collect(Collectors.toList()); // sammelt alle Inputs und fügt sie der Liste hinzu
        int inputCount = inputList.size();

        List<List<Integer>> allResults = new ArrayList<>(); 

        // alle möglichen Kombinationen von Inputs generieren
        List<List<Integer>> inputCombinations = generateInputCombinations(inputCount); 

        // sortierte Gates
        List<T> sortedOutputs = getSortedOutputs();

        // über alle Kombinationen iterieren
        for (List<Integer> combination : inputCombinations) {
            
            // Eingänge setzten
            for (int i = 0; i < inputCount; i++) {
                Input input = (Input) inputList.get(i);
                int value = combination.get(i);

                // nur setzem, wenn der Wert sich geändert hat
                if (input.inputValue != value) input.inputValue = value;
            }
            
            // Schaltung auswerten
            evaluateCircuit();
            isRedrawing = true;
            drawNewCircuit();
            isRedrawing = false;

            // Ergebnisse der Gates in der Reihenfolge speichern
            List<Integer> resultRow = new ArrayList<>();
            for (T output : sortedOutputs) 
                if (output instanceof Gate gate) 
                    resultRow.add(gate.output);
            
            // Ergebnis zur Auswertungsliste hinzufügen
            allResults.add(resultRow);        
        }
        
        return allResults;
    }
    // evalOutputs

    // Liste von rechts nach links sortierten Gates rausbekommen
    List<T> getSortedOutputs() {
        List<T> sortedOutputs = new ArrayList<>();
        Set<T> visited = new HashSet<>();

        for (Connection<T> connection : connections) {
            T destination = connection.destination;
            if (!visited.contains(destination)) sortHelper(destination, visited, sortedOutputs); // wenn das Ziel noch nicht besucht wurde
        }

        Collections.reverse(sortedOutputs); // Ergebnis umdrehen
        return sortedOutputs; 
    }

    // schaut ob, eine Komponente Quelle in der Verbindung ist und holt sich davon das Ziel nur einmal raus
    void sortHelper(T component, Set<T> visited, List<T> sortedOutputs) {
        visited.add(component); // als besucht markiert, damit die Komponenete nicht noch mal bearbeitet wird

        for (Connection<T> connection : connections) 
            // wenn aktuelle Komponente die Quelle einer Verbindung ist und noch nicht besucht wurde
            if (connection.source.equals(component) && !visited.contains(connection.destination)) 
                sortHelper(connection.destination, visited, sortedOutputs); // wird rekursiv für die Zielkomponente aufgerufen 

        if (component instanceof Gate) sortedOutputs.add(component); // nur Gates        
    }    

    // komplette Schaltung löschen
    void deleteCircuit() {
        turtle1.reset();
        turtle2.reset();

        components.clear();
        firstInputPositions.clear();
        secondInputPositions.clear();
        outputPositions.clear();
        connections.clear();
        wirePoints.clear();
        offsetX = 5;
        offsetY = 5;
        drawCircuitField();
        System.out.println("Gesamte Schaltung wurde gelöscht.");
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
    // add
    void addComponent(int row, int col, T component) {
        // vorhandene Position prüfen
        if (!isValidPosition(row, col)) 
            throw new IllegalArgumentException("Ungültige Position: (" + row + ", " + col + "). Diese Position existiert nicht im Schaltungsfeld.");
        
        // Spalte 1 auf Input-Objekte prüfen
        if (col == 1 && !(component instanceof Input)) throw new IllegalArgumentException("Spalte 1 ist nur für Input-Objekte reserviert.");

        // andere Spalten auf Input-Objekte prüfen
        if (col != 1 && (component instanceof Input)) throw new IllegalArgumentException("Input-Objekte dürfen nur in Spalte 1 hinzugefügt werden.");
        
        Point position = new Point(col, row);
        Rectangle gateArea = getGateArea(row, col);

        // freie Position prüfen
        if (components.containsKey(position)) throw new IllegalArgumentException("An dieser Position existiert bereits eine Komponente!");

        // bereits existierendes Objekt im Schaltungfeld prüfen
        if (components.containsValue(component)) 
            throw new IllegalArgumentException("Das Objekt " + component + " existiert bereits im Schaltungsfeld und kann nicht erneut hinzugefügt werden.");

        // prüft, ob der Bereich durch ein Kabel blockiert ist
        for (Point wirePoint : wirePoints) 
            if (gateArea.contains(wirePoint)) 
                throw new IllegalArgumentException("In dieser Zelle verläuft ein Kabel. Komponente kann hier nicht platziert werden.");

        // Komponente zur Map hinzufügen
        components.put(position, component);
        
        turtle1.moveTo(getPixel(col), getPixel(row)).backward(25).left(90).forward(25).right(90); // zur Position gehen

        // Typprüfung und Zeichnen der Komponente
        if (component instanceof Gate) selectGateAndDraw(component);
        else if (component instanceof Input) drawInput(component);
        else throw new IllegalArgumentException("Unbekannter Komponententyp: " + component.getClass().getSimpleName());

        System.out.println(component + " an Position (" + row + ", " + col + ") hinzugefügt.");
    }
    // add

    // Komponente hinzufügen ohne Überprüfung, um dynamisch zeichnen zu können
    void addComponentWithoutChecks(int row, int col, T component) {
        
        turtle1.moveTo(getPixel(col), getPixel(row)).backward(25).left(90).forward(25).right(90); // zur Position gehen

        // Typprüfung und Zeichnen der Komponente
        if (component instanceof Gate) selectGateAndDraw(component); 
        else if (component instanceof Input) drawInput(component);
        // else if Wire..., else if Input...
        else throw new IllegalArgumentException("Unbekannter Komponententyp: " + component.getClass().getSimpleName());
    }

    // Position in Pixelgröße umrechnen
    int getPixel (int colOrRow) {
        int cellWidthOrHeight = 100;
        int pixel = colOrRow * cellWidthOrHeight;
        return pixel;
    }

    // Gatterbereich abrufen
    Rectangle getGateArea(int row, int col) {
        int border = 50;
        int gateSize = 50;
        
        // Startpunkt linke obere Ecke 
        int startX = col * 100 + border - 75;
        int startY = row * 100 + border - 75;

        return new Rectangle(startX, startY, gateSize, gateSize);
    }

    // Komponente verbinden
    // connect
    void connectComponents(T sourceComponent, T destinationComponent, int inputNumber) {        
        // Position prüfen
        if (!isValidConnection(sourceComponent, destinationComponent)) return;
        
        if (inputNumber != 1 && inputNumber != 2) throw new IllegalArgumentException("Es gibt nur zwei Eingänge. Bitte nur Eingang 1 oder 2 auswaehlen.");
        
        Point sourceOutput = outputPositions.get(sourceComponent);
        Point destInput = (inputNumber == 1) ? firstInputPositions.get(destinationComponent) : secondInputPositions.get(destinationComponent);

        if (sourceOutput == null || destInput == null) throw new IllegalArgumentException("Verbindung nicht möglich. Komponentenposition nicht gefunden.");

        // doppelte Verbindungen vermeiden
        if (isConnectionPresent(destinationComponent, inputNumber)) 
            throw new IllegalArgumentException("Verbindung existiert bereits: " + sourceComponent + " -> " + destinationComponent + " (Eingang " + inputNumber + ")");

        // prüfen, ob ein offset in x-Richtung erforderlich ist
        boolean applyXOffset = connections.stream()
            .filter(conn -> conn.destination.equals(destinationComponent))
            .map(conn -> conn.source)
            .anyMatch(otherSource -> 
                checkSourcesYPositions(sourceComponent, otherSource, destinationComponent));
        
        // prüfen, ob ein offset in y-Richtung erforderlich ist
        boolean applyYOffset = connections.stream()
            .filter(conn -> !conn.destination.equals(destinationComponent) && conn.inputNumber == inputNumber)
            .anyMatch(conn -> compareHorizontalPositions(sourceComponent, destinationComponent));

        // prüft, ob eine Quelle bereits eine Verbindung hat
        boolean isAlreadyConnected = isSourceConnected(sourceComponent);

        // Verbindung in die Liste eintragen
        connections.add(new Connection<>(sourceComponent, destinationComponent, inputNumber));
        System.out.println("Verbindung hinzugefügt: " + sourceComponent + " -> " + destinationComponent + " (Eingang " + inputNumber + ")");

        

        turtle1.moveTo(sourceOutput.x, sourceOutput.y);
        drawConnection(sourceOutput, destInput, sourceComponent, destinationComponent, applyXOffset, applyYOffset, isAlreadyConnected);

        if (!isRedrawing) {
            isRedrawing = true;    // Schutz aktivieren
            evaluateCircuit();
            drawNewCircuit();      // Alles neu zeichnen
            isRedrawing = false;   // Schutz deaktivieren
        }
    }
    // connect

    // Verbindungen neuzeichnen
    void reconnectComponents() {
        // Temporäre Kopie der Verbindungen
        List<Connection<T>> tempConnections = new ArrayList<>(connections);
    
        // Verbindungen zurücksetzen
        connections.clear();
    
        // Offsets zurücksetzen
        offsetX = 5;
        offsetY = 5;
    
        // jede Verbindung aus der temporären Liste neu zeichnen
        for (Connection<T> connection : tempConnections) {
            Point sourceOutput = outputPositions.get(connection.source);
            Point destInput = (connection.inputNumber == 1)
                ? firstInputPositions.get(connection.destination)
                : secondInputPositions.get(connection.destination);
    
            if (sourceOutput == null || destInput == null) {
                System.out.println("Verbindung nicht möglich. Komponentenposition nicht gefunden.");
                continue;
            }
    
            // Offset prüfen
            boolean applyXOffset = tempConnections.stream()
                .filter(conn -> conn.destination.equals(connection.destination))
                .map(conn -> conn.source)
                .anyMatch(otherSource -> checkSourcesYPositions(connection.source, otherSource, connection.destination));
    
            boolean applyYOffset = tempConnections.stream()
                .filter(conn -> !conn.destination.equals(connection.destination) && conn.inputNumber == connection.inputNumber)
                .anyMatch(conn -> compareHorizontalPositions(connection.source, connection.destination));
    
            // Prüfen, ob Quelle bereits verbunden ist
            boolean isAlreadyConnected = isSourceConnected(connection.source);
    
            // Verbindung wieder in die Liste eintragen
            connections.add(connection);
            
            // Verbindung zeichnen
            turtle1.moveTo(sourceOutput.x, sourceOutput.y);
            drawConnection(sourceOutput, destInput, connection.source, connection.destination, applyXOffset, applyYOffset, isAlreadyConnected);
        }
    }
    
    // Verbindung zeichnen lassen
    void drawConnection(Point sourceOutput, Point destInput, T sourceComponent, T destinationComponent, boolean applyXOffset, boolean applyYOffset, boolean isAlreadyConnected) {
        int startX = sourceOutput.x;
        int startY = sourceOutput.y;
        int endX = destInput.x;
        int endY = destInput.y;
        boolean movedDown = false; // um, die Turtle wieder in richtige Position zu bringen

        boolean detourNeeded = needsDetour(sourceComponent, destinationComponent);

        // zähle die Anzahl der Verbindungen zu dieser Zielkomponente also 0, 1 oder 2
        long existingConnections = connections.stream()
            .filter(conn -> conn.destination.equals(destinationComponent))
            .count();

        // Startposition
        turtle1.moveTo(startX, startY).penDown();

        if ((sourceComponent instanceof Gate gate && gate.output == 1) || (sourceComponent instanceof Input input && input.inputValue == 1)) turtle1.color(0, 150, 0); // Kabel grün bei input == 1

        // y-Offset anwenden  
        startY = applyDetourIfNeeded(detourNeeded, applyYOffset, startY, endY, isAlreadyConnected);

        if (compareHorizontalPositions(sourceComponent, destinationComponent) && !applyYOffset) offsetX = 5; // damit offsetX nicht zu sehr wächst

        // Kabel nach rechts zeichnen
        if ((applyXOffset && existingConnections > 0)) {
            moveHorizontally(startX, endX, startY, endY, offsetX, existingConnections, applyXOffset, false);
            offsetX += 5;
        } else if (isAlreadyConnected) {
            moveHorizontally(startX, endX, startY, endY, offsetX, existingConnections, false, isAlreadyConnected);
            offsetX += 5;
        }
        else moveHorizontally(startX, endX, startY, endY, 0, existingConnections, false, false);

        // Kabel nach oben oder unten zeichnen
        moveVertically(startX, endX, startY, endY, offsetX, existingConnections, movedDown, applyXOffset, applyYOffset, isAlreadyConnected);
        turtle1.color(0, 0, 0);
    }

    // Kabel nach rechts zeichnen
    void moveHorizontally(int startX, int endX, int startY, int endY, int offsetX, long existingConnections, boolean applyXOffset, boolean isAlreadyConnected) {
        if ((applyXOffset && existingConnections > 0) || isAlreadyConnected) { // mit offset
            while (startX < (endX - offsetX)) {
                startX ++; 
                turtle1.forward(1);
                wirePoints.add(new Point(startX, startY));
            }
        } else { // ohne offset
            while (startX < endX) { 
                startX ++; 
                turtle1.forward(1);
                wirePoints.add(new Point(startX, startY));
            }
        }
    }

    // Kabel nach oben oder unten zeichnen
    void moveVertically(int startX, int endX, int startY, int endY, int offsetX, long existingConnections, boolean movedDown, boolean applyXOffset, boolean applyYOffset, boolean isAlreadyConnected) {
        movedDown = setVerticalDirection(startY, endY); // prüft ob nach unten oder oben gezeichnet werdenn muss 
        if (applyXOffset && existingConnections > 0) {
            if (isAlreadyConnected && !applyYOffset) drawIntersectionCircle();
            startY = drawVerticalWire(startX, startY, endY); // hoch oder runter zeichnen
            if (offsetX > 15) turtle1.forward(offsetY - 5); // damit es richtig verbindet, sobald ein offset in y-Richtung angewendet wurde
            // offset-Strich zeichnen
            if (movedDown) turtle1.left(90).forward(offsetX - 5).right(90);
            else turtle1.right(90).forward(offsetX - 5).left(90);
        } else if (isAlreadyConnected) {
            if (!applyYOffset) drawIntersectionCircle(); 
            startY = drawVerticalWire(startX, startY, endY);
            if (offsetX > 5) turtle1.forward(offsetY - 5);
            // offset-Strich zeichnen
            if (movedDown) turtle1.left(90).forward(offsetX - 5).right(90);
            else turtle1.right(90).forward(offsetX - 5).left(90);
        } else {
            startY = drawVerticalWire(startX, startY, endY);
            if (offsetY > 5) turtle1.forward(offsetY - 5); 
        }

        // Turtle wieder nach rechts drehen
        if (movedDown) turtle1.left(90).penUp();  // nach rechts drehen, wenn nach unten gezeichnet wurde
        else turtle1.right(90).penUp();  // nach rechts drehen, wenn nach oben gezeichnet wurde
    }

    // nach oben oder nach unten drehen
    boolean setVerticalDirection(int startY, int endY) {
        if (startY < endY) {
            turtle1.right(90); // nach unten drehen
            return true; // movedDown = true
        } else if (startY > endY) {
            turtle1.left(90);  // nach oben drehen
            return false; // movedDown = false
        }
        return false;
    }

    // Verbindung nach unten oder oben zeichen 
    int drawVerticalWire(int startX, int startY, int endY) {
        while (startY != endY) {
            startY += (startY < endY) ? 1 : -1; 
            turtle1.forward(1);
            wirePoints.add(new Point(startX, startY));  // Kabelpunkt speichern
        }
        if (offsetY == 10 || offsetY == 15) turtle1.forward(offsetY - 5);
        
        return startY;
    }
    
    // prüft, ob Zelle schon besetzt ist
    boolean isCellOccupied(int row, int col) {
        Point position = new Point(col, row);
        return components.containsKey(position);
    }
    
    // prüft, ob eine Zelle auf dem Weg besetzt ist (außer Ziel)
    boolean needsDetour(T sourceComponent, T destinationComponent) {
        // den Punkt für die Quelle rausbekommen
        Point sourcePosition = components.entrySet().stream()
            .filter(entry -> entry.getValue().equals(sourceComponent))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);

        // den Punkt für das Ziel rausbekommen
        Point destinationPosition = components.entrySet().stream()
            .filter(entry -> entry.getValue().equals(destinationComponent))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);

        int sourceCol = sourcePosition.x;
        int sourceRow = sourcePosition.y;
        int destCol = destinationPosition.x;

        if (sourceCol < destCol) {
            for (int col = sourceCol + 1; col < destCol; col++) {
                Point currentCell = new Point(col, sourceRow);
    
                // Prüfen, ob die Zelle besetzt ist und nicht die Zielkomponente ist
                if (isCellOccupied(sourceRow, col) && !destinationComponent.equals(components.get(currentCell))) {
                    return true; // Umgehung erforderlich
                }
            }
        }
        return false; // Keine Umgehung erforderlich
    }
    
    // drumherum zeichnen
    int applyDetourIfNeeded(boolean detourNeeded, boolean applyYOffset, int startY, int endY, boolean isAlreadyConnected) {
        if (detourNeeded && !applyYOffset && !isAlreadyConnected ) {
            if (startY < endY) {
                turtle1.right(90).forward(30).left(90);
                startY += 30;
            } else if (startY > endY) {
                turtle1.left(90).forward(30).right(90);
                startY -= 30;
            }
        } else if (detourNeeded && applyYOffset) {
            if (startY < endY) {
                turtle1.right(90).forward(30 + offsetY).left(90);
                startY += 30;
            } else if (startY > endY) {
                turtle1.left(90).forward(30 + offsetY).right(90);
                startY -= 30;
            }
            offsetY += 5;
        }
        return startY;  // aktualisiertes startY zurückgeben
    }

    // prüfen, ob beide Quellen oberhalb oder unterhalb sind 
    boolean checkSourcesYPositions(T source1, T source2, T destination) {
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

        destinationPosition = convertToPixel(destinationPosition); // Position der Zielkomponente in Pixel umwandeln

        boolean bothAbove = source1Position.y < destinationPosition.y && source2Position.y < destinationPosition.y;
        boolean bothBelow = source1Position.y > destinationPosition.y && source2Position.y > destinationPosition.y;

        return bothAbove || bothBelow;
    }
    
    // vergleicht, ob Quelle und Ziel in einer horizontalen Linie stehen
    boolean compareHorizontalPositions(T sourceComponent, T destinationComponent) {
        Point sourcePosition = components.entrySet().stream()
            .filter(entry -> entry.getValue().equals(sourceComponent))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);

        // den Punkt für das Ziel rausbekommen
        Point destinationPosition = components.entrySet().stream()
            .filter(entry -> entry.getValue().equals(destinationComponent))
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(null);

        if (sourcePosition == null || destinationPosition == null) {
            throw new IllegalArgumentException("Komponentenposition nicht gefunden.");
        }

        // Wenn die Zielkomponente in derselben Zeile wie die Quelle liegt
        return sourcePosition.y == destinationPosition.y;
    }
    
    // zeichnet den Punkt bei einer Verbindungskreuzung
    void drawIntersectionCircle() {
        double radius = 1.5;
        double stepSize = (2 * Math.PI * radius) / 360;
        turtle1.penDown();
        for (int i = 0; i < 360; i++) {
            turtle1.forward(stepSize).right(1);
        }
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

    // prüft, ob eine Quelle bereits eine Verbindung hat
    boolean isSourceConnected(T sourceComponent) {
        return connections.stream()
            .anyMatch(conn -> conn.source.equals(sourceComponent));           
    }
    
    // prüft, ob schon eine Verbindung in der Liste zur gleichen Input-Nummer vorhanden ist
    boolean isConnectionPresent(T destination, int inputNumber) {
        return connections.stream()
        .anyMatch(conn -> conn.destination.equals(destination) &&
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
        if (!components.containsValue(source)) throw new IllegalArgumentException("Quelle nicht gefunden.");

        if (!components.containsValue(destination)) throw new IllegalArgumentException("Ziel nicht gefunden.");

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
    // setInput
    void setInput(T component, int value) {
        isRedrawing = true;

        if (value != 0 && value != 1) throw new IllegalArgumentException("Bitte nur 1 oder 0 schalten.");
        if (component instanceof Input inputComponent) {
            inputComponent.inputValue = value; // Eingang wird umgeschaltet
            System.out.println(inputComponent.getInputName() + " wurde auf " + inputComponent.inputValue + " geschaltet."); 
            evaluateCircuit();
            drawNewCircuit();
        } else throw new IllegalArgumentException("Fehler: Nicht kompatible Komponente. Bitte Input-Komponente angeben.");
        
        isRedrawing = false;
    }
    // setInput

    // Ouput der jeweiligen Komponente abrufen
    int getComponentOutput(T component) {
        if (component instanceof Input input) return input.inputValue;
        else if (component instanceof Gate gate) return gate.output;
        else throw new IllegalArgumentException("Unbekannte Komponente: " + component );
    }

    // Schaltung auswerten
    // evaluate
    void evaluateCircuit() { 
        boolean hasChanged;

        do {
            hasChanged = false; 
            
            // iteriere über alle Verbindungen und berechne die Outputs
            for (Connection<T> connection : connections) {
                T source = connection.source;
                T destination = connection.destination;
                int inputNumber = connection.inputNumber;
                int sourceOutput = getComponentOutput(source); // Output der Quelle ermitteln
                
                 // Zielkomponente prüfen (muss immer Gate sein)
                if (destination instanceof Gate gate) {
                    int previousOutput = gate.output;

                    // setze den Eingangswert abhängig vom Eingang (1 oder 2)
                    if (inputNumber == 1) gate.setInput1(sourceOutput);
                    else if (inputNumber == 2) gate.setInput2(sourceOutput);
                    
                    gate.inputToOutput();

                    // prüfen, ob sich der Output geändert hat
                    if (gate.output != previousOutput) {
                        System.out.println("[" + gate.getName() + "] Output geändert:");
                        System.out.println("  Vorher: " + previousOutput);
                        System.out.println("  Nachher: " + gate.output);
                        hasChanged = true;
                    }
                }
            }
        } while (hasChanged); // wiederholen, solange sich Outputs ändern
    }
    // evaluate

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

    // Koordianten zu Mitte der Zelle
    Point convertToPixel(Point gridPos) {
        int cellSize = 100;  // Größe einer Zelle (100x100 Pixel)
        int offset = 50;     // Startversatz (linker Rand)
        
        // Berechnung zur Mitte der Zelle
        int x = gridPos.x * cellSize + offset + (cellSize / 2);
        int y = gridPos.y * cellSize + offset + (cellSize / 2);
        
        return new Point(x, y);
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
    // drawNew
    void drawNewCircuit() {
        turtle1.reset();
        drawCircuitField();
        for (Map.Entry<Point, T> entry : components.entrySet()) {
            Point position = entry.getKey();
            T component = entry.getValue(); 
            addComponentWithoutChecks(position.y, position.x, component);
        }
        reconnectComponents(); // Kabel wieder zeichnen
    }
    // drawNew  
    
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
        if (!isRedrawing) System.out.println("Koordinaten Ausgang: x = " + x + ", y = " + y);
        x -= 60;
        y -= 15;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).backward(30); // Test
        point = new Point(x, y);
        firstInputPositions.put(component, point); // die Koordinaten vom oberen Eingang gespeichert
        if (!isRedrawing) System.out.println("Koordinaten oberen Eingang: x = " + x + ", y = " + y);
        y += 30;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).backward(30); // Test
        point = new Point(x, y);
        secondInputPositions.put(component, point); // die Koordinaten vom unteren Eingang gespeichert
        if (!isRedrawing) System.out.println("Koordinaten unteren Eingang: x = " + x + ", y = " + y);
    }

    void saveEndingsOfnGate(int x, int y, T component) {
        x += 43;
        y += 1;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).forward(30); // Test
        Point point = new Point(x, y);
        outputPositions.put(component, point); // die Koordinaten vom Ausgang gespeichert
        if (!isRedrawing) System.out.println("Koordinaten Ausgang: x = " + x + ", y = " + y);
        x -= 73;
        y -= 16;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).backward(30); // Test
        point = new Point(x, y);
        firstInputPositions.put(component, point); // Koordinaten vom oberen Eingang gespeichert
        if (!isRedrawing) System.out.println("Koordinaten oberen Eingang: x = " + x + ", y = " + y);
        y += 30;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).backward(30); // Test
        point = new Point(x, y);
        secondInputPositions.put(component, point); // Koordinaten vom unteren Eingang gespeichert
        if (!isRedrawing) System.out.println("Koordinaten unteren Eingang: x = " + x + ", y = " + y);
    }

    void saveEndingsOfNot(int x, int y, T component) {
        x += 43;
        y += 1;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).forward(30); // Test
        Point point = new Point(x, y);
        outputPositions.put(component, point); // die Koordinaten vom Ausgang gespeichert
        if (!isRedrawing) System.out.println("Koordinaten Ausgang: x = " + x + ", y = " + y);
        x -= 73;
        y -= 1;
        // turtle1.moveTo(x, y).penDown().color(0, 255, 0).backward(30); // Test
        point = new Point(x, y);
        firstInputPositions.put(component, point); // Koordinaten vom Eingang gespeichert
        if (!isRedrawing) System.out.println("Koordinaten Eingang: x = " + x + ", y = " + y);
    }

    void saveEndingOfInput(int x, int y, T component) {
        x += 25;
        y += 2;
        // turtle1.moveTo(x, y).penDown().forward(30); // Test
        Point point = new Point(x, y);
        outputPositions.put(component, point); // die Koordinaten vom Ende des Input-Objekts gespeichert
        if (!isRedrawing) System.out.println("Koordinaten Ausgang x = " + x + ", y = " + y);
    }

    // not-Output je nach Wert und Farbe zeichnen
    void drawNotOutputWithValueAndColor(Gate gate) {
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().color(0, 150, 0).forward(5).left(90).forward(1).penUp().backward(5).text(" " + gate.output, null, 15, null).forward(5).right(90).color(0, 0, 0);
    }

    void drawNotOutputWithColor(Gate gate) {
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().color(0, 150, 0).forward(5).left(90).forward(1).right(90).color(0, 0, 0);
    }

    void drawNotOutputWithValue(Gate gate) {
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).penUp().backward(5).text(" " + gate.output, null, 15, null).forward(5).right(90);
    }

    void drawNotOutput(Gate gate) {
        turtle1.forward(31).forward(6).left(90).forward(5).right(90).penDown();
        drawNotCircle();
        turtle1.penUp().backward(6).left(90).backward(6).right(90).forward(13).penDown().forward(5).left(90).forward(1).right(90);
    }

    // AND-Gate zeichnen
    void drawANDGate(T component) {
        if (!(component instanceof Gate gate)) throw new IllegalArgumentException("Komponente ist kein Gatter.");
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("&", null, 13, null).backward(7).right(90);
        // Output
        if (gate.output == 1) { // grün
            if (!isSourceConnected(component)) turtle1.forward(31).penDown().color(0, 150, 0).forward(5).penUp().left(90).backward(4).text(" " + gate.output, null, 15, null).forward(4).right(90).color(0, 0, 0);
            else turtle1.forward(31).penDown().color(0, 150, 0).forward(5).color(0, 0, 0); 
        } else {
            if (!isSourceConnected(component)) turtle1.forward(31).penDown().forward(5).penUp().left(90).backward(4).text(" " + gate.output, null, 15, null).forward(4).right(90);
            else turtle1.forward(31).penDown().forward(5); 
        }
        // Input 1
        if (gate.input1 == 1) turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().color(0, 150, 0).forward(5).color(0, 0, 0).penUp(); // grün
        else turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp();
        // Input 2
        if (gate.input2 == 1) turtle1.backward(5).left(90).forward(30).right(90).penDown().color(0, 150, 0).forward(5).penUp().right(180).color(0, 0, 0); // grün
        else turtle1.backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfGate(x, y, component);
    }

    // OR-Gate zeichnen
    void drawORGate(T component) {
        if (!(component instanceof Gate gate)) throw new IllegalArgumentException("Komponente ist kein Gatter.");
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung
        turtle1.penUp().forward(17).left(90).backward(18).text("≥1", null, 13, null).backward(7).right(90);
        // Output
        if (gate.output == 1) { // grün
            if (!isSourceConnected(component)) turtle1.forward(33).penDown().color(0, 150, 0).forward(5).penUp().left(90).backward(4).text(" " + gate.output, null, 15, null).forward(4).right(90).color(0, 0, 0);
            else turtle1.forward(33).penDown().color(0, 150, 0).forward(5).color(0, 0, 0); 
        } else {
            if (!isSourceConnected(component)) turtle1.forward(33).penDown().forward(5).penUp().left(90).backward(4).text(" " + gate.output, null, 15, null).forward(4).right(90);
            else turtle1.forward(33).penDown().forward(5); 
        }
        // Input 1
        if (gate.input1 == 1) turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().color(0, 150, 0).forward(5).color(0, 0, 0).penUp(); // grün
        else turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp();
        // Input 2
        if (gate.input2 == 1) turtle1.backward(5).left(90).forward(30).right(90).penDown().color(0, 150, 0).forward(5).penUp().right(180).color(0, 0, 0); // grün
        else turtle1.backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfGate(x, y, component);
    }

    // XOR-Gate zeichnen
    void drawXORGate(T component) {
        if (!(component instanceof Gate gate)) throw new IllegalArgumentException("Komponente ist kein Gatter.");
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung
        turtle1.penUp().forward(17).left(90).backward(18).text("=1", null, 13, null).backward(7).right(90);
        // Output
        if (gate.output == 1) { // grün
            if (!isSourceConnected(component)) turtle1.forward(33).penDown().color(0, 150, 0).forward(5).penUp().left(90).backward(4).text(" " + gate.output, null, 15, null).forward(4).right(90).color(0, 0, 0);
            else turtle1.forward(33).penDown().color(0, 150, 0).forward(5).color(0, 0, 0); 
        } else {
            if (!isSourceConnected(component)) turtle1.forward(33).penDown().forward(5).penUp().left(90).backward(4).text(" " + gate.output, null, 15, null).forward(4).right(90);
            else turtle1.forward(33).penDown().forward(5); 
        }
        // Input 1
        if (gate.input1 == 1) turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().color(0, 150, 0).forward(5).color(0, 0, 0).penUp(); // grün
        else turtle1.penUp().backward(55).left(90).forward(15).left(90).penDown().forward(5).penUp();
        // Input 2
        if (gate.input2 == 1) turtle1.backward(5).left(90).forward(30).right(90).penDown().color(0, 150, 0).forward(5).penUp().right(180).color(0, 0, 0); // grün
        else turtle1.backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfGate(x, y, component);
    }

    // NAND-Gate zeichnen
    void drawNANDGate(T component) {
        if (!(component instanceof Gate gate)) throw new IllegalArgumentException("Komponente ist kein Gatter.");
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("&", null, 13, null).backward(7).right(90);
        // not-Output
        if (gate.output == 1) { // grün
            if (!isSourceConnected(component)) drawNotOutputWithValueAndColor(gate);
            else drawNotOutputWithColor(gate);
        } else {
            if (!isSourceConnected(component)) drawNotOutputWithValue(gate);  
            else drawNotOutput(gate);
        }
        // Input 1 
        if (gate.input1 == 1) turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().color(0, 150, 0).forward(5).color(0, 0, 0).penUp(); // grün
        else turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().forward(5).penUp();
        // Input 2 
        if (gate.input2 == 1) turtle1.backward(5).left(90).forward(30).right(90).penDown().color(0, 150, 0).forward(5).color(0, 0, 0).penUp().right(180); // grün
        else turtle1.backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfnGate(x, y, component);
    }

    // NOR-Gate zeichnen
    void drawNORGate(T component) {
        if (!(component instanceof Gate gate)) throw new IllegalArgumentException("Komponente ist kein Gatter.");
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("≥1", null, 13, null).backward(7).right(90);
        // not-Output 
        if (gate.output == 1) { // grün
            if (!isSourceConnected(component)) drawNotOutputWithValueAndColor(gate);
            else drawNotOutputWithColor(gate);
        } else {
            if (!isSourceConnected(component)) drawNotOutputWithValue(gate);  
            else drawNotOutput(gate);
        }
        // Input 1 
        if (gate.input1 == 1) turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().color(0, 150, 0).forward(5).color(0, 0, 0).penUp(); // grün
        else turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().forward(5).penUp();
        // Input 2 
        if (gate.input2 == 1) turtle1.backward(5).left(90).forward(30).right(90).penDown().color(0, 150, 0).forward(5).color(0, 0, 0).penUp().right(180); // grün
        else turtle1.backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfnGate(x, y, component);
    }

    // XNOR-Gate zeichnen
    void drawXNORGate(T component) {
        if (!(component instanceof Gate gate)) throw new IllegalArgumentException("Komponente ist kein Gatter.");
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text("=1", null, 13, null).backward(7).right(90);
        // not-Output 
        if (gate.output == 1) { // grün
            if (!isSourceConnected(component)) drawNotOutputWithValueAndColor(gate);
            else drawNotOutputWithColor(gate);
        } else {
            if (!isSourceConnected(component)) drawNotOutputWithValue(gate);  
            else drawNotOutput(gate);
        }
        // Input 1 
        if (gate.input1 == 1) turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().color(0, 150, 0).forward(5).color(0, 0, 0).penUp(); // grün
        else turtle1.penUp().backward(68).left(90).forward(15).left(90).penDown().forward(5).penUp();
        // Input 2 
        if (gate.input2 == 1) turtle1.backward(5).left(90).forward(30).right(90).penDown().color(0, 150, 0).forward(5).color(0, 0, 0).penUp().right(180); // grün
        else turtle1.backward(5).left(90).forward(30).right(90).penDown().forward(5).penUp().right(180);
        // Koordinaten des Ausgangs und der Eingänge abspeichern
        saveEndingsOfnGate(x, y, component);
    }

    // NOT-Gate zeichnen
    void drawNOTGate(T component) {
        if (!(component instanceof Gate gate)) throw new IllegalArgumentException("Komponente ist kein Gatter.");
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().left(90).forward(5).text(gate.getName(), null, 12, null).backward(5).right(90);
        drawSmallSquare();
        // Beschriftung 
        turtle1.penUp().forward(19).left(90).backward(18).text(" 1", null, 13, null).backward(7).right(90);
        // not-Output 
        if (gate.output == 1) { // grün
            if (!isSourceConnected(component)) drawNotOutputWithValueAndColor(gate);
            else drawNotOutputWithColor(gate);
        } else {
            if (!isSourceConnected(component)) drawNotOutputWithValue(gate);  
            else drawNotOutput(gate);
        }
        // nur Input 1
        if (gate.input1 == 1) turtle1.penUp().backward(68).penDown().color(0, 150, 0).backward(5).penUp().color(0, 0, 0);
        else turtle1.penUp().backward(68).penDown().backward(5).penUp();
        
        // Koordinaten des Ein- und Ausgangs abspeichern
        saveEndingsOfNot(x, y, component);
    }

    // Input in der ersten Spalte zeichnen
    void drawInput(T component) { 
        if (!(component instanceof Input input)) throw new IllegalArgumentException("Komponente ist kein Input.");
        // in die Mitte des Feldes gehen
        int x = getPixelPositionX(component);
        int y = getPixelPositionY(component);
        // Benennung
        turtle1.penUp().forward(15).left(90).backward(20).text(input.getInputName(), null, 14, null).right(90).backward(15);
        if (input.inputValue == 1) {
            // Input-Wert bei 1 grün
            turtle1.left(90).backward(10).color(0, 150, 0).text("" + input.inputValue, null, 15, null).forward(3).right(90).forward(10);
            // Input-Objekt bei 1 grün
            turtle1.penDown().forward(40).penUp().color(0, 0, 0);
        } else {
            // Input-Wert
            turtle1.left(90).backward(10).text("" + input.inputValue, null, 15, null).forward(3).right(90).forward(10);
            // Input-Objekt
            turtle1.penDown().forward(40).penUp(); 
        }
        saveEndingOfInput(x, y, component);
    }
    
    // Schaltung speichern
    void saveCircuit(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
            System.out.println("Datei " + fileName +" wurde gespeichert.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Methode, die gespeicherte Objekte lädt
    @SuppressWarnings("unchecked")
    public Circuit<T> loadCircuit(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Circuit <T> loadedCircuit = (Circuit<T>) ois.readObject();
            this.width = loadedCircuit.width;
            this.height = loadedCircuit.height;
            this.turtle1 = new Turtle(this.width, this.height);
            this.turtle2 = new Turtle(1600, 1000);
            this.components = loadedCircuit.components;
            this.firstInputPositions = loadedCircuit.firstInputPositions;
            this.secondInputPositions = loadedCircuit.secondInputPositions;
            this.outputPositions = loadedCircuit.outputPositions;
            this.connections = loadedCircuit.connections;
            this.wirePoints = loadedCircuit.wirePoints;
            this.maxCols = loadedCircuit.maxCols;
            this.maxRows = loadedCircuit.maxRows;
            this.offsetX = loadedCircuit.offsetX;
            this.offsetY = loadedCircuit.offsetY;
            this.isRedrawing = loadedCircuit.isRedrawing;
            this.drawNewCircuit();
            return this;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// Gatter mit verschiedener Logik
class Gate implements Serializable {
    private static final long serialVersionUID = 1L;

    private String type;
    private String name;
    public int input1;
    public int input2;
    public int output;
    

    // Konstruktor
    Gate(String type, String name) {
        this.type = type.toUpperCase();
        this.name = name;
        this.output = inputToOutput();
    }

    // wandelt den Input je nach Logik in Output um
    int inputToOutput() {
        output = switch (type) {
            case "AND" -> this.input1 & this.input2;
            case "OR" -> this.input1 | this.input2;
            case "XOR" -> this.input1 ^ this.input2;
            case "NAND" -> (this.input1 & this.input2) == 1 ? 0 : 1;
            case "NOR" -> (this.input1 | this.input2) == 1 ? 0 : 1;
            case "XNOR" -> (this.input1 ^ this.input2) == 0 ? 1 : 0;
            case "NOT" -> this.input1 == 1 ? 0 : 1;
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

    // Inputs setzten
    void setInput1(int value) {
        this.input1 = value;
    }

    void setInput2(int value) {
        this.input2 = value;
    }

    @Override
    public String toString() {
        return "Gate type: " + this.type + " named: " + this.name;
    }
}

// Eingänge zum Schalten
class Input implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    public int inputValue;

    // Konstruktor mit direkter Input-Eingabe
    Input(String name, int inputValue) {
        assert inputValue == 1 || inputValue == 0;
        this.name = name;
        this.inputValue = inputValue;
    }

    // Konstruktor ohne Input-Eingabe
    Input (String name) {
        this.name = name;
        this.inputValue = 0;
    }

    // Nanme für die Zuordnung
    String getInputName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

// Verbindungen
class Connection<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
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

// vorgefertigter HalfAdder
// drawHalfAdder
class HalfAdder {
    Circuit<Object> circuit;
    Input x1;
    Input x2;
    Gate xorGate;
    Gate andGate;

    // Konstruktor nimmt eine bestehende Schaltung und fügt in ihr den HalfAdder ein
    HalfAdder(Circuit<Object> circuit) {
        this.circuit = circuit; 
        createHalfAdder();
    }

    private void createHalfAdder() {
        // Eingänge erstellen
        x1 = new Input("x1", 0);
        x2 = new Input("x2", 0);
    
        // Gatter erstellen
        xorGate = new Gate("XOR", "Summe");
        andGate = new Gate("AND", "Übertrag");
    
        // Komponenten hinzufügen
        circuit.addComponent(2, 1, x1); 
        circuit.addComponent(3, 1, x2); 
        circuit.addComponent(2, 3, xorGate); 
        circuit.addComponent(4, 3, andGate); 
    
        // Komponenten verbinden
        circuit.connectComponents(x1, xorGate, 1); 
        circuit.connectComponents(x2, xorGate, 2); 
        circuit.connectComponents(x1, andGate, 1); 
        circuit.connectComponents(x2, andGate, 2);

        System.out.println("HalfAdder wurde erfolgreich gezeichnet.");
    }
}
// drawHalfAdder

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

/*
Circuit<Object> c1 = new Circuit<>("Circ 1", 15, 10);
Input input1 = new Input("x1", 1);
c1.addComponent(2, 1, input1);
Input input2 = new Input("x2", 0);
c1.addComponent(3, 1, input2);
Gate xnorGate1 = new Gate("xnor", "xnorGate1");
c1.addComponent(1, 2, xnorGate1);
c1.connectComponents(input1, xnorGate1, 1);
c1.connectComponents(input2, xnorGate1, 2);
*/

/*
Circuit<Object> c1 = new Circuit<>("Circ 1", 15, 10);
Input input1 = new Input("x1", 1);
Input input2 = new Input("x2", 0);
Input input3 = new Input("x3", 1);
Gate andGate1 = new Gate("and", "andGate1");
Gate xnorGate1 = new Gate("xnor", "xnorGate1");
c1.addComponent(1, 1, input1);
c1.addComponent(2, 1, input2);
c1.addComponent(3, 1, input3);
c1.addComponent(3, 3, xnorGate1);
c1.addComponent(4, 3, andGate1);
c1.connectComponents(input3, andGate1, 2);
c1.connectComponents(input1, andGate1, 1);
c1.connectComponents(input1, xnorGate1, 1);
c1.connectComponents(input2, xnorGate1, 2);
*/

/* 
Circuit<Object> c1 = new Circuit<>("Circ 1", 15, 10);
Gate andGate1 = new Gate("and", "andGate1");
Gate xnorGate1 = new Gate("xnor", "xnorGate1");
Gate nandGate1 = new Gate("nand", "nandGate1");
Gate orGate1 = new Gate("or", "orGate1");
c1.addComponent(2, 2, xnorGate1);
c1.addComponent(2, 3, nandGate1);
c1.addComponent(2, 4, andGate1);
c1.addComponent(2, 5, orGate1);
c1.connectComponents(xnorGate1, andGate1, 2);
c1.connectComponents(xnorGate1, orGate1, 2); 
c1.connectComponents(xnorGate1, andGate1, 1);
c1.connectComponents(xnorGate1, orGate1, 1);

Gate norGate1 = new Gate("nor", "norGate1");
c1.addComponent(2, 6, norGate1);
c1.connectComponents(nandGate1, norGate1, 2);
c1.connectComponents(nandGate1, norGate1, 1);
Gate xorGate1 = new Gate("xor", "xorGate1");
c1.addComponent(2, 7, xorGate1);
c1.connectComponents(andGate1, xorGate1, 2);
c1.connectComponents(andGate1, xorGate1, 1);
*/

/* Halfadder
Circuit<Object> c1 = new Circuit<>("Half-Adder", 15, 6);

Input x1 = new Input("x1", 0); 
Input x2 = new Input("x2", 0);

Gate xorGate = new Gate("XOR", "Summe");
Gate andGate = new Gate("AND", "Übertrag");

c1.addComponent(2, 1, x1);
c1.addComponent(3, 1, x2);
c1.addComponent(2, 3, xorGate);
c1.addComponent(4, 3, andGate);

c1.connectComponents(x1, xorGate, 1); 
c1.connectComponents(x2, xorGate, 2);  
c1.connectComponents(x1, andGate, 1);  
c1.connectComponents(x2, andGate, 2);  
c1.drawTable();
*/

/*
Circuit<Object> c1 = new Circuit<>("Circ 1", 15, 6);
Input input1 = new Input("x1", 0);
Input input2 = new Input("x2", 0);
Gate norGate1 = new Gate("nor", "norGate1");
Gate nandGate1 = new Gate("nand", "nandGate1");
c1.addComponent(2, 1, input1);
c1.addComponent(3, 1, input2);
c1.addComponent(2, 3, norGate1);
c1.addComponent(4, 3, nandGate1);
c1.connectComponents(input1, norGate1, 1);
c1.connectComponents(input2, norGate1, 2);
c1.connectComponents(input1, nandGate1, 1);
c1.connectComponents(input2, nandGate1, 2);
c1.drawTable();
*/