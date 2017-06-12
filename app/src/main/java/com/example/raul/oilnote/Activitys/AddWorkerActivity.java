package com.example.raul.oilnote.Activitys;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.raul.oilnote.R;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.example.raul.oilnote.Utils.ImageHelper.rounderImage;


public class AddWorkerActivity extends BaseActivity {

    protected EditText name_worker, nick_worker, phone_worker;
    protected ImageView imagen_worker;
    protected static int ACT_CAMARA = 2;
    protected static int ACT_GALERIA = 1;
    protected static Uri fotoGaleria;
    protected static InputStream is;
    protected static BufferedInputStream bis;
    protected static Bitmap bitmap;
    protected static OutputStream os;
    protected Bundle datos;
    protected File path;
    protected File fich_salida;
    protected long nreg_afectados = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState,R.layout.activity_add_worker);

        // EditText:
        name_worker    = (EditText) findViewById(R.id.et_add_worker_name);
        phone_worker   = (EditText) findViewById(R.id.et_add_worker_phone);

        // ImagenView:
        imagen_worker  = (ImageView) findViewById(R.id.imagen);

        // Dialogos para los mensajes de información.
        alert = new AlertDialog.Builder(this);

       /*// Convierte nuestra imagen a formato mapa de bits.
        Bitmap myBitmap = decodeBase64("/9j/4AAQSkZJRgABAQAAAQABAAD/2wCEAAkGBxITEBUSEBIVEBUVEBUVFRUVDw8QEBUVFRUWFxUVFRUYHSggGBolHRUVITEhJSkrLi4uFx8zODMtNygtLisBCgoKDg0OFw8QFSsdFR0rLS0rLSstLSsrKystLSstLSsrKy0rLS0tLS0rLS0tLSstLS0uLTgrNy0tLS0tLTc3K//AABEIAK4BIgMBIgACEQEDEQH/xAAcAAABBQEBAQAAAAAAAAAAAAABAAIDBAUGBwj/xAA+EAACAQIDBQUFBgUDBQEAAAAAAQIDEQQhMQUSQVFhBiJxgZETMqGxwRRSYnKS0QcjQrLhM/DxJYKDouIV/8QAGQEAAwEBAQAAAAAAAAAAAAAAAAECAwQF/8QAIBEBAQACAgIDAQEAAAAAAAAAAAECEQMhMUESEzJhBP/aAAwDAQACEQMRAD8A9Kxr7jXNqP6ml9SZsgxGcoL8Tl+lP6tExCyEANwAMDY4bU0fgxUnzdtStv4irPXerTefWTZb2dEz8Uv5srffl/czX2bGyDK9NMJ21oPIkgQ0ZEqly0MK68UtKZZpVORUisyxC3AhrGxhKzbTOm2biLqyzZxmHm014HU7KnGNiKt0EleNnqLDuys8mROKnZxl6PJkW2YyUFKLzWpKV2tKMWr8R1ndPVFWEPbUVfJ5O/UWGquL3J+WYy0s1J3MrExztz4dDRb/AOTPxLzuG1SdOY7J3o4qphm3bevHlbVfA7hnG4yLp4yjVWW89x8nyT9Wdiztwu5t5nNj8crDWNY5gLZGMax7AwCLcVw2CEASCBIIwQUIIjJDgDkMCh1gJDkMDYQbBEZlr1b/AHaf9z/+SYjorOb5zt+mKXzbJSYYCEIYJgYQCJ86bWp7uKqq27arPLS3eZdwDyJu3OH3No10vv3y/FFMrYAnLw142nTZMpFa45SMbHTKv0WixCHFFTDouQIrWJqnBrmbWxsQpTcZcv8AfzMTe4BwOKtNZ816cGKxUrrJb2GTb70N5Z9HoaOPxSnhZzjnaN/K6uZ+CrwqQdOealFxfg1Yq9mZN0qlCWbjv038UKRWttPZOIe4nexbrw3rTWdnmjF2PV3sLe+asvR5/U0sJibLmnn6isC5cqYiJLSmmr6tv06CrRyENue27Fbil92cXyep1dN3inzSOY2/C9CouO7fzWZ0uGjanHnur5HVw/l5/wDp/RzGscxrNnMDGMeNkAMiOFFBAAFCsFDBWCJCACh1gIcgMUOQEOQwQQCAG4L/AE0/vXl+qTf1ROMpQ3YqPKKXorDiFEIQgKkATAwDxL+JUf8AqVXrGm9Lf0r9jOwMbK50P8VcO1jYzaynQjZ8Hutr1MTCruLwJy8NONYgyRIhTKWOxu7xz5EadG9N6hVS1J1jaelzh6+15vJX9GVftNR3Vm79GH1leZ6G8fTtlJfUasbSu3e9+qSueeXq8IyHQ9t4eLQ/riftt9PSNnbaipZO+fPI2di7QX2ypbSe7O3jFX+KPMNnb6kndXsdVsCq1X3r8EvJEZ4yeG/Fnb1W7U2h7L2sFlarJL1bXwKOM7RulNOPuvXj4WRW7SQtUk1pJ7z8bf4OUxk09WwwkLlyu+ndPtrBZ2Suud8+fiTx7ZqUVpe/PU8tcIt2Tm+iSZp4PBwdlKU4cm47v+C7hixmWfp6Xhdu0q0lBu29l08Oh2SPPOzmwacZxbbm95ccvQ9EHx69M+be5s1iYhGjAxjJD2MkgIbCsKwQBBAgoZkIIUAJDkxIIAUOQEOAwEEIA5iAIShQLiAIiOX7S9rlhqnsoU/azSTleW7GN80stXY6c8Z7eTnDaFZXaTlf1jGxNrTixmV7Tdte0FPG0YNU5Uq1OWl1KEoPW0vo0YmyZ71Np6xdmRU81fiS4B2nPqkxb21uExu4mmyF0o6yS5ktcoYqfPRZkmkc2/cjbq9X5DlsyrLi36fJFSW02ov2Ud5xV3J6JeBBHamK3v8AVlHK/daivDJFapfLGLVXY1VafsU6mHnB95M9BxGwJOlGpCrUs4Rk1Lvbt7atLm7HOYrCVL2nKO6nx970Qt6V8ZWPQbOx7JYdzkuZk0qSupWOu7IYe0nJZGeV6a8Uu0XbHZs2oWXjZcTicRs9r3nbM9px1FTirrO3Hmked7StdudKO9ezeaeXNcxYZKzx7YGGqQoSipRV5W70soxV9XbM6rZ2PlVpS3KVKuoQjKcY78Z2ldPd3spWatw1MDF4TD183KdOVks4qrTsvRpnQ9m4UMMqn832sppRShTlG0Vmve4tmts0w1lvppdlMZSnUSpvLebUZZTjl7rXRnbnM9n9nRjU33FRbTllla/DqdMPj1pz8/6NAEDNGJo16ocNWoGNhCEMqSCgDkAJsKAFAZyHIahyQA4KFYNgBCDYAGQkIAlCAQBELOI/iN2cdaH2ikrzhHvq2bgs1JdVnfp4HatgDR45XG7jwDCvJobs+X82XVfU6Httsf7LinKCtSqLejyV/eS8H8Gc/ho7s0+Dbs7menXld47acolPEUEy8hkokkz6NLdeSXVElKNKLT9lezyTnJwXkWVQuPhgZPkP5HMV2rtypOO7vNLlG8Y+fMqKG87st4bZTavJ+RNiKCirEXJpMVJ+6dN2Srd6KTt9Tlt/gbvZt2mmhWLwvb0TFxsocVf5nJdoMBee9bVZnUYmXcV+hBRjGorO2Rnequzbz17Ek3kvibex9gSj3pq1s0nbM62ODguC6ZBrQtEdzqLgiwPvr8rNEz8Gu+30L1zp4vy8/n/ZzGsG8Js0ZENjxCNj9QBwgCGQjhoQAjhoQB6JIozqlR+0S6l+pK0b2b6RV36BDSpCYYcM9RYtWsn4jMAEaQhbBzAZ2zdrxrOSirOOtndZ9bF9MkxFcDBcZCBsFwCDL7S7GjiqDpuyks4SfB8n0Z5DjsE6ctyStKFSzT4PQ9xucP8AxH2ZD2axCVpb8Yzto1Z2b6q1ibGuGeunH2JFAYpFiBlk6MU+HwyZpUqaitDOoz5FuMzNtiuOaSMHbeJsnbW+nO5pVauRi7XnaPXeTHjOxb0hp4aUVdu50ewI95M53DY5TVtOhu7OxG7oXkMPL0CM96nfkc/j6rozUk+7Nu/R/syfZu0MmuY/EU6dWEk3yt0Mmtumjs/Gqcd5cixWlll4nK7NlUi3BOzTtpn6HR1J5E1NPwD70vBF0zdj1FL2kl9/d84rP5mjc7MOsY8zlu86I1iAWzIEUKTCBFcWfQQhkKY6/QamOuBlcTkAUgCjTler6mgsalqpfpMWvUnGV4q+VtGVntma1h8xm6iliYyeV/NNBxU7yOZp9oLaw+JpYTa0KrSSafhl6hTbMYq3kISmugiQp0aMY5RiororIkuM3g3AzrguC4LiAguC4AA3MLtnBSwk0+kvRo3LnOdtcQo4eabzklFdW3f5JionmPPE8vDL0Jqcirv59H8ySLzyMsnXFym7E6mUb8i1SldGbXGpKkjA25iFdJcjWxzdnumFV3W7T7kvxZJ+DLwkZ8m2VKs73Tszd2ftW672TSzdsmUJ4SH3o+pq7JoUN1xqSV3xNMtaThMttDAbRlLODvfJZM6jZmCqKDbvbW/G5iYHEYbDWiv5rvdbub9DbobUr1e5Cl7KL/qlwXgZX+NvryvdWMOu/fVrKXhwNDE1FFdbFCGBlSqXct5SWbeWZJiJb0lBZ3fmZeae9Rr7GoblFLS7lJ+Mncu3GxVkkuCsG52zw823d2NwNjWwDIZBuMbHAQ3CNuFMZHII24gM4ILiAFJEcqKfBehIJIWjV3hIcYp+RNRpJaJLyH8ApFA+4gCEbMccQtKtOX5qEl8Yz+gY1MQtY0ZeE6kPg4smuJMlVRPGVVrQb/LWpy+ErDlj3xo1o/8AjU/7GyRSCpC2SH/9Sl/U5R/NRqx+cR0do0XpVh+uKfoyffI604qLc7WWrlay9QI5VovNSi10kmjzPtntn21VqD/lwdov7z4y8ORc7TbYpze5QhCMeM1CMZy8LaR+ZyVcm5NsMPdOhK6LFGpddTPpSzsTdURW0aKWQ+iyvh6t0Tx1Iq4tQjcq7TwcZa5+RY9pZEdaV0EX5jHpbDp3zWRq4Xs7Q+7/AOzGRv4DMRiJpd39jTdLHKY+nW7H2bTppZRj6XfmblNRR5/RxdSTVm0rJaHRYGrJRzlfxM8o0+7fWm5i8ReOZHsqO9Vvwir/AERlVazZv7EpONPeavvO+nAXHqXthzW3HpqAZGqi8PgOOrcrgssFsDEwZjIlqOGoNwAhTG3EgI4SBcSGD7iQ24UJR1xyGoIAWw3GiHszriG3CIKdxXI7icidqPTDvGZidrQjlHvv0j6mZiMbUnq7Lksl/ki5yLnHa2cZtaEMl33yWnmzm9pYmpW995cIr3V+/mSRpDo0TK52tseORzuNwjWaMqvE7avhrrM53aeAad0KVemA45j1IklAiki9piaE92XR6/uXozyMuM+D0+RLTnu5PTgxU404zy5jWQpj4TT0CRXySt3K9bMs02r2vYtKguFnlcZKOEi07L/B0VBdy7fqZkJfDUmjiU9NPC2grNnLI0qbUnGMdW7eXFnb4GcXFR0skrHNdmsF3fazVnJZJ8Fz8zbh3WZU7GusMnwRWxGDtnHL5FrDVrlmauhy2eEWS+WHK61QrmlVoFCvT3czbDl9VjnxdbxRxYRq0EbuY64bjUwgDhDbiuBnJhTGoIA9MKYy4rgEjBcaEZiIFxCDJxWKjCN5Pw5sw8VjZVHZvdjyX1KeIxEqkt55clyXLxA6dRvJxS8Gznyz9OrDDXaeKJ6cCBUZ3SWfN5JLwRfw+4nu70b8royraBTpkvsi5RpLgSVaCaEemZOBUxeHTRo1YlWswGnK47AZ3sZFWm1qjssSkzKxGCvoi5kmxzU0MVW2TzXyNPE7OfAzatKUdS5YlNSxFvxR+K8CdVE1dO6+JmbrXu5dODHxn0sxhflVatbl4k9DHta6epToXb5nR4HYMGk5N58EK5SF8bfDArY3PK/h8kufA6XYWzJTalVVo5Pdas5fsjX2bsKjHvKCbXF5s2aFDMjLk9ReOGu6u4dWSHyWY1MfFGUXU1GTTNKjVuijGOQ+DsUzq7VKlaN0Txncr1JCDO6cg3G18peI3eO3C7ji5JrJImOQyLHIpAiAxXAxQRtxXAHhQxMKYwfcVxiYbgZ1wkdxCDhaE4vKOdujsXqEf+CKfdVkk789PMs4bDtZylvPwSRxV6MizRgh/wBkpt3cIt891XIXUf8ASk7c218kP2fjFOe400+mcRSHuLEsVGDSlkuDt3fC5bjUT4rpmTQwa0ed+eY2lsqnBuUVZ2vq7eS4AVpk6NyGpgovUt4Oe9BS5lfas3GL/I36B2cm1Wezo8CnWwCNWk7xT5pAjC4yYU8GuRmY3ZkWnZft5HU1KKf+8ivPCgHneK2c4u6z+ZBSoXdrZ+B32I2dF3ZUWAjfJWK+RfFkbMwCvp8DpsPT5cg4XCRiW4xRlbtpMU1DJFzDxK1KlmjRpxshK0O6PpxJFHIMUCbE9FDpxGU2SJZItnYiTI6sh05adUQ12JKpj77t46rTiZjr1vwP/tf7mpUd4srwhdJ9Dp4L5jm554qn9uqrWMX6oMdste9Tfk7k9airMpU6fHmdOnO1qOIUkmk146km8VKWSsSpkKTXDcjTCmAPuOuR3DfIZnphuR7w4AdcQy4hG//Z");

        ImageHelper.rounderImage(myBitmap,imagen);
        imagen.setImageBitmap(myBitmap);*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        menu.findItem(R.id.action_save).setVisible(true);
        menu.findItem(R.id.action_save).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.findItem(R.id.action_discard).setVisible(true);
        menu.findItem(R.id.action_discard).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {

            case R.id.action_save:
                new AddWorkerTask().execute();
                break;

            case R.id.action_discard:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                                startActivityForResult(intent, ACT_CAMARA);
                            }
                        }
                        // Si la opción es la galería la abrirá:
                        if(pos == 1){
                            if(isPermissionGaleryActivated()){
                                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                                startActivityForResult(intent, ACT_GALERIA);
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

        if (requestCode == ACT_GALERIA && resultCode == RESULT_OK) {
            fotoGaleria = data.getData();
            cropCapturedImage(fotoGaleria);
        }

        if (requestCode == ACT_CAMARA && resultCode == RESULT_OK) {
            //bitmap = (Bitmap) data.getExtras().get("data");
            //rounderImage(bitmap,imagen_worker);

            Bitmap imagen = (Bitmap) data.getExtras().get("data");

            //Este metodo nos retornara la url temporal de la imagen tomada
            //Uri ulrImagen = getImageUri(this,imagen);

            rounderImage(cropBitmap(imagen, 400,200),imagen_worker);

            //cropCapturedImage(ulrImagen);
        }

        if(requestCode == 3535 && resultCode == RESULT_OK){

            //Este seria el bitmap de nuestra imagen cortada.
            bitmap = (Bitmap) data.getExtras().get("data");
            rounderImage(bitmap,imagen_worker);
        }
    }

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

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

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

    class AddWorkerTask extends AsyncTask<Void,Void,JSONObject>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            onProgressDialog(AddWorkerActivity.this, getResources().getString(R.string.save));
        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            onStopProgressDialog();

            if(jsonObject != null){

            }
        }
    }


}
