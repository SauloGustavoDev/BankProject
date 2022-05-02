package saulodev.com.bankproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private TextView nomeCompleto;
    private TextView cpfExibido;
    private TextView nascimentoExibido;
    private TextView emailExibido;
    private TextView celularExibido;
    private EditText password;
    private EditText passwordConfirmar;
    private ImageView trocarSenha;
    private ImageView deletarConta;
    private ImageView logout;
    private TextView alteraSenha;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        inicializaComponentes();
        exibeDados();
        logout.setOnClickListener(View -> { logout();});
        deletarConta.setOnClickListener(View -> {abrirDialogDeletar(); });
        trocarSenha.setOnClickListener(View ->{
            Intent passaDados = new Intent(getApplicationContext(), AlterarSenhaActivity.class);
            Bundle extras = getIntent().getExtras();
            String cpfResgatado = extras.getString("cpf");
            passaDados.putExtra("cpf", cpfResgatado);


            startActivity(passaDados);
        });



    }

    private void exibeDados(){
        Bundle extras = getIntent().getExtras();
        String cpfResgatado = extras.getString("cpf");


        SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
        String consulta = "SELECT nomeCompleto, cpf, nascimento, email, celular FROM usuario WHERE cpf = " + cpfResgatado;
        Cursor cursor = banco.rawQuery(consulta, null);
        int indiceNome = cursor.getColumnIndex("nomeCompleto");
        int indiceCpf = cursor.getColumnIndex("cpf");
        int indiceNascimento = cursor.getColumnIndex("nascimento");
        int indiceEmail = cursor.getColumnIndex("email");
        int indiceCelular = cursor.getColumnIndex("celular");

        cursor.moveToFirst();
        String nome = cursor.getString(indiceNome);
        String cpf = cursor.getString(indiceCpf);
        String nascimento = cursor.getString(indiceNascimento);
        String email = cursor.getString(indiceEmail);
        String celular = cursor.getString(indiceCelular);
        Log.i("Size", cpf);
        if (cpf.length() == 9){
            cpf ="00" + cpf;
        } else if(cpf.length() == 10){
            cpf ="0" + cpf;
        }
        cursor.close();
        banco.close();
        nascimento = nascimento.replaceAll("/", "");
        nomeCompleto.setText(nome);
        cpfExibido.setText(formataCpf(cpf));
        nascimentoExibido.setText(formataNascimento(nascimento));
        emailExibido.setText(email);
        celularExibido.setText(formataNumero(celular));
    }

    private void inicializaComponentes(){
        nomeCompleto = findViewById(R.id.nomeCompleto);
        cpfExibido = findViewById(R.id.cpf);
        nascimentoExibido = findViewById(R.id.nascimento);
        emailExibido = findViewById(R.id.email);
        celularExibido = findViewById(R.id.celular);
        trocarSenha = findViewById(R.id.alterarSenha);
        deletarConta = findViewById(R.id.deletarUsuario);
        logout = findViewById(R.id.logout);
        //alteraSenha = findViewById(R.id.btnConfirma);
    }

    private String formataNumero(String numero){
        String ddd = numero.substring(0 , 2);
        String numeroBloco1 = numero.substring(2, 7);
        String numeroBloco2 = numero.substring(7, 11);
        numero ="(" + ddd + ") " + numeroBloco1 + "-" + numeroBloco2;
        return numero;
    }

    private String formataCpf(String cpf){
        String cpfBloco1 = cpf.substring(0, 3);
        String cpfBloco2 = cpf.substring(3, 6);
        String cpfBloco3 = cpf.substring(6, 9);
        String cpfBloco4 = cpf.substring(9, 11);
        cpf = cpfBloco1+"."+cpfBloco2+"."+cpfBloco3+"-"+cpfBloco4;
        return cpf;
    }

    private String formataNascimento(String nascimento){
        String dataDia = nascimento.substring(0 , 2);
        String dataMes = nascimento.substring(2 , 4);
        String dataAno = nascimento.substring(4 , 8);
        nascimento = nascimento = dataDia + "/" + dataMes + "/" + dataAno;
        return nascimento;

    }

    private void deletarConta(){
        Bundle extras = getIntent().getExtras();
        String cpfResgatado = extras.getString("cpf");
        try {
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            banco.execSQL("DELETE FROM USUARIO WHERE cpf = "+ cpfResgatado +"");
            logout();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "Erro ao deleter usuário!", Toast.LENGTH_LONG).show();
        }


    }

    private void abrirDialogDeletar(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Deleção de Conta!");
        dialog.setMessage("Você realmente deseja deletar sua conta?");
        dialog.setIcon(getDrawable(R.drawable.ic_warning));
        dialog.setPositiveButton("SIM!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deletarConta();
            }
        });
        dialog.setNegativeButton("NÃO!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog build = dialog.create();
        build.show();
    }



    private void logout(){
        //abrirDialog();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private boolean validaSenha(String password) {
        if (password.indexOf("0") == 0){
            Toast.makeText(getApplicationContext(), "A senha não pode começar com 0", Toast.LENGTH_SHORT).show();
            return (false);
        }
        if (password.equals("000000") || password.equals("111111") ||
                password.equals("222222") || password.equals("333333") ||
                password.equals("444444") || password.equals("555555") ||
                password.equals("666666") || password.equals("777777") ||
                password.equals("888888") || password.equals("999999") ||
                password.length() < 6){
            Toast.makeText(getApplicationContext(), "É necessário informar uma senha válida!", Toast.LENGTH_SHORT).show();
            return (false);
        }else{
            return (true);
        }
    }

    private boolean validaConfirmarSenha(String senhaConfirma_Validando) {
        return senhaConfirma_Validando.equals(password.getText().toString());
    }

}