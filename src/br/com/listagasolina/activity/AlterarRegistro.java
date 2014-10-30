package br.com.listagasolina.activity;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import br.com.listagasolina.modelo.DataUtils;
import br.com.listagasolina.modelo.Registro;
import br.com.listagasolina.modelo.RegistroRepositorio;
import br.com.listagasolina.modelo.RegistroRepositorioScript;

public class AlterarRegistro extends Activity implements Button.OnClickListener {
	private Button botaoAlterarData;
	private EditText editTextLitros;
	private EditText editTextValor;
	private EditText editTextKilometragem;
	private Button botaoConfirmar;
	
	static final int DATE_DIALOG_ID = 0;
	
	Registro registro;
	
	private RegistroRepositorio repositorio;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alterar_registro);
		registro = (Registro) getIntent().getSerializableExtra("registro");
		
		botaoAlterarData = (Button) findViewById(R.id.btnAlterarData);
		botaoAlterarData.setOnClickListener(this);
		configurarTextoBotaoData();
		
		editTextLitros = (EditText) findViewById(R.id.edtRegistroAlterarLitros);
		editTextLitros.setText(String.valueOf(registro.getLitros()));
		
		editTextValor = (EditText) findViewById(R.id.edtRegistroAlterarValor);
		editTextValor.setText(String.valueOf(registro.getValor()));
		
		editTextKilometragem = (EditText) findViewById(R.id.edtRegistroAlterarKilometragem);
		editTextKilometragem.setText(String.valueOf(registro.getKilometragem()));
		
		botaoConfirmar = (Button) findViewById(R.id.btConfirmaAlterarRegistro);
		botaoConfirmar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				
				String novoValor = editTextValor.getText().toString().trim();
				String novoLitros = editTextLitros.getText().toString().trim();
				String novoKilometragem = editTextKilometragem.getText().toString().trim();
				
				if(!camposInvalidos(novoValor, novoLitros, novoKilometragem)){
					registro.setValor(Integer.parseInt(novoValor));
					registro.setLitros(Integer.parseInt(novoLitros));
					registro.setKilometragem(Integer.parseInt(novoKilometragem));
										
					if(!RegistroRepositorioScript.getConexao().isOpen()){
						repositorio = new RegistroRepositorioScript(AlterarRegistro.this);
					}
					
					repositorio.salvar(registro);
					
					Toast.makeText(AlterarRegistro.this, "Registro " + registro.getId() + " alterado", Toast.LENGTH_SHORT).show();
				
					retornarParaMain();					
				}					
			}
		});		
	}	
	
	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(registro.getData());
		int ano = calendario.get(Calendar.YEAR);
		int mes = calendario.get(Calendar.MONTH);
		int dia = calendario.get(Calendar.DAY_OF_MONTH);
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			
			Calendar calendar = Calendar.getInstance();
			calendar.set(year, monthOfYear, dayOfMonth);
			registro.setData(calendar.getTime());
			configurarTextoBotaoData();
		}	
	};
	
	public void onBackPressed() {
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(AlterarRegistro.this);
	    alertDialogBuilder.setTitle("Atenção");
	 
	    alertDialogBuilder.setMessage("Voltar a tela inicial?").setCancelable(false).setPositiveButton("Sim",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                retornarParaMain();
	            }
	        })
	        .setNegativeButton("Não",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	            }
	        });
	 
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}

	@Override
	public void onClick(View v) {
		if (v == botaoAlterarData)
			showDialog(DATE_DIALOG_ID);
	}
	
	private boolean camposInvalidos(String novoValor, String novoLitros, String novoKilometragem) {
		if(novoValor.isEmpty()){
			Toast.makeText(AlterarRegistro.this, "Campo Valor deve ser informado", Toast.LENGTH_SHORT).show();
			return true;
		}
		if(novoLitros.isEmpty()){
			Toast.makeText(AlterarRegistro.this, "Campo Litros deve ser informado", Toast.LENGTH_SHORT).show();
			return true;
		}
		if(novoKilometragem.isEmpty()){
			Toast.makeText(AlterarRegistro.this, "Campo Kilometragem deve ser informado", Toast.LENGTH_SHORT).show();
			return true;
		}
		return false;
	}
	
	private void configurarTextoBotaoData() {
		botaoAlterarData.setText(DataUtils.getDateToString(registro.getData()));
		
	}

	
	private void retornarParaMain() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}
}
