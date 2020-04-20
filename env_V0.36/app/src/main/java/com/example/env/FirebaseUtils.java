package com.example.env;

import android.graphics.Bitmap;
import android.net.Uri;

import androidx.annotation.NonNull;

import android.util.Log;

import com.example.env.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;


public class FirebaseUtils {

    public interface firebaseCallback<T> {
        void onCallback(T value);
    }

    public static FirebaseUser myCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    public static String currentTelegramID = "";
    public static String currentEmail = "test";
    public static String telegramID = "blyat";
    public static ArrayList<String> bannedUsersList = new ArrayList<>();
    public static UserListings myUserListings = new UserListings();


    static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    // for Firebase Storage
    static FirebaseStorage storage = FirebaseStorage.getInstance();
    // Create a storage reference from our app
    static StorageReference storageRef = storage.getReferenceFromUrl("gs://envfirebaseproject.appspot.com/");

    public static void getAllListings() {
        mDatabase.child("testProducts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get object and use the values to update the UI
                Log.d("HOME_TAG", "getting value");
                Object allListing = dataSnapshot.getValue();
                // ...
                HashMap allListingHashmap = ((HashMap) allListing); // cast this bitch into a hashmap
                Log.d("HOME_TAG", allListingHashmap.toString());

                for (Object item : allListingHashmap.values()) {
                    final HashMap itemHashmap = ((HashMap) item); // this is the hashmap of each item
                    System.out.println(itemHashmap);

                    String imageName = String.format("%s.jpg", itemHashmap.get("imgNumber").toString());
                    Log.d("HOME_TAG", imageName);
                    StorageReference imageref = storageRef.child(imageName);

                    imageref.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {
                            Log.d("HOME_TAG", "get image success");
                            Log.d("HOME_TAG", bytes.toString());
                            Bitmap imgBitmap = Utils.byteArrayToBitmap(bytes);
                            Log.d("HOME_TAG", imgBitmap.toString());
                            String imageName = itemHashmap.get("title").toString();
                            String price = itemHashmap.get("price").toString().substring(1);
                            String category = itemHashmap.get("category").toString();
                            String description = itemHashmap.get("description").toString();
                            String user = itemHashmap.get("user").toString();
                            String email = itemHashmap.get("email").toString();
                            String telegramID = itemHashmap.get("telegramID").toString();

                            long listingID = Long.parseLong(itemHashmap.get("imgNumber").toString());
                            Log.d("HOME_TAG", "Name " + imageName +
                                    " PRICE " + price +
                                    " CATE " + category +
                                    " DESC "+ description
                            + " USER " + user
                                    + " MAIL "+ email
                                    + " TELE " + telegramID +
                                    " ID " + listingID);


                            myUserListings.addListing(imageName, price, imgBitmap, category, description, user, listingID,
                                    email, telegramID);
                            Log.d("HOME_TAG", "added item");
                            //Log.d("HOME_TAG", String.valueOf(userListings.userListings));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Log.d("HOME_TAG", String.valueOf(exception));
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("HOME_TAG", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });
    }


    public static void pushListing(long timestamp, String title, String price, byte[] imageBytes, String category, String description, FirebaseUser currentUser) throws InterruptedException {
        // will be the item name in our DB

        //long listingTimestamp = System.currentTimeMillis();
        //Log.d(TAG, "Entering pushListing function");

        String imageName = String.format("%d.jpg", timestamp);

        final StorageReference imageref = storageRef.child(imageName);

        Bitmap myBitmap = Utils.byteArrayToBitmap(imageBytes);

        UploadTask uploadTask = imageref.putBytes(imageBytes);
        System.out.println("upload task created");
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                System.out.println("Upload failed");
                System.out.println(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                System.out.println("Successful upload");
                System.out.println(taskSnapshot.getMetadata());
            }
        });

        System.out.println("preparing for urltask");


        //supposed to get us the URL to download this image
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return imageref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Uri> task) {
                                         if (task.isSuccessful()) {

                                             System.out.println("Get Uri successful");
                                             Uri downloadUri = task.getResult();

                                         } else {
                                             System.out.println("Get Uri failed");
                                         }

                                     }
                                 }

        );

        /*System.out.println("supposedly done getting URL?");

        String stringURL = imageref.getDownloadUrl().getResult().toString();

        System.out.println("declared stringurl");*/

        ListingForDatabase listing = new ListingForDatabase(title, price, String.valueOf(timestamp), category, description,
                myCurrentUser.getUid(), myCurrentUser.getEmail(), currentTelegramID);
        Log.d("UTIL_TEST", listing.getEmail() + " " + listing.getTelegramID());

        Listing listingToBePassed = new Listing(title, price, myBitmap, category, description, currentUser.getUid(),
                timestamp, currentUser.getEmail(), currentTelegramID);
//        myUserListings.addListing(listingToBePassed);
        HomeFragment.homeUserListings.addListing(listingToBePassed);
        
        mDatabase.child("testProducts").child(String.valueOf(timestamp)).setValue(listing);
        //String imageHexString = new String(Hex.encodeHex(imageHex));
        //Log.d(TAG, "Converted to hex string");


    }

    public static void getImage() {

    }

    public static void addUser(String UID, String teleID, String email, Boolean adminRights) {
        UserForFirebase newUser = new UserForFirebase(UID, teleID, email, adminRights);
        mDatabase.child("usersList").child(UID).setValue(newUser);

    }

    public static void getTelegramFromUID(String UID) {
        mDatabase.child("usersList").child(UID).child("teleID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("GET_TELE", "getting Tele id");
                String teleID = (String) dataSnapshot.getValue();
                telegramID = teleID;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("GET_TELE", "get teleID failed");
                telegramID = null;
                System.out.println(databaseError.getDetails());
            }
        });
    }

    public static void updateCurrentTelegramID() {
        mDatabase.child("usersList").child(myCurrentUser.getUid()).child("teleID").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("GET_TELE", "getting Tele id of current user");
                String teleID = (String) dataSnapshot.getValue();
                currentTelegramID = teleID;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("GET_TELE", "get teleID failed");
                currentTelegramID = "";
                System.out.println(databaseError.getDetails());
            }
        });
    }

    public static void updateCurrentEmail() {
        mDatabase.child("usersList").child(myCurrentUser.getUid()).child("email").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("GET_EMAIL", "getting email of current user");
                String emailAdd = (String) dataSnapshot.getValue();
                currentEmail = emailAdd;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("GET_TELE", "get teleID failed");
                currentEmail = "";
                System.out.println(databaseError.getDetails());
            }
        });
    }

    public static void addBannedWord(String word) {
        long timestamp = System.currentTimeMillis();
        mDatabase.child("bannedWords").child(String.valueOf(timestamp)).setValue(word);
    }

    public static void getBannedUsers() {
        mDatabase.child("bannedUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap bannedUsers = (HashMap) dataSnapshot.getValue();
                ArrayList<String> bannedUsersArray = new ArrayList<>(bannedUsers.values());
                for (String user : bannedUsersArray) {
                    bannedUsersList.add(user);
                }
                Log.d("GET_BANNED", bannedUsersList.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println(databaseError.getDetails());
            }
        });
    }
}
