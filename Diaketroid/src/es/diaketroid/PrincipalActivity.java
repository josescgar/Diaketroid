package es.diaketroid;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

public class PrincipalActivity extends TabActivity {
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menutabs);
		
		TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, CuotaActivity.class);
	    spec = tabHost.newTabSpec("cuota").setIndicator("Cuota").setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, HistorialActivity.class);
	    spec = tabHost.newTabSpec("historial").setIndicator("Historial").setContent(intent);
	    tabHost.addTab(spec);
	    
	    intent = new Intent().setClass(this, CuentaActivity.class);
	    spec = tabHost.newTabSpec("cuenta").setIndicator("Cuenta").setContent(intent);
	    tabHost.addTab(spec);
	    
	    tabHost.setCurrentTab(0);
	}
}
