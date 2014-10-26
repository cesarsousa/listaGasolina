package br.com.listagasolina.modelo;

import android.app.ListActivity;
import android.database.sqlite.SQLiteDatabase;

public class RegistroRepositorio extends ListActivity{
		
	protected static SQLiteDatabase db;
	
	
	
	
	
	public void abrirConexao(){
		if(!db.isOpen()){
			new RegistroRepositorioScript(this);
		}
	}
	
	public void fecharConexao(){
		if(db != null) db.close();
	}
	

	
	

}
