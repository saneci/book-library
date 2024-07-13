package ru.saneci.booklibrary.business.util;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.IntStream;

public final class TemplateUtil {

    private TemplateUtil() {
    }

    /**
     * Required for pagination block.
     * Generates a sequential ordered List of int elements by an incremental step of 1 (list size = 4).
     */
    public static <T> List<Integer> generatePageNumbers(Page<T> page) {
        List<Integer> pageNumbers;
        int currentPageNumber = page.getNumber();
        int lastPageNumber = page.getTotalPages() - 1;

        if (lastPageNumber < 4) {
            pageNumbers = range(0, lastPageNumber);
        } else if (currentPageNumber < 2) {
            pageNumbers = range(0, 3);
        } else if (currentPageNumber < lastPageNumber - 1) {
            pageNumbers = range(currentPageNumber - 1, currentPageNumber + 2);
        } else {
            pageNumbers = range(currentPageNumber - 1, lastPageNumber);
        }

        return pageNumbers;
    }

    private static List<Integer> range(int startInclusive, int endInclusive) {
        return IntStream.rangeClosed(startInclusive, endInclusive).boxed().toList();
    }
}
