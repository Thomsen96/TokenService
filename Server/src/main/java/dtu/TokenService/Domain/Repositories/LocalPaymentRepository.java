package dtu.TokenService.Domain.Repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import dtu.TokenService.Domain.Entities.Payment;
import dtu.TokenService.Domain.Repositories.Exceptions.ArgumentNullException;
import dtu.TokenService.Domain.Repositories.Exceptions.EntityNotFoundException;
import dtu.TokenService.Domain.Repositories.Interfaces.PaymentRepository;

public class LocalPaymentRepository implements PaymentRepository {


    private final static ArrayList<Payment> payments = new ArrayList<>();
    private static int counter = 0;



    @Override
    public ArrayList<Payment> getAll() {
        return payments;
    }

    @Override
    public Payment create(Payment entity) {
        payments.add(entity);
        return entity;
    }

}
