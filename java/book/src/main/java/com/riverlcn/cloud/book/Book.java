package com.riverlcn.cloud.book;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author river
 */
@Data
@AllArgsConstructor
public class Book implements Serializable {
    private Long id;
    private String author;
    private String title;
}
