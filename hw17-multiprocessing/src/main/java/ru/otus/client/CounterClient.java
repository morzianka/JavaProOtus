package ru.otus.client;

import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import ru.otus.grpc.CounterRq;
import ru.otus.grpc.CounterRs;
import ru.otus.grpc.CounterServiceGrpc.CounterServiceStub;

import javax.annotation.PostConstruct;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Shabunina Anita
 */
@Slf4j
@Service
@DependsOn("counterServer")
@RequiredArgsConstructor
public class CounterClient {

    private final AtomicInteger currentNumber = new AtomicInteger();
    @GrpcClient("counterService")
    private CounterServiceStub counterServiceStub;

    @PostConstruct
    public void call() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                count(0, 30);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
        }).start();
        new Thread(() -> print(50)).start();
    }

    public void count(int start, int end) {
        CounterRq rq = CounterRq.newBuilder()
            .setStart(start)
            .setEnd(end)
            .build();

        StreamObserver<CounterRs> streamObserver = new StreamObserver<>() {

            @Override
            public void onNext(CounterRs value) {
                currentNumber.set(value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                log.error(t.getMessage(), t);
            }

            @Override
            public void onCompleted() {
                log.info("request completed");
            }
        };
        counterServiceStub.count(rq, streamObserver);
    }

    public void print(int quantity) {
        int currentValue = 0;
        int prev = -1;
        for (int i = 0; i < quantity; i++) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                Thread.currentThread().interrupt();
            }
            int currentFromServer = currentNumber.get();
            currentValue = prev == currentFromServer ? currentValue + 1 :
                currentValue + currentFromServer + 1;
            prev = currentFromServer;
            log.info("currentValue:" + currentValue);
        }
    }
}
