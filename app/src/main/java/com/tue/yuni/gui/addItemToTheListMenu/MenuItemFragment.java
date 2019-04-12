package com.tue.yuni.gui.addItemToTheListMenu;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.tue.yuni.R;
import com.tue.yuni.models.MenuItem;
import com.tue.yuni.services.network.NetworkService;
import com.tue.yuni.storage.PasswordStorage;
import com.tue.yuni.storage.RemoteStorage;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v4.app.Fragment;

public class MenuItemFragment extends Fragment implements View.OnClickListener, RemoteStorage.RequestCompletedHandler, RemoteStorage.ErrorHandler
{
    private MenuItem menuItem;
    private List<String> categories;

    // Ui Components
    private EditText nameTxt;
    private EditText descriptionTxt;
    private Spinner spinner;
    private ImageView imageView;
    private Button addItem;

    // New parameters
    private String itemName;
    private String itemDescription;
    private String itemCategory;

    //Galery stuff
    private static final int PICK_IMAGE = 100;
    private Uri imageUri;

    //Camera stuff
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private String mCurrentPhotoPath;
    private static final int REQUEST_TAKE_PHOTO = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu_item, container, false);

        // Get UI elements
        nameTxt = v.findViewById(R.id.nameID);
        descriptionTxt = v.findViewById(R.id.descriptionID);
        spinner = v.findViewById(R.id.categoryspinner);
        imageView = v.findViewById(R.id.imageView2);
        addItem = v.findViewById(R.id.button);

        // Initialize UI elements
        ArrayAdapter<String> adapter;
        if (menuItem != null) {
            nameTxt.setText(menuItem.getName());
            descriptionTxt.setText(menuItem.getDescription());
            addItem.setText(getContext().getString(R.string.done));
        }
        if (categories != null) {
            // Create an ArrayAdapter using the string array and a default spinner layout
            adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
        }
        // Set default spinner value
        if (categories != null) {
            spinner.setSelection(categories.indexOf(itemCategory));
        } else {
            spinner.setSelection(0);
        }

        //Camera button initialization
        Button photoButton = v.findViewById(R.id.button2);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        //Initialization of Add Dish button
        addItem.setOnClickListener(this);

        // Restore data if screen was rotated
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("img")) {
                imageUri = savedInstanceState.getParcelable("img");
                imageView.setImageURI(imageUri);
            }
        }

        return v;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imageUri != null) {
            outState.putParcelable("img", imageUri);
        }
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        if (args != null) {
            if (args.containsKey("menuItem")) {
                this.menuItem = args.getParcelable("menuItem");
                itemCategory = menuItem.getCategory();
            } else {
                itemCategory = args.getString("category");
            }
            if (args.containsKey("categories"))
                this.categories = Arrays.asList(args.getStringArray("categories"));
        }
    }

    @Override
    public void onClick(View v) {
        if (v == addItem) {
            if (NetworkService.networkAvailabilityHandler(getActivity().getApplicationContext())) {
                if (descriptionTxt.getText().toString().equals("") || nameTxt.getText().toString().equals("") || imageView.getDrawable() == null) {
                    final String error = "All the fields must be completed";
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Error");
                    builder.setMessage(error);
                    builder.show();
                } else {
                    //Getting parameters
                    itemDescription = descriptionTxt.getText().toString();
                    itemName = nameTxt.getText().toString();
                    itemCategory = spinner.getAdapter().getItem(spinner.getSelectedItemPosition()).toString();

                    // Post Item To Database
                    if (menuItem != null) {
                        RemoteStorage.get().updateMenuItem(PasswordStorage.get().getPassword(),
                                menuItem.getId(), itemName, itemDescription, itemCategory,
                                this, this);
                    } else {
                        RemoteStorage.get().createMenuItem(PasswordStorage.get().getPassword(),
                                itemName, itemDescription, itemCategory, this, this);
                    }


                }
            }
        }
    }

    @Override
    public void onError(Exception e) {
        // ToDo
    }

    @Override
    public void onCompleted() {
        // Build and show Dialog confirming changes have been applied
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Success");
        if (menuItem != null) {
            builder.setMessage(getContext().getString(R.string.itemModified));
        } else {
            builder.setMessage(getContext().getString(R.string.itemAdded));
        }
        builder.show();

        // Leave fragment
        getActivity().onBackPressed();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new
                        Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                openCamera();
            } else {
                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            setPic();
        }
    }

    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    void openCamera(){
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent();
            galleryAddPic();
        }
    }

    private void selectImage(){

        final CharSequence[] items={"Camera","Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Image");

        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    openCamera();
                } else if (items[i].equals("Gallery")) {
                    openGallery();
                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //TODO:
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.tue.yuni.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public void setPic() {
        imageUri = Uri.fromFile(new File(mCurrentPhotoPath));
        imageView.setImageURI(imageUri);
    }
}
