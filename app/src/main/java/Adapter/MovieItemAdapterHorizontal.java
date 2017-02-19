package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.d2q.demo.assignment1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Model.Movie;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by Tuan on 2017/02/19.
 */
public class MovieItemAdapterHorizontal extends ArrayAdapter<Movie> {


    public MovieItemAdapterHorizontal(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        Movie movie = getItem(position);
        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.movie_item_adapter_horizontal,null);
            ViewHolder vh = new ViewHolder();
            vh.poster = (ImageView) v.findViewById(R.id.moviePoster);
            vh.title = (TextView) v.findViewById(R.id.txtTitle);
            vh.description = (TextView) v.findViewById(R.id.txtDescription);
            v.setTag(vh);
//            if (position == 0){
//                v = vi.inflate(R.layout.list_adapter_big, null);
//            } else {
//                v = vi.inflate(R.layout.list_adapter, null);
//            }
            setContentToViewHolder(vh, movie);
        } else {
            ViewHolder vh = (ViewHolder) v.getTag();
            setContentToViewHolder(vh, movie);
        }
        return v;
    }
    private void setContentToViewHolder(ViewHolder vh, Movie movie){
        setImageToImageViewFromPath(vh.poster,movie.getBackdrop_path());
        vh.title.setText(movie.getTitle());
        vh.description.setText(movie.getOverview());
    }
    private void setImageToImageViewFromPath(ImageView iv, String path) {
        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w500"+path).transform(new RoundedCornersTransformation(10, 10)).placeholder(R.drawable.backdrop_placholder).into(iv);
    }
    private class ViewHolder{
        public ImageView poster;
        public TextView title;
        public TextView description;
    }
}
