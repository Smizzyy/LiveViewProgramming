// Test

Turtle turtle = new Turtle(500, 500);

turtle.penUp();
turtle.backward(75);
turtle.penDown();

for (int i = 1; i <= 6; i++) {
    turtle.left(360.0 / 6).forward(80);
}

turtle.penUp();
turtle.forward(225);
turtle.penDown();

for (int i = 1; i <= 6; i++) {
    turtle.left(360.0 / 6).forward(80);
}
