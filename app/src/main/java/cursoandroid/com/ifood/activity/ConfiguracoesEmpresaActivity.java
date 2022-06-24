package cursoandroid.com.ifood.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import cursoandroid.com.ifood.R;
import cursoandroid.com.ifood.helper.ConfiguravaoFirebase;
import cursoandroid.com.ifood.helper.UsuarioFirebase;
import cursoandroid.com.ifood.model.Empresa;


public class ConfiguracoesEmpresaActivity extends AppCompatActivity {

    private EditText editEmpresaNome, editEmpresaCategoria,
                    editEmpresaTempo, editEmpresaTaxa;
    private ImageView imagePerfilEmpresa;

    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private String idUsuarioLogado;
    private DatabaseReference firebaseRef;
    private String urlImagemSelecionada = "";
    @SuppressLint("QueryPermissionsNeeded")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_empresa);

        //Configurações iniciais
        inicializarComponentes();

        storageReference = ConfiguravaoFirebase.getFirebaseStorage();
        firebaseRef = ConfiguravaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdUsuario();
        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        imagePerfilEmpresa.setOnClickListener(view -> {
            Intent i = new Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            );
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i, SELECAO_GALERIA);
            }
        });

        // recuperar dados da empresa
        recuperarDadosEmpresa();

    }
    private  void recuperarDadosEmpresa(){
        DatabaseReference empresaRef = firebaseRef
                .child("empresa")
                .child(idUsuarioLogado);
        empresaRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.getValue() != null){
                    Empresa empresa = snapshot.getValue(Empresa.class);
                    editEmpresaNome.setText(empresa.getNome());
                    editEmpresaCategoria.setText(empresa.getCategoria());
                    editEmpresaTaxa.setText( Double.toString(empresa.getPrecoEntrega()));
                    editEmpresaTempo.setText(empresa.getTempo());

                    urlImagemSelecionada = empresa.getUrlImagem();
                    if ( urlImagemSelecionada != ""){
                        Picasso.get()
                                .load(urlImagemSelecionada)
                                .into(imagePerfilEmpresa);
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
        public void validarDadosEmpresa(View view){
            //Valida se os cmapos foram preenchidos
            String nome = editEmpresaNome.getText().toString();
            String taxa = editEmpresaTaxa.getText().toString();
            String categoria = editEmpresaCategoria.getText().toString();
            String tempo = editEmpresaTempo.getText().toString();

            if (!nome.isEmpty()) {
                if (!taxa.isEmpty()) {
                    if (!categoria.isEmpty()) {
                        if (!tempo.isEmpty()) {
                            Empresa empresa = new Empresa();
                            empresa.setIdUsuario(idUsuarioLogado);
                            empresa.setNome(nome);
                            empresa.setPrecoEntrega( Double.parseDouble(taxa));
                            empresa.setCategoria(categoria);
                            empresa.setTempo(tempo);
                            empresa.setUrlImagem(urlImagemSelecionada);
                            empresa.salvar();
                            //finish();
                        } else {
                            exibirMensagem("Digite um tempo de entrega");
                        }
                    } else {
                        exibirMensagem("Digite uma categoria ");
                    }
                } else {
                    exibirMensagem(" Digite uma taxa de entrega");
                }
            }else{
                    exibirMensagem("Digite um nome para a empresa");
                }
            }


    private void exibirMensagem(String texto){
        Toast.makeText(this, texto, Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                if (requestCode == SELECAO_GALERIA) {
                    Uri localImagem = data.getData();
                    imagem = MediaStore.Images
                            .Media
                            .getBitmap(
                                    getContentResolver(),
                                    localImagem
                            );
                }
                if( imagem != null) {

                    imagePerfilEmpresa.setImageBitmap(imagem);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("empresas")
                            .child(idUsuarioLogado + ".jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(e -> {
                        Toast.makeText(ConfiguracoesEmpresaActivity.this,
                        "Erro ao fazer upload da imagem",
                                Toast.LENGTH_SHORT).show();
                        Log.d("LALALALA", e.toString());
                    })
                            .addOnSuccessListener(taskSnapshot -> imagemRef.getDownloadUrl().addOnCompleteListener(task -> {
                                Uri url = task.getResult();
                                urlImagemSelecionada = url.toString();
                                Toast.makeText( ConfiguracoesEmpresaActivity.this,
                                        "Sucesso ao fazer upload da imagem",
                                        Toast.LENGTH_SHORT).show();
                    }));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private void inicializarComponentes(){
        editEmpresaNome = findViewById(R.id.editProdutoNome);
        editEmpresaCategoria = findViewById(R.id.editProdutoDescricao);
        editEmpresaTaxa = findViewById(R.id.editProdutoPreco);
        editEmpresaTempo = findViewById(R.id.editEmpresaTempo);
        imagePerfilEmpresa = findViewById(R.id.imagePerfilEmpresa);
    }
}
