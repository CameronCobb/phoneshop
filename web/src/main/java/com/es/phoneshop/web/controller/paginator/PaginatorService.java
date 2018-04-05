package com.es.phoneshop.web.controller.paginator;

import com.es.core.dao.PhoneDao;
import com.es.phoneshop.web.controller.exception.InvalidUrlParamException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class PaginatorService {
    private static PhoneDao phoneDao;
    public static final String NEXT_PAGE = "next";
    public static final String PREV_PAGE = "prev";
    public static final int PHONES_TO_DISPLAY = 5;
    public static final int PREFERABLE_PAGES_AMOUNT = 10;

    @Autowired
    public PaginatorService(PhoneDao phoneDao){
        PaginatorService.phoneDao = phoneDao;
    }

    public static int getPageBeginNumber(Integer pageNumber, String search)
            throws InvalidUrlParamException{
        if(pageNumber < 1){
            throw new InvalidUrlParamException("Invalid page number param: " + pageNumber);
        }
        pageNumber = getValidNewPage(pageNumber, search);
        return (pageNumber % PREFERABLE_PAGES_AMOUNT != 0 || pageNumber == 0) ?
                pageNumber - (pageNumber % PREFERABLE_PAGES_AMOUNT) + 1 : pageNumber - (PREFERABLE_PAGES_AMOUNT - 1);
    }

    public static int getNewPage(Integer currentPage, String pageAction, String search)
            throws InvalidUrlParamException {
        if (NEXT_PAGE.equals(pageAction)) {
            return getPageBeginNumber(currentPage + PREFERABLE_PAGES_AMOUNT, search);
        } else if (PREV_PAGE.equals(pageAction)) {
            return (currentPage - PREFERABLE_PAGES_AMOUNT < 1) ? currentPage : currentPage - PREFERABLE_PAGES_AMOUNT;
        } else {
            throw new InvalidUrlParamException("Page action \"" + pageAction + "\" is not supported");
        }
    }

    public static int getValidNewPage(Integer newPage, String search) {
        int maxPageNumber = getMaxPageNumber(search);
        return (newPage > maxPageNumber) ? maxPageNumber : newPage;
    }

    public static int getPageAmountToDisplay(Integer pageBeginNumber,
                                             String search){
        int maxPageNumber = getMaxPageNumber(search);
        return (maxPageNumber - pageBeginNumber > PREFERABLE_PAGES_AMOUNT) ? PREFERABLE_PAGES_AMOUNT : maxPageNumber - pageBeginNumber + 1;
    }

    private static int getMaxPageNumber(String search){
        int maxPageNumber = (int) Math.ceil((double)(phoneDao.countAvailablePhone(search) / PHONES_TO_DISPLAY));
        return maxPageNumber != 0 ? maxPageNumber : 1;
    }
}