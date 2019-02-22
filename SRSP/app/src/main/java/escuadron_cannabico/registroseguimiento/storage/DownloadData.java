package escuadron_cannabico.registroseguimiento.storage;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.libre.escuadroncliente.ui.util.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.libre.escuadroncliente.ui.util.Constants.CSV_FILE;
import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE;
import static com.libre.escuadroncliente.ui.util.Constants.JSON_FILE_CONFIG;
import static com.libre.escuadroncliente.ui.util.Constants.URL_REMOTE;
import static com.libre.escuadroncliente.ui.util.Data.saveJSONFile;
import static escuadron_cannabico.registroseguimiento.utils.Constants.CSV_FILE;
import static escuadron_cannabico.registroseguimiento.utils.Constants.JSON_FILE;
import static escuadron_cannabico.registroseguimiento.utils.Constants.URL_REMOTE;

public class DownloadData {

    private FirebaseStorage storage;
    private FirebaseAuth auth;
    final long ONE_MEGABYTE = 1024 * 1024;
    private PreferencesStorage preferences;
    private DBHelper db;

    public void DownloadData(Context context){

        storage=FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();
        preferences=new PreferencesStorage(context);
        db=new DBHelper(context);

    }


    public void DBM(){


            StorageReference fileRef = storage.getReferenceFromUrl(URL_REMOTE).child(JSON_FILE);
            if (fileRef != null) {
                fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        try {
                            String str = new String(bytes, "UTF-8");


                        }catch (UnsupportedEncodingException ex){
                            ex.printStackTrace();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {

                    }
                });
            }

    }
    public void DBR(){


        StorageReference fileRef = storage.getReferenceFromUrl(URL_REMOTE).child(CSV_FILE);
        if (fileRef != null) {
            fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try {
                        String str = new String(bytes, "UTF-8");


                    }catch (UnsupportedEncodingException ex){
                        ex.printStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }

    }

    public void CNF(){


        StorageReference fileRef = storage.getReferenceFromUrl(URL_REMOTE).child(JSON_FILE_CONFIG);
        if (fileRef != null) {

            fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    try{

                        saveJSONFile(bytes, "config");
                        JSONObject dataObject = Data.loadJSONFileObjet("configuracion", "config");
                        JSONArray items = dataObject.getJSONArray("items");
                        JSONObject jsonObject = items.getJSONObject(0);
                        boolean status=jsonObject.getBoolean("activo");
                        boolean delivery=jsonObject.getBoolean("delivery");
                        JSONArray pay = jsonObject.getJSONArray("pay");
                        List<String> account=new ArrayList<>();
                        for (int a=0;a<=pay.length()-1;a++) {
                            JSONObject jsonObjectAccount = pay.getJSONObject(a);
                            account.add(jsonObjectAccount.get("banco")+","+jsonObjectAccount.get("tarjeta"));
                        }

                        preferences.saveData("REGISTER_USER_ACTIVE", ""+status);
                        preferences.saveData("DELIVER_ACTIVE", ""+delivery);
                        preferences.saveData("PAY_ACCOUNT_ONE", account.get(0));
                        preferences.saveData("PAY_ACCOUNT_TWO", account.get(1));
                        preferences.saveData("PAY_ACCOUNT_THREE", account.get(2));
                        preferences.saveData("PAY_ACCOUNT_FOUR", account.get(3));

                    }catch (JSONException ex){
                        ex.getStackTrace();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {

                }
            });
        }

    }
}
