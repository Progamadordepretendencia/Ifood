package cursoandroid.com.ifood.model;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;

import cursoandroid.com.ifood.helper.ConfiguravaoFirebase;

public class Empresa {
    private  String idUsuario;
    private  String urlImagem;
    private  String nome;
    private  String tempo;
    private  String categoria;
    private double precoEntrega;

    public Empresa() {
    }
    public void salvar(){
        DatabaseReference firebaseRef = ConfiguravaoFirebase.getFirebase();
        DatabaseReference empresaRef = firebaseRef.child("empresa")
                .child(getIdUsuario());
        empresaRef.setValue(this)
                .addOnSuccessListener(a -> {
                    Log.d("FIREBASE", "Upado com sucesso: "+ a.toString());
                })
                .addOnFailureListener(error -> Log.d("FIREBASE", "Error: "+ error.getMessage()));
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPrecoEntrega() {return precoEntrega;}

    public void setPrecoEntrega(double precoEntrega) {
        this.precoEntrega = precoEntrega;
    }
}
