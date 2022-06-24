package cursoandroid.com.ifood.model;

import com.google.firebase.database.DatabaseReference;

import cursoandroid.com.ifood.helper.ConfiguravaoFirebase;

public class Usuario {

    private String idUsuario;
    private String nome;
    private String endereco;

    public Usuario() {
    }
    public void salvar(){
        DatabaseReference firebaseRef = ConfiguravaoFirebase.getFirebase();
        DatabaseReference usuarioRef = firebaseRef
                .child("usuarios")
                .child(getIdUsuario());
        usuarioRef.setValue(this);
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

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endero) {
        this.endereco = endero;
    }
}
