package cg.ndokisteam.kitokotest.camera_activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import cg.ndokisteam.kitokotest.MainActivity;
import cg.ndokisteam.kitokotest.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class CameraActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();

    ImageView ivCamera,ivGallerie,ic,ig;
    CircleImageView ivPhoto;
    ImageButton DialogueMedia;
    Dialog dialog;

    //Views de saisis

    EditText nomUser;
    private RadioGroup mGroup;

    //Constantes pour les radioButton
    final int HOMME_ID = 0;
    final int FEMME_ID = 1;


    String nom;
    int sexe;
    byte[] image;

    static final int REQUEST_CAMERA_IMAGE = 13323;
    final int GALLERY_REQUEST = 22131;

    GalleryPhoto galleryPhoto; //PhotoUtil Librairie

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        galleryPhoto = new GalleryPhoto(getApplicationContext());

        init();
        dialogueMedia();

//        //Desactive le btn camera s'il n'y a pas de camera
//        if (!hasCamera())
//            ivCamera.setEnabled(false);

        //CAMERA
        ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Active la CAMERA
                lanceCamera();
                dialog.dismiss();
            }
        });

        //GALLERIE
        ig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY_REQUEST);
                dialog.dismiss();
            }
        });

        //Bouton Dialogue
        DialogueMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show(); //Affiche dialogue
            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendData();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cam, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id ==android.R.id.home) {
            startActivity(new Intent(CameraActivity.this,MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void init()
    {
        ivCamera= (ImageView) findViewById(R.id.ivCam);
        ivGallerie = (ImageView) findViewById(R.id.ivGallerie);
        ivPhoto = (CircleImageView) findViewById(R.id.ivImageUser);
        DialogueMedia = (ImageButton) findViewById(R.id.ibDialogueMedia);

        nomUser = (EditText) findViewById(R.id.edName);
        mGroup = (RadioGroup) findViewById(R.id.group);
    }

    //Faire si user n'a pas de CAMERA

    private Boolean hasCamera()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Prendre une photo
    private void lanceCamera()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //prendre la photo et affecter le resultat
        startActivityForResult(intent,REQUEST_CAMERA_IMAGE);
    }

    //Returner l'image prise
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CAMERA_IMAGE){
                //get photo
                Bundle extras = data.getExtras();
                Bitmap photo = (Bitmap) extras.get("data");
                ivPhoto.setImageBitmap(photo);
                try {
                    Bitmap bitmap = ImageLoader.init().from("data").requestSize(512, 512).getBitmap();
                    ivPhoto.setImageBitmap(photo);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Erreur chargement de la photo...", Toast.LENGTH_SHORT).show();
                }

            }
            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();

                galleryPhoto.setPhotoUri(uri);
                String photoPath = galleryPhoto.getPath();
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivPhoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Erreur chargement de la photo...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Dialogue Creation
    public void dialogueMedia()
    {
        dialog = new Dialog(this);
        //titre
        dialog.setTitle("Prendre une photo");
        //Content
        dialog.setContentView(R.layout.dialog_media);
        ic = (ImageView) dialog.findViewById(R.id.ivCam);
        ig = (ImageView) dialog.findViewById(R.id.ivGal);

    }

    //Recuperation des donnees Utilisateur
    private void sendData()
    {
        Intent intent = new Intent(CameraActivity.this,ResultActivity.class);
        image = imageViewToByte(ivPhoto);

        if ( mGroup.getCheckedRadioButtonId() == R.id.radio1) //HOMME
        {
            nom = nomUser.getText().toString();
            sexe = HOMME_ID;
            intent.putExtra("NOM",nom);
            intent.putExtra("SEXE",sexe);
            intent.putExtra("IMAGE",image);

            startActivity(intent);

        }
        if ( mGroup.getCheckedRadioButtonId() == R.id.radio2)
        {   //FEMME
            nom = nomUser.getText().toString();
            sexe = FEMME_ID;
            intent.putExtra("NOM",nom);
            intent.putExtra("SEXE",sexe);
            startActivity(intent);
        }
    }

    //Compress de l'mage en Byte

    private byte[] imageViewToByte(ImageView image)
    {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }


}
