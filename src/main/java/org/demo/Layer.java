package org.demo;

import java.util.Arrays;

public class Layer {
    final int width;
    final int height;
    final char[] buffer;

    int column = 0;
    int row = 0;
    boolean dirty = false;

    Layer(int width, int height) {
        this.width = width;
        this.height = height;
        this.buffer = new char[width * height];
    }

    Layer erase() {
        this.column = 0;
        this.row = 0;
        this.dirty = false;
        Arrays.fill(this.buffer, '\0');
        return this;
    }

    Layer move(int columnDelta, int rowDelta) {
        return column(column + columnDelta).row(row + rowDelta);
    }

    Layer column(int column) {
        this.dirty = true;
        this.column = (column + width) % width;
        return this;
    }

    Layer row(int row) {
        this.dirty = true;
        this.row = (row + height) % height;
        return this;
    }

    Layer put(char character) {
        dirty = true;
        buffer[row * width + column] = character;
        return this;
    }

    Layer put(int column, int row, char character) {
        dirty = true;
        buffer[row * width + column] = character;
        return this;
    }

    char get() {
        return this.buffer[row * this.width + column];
    }
}
