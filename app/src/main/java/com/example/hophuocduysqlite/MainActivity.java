package com.example.hophuocduysqlite;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Database;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private View lv_User;
    ArrayList<User> users;
    UserAdapter adapter;
    private DatabaseHandler database;
    private View btnSave;
    private View btnCancel;
    private View btnRemove;
    private View edt_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_User = findViewById(R.id.listUser);
        users = new ArrayList<>();

        adapter = new UserAdapter(this, R.layout.items_list, users);

        database = new DatabaseHandler(this, "Note.Sqlite", null, 1);
        database.queryData("CREATE TABLE IF NOT EXISTS Travel(id INTEGER PRIMARY KEY AUTOINCREMENT , name VARCHAR(50))");



        database.queryData("INSERT INTO User VALUES(null,'Đỗ Anh Bôn')");
        database.queryData("INSERT INTO User VALUES(null,'Hoàng Quốc Cường')");
        database.queryData("INSERT INTO User VALUES(null,'Phạm Minh Dũng')");

        getAllData();

        btnSave = findViewById(R.id.btnAdd);
        btnRemove = findViewById(R.id.btnRemove);
        btnCancel = findViewById(R.id.btnCancel);
        edt_Name = findViewById(R.id.editTextName);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String string = edt_Name.getTooltipText().toString();
                if (string.equals(""))
                    Toast.makeText(MainActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                else {
                    database.queryData("INSERT INTO Travel VALUES(null,'" + string + "')");
                    Toast.makeText(MainActivity.this, "Add Success", Toast.LENGTH_SHORT).show();
                    getAllData();
                }

            }
        });
    }

    private void getAllData() {
        Cursor cursor = database.queryDataReturn("SELECT * FROM Travel");
        users.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            users.add(new User(id, name));
        }
        adapter.notifyDataSetChanged();
    }

    public void deleteUser(String name, int id){
        AlertDialog.Builder dialogXoa = new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa '"+name+"' không ???");
        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                database.queryData("DELETE FROM Travel where id = '"+id+"'");
                Toast.makeText(MainActivity.this, "Đã xóa " + name, Toast.LENGTH_SHORT).show();
                getAllData();
            }
        });
    }
}