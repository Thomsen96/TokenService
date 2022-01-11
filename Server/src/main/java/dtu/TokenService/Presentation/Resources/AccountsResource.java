package dtu.TokenService.Presentation.Resources;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Repositories.LocalPaymentRepository;
import dtu.TokenService.Domain.Repositories.Exceptions.ArgumentNullException;
import dtu.TokenService.Domain.Repositories.Exceptions.EntityNotFoundException;
import dtu.TokenService.Domain.Repositories.Interfaces.LocalAccountRepository;
import dtu.ws.fastmoney.Account;
import dtu.ws.fastmoney.BankServiceException_Exception;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/accounts")
public class AccountsResource  {

    private static TokenService service = new TokenService(new LocalPaymentRepository(), new LocalAccountRepository());


    @GET
    public Response get(@QueryParam("id") String id) {
        System.out.println(id);
        try {
            System.out.println(service.getAccount(id));
            return Response.status(Response.Status.OK).entity(service.getAccount(id)).build();
        } catch (ArgumentNullException e) {
            return Response.status(Response.Status.OK).entity(service.getAllAccounts()).build();
        } catch (EntityNotFoundException e) {
            System.out.println(id);
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (BankServiceException_Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @DELETE
    public Response delete(@QueryParam("id")String id) {
        return Response.status(Response.Status.OK).entity(service.deleteAccount(id)).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Account entity) {
        return Response.status(Response.Status.CREATED).entity(service.createAccount(entity)).build();
    }
}
