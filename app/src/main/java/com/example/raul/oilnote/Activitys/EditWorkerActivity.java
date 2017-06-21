package com.example.raul.oilnote.Activitys;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.raul.oilnote.R;
import com.example.raul.oilnote.Utils.Codification;
import com.example.raul.oilnote.Utils.Connection;
import com.example.raul.oilnote.Utils.ImageHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.example.raul.oilnote.Utils.GlobalVars.BASE_URL_WRITE;
import static com.example.raul.oilnote.Utils.ImageHelper.rounderImage;

public class EditWorkerActivity extends BaseActivity {

    protected EditText name_worker, phone_worker;
    protected String cod, name, phone, photo;
    protected AlertDialog.Builder alert;
    protected ImageView imagen_worker;
    protected static int ACT_CAMARA2 = 2;
    protected static int ACT_GALERIA2 = 1;
    protected static Uri fotoGaleria;
    protected Bitmap bitmap;
    protected Connection connection;
    protected JSONObject jsonObject;
    protected Toolbar toolbar;
    protected Bundle bundle;
    protected String photocamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_worker);

        // Bundle:
        bundle          = getIntent().getExtras();

        // String:
        cod             = bundle.getString("cod");
        name            = bundle.getString("type_expense");
        phone           = bundle.getString("phone");
        photo           = bundle.getString("photo");

        // EditText:
        name_worker     = (EditText) findViewById(R.id.et_edit_worker_name);
        phone_worker    = (EditText) findViewById(R.id.et_edit_worker_phone);

        // ImagenView:
        imagen_worker   = (ImageView) findViewById(R.id.imagen);

        // Dialogos para los mensajes de información:
        alert           = new AlertDialog.Builder(this);

        // JsonObject:
        jsonObject      = new JSONObject();

        // Clase conexión:
        connection      = new Connection();

        // Toolbar:
        toolbar         = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null){
            toolbar.setTitle(R.string.name);
            setSupportActionBar(toolbar);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Rellenar campos de la actividad:
        loadDataWorker();
    }

    // Relleno los campos con los datos del trabajador:
    public void loadDataWorker(){
        // Nombre:
        name_worker.setText(name);

        // Teléfono (de tenerlo):
        if(!phone.equals("")){
            phone_worker.setText(phone);
        }else{
            phone_worker.setText("");
        }

        // Foto (de tenerla):
        if(!photo.equals("")){
            ImageHelper.rounderImage(photo,imagen_worker);
        }else {
            imagen_worker.setImageResource(R.drawable.user_white);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_save_edit).setVisible(true);
        menu.findItem(R.id.action_save_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_save_edit:
                if(checkData()){
                    new EditWorkerTask().execute();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonEdit(View v){
        if(checkData()){
            new EditWorkerTask().execute();
        }
    }

    public boolean checkData(){
        if(name_worker.length() <= 2){
            name_worker.setError(getResources().getString(R.string.name_small));
            name_worker.requestFocus();
            return false;
        }
        return true;
    }

    public void onClickOptionCamera(View v){
        final CharSequence[] items = new CharSequence[2];

        items[0] = getResources().getString(R.string.from_camera);
        items[1] = getResources().getString(R.string.from_galery);

        alert.setTitle(getResources().getString(R.string.options))
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int pos) {

                        // Si la opción es la camara la abrirá:
                        if(pos == 0){
                            // Comprueba si tiene los permisos de cámara activados la aplicación:
                            if(isPermissionCameraActivated()){
                                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                startActivityForResult(intent, ACT_CAMARA2);
                            }
                        }
                        // Si la opción es la galería la abrirá:
                        if(pos == 1){
                            if(isPermissionGaleryActivated()){
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                startActivityForResult(intent, ACT_GALERIA2);
                            }
                        }
                    }
                });
        alert.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface alert, int which) {
                        // TODO Auto-generated method stub
                        alert.dismiss();
                    }
                });
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ACT_GALERIA2 && resultCode == RESULT_OK) {
            fotoGaleria = data.getData();
            cropCapturedImage(fotoGaleria);
        }

        if (requestCode == ACT_CAMARA2 && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            Bitmap bmp = (Bitmap) data.getExtras().get("data");
            photocamera =  Codification.encodeToBase64(bmp,99);
            rounderImage(cropBitmap(bmp, 400,200),imagen_worker);
        }

        if(requestCode == 3535 && resultCode == RESULT_OK){
            bitmap = (Bitmap) data.getExtras().get("data");
            photocamera =  Codification.encodeToBase64(bitmap,99);
            rounderImage(bitmap,imagen_worker);
        }
    }

    // Recorta automaticamente la imagen tomada con la cámara:
    public Bitmap cropBitmap(Bitmap original, int height, int width) {
        Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(croppedImage);

        Rect srcRect = new Rect(0, 0, original.getWidth(), original.getHeight());
        Rect dstRect = new Rect(0, 0, width, height);

        int dx = (srcRect.width() - dstRect.width()) / 2;
        int dy = (srcRect.height() - dstRect.height()) / 2;

        // If the srcRect is too big, use the center part of it.
        srcRect.inset(Math.max(0, dx), Math.max(0, dy));

        // If the dstRect is too big, use the center part of it.
        dstRect.inset(Math.max(0, -dx), Math.max(0, -dy));

        // Draw the cropped bitmap in the center
        canvas.drawBitmap(original, srcRect, dstRect, null);

        original.recycle();

        return croppedImage;
    }

    // Intent con el cual recortamos la imagen sacada de la galeria, para ajustar su tamaño de necesitarlo:
    public void cropCapturedImage(Uri urlImagen){

        //inicializamos nuestro intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        cropIntent.setDataAndType(urlImagen, "image/*");

        //Habilitamos el crop en este intent
        cropIntent.putExtra("crop", "true");

        cropIntent.putExtra("aspectX", 1);
        cropIntent.putExtra("aspectY", 1);

        //indicamos los limites de nuestra imagen a cortar
        cropIntent.putExtra("outputX", 400);
        cropIntent.putExtra("outputY", 250);

        //True: retornara la imagen como un bitmap, False: retornara la url de la imagen la guardada.
        cropIntent.putExtra("return-data", true);

        //iniciamos nuestra activity y pasamos un codigo de respuesta.
        startActivityForResult(cropIntent, 3535);
    }

    // Comprueba si tiene los permisos de cámara activados la aplicación:
    public boolean isPermissionCameraActivated(){
        if(Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                return true;
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
                return false;
            }
        }else{
            return true;
        }
    }

    public boolean isPermissionGaleryActivated(){
        if(Build.VERSION.SDK_INT >= 23){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                return true;
            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                return false;
            }
        }else{
            return true;
        }
    }

    // Le preguntamos al usuario si desea dar permisos a la aplicación para poder usar la cámara:
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, getResources().getString(R.string.permissions_acepted), Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(this, getResources().getString(R.string.permissions_canceled), Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        setResult(RESULT_CANCELED);
        finish();
        return false;
    }

    class EditWorkerTask extends AsyncTask<Void,Void,JSONObject> {

        private HashMap<String, String> parametrosPost = new HashMap<>();
        private String _name, _phone, _photo;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            onProgressDialog(EditWorkerActivity.this, getResources().getString(R.string.save));

            _name = name_worker.getText().toString();

            // Si el campo teléfono está relleno y cumple las condiciones:
            if(!phone_worker.getText().toString().isEmpty() && phone_worker.length() >= 9){
                _phone = phone_worker.getText().toString();
            }else{
                _phone = "";
            }

            // Si se ha tomado una foto para el contacto:
            if(bitmap != null){
                _photo = photocamera;
            }else{
                _photo = photo;
            }
        }

        @Override
        protected JSONObject doInBackground(Void... params) {

            try {
                parametrosPost.put("ins_sql",   "UPDATE workers " +
                                                "SET worker_name = '"+ _name +"', worker_phone = '"+ _phone +"', worker_photo = '"+ _photo +"' " +
                                                "WHERE worker_cod = "+ cod +";");
                jsonObject = connection.sendWrite(BASE_URL_WRITE, parametrosPost);
                Log.e("parametrosPost",""+parametrosPost);
                //UPDATE
                /*Mysql> UPDATE refranero SET fecha="2003-06-01" WHERE ID=1;
                UPDATE tabla SET campo = ‘valor’, campo2 = ‘valor2’ WHERE condición*/

                if (jsonObject != null) {
                    return jsonObject;
                }

            } catch (JSONException e) {
                e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            onStopProgressDialog();

            if (jsonObject != null) {
                try {
                    if(jsonObject.getInt("added") == 1){
                        Toast.makeText(EditWorkerActivity.this, getResources().getString(R.string.successful_edit_worker), Toast.LENGTH_SHORT).show();

                        String _nameExtra    = _name;
                        String _phoneExtra   = _phone;
                        String _photoExtra   = _photo;

                        Intent i = getIntent();

                        i.putExtra("type_expense", _nameExtra);
                        i.putExtra("phone", _phoneExtra);
                        i.putExtra("photo", _photoExtra);
                        setResult(RESULT_OK, i);
                        finish();
                    }else{
                        Snackbar.make(findViewById(R.id.LinearEditWorker), getResources().getString(R.string.error_edit_worker), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Snackbar.make(findViewById(R.id.LinearEditWorker), getResources().getString(R.string.error_edit_worker), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
