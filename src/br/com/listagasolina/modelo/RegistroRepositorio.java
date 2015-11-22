package br.com.listagasolina.modelo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RegistroRepositorio extends ListActivity{
		
	protected static SQLiteDatabase db;
	private static final String NOME_TABELA = "registro";
	
	/**
	 * 
	 * @param registro
	 * @return
	 */
	public long salvar(Registro registro){
		long id = registro.getId();
		if(id == 0){
			id = inserir(registro);
		}else{
			atualizar(registro);
		}
		return id;
	}	
	
	/**
	 * 
	 * @param registro
	 * @return
	 */
	private long inserir(Registro registro) {
		ContentValues values = new ContentValues();
		values.put(Registro.DATA, DataUtils.getDateToFullString(registro.getData()));
		values.put(Registro.LITROS, registro.getLitros());
		values.put(Registro.VALOR, registro.getValor());
		values.put(Registro.KILOMETRAGEM, registro.getKilometragem());
		return inserir(values); 
	}	
	private long inserir(ContentValues values) {
		Log.i(Constante.TAG_LOG, "Iserir novo registro");
		long result = db.insert(NOME_TABELA, "", values);
		return result;
	}

	/**
	 * 
	 * @param registro
	 * @return
	 */
	private int atualizar(Registro registro) {
		ContentValues values = new ContentValues();
		values.put(Registro.DATA, DataUtils.getDateToFullString(registro.getData()));
		values.put(Registro.LITROS, registro.getLitros());
		values.put(Registro.VALOR, registro.getValor());
		values.put(Registro.KILOMETRAGEM, registro.getKilometragem());
		
		String _id = String.valueOf(registro.getId());
		@SuppressWarnings("static-access")
		String where = registro._ID + "=?";
		String[] whereArgs = new String[]{_id};
		
		return atualizar(values, where, whereArgs);
	}	
	private int atualizar(ContentValues values, String where, String[] whereArgs) {
		int result = db.update(NOME_TABELA, values, where, whereArgs);
		return result;
	}
	
	/**
	 * 
	 * @return
	 * @throws ParseException 
	 */
	public List<Registro> listarTodos() throws ParseException{
		List<Registro> registros = new ArrayList<Registro>();
		Cursor cursor = getCursor();
		if(cursor.moveToFirst()){
			int _id = cursor.getColumnIndex(Registro._ID);
			int data = cursor.getColumnIndex(Registro.DATA);
			int litros = cursor.getColumnIndex(Registro.LITROS);
			int valor = cursor.getColumnIndex(Registro.VALOR);
			int kilometragem =  cursor.getColumnIndex(Registro.KILOMETRAGEM);
			
			do{
				Registro registro = new Registro();
				registro.setId(cursor.getLong(_id));
				registro.setData(DataUtils.getStringToFullDate(cursor.getString(data)));
				registro.setLitros(cursor.getInt(litros));
				registro.setValor(cursor.getInt(valor));
				registro.setKilometragem(cursor.getInt(kilometragem));
				registros.add(registro);
			}while(cursor.moveToNext());
		}
		return registros;
	}
	
	public Registro buscar(long id) throws ParseException{
		Registro registro = null;
		Cursor cursor = db.query(true, NOME_TABELA, Registro.colunas, Registro._ID + "=" + id, null, null, null, null, null);
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			registro = new Registro();
			registro.setId(cursor.getLong(0));
			registro.setData(DataUtils.getStringToFullDate(cursor.getString(1)));
			registro.setLitros(cursor.getInt(2));
			registro.setValor(cursor.getInt(3));
			registro.setKilometragem(cursor.getInt(4));
		}
		return registro;
	}
	
	

	private Cursor getCursor() {
		try {
			return db.query(NOME_TABELA, Registro.colunas, null, null, null, null, null, null);
		} catch (SQLException e) {
			Log.e(Constante.TAG_LOG, "Erro ao buscar registros: " + e.toString());
			return null;
		}
	}
	
	public void fecharConexao(){
		if(db != null) db.close();
	}

	public int deletar(long id) {
		String where = Registro._ID + "=?";
		String _id = String.valueOf(id);
		String[] whereArgs = new String[]{_id};
		return deletar(where, whereArgs);
		
	}

	private int deletar(String where, String[] whereArgs) {
		return db.delete(NOME_TABELA, where, whereArgs);
	}
}
