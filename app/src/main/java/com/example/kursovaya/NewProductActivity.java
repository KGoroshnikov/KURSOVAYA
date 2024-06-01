package com.example.kursovaya;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NewProductActivity extends SideMenuController {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_PICK = 2;

    private ImageView imageView;
    private EditText productNameEditText, productPriceEditText, productDescriptionEditText;
    private Button addImageButton, saveButton;

    private DatabaseReference databaseReference;
    private StorageReference storageReference;

    private Uri imageUri;

    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        imageView = findViewById(R.id.imageView2);
        productNameEditText = findViewById(R.id.product_name_edit_text);
        productPriceEditText = findViewById(R.id.product_price_edit_text);
        productDescriptionEditText = findViewById(R.id.product_description_edit_text);
        addImageButton = findViewById(R.id.add_image_button);
        saveButton = findViewById(R.id.add_button);
        Button btnOpenCamera = findViewById(R.id.add_camera_button);

        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        storageReference = FirebaseStorage.getInstance().getReference("product_images");

        addImageButton.setOnClickListener(v -> openImageChooser());

        saveButton.setOnClickListener(v -> saveProduct());

        btnOpenCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    dispatchTakePictureIntent();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        SideMenuController.setupSideMenu(this);
    }

    private void dispatchTakePictureIntent() throws IOException {
        File photoFile = createImageFile();

        // Если файл создан успешно, создаем Uri для него
        if (photoFile != null) {
            imageUri = FileProvider.getUriForFile(this, "com.example.kursovaya.fileprovider", photoFile);

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImageFile() throws IOException {
        // Создание уникального имени файла для сохранения фото
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* префикс */
                ".jpg",         /* расширение */
                storageDir      /* каталог для сохранения файла */
        );
        // Сохраняем путь к файлу
        currentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }


    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICK);
    }

    private void saveProduct() {
        String productName = productNameEditText.getText().toString();
        String productPrice = productPriceEditText.getText().toString();
        String productDescription = productDescriptionEditText.getText().toString();

        if (TextUtils.isEmpty(productDescription) || TextUtils.isEmpty(productName) || TextUtils.isEmpty(productPrice) || imageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        String productId = databaseReference.push().getKey();
        Product product = new Product(productName, Integer.parseInt(productPrice), "null", productDescription);

        // Upload image to Firebase Storage
        compressAndUploadImage(productId, product);
    }



    private void compressAndUploadImage(String productId, Product product) {
        // Код сжатия изображения
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
            // Уменьшаем размер изображения до 800x800 пикселей
            int maxWidth = 800;
            int maxHeight = 800;
            float scale = Math.min(((float) maxWidth) / bitmap.getWidth(), ((float) maxHeight) / bitmap.getHeight());
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Сжимаем изображение с качеством 50% и записываем в ByteArrayOutputStream
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] imageData = baos.toByteArray();

            // Загружаем сжатое изображение в Firebase Storage
            uploadCompressedImageToStorage(productId, product, imageData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadCompressedImageToStorage(String productId, Product product, byte[] imageData) {
        StorageReference imageRef = storageReference.child("product_" + productId + ".jpg");

        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                product.setImage(imageUrl);
                databaseReference.child(productId).setValue(product);
                Toast.makeText(NewProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                finish();
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(NewProductActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Используйте URI, который вы указали при вызове камеры
            File imgFile = new  File(currentPhotoPath);
            if(imgFile.exists()){
                Bitmap imageBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageView.setImageBitmap(imageBitmap);
                imageView.setVisibility(View.VISIBLE);
            }
        }
    }

}
