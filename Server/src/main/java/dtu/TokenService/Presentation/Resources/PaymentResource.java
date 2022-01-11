package dtu.TokenService.Presentation.Resources;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import dtu.TokenService.Application.TokenService;
import dtu.TokenService.Domain.Entities.Payment;
import dtu.TokenService.Domain.Repositories.LocalPaymentRepository;
import dtu.TokenService.Domain.Repositories.Interfaces.LocalAccountRepository;
import dtu.ws.fastmoney.BankServiceException_Exception;

@Path("/payments")
public class PaymentResource  {
    private static final TokenService service = new TokenService(new LocalPaymentRepository(), new LocalAccountRepository());


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response pay(Payment payment) {
        try {
            service.pay(payment.amount, payment.cid, payment.mid);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (BankServiceException_Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }

}
