package io.sthorng.domain.usecase;

import io.sthorng.domain.exception.ValidationException;

public interface IUsecase<T, U> {
    public U process(T t) throws ValidationException;
}
