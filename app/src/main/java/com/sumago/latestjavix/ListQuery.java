package com.sumago.latestjavix;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListQuery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListQuery extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListQuery() {
        // Required empty public constructor
    }
    String VALUE="";
    public ListQuery(String value) {
        // Required empty public constructor
    VALUE=value;
    }
    ArrayList<String> arrayList=null;
    public ListQuery(ArrayList<String> arrayList) {
        // Required empty public constructor
        this.arrayList=arrayList;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListQuery.
     */
    // TODO: Rename and change types and number of parameters
    public static ListQuery newInstance(String param1, String param2) {
        ListQuery fragment = new ListQuery();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_list_query, container, false);

       String ar[]=new String[arrayList.size()];

       for(int i=0;i<arrayList.size();i++)
       {
           ar[i]=arrayList.get(i);
           //System.out.println("JILANI-"+ar[i]);

       }
        System.out.println("JILANI-"+ar.length);
       String[] art={"Rahul","Jilani","Rakesh"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(root.getContext(),R.layout.querylist_row, ar);

        ListView listView = (ListView)root.findViewById(R.id.queryListView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView=(TextView)view;
                String val=textView.getText().toString();

                Toast.makeText(MyConfig.CONTEXT,val+" : "+QueryActivity.QI,Toast.LENGTH_SHORT).show();
                if(QueryActivity.QI==1)
                {
                    QueryActivity.VAL=val;
                    QueryActivity.VALI="";
                    ArrayList<QuestionaryY> reci=QuestionaryX.quest.get(val);
                    int size=reci.size();
                    Toast.makeText(MyConfig.CONTEXT,"SIZE "+size,Toast.LENGTH_SHORT).show();
                    ArrayList<String> recii=new ArrayList<String>();
                    for(int ri=0;ri<size;ri++)
                    {
                        QuestionaryY questionaryY=reci.get(ri);
                        recii.add(questionaryY.childText);

                    }
                    QueryActivity.QI=2;
                    QueryActivity.arrayListStatic=recii;


                    Intent intent=new Intent(getActivity(),QueryActivity.class);
                    getActivity().startActivity(intent);
                    getActivity().finish();

                }
                if(QueryActivity.QI==2)
                {
                    //QueryActivity.VAL=QueryActivity.VAL;
                   // QueryActivity.VALI=val;

                    ArrayList<QuestionaryY> reci=QuestionaryX.quest.get(val);
                    int size=reci.size();
                    ArrayList<String> recii=new ArrayList<String>();
                    for(int ri=0;ri<size;ri++)
                    {
                        QuestionaryY questionaryY=reci.get(ri);
                        if(questionaryY.childText.equals(QueryActivity.VALI)) {
                            recii=questionaryY.hintList;
                            break;
                        }
                    }
                    QueryActivity.QI=3;
                    QueryActivity.arrayListStatic=recii;
                    Intent intent=new Intent(getActivity(),QueryActivity.class);
                    getActivity().startActivity(intent);

                }
            }
        });


        return root;
    }

}