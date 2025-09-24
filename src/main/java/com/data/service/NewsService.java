package com.data.service;

import com.data.dto.request.NewsRequestDTO;
import com.data.dto.response.NewsResponseDTO;
import com.data.dto.response.PageDTO;
import com.data.dto.response.UserResponseDTO;
import com.data.entity.News;
import com.data.entity.User;
import com.data.enums.NewsCategory;
import com.data.repository.NewsRepository;
import com.data.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NewsService {
    NewsRepository newsRepository;
    UserRepository userRepository;

    public List<NewsResponseDTO> getAllNews() {
        List<News> newsList = newsRepository.findAll();
        List<NewsResponseDTO> result = new ArrayList<>();
        for (News news : newsList) {
            result.add(convertToDTO(news));
        }
        return result;
    }

    public NewsResponseDTO getNewsById(Long id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isPresent()) {
            return convertToDTO(optionalNews.get());
        }
        return null;
    }

    public PageDTO<NewsResponseDTO> searchNews(Pageable pageable, String title, String poster) {
        Page<News> page = newsRepository.searchNews(title, poster, pageable);
        return PageDTO.of(page.map(this::convertToDTO));
    }

    public NewsResponseDTO createNews(NewsRequestDTO request) {
        User poster = userRepository.findById(request.getPostedById())
                .orElseThrow(() -> new RuntimeException("Poster not found"));

        News news = new News();
        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setPostedBy(poster);
        news.setPostedAt(LocalDateTime.now());
        news.setNewsCategory(request.getNewsCategory());

        return convertToDTO(newsRepository.save(news));
    }

    public NewsResponseDTO updateNews(Long id, NewsRequestDTO request) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found"));

        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setNewsCategory(request.getNewsCategory());

        if (request.getPostedById() != null) {
            User poster = userRepository.findById(request.getPostedById())
                    .orElseThrow(() -> new RuntimeException("Poster not found"));
            news.setPostedBy(poster);
        }

        return convertToDTO(newsRepository.save(news));
    }

    public void deleteNews(Long id) {
        if (!newsRepository.existsById(id)) {
            throw new RuntimeException("News not found");
        }
        newsRepository.deleteById(id);
    }

    private NewsResponseDTO convertToDTO(News news) {
        NewsResponseDTO dto = new NewsResponseDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setContent(news.getContent());
        dto.setPostedAt(news.getPostedAt().toString());
        dto.setPostedBy(new UserResponseDTO(news.getPostedBy()));
        dto.setNewsCategory(news.getNewsCategory());
        dto.setImageUrl(news.getImageUrl());
        return dto;
    }

    public List<NewsResponseDTO> getNewestNews() {
        return newsRepository.findAllByOrderByPostedAtDesc()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<NewsResponseDTO> getNewsByCategory(NewsCategory newsCategory) {
        return newsRepository.findByNewsCategoryOrderByPostedAtDesc(newsCategory)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
}