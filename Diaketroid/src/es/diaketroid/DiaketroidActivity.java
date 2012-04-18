package es.diaketroid;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DiaketroidActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void identificarse(View v){
    	String username = ((EditText)findViewById(R.id.campoNombre)).getText().toString();
    	String password = ((EditText)findViewById(R.id.campoPassword)).getText().toString();
    	LoginTask task = new LoginTask();
    	task.execute(username,password);
    }
    
    class LoginTask extends AsyncTask<String,Void,String>{

    	 @Override
        protected void onPreExecute() {
           
        }
    	
    	@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return null;
		}
    	
    	@Override
        protected void onPostExecute(String result) {
           
        }
    	
    }
}