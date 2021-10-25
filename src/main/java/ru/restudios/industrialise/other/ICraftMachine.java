package ru.restudios.industrialise.other;

public interface ICraftMachine {

    boolean canCraft();

    void startCraft();

    int timeoutLeft();
}
