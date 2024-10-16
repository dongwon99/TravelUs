package com.ssafy.soltravel.v2.service.exchange;

import com.ssafy.soltravel.v2.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExchangeBatchTriggerService {

    private final JobLauncher jobLauncher;
    private final Job exchangeRateJob;
    private final Job travelNotificationJob;

    /**
     * RabbitMQ 메시지 수신
     */
    @RabbitListener(queues = RabbitMQConfig.EXCHANGE_RATE_QUEUE)
    public void receiveMessage(String message) {
        try {
            jobLauncher.run(exchangeRateJob,
                new JobParametersBuilder()
                    .addString("message", message)
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTravelNotification() {
        try {
            jobLauncher.run(travelNotificationJob,
                new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
