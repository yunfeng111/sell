package com.kino.sell.service.impl;

import com.kino.sell.dto.OrderDTO;
import com.kino.sell.enums.ResultEnum;
import com.kino.sell.exception.SellException;
import com.kino.sell.service.BuyerService;
import com.kino.sell.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: kino
 * @Date: 2019/3/11 23:24
 */
@Slf4j
@Service
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;



    @Override
    public OrderDTO findOrderOne(String openid, String orderId) {
        return checkOrderOwner(openid,orderId);
    }

    @Override
    public OrderDTO cancelOrder(String openid, String orderId) {
        OrderDTO orderDTO = checkOrderOwner(openid, orderId);
        if (orderDTO == null) {
            log.error("【取消订单】查不到改订单, orderId={}", orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIST);
        }

        return orderService.cancel(orderDTO);
    }

    private OrderDTO checkOrderOwner(String openid,String orderId){
        OrderDTO orderDTO = orderService.findOne(orderId);
        if(!orderDTO.getBuyerOpenid().equals(openid)){
            log.error("【查询订单】订单的openid不一致. openid={}, orderDTO={}", openid, orderDTO);
            throw new SellException(ResultEnum.ORDER_OWNER_ERROR);
        }
        return orderDTO;
    }
}
