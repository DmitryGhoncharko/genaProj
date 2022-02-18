package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.dto.DrugUserOrderDto;
import by.ghoncharko.webproject.entity.BankCard;
import by.ghoncharko.webproject.entity.DrugUserOrder;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.model.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class ShowOrderPageCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ShowOrderPageCommand.class);
    private final RequestFactory requestFactory;
    private final OrderService orderService;
    public ShowOrderPageCommand(RequestFactory requestFactory, OrderService orderService){
        this.requestFactory = requestFactory;
        this.orderService = orderService;
    }

    @Override
    public CommandResponse execute(CommandRequest request)
    {
        final Optional<Object> userFromSesstion = request.retrieveFromSession("user");
        if(userFromSesstion.isPresent()){
            final User user = (User)userFromSesstion.get();
            final int userId = user.getId();
            final Optional<DrugUserOrderDto> drugUserOrderDtoFromDB = orderService.findNotPaidOrderByUserId(userId);
            if(drugUserOrderDtoFromDB.isPresent()){
                final DrugUserOrderDto drugUserOrderDto = drugUserOrderDtoFromDB.get();
                final List<DrugUserOrder> drugUserOrderList = drugUserOrderDto.getDrugUserOrderList();
                final List<BankCard> bankCardList = drugUserOrderDto.getBankCardList();
                final Double finalPrice = drugUserOrderDto.getFinalPrice().doubleValue();
                request.addAttributeToJsp("drugUserOrders",drugUserOrderList);
                request.addAttributeToJsp("bankCards", bankCardList);
                request.addAttributeToJsp("orderFinalPrice",finalPrice);
            }
            return requestFactory.createForwardResponse(PagePath.ORDER_PAGE_PATH);
        }
        return null;
    }

}
