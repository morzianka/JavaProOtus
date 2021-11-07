package ru.otus.server;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.otus.grpc.CounterRq;
import ru.otus.grpc.CounterRs;
import ru.otus.grpc.CounterServiceGrpc;

/**
 * @author Shabunina Anita
 */
@Slf4j
@GrpcService
public class CounterServer extends CounterServiceGrpc.CounterServiceImplBase {

    @Override
    public void count(CounterRq request, StreamObserver<CounterRs> responseObserver) {
        for (int i = request.getStart(); i <= request.getEnd(); i++) {
            responseObserver.onNext(CounterRs.newBuilder()
                .setResult(i)
                .build());
            log.info("new value:" + i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
                responseObserver.onError(e);
                Thread.currentThread().interrupt();
            }
        }
        responseObserver.onCompleted();
    }
}
