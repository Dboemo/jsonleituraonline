package com.example.jsonleituraonline;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static String url = "http://200.132.17.49/webservice/escolaridades.json";

    String id_escolaridade, descricao;
    TextView tv1;
    private ListView lv;
    ArrayList<HashMap<String, String>> solista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        solista = new ArrayList<>();
        lv = (ListView) findViewById(R.id.list);
        new buscavalores().execute();
    }
    private class  buscavalores extends AsyncTask<Void, Void, Void> {


        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Lendo Dados ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {

            manipulahttp sh = new manipulahttp();

            // Fazendo uma solicitação para url e obtendo resposta
            String jsonStr = sh.requisitaservico(url);

            String VetRet[][];
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                JSONArray sistemas = jsonObj.getJSONArray("data");
                VetRet = new String[sistemas.length()][2];
                for (int i = 0; i < sistemas.length(); i++) {
                    JSONObject so = sistemas.getJSONObject(i);
                    String id_escolaridade = so.getString("id_escolaridade");
                    String descricao = so.getString("descricao");

                    HashMap<String, String> sistema = new HashMap<>();
                    sistema.put("id_escolaridade",id_escolaridade);
                    sistema.put("descricao",descricao);
                    solista.add(sistema);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            ListAdapter adapter = new SimpleAdapter(
                    MainActivity.this, solista,
                    R.layout.list_item, new String[]{"id_escolaridade", "descricao",
                    "mobile"}, new int[]{R.id.id_escolaridade,
                    R.id.descricao});

            lv.setAdapter(adapter);

            manipulahttp sh = new manipulahttp();
            Toast.makeText(MainActivity.this,"Teste de recebimento",Toast.LENGTH_LONG).show();
        }

    }

}