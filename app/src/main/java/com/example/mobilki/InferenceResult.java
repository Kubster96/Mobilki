package com.example.mobilki;

public class InferenceResult {
    private final float[] input;
    private final float result;
    private final int mode;

    public InferenceResult(float[] input, float result, int mode) {
        this.input = input;
        this.result = result;
        this.mode = mode;
    }
}
