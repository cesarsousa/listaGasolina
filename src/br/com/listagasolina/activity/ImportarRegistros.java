package br.com.listagasolina.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.com.listagasolina.modelo.ConversorDeTextos;
import br.com.listagasolina.modelo.DataUtils;
import br.com.listagasolina.modelo.Registro;
import br.com.listagasolina.modelo.RegistroRepositorio;
import br.com.listagasolina.modelo.RegistroRepositorioScript;

public class ImportarRegistros extends Activity {
	
	
	private EditText textoDosRegistros;
	private Button btImportarRegistros;
	
	private RegistroRepositorio repositorio;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.importar_registros);
		
		textoDosRegistros = (EditText) findViewById(R.id.edtImportarRegistros);
		btImportarRegistros = (Button) findViewById(R.id.btImportarRegistros);		 
		
		btImportarRegistros.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				
				final String strTextoRegistros = textoDosRegistros.getText().toString();
				repositorio = new RegistroRepositorioScript(ImportarRegistros.this);
				
				
				ConversorDeTextos conversor = new ConversorDeTextos(strTextoRegistros);				
				ArrayList<Registro> registros = new ArrayList<Registro>();
				registros = conversor.obterRegistros();				
				
				for(Registro registro:registros){
					repositorio.salvar(registro);
				}
				
				Toast.makeText(ImportarRegistros.this, "Registros criados com sucesso", Toast.LENGTH_SHORT).show();
				
				Intent intent = new Intent(ImportarRegistros.this, MainActivity.class);
        		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		startActivity(intent);
				
			}
		});		
	}
}
