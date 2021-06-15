package com.neshan.task1.domain.model.animate;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class JourneyEventBus {


    private static final JourneyEventBus journeyEventBus = new JourneyEventBus();

    private PublishSubject<Object> subject = PublishSubject.create();

    private JourneyEventBus() {
    } //private constructor.

    public static synchronized JourneyEventBus getInstance() {
        return journeyEventBus;
    }

    public void setOnJourneyBegin(Object journeyEvent) {
        subject.onNext(journeyEvent);
    }

    public Observable<Object> getOnJourneyEvent() {
        return subject;
    }

    public void setOnJourneyEnd(Object journeyEvent) {
        subject.onNext(journeyEvent);
    }

    public void setOnJourneyUpdate(Object journeyEvent) {
        subject.onNext(journeyEvent);
    }
}