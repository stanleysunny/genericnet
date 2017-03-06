import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.temenos.dshubhamrajput.genericnet.AcctSumActivity;
import com.temenos.dshubhamrajput.genericnet.R;

import java.util.ArrayList;

/**
 * Created by upriya on 06-03-2017.
 */

public class AddBeneficiary extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
