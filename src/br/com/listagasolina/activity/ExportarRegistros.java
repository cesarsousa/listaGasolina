package br.com.listagasolina.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import br.com.listagasolina.modelo.Registro;

public class ExportarRegistros extends Activity{
	
	private TextView textoRegistros;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exportar_registros);
		
		ClipboardManager myClipboard = myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
		ClipData myclip;		
		textoRegistros = (TextView) findViewById(R.id.txtExportarRegistros);
		
		StringBuilder sb = new StringBuilder();
		List<Registro> registros = (List<Registro>) getIntent().getSerializableExtra("registros");
		
		for(Registro r : registros){
			sb.append(r.getRegistroParaEsportacao() + "\n");
		}
		
		textoRegistros.setText(sb.toString());
		
		int min = 0;
		int max = textoRegistros.getText().length();
		if(textoRegistros.isFocused()){
			final int selStart = textoRegistros.getSelectionStart();
			final int selEnd = textoRegistros.getSelectionEnd();
			min = Math.max(0, Math.min(selStart, selEnd));
			max = Math.max(0, Math.max(selStart, selEnd));
		}
		final CharSequence selectedText = textoRegistros.getText().subSequence(min, max);
		String texto = selectedText.toString();
		
		myclip = ClipData.newPlainText("texto", texto);
		myClipboard.setPrimaryClip(myclip);
		
	}
	
	public void onBackPressed() {
	    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExportarRegistros.this);
	    alertDialogBuilder.setTitle("Atenção");
	 
	    alertDialogBuilder.setMessage("Voltar a tela inicial?").setCancelable(false).setPositiveButton("Sim",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	            	Intent intent = new Intent(ExportarRegistros.this, MainActivity.class);
	        		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        		startActivity(intent);
	            }
	        })
	        .setNegativeButton("Não",
	        new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id) {
	                dialog.cancel();
	            }
	        });
	 
	    AlertDialog alertDialog = alertDialogBuilder.create();
	    alertDialog.show();
	}

}
