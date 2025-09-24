package com.data.dto.request;

import com.data.enums.NewsCategory;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewsRequestDTO {
    String title;
    String content;
    Long postedById;
    NewsCategory newsCategory;
    String imageUrl;
}
