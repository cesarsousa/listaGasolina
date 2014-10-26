package br.com.listagasolina.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class RegistroRepositorioScript extends RegistroRepositorio {	

	private static final String NOME_BANCO = "listagasolina";
	private static final int VERSAO_BANCO = 1;
	private static final String NOME_TABELA = "registro";	
	private static final String SCRIPT_DATABASE_DELETE = "DROP TABLE IF EXISTS " + NOME_TABELA;
	private static final String[] SCRIPT_DATABASE_CREATE = new String[]
		{
		"create table registro ( " +
				"_id integer primary key autoincrement, " +
				"data date not null, " +
				"litros numeric not null, " +
				"valor numeric not null, " +
				"kilometragem text not null );"		
		};
	
	private SQLiteHelper dbHelper;

	public RegistroRepositorioScript(Context context) {
		dbHelper = new SQLiteHelper(context, NOME_BANCO, VERSAO_BANCO, SCRIPT_DATABASE_CREATE, SCRIPT_DATABASE_DELETE);
		db = dbHelper.getWritableDatabase();		
	}	
	
	public static SQLiteDatabase getConexao(){
		return db;
	}
	

}
