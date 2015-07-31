package br.com.listagasolina.activity;


import java.text.ParseException;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint.FontMetrics;
import android.os.Bundle;
import android.transition.Visibility;
import android.view.ActionProvider.VisibilityListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
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

public class MainActivity extends ListActivity {
	
	private AlertDialog alertDialog;
	
	private Button btMenuAdicionar;
	private Button btMenuRemoverTodos;
	private Button btMenuSair;
	
	private RegistroRepositorio repositorio;
	private static List<Registro> registros;
	
	
	private static final int ESTATISTICA = 0;
    private static final int REMOVER_TODOS = 1;
    private static final int MENSAL = 3;
    private static final int DELETAR = 5;
	
	 //definição das constantes utilizadas na criação do menu
    /*private static final int ARQUIVO = 0;
    private static final int EDITAR = 1;
    private static final int FORMATAR = 2;
 
    private static final int ARQ_NOVO = 3;
    private static final int ARQ_SALVAR = 4;
 
    private static final int EDITAR_RECORTAR = 5;
    private static final int EDITAR_COPIAR = 6;
     
    private static final int FORMATAR_FONTE = 7;*/
	

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
		
		/*Registro registroAnterior = null;*/
		/*if(registroSelecionado.getId()-1 > 0){
			try {
				registroAnterior = repositorio.buscar(registroSelecionado.getId()-1);
			} catch (ParseException e) {
				mensagem("Erro na busca do registro anterior");
			}
		}*/	    	
    	
		StringBuilder sb = new StringBuilder();
		/*if(registroAnterior == null){
			sb.append("Registro anterior não encontrado");
		}else{
			sb.append(registroAnterior.toString() + ".\n\n");
			sb.append(registroSelecionado.toString() + ".\n\n");
			sb.append("Total de " + DataUtils.diasEntre(registroAnterior.getData(), registroSelecionado.getData()) + " dias.\n");
			sb.append("Km percorridos: " + String.valueOf(registroSelecionado.getKilometragem() - registroAnterior.getKilometragem()) + " Km\n");
			sb.append("Média de Consumo: " + obterMediaDeConsumo(registroAnterior, registroSelecionado) + " Km/L");
		}*/
		
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
	            //cria o menu
			 	MenuItem menuEstatisticas = menu.add(ESTATISTICA, MENSAL, 0, "Estatísticas");
	            MenuItem menuRemover = menu.add(REMOVER_TODOS, DELETAR, 0, "Remover Todos");
	                    
	            
	            /*//cria o menu e submenus
	            SubMenu menuArquivo = menu.addSubMenu(ARQUIVO, 0, 0, "Arquivo");
	            SubMenu menuEditar = menu.addSubMenu(EDITAR, 1, 0, "Editar");
	            MenuItem menuFormatar = menu.add(FORMATAR, FORMATAR_FONTE, 0, "Formatar");        
	             
	            //define uma tecla de atalho para o menu, nesse caso a 
	            //tecla de atalho é a letra "F"
	            menuFormatar.setShortcut('0', 'F');
	             
	            //adiciona um ícone ao menu
	            //menuFormatar.setIcon(R.drawable.icon);
	            menuFormatar.setIcon(android.R.drawable.ic_menu_edit);
	             
	            //caso seja necessário desabilitar o menu Arquivo
	            //abaixo segue exemplo
	            //menu.findItem(ARQUIVO).setEnabled(false);
	     
	            menuArquivo.add(ARQUIVO, ARQ_NOVO, 0, "Novo");
	            menuArquivo.add(ARQUIVO, ARQ_SALVAR, 1, "Salvar");
	            menuArquivo.setIcon(android.R.drawable.ic_menu_more);
	             
	            //caso seja necessário desabilitar um subitem do menu Arquivo
	            //abaixo segue exemplo
	            //menuArquivo.findItem(ARQ_NOVO).setEnabled(false);
	             
	            menuEditar.add(EDITAR, EDITAR_RECORTAR, 0, "Recortar");
	            menuEditar.add(EDITAR, EDITAR_COPIAR, 1, "Copiar");
	     
	            //caso seja necessário desabilitar um subitem do menu Editar
	            //abaixo segue exemplo
	            //menuEditar.findItem(EDITAR_COPIAR).setEnabled(false);
*/	        }
	        catch (Exception e) {
	            mensagem("Erro : " + e.getMessage());
	        }  
		return super.onCreateOptionsMenu(menu);
	}
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	        //de acordo com o item selecionado você executará
	        //a função desejada
	        switch (item.getItemId()) {
	        
	        case MENSAL:    
                
                Intent intent = new Intent(MainActivity.this, Estatisticas.class);
                startActivity(intent);           
                
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
	        
	            /*case ARQ_NOVO:     
	                mensagem("Você selecionou o menu Novo");
	                break;
	            case ARQ_SALVAR:     
	            	mensagem("Você selecionou o menu Salvar");
	                break;
	            case EDITAR_COPIAR: 
	            	mensagem("Você selecionou o menu Copiar");
	                break;
	            case EDITAR_RECORTAR: 
	            	mensagem("Você selecionou o menu Recortar");
	                break;
	            case FORMATAR_FONTE: 
	            	mensagem("Você selecionou o menu Formatar");
	                break;*/
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
	
	private String obterMediaDeConsumo(Registro registroAnterior, Registro registroSelecionado) {
		if(registroSelecionado.getKilometragem() - registroAnterior.getKilometragem() < 0){
			return String.valueOf(0);
		}
		
		int litros = registroSelecionado.getLitros();
		int kmPercorridos = registroSelecionado.getKilometragem() - registroAnterior.getKilometragem();
		
		return String.valueOf(kmPercorridos/litros);
	}

	@Override
    protected void onPause() {
    	repositorio.fecharConexao();
    	super.onPause();
    }
}
