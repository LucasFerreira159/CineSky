package lucasferreira.com.cinesky.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lucasferreira.com.cinesky.R;
import lucasferreira.com.cinesky.model.Filme;

/**
 * Classe responsável por gerenciar as personalizações de itens da RecyclerView
 * Date 25/06/2018
 * @Author Lucas Ferreira
 */
public class AdapterFilmes extends RecyclerView.Adapter<AdapterFilmes.MyViewHolder>{

    private ArrayList<Filme> listaFilmes;
    private Context context;

    public AdapterFilmes(ArrayList<Filme> listaFilmes, Context context) {
        this.listaFilmes = listaFilmes;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filmes, parent, false);
        return new AdapterFilmes.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Filme filme = listaFilmes.get(position);
        holder.textTituloVideo.setText(filme.getTitulo());

        Picasso.get().load(filme.getCapa())
                .error(context.getResources().getDrawable(R.drawable.noimage))
                .into(holder.imageCapa);
    }

    @Override
    public int getItemCount() {
        return listaFilmes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textTituloVideo;
        RoundedImageView imageCapa;

        public MyViewHolder(View itemView) {
            super(itemView);

            textTituloVideo = itemView.findViewById(R.id.text_titulo);
            imageCapa = itemView.findViewById(R.id.image_capa);
        }
    }
}
