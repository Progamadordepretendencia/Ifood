package cursoandroid.com.ifood.model;

import com.google.firebase.database.DatabaseReference;

import cursoandroid.com.ifood.helper.ConfiguravaoFirebase;

public class Produto {
    private String idUsuario;
    private String nome;
    private String descricao;
    private Double preco;

    public Produto() {
    }
    public  void salvar(){
        DatabaseReference firebaseRef = ConfiguravaoFirebase.getFirebase();
        DatabaseReference produtoRef = firebaseRef
                .child("produtos")
                .child(getIdUsuario())
                .push();
        produtoRef.setValue(this);
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
