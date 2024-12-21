// Robotic Face

Turtle turtle = new Turtle(500, 500);

void drawCircle() {
    for (int i = 1; i <= 25; i++) {
        turtle.left(360.0 / 25).forward(20);
    }    
}

turtle.penUp();
turtle.backward(100);
turtle.penDown();

drawCircle(); // linkes Auge

turtle.penUp();
turtle.forward(220);
turtle.penDown();

drawCircle(); // rechtes Auge

turtle.penUp();
turtle.right(90).forward(100);
turtle.penDown();

// Mund
turtle.forward(100); // rechte Seite des Mundes
turtle.right(90).forward(250); // untere Seite des Mundes
turtle.right(90).forward(100); // linke Seite des Mundes
turtle.right(90).forward(250); // obere Seite des Mundes

// Zähne
for (int i = 1; i <= 4; i++) {
    turtle.penUp();
    turtle.backward(62.5);
    turtle.penDown();
    turtle.right(90).forward(100);
    turtle.penUp();
    turtle.backward(100).left(90);
    turtle.penDown();
}

