package com.trackerbuddy.carassist;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

     public Map<Integer,BluetoothDevice> BluetoothMenu =  new HashMap<Integer, BluetoothDevice>();
    private static final String cardevicedetails  = "RegisteredCarDevice3.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            FileInputStream registeredDevice=openFileInput(cardevicedetails);
            String registeredCar="";
            int size;
            String neuText = null;

            // read inside if it is not null (-1 means empty)
            while ((size = registeredDevice.read()) != -1) {
                // add & append content
                neuText += Character.toString((char) size);
            }

            registeredDevice.close();
            if(neuText.isEmpty()){
               // throw FileNotFoundException;
            }
            else {
                String[] cardetails=neuText.split(",");
                displayCarDetails(cardetails[0],cardetails[1]);
            }
        } catch (FileNotFoundException e) {
            Button setupButton= (Button)findViewById(R.id.button);
            setupButton.setVisibility(View.VISIBLE);
            setupButton.setOnCreateContextMenuListener(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId()== R.id.button) {
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
            menu.setHeaderTitle("Select Car Device");
            int menuId= 1;

            if (pairedDevices.size() > 0) {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    BluetoothMenu.put(menuId,device);
                    menu.add(0,menuId,0,deviceName);
                    menuId++;
                }
            }
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        int menuId=item.getItemId();
        BluetoothDevice cardevice = BluetoothMenu.get(menuId);
        try{
            FileOutputStream fos = openFileOutput(cardevicedetails,MODE_PRIVATE);
            String cardetails=cardevice.getName().trim()+","+cardevice.getAddress();
            fos.write(cardetails.getBytes());
            fos.close();
            findViewById(R.id.button).setVisibility(View.INVISIBLE);
            Toast.makeText(this, "Device "+cardevice.getName()+" is set!",Toast.LENGTH_LONG).show();
            displayCarDetails(cardevice.getName(),cardevice.getAddress());
            return true;
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return false;
    }

    public void displayCarDetails(String name, String addr){
        TextView v= (TextView)findViewById(R.id.text2);

        v.setText("Registered Device details :\nName:"+name+"\nMac Addr:"+addr);
    }

}
