package com.example.mobilki;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;


public class ImageModel {


    public enum Execution {
        LOCAL, CLOUD
    }

    private final Module module;

    public ImageModel(Module module) {
        this.module = module;
    }

    public Execution classify(String filePath, Resources resources) {

        Tensor inputTensor = TensorUtils.inputToTensor(filePath, resources);

        double[] outputTensor = module.forward(IValue.from(inputTensor)).toTensor().getDataAsDoubleArray();

        return Execution.values()[(int) outputTensor[0]];
    }


}
