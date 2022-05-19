package saulodev.com.bankproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TransferenciaActivity extends AppCompatActivity {
    private EditText valorTransferencia;
    private TextView txtCpf;
    private EditText cpf;
    private EditText descricao;
    private TextView txtAgencia;
    private EditText agencia;
    private TextView txtConta;
    private EditText conta;
    private TextView txtDigito;
    private EditText digito;
    private CheckBox agenciaEConta;
    private Button transferir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferencia);
        inicializaComponents();
        agenciaEConta.setOnClickListener(View ->{
            checkBoxAgenciaeConta();
        });
        transferir.setOnClickListener(View -> {
            if (agenciaEConta.isChecked()) {
                realizaTransferenciaAgenciaeConta();
            }else {
                realizaTransferenciaCpf();
            }
        });
    }

    private void inicializaComponents(){
        valorTransferencia= findViewById(R.id.edtSaldoTransf);
        valorTransferencia.addTextChangedListener(new MoneyMask(valorTransferencia));
        txtCpf = findViewById(R.id.txtCpf);
        cpf = findViewById(R.id.edtDocumento);
        cpf.addTextChangedListener(TextMask.insert(TextMask.CPF_MASK, cpf));
        descricao = findViewById(R.id.edtDesc);
        txtAgencia = findViewById(R.id.txtAgencia);
        agencia = findViewById(R.id.edtAgencia);
        txtConta = findViewById(R.id.txtConta);
        conta = findViewById(R.id.edtConta);
        txtDigito = findViewById(R.id.txtDigito);
        digito = findViewById(R.id.digitoVerificadorTransferencia);
        agenciaEConta = findViewById(R.id.checkoBoxTransf);
        transferir = findViewById(R.id.btnTransferir);
    }

    private void checkBoxAgenciaeConta(){
        if (agenciaEConta.isChecked()){
            cpf.setVisibility(View.INVISIBLE);
            txtCpf.setVisibility(View.INVISIBLE);
            txtAgencia.setVisibility(View.VISIBLE);
            agencia.setVisibility(View.VISIBLE);
            txtConta.setVisibility(View.VISIBLE);
            conta.setVisibility(View.VISIBLE);
            txtDigito.setVisibility(View.VISIBLE);
            digito.setVisibility(View.VISIBLE);
        }else {
            cpf.setVisibility(View.VISIBLE);
            txtCpf.setVisibility(View.VISIBLE);
            txtAgencia.setVisibility(View.INVISIBLE);
            agencia.setVisibility(View.INVISIBLE);
            txtConta.setVisibility(View.INVISIBLE);
            conta.setVisibility(View.INVISIBLE);
            txtDigito.setVisibility(View.INVISIBLE);
            digito.setVisibility(View.INVISIBLE);
        }
    }

    private boolean realizaTransferenciaCpf(){
        try {
            Bundle extras = getIntent().getExtras();
            String cpfResgatado = extras.getString("cpf");
            String saldo = valorTransferencia.getText().toString();
            Double valor = Double.parseDouble(saldo.replaceAll(",", ""));
            String ponto = "\\.";
            String cpfCerto = cpf.getText().toString().replaceAll( ponto, "");
            Log.e("Resultado" , cpfResgatado);
            Log.e("Resultado", cpfCerto);
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            String consulta = "SELECT saldo FROM usuario WHERE cpf = " + cpfResgatado;
            Cursor cursor = banco.rawQuery(consulta, null);
            int indiceSaldo = cursor.getColumnIndex("saldo");
            cursor.moveToFirst();
            String saldoRecuperado = cursor.getString(indiceSaldo);
            if (Double.parseDouble(saldoRecuperado) < valor){
                Toast.makeText(getApplicationContext(), "Saldo em conta insuficiente!", Toast.LENGTH_LONG).show();
                return (false);
            }else {
                banco.execSQL("UPDATE usuario set saldo = saldo - "+valor+" WHERE cpf = " + cpfResgatado +"");
                banco.execSQL("UPDATE usuario SET saldo = saldo + "+valor+" WHERE cpf = "+ cpfCerto.replaceAll("-", "")+"");
                Toast.makeText(getApplicationContext(), "Transferencia Realizada!", Toast.LENGTH_LONG).show();
                Intent passaDados = new Intent(getApplicationContext(), HomeActivity.class);
                passaDados.putExtra("cpf", cpfResgatado);
                startActivity(passaDados);
                finish();
                return (true); }
        }catch (Exception e){
            e.printStackTrace();
            return (false);
        }
    }

    private boolean realizaTransferenciaAgenciaeConta(){
        try {
            Bundle extras = getIntent().getExtras();
            String cpfResgatado = extras.getString("cpf");
            String saldo = valorTransferencia.getText().toString();
            Double valor = Double.parseDouble(saldo.replaceAll(",", ""));
            int agenciaF = Integer.parseInt(agencia.getText().toString());
            int contaF = Integer.parseInt(conta.getText().toString());
            int digitoF = Integer.parseInt(digito.getText().toString());
            SQLiteDatabase banco = openOrCreateDatabase("usuario", MODE_PRIVATE, null);
            String consulta = "SELECT saldo FROM usuario WHERE cpf = " + cpfResgatado;
            Cursor cursor = banco.rawQuery(consulta, null);
            int indiceSaldo = cursor.getColumnIndex("saldo");
            cursor.moveToFirst();
            String saldoRecuperado = cursor.getString(indiceSaldo);
            if (Double.parseDouble(saldoRecuperado) < valor){
                Toast.makeText(getApplicationContext(), "Saldo em conta insuficiente!", Toast.LENGTH_LONG).show();
                return (false);

            }else{
                banco.execSQL("UPDATE usuario set saldo = saldo - "+valor+" WHERE cpf = " + cpfResgatado +"");
                banco.execSQL("UPDATE usuario SET saldo = saldo + "+valor +" WHERE agencia = "+ agenciaF +" AND conta = "+ contaF + " AND digito = "+digitoF+"");
                Toast.makeText(getApplicationContext(), "Transferencia Realizada!", Toast.LENGTH_LONG).show();
                Intent passaDados = new Intent(getApplicationContext(), HomeActivity.class);
                passaDados.putExtra("cpf", cpfResgatado);
                startActivity(passaDados);
                finish();
                return (true);
            }

        }catch (Exception e){
            e.printStackTrace();
            return (false);
        }
    }


}