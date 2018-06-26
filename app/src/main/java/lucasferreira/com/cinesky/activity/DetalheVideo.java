package lucasferreira.com.cinesky.activity;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import lucasferreira.com.cinesky.R;
import lucasferreira.com.cinesky.model.Filme;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

/**
 * Activity responsável por detalhar dados do filme, através de um objeto serializado
 * Date 25/06/2017
 *
 * @Author Lucas Ferreira
 */

public class DetalheVideo extends AppCompatActivity {

    //Widgets
    private TextView titulo;
    private TextView descricao;
    private TextView ano;
    private TextView tempo;
    private ImageView capa;

    //Classes
    private Filme filmeSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhe_video);
        inicializaViews();

        //Recupera o objeto Filme enviado pela recyclerView
        filmeSelecionado = (Filme) getIntent().getSerializableExtra(getString(R.string.filme_selecionado));

        //Se o objeto não for nulo iremos setar os valores para os respectivos widgets
        if (filmeSelecionado != null) {
            titulo.setText(filmeSelecionado.getTitulo());
            descricao.setText(filmeSelecionado.getDescricao());
            ano.setText(filmeSelecionado.getAno());
            tempo.setText(filmeSelecionado.getTempo());

            Picasso.get().load(filmeSelecionado.getCapa()).into(capa);
        }

        //Se estiver com versao acima ou igual Oreo ira justificar o texto com novo recurso TextView
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            descricao.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        //Configura a ActionBar
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(filmeSelecionado.getTitulo());
    }

    private void inicializaViews() {
        titulo = findViewById(R.id.text_titulo);
        descricao = findViewById(R.id.text_descricao);
        ano = findViewById(R.id.text_ano);
        tempo = findViewById(R.id.text_tempo);
        capa = findViewById(R.id.image_capa);
    }


    /**
     * Método responsável por tratar o DisplayHomeAsUpEnabled
     * Quando clicar na flecha, será fechada a activity detalhes
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();

    }
}
