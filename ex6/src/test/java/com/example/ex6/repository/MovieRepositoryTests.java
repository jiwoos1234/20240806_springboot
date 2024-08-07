package com.example.ex6.repository;

import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieRepositoryTests {
  @Autowired
  MovieRepository movieRepository;

  @Autowired
  MovieImageRepository movieImageRepository;

  @Transactional
  @Commit
  @Test
  public void insertMovies() {
    IntStream.rangeClosed(1, 100).forEach(i -> {
      Movie movie = Movie.builder().title("Movie..." + i).build();
      movieRepository.save(movie);
      System.out.println("----------------------------");
      int cnt = (int) (Math.random() * 5) + 1;
      for (int j = 0; j < cnt; j++) {
        MovieImage movieImage = MovieImage.builder()
            .uuid(UUID.randomUUID().toString())
            .movie(movie)
            .imgName("test" + j + ".jpg")
            .build();
        movieImageRepository.save(movieImage);
      }
    });
  }

  @Test
  public void testListPage() {
    PageRequest pageRequest =
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
    Page<Object[]> result = movieRepository.getListPage(pageRequest);
    for (Object[] objArr : result.getContent()) {
      System.out.println(Arrays.toString(objArr));
    }
  }
  @Test
  public void testListPageImg() {
    PageRequest pageRequest =
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
    Page<Object[]> result = movieRepository.getListPageImg(pageRequest);
    for (Object[] objArr : result.getContent()) {
      System.out.println(Arrays.toString(objArr));
    }
  }
  @Test
  public void testGetListPageImgNative() {
    PageRequest pageRequest =
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "movie_mno"));
    Page<Object[]> result = movieRepository.getListPageImgNative(pageRequest);
    for (Object[] objArr : result.getContent()) {
      System.out.println(Arrays.toString(objArr));
    }
  }

  @Test
  public void testGetListPageImgJPQL() {
    PageRequest pageRequest =
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "mno"));
    Page<Object[]> result = movieRepository.getListPageImgJPQL(pageRequest);
    for (Object[] objArr : result.getContent()) {
      System.out.println(Arrays.toString(objArr));
    }
  }

  @Test
  public void testGetMaxQuery() {
    PageRequest pageRequest =
        PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "movie"));
    Page<Object[]> result = movieRepository.getMaxQuery(pageRequest);
    for (Object[] objArr : result.getContent()) {
      System.out.println(Arrays.toString(objArr));
    }
  }

  @Test
  public void testGetMovieWithAll() {
    List<Object[]> result = movieRepository.getMovieWithAll(100L);
    for (Object[] arr : result) {
      System.out.println(Arrays.toString(arr));
    }
  }
}
