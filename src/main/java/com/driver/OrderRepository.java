package com.driver;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {

    HashMap<String,Order> ordermap = new HashMap<>();
    HashMap<String,DeliveryPartner> partnermap = new HashMap<>();

    HashMap<String,ArrayList<String>> OrderPartnerMap = new HashMap<>();

    public void addOrder(Order order) {
        String id = order.getId();
        ordermap.put(id,order);

    }

    public void addPartner(String partnerId) {
        partnermap.put(partnerId,new DeliveryPartner(partnerId));
    }

    public void addOrderPartnerPair(String orderId, String partnerId) {

        if(OrderPartnerMap.containsKey(partnerId)){
           ArrayList list = OrderPartnerMap.get(partnerId);
            list.add(orderId);
            OrderPartnerMap.put(partnerId,list);
        }else{
            ArrayList<String> list = new ArrayList<String>();
            list.add(orderId);
            OrderPartnerMap.put(partnerId,list);
        }

    }

    public Order getOrderById(String orderId) {
        if(ordermap.containsKey(orderId)) return ordermap.get(orderId);
        else return null;
    }

    public DeliveryPartner getPartnerById(String partnerId) {
        if(partnermap.containsKey(partnerId)) return partnermap.get(partnerId);
        else return null;
    }

    public Integer getOrderCountByPartnerId(String partnerId) {
        if(OrderPartnerMap.containsKey(partnerId)){
            return OrderPartnerMap.get(partnerId).size();
        }
        else return 0;
    }

    public List<String> getOrdersByPartnerId(String partnerId) {
        if(OrderPartnerMap.containsKey(partnerId)){
            return OrderPartnerMap.get(partnerId);
        }
        else return null;
    }

    public List<String> getAllOrders() {
        List<String> order = new ArrayList<>();
        for(String id : ordermap.keySet()){
            order.add(id);
        }return  order;
    }

    public Integer getCountOfUnassignedOrders() {
        int count =0;
        boolean con = false;
        for(String odrid : ordermap.keySet()){
            for(ArrayList<String> list : OrderPartnerMap.values()){
                if(list.contains(odrid))  {
                    con = true;
                    break;
                }
            }
            if(con) {
                con = false;
                continue;
            }
            else {
                count++;
            }
        }
        return count;
    }

    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time, String partnerId) {
        int hour = Integer.valueOf(time.substring(0,2))*60;
        int timming = hour+Integer.valueOf(time.substring(3));
        boolean get = false;
        String gotit = "";
        if(OrderPartnerMap.containsKey(partnerId)){
            ArrayList<String> list = OrderPartnerMap.get(partnerId);
            for(String odrid :list){
                for(String id : ordermap.keySet()){
                    if(odrid==id && timming == ordermap.get(id).getDeliveryTime()) {
                        get = true;
                        break;
                    }
                }
                if(get) {
                    gotit = odrid;
                    break;
                }
            }
            for(int i=0;i<list.size()-1;i++){
                if(gotit == list.get(i)){
                    return list.size()-(i+1);
                }
            }

        }
        return 0;
    }

    public String getLastDeliveryTimeByPartnerId(String partnerId) {
        String id = OrderPartnerMap.get(partnerId).get(OrderPartnerMap.get(partnerId).size()-1);
        for (Order odr : ordermap.values()){
            if(id == odr.getId()) return String.valueOf(odr.getDeliveryTime());
        }
        return null;
    }

    public void deletePartnerById(String partnerId) {
        partnermap.remove(partnerId);
        OrderPartnerMap.remove(partnerId);
    }

    public void deleteOrderById(String orderId) {
        ordermap.remove(orderId);
        for (String ptr : OrderPartnerMap.keySet()){
                if(OrderPartnerMap.get(ptr).contains(orderId)) {
                    ArrayList list = OrderPartnerMap.get(ptr);
                    list.remove(orderId);
                    OrderPartnerMap.put(ptr,list);
                }
        }
    }
}
