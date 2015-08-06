package br.com.listagasolina.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
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

public class MainActivity extends ListActivity implements Runnable{
	
	private AlertDialog alertDialog;
	
	private Button btMenuAdicionar;
	private Button btMenuRemoverTodos;
	private Button btMenuSair;
	
	private RegistroRepositorio repositorio;
	private static List<Registro> registros;
	
	private static final int ESTATISTICA_GERAL = 0;
    private static final int REMOVER_TODOS = 1;
    private static final int EXPORTAR = 2;
    private static final int IMPORTAR = 4;
    private static final int ARQUIVO_EXPORTAR = 6;
    private static final int ARQUIVO_IMPORTAR = 7;
    private static final int ESTATISTICA = 3;
    private static final int DELETAR = 5;
    
    private static boolean importarRegistros = false;
    
    
    private ProgressDialog dialog;
	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		repositorio = new RegistroRepositorioScript(this);
			
		btMenuAdicionar = (Button) findViewById(R.id.btMenuAdicionar);
		btMenuRemoverTodos = (Button) findViewById(R.id.btMenuRemoverTodos);
		btMenuRemoverTodos.setVisibility(View.GONE);
		btMenuSair = (Button) findViewById(R.id.btMenuSair);
		
		btMenuAdicionar.setOnClickListener(new OnClickListener() {
			
			@SuppressLint("InflateParams")
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

	@SuppressWarnings("deprecation")
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		verificarConexao();
		
		final Registro registroSelecionado = (Registro) this.getListAdapter().getItem(position);
		
		StringBuilder sb = new StringBuilder();
		sb.append(registroSelecionado.toString() + ".\n\n");
		
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
    	alertDialog.setTitle("Histórico");
    	alertDialog.setMessage(sb.toString());
    	alertDialog.setButton("Atualizar", new DialogInterface.OnClickListener() {
    	   public void onClick(DialogInterface dialog, int which) {
    		   Intent intent = new Intent(MainActivity.this, AlterarRegistro.class);
    		   intent.putExtra ("registro", registroSelecionado);
    		   startActivity(intent);
    	   }
    	});
    	alertDialog.setButton2("Deletar", new DialogInterface.OnClickListener() {
     	   public void onClick(DialogInterface dialog, int which) {
     		   repositorio.deletar(registroSelecionado.getId());
     		   
     		   mensagem("Registro removido com sucesso");
     		   
     		  renderizarRegistros();
     	   }
     	});
    	
    	alertDialog.show();	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		 try
	        {
			 	MenuItem menuEstatisticas = menu.add(ESTATISTICA_GERAL, ESTATISTICA, 0, "Estatísticas");
			 	//MenuItem menuExportar = menu.add(ARQUIVO_EXPORTAR, EXPORTAR, 0, "Exportar");
			 	//MenuItem menuImportar = menu.add(ARQUIVO_IMPORTAR, IMPORTAR, 0, "Importar");
	            MenuItem menuRemover = menu.add(REMOVER_TODOS, DELETAR, 0, "Remover Todos");
	        }catch (Exception e) {
	            mensagem("Erro : " + e.getMessage());
	        }  
		return super.onCreateOptionsMenu(menu);
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	       
	        switch (item.getItemId()) {
	        
	        case ESTATISTICA:
                ArrayList<Registro> registros;
				try {
					registros = (ArrayList<Registro>) repositorio.listarTodos();
					Intent intent = new Intent(MainActivity.this, Estatisticas.class);
	                intent.putExtra("registros", registros);
	                startActivity(intent);  
				} catch (ParseException e1) {
					e1.printStackTrace();
					mensagem("Não foi possível obter listagem de registro :(");
				}               
                break;          
            case DELETAR: 
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
			
                break;
                
            case IMPORTAR:
            	importarRegistros = true;
            	dialog = ProgressDialog.show(MainActivity.this, "importando registros, por favor aguarde...", null, false, true);
				new Thread(MainActivity.this).start();
            	
            	break;
            	
            case EXPORTAR:
            	importarRegistros = false;
            	dialog = ProgressDialog.show(MainActivity.this, "Exportando registros, por favor aguarde...", null, false, true);
				new Thread(MainActivity.this).start();
            	
            	break;
	        }
	        return true;
	    }   
	
	private String validarCampos(String strLitros, String strValor, String strKilometragem) {
		StringBuilder retorno = new StringBuilder();						
		if(strLitros.isEmpty()) retorno.append("Campo litros é obrigatório");
		else if(strValor.isEmpty()) retorno.append("Campo valor é obrigatório");
		else if(strKilometragem.isEmpty()) retorno.append("Campo kilometragem é obrigatório");					
		else{
			try {
				Double.parseDouble(strLitros);
			} catch (NumberFormatException e) {
				retorno.append("Campo litros é inválido");
			}
			
			try {
				Double.parseDouble(strValor);
			} catch (NumberFormatException e) {
				retorno.append("Campo valor é inválido");
			}
			
			try {
				Double.parseDouble(strKilometragem);
			} catch (NumberFormatException e) {
				retorno.append("Campo kilometragem é inválido");
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

	@Override
    protected void onPause() {
    	repositorio.fecharConexao();
    	super.onPause();
    }

	@Override
	public void run() {
		if(importarRegistros){
			
		}else{
			
		}		
	}
}
