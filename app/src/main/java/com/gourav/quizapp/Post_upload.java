package com.gourav.quizapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Post_upload extends AppCompatActivity {
    String realPath = null;
    int IMG_RESULT = 1;

    FirebaseStorage storage;
    StorageReference storageReference;
    int CAMERA_REQUEST = 1;
    int i = 0;
    private static final int REQUEST_CAPTURE_IMAGE = 100;
    public static final int PICK_IMAGE = 2;
    String string;
    String mydate;
    @BindView(R.id.Title)
    EditText Title;
    @BindView(R.id.description)
    EditText description;
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.posttick)
    ImageButton posttick;
    int flag = 0;
    @BindView(R.id.radiopost)
    RadioButton radiopost;
    @BindView(R.id.Videothumbnail)
    RadioButton Videothumbnail;
    @BindView(R.id.radio)
    RadioGroup radio;

    @BindView(R.id.imageviewforcamera)
    TextView text;
    @BindView(R.id.toolbarpost)
    Toolbar toolbarpost;
    private String mImageFileLocation = "";
    DatabaseReference myRef;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);

        setSupportActionBar(toolbarpost);
        getSupportActionBar().setTitle("Upload");
        ButterKnife.bind(this);
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        if (radiopost.isChecked()) {
            myRef = database.getReference("Posts").push();
            flag = 0;
        } else {
            description.setHint("Link Of Video");
            text.setText("Select the Image for Thumbnail");
            myRef = database.getReference("Video").push();
            flag = 1;
        }

        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (rb.getText().equals("Post")) {
                    description.setHint("Description");
                }
                else
                {
                    description.setHint("Link");
                }
            }
        });

    }


    public void cameraOpen(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (resCode == Activity.RESULT_OK && data != null) {
            String realPath = null;
            realPath = getRealPathFromURI(this, data.getData());
            Uri uriFromPath = Uri.fromFile(new File(realPath));
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            imageview.setImageBitmap(bitmap);
        }
    }

    private void setTextViews(int sdk, String uriPath, String realPath) {
        Toast.makeText(this, "Real Path: " + realPath, Toast.LENGTH_SHORT).show();

    }

    @OnClick(R.id.posttick)
    public void onViewClicked() {
        if (Title.getText().toString().equals("") && description.getText().toString().equals("")) {
            Toast.makeText(this, "PLease enter the Input ", Toast.LENGTH_SHORT).show();
        } else {
            uploadImage();

            String title = Title.getText().toString();
            String desc = description.getText().toString();
            if (flag == 0) {
                myRef.child("title").setValue(title);
                myRef.child("description").setValue(desc);

                myRef.child("image").setValue(realPath);
            } else {
                myRef.child("title").setValue(title);
                myRef.child("link").setValue(desc);

                myRef.child("thumbnail").setValue(realPath);

            }

        }
    }

    private void uploadImage() {

        if (realPath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            StorageTask<UploadTask.TaskSnapshot> uploaded = ref.putFile(Uri.parse(realPath))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Toast.makeText(Post_upload.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Post_upload.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }


    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index
                = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

}

