package edu.washington.glassdub.glassdub;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.kumulos.android.Kumulos;
import com.kumulos.android.ResponseHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


/**
 * A simple {@link Fragment} subclass.
 */

public class ReviewList extends Fragment {
    private String[] titles = {"Review 1", "Review 2", "Review 3"};
    private String[] subtitles = {"User 1 - Date 1", "User 2 - Date 2", "User 3 - Date 3"};
    private String[] contents = {"Contents 1", "Contents 2", "Contents 3"};
    private int[] counts = {2, 1, 4};

    private static String TAG = "ReviewList";
    private TextView noReviews;

    //private ViewPagerAdapter vpAdapter;

    public ReviewList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_review_list, container, false);

        noReviews = (TextView) view.findViewById(R.id.noReviews);

        final String company = getArguments().getString("company");


        Map<String, String> reviewParam = new HashMap<>();
//        reviewParam.put("companyID", companyID);

        String api_function;
        if (getArguments().getString("companyID") != null ) {
            reviewParam.put("companyID", getArguments().getString("companyID"));
            api_function = "getReviewsForCompany";
        } else {
            reviewParam.put("job", getArguments().getString("job"));
            api_function = "getReviewsForJob";
        }

        Kumulos.call(api_function, reviewParam, new ResponseHandler() {

            @Override
            public void didCompleteWithResult(Object result) {
                if (result.toString().equals("32") || result.toString().equals("64") || result.toString().equals("128")) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            getActivity());
                    alertDialogBuilder
                            .setTitle("Error")
                            .setMessage("We were unable to fetch the reviews. Please try again.")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    final ArrayList<LinkedHashMap<String, Object>> objects = (ArrayList<LinkedHashMap<String, Object>>) result;
                    if(objects.size() == 0) {
                        noReviews.setVisibility(VISIBLE);
                    } else {
                        noReviews.setVisibility(INVISIBLE);
                        CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.list_item, getData(objects));

                        ListView companyReviewList = (ListView) view.findViewById(R.id.review_listview);
                        companyReviewList.setAdapter(adapter);

                        companyReviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                                Intent intent = new Intent(getActivity(), ReviewPage.class);
                                Log.d(TAG, "object: " + objects.get(position).toString());
                                intent.putExtra("reviewID", objects.get(position).get("job_reviewID").toString());
                                intent.putExtra("company", company);
                                startActivity(intent);
                            }
                        });
                    }
                }
            }
        });

        return view;
    }

    private CustomItem[] getData(ArrayList<LinkedHashMap<String, Object>> objects) {
        CustomItem data[] = new CustomItem[objects.size()];

        for (int i = 0; i < objects.size(); i++) {
            LinkedHashMap<String, Object> obj = objects.get(i);
            data[i] = new CustomItem(obj.get("title").toString(), formatDate(obj.get("timeCreated").toString()).toString(), obj.get("body").toString(), Integer.valueOf(obj.get("rating").toString()));
        }

        return data;
    }

    private String formatDate(String original) {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);

        Date date = null;
        String result = "";

        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse(original);
            result = format.format(date);
        } catch (ParseException e) {
            e.getStackTrace();
        }
        Log.d(TAG, "original: " + original + " new: " + result);
        return result;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    public void setUsers(String[] users) {
        this.subtitles = users;
    }

    public void setReviews(String[] reviews) {
        this.contents = reviews;
    }

    public void setCounts(int[] counts) {
        this.counts = counts;
    }

}