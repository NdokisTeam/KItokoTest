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

    ImageView ic,ig;
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
    byte[] images;

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
                //HOMME
                images = imageViewToByte(ivPhoto);
                nom = nomUser.getText().toString();

                if ( mGroup.getCheckedRadioButtonId() == R.id.radio1)
                {
                    sexe = HOMME_ID;
                    sendData(nom,sexe,images);
                }

                //FEMME
                if ( mGroup.getCheckedRadioButtonId() == R.id.radio2)
                {
                    sexe = FEMME_ID;
                    sendData(nom,sexe,images);

                }

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
        DialogueMedia = (ImageButton) findViewById(R.id.ibDialogueMedia);
        nomUser = (EditText) findViewById(R.id.edName);
        ivPhoto = (CircleImageView) findViewById(R.id.ivImageUser);
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
    private void sendData(String n,int sexe,byte[]images)
    {
        Intent intent = new Intent(CameraActivity.this,ResultActivity.class);
            intent.putExtra("NOM",n);
            intent.putExtra("SEXE",sexe);
            intent.putExtra("IMAGE",images);
            startActivity(intent);
             finish();
    }//fin

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
