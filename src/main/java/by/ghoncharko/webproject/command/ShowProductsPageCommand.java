package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.dto.ProductPaginationDto;
import by.ghoncharko.webproject.entity.Product;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.ProductService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class ShowProductsPageCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ShowProductsPageCommand.class);
    private final RequestFactory requestFactory;
    private final ProductService productService;
    public ShowProductsPageCommand(RequestFactory requestFactory, ProductService productService) {
        this.requestFactory = requestFactory;
        this.productService = productService;
    }

    @Override
    public CommandResponse execute(CommandRequest request) {
       final Optional<Object> userFromSession = request.retrieveFromSession("user");
        final Integer pageNumber = Integer.valueOf(request.getParameter("page"));
       try{
        if(userFromSession.isPresent()){
            final   User user =(User)userFromSession.get();
            final Role userRole = user.getRole();
                if(userRole == Role.PHARMACY){
                    final ProductPaginationDto productPaginationDto = productService.findAllDrugsLimitOffsetPagination(pageNumber);
                    final List<Product> productList = productPaginationDto.getDrugList();
                    final int countPages = productPaginationDto.getCountPages();
                    request.addAttributeToJsp("currentPageNumber",pageNumber);
                    request.addAttributeToJsp("maxPagesCount",countPages);
                    request.addAttributeToJsp("drugs", productList);
                }else {
                    final ProductPaginationDto productPaginationDto = productService.findAllDrugsWhereCountMoreThanZeroAndCalculateDrugCountWithCountInOrderAndDrugNotDeletedLimitOffsetPagination(user, pageNumber);
                    final List<Product> productList = productPaginationDto.getDrugList();
                    final int countPages = productPaginationDto.getCountPages();
                    request.addAttributeToJsp("currentPageNumber",pageNumber);
                    request.addAttributeToJsp("maxPagesCount",countPages);
                    request.addAttributeToJsp("drugs", productList);
                }
        }else {
            final ProductPaginationDto productPaginationDto = productService.findAllDrugsWhereCountMoreThanZeroAndDrugNotDeletedLimitOffsetPagination(pageNumber);
            final List<Product> productList = productPaginationDto.getDrugList();
            final int countPages = productPaginationDto.getCountPages();
            request.addAttributeToJsp("currentPageNumber",pageNumber);
            request.addAttributeToJsp("maxPagesCount",countPages);
            request.addAttributeToJsp("drugs", productList);
        }
       }catch (ServiceException e){
           LOG.error("Cannot find all drugs",e);
       }
        return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
    }

}
