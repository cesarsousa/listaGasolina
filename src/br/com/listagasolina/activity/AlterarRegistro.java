package br.com.listagasolina.activity;

import java.util.Calendar;

import br.com.listagasolina.modelo.Registro;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class AlterarRegistro extends Activity implements Button.OnClickListener {
	private Button botao;
	static final int DATE_DIALOG_ID = 0;
	
	Registro registro;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alterar_registro);
		botao = (Button) findViewById(R.id.btnAlterarData);
		botao.setOnClickListener(this);		
		registro = (Registro) getIntent().getSerializableExtra("registro");
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(registro.getData());
		int ano = calendario.get(Calendar.YEAR);
		int mes = calendario.get(Calendar.MONTH);
		int dia = calendario.get(Calendar.DAY_OF_MONTH);
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, ano, mes, dia);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String data = String.valueOf(dayOfMonth) + " /"
					+ String.valueOf(monthOfYear + 1) + " /"
					+ String.valueOf(year);
			Toast.makeText(AlterarRegistro.this, "DATA = " + data, Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public void onClick(View v) {
		if (v == botao)
			showDialog(DATE_DIALOG_ID);
	}
}
