package by.ghoncharko.webproject.command;

import by.ghoncharko.webproject.controller.RequestFactory;
import by.ghoncharko.webproject.dto.DrugsPaginationDto;
import by.ghoncharko.webproject.entity.Drug;
import by.ghoncharko.webproject.entity.Role;
import by.ghoncharko.webproject.entity.User;
import by.ghoncharko.webproject.exception.ServiceException;
import by.ghoncharko.webproject.model.service.DrugService;
import by.ghoncharko.webproject.model.service.DrugServiceImpl;
import by.ghoncharko.webproject.model.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.List;
import java.util.Optional;

public class ShowPreparatesPageCommand implements Command{
    private static final Logger LOG = LogManager.getLogger(ShowPreparatesPageCommand.class);
    private final RequestFactory requestFactory;
    private final DrugService drugService;
    public ShowPreparatesPageCommand(RequestFactory requestFactory, DrugService drugService) {
        this.requestFactory = requestFactory;
        this.drugService = drugService;
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
                    final DrugsPaginationDto drugsPaginationDto = drugService.findAllDrugsLimitOffsetPagination(pageNumber);
                    final List<Drug> drugList = drugsPaginationDto.getDrugList();
                    final int countPages = drugsPaginationDto.getCountPages();
                    request.addAttributeToJsp("currentPageNumber",pageNumber);
                    request.addAttributeToJsp("maxPagesCount",countPages);
                    request.addAttributeToJsp("drugs",drugList);
                }else {
                    final DrugsPaginationDto drugsPaginationDto = drugService.findAllDrugsWhereCountMoreThanZeroAndCalculateDrugCountWithCountInOrderAndDrugNotDeletedLimitOffsetPagination(user, pageNumber);
                    final List<Drug> drugList = drugsPaginationDto.getDrugList();
                    final int countPages = drugsPaginationDto.getCountPages();
                    request.addAttributeToJsp("currentPageNumber",pageNumber);
                    request.addAttributeToJsp("maxPagesCount",countPages);
                    request.addAttributeToJsp("drugs",drugList);
                }
        }else {
            final DrugsPaginationDto drugsPaginationDto = drugService.findAllDrugsWhereCountMoreThanZeroAndDrugNotDeletedLimitOffsetPagination(pageNumber);
            final List<Drug> drugList = drugsPaginationDto.getDrugList();
            final int countPages = drugsPaginationDto.getCountPages();
            request.addAttributeToJsp("currentPageNumber",pageNumber);
            request.addAttributeToJsp("maxPagesCount",countPages);
            request.addAttributeToJsp("drugs",drugList);
        }
       }catch (ServiceException e){
           LOG.error("Cannot find all drugs",e);
       }
        return requestFactory.createForwardResponse(PagePath.PREPARATES_PAGE_PATH);
    }

}
