package lucasferreira.com.cinesky.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lucasferreira.com.cinesky.R;
import lucasferreira.com.cinesky.adapter.AdapterFilmes;
import lucasferreira.com.cinesky.listener.RecyclerItemClickListener;
import lucasferreira.com.cinesky.model.Filme;

/**
 * Activity responsável por listar filmes e consumir serviço WEB
 * Date 25/06/2017
 * @Author Lucas Ferreira
 */

public class ListaFilmes extends AppCompatActivity {

    //Widgets
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    //Classes
    private AdapterFilmes adapterFilmes;

    //Var
    private ArrayList<Filme> filmes = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private HttpGet httpGet = new HttpGet("https://sky-exercise.herokuapp.com/api/Movies");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_filmes);

        //Configura ActionBar
        getSupportActionBar().setTitle("Cine SKY");

        //Inicia Componentes
        inicializaViews();

        //Chama o serviço WEB
        new RequisicaoFilmes().execute();

        //Configura RecyclerView
        recyclerView.setHasFixedSize(true);

        //Verifica qual a orientação de tela, se for vertical irá gerar grid com 2 colunas, caso contrário 4
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            mLayoutManager = new GridLayoutManager(this, 2);
        }
        else{
            mLayoutManager = new GridLayoutManager(this, 4);
        }
        recyclerView.setLayoutManager(mLayoutManager);

        //Configura adapter
        adapterFilmes = new AdapterFilmes(filmes, getApplicationContext());
        recyclerView.setAdapter(adapterFilmes);

        //Trata eventos de click da recyclerView
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        this,
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Filme filmeSelecionado = filmes.get(position);

                                //Envia o objeto anuncio para outra tela executa-lo
                                Intent it = new Intent(ListaFilmes.this, DetalheVideo.class);
                                it.putExtra(getString(R.string.filme_selecionado), filmeSelecionado);
                                startActivity(it);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }

    /**
     * Método responsável por inicializar os componentes
     */
    public void inicializaViews() {
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.listaFilmes);
    }

    //Cria uma AsyncTask para recuperar dados da API
    private class RequisicaoFilmes extends AsyncTask<Void, String, String> {

        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("resposta", response.toString());

                    //Limpa a lista e esconde a progressBar
                    filmes.clear();
                    progressBar.setVisibility(View.INVISIBLE);

                    //Faz a lista de filmes receber dados do método parse
                    filmes = parseFilmes(jsonArray);

                    adapterFilmes.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Método responsável por fazer o parse do JSON
     * @param jsonArray
     * @return
     */
    private ArrayList<Filme> parseFilmes(JSONArray jsonArray) {

        try {
            JSONObject jsonObject;
            for (int n = 0; n < jsonArray.length(); n++) {

                jsonObject = jsonArray.getJSONObject(n);
                String nome = jsonObject.getString("title");
                String descricao = jsonObject.getString("overview");
                String tempo = jsonObject.getString("duration");
                String anoLancamento = jsonObject.getString("release_year");
                String capa = jsonObject.getString("cover_url");

                Filme filme = new Filme();
                filme.setTitulo(nome);
                filme.setDescricao(descricao);
                filme.setTempo(tempo);
                filme.setAno(anoLancamento);
                filme.setCapa(capa);

                filmes.add(filme);
                adapterFilmes.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return filmes;
    }
}
