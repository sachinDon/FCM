package info.wkweb.com.vmart;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Debug;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

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
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.HttpsURLConnection;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import me.relex.circleindicator.CircleIndicator;

import static android.content.Context.MODE_PRIVATE;
import static android.text.InputType.TYPE_NULL;

public class CustomerFragment extends Fragment {


Context context;
    RelativeLayout relatives_location,relative_cust_home_price,relative_customer_aboutus;
    SearchView searchbar;
    ScrollView scroll_cust_homes;
    TextView cust_home_textviewserView,text_contactus_text_eid1,text_contactus_text_eid2,text_contactus_text_eid3
,text_franchise_text_eid1,text_result_cust_home,text_carrer_text_eid1;
    ProgressBar progress_customer;
    JSONArray array_images;
JSONArray jsonArr_brandimages;
    JSONArray jsonArr_ofsimages;
    JSONArray jsonArr_bannerimages;
    JSONArray jsonArr_sf;
     ViewPager viewPager, viewPager_brand, viewPager_ofs, viewPager_sf;
    CircleIndicator indicator,indicator_brand,indicator_ofs,indicator_sf;
    int currentPage = 0,currentPage_brans = 0, currentPage_ofs = 0,currentPage_sf = 0;

    Timer timer,timer_brand,timer_ofs,timer_sf;

    public SharedPreferences pref;
    SharedPreferences.Editor editor;


    public CustomerFragment() {
        // Required empty public constructor







    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        pref = getActivity().getSharedPreferences("MyPref", MODE_PRIVATE);
        editor = pref.edit();
        relatives_location = (RelativeLayout) view.findViewById(R.id.relatives_location);
        relative_customer_aboutus = (RelativeLayout) view.findViewById(R.id.relative_customer_aboutus);
        relative_cust_home_price = (RelativeLayout) view.findViewById(R.id.relative_cust_home_price);
        searchbar = (SearchView) view.findViewById(R.id.cust_home_searchView);
        cust_home_textviewserView = (TextView) view.findViewById(R.id.cust_home_textviewserView);
        progress_customer=(ProgressBar)view.findViewById(R.id.progress_customer_home);
        viewPager = (ViewPager) view.findViewById(R.id.viewPager_banner);
        indicator = (CircleIndicator) view.findViewById(R.id.indicator_banner);

        viewPager_brand = (ViewPager) view.findViewById(R.id.viewPager_brand);
        indicator_brand = (CircleIndicator) view.findViewById(R.id.indicator_brand);

        viewPager_ofs = (ViewPager) view.findViewById(R.id.viewPager_our_fm);
        indicator_ofs = (CircleIndicator) view.findViewById(R.id.indicator_our_fm);

        viewPager_sf = (ViewPager) view.findViewById(R.id.viewPager_source_fm);
        indicator_sf = (CircleIndicator) view.findViewById(R.id.indicator_source_fm);

        text_contactus_text_eid1 = (TextView) view.findViewById(R.id.text_contactus_text_eid1);
        text_contactus_text_eid2 = (TextView) view.findViewById(R.id.text_contactus_text_eid2);
        text_contactus_text_eid3 = (TextView) view.findViewById(R.id.text_contactus_text_eid3);
        text_franchise_text_eid1 = (TextView) view.findViewById(R.id.text_franchise_text_eid1);
        text_result_cust_home = (TextView) view.findViewById(R.id.text_result_cust_home);
        text_carrer_text_eid1 = (TextView) view.findViewById(R.id.text_carrer_text_eid1);
        scroll_cust_homes= (ScrollView) view.findViewById(R.id.scroll_cust_homes);

        scroll_cust_homes.setVisibility(View.INVISIBLE);
        text_result_cust_home.setVisibility(View.INVISIBLE);
        SpannableString content = new SpannableString("info@fairpricemandi.com");
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        text_contactus_text_eid1.setText(content);
        SpannableString content1 = new SpannableString("sales@fairpricemandi.com");
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        text_contactus_text_eid2.setText(content1);
        SpannableString content2 = new SpannableString("fairpricemandi@gmail.com");
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
        text_contactus_text_eid3.setText(content2);

        SpannableString content3 = new SpannableString("info@fairpricemandi.com");
        content3.setSpan(new UnderlineSpan(), 0, content3.length(), 0);
        text_franchise_text_eid1.setText(content3);

//        SpannableString content4 = new SpannableString("fairpricemandi@gmail.com");
//        content4.setSpan(new UnderlineSpan(), 0, content4.length(), 0);
//        text_franchise_text_eid2.setText(content4);

        SpannableString content5 = new SpannableString("fairpricemandi@gmail.com");
        content5.setSpan(new UnderlineSpan(), 0, content5.length(), 0);
        text_carrer_text_eid1.setText(content2);

        array_images =new JSONArray();
        jsonArr_brandimages =new JSONArray();
        jsonArr_ofsimages =new JSONArray();
        jsonArr_bannerimages =new JSONArray();
        jsonArr_sf =new JSONArray();



        int id = searchbar.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        EditText textView = (EditText) searchbar.findViewById(id);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.WHITE);
        textView.clearFocus();
        textView.setEnabled(false);
//        textView.setFocusableInTouchMode(true);
//        textView.setInputType(TYPE_NULL);
        relative_cust_home_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intents = new Intent(getActivity(),CustomersActivity.class);
                startActivity(intents);
            }
        });
        cust_home_textviewserView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intents = new Intent(getActivity(),CustomersActivity.class);
                startActivity(intents);
            }
        });

        relatives_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationsActivity.str_viewact = "loc";
                Intent intents = new Intent(getActivity(),LocationsActivity.class);
                startActivity(intents);
            }
        });
        relative_customer_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intens = new Intent(getActivity(),AboutUsActivity.class);
                startActivity(intens);
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

                Log.d("State change","State");
            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                currentPage = position;
                Log.d("onPageScrolled ","onPageScrolled");
            }

            public void onPageSelected(int position) {
                Log.d("onPageSelected ","onPageSelected");
            }
        });

        viewPager_brand.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {


            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                currentPage_brans = position;

            }

            public void onPageSelected(int position) {

            }
        });

        viewPager_ofs.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            public void onPageScrollStateChanged(int state) {


            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                currentPage_ofs = position;

            }

            public void onPageSelected(int position) {

            }
        });

        viewPager_sf.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {


            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
                currentPage_sf = position;
            }

            public void onPageSelected(int position) {

            }
        });

        new Communication_banner().execute();



        return view;
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

    public class ViewPagerAdapter extends PagerAdapter {

        private Context context;
        private LayoutInflater layoutInflater;

        public ViewPagerAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return jsonArr_bannerimages.length();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.list_images_slides, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            ImageView image_slide_play = (ImageView) view.findViewById(R.id.image_slide_play);
            image_slide_play.setVisibility(View.INVISIBLE);


            try {
                String str_image = String.valueOf(jsonArr_bannerimages.get(position));


                Picasso.with(getActivity())
                        .load(str_image)
                        .placeholder(R.drawable.default1)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

        }
    }

    public class ViewPagerAdapter_brand extends PagerAdapter {

        private Context context;
        private LayoutInflater layoutInflater;

        public ViewPagerAdapter_brand(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return jsonArr_brandimages.length();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.list_images_slides, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            ImageView image_slide_play = (ImageView) view.findViewById(R.id.image_slide_play);
            image_slide_play.setVisibility(View.INVISIBLE);
            try {
                String str_image = String.valueOf(jsonArr_brandimages.get(position));


                Picasso.with(getActivity())
                        .load(str_image)
                        .placeholder(R.drawable.default1)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

        }
    }

    public class ViewPagerAdapter_ofs extends PagerAdapter {

        private Context context;
        private LayoutInflater layoutInflater;

        public ViewPagerAdapter_ofs(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return jsonArr_ofsimages.length();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.list_images_slides, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            ImageView image_slide_play = (ImageView) view.findViewById(R.id.image_slide_play);
            image_slide_play.setVisibility(View.VISIBLE);
            RelativeLayout relative_slide_images = (RelativeLayout) view.findViewById(R.id.relative_slide_images);

            try {
                String str_image = String.valueOf(jsonArr_ofsimages.get(position));

                relative_slide_images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=NxCKhpzKl18"));
                        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/watch?v=NxCKhpzKl18"));
                        try {
                            context.startActivity(appIntent);
                        } catch (ActivityNotFoundException ex) {
                            context.startActivity(webIntent);
                        }

                    }
                });
                Picasso.with(getActivity())
                        .load(str_image)
                        .placeholder(R.drawable.default1)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

        }
    }

    public class ViewPagerAdapter_sf extends PagerAdapter {

        private Context context;
        private LayoutInflater layoutInflater;

        public ViewPagerAdapter_sf(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return jsonArr_sf.length();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.list_images_slides, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            ImageView image_slide_play = (ImageView) view.findViewById(R.id.image_slide_play);
            image_slide_play.setVisibility(View.INVISIBLE);
            try {
                String str_image = String.valueOf(jsonArr_sf.get(position));


                Picasso.with(getActivity())
                        .load(str_image)
                        .placeholder(R.drawable.default1)
                        .into(imageView, new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError() {

                            }
                        });
            } catch (JSONException e) {
                e.printStackTrace();
            }


            ViewPager vp = (ViewPager) container;
            vp.addView(view, 0);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            ViewPager vp = (ViewPager) container;
            View view = (View) object;
            vp.removeView(view);

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
    public class Communication_banner extends AsyncTask<String, Void, String> implements DialogInterface.OnCancelListener {

        protected void onPreExecute() {

            super.onPreExecute();
        }

        protected String doInBackground(String... arg0) {



            try {

                URL url = new URL(Urlclass.banner);

                JSONObject postDataParams = new JSONObject();

                postDataParams.put("email", pref.getString("userid",""));
                postDataParams.put("type","customer");

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

            progress_customer.setVisibility(View.INVISIBLE);
            if (result !=null)
            {

                if (result.equalsIgnoreCase("nullerror"))

                {



                }
                else if (result.equalsIgnoreCase("error"))
                {


                }
                else if (result.equalsIgnoreCase("nodata"))
                {
                scroll_cust_homes.setVisibility(View.INVISIBLE);
                text_result_cust_home.setVisibility(View.VISIBLE);

                }
                else
                {
                    if (!result.equalsIgnoreCase("") && result.length()>=50) {
                        try {
                            array_images = new JSONArray(result);


                           if (array_images.length() !=0)
                           {
                               scroll_cust_homes.setVisibility(View.VISIBLE);
                               text_result_cust_home.setVisibility(View.INVISIBLE);

                               JSONObject object = new JSONObject(String.valueOf(array_images.getJSONObject(0)));

                               jsonArr_brandimages = new JSONArray(object.getString("brand"));
                               jsonArr_ofsimages = new JSONArray(object.getString("ofs"));
                               jsonArr_bannerimages = new JSONArray(object.getString("banner"));
                               jsonArr_sf = new JSONArray(object.getString("sf"));

                               if (jsonArr_bannerimages.length() != 0)
                               {
                                   ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity());
                                   viewPager.setAdapter(viewPagerAdapter);
                                   indicator.setViewPager(viewPager);

                                   final Handler handler = new Handler();

                                   final Runnable update = new Runnable() {
                                       public void run() {

                                           viewPager.setCurrentItem(currentPage, true);
                                           if (currentPage == jsonArr_bannerimages.length() - 1) {
                                               currentPage = 0;
                                           } else {
                                               ++currentPage;
                                           }
                                       }
                                   };


                                   timer = new Timer();
                                   timer.schedule(new TimerTask() {

                                       @Override
                                       public void run() {
                                           handler.post(update);
                                       }
                                   }, 300, 3500);
                               }

                               if (jsonArr_brandimages.length() != 0)
                               {
                                   ViewPagerAdapter_brand viewPagerAdapter = new ViewPagerAdapter_brand(getActivity());
                                   viewPager_brand.setAdapter(viewPagerAdapter);
                                   indicator_brand.setViewPager(viewPager_brand);

                                   final Handler handler = new Handler();

                                   final Runnable update = new Runnable() {
                                       public void run() {

                                           viewPager_brand.setCurrentItem(currentPage_brans, true);
                                           if (currentPage_brans == jsonArr_brandimages.length() - 1) {
                                               currentPage_brans = 0;
                                           } else {
                                               ++currentPage_brans;
                                           }
                                       }
                                   };


                                   timer_brand = new Timer();
                                   timer_brand.schedule(new TimerTask() {

                                       @Override
                                       public void run() {
                                           handler.post(update);
                                       }
                                   }, 300, 3500);
                               }



                               if (jsonArr_ofsimages.length() != 0)
                               {
                                   ViewPagerAdapter_ofs viewPagerAdapter = new ViewPagerAdapter_ofs(getActivity());
                                   viewPager_ofs.setAdapter(viewPagerAdapter);
                                   indicator_ofs.setViewPager(viewPager_ofs);

                                   final Handler handler = new Handler();

                                   final Runnable update = new Runnable() {
                                       public void run() {

                                           viewPager_ofs.setCurrentItem(currentPage_ofs, true);
                                           if (currentPage_ofs == jsonArr_ofsimages.length() - 1) {
                                               currentPage_ofs= 0;
                                           } else {
                                               ++currentPage_ofs;
                                           }
                                       }
                                   };


                                   timer_ofs = new Timer();
                                   timer_ofs.schedule(new TimerTask() {

                                       @Override
                                       public void run() {
                                           handler.post(update);
                                       }
                                   }, 300, 3500);
                               }


                               if (jsonArr_sf.length() != 0)
                               {
                                   ViewPagerAdapter_sf viewPagerAdapter = new ViewPagerAdapter_sf(getActivity());
                                   viewPager_sf.setAdapter(viewPagerAdapter);
                                   indicator_sf.setViewPager(viewPager_sf);

                                   final Handler handler = new Handler();

                                   final Runnable update = new Runnable() {
                                       public void run() {

                                           viewPager_sf.setCurrentItem(currentPage_sf, true);
                                           if (currentPage_sf == jsonArr_sf.length() - 1) {
                                               currentPage_sf = 0;
                                           } else {
                                               ++currentPage_sf;
                                           }
                                       }
                                   };


                                   timer_sf = new Timer();
                                   timer_sf.schedule(new TimerTask() {

                                       @Override
                                       public void run() {
                                           handler.post(update);
                                       }
                                   }, 300, 3500);
                               }

                           }
                           else
                           {
                               scroll_cust_homes.setVisibility(View.INVISIBLE);
                               text_result_cust_home.setVisibility(View.VISIBLE);
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

//    public void onBackPressed() {
//
//    }
}
