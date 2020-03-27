package com.sumin.mymovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sumin.mymovies.adapters.MovieAdapter;
import com.sumin.mymovies.data.MainViewModel;
import com.sumin.mymovies.data.Movie;
import com.sumin.mymovies.utils.JSONUtils;
import com.sumin.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

//если искать непопулярный фильм, то там может не быть постера, лучше задать запрос nemo например
public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSearch;
    private Button buttonSearch;
    private EditText editTextSearch;
    private MainViewModel mainViewModel;
    private MovieAdapter adapter;
    private Movie movie;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //переход из меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavourite:
                Intent intentToFavourite = new Intent(this, FavouriteActivity.class);
                startActivity(intentToFavourite);
                break;
            case R.id.itemSearch:
                Intent intentToSeatch = new Intent(this,SearchActivity.class);
                startActivity(intentToSeatch);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //поиск работает только с интернетом и по одному слову, пробел не работает, и я не понимаю почему
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        recyclerViewSearch = findViewById(R.id.recyclerViewSearch);
        buttonSearch = findViewById(R.id.buttonSearch);
        editTextSearch = findViewById(R.id.editTextSearch);
        recyclerViewSearch.setLayoutManager(new GridLayoutManager(this, 2));
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        adapter = new MovieAdapter();
        recyclerViewSearch.setAdapter(adapter);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"qwe",Toast.LENGTH_SHORT);
                if(editTextSearch.getText() != null){
                    JSONObject jsonObjectSearch = NetworkUtils.getJSONForSearch(editTextSearch.getText().toString());
                    ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObjectSearch);
                    adapter.setMovies(movies);
                    adapter.setOnPosterClickListener(new MovieAdapter.OnPosterClickListener() {
                        @Override
                        public void onPosterClick(int position) {
                            Movie movie = adapter.getMovies().get(position);
                            Intent intent = new Intent(SearchActivity.this, DetailActivity.class);
                            intent.putExtra("id", movie.getId());
                            startActivity(intent);
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(), "search field is empty", Toast.LENGTH_SHORT);
                }
            }
        });

    }

}
