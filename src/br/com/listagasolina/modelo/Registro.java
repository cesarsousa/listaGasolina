package br.com.listagasolina.modelo;

import java.util.Date;

import android.provider.BaseColumns;

public class Registro implements Comparable<Registro>, BaseColumns{
	
	
	private long id;
	private Date data;
	private double litros;
	private double valor;
	private double kilometragem;
	
	public static final String DATA = "data";
	public static final String LITROS = "LITROS";
	public static final String VALOR = "valor";
	public static final String KILOMETRAGEM = "kilometragem";
	public static String[] colunas = {
		Registro._ID, 
		Registro.DATA, 
		Registro.LITROS, 
		Registro.VALOR, 
		Registro.KILOMETRAGEM};
	
	public Registro(){}
	
	public Registro(Date data, double litros, double valor, double kilometragem) {
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

	public double getLitros() {
		return litros;
	}

	public void setLitros(double litros) {
		this.litros = litros;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public double getKilometragem() {
		return kilometragem;
	}

	public void setKilometragem(double kilometragem) {
		this.kilometragem = kilometragem;
	}

	@Override
	public int compareTo(Registro registro) {
		return (this.data).compareTo(registro.getData());
	}

}
