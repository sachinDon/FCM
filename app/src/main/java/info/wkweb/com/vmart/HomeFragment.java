package info.wkweb.com.vmart;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;


public class HomeFragment extends Fragment {
    Context context;
    JSONArray array_vegitables,array_vegitables1,array_root,array_ground,array_underground,array_leaf,array_fruits;
    public SharedPreferences pref;
    SharedPreferences.Editor editor;
    RecyclerView recyclerView;
    RelativeLayout relative_slects_homecat;
    TextView text_setcat_homecat,text_result_vendor_home;
    RelativeLayout relative_vendortodayprice,relative_vendor_divs,relative_vendor_custrate;
    SearchView searchbar;
    VenderAdapter adapter;
    ProgressBar progress_homevend;
    String str_category_select;

    public HomeFragment() {
        // Required empty public constructor
    }







    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();

        progress_homevend  = (ProgressBar) view.findViewById(R.id.progress_homevend);
        relative_vendortodayprice = (RelativeLayout) view.findViewById(R.id.relative_vendortodayprice);
        relative_vendor_divs = (RelativeLayout) view.findViewById(R.id.relative_vendor_divs);
        relative_vendor_custrate  = (RelativeLayout) view.findViewById(R.id.relative_vendor_custrate);

         recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView_vend);
        searchbar = (SearchView) view.findViewById(R.id.vend_searchView);
        text_result_vendor_home = (TextView) view.findViewById(R.id.text_result_vendor_home);
        text_result_vendor_home.setVisibility(View.INVISIBLE);
        int id = searchbar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText textView = (EditText) searchbar.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);




        relative_slects_homecat = (RelativeLayout) view.findViewById(R.id.relative_slects_homecat);
        text_setcat_homecat = (TextView) view.findViewById(R.id.text_setcat_homecat);
        str_category_select ="All";
        array_vegitables1 = new JSONArray();
        array_vegitables = new JSONArray();
        array_fruits = new JSONArray();
        array_ground = new JSONArray();
        array_root = new JSONArray();
        array_leaf = new JSONArray();
        array_underground = new JSONArray();
//        showInputMethod(view.findFocus());
//        searchbar.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//                if (hasFocus) {
//                    showInputMethod(view.findFocus());
//                }
//            }
//        });


        relative_slects_homecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                final Integer tagid = (Integer)v.getTag();

                PopupMenu menu = new PopupMenu(context, v);


                menu.getMenu().add("All");
                menu.getMenu().add("Root");
                menu.getMenu().add("Leaf");
                menu.getMenu().add("Ground");
                menu.getMenu().add("Beans");
                menu.getMenu().add("Fruits");
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        Integer int_index = item.getGroupId();
                        text_setcat_homecat.setText(String.valueOf(item));
                        str_category_select = String.valueOf(item);
                        if (str_category_select.equalsIgnoreCase("All"))
                        {
                            array_vegitables = array_vegitables1;
                        }
                        if (str_category_select.equalsIgnoreCase("Root"))
                        {
                            array_vegitables = array_root;
                        }
                        if (str_category_select.equalsIgnoreCase("Ground"))
                        {
                            array_vegitables = array_ground;
                        }
                        if (str_category_select.equalsIgnoreCase("Leaf"))
                        {
                            array_vegitables = array_leaf;
                        }
                        if (str_category_select.equalsIgnoreCase("Beans"))
                        {
                            array_vegitables = array_underground;
                        }
                        if (str_category_select.equalsIgnoreCase("Fruits"))
                        {
                            array_vegitables = array_fruits;
                        }


                    if (array_vegitables != null) {

                        adapter = new VenderAdapter(array_vegitables,getActivity());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(adapter);
                    }

                        return false;
                    }
                });

                menu.show();
            }
        });

        searchbar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                Log.d("seach_query", query);
                // do something on text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {


                array_vegitables = new JSONArray();
                if (array_vegitables1 != null)
                {
                    if (TextUtils.isEmpty(newText.toString())) {

                        //str_search_txt = "";

                        if (str_category_select.equalsIgnoreCase("All"))
                        {
                            array_vegitables = array_vegitables1;
                        }
                        if (str_category_select.equalsIgnoreCase("Root"))
                        {
                            array_vegitables = array_root;
                        }
                        if (str_category_select.equalsIgnoreCase("Ground"))
                        {
                            array_vegitables = array_ground;
                        }
                        if (str_category_select.equalsIgnoreCase("Leaf"))
                        {
                            array_vegitables = array_leaf;
                        }
                        if (str_category_select.equalsIgnoreCase("Beans"))
                        {
                            array_vegitables = array_underground;
                        }
                        if (str_category_select.equalsIgnoreCase("Fruits"))
                        {
                            array_vegitables = array_fruits;
                        }

                    } else {

                        for (int i = 0; i < array_vegitables1.length(); i++) {

                            try {
                                String string = array_vegitables1.getJSONObject(i).getString("name");
                                String str_category = array_vegitables1.getJSONObject(i).getString("category");
                               // str_search_txt = newText;
                                if ((string.toLowerCase()).contains(newText.toLowerCase())) {

                                    if (str_category_select.equalsIgnoreCase("All"))
                                    {
                                        array_vegitables.put(array_vegitables1.getJSONObject(i));
                                    }
                                    if (str_category_select.equalsIgnoreCase("Root"))
                                    {
                                        if (str_category.equalsIgnoreCase("root")) {
                                            array_vegitables.put(array_vegitables1.getJSONObject(i));
                                        }
                                    }
                                    if (str_category_select.equalsIgnoreCase("Ground"))
                                    {
                                        if (str_category.equalsIgnoreCase("ground")) {
                                            array_vegitables.put(array_vegitables1.getJSONObject(i));
                                        }
                                    }
                                    if (str_category_select.equalsIgnoreCase("Leaf")) {
                                        if (str_category.equalsIgnoreCase("leaf")) {
                                            array_vegitables.put(array_vegitables1.getJSONObject(i));
                                        }
                                    }
                                    if (str_category_select.equalsIgnoreCase("Beans")) {
                                        if (str_category.equalsIgnoreCase("beans")) {
                                            array_vegitables.put(array_vegitables1.getJSONObject(i));
                                        }
                                    }
                                    if (str_category_select.equalsIgnoreCase("Fruits"))
                                    {
                                        if (str_category.equalsIgnoreCase("fruit")) {
                                            array_vegitables.put(array_vegitables1.getJSONObject(i));
                                        }
                                    }


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }


                    }
                    if (array_vegitables != null) {

                        adapter = new VenderAdapter(array_vegitables,getActivity());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(adapter);
                    }


                }
                else
                    {
                        adapter = new VenderAdapter(array_vegitables,getActivity());
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(adapter);
                }
                return false;
            }
        });

        relative_vendor_custrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(),RatelistActivity.class);
                startActivity(intent);


            }
        });

        relative_vendortodayprice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                  Intent intent = new Intent(getActivity(),TodaysPricesActivity.class);
                  startActivity(intent);


            }
        });

        relative_vendor_divs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getActivity(),VenderDeliveryActivity.class);
                startActivity(intent);


            }
        });

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(cartupdate,
                new IntentFilter("cartcustomerupdatev"));

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(cartupdate1,
                new IntentFilter("cartcustomerupdate"));

        new Communication_vegitables().execute();





        return view;
    }

    private BroadcastReceiver cartupdate = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            new Communication_vegitables().execute();

        }
    };
    private BroadcastReceiver cartupdate1 = new BroadcastReceiver() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onReceive(Context context, Intent intent)
        {

            new Communication_vegitables().execute();

        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public class Communication_vegitables extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.vegitable);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("type","vendor");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String result)
        {

            progress_homevend.setVisibility(View.INVISIBLE);

            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                } else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("nodata"))
                {

                    text_result_vendor_home.setVisibility(View.VISIBLE);
                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_vegitables = new JSONArray(result);
                            array_vegitables1 = new JSONArray(result);
                            array_fruits = new JSONArray();
                            array_ground = new JSONArray();
                            array_root = new JSONArray();
                            array_leaf = new JSONArray();
                            array_underground = new JSONArray();
                            if (array_vegitables !=null) {

                                text_result_vendor_home.setVisibility(View.INVISIBLE);
                              for (int i = 0;i<array_vegitables1.length() ; i++)
                              {
                                  JSONObject obj_values = new JSONObject(String.valueOf(array_vegitables1.getJSONObject(i)));

                                  String str_category = obj_values.getString("category");
                                  if(str_category.equalsIgnoreCase("root"))
                                  {
                                      array_root.put(obj_values);
                                  }
                                  if(str_category.equalsIgnoreCase("ground"))
                                  {
                                      array_ground.put(obj_values);
                                  }
                                  if(str_category.equalsIgnoreCase("leaf"))
                                  {
                                      array_leaf.put(obj_values);
                                  }
                                  if(str_category.equalsIgnoreCase("beans"))
                                  {
                                      array_underground.put(obj_values);
                                  }
                                  if(str_category.equalsIgnoreCase("fruit"))
                                  {
                                      array_fruits.put(obj_values);
                                  }


                              }

                              if (str_category_select.equalsIgnoreCase("All"))
                              {
                                  array_vegitables = array_vegitables1;
                              }
                                if (str_category_select.equalsIgnoreCase("Root"))
                                {
                                    array_vegitables = array_root;
                                }
                                if (str_category_select.equalsIgnoreCase("Ground"))
                                {
                                    array_vegitables = array_ground;
                                }
                                if (str_category_select.equalsIgnoreCase("Leaf"))
                                {
                                    array_vegitables = array_leaf;
                                }
                                if (str_category_select.equalsIgnoreCase("Beans"))
                                {
                                    array_vegitables = array_underground;
                                }
                                if (str_category_select.equalsIgnoreCase("Fruits"))
                                {
                                    array_vegitables = array_fruits;
                                }

                                adapter = new VenderAdapter(array_vegitables,getActivity());
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(adapter);
                            }
                            else
                            {
                                text_result_vendor_home.setVisibility(View.VISIBLE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {


                    }
                }
            }
            else
            {


            }

        }

        @Override
        public void onCancel(DialogInterface dialogInterface) {
            this.cancel(true);

        }
    }


    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
    private void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

//    public void onBackPressed() { }

}
