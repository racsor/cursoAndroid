package org.racsor.layoutapp;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {
	int mVistasTotales=0;
	int mVistasUsadas=0;
	Activity mActivity;
	public MyAdapter(Activity act){
		mActivity=act;
	}
	
	@Override
	public int getCount() {
		return 100000;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class DatosTag{
		TextView tv1;
		TextView tv2;
		int id;
		DatosTag (TextView tv1, TextView tv2, int id){
			this.tv1=tv1;
			this.tv2=tv2;
			this.id=id;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mVistasTotales++;
		if(convertView==null){
			mVistasUsadas++;
			convertView=mActivity.getLayoutInflater().inflate(android.R.layout.simple_list_item_2,parent,false);
			DatosTag dt=new DatosTag(((TextView)convertView.findViewById(android.R.id.text1)), ((TextView)convertView.findViewById(android.R.id.text2)), mVistasUsadas);
			convertView.setTag(dt);
		}
		//convertview esta preparado para usarse
		DatosTag datos=(DatosTag)convertView.getTag();
		datos.tv1.setText("Pos: "+ position+" ** Vistas totales: "+mVistasTotales);
		datos.tv2.setText("Vistas creadas: "+ mVistasUsadas+" ** Id de vist:"+datos.id);
		
		return convertView;
	}

}
