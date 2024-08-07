package com.example.ex7.repository.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchRepository {

  Page<Object[]> searchPage(
      String type, String keyword, Pageable pageable);

}
