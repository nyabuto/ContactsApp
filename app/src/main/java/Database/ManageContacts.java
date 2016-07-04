package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Geofrey on 2/12/2016.
 */
public class ManageContacts extends DatabaseHandler {
private  static final String TABLE_MPESA="mpesa_charges";
    private  static final String TABLE_ATM="atm_charges";
SQLiteDatabase db;
String TABLE_NAME="all_contacts";

    public ManageContacts(Context context) {
        super(context);
    }


   public boolean addContact(String name,String phone,String imageUrl,String email){
       db = this.getWritableDatabase();
       ContentValues values = new ContentValues();
       values.put("name",name);
       values.put("phone_no",phone);
       values.put("email",email);
       values.put("image_url",imageUrl);

       boolean status=db.insert(TABLE_NAME,null,values)>0;
      return status;
   }

    public int checkContact(String name, String phone,String email){
        db=this.getReadableDatabase();
        String[] whereArgs={name,phone,email};
        String whereClause="(name=? AND phone_no=?) OR email=?";

        int no_existing=db.query(TABLE_NAME, new String[]{"id"}, whereClause,whereArgs, null, null, null, null).getCount();

      return no_existing;
    }

public int deleteContacts(String[] allIDs){
    db = this.getWritableDatabase();
    int no_deleted=0;
    String whereClause="";
    for (int i=0;i<allIDs.length;i++){
      whereClause+="id=? OR ";
    }
    whereClause=whereClause.substring(0,whereClause.length()-3);
    Log.d("where clause : ",whereClause);

           no_deleted+= db.delete(TABLE_NAME,whereClause,allIDs);

    return no_deleted;
}

    public ArrayList<HashMap<String,String>> getAllContacts(){
        db = this.getReadableDatabase();
        String id,name,phone_no,image_url,email;

        ArrayList<HashMap<String,String>> allContactsArray = new ArrayList<HashMap<String, String>>();

        String sql="SELECT id,name,phone_no,email,image_url FROM "+TABLE_NAME+" ORDER BY name,phone_no";
        Cursor cursor=db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{

                HashMap<String, String> contactMap = new HashMap<String,String>();
                id =cursor.getString(cursor.getColumnIndexOrThrow("id"));
                name =cursor.getString(cursor.getColumnIndexOrThrow("name"));
                phone_no =cursor.getString(cursor.getColumnIndexOrThrow("phone_no"));
                email=cursor.getString(cursor.getColumnIndexOrThrow("email"));
                image_url =cursor.getString(cursor.getColumnIndexOrThrow("image_url"));



                contactMap.put("id",id);
                contactMap.put("name",name);
                contactMap.put("phone_no",phone_no);
                contactMap.put("email",email);
                contactMap.put("image_url",image_url);
                allContactsArray.add(contactMap);

            } while (cursor.moveToNext());
        }
return allContactsArray;
    }

    public HashMap<String,String> getContactDetails(String id){
     db = this.getReadableDatabase();
        String name,phone_no,image_url,email;
        HashMap<String, String> contactMap = new HashMap<String,String>();
        String sql="SELECT name,phone_no,image_url,email FROM "+TABLE_NAME+" WHERE id='"+id+"' ORDER BY name,phone";
        Cursor cursor=db.rawQuery(sql,null);

        if(cursor.moveToFirst()){
            do{

                name =cursor.getString(1);
                phone_no =cursor.getString(2);
                image_url =cursor.getString(3);
                email =cursor.getString(4);

                contactMap.put("name",name);
                contactMap.put("phone_no",phone_no);
                contactMap.put("image_url",image_url);
                contactMap.put("email",email);


            } while (cursor.moveToNext());
        }
        return contactMap;
    }

    public boolean updateContact(String id,String name,String phone,String email){
        db= this.getWritableDatabase();
       ContentValues values = new ContentValues();
       // values.put("id",id);
        values.put("name",name);
        values.put("phone_no",phone);
        values.put("email",email);
        String[] whereArgs = {id};
        boolean status = db.update(TABLE_NAME,values,"id=?",whereArgs)>0;
       // boolean status = db.rawQuery("UPDATE "+TABLE_NAME+" SET name='"+name+"' AND phone_no='"+phone+"' WHERE id='"+id+"'",null).getCount()>0;

        return status;
    }
}
