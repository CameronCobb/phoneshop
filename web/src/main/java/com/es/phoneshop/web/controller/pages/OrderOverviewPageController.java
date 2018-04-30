package com.es.phoneshop.web.controller.pages;

import com.es.core.dao.orderDao.OrderDao;
import com.es.core.model.order.exception.NoSuchOrderException;
import org.h2.util.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/orderOverview")
public class OrderOverviewPageController {
    @Resource
    private OrderDao orderDao;

    @RequestMapping(method = RequestMethod.GET, value = "/{orderId}")
    public String showOrder(@PathVariable Long orderId, Model model){
        model.addAttribute("order", orderDao.get(orderId).orElseThrow(NoSuchOrderException::new));
        return "orderOverview";
    }
}
