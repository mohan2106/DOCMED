package com.lenovo.doc;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class order_medicine_activity extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    public static final int PICK_CAMERA=2;
    //private ListView lv;
    private File file=null;
    private Button contin;
    private LinearLayout came,gallery,my_pres;
    private ArrayList<Uri> imageList=new ArrayList<Uri>();
    private ImageView img;
    private Uri uri=null;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_medicine_activity);

        came=(LinearLayout)findViewById(R.id.camera_picker);
        gallery=(LinearLayout)findViewById(R.id.gallery_picker);
        my_pres=(LinearLayout)findViewById(R.id.old_picker);
        img=(ImageView)findViewById(R.id.pres_image);
        progressDialog=new ProgressDialog(this);
        //lv=(ListView)findViewById(R.id.show_prescription);
        contin=(Button)findViewById(R.id.continue_to_pharma);
        mAuth=FirebaseAuth.getInstance();
        //firebaseDatabase=FirebaseDatabase.getInstance();
        contin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPriscription();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageSelection();
            }
        });
        came.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                /*file = new File(getActivity().getExternalCacheDir(),
                        String.valueOf(System.currentTimeMillis()) + ".jpg");
                uri= Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);*/
                //getActivity().startActivityForResult(intent.createChooser(intent,"choose camera"),PICK_CAMERA);
                if(intent.resolveActivity(getPackageManager())!=null){
                    File dir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    String PictureName=getPictureName();
                    file=new File(dir, PictureName);
                    uri=Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY,1);
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());
                    startActivityForResult(intent.createChooser(intent,"choose camera"),PICK_CAMERA);
                }
            }
        });
    }
    private String getPictureName(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyMMdd HHmmss");
        String timestamp=sdf.format(new Date());
        return "medicine_Prescription"+timestamp+".jpg";
    }
    private void startImageSelection(){

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        //intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        //intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==Activity.RESULT_OK){
            if (requestCode==PICK_IMAGE){
                uri=data.getData();
                Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
                Bitmap bitmap= null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),uri);
                    img.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode==PICK_CAMERA){
                /*Bitmap ne=(Bitmap)data.getExtras().get("data");
                //uri=getImageUri(com.facebook.FacebookSdk.getApplicationContext(), ne);
                uri=data.getData();
                //Toast.makeText(getActivity(), uri.toString(), Toast.LENGTH_SHORT).show();
                img.setImageBitmap(ne);*/
                Toast.makeText(this, "image is stored at"+file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void uploadPriscription(){

        final StorageReference ref = FirebaseStorage.getInstance().getReference("medicinePrescription/"+System.currentTimeMillis()+".jpg");
        final String uid=mAuth.getUid();
        UploadTask uploadTask = ref.child(uid).putFile(uri);
        //final FirebaseUser user=mAuth.getCurrentUser();
        progressDialog.setMessage("Uploading prescription");
        progressDialog.show();

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {

            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                if (!task.isSuccessful()) {

                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.child(uid).getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                //  progressbar2.setVisibility(View.GONE);
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(order_medicine_activity.this, "Upload of priscription is successful", Toast.LENGTH_SHORT).show();
                    Uri downloadUri = task.getResult();
                    final String downloadURL = downloadUri.toString();
                    /*DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("UserInformation");
                    mDatabase.child(uid).child("image").push().setValue(downloadURL);*/
                    startActivity(new Intent(order_medicine_activity.this,chooseMedicine.class));

                } else {
                    Toast.makeText(order_medicine_activity.this,"Some error occured", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
