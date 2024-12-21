int length = 900;
int height = 900;

Turtle turtle = new Turtle(length, height);

// Stift an linksoben Ecke 
turtle.penUp().backward(length/2).left(90).forward(height/2).penDown();

int RGB = 255;
int colorsCounter = 4;

// Rot
for (int j = 0; j < 4; j++) {
    for (int i = 0; i < length/4; i++) {
        turtle.color(RGB/colorsCounter, 0, 0).backward(height/3).penUp().forward(height/3).penDown().right(90).forward(1).left(90);
    }
    colorsCounter--; 
}

turtle.penUp().left(90).forward(length).left(90).forward(height/3).left(180).penDown();

colorsCounter = 4;

// Grün
for (int j = 0; j < 4; j++) {
    for (int i = 0; i < length/4; i++) {
        turtle.color(0, RGB/colorsCounter, 0).backward(height/3).penUp().forward(height/3).penDown().right(90).forward(1).left(90);
    }
    colorsCounter--; 
}

turtle.penUp().left(90).forward(length).left(90).forward(height/3).left(180).penDown();

colorsCounter = 4;

// Blau
for (int j = 0; j < 4; j++) {
    for (int i = 0; i < length/4; i++) {
        turtle.color(0, 0, RGB/colorsCounter).backward(height/3).penUp().forward(height/3).penDown().right(90).forward(1).left(90);
    }
    colorsCounter--; 
}