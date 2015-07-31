package br.com.listagasolina.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Estatisticas extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		
		  TextView view = new TextView(this);
		  
		  StringBuilder sb = new StringBuilder();
		  sb.append("Estatísticas Gerais\n\n");
		  sb.append("Total Geral\n\n");
		  sb.append("Toatla de kilometros\n\n");
		  
		  
		  view.setText(sb.toString());
	        setContentView(view);
	}

}
