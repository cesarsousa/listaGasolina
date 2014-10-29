package br.com.listagasolina.modelo;

import java.util.List;

import com.example.listagasolina.R;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class RegistroAdapter extends BaseAdapter implements ListAdapter {
	
	private Context context;
	private List<Registro> registros;
	
	public RegistroAdapter(Context context, List<Registro> registros) {
		this.context = context;
		this.registros = registros;
	}

	@Override
	public int getCount() {
		return registros.size();
	}

	@Override
	public Object getItem(int position) {
		return registros.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		
		Registro registro  = registros.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View registroView = inflater.inflate(R.layout.list_adapter_registro, null);
		
		TextView textViewData = (TextView) registroView.findViewById(R.id.listAdapterRegistroData);
		textViewData.setText(DataUtils.getDateToString(registro.getData()));
		
		TextView textViewLitros = (TextView) registroView.findViewById(R.id.listAdapterRegistroLitros);
		textViewLitros.setText(String.valueOf(registro.getLitros()) + " L");
		
		TextView textViewValor = (TextView) registroView.findViewById(R.id.listAdapterRegistroValor);
		textViewValor.setText(" R$ " + String.valueOf(registro.getValor()));
		
		TextView textViewKilometragem = (TextView) registroView.findViewById(R.id.listAdapterRegistroKilometragem);
		textViewKilometragem.setText("Km " + String.valueOf(registro.getKilometragem()));
		
		
		
		return registroView;
		
	}

}