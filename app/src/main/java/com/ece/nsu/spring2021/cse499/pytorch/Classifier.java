package com.ece.nsu.spring2021.cse499.pytorch;
import android.graphics.Bitmap;
import android.view.View;

import org.pytorch.IValue;
import org.pytorch.LiteModuleLoader;
import org.pytorch.Module;
import org.pytorch.Tensor;
import org.pytorch.torchvision.TensorImageUtils;


public class Classifier {
    Module model;

    public Classifier(String path)  {
        this.model = LiteModuleLoader.load(path);
    }


    private Tensor preprocess(Bitmap bitmap){

        bitmap = Bitmap.createScaledBitmap(bitmap,224,224,false);
        return TensorImageUtils.bitmapToFloat32Tensor(bitmap,TensorImageUtils.TORCHVISION_NORM_MEAN_RGB, TensorImageUtils.TORCHVISION_NORM_STD_RGB);
    }

    public int argMax(float[] pred)
    {
        float max = pred[0];
        int index = 0;
        for(int i=0;i<3;i++)
        {
            if(pred[i]>max){
                index = i;
                max = pred[i];
            }
        }
        return index;
    }

    public String predict(Bitmap bitmap, String[] classNames)
    {
        Tensor tensor = preprocess((bitmap));
        IValue inputs = IValue.from(tensor);
        Tensor outputs = model.forward(inputs).toTensor();
        float[] scores = outputs.getDataAsFloatArray();
        int classIndex = argMax(scores);
        return classNames[classIndex];
    }




}
