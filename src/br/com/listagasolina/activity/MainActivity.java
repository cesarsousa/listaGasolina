package br.com.listagasolina.activity;


import java.text.ParseException;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import br.com.listagasolina.modelo.DataUtils;
import br.com.listagasolina.modelo.Registro;
import br.com.listagasolina.modelo.RegistroAdapter;
import br.com.listagasolina.modelo.RegistroRepositorio;
import br.com.listagasolina.modelo.RegistroRepositorioScript;

import com.example.listagasolina.R;

public class MainActivity extends ListActivity {
	
	private AlertDialog alertDialog;
	
	private Button btMenuAdicionar;
	private Button btMenuRemoverTodos;
	private Button btMenuSair;
	
	private RegistroRepositorio repositorio;
	private static List<Registro> registros;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		repositorio = new RegistroRepositorioScript(this);
			
		btMenuAdicionar = (Button) findViewById(R.id.btMenuAdicionar);
		btMenuRemoverTodos = (Button) findViewById(R.id.btMenuRemoverTodos);
		btMenuSair = (Button) findViewById(R.id.btMenuSair);
		
		btMenuAdicionar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(MainActivity.this);
				final View viewFormAlterar = factory.inflate(R.layout.form_add_registro, null);	
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		    	builder.setView(viewFormAlterar);
		    	builder.setTitle("Novo Registro");
		    	builder.setMessage("Entrar com as informações");
		    	
		    	builder.setPositiveButton("ADICIONAR", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {						
						
						final EditText editLitros = (EditText) viewFormAlterar.findViewById(R.id.litros);
						String strLitros = editLitros.getText().toString().trim();
						
						final EditText editValor = (EditText) viewFormAlterar.findViewById(R.id.valor);
						String strValor = editValor.getText().toString().trim();
						
						final EditText editKilometragem = (EditText) viewFormAlterar.findViewById(R.id.kilometragem);
						String strKilometragem = editKilometragem.getText().toString().trim();
						
						String mensagem = validarCampos(strLitros, strValor, strKilometragem);
						
						if(!mensagem.isEmpty()){
							mensagem(mensagem);	
						}else{
							Registro registro = new Registro(
									DataUtils.getDateNow(),
									Integer.parseInt(strLitros), 
									Integer.parseInt(strValor),
									Integer.parseInt(strKilometragem));
							repositorio.salvar(registro);				
							renderizarRegistros();
						}
					}				
				});		    	
		    	builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						mensagem("Operação Cancelada");
					}
				});
		    	alertDialog = builder.create();
		    	alertDialog.show();  
			}
		});
		
		btMenuRemoverTodos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {			
				
				DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				        	verificarConexao();
							
							try {
								for(Registro registro : repositorio.listarTodos()){
									repositorio.deletar(registro.getId());
								}
							} catch (ParseException e) {
								mensagem("Erro durante leitura de registos");
							}
							renderizarRegistros();
				            break;

				        case DialogInterface.BUTTON_NEGATIVE:
				        	mensagem("Remoção cancelada");
				            break;
				        }
				    }
				};

				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage("Remover todos os registros?").setPositiveButton("Sim", dialogClickListener).setNegativeButton("Não", dialogClickListener).show();
			}
		});
		
		btMenuSair.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();		
			}
		});
		
		renderizarRegistros();
		
	}	

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		verificarConexao();
		
		final Registro registroSelecionado = (Registro) this.getListAdapter().getItem(position);
		
		Registro registroAnterior = null;
		if(registroSelecionado.getId()-1 > 0){
			try {
				registroAnterior = repositorio.buscar(registroSelecionado.getId()-1);
			} catch (ParseException e) {
				mensagem("Erro na busca do registro anterior");
			}
		}	    	
    	
		StringBuilder sb = new StringBuilder();
		if(registroAnterior == null){
			sb.append("Registro anterior não encontrado");
		}else{
			sb.append(registroAnterior.toString() + ".\n\n");
			sb.append(registroSelecionado.toString() + ".\n\n");
			sb.append("Total de " + DataUtils.diasEntre(registroAnterior.getData(), registroSelecionado.getData()) + " dias.\n");
			sb.append("Média de Consumo: " + obterMediaDeConsumo(registroAnterior, registroSelecionado) + "Km/L");
		}
		
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle("Histórico");
    	alertDialog.setMessage(sb.toString());
    	/*alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
    	   public void onClick(DialogInterface dialog, int which) {}});  */ 	
    	alertDialog.show();	
	}

	private String validarCampos(String strLitros, String strValor, String strKilometragem) {
		StringBuilder retorno = new StringBuilder();						
		if(strLitros.isEmpty()) retorno.append("\nCampo litros é obrigatório");
		else if(strValor.isEmpty()) retorno.append("\nCampo valor é obrigatório");
		else if(strKilometragem.isEmpty()) retorno.append("\nCampo kilometragem é obrigatório");					
		else{
			try {
				Double.parseDouble(strLitros);
			} catch (NumberFormatException e) {
				retorno.append("\nCampo litros é inválido");
			}
			
			try {
				Double.parseDouble(strValor);
			} catch (NumberFormatException e) {
				retorno.append("\nCampo valor é inválido");
			}
			
			try {
				Double.parseDouble(strKilometragem);
			} catch (NumberFormatException e) {
				retorno.append("\nCampo kilometragem é inválido");
			}						
		}
		return retorno.toString();
	}
	
	private void renderizarRegistros() {		
		verificarConexao();		
		try {
			registros = repositorio.listarTodos();
			setListAdapter(new RegistroAdapter(this, registros));
		} catch (ParseException e) {
			mensagem("Erro durante a busca de registros");
		}
	}
	
	private void verificarConexao() {
		if(!RegistroRepositorioScript.getConexao().isOpen()){
			repositorio = new RegistroRepositorioScript(MainActivity.this);
		}		
	}
	
	private void mensagem(String mensagem) {
		Toast.makeText(MainActivity.this, mensagem, Toast.LENGTH_SHORT).show();
	}
	
	private String obterMediaDeConsumo(Registro registroAnterior, Registro registroSelecionado) {
		if(registroSelecionado.getKilometragem() - registroAnterior.getKilometragem() < 0){
			return String.valueOf(0);
		}
		
		
		return String.valueOf(registroSelecionado.getKilometragem() - registroAnterior.getKilometragem());
	}

	@Override
    protected void onPause() {
    	repositorio.fecharConexao();
    	super.onPause();
    }
}
