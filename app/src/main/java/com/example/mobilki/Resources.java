package com.example.mobilki;

public class Resources {

    private String processorName;
    private int processorCores;
    private int processorFrequency;
    private long memory;
    private int battery;
    private boolean hasWifi;
    private int downloadSpeed;
    private int uploadSpeed;
    private String network;

    public Resources(String processorName, int processorCores, int processorFrequency, long memory, int battery, boolean hasWifi, int downloadSpeed, int uploadSpeed, String network) {
        this.processorName = processorName;
        this.processorCores = processorCores;
        this.processorFrequency = processorFrequency;
        this.memory = memory;
        this.battery = battery;
        this.hasWifi = hasWifi;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
        this.network = network;
    }

    public String getProcessorName() {
        return processorName;
    }

    public int getProcessorCores() {
        return processorCores;
    }

    public int getProcessorFrequency() {
        return processorFrequency;
    }

    public long getMemory() {
        return memory;
    }

    public int getBattery() {
        return battery;
    }

    public boolean isHasWifi() {
        return hasWifi;
    }

    public int getDownloadSpeed() {
        return downloadSpeed;
    }

    public int getUploadSpeed() {
        return uploadSpeed;
    }

    public String getNetwork() {
        return network;
    }
}
