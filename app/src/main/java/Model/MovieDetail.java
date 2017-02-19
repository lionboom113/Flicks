package Model;

import java.util.List;

/**
 * Created by Tuan on 2017/02/19.
 */
public class MovieDetail extends Movie{
    private long budget;
    private List<Genre> genres;
    private int vote_count;

    public long getBudget() {
        return budget;
    }

    public void setBudget(long budget) {
        this.budget = budget;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }
}
