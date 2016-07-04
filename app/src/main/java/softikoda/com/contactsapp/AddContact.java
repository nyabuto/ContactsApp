package softikoda.com.contactsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import Database.ManageContacts;

public class AddContact extends AppCompatActivity {
EditText add_name,add_phone,add_email;
    ImageView add_image;
    Button add_btn;
    String name,phone,image_url,email;
    private static final int RESULT_LOAD_IMAGE = 1;
    Uri selectedImage;
    private final Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        add_name = (EditText) findViewById(R.id.add_name);
        add_phone=(EditText) findViewById(R.id.add_phone);
        add_email=(EditText) findViewById(R.id.add_email);
        add_image = (ImageView) findViewById(R.id.add_image);
        add_btn = (Button) findViewById(R.id.add_btn);
        name=phone=image_url="";
        add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),"Image to be loaded",Toast.LENGTH_SHORT).show();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(selectedImage==null){
    image_url="";
}
                else{
    image_url=""+selectedImage;
                }

                name=add_name.getText().toString().trim();
                phone=add_phone.getText().toString().trim();
                email=add_email.getText().toString().trim();

                if(name.equals("") || phone.equals("") || email.equals("")){
                    Toast.makeText(getApplicationContext(),"Enter fullname, email and password.",Toast.LENGTH_SHORT).show();
                }
                else{
//                    save to the database
                    ManageContacts contacts = new ManageContacts(context);
                    contacts.addContact(name,phone,image_url,email);

                    Snackbar.make(v, "Contact Saved successfully.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            selectedImage = data.getData();
        }
        add_image.setImageURI(selectedImage);
        Log.d("Image uri",selectedImage.getPath());
 }

}
