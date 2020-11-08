package com.example.listview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.listview.Model.LanguageInfo;
import com.example.listview.Utility.DbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    ListView toDoList;
    FloatingActionButton fab;
    EditText txtName,txtDesc,txtDate,txtid;
    Button btnSave,btnCancel,btnDelete;
    AlertDialog alertDialog;
    int tmpPosition=-9;
    LanguageAdapter adapter;
    ArrayList<LanguageInfo> arrayList= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toDoList=(ListView) findViewById(R.id.lstToDo);

        fab=(FloatingActionButton)findViewById(R.id.fab);
        //Get Data from list
        //GenerateList();
        //One Line Array
        /*
        //context,Layout,Array
        String[] nameList={"Java","Android","C#","C++","PHP","Python","GO"};
        ArrayAdapter adapter=new ArrayAdapter<String>(this,R.layout.list_item,nameList);

        toDoList=(ListView) findViewById(R.id.lstToDo);
        toDoList.setAdapter(adapter);
        */

        try {
            //Get Data from sqlLite
            GetAllData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        adapter=new LanguageAdapter(this,arrayList);
        toDoList.setAdapter(adapter);
        toDoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                showPopupDialog(arrayList.get(i));
                tmpPosition=i;//Array index
                //Toast.makeText(MainActivity.this,arrayList.get(i).getName(),Toast.LENGTH_SHORT).show();
            }
        });

        fab.setOnClickListener(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        try {
            GetAllData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public  void GetAllData() throws ParseException {
        DbHelper dbHelper=new DbHelper(MainActivity.this);
        arrayList= dbHelper.GetAll();
    }
    private void GenerateList()
    {
        for (int i=0;i<2;i++)
        {
            LanguageInfo info=new LanguageInfo();
            info.setId(i+1);
            info.setName("Language"+(i+1));
            Date d=new Date();
            info.setReleaseDate(d);
            info.setDescription(info.getName()+" " +"Lorem Ipsum is simply dummy text of the printing and typesetting industry.");
            arrayList.add(info);
        }
    }

    private void showPopupDialog(LanguageInfo info)
    {
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(MainActivity.this);

        LayoutInflater inflater=LayoutInflater.from(this);
        View v=inflater.inflate(R.layout.language_view,null);
        dialogBuilder.setView(v);
        dialogBuilder.setCancelable(false);
        alertDialog= dialogBuilder.create();
        alertDialog.show();//Showing

         txtid=(EditText) v.findViewById(R.id.txtid);
         txtName=(EditText) v.findViewById(R.id.txtName);
         txtDesc=(EditText) v.findViewById(R.id.txtDesc);
         txtDate=(EditText) v.findViewById(R.id.txtDate);

         if(info !=null)
         {
             txtid.setText(String.valueOf(info.getId()));
             txtName.setText(info.getName());
             txtDesc.setText(info.getDescription());
             txtDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(info.releaseDate));
         }
         btnSave =(Button) v.findViewById(R.id.btnSave);
         btnCancel =(Button) v.findViewById(R.id.btnCancel);
         btnDelete =(Button) v.findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.fab:
                tmpPosition=-9;
                showPopupDialog(null);
                break;
            case R.id.btnSave:
                //Code to insert Here
                String name=txtName.getText().toString();
                String desc=txtDesc.getText().toString();
                String releaseDate=txtDate.getText().toString();

                int id=Integer.parseInt(txtid.getText().toString());

                LanguageInfo info=new LanguageInfo();
                if(tmpPosition>=0)
                {
                    info=arrayList.get(tmpPosition);
                }
                info.setId(id);
                info.setName(name);
                info.setDescription(desc);

                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    info.setReleaseDate(df.parse(releaseDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Insert to Database
                DbHelper db = new DbHelper(this);

                if(tmpPosition<0) { //ForNew Data
                    id = db.Insert(info);
                    info.setId(id);
                    arrayList.add(info);
                }
                else //For Update Data
                {
                    db.Update(info);
                }

                adapter.notifyDataSetChanged();
                alertDialog.hide();
                tmpPosition=-9;
                break;
            case R.id.btnCancel:
                tmpPosition=-9;
                alertDialog.cancel();
                break;
            case R.id.btnDelete:
                int deletedid=Integer.parseInt(txtid.getText().toString());
                if(deletedid<0)
                {
                    return;
                }
                else
                {
                    if(tmpPosition>=0)
                    {
                        arrayList.remove(tmpPosition);
                        adapter.notifyDataSetChanged();
                    }
                }
                alertDialog.hide();
                tmpPosition=-9;
                break;
        }
    }

    public void onClickold(View view) {
        switch (view.getId())
        {
             case R.id.fab:
                 tmpPosition=-9;
                 showPopupDialog(null);
                break;
            case R.id.btnSave:
                //Code to insert Here
                String name=txtName.getText().toString();
                String desc=txtDesc.getText().toString();
                String releaseDate=txtDate.getText().toString();

                int id=Integer.parseInt(txtid.getText().toString());
                if(arrayList ==null || arrayList.size()==0)
                {
                    id=1;
                }
                else if(id<0)
                {
                    id += arrayList.get(arrayList.size()-1).getId();
                }

                LanguageInfo info=new LanguageInfo();
                if(tmpPosition>=0)
                {
                    info=arrayList.get(tmpPosition);
                }
                info.setId(id);
                info.setName(name);
                info.setDescription(desc);

                SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
                try {
                    info.setReleaseDate(df.parse(releaseDate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //add to list if currenct position is less than 0
                if(tmpPosition<0) {
                    arrayList.add(info);
                }
                adapter.notifyDataSetChanged();
                alertDialog.hide();
                tmpPosition=-9;
                break;
            case R.id.btnCancel:
                tmpPosition=-9;
                alertDialog.cancel();
                break;
            case R.id.btnDelete:
                int deletedid=Integer.parseInt(txtid.getText().toString());
                if(deletedid<0)
                {
                    return;
                }
                else
                {
                    if(tmpPosition>=0)
                    {
                        arrayList.remove(tmpPosition);
                        adapter.notifyDataSetChanged();
                    }
                }
                alertDialog.hide();
                tmpPosition=-9;
                break;
        }
    }
}