package cg.ndokisteam.kitokotest;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
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
import java.util.ArrayList;
import java.util.Random;

import cg.ndokisteam.kitokotest.camera_activity.ResultActivity;
import cg.ndokisteam.kitokotest.data.MessageDao;
import cg.ndokisteam.kitokotest.models.Message;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    //Variable de la base de donnes

    MessageDao messageDao;
    private ArrayList<Message> msgListe;
    private Message message;
   // private ArrayList<Message> femmes;

    Random random;
    ArrayList<cg.ndokisteam.kitokotest.models.Message>list;
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


    String nom,kitokoMessage,validation;
    int sexe;
    byte[] images;
    Long type;

    static final int REQUEST_CAMERA_IMAGE = 13323;
    final int GALLERY_REQUEST = 22131;

    GalleryPhoto galleryPhoto; //PhotoUtil Librairie

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        galleryPhoto = new GalleryPhoto(getApplicationContext());

        init();
        messageDao=new MessageDao(this);
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
                //MESSAGE POUR HOMME
                images = imageViewToByte(ivPhoto);
                nom = nomUser.getText().toString();

                if ( mGroup.getCheckedRadioButtonId() == R.id.radio1)
                {
                    sexe = HOMME_ID;
                    //Gestion des Message
                    message = getOneMessage(sexe);
                    kitokoMessage = message.getMessage();
                    validation = message.getValidation();
                    type = message.getTypeMessage();

                    //Envoyer les donnees
                    sendData(nom,images,kitokoMessage,validation,type);
                  //  Toast.makeText(MainActivity.this,message.getValidation()+" ",Toast.LENGTH_LONG).show();
                }

                //MESSAGE POUR FEMME
                if ( mGroup.getCheckedRadioButtonId() == R.id.radio2)
                {
                    sexe = FEMME_ID;
                    //Gestion des Message
                    message = getOneMessage(sexe);
                    kitokoMessage = message.getMessage();
                    validation = message.getValidation();
                    type = message.getTypeMessage();
                    //Envoyer les donnees
                    sendData(nom,images,kitokoMessage,validation,type);

                }
                finish();
            }
        });
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

//        //noinspection SimplifiableIfStatement
//        if (id ==android.R.id.home) {
//
//            return true;
//        }

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
    public void sendData(String n,byte[]images,String message,String validation,Long type)
    {
        Intent intent = new Intent(MainActivity.this,ResultActivity.class);
        String stringType = String.valueOf(type);
        intent.putExtra("NOM",n);
        intent.putExtra("MESSAGE",message);
        intent.putExtra("VALIDATION",validation);
        intent.putExtra("TYPE",stringType);
        intent.putExtra("IMAGE",images);
        startActivity(intent);
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



    //Recuperation des donne de la base

    private Message getOneMessage(int sexe)
    {
        int index;
        Message oneMessage = new Message();
        msgListe = messageDao.getMessagesGender((long) sexe);

        for (int i=0;i<msgListe.size();i++) {

            random = new Random();
            index = random.nextInt(msgListe.size());
            oneMessage = msgListe.get(index);
        }
        return  oneMessage;
    }



}


