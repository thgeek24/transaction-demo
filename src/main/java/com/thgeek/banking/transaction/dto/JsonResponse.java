package com.thgeek.banking.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * General JSON response wrapper
 *
 * @author Tao Hong
 * @version 1.0
 * @since 2025/03/19 15:30
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Status code: 0 indicates success, -1 indicates failure
     */
    private Integer code;

    /**
     * Response message
     */
    private String msg;

    /**
     * Business data wrapper
     */
    private Data<T> content;

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Data<T> implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /**
         * Total number of data
         */
        private Long count;

        /**
         * Current query count
         */
        private Long limit;

        /**
         * Data list
         */
        private List<T> data;
    }

    /**
     * Build a successful response
     */
    public static <T> JsonResponse<T> success() {
        return JsonResponse.<T>builder()
                .code(0)
                .msg("Operation successful")
                .content(new Data<>(0L, 0L, Collections.emptyList()))
                .build();
    }

    /**
     * Build a successful response with a single data item
     */
    public static <T> JsonResponse<T> successOne(T item) {
        return item != null ? JsonResponse.<T>builder()
                .code(0)
                .msg("Operation successful")
                .content(new Data<>(1L, 1L, Collections.singletonList(item)))
                .build()
                : error("No data");
    }

    /**
     * Build a successful response with paginated data items.
     *
     * @param page the page containing the data items
     * @param <T>  the type of the data items
     * @return a JsonResult containing the paginated data items
     */
    public static <T> JsonResponse<T> successPage(Page<T> page) {
        long count = page.getTotalElements();
        long limit = page.getContent().size();
        return JsonResponse.<T>builder()
                .code(0)
                .msg("Operation successful")
                .content(new Data<>(count, limit, page.getContent()))
                .build();
    }

    /**
     * Build a successful response with multiple data items
     */
    public static <T> JsonResponse<T> successMany(List<T> items) {
        long size = Optional.ofNullable(items).map(List::size).orElse(0);
        return JsonResponse.<T>builder()
                .code(0)
                .msg("Operation successful")
                .content(new Data<>(size, size, items != null ? items : Collections.emptyList()))
                .build();
    }

    /**
     * Build an error response with message
     */
    public static <T> JsonResponse<T> error(String msg) {
        return JsonResponse.<T>builder()
                .code(-1)
                .msg(msg)
                .content(new Data<>())
                .build();
    }

    /**
     * Build an error response with code and message
     */
    public static <T> JsonResponse<T> error(Integer code, String msg) {
        return JsonResponse.<T>builder()
                .code(code)
                .msg(msg)
                .content(new Data<>())
                .build();
    }
}