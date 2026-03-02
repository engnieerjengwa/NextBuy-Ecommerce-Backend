package com.ecommerce.NexBuy.dto.response;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WishlistResponseDto {

    private Long id;
    private String name;
    private LocalDateTime dateCreated;
    private List<WishlistItemResponseDto> items;
}
