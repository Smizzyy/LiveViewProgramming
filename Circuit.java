class Circuit {
    Turtle turtle1;
    String name;
    // Map...
    int width = 2000;
    int height = 2000;
    

    Circuit(String name) {
        this.turtle1 = new Turtle(this.width, this.height);
        // Map...
        this.name = name;
    }

    void drawSquare() {
        for (int i = 0; i < 4; i++) {
            turtle1.forward(100).right(90);
        }
    }
    
    void drawCircuitField(int rows, int cols) {
        int x = 50;
        int y = 50;
        turtle1.moveTo(x, y);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                turtle1.penDown();
                drawSquare();
                turtle1.penUp().forward(100);
            }
            turtle1.moveTo(x, y += 100);
        }
    }
}
