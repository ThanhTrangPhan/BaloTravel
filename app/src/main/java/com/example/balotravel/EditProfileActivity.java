package com.example.balotravel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.balotravel.Fragment.ProfileFragment;
import com.example.balotravel.Model.Image;
import com.example.balotravel.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;

public class EditProfileActivity extends AppCompatActivity {

    TextInputLayout  fullName,username,phoneNo, bio;
    TextView tvName;
    Button btnUpdate;
    String _USERNAME, _FULLNAME, _PHONENO, _ADDRESS;
    int REQUEST_CODE_IMAGE = 1;
    DatabaseReference mData;
    ImageView img;
    User currentUser;
    Uri uri;
    boolean isImageClicked =false;
//    DatabaseReference reference;
//    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
//        new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//             if(result.getResultCode() == RESULT_OK){
//                 Intent intent = result.getData();
//                 String bitmap = intent.getStringExtra("data");
//                 imgHinh.setImageBitmap(bitmap);
//             }
//            }
//        });
    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    protected DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://balotravel-9a424-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
    FirebaseStorage storage = FirebaseStorage.getInstance();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        mData = FirebaseDatabase.getInstance().getReference();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://balotravel-9a424.appspot.com");

        tvName=findViewById(R.id.tvName);
        fullName=findViewById(R.id.edt_fullname);
//        username=findViewById(R.id.edt_username);
        phoneNo=findViewById(R.id.edt_phone_no);
        bio =findViewById(R.id.edt_bio);
        btnUpdate=findViewById(R.id.btn_update);
        img = findViewById(R.id.profile_image);
        showAllUserData();

       img.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {

               isImageClicked = true;
               Log.d("image","Image: "+isImageClicked);
               Intent intent = new Intent(Intent.ACTION_PICK);
               intent.setType("image/*");
               startActivityForResult(intent,REQUEST_CODE_IMAGE);
           }
       });

       btnUpdate.setOnClickListener(new View.OnClickListener() {
           private boolean validateFullname(){
               String val = fullName.getEditText().getText().toString();
               if(val.isEmpty()){
                   fullName.setError("Bạn cần nhập trường này");
                   return false;
               }else{
                   fullName.setError(null);
                   return true;
               }
           }
           private boolean validateBio(){
               String val = bio.getEditText().getText().toString();
               if(val.isEmpty()){
                   bio.setError("Bạn cần nhập trường này");
                   return false;
               }else{
                   bio.setError(null);
                   return true;
               }
           }
           private boolean validatePhone(){
               String pattern = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";
               String val = phoneNo.getEditText().getText().toString();
               if(val.isEmpty()){
                   phoneNo.setError("Bạn cần nhập trường này");
                   return false;
               }else if(!val.matches(pattern)){
                   phoneNo.setError("Bạn nhập sai số điện thoại");
                   return false;
               }else{
                   phoneNo.setError(null);
                   return true;
               }
           }
           public void onClick(View view) {
               if(!validateFullname() | !validateBio() | !validatePhone()){
                   return;
               }
               UploadTask uploadTask = null;
               StorageReference mountainsRef = null;
               if(isImageClicked){

                   Calendar calendar = Calendar.getInstance();

                   mountainsRef = storageRef.child("image" + calendar.getTimeInMillis() + ".png");
                   img.setDrawingCacheEnabled(true);
                   img.buildDrawingCache();
                   Bitmap bitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
                   ByteArrayOutputStream baos = new ByteArrayOutputStream();
                   bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                   byte[] data = baos.toByteArray();

                   uploadTask = mountainsRef.putBytes(data);
                   StorageReference finalMountainsRef = mountainsRef;
                   uploadTask.addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception exception) {
                           // Handle unsuccessful uploads
                           Toast.makeText(EditProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
                       }
                   }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                           // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                           finalMountainsRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                               @Override
                               public void onSuccess(Uri uri) {

                                   final String downloadUrl =
                                           uri.toString();

                                   Image image = new Image(String.valueOf(downloadUrl));

                                   mData.child("images").push().setValue(image, new DatabaseReference.CompletionListener() {
                                       @Override
                                       public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                           if(error == null){
                                               Toast.makeText(EditProfileActivity.this, "Luu du lieu thanh cong", Toast.LENGTH_SHORT).show();
                                           }else{
                                               Toast.makeText(EditProfileActivity.this, "Loi luu du lieu", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   });
                                   User user=new User(
                                           mAuth.getCurrentUser().getEmail(),
                                           Integer.parseInt(phoneNo.getEditText().getText().toString()),
                                           fullName.getEditText().getText().toString(),
                                           bio.getEditText().getText().toString(),
                                           downloadUrl,
                                           mAuth.getUid()
                                   );
                                   currentUser.setImage_profile(downloadUrl);
                                   mDatabase.child("users").child(mAuth.getUid()).setValue(user);
                                   Glide.with(EditProfileActivity.this).load(Uri.parse(currentUser.getImage_profile())).into(img);
                               }
                           });
                       }
                   });
               } else {

                   User user=new User(
                           mAuth.getCurrentUser().getEmail(),
                           Integer.parseInt(phoneNo.getEditText().getText().toString()),
                           fullName.getEditText().getText().toString(),
                           bio.getEditText().getText().toString(),
                           currentUser.getImage_profile(),
                           mAuth.getUid()
                   );
                   mDatabase.child("users").child(mAuth.getUid()).setValue(user);

               }
               
               Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
               startActivity(intent);
           }
       });
    }

               UploadTask uploadTask = null;
               StorageReference mountainsRef = null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data !=null && data.getData() != null){
           uri = data.getData();
           try {
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
               img.setImageBitmap(bitmap);
           } catch (FileNotFoundException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showAllUserData(){
        Intent intent = getIntent();
        currentUser= (User) intent.getSerializableExtra("currentUser");
        mDatabase.child("users").child(mAuth.getUid()).child("image_profile").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentUser.setImage_profile(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        Glide.with(EditProfileActivity.this).load(Uri.parse(currentUser.getImage_profile())).into(img);
        fullName.getEditText().setText(currentUser.getFullname());
        phoneNo.getEditText().setText(currentUser.getPhonenumber()+"");
        bio.getEditText().setText(currentUser.getBio());
        tvName.setText(mAuth.getCurrentUser().getEmail().toString());

    }


//    public void update(View view){
//           if(isFullNameChanged() || isUserNameChanged() || isPhoneNoChanged() || isAddressChanged()){
//               Toast.makeText(this, "Data has been updated", Toast.LENGTH_LONG).show();
//           }
//    }
//    private boolean isFullNameChanged() {
//        if(_FULLNAME.equals(fullName.getEditText().getText().toString())){
//            reference.child(_FULLNAME).child("fullname").setValue(fullName.getEditText().getText().toString());
//            return true;
//        }
//        else{
//            return false;
//        }
//    }
//    private boolean isUserNameChanged() {
//    }
//    private boolean isPhoneNoChanged() {
//    }
//    private boolean isAddressChanged() {
//    }
}
