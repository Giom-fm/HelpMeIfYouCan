package de.helpmeifyoucan.helpmeifyoucan.services;

import de.helpmeifyoucan.helpmeifyoucan.models.AbstractHelpModel;
import de.helpmeifyoucan.helpmeifyoucan.models.UserModel;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;

import java.util.LinkedList;
import java.util.List;

class ServiceSubject extends Subject<UserModel> {

    private List<AbstractHelpModelService<? extends AbstractHelpModel>> observers =
            new LinkedList<>();


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
    public void onNext(UserModel userModel) {

    }

    public void onUpdate(UserModel userModel) {
        observers.forEach(x -> x.onUpdate(userModel));
    }

    @Override
    public void onError(Throwable throwable) {
        observers.forEach(x -> x.onError(throwable));
    }

    @Override
    public void onComplete() {
        observers.forEach(AbstractHelpModelService::onComplete);
    }

    public void onDelete(UserModel userModel) {
        observers.forEach(x -> x.onDelete(userModel));
    }


    protected void subscribeActual(AbstractHelpModelService<? extends AbstractHelpModel> observer) {
        this.observers.add(observer);

    }

    @Override
    protected void subscribeActual(Observer<? super UserModel> observer) {

    }
}
