package de.helpmeifyoucan.helpmeifyoucan.services;

import com.mongodb.client.MongoDatabase;
import de.helpmeifyoucan.helpmeifyoucan.models.AbstractEntity;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class AbstractObserverService<T extends AbstractEntity> extends AbstractService<T> implements Observer<AbstractObserverService<T>> {
    public AbstractObserverService(MongoDatabase database) {
        super(database);
    }

    @Override
    public void onSubscribe(Disposable disposable) {

    }

    @Override
    public void onNext(AbstractObserverService<T> tAbstractObserverService) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

}
