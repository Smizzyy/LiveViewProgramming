// Taschenrechner in Umgekehrter Polnischer Notation
import java.util.Stack;

Clerk.markdown("""
# Taschenrechner in Umgekehrter Polnischer Notation

""");
class RPN {
    Stack<Float> calcStack;
    Turtle turtle;

    RPN() {
        this.calcStack = new Stack<>();
        turtle = new Turtle(1500, 200);
    }

    float evaluateRPN(String s) {
        calcStack.clear(); // Stack vor jeder Berechnung leeren
        turtle.reset();
        turtle.penUp().backward(700).left(90); // Startposition
        float tempFloat = 0;
        float result = 0;
        float last;
        float secondLast;
        String fullFloat = "";
        char tokens[] = s.toCharArray();
        
        for (char c : tokens) {
            if (c == ' ') { // wenn die Zahl endet, in Stack pushen
                if (!fullFloat.isEmpty()) { // nur wenn fullFloat nicht leer ist    
                    tempFloat = Float.parseFloat(fullFloat); // von String in float umwandeln und in einer temporären float-Variable speichern
                    calcStack.push(tempFloat); // in den Stack pushen
                    fullFloat = ""; // String zurücksetzen
                }    
            } else if (c == '+') {
                if (calcStack.size() >= 2) {
                    result = calcStack.pop() + calcStack.pop();
                    calcStack.push(result);
                }
            } else if (c == '-') {
                if (calcStack.size() >= 2) {
                    last = calcStack.pop();
                    secondLast = calcStack.pop();
                    result = secondLast - last; // vorletztes mit letztes Element subtrahieren
                    calcStack.push(result);
                }
            } else if (c == '*') {
                if (calcStack.size() >= 2) {
                    result = calcStack.pop() * calcStack.pop();
                    calcStack.push(result);
                }
            } else if (c == '/') {
                if (calcStack.size() >= 2) {
                    last = calcStack.pop();
                    secondLast = calcStack.pop();
                    if (last == 0) throw new ArithmeticException("Division durch Null nicht erlaubt");
                    result = secondLast / last; // vorletztes mit letztes Element dividieren
                    calcStack.push(result);
                }
            } else {
                fullFloat += c; // float-Zahl in einen String zusammensetzen
            }  
        }

        result = calcStack.pop(); // gibt das endgültige Ergebnis zurück

        if (!calcStack.isEmpty()) {
            turtle.color(255, 0, 0);
            turtle.text("Ungültige Eingabe", null, 50, null);
            throw new IllegalArgumentException("Ungültige Eingabe"); 
        } 

        // LiveView-Ausgabe
        turtle.text(s + " = " + Float.toString(result), null, 40, null);

        return result;
    }
}