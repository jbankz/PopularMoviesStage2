package bankzworld.com.db;

import android.provider.BaseColumns;

/**
 * Created by jaycee on 8/11/2017.
 */

public class FavContract {

    public static final class FavEntry implements BaseColumns {

        public static final String FAVOURITE_TABLE_NAME = "favorites";

        public static final String FAVOURITE_ID = "id";

        public static final String FAVOURITE_TITLE = "title";

        public static final String FAVOURITE_POSTER_PATH= "poster_path";

        public static final String FAVOURITE_BACKDROP_PATH = "backdrop_path";

        public static final String FAVOURITE_OVERVIEW = "overview";

        public static final String FAVOURITE_RELEASE_DATE = "release_date";

        public static final String FAVOURITE_VOTE_AVERAGE = "vote_average";


    }
}
