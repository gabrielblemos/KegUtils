package com.keg.kegutilsexample;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.keg.kegutils.RecyclerView.RecyclerScrollListener;
import com.keg.kegutils.RecyclerView.LoadableAdapter;
import com.keg.kegutils.RecyclerView.SimpleAdapter;
import com.keg.kegutils.View.ValidationEditText;
import com.keg.kegutils.View.Validator;
import com.keg.kegutils.RecyclerView.ItemOffsetDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.uol.ps.library.business.PSCheckout;
import br.com.uol.ps.library.business.PSCheckoutConfig;
import br.com.uol.ps.library.business.PSCheckoutRequest;
import br.com.uol.ps.library.business.PagSeguroResponse;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ValidationEditText editText;

    private List<String> elements;
    private SimpleAdapter simpleAdapter;
    private LoadableAdapter loadableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PSCheckoutConfig psCheckoutConfig = new PSCheckoutConfig();
        psCheckoutConfig.setSellerEmail("gabrieldebritolemos@gmail.com");
        psCheckoutConfig.setSellerToken("7C565004E7DF4867A2DA7B25317565D7");
        psCheckoutConfig.setEnvironment(PSCheckout.Environment.QA);
        psCheckoutConfig.setContainer(R.id.fragment_container);
        PSCheckout.init(this, psCheckoutConfig);

        String id = "001";
        String quantidade = "1";
        BigDecimal valor = new BigDecimal(1.0);
        PSCheckoutRequest psCheckoutRequest = new PSCheckoutRequest().withReferenceCode("123")
                .withNewItem("Descrição produto", quantidade, valor, id);

        PSCheckout.PSCheckoutListener psCheckoutListener = new PSCheckout.PSCheckoutListener() {
            @Override
            public void onSuccess(PagSeguroResponse pagSeguroResponse, Context context) {
            }

            @Override
            public void onFailure(PagSeguroResponse pagSeguroResponse, Context context) {
            }

            @Override
            public void onProcessing(Context context) {
            }
        };
        PSCheckout.pay(psCheckoutRequest, psCheckoutListener);
        //setUpRecyclerView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Fornece controle para LIB de Activity results
        PSCheckout.onActivityResult(this, requestCode, resultCode, data);//Controle Lib Activity Life Cycle
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PSCheckout.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (PSCheckout.onBackPressed(this)) {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                PSCheckout.onHomeButtonPressed(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PSCheckout.onDestroy();
    }

    private void setUpRecyclerView() {
        //mRecyclerView  = (RecyclerView) findViewById(R.id.recycler_view);

        ItemOffsetDecoration itemDecoration;
        itemDecoration = new ItemOffsetDecoration(this);
        mRecyclerView.addItemDecoration(itemDecoration);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        elements = new ArrayList<>();
        elements.add("one");
        elements.add("two");
        elements.add("tree");
        elements.add("four");
        elements.add("five");

//        mRecyclerView.setAdapter(getSimpleAdapter());
        mRecyclerView.setAdapter(getLoadableAdapter());
        RecyclerScrollListener scrollListener = new RecyclerScrollListener(loadableAdapter, mLayoutManager);
        mRecyclerView.addOnScrollListener(scrollListener);
    }

    public SimpleAdapter getSimpleAdapter() {
        if (simpleAdapter == null) {
            simpleAdapter = new SimpleAdapter<>(this);
            simpleAdapter.insertData(elements);
        }
        return simpleAdapter;
    }

    public LoadableAdapter getLoadableAdapter() {
        if (loadableAdapter == null) {
            loadableAdapter = new LoadableAdapter(this) {
                @Override
                protected String buildUrl(int loadPage) {
                    return "http://192.168.1.100:3000/kegutils/list?page=" + loadPage;
                }

                @Override
                protected List loadParser(JSONObject objectJSON) {
                    if (!objectJSON.has("itens")) {
                        isLastPage = false;
                        return null;
                    }

                    List returnList = new ArrayList();
                    try {
                        JSONArray array = objectJSON.getJSONArray("itens");
                        for (int i=0; i<array.length(); i++) {
                            returnList.add(array.get(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return returnList;
                }
            };
        }
        return loadableAdapter;
    }

}
