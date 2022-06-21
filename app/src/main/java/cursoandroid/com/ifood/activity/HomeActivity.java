package cursoandroid.com.ifood.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import cursoandroid.com.ifood.R;
import cursoandroid.com.ifood.helper.ConfiguravaoFirebase;

public class HomeActivity extends AppCompatActivity {
    private FirebaseAuth autenticacao;
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        inicializarComponentes();
        autenticacao = ConfiguravaoFirebase.getFirebaseAutenticacao();

        //Configurações Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ifood");
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_usuario, menu);

        //configuração botão de pesquisa
        MenuItem item = menu.findItem(R.id.menupesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sair:
                deslogarUsuario();
                break;
            case R.id.menuConfiguracoes :
                abrirConfiguracoes();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    private void inicializarComponentes(){
        searchView = findViewById(R.id.materialSearchView);
    }

    private void deslogarUsuario(){
        try {
            autenticacao.signOut();
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void abrirConfiguracoes(){
        startActivity(new Intent(HomeActivity.this, ConfiguracoesUsuarioActivity.class));
    }
}
