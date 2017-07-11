package cg.ndokisteam.kitokotest.camera_activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cg.ndokisteam.kitokotest.MainActivity;
import cg.ndokisteam.kitokotest.R;

public class ResultActivity extends AppCompatActivity {

    TextView titre,acceuil,nomUser;
    ImageView photo;
    int  sexe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        titre = (TextView) findViewById(R.id.tvTitreResultat);
        acceuil = (TextView) findViewById(R.id.tvResultat2);
        nomUser = (TextView) findViewById(R.id.tvNomUserResultat);
        photo = (ImageView) findViewById(R.id.photoResult);


        Intent intent = this.getIntent();
        String nom = intent.getExtras().getString("NOM");
       sexe = intent.getExtras().getInt("SEXE");
       byte[] image = intent.getExtras().getByteArray("IMAGE");


        nomUser.setText(nom.toUpperCase());
        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);//Decopression en Bitmap
        photo.setImageBitmap(bitmap);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sexe == 0)
                Snackbar.make(view, "Oza HOMME", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                if (sexe == 1)
                    Snackbar.make(view, "Oza FEMME", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
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
            startActivity(new Intent(ResultActivity.this,CameraActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    //init methode
//    private void init()
//    {
//        titre = (TextView) findViewById(R.id.tvTitreResultat);
//        acceuil = (TextView) findViewById(R.id.tvResultat2);
//        nomUser = (TextView) findViewById(R.id.tvNomUserResultat);
//        photo = (ImageView) findViewById(R.id.photoResult);
//
//    }

}
