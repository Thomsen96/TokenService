package dtu.TokenService.Domain.Repositories.Exceptions;

public class EntityNotFoundException extends Throwable {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
