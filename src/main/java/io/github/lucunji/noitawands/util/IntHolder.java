package io.github.lucunji.noitawands.util;

public class IntHolder {
    public int value;
    public IntHolder() {
        this(0);
    }
    public IntHolder(int val) {
        value = val;
    }
    public void set(int newVal) {
        this.value = newVal;
    }
}
