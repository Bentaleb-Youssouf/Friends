package ti.dam.bentaleb.benali.friends;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ti.dam.bentaleb.benali.friends.Database.MyHelper;

/**
 * Created by Bentaleb Youssouf on 12/3/2017.
 */

public class AddDialog extends Dialog {



    EditText name ;
    EditText prenom ;

    public AddDialog(@NonNull Context context) { super(context); }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.add_dialog);

        name =  (EditText)findViewById(R.id.nom_add);
        prenom =  (EditText)findViewById(R.id.prenom_add);


        Button cancelBtn = (Button)findViewById(R.id.cancelBtn);

        Button addFriendBtn = (Button)findViewById(R.id.addFriendBtn);
        addFriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MyHelper helper = new MyHelper(getContext());

                //helper.createFriend(new FriendItem(R.drawable.avatar_02,name.getText().toString()
                        //,"winta tjini","2 min ago"));

                Toast.makeText(getContext(),"Button add click",Toast.LENGTH_SHORT).show();
                //FriendActivity.adapter.notifyDataSetChanged();
                dismiss();

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
