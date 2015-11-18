package br.com.listagasolina.modelo;

import java.io.Serializable;
import java.util.Date;

import android.provider.BaseColumns;

public class Registro implements Comparable<Registro>, BaseColumns, Serializable{
	
	private static final long serialVersionUID = -7506295754851813731L;
	private long id;
	private Date data;
	private int litros;
	private int valor;
	private int kilometragem;
	
	public static final String DATA = "data";
	public static final String LITROS = "litros";
	public static final String VALOR = "valor";
	public static final String KILOMETRAGEM = "kilometragem";
	public static String[] colunas = {
		Registro._ID, 
		Registro.DATA, 
		Registro.LITROS, 
		Registro.VALOR, 
		Registro.KILOMETRAGEM};
	
	public Registro(){}
	
	public Registro(Date data, int litros, int valor, int kilometragem) {
		super();
		this.data = data;
		this.litros = litros;
		this.valor = valor;
		this.kilometragem = kilometragem;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public int getLitros() {
		return litros;
	}

	public void setLitros(int litros) {
		this.litros = litros;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	public int getKilometragem() {
		return kilometragem;
	}

	public void setKilometragem(int kilometragem) {
		this.kilometragem = kilometragem;
	}
	
	@Override
	public String toString() {
		return 
				DataUtils.getDateToString(getData()) + "\n\n" +
				"ID " + getId() + "\n" + 
				getLitros() + "L" + "\n" + 
				"R$ " + getValor() + "\n" +
				"Km " + getKilometragem();
	}
	
	public String getEstatistica() {
		return DataUtils.getDateToString(getData()) + " " + getLitros() + "L R$ " + getValor() + " KM " + getKilometragem();
	}

	@Override
	public int compareTo(Registro registro) {
		return (this.data).compareTo(registro.getData());
	}

	public String getRegistroParaEsportacao() {
		return DataUtils.getDateToString(getData())+"?"+litros+"?"+valor+"?"+kilometragem;
	}

}
