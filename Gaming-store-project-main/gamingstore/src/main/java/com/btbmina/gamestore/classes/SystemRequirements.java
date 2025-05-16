package com.btbmina.gamestore.classes;

public class SystemRequirements {

    private int id; // Optional: DB identifier
    private String os;
    private String processor;
    private String memory;
    private String graphics;
    private String storage;

    public SystemRequirements(String os, String processor, String memory, String graphics, String storage) {
        this.os = os;
        this.processor = processor;
        this.memory = memory;
        this.graphics = graphics;
        this.storage = storage;
    }

    // Optionnel si utilisé avec BDD
    public SystemRequirements(int id, String os, String processor, String memory, String graphics, String storage) {
        this(os, processor, memory, graphics, storage);
        this.id = id;
    }

    // Getters et Setters
    public int getId() { return id; }
    public String getOs() { return os; }
    public String getProcessor() { return processor; }
    public String getMemory() { return memory; }
    public String getGraphics() { return graphics; }
    public String getStorage() { return storage; }

    public void setId(int id) { this.id = id; }
    public void setOs(String os) { this.os = os; }
    public void setProcessor(String processor) { this.processor = processor; }
    public void setMemory(String memory) { this.memory = memory; }
    public void setGraphics(String graphics) { this.graphics = graphics; }
    public void setStorage(String storage) { this.storage = storage; }

    @Override
    public String toString() {
        return "OS: " + os + ", CPU: " + processor + ", RAM: " + memory + ", GPU: " + graphics + ", Storage: " + storage;
    }
}
