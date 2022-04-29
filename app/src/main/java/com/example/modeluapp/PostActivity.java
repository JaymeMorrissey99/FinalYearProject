package com.example.modeluapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.List;

import Models.Post;

public class PostActivity extends AppCompatActivity {

    Uri imageUrl;
    String myUrl = "";
    StorageReference storageReference;
    DatabaseReference userRef;
    EditText postDescription;
    String uName, profileImage;
    ImageView close, postPicture;
    TextView post;
    StorageTask uploadTask ;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    List<Post> postList;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postDescription = findViewById(R.id.postDes);
        postPicture = findViewById(R.id.postPic);
        post = findViewById(R.id.postBtn);
        close = findViewById(R.id.close);
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("posts");
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, MainActivity.class));
            }
        });

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPicture();
            }
        });

        CropImage.activity()
                .setAspectRatio(1,1)
                .start(PostActivity.this);


    }

    private String getFileExtensions(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            imageUrl = result.getUri();

            postPicture.setImageURI(imageUrl);
        }else {
            Toast.makeText(PostActivity.this,"Searching gone wrong!",Toast.LENGTH_SHORT).show();

            startActivity(new Intent(PostActivity.this, MainActivity.class));
            finish();
        }
    }

    private void uploadPicture() {
        String curUser = firebaseUser.getUid();
        Query q = userRef.orderByChild("id").equalTo(curUser);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot s: snapshot.getChildren()){
                        uName = s.child("Username").getValue().toString();
                        profileImage = s.child("image").getValue().toString();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        String name = firebaseUser.getDisplayName();

        if(imageUrl != null){
            final StorageReference filereferance = storageReference.child(System.currentTimeMillis()
                    + "."+ getFileExtensions(imageUrl));
            uploadTask = filereferance.putFile(imageUrl);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filereferance.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downloadURL = (Uri) task.getResult();
                        myUrl = downloadURL.toString();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Posts");
                        String postid = reference.push().getKey();
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("postid",postid);
                        hashMap.put("postimage",myUrl);
                        hashMap.put("description",postDescription.getText().toString());
                        hashMap.put("username", uName);
                        hashMap.put("uID", curUser);
                        hashMap.put("profileImg", profileImage);

                        reference.child(postid).setValue(hashMap);
                        startActivity(new Intent(PostActivity.this, MainActivity.class));
                    }
                    else{
                        Toast.makeText(PostActivity.this, "Failed to Post!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "no Image Selected!", Toast.LENGTH_SHORT).show();
        }
    }

}