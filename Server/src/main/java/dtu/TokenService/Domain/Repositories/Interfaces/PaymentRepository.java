package dtu.TokenService.Domain.Repositories.Interfaces;

import java.util.Collection;

import dtu.TokenService.Domain.Entities.Payment;

public interface PaymentRepository {
    public Collection<Payment> getAll();
    public Payment create(Payment payment);



}
