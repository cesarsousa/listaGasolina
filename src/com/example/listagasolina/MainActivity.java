package com.example.listagasolina;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private AlertDialog alertDialog;
	
	private Button btMenuAdicionar;
	private Button btMenuRemoverTodos;
	private Button btMenuSair;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		btMenuAdicionar = (Button) findViewById(R.id.btMenuAdicionar);
		btMenuRemoverTodos = (Button) findViewById(R.id.btMenuRemoverTodos);
		btMenuSair = (Button) findViewById(R.id.btMenuSair);
		
		btMenuAdicionar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater factory = LayoutInflater.from(MainActivity.this);
				final View viewFormAlterar = factory.inflate(R.layout.form_add_historico, null);	
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		    	builder.setView(viewFormAlterar);
		    	builder.setTitle("Descrição");
		    	builder.setMessage("Entrar com as informações e...");
		    	
		    	builder.setPositiveButton("ADICIONAR", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						Toast.makeText(MainActivity.this, "Adicionar novo", Toast.LENGTH_SHORT).show();
						
						
						/*final EditText editQuantidade = (EditText) viewFormAlterar.findViewById(R.id.alterarQuantidade);
						String strQuantidade = editQuantidade.getText().toString().trim();
						int quantidade = "".equals(strQuantidade) ? 0 : Integer.parseInt(strQuantidade);
									
						final EditText editValor = (EditText) viewFormAlterar.findViewById(R.id.alterarValor);
						
						String strValor = editValor.getText().toString().trim();				
						double valor = "".equals(strValor) ? 0 : Double.parseDouble(editValor.getText().toString().trim());
						
						produtoSelecionado.setQuantidade(quantidade);
						produtoSelecionado.setValor(quantidade == 0 ? 0 : valor);
						produtoSelecionado.setRiscado(quantidade == 0 ? false : true);				
						
						repositorio.salvar(produtoSelecionado);				
						
						renderizarProdutos();*/
					}
				
				});		    	
		    	builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						Toast.makeText(MainActivity.this, "Operação Cancelada", Toast.LENGTH_SHORT).show();
					}
				});
		    	alertDialog = builder.create();
		    	alertDialog.show();  
				
				
				renderizarHistorico(); 
			}
		});
		
		btMenuRemoverTodos.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Toast.makeText(MainActivity.this, "Remover Todos", Toast.LENGTH_SHORT).show();
			}
		});
		
		btMenuSair.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();		
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void renderizarHistorico() {
		// TODO Auto-generated method stub
		
	}
}
