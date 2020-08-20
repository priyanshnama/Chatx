package com.priyanshnama.chatx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import java.util.ArrayList;

public class FindUserActivity extends AppCompatActivity {

    private RecyclerView.Adapter<UserListAdapter.UserListViewHolder> userListAdapter;
    private ArrayList<UserObject> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);

        userList = new ArrayList<>();

        initializeRecyclerView();
        getContactList();
    }

    public void getContactList(){
        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        assert phones != null;
        while (phones.moveToNext()){
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            UserObject mContact = new UserObject(name, phone);
            userList.add(mContact);
            userListAdapter.notifyDataSetChanged();
        }
        phones.close();
    }

    private void initializeRecyclerView() {
        RecyclerView mUserList = findViewById(R.id.userList);
        mUserList.setNestedScrollingEnabled(false);
        mUserList.setHasFixedSize(false);
        mUserList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
        userListAdapter = new UserListAdapter(userList);
        mUserList.setAdapter(userListAdapter);
    }

    public static class UserObject {
        private String name, phone;

        public UserObject(String name, String phone){
            this.name = name;
            this.phone = phone;
        }

        public String getName() { return name; }
        public String getPhone() { return phone; }
    }
}