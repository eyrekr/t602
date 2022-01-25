package org.demo.core;

import org.demo.tty.Ascii;

import java.util.Arrays;

public class Layer {
    public final int width;
    public final int height;
    private final char[] buffer;

    private int row = 0;
    private int column = 0;

    Layer(final int width, final int height) {
        assert (width > 0);
        assert (height > 0);

        this.width = width;
        this.height = height;
        this.buffer = new char[width * height];
    }

    public Layer reset() {
        this.row = 0;
        this.column = 0;
        Arrays.fill(this.buffer, '\0');
        return this;
    }

    public Layer becomeExactCopyOf(final Layer layer) {
        assert (width == layer.width);
        assert (height == layer.height);

        this.row = layer.row;
        this.column = layer.column;
        System.arraycopy(layer.buffer, 0, this.buffer, 0, Math.min(this.buffer.length, layer.buffer.length));
        return this;
    }

    public Layer merge(final Layer layer) {
        for (int i = 0; i < Math.min(this.buffer.length, layer.buffer.length); i++) {
            final char ch = layer.buffer[i];
            if (ch != Ascii.Null.character) {
                this.buffer[i] = ch;
            }
        }
        return this;
    }

    public boolean isInTheSameStateAs(final Layer layer) {
        if (this.row != layer.row || this.column != layer.column) {
            return false;
        }
        for (int i = 0; i < Math.min(this.buffer.length, layer.buffer.length); i++) {
            if (this.buffer[i] != layer.buffer[i]) {
                return false;
            }
        }
        return true;
    }

    public Layer moveCursorBy(final int columnDelta, final int rowDelta) {
        return setColumn(this.column + columnDelta).setRow(this.row + rowDelta);
    }

    public int getColumn() {
        return this.column;
    }

    public Layer setColumn(final int column) {
        final int adjustedColumn = column % this.width;
        this.column = adjustedColumn < 0
                ? this.width - adjustedColumn
                : adjustedColumn;
        return this;
    }

    public int getRow() {
        return this.row;
    }

    public Layer setRow(final int row) {
        final int adjustedRow = row % this.height;
        this.row = adjustedRow < 0
                ? this.height - adjustedRow
                : adjustedRow;
        return this;
    }

    public Layer put(final char character) {
        return put(this.column, this.row, character);
    }

    public Layer put(final int column, final int row, final char character) {
        assert (column >= 0);
        assert (column < this.width);
        assert (row >= 0);
        assert (row < this.height);

        this.buffer[row * this.width + column] = character;
        return this;
    }

    public Layer put(final String string) {
        return put(this.column, this.row, string);
    }

    public Layer put(final int column, final int row, final String string) {
        assert (column >= 0);
        assert (column < this.width);
        assert (row >= 0);
        assert (row < this.height);

        final int position = row * this.width + column;
        for (int i = 0; i < string.length(); i++) {
            this.buffer[(position + i) % this.buffer.length] = string.charAt(i);
        }
        return this;
    }

    public char get() {
        return this.buffer[this.row * this.width + this.column];
    }

    public char get(final int column, final int row) {
        assert (column >= 0);
        assert (column < this.width);
        assert (row >= 0);
        assert (row < this.height);

        return this.buffer[row * this.width + column];
    }
}
