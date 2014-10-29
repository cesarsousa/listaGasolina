package br.com.listagasolina.modelo;

import android.annotation.SuppressLint;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DataUtils {
	
	/**
	 * Converte um objeto para uma representação em formato string.
	 * @param postagem
	 * @return data no formato dd/MM/yyyy HH:mm:ss.
	 */
	public static String getDateToFullString(Date postagem) {
		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return spf.format(postagem.getTime());
	}
	
	/**
	 * Converte um objeto para uma representação em formato string.
	 * @param postagem
	 * @return data no formato dd/MM/yyyy.
	 */
	public static String getDateToString(Date postagem) {
		SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
		return spf.format(postagem.getTime());
	}
	
	/**
	 * 
	 * @param data Representacao de uma data no formato dd/MM/yyyy HH:mm:ss
	 * @return Um objeto do tipo <code>Date</code>
	 * @throws ParseException Caso a String data não esteja no formato 'dd/MM/yyyy HH:mm:ss'.
	 */
	public static Date getStringToDate(String data) throws ParseException {
		return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(data);
	}
	
	/**
	 * Retorna o número de dias decorridos de duas datas.
	 * @param dataPassado
	 * @param dataFuturo
	 * @return
	 */
	public static int diasEntre(Date dataPassado, Date dataFuturo){   
	       Calendar dInicial = Calendar.getInstance();   
	       dInicial.setTime(dataPassado);  
	       Calendar dFinal = Calendar.getInstance();  
	       dFinal.setTime(dataFuturo);  
	       int count = 0;      
	       while (dInicial.get(Calendar.DAY_OF_MONTH) != dFinal.get(Calendar.DAY_OF_MONTH)){      
	           dInicial.add(Calendar.DAY_OF_MONTH, 1);      
	           count ++;      
	       }      
	       return count;     
	   }  
	
	public static Date getDateNow(){
		return new Date();
	}

}
