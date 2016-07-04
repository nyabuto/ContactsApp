package softikoda.com.contactsapp;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import Database.DataObjects;
import Database.ManageContacts;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    public ArrayList<DataObjects> mDataset;
    MyContacts contacts = new MyContacts();
    final ArrayList<String> selectedIDs = new ArrayList<>();
public Context context;
    public ManageContacts manageContacts;
    public static class DataObjectHolder extends RecyclerView.ViewHolder
            implements View .OnClickListener {
        TextView name,phone,emailtxt;
        CheckBox checkBox;
        ImageView image;
        ImageButton sendSMS,call,editDetails,delete,email;

        public DataObjectHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            phone = (TextView) itemView.findViewById(R.id.phone);
            emailtxt = (TextView) itemView.findViewById(R.id.emailtxt);
            checkBox = (CheckBox)itemView.findViewById(R.id.checkBox);
            image = (ImageView) itemView.findViewById(R.id.image);
            sendSMS = (ImageButton) itemView.findViewById(R.id.sendSMS);
            call = (ImageButton) itemView.findViewById(R.id.call);
            editDetails = (ImageButton) itemView.findViewById(R.id.editDetails);
            delete = (ImageButton) itemView.findViewById(R.id.delete);
            email = (ImageButton) itemView.findViewById(R.id.email);

            Log.i(LOG_TAG, "Adding Listener");
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
         //  myClickListener.onItemClick(getAdapterPosition(), v);


        }
    }

    public MyRecyclerViewAdapter(ArrayList<DataObjects> myDataset, Context context, ManageContacts manageContacts) {
        mDataset = myDataset;
        this.context=context;
        this.manageContacts=manageContacts;
        selectedIDs.clear();
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_view_row, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.name.setText(mDataset.get(position).getName());
        holder.phone.setText(mDataset.get(position).getPhone_no());
        holder.emailtxt.setText(mDataset.get(position).getEmail());
//        File imageFile=new File()
holder.image.setImageURI(Uri.parse(mDataset.get(position).getImageUrl()));

        holder.sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,phone;
                name=mDataset.get(position).getName();
                phone=mDataset.get(position).getPhone_no();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("sms:"+phone));
                context.startActivity(intent);

            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,phone;
                name=mDataset.get(position).getName();
                phone=mDataset.get(position).getPhone_no();

  Intent intent = new Intent(Intent.ACTION_DIAL);
    intent.setData(Uri.parse("tel:"+phone));
    context.startActivity(intent);
            }
        });

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name,phone,emailAddress;
                name=mDataset.get(position).getName();
                emailAddress=mDataset.get(position).getEmail();

                String[] TO = {emailAddress};
                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Ref : ");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi "+name);

                try {
                    context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        holder.editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             String name,phone,email;
             final String  id= mDataset.get(position).getId();
                name=mDataset.get(position).getName();
                phone=mDataset.get(position).getPhone_no();
                email=mDataset.get(position).getEmail();


                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.edit_contact);
                dialog.setTitle("Update user details");
                dialog.setCancelable(true);

                final EditText editName = (EditText) dialog.findViewById(R.id.edit_name);
                final EditText editPhone = (EditText) dialog.findViewById(R.id.edit_phone);
                final EditText editEmail = (EditText) dialog.findViewById(R.id.edit_email);
                Button btnUpdate = (Button) dialog.findViewById(R.id.edit_save);
                editName.setText(name);
                editPhone.setText(phone);
                editEmail.setText(email);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                        String  newName = editName.getText().toString().trim();
                        String newPhone = editPhone.getText().toString().trim();
                        String newemail = editEmail.getText().toString().trim();

                        if(!newName.equals("") && !newPhone.equals("")){
Log.d("To update","name is : "+newName+" and phone : "+newPhone);
                            manageContacts = new ManageContacts(context);
                            manageContacts.updateContact(id,newName,newPhone,newemail);
                            Toast.makeText(context,"User details updated successfully.",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(context,"Enter name and phone number",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

//        DELETE ELEMENTS FROM THE SYSTEM

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactId=mDataset.get(position).getId();
                String[] toDelete = {contactId};
                Log.d("contactid",contactId);
                manageContacts = new ManageContacts(context);
                manageContacts.deleteContacts(toDelete);
                deleteItem(position);
                Toast.makeText(context,"Contact deleted successfully.",Toast.LENGTH_SHORT).show();

//                String[] toDelete = new String[selectedIDs.size()];
//                selectedIDs.toArray(toDelete);
//                manageContacts = new ManageContacts(context);
//               int no_deleted= manageContacts.deleteContacts(toDelete);
//                Toast.makeText(context,"No deleted : "+no_deleted,Toast.LENGTH_SHORT).show();
            }
        });

holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(holder.checkBox.isChecked()){
          if(selectedIDs.contains(mDataset.get(position).getId()))  {

          }
            else{
              selectedIDs.add(mDataset.get(position).getId());
          }
        }

        else{
            if(selectedIDs.contains(mDataset.get(position).getId()))  {
                selectedIDs.remove(mDataset.get(position).getId());
            }
            else{

            }
        }
        MyContacts contact = new MyContacts();
        String[] toDelete = new String[selectedIDs.size()];
       selectedIDs.toArray(toDelete);
        contact.receiveToDelete(selectedIDs);
    }
});

    }

    public void addItem(DataObjects dataObj, int index) {
        mDataset.add(index, dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

public static class ClassDelete{
//    public String[] returnToDelete(){
////        String[] toDelete = new String[selectedIDs.size()];
////        selectedIDs.toArray(toDelete);
////        return toDelete;
//    }
}
}