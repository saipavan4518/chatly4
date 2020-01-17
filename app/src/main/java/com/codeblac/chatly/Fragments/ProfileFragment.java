package com.codeblac.chatly.Fragments;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codeblac.chatly.R;
import com.codeblac.chatly.UserInfo;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class ProfileFragment extends Fragment {

    CircleImageView image_profile;
    TextView username;

    DatabaseReference dbr;
    FirebaseUser fbu;

    StorageReference storageReference;
    private static final int IMAGE_REQUEST=1;
    private Uri imageUrl;
    private StorageTask uploadTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_profile, container, false);

        image_profile=v.findViewById(R.id.profile_pic);
        username=v.findViewById(R.id.username);

        storageReference= FirebaseStorage.getInstance().getReference(("uploads"));


        fbu = FirebaseAuth.getInstance().getCurrentUser();
        dbr= FirebaseDatabase.getInstance().getReference().child("users");
        dbr.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getActivity() == null) {
                    return;
                }
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    UserInfo ui=ds.getValue(UserInfo.class);
                    if(String.valueOf(ui.getUid()).equals(fbu.getEmail())){
                        username.setText(ui.getFull_Name());
                        if(String.valueOf(ui.getImageUrl()).equals("default")){
                            image_profile.setImageResource(R.drawable.contact);
                        }else{
                            Glide.with(getContext()).load(ui.getImageUrl()).into(image_profile);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });


        return v;
    }
    private void openImage(){
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(getContext());
        pd.setMessage("Uploading");
        pd.show();

        if(imageUrl != null){
            final StorageReference fr = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUrl));
            uploadTask = fr.putFile(imageUrl);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fr.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri download=task.getResult();
                        String muri = download.toString();

                        dbr = FirebaseDatabase.getInstance().getReference("users").child(fbu.getUid());
                        HashMap<String, Object> map=new HashMap<>();
                        map.put("imageUrl",muri);
                        dbr.updateChildren(map);
                        pd.dismiss();
                    }else{
                        Toast.makeText(getContext(),"Failed!",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage()+".sai",Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else{
            Toast.makeText(getContext(),"No Image Selected",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUrl = data.getData();

            if(uploadTask !=null && uploadTask.isInProgress()){
                Toast.makeText(getContext(),"Upload is in Process",Toast.LENGTH_SHORT).show();
            }else{
                uploadImage();
            }
        }
    }
}
