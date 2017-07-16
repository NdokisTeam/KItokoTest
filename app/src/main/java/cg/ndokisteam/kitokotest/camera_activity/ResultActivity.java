package cg.ndokisteam.kitokotest.camera_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import cg.ndokisteam.kitokotest.MainActivity;
import cg.ndokisteam.kitokotest.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ResultActivity extends AppCompatActivity {

    TextView titre,acceuil,nomUser,tvMessage,tvValidation,tvType;
    CircleImageView photo;


    String nom,message,validation,typeMessage;
    MyTaskTest myTaskTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        titre = (TextView) findViewById(R.id.tvTitreResultat);
        acceuil = (TextView) findViewById(R.id.tvResultat2);


        nomUser = (TextView) findViewById(R.id.tvNomUserResultat);
        photo = (CircleImageView) findViewById(R.id.photoResult);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvType = (TextView) findViewById(R.id.tvType);
        tvValidation = (TextView) findViewById(R.id.tvValidation);

        Intent intent = this.getIntent();
         nom = intent.getExtras().getString("NOM");
        message = intent.getExtras().getString("MESSAGE");
        validation = intent.getExtras().getString("VALIDATION");
       byte[] image = intent.getExtras().getByteArray("IMAGE");
        typeMessage = intent.getExtras().getString("TYPE");

        Bitmap bitmap = BitmapFactory.decodeByteArray(image,0,image.length);//Decopression en Bitmap

        //nomUser.setText(nom.toUpperCase());
       // photo.setImageBitmap(bitmap);

        //Gestion de ma Tache
        myTaskTest = new MyTaskTest(ResultActivity.this,nomUser,photo,tvMessage,tvValidation,tvType,bitmap,nom,message,validation,typeMessage);
        myTaskTest.execute("get",nom,message,validation,typeMessage);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                Snackbar.make(view, message+" et "+validation, Snackbar.LENGTH_LONG)
////                        .setAction("Action", null).show();
//            }
//        });
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
            startActivity(new Intent(ResultActivity.this,MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //CREATION DE LA TACHE

    static class  MyTaskTest extends AsyncTask<String,Integer,String>
    {
        ProgressDialog progressDialog;

        Context context;
        TextView tvName,tvValidation,tvType,tvMessage;
        CircleImageView images;
        Bitmap photo;

        //Les Datas
        String nom;
        String message;
        String validation ;
        String type;

        public MyTaskTest(Context context, TextView tvName, CircleImageView images,
                          TextView tvMessage, TextView tvValidation, TextView tvType,Bitmap image,String nom,String message,
                          String validation,String type) {
            this.context = context;
            this.tvName = tvName;
            this.images = images;
            this.tvMessage = tvMessage;
            this.tvValidation = tvValidation;
            this.tvType = tvType;
            this.photo = image;
            //les datas
            this.nom = nom;
            this.message = message;
            this.validation = validation;
            this.type = type;
        }

        @Override
        protected String doInBackground(String... params) {
            int i = 0;
            synchronized (this)
            {
                   while(i<10)
                   {
                       try {


                           wait(1000);
                           i++;
                           publishProgress(i);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }

                   }
            }
            // return "Le scan de votre profile est terminé";
            return  "Le scan de votre profile est terminé ";
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMax(10);
            progressDialog.setMessage("Demarrage du scan de votre profile ");
            progressDialog.setProgress(0);
            progressDialog.setProgressStyle(progressDialog.STYLE_SPINNER);
            progressDialog.show();
            //   super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String  result) {
            //progressDialog.setTitle("Scannage du profile");
            Toast.makeText(context,result,Toast.LENGTH_LONG).show();
            //progressDialog.setMessage(result);
            this.tvName.setText(this.nom);
            this.tvMessage.setText(message);
            this.tvValidation.setText(validation);
            this.tvType.setText(type);
            this.images.setImageBitmap(photo);
            progressDialog.hide();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            progressDialog.setProgress(progress);
            progressDialog.setMessage("Scan du profile est en cours...");
            // super.onProgressUpdate();
        }
    }

}
