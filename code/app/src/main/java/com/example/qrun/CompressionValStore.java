package com.example.qrun;

public class CompressionValStore {
    public int compressionVal = 0;
    private static final CompressionValStore compValStoreInstance = new CompressionValStore();
    public static CompressionValStore getInstance() {
        return compValStoreInstance;
    }
    public void setValue(int compressionVal){
        this.compressionVal = compressionVal;
    }
    public int getValue(){
        return compressionVal;
    }

}
