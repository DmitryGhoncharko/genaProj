package by.ghoncharko.webproject.entity;

public final class OrderStatusHolder {
    public static final OrderStatus ACTIVE = new OrderStatus.Builder().
            withId(1).
            withName("active").build();
    public static final OrderStatus INACTIVE = new OrderStatus.Builder().
            withId(2).
            withName("inactive").build();
    private OrderStatusHolder(){

    }
}
