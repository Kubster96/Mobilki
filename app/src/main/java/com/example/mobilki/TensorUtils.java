package com.example.mobilki;

import org.pytorch.Tensor;

import java.io.File;
import java.nio.FloatBuffer;

public class TensorUtils {
    public static java.nio.FloatBuffer createDirectFloatBuffer(
            float[] data) {
        if (data == null) {
            return null;
        }
        java.nio.FloatBuffer buf = java.nio.ByteBuffer
                .allocateDirect((Float.SIZE / 8) * data.length)
                .order(java.nio.ByteOrder.nativeOrder()).asFloatBuffer();
        buf.clear();
        buf.put(data);
        buf.flip();
        return buf;
    }

    public static float[] prepareInput(String filePath, Resources resources) {
        int processorCores = resources.getProcessorCores();
        double processorFrequency = resources.getProcessorFrequency();
        int battery = resources.getBattery();
        long memory = resources.getMemory() / 1_000_000;
        int downloadSpeed = resources.getDownloadSpeed() / 1_000;
        int uploadSpeed = resources.getUploadSpeed() / 1_000;
        long fileSize = new File(filePath).length();
        fileSize = (long) Math.sqrt(fileSize / 3);

        return new float[]{processorCores, (float) processorFrequency, battery, memory, downloadSpeed, uploadSpeed, fileSize};
    }

    public static Tensor inputToTensor(String filePath, Resources resources) {
        FloatBuffer inputBuffer = createDirectFloatBuffer(prepareInput(filePath, resources));
        return Tensor.fromBlob(inputBuffer, new long[]{1L, 1L, 7L});
    }
}
