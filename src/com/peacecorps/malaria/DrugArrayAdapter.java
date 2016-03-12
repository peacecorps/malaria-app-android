package com.peacecorps.malaria;

/**
 * Created by Ankita on 8/13/2015.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

public class DrugArrayAdapter extends ArrayAdapter<String>{
    /**Customized List Adapter with an Image View on Left and Text View on Right**/
    private final Activity context;
    private final String[] dname;
    private final Integer[] imageId;
    private final String[] drugDescriptions;
    public DrugArrayAdapter(Activity context,
                      String[] dname, Integer[] imageId, String[] drugDescriptions) {
        super(context, R.layout.trip_drug_item, dname);
        this.context = context;
        this.dname = dname;
        this.imageId = imageId;
        this.drugDescriptions = drugDescriptions;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //inflating the customized view
        View rowView= inflater.inflate(R.layout.trip_drug_item, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.drugItem);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imgDrug);
        txtTitle.setText(dname[position]);

        imageView.setImageResource(imageId[position]);
        return rowView;
    }

    @Override
    public View getDropDownView(int position, View View, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        //inflating the customized view
        View rowView= inflater.inflate(R.layout.trip_drug_info, null, true);
        final TextView txtTitle = (TextView) rowView.findViewById(R.id.nameDrug);
        Button infoButton = (Button) rowView.findViewById(R.id.infoDrug);

        txtTitle.setText(dname[position]);
        addClickListener(position, infoButton);

        return rowView;
    }
    //When the info button is clicked for the drug, an alert dialog will show its description
    public void addClickListener(final int position, Button infoButton) {
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] layoutid={R.layout.malarone,R.layout.doxycycline,R.layout.mefloquine};
                int[] textViewid={R.id.textViewmal,R.id.textViewdoxy,R.id.textViewmef};
                String[] linkadd={"http://www.drugs.com/malarone.html","http://www.drugs.com/doxycycline.html","http://www.drugs.com/cdi/mefloquine.html"};
                AlertDialog.Builder builder  = new AlertDialog.Builder(context);





                LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(layoutid[position], null);

                TextView textView = (TextView) layout.findViewById(textViewid[position]);
                String text=textView.getText().toString();
                String add=linkadd[position];
                int start = text.indexOf(linkadd[position]);
                int end = start + add.length();

                SpannableString spannableString = new SpannableString(text);

                if(position==0)
                {
                    spannableString.setSpan(new GoToMal(), start, end, 0);
                }
                else
                if(position==1)
                {
                    spannableString.setSpan(new GoToDoxy(), start, end, 0);
                }
                else
                {
                    spannableString.setSpan(new GoToMef(), start, end, 0);
                }
                textView.setText(spannableString);

                textView.setMovementMethod(new LinkMovementMethod());


                builder.setView(layout);
                builder.setCancelable(true);



                builder.create().show();
            }
        });
    }
    private static class GoToMal extends ClickableSpan {
        @Override
        public void onClick(View view) {



            String url = "http://www.drugs.com/malarone.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));

            view.getContext().startActivity(i);
        }
    }

    private static class GoToDoxy extends ClickableSpan {
        @Override
        public void onClick(View view) {



            String url = "http://www.drugs.com/doxycycline.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));

            view.getContext().startActivity(i);
        }
    }


    private static class GoToMef extends ClickableSpan {
        @Override
        public void onClick(View view) {



            String url = "http://www.drugs.com/cdi/mefloquine.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));

            view.getContext().startActivity(i);
        }
    }

}


