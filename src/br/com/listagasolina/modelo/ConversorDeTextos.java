package br.com.listagasolina.modelo;

import java.text.ParseException;
import java.util.ArrayList;

public class ConversorDeTextos {	
	
	private String registros;

	public ConversorDeTextos(String strTextoRegistros) {
		this.registros = strTextoRegistros;
	}

	public ArrayList<Registro> obterRegistros() {
		ArrayList<Registro> listaDeRegistros = new ArrayList<Registro>();		
			
		String[] linhasCompletas = registros.split("@");

		for (String linhaCompleta : linhasCompletas) {
			String[] linha = linhaCompleta.split("#");

			try {
				Registro registro = new Registro(
						DataUtils.getStringToDate(linha[0].trim()),
						Integer.valueOf(linha[1].trim()), 
						Integer.valueOf(linha[2].trim()),
						Integer.valueOf(linha[3].trim()));

				listaDeRegistros.add(registro);
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}					
		return listaDeRegistros;		
	}
}
