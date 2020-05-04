package de.helpmeifyoucan.helpmeifyoucan.services.observable.subjects;

import de.helpmeifyoucan.helpmeifyoucan.services.AbstractObserverService;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;

import java.util.LinkedList;
import java.util.List;

public abstract class ServiceSubject<T extends AbstractObserverService<?>> extends Subject<T> {

    protected List<T> observers = new LinkedList<>();

    @Override
    public boolean hasObservers() {
        return observers.isEmpty();
    }

    @Override

    public boolean hasThrowable() {
        return false;
    }

    @Override
    public boolean hasComplete() {
        return false;
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }


    @Override
    public void onSubscribe(Disposable disposable) {
    }

    @Override
    public void onError(Throwable throwable) {
        observers.forEach(x -> x.onError(throwable));
    }

    @Override
    public void onComplete() {
        observers.forEach(x -> x.onComplete());
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {

    }

    public void subscribe(T observer) {
        observers.add(observer);
    }
}
