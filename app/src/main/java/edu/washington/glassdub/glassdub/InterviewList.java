package edu.washington.glassdub.glassdub;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */

/**
 * TODO: Load the list of fetched interview reviews from Kumulos (after the user has searched or after user goes to a job page)
 * TODO: If there are no interviews, make noInterviews textview visible.
 */

public class InterviewList extends Fragment {
    private String[] titles = {"Interview 1", "Interview 2", "Interview 3"};
    private String[] subtitles = {"User 1 - Date 1", "User 2 - Date 2", "User 3 - Date 3"};
    private String[] contents = {"Contents 1", "Contents 2", "Contents 3"};
    private int[] counts = {2, 1, 4};

    private static String TAG = "InterviewList";

    //private ViewPagerAdapter vpAdapter;

    public InterviewList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_interview_list, container, false);

        if ((titles.length == subtitles.length) && (titles.length == contents.length) &&
                (titles.length == counts.length)) {

            // TODO: Populate with data
            CustomAdapter adapter = new CustomAdapter(getActivity(), R.layout.list_item, getData());

            ListView companyReviewList = (ListView) view.findViewById(R.id.interview_listview);
            companyReviewList.setAdapter(adapter);

            companyReviewList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                    Intent intent = new Intent(getActivity(), InterviewPage.class);
                    startActivity(intent);
                }
            });
        } else {
            Log.d(TAG, "arrays don't have same length");

        }
        return view;
    }

    private CustomItem[] getData() {
        CustomItem data[] = new CustomItem[titles.length];

        for (int i = 0; i < titles.length; i++) {
            data[i] = new CustomItem(titles[i], subtitles[i], contents[i], counts[i]);
        }

        return data;
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