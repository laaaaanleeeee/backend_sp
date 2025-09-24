package com.data.entity;

import com.data.enums.NewsCategory;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "news")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String content;

    @Column(nullable = false, name = "posted_at")
    LocalDateTime postedAt;

    @ManyToOne
    @JoinColumn(name = "posted_by", nullable = false)
    User postedBy;

    @Enumerated(EnumType.STRING)
    NewsCategory newsCategory;

    @Column(name = "image_url")
    String imageUrl;
}
