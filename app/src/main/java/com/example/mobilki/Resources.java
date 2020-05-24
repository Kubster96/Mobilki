package com.example.mobilki;

public class Resources {

    private int processorCores;
    private double processorFrequency;
    private long memory;
    private int battery;
    private boolean hasWifi;
    private int downloadSpeed;
    private int uploadSpeed;
    private String network;

    public Resources(int processorCores, double processorFrequency, long memory, int battery, boolean hasWifi, int downloadSpeed, int uploadSpeed, String network) {
        this.processorCores = processorCores;
        this.processorFrequency = processorFrequency;
        this.memory = memory;
        this.battery = battery;
        this.hasWifi = hasWifi;
        this.downloadSpeed = downloadSpeed;
        this.uploadSpeed = uploadSpeed;
        this.network = network;
    }

    public int getProcessorCores() {
        return processorCores;
    }

    public double getProcessorFrequency() {
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
