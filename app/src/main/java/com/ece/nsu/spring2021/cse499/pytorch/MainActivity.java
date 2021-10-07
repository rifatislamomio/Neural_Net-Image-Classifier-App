package com.ece.nsu.spring2021.cse499.pytorch;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    static final String[] classNames = {"Fire", "Neutral", "Smoke"};
    private static final int RESULT_PICK_IMG = 1001;
    private static final int RESULT_CAPTURE_IMG = 1002;

    Classifier classifier;
    ImageView imageView;
    TextView textView, tvModelName;
    String modelName="";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void init()
    {
    	setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.capturedImage);
        textView = findViewById(R.id.pred);
        tvModelName = findViewById(R.id.modeltv);
        intent = getIntent();
        modelName = intent.getStringExtra("model-name");
        tvModelName.setText(modelName);

        try {
            classifier = new Classifier(Utils.assetFilePath(this,modelName));
        } catch (IOException e) { e.printStackTrace(); }
    }

    public void Capture(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, RESULT_CAPTURE_IMG);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode)
        {
            case RESULT_PICK_IMG:
            {
                if (resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        Bitmap pickedImage = BitmapFactory.decodeStream(imageStream);
                        getPredictedResult(pickedImage);
                        break;

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
            }
        }
            case RESULT_CAPTURE_IMG:
            {
                if (resultCode == RESULT_OK) {
                    Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                    getPredictedResult(imageBitmap);
                    break;
               }
            }
        }
    }

    public void getPredictedResult(Bitmap bitmap)
    {
        imageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 300, 300, false));
        String predictedClass = classifier.predict(bitmap, classNames);
        textView.setText("Predicted: ".concat(predictedClass));
    }

    public void pickPhoto(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_PICK_IMG);
    }

    public void reload(View View) {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

}