package ru.yandex.strictweb.scriptjava.base;

@Native
public abstract class Screen {
    public int availTop, availLeft;
    public int availHeight, availWidth;
    
    public int height, width;
    public int left, top;
    
    public int colorDepth, pixelDepth;
}
