package payment;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.my.easybus.Payment;

import org.json.JSONObject;

import BusinessLogic.LogicClass;
import encrypt.Vendor;
import utility.Utility;

/**
 * Created by Richard.Ezama on 4/22/2018.
 */

public class PostData {
    private Context context;
    private ProgressBar progressbar;
    private String amount, description, redirecturl, ticket;

    public PostData(Context context, ProgressBar progressbar,
                    String amount, String description, String ticket) {
        this.context = context;
        this.progressbar = progressbar;
        this.amount = amount;
        this.description = description;
        this.redirecturl = redirecturl;
    }

    public void startPost() {
        new AsyncTask<Void, Integer, String>() {
            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                progressbar.setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... arg0) {

                return LogicClass.
                        paymentAPI(context, amount, description, ticket);
            }

            // Show Dialog Box with Progress bar
            @Override
            protected void onPostExecute(String result) {
                // TODO Auto-generated method stub
                super.onPostExecute(result);
                progressbar.setVisibility(View.GONE);
                Utility.shoAlert(result, context);

            }
        }.execute();
    }
}
