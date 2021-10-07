package com.ece.nsu.spring2021.cse499.pytorch;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class SelectModelActivity extends AppCompatActivity {
    RadioGroup radioGroup;
    Button doneBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init()
    {
        setContentView(R.layout.activity_select_model);
        radioGroup = findViewById(R.id.radio_group);
        radioGroup.check(R.id.radio_ccnn);
    }

    public void selectModel(View view)
    {
        int selectedButtonId = radioGroup.getCheckedRadioButtonId();
        String selectedModel ="";
        switch (selectedButtonId)
        {
            case R.id.radio_ccnn:
            {
                selectedModel = "CCNN_v2.ptl";
                break;
            }
            case R.id.radio_mnet:
            {
                selectedModel = "MobileNet_v2.ptl";
                break;
            }
            case R.id.radio_resnet:
            {
                selectedModel = "ResNet_v2.ptl";
                break;
            }
            case R.id.radio_anet:
            {
                selectedModel = "AlexNet_v2.ptl";
                break;
            }
            default: break;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("model-name",selectedModel);
        startActivity(intent);
    }
}