package br.com.listagasolina.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import br.com.listagasolina.modelo.Registro;

public class Estatisticas extends Activity {
	
	private TextView textoEstatisticas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.estatisticas);
		
		textoEstatisticas = (TextView) findViewById(R.id.txtEstatisticas);		
		
		StringBuilder sb = new StringBuilder();
		sb.append("Estatísticas Gerais\n");
		
		List<Registro> registros = (List<Registro>) getIntent().getSerializableExtra("registros");
				
		while(registros.size() > 0){
			Calendar dataInicial = Calendar.getInstance();
			dataInicial.setTime(registros.get(0).getData());
			
			List<Registro> listaMensal = new ArrayList<Registro>();
			for(Registro reg : registros){
				Calendar dataDoRegistro = Calendar.getInstance();
				dataDoRegistro.setTime(reg.getData());
				if(dataInicial.get(Calendar.MONTH) == dataDoRegistro.get(Calendar.MONTH)
						&& dataInicial.get(Calendar.YEAR) == dataDoRegistro.get(Calendar.YEAR)){
					listaMensal.add(reg);
				}				
			}
			
			registros.removeAll(listaMensal);
			
			int totalValorDoMes = getTotalValor(listaMensal);
			int totalGasolinaDoMes = getTotalGasolina(listaMensal);
			
			Calendar dataReferencia = Calendar.getInstance();
			dataReferencia.setTime(listaMensal.get(0).getData());
			sb.append("\n\nREFERÊNCIA " + (dataReferencia.get(Calendar.MONTH)+1) + "/" + dataReferencia.get(Calendar.YEAR) + "\n\n");			
			
			int kmInicial = listaMensal.get(0).getKilometragem();
			int kmFinal = listaMensal.get(listaMensal.size()-1).getKilometragem();
			
			for(Registro r : listaMensal){
				sb.append(r.getEstatistica() + "\n");
			}
			
			sb.append("\n" + listaMensal.size() + " Registro\n");
			sb.append("\n");
			sb.append("Total de abastecimento: " + totalGasolinaDoMes + " L\n");
			sb.append("Gasto mensal: R$ " + totalValorDoMes + "\n");
			sb.append("Total Kilometros: " + (kmFinal - kmInicial) + "\n");
			//sb.append("Média por litro: " + ((kmFinal - kmInicial)/totalGasolinaDoMes) + "\n");
			sb.append(".........................................");
		}

		textoEstatisticas.setText(sb.toString());
	}

	

	private int getTotalGasolina(List<Registro> listaMensal) {
		int soma = 0;
		for(Registro r : listaMensal){
			soma = soma + r.getLitros();
		}
		return soma;
	}

	private int getTotalValor(List<Registro> listaMensal) {
		int soma = 0;
		for(Registro r : listaMensal){
			soma = soma + r.getValor();
		}
		return soma;
	}
	
	public void onBackPressed() {
		Intent intent = new Intent(Estatisticas.this, MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
