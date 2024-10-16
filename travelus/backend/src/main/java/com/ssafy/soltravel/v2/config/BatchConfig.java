package com.ssafy.soltravel.v2.config;

import com.ssafy.soltravel.v2.domain.Enum.NotificationType;
import com.ssafy.soltravel.v2.domain.TravelGroup;
import com.ssafy.soltravel.v2.domain.User;
import com.ssafy.soltravel.v2.dto.account.AccountDto;
import com.ssafy.soltravel.v2.dto.exchange.ExchangeRateCacheDto;
import com.ssafy.soltravel.v2.dto.moneyBox.MoneyBoxDto;
import com.ssafy.soltravel.v2.dto.notification.PushNotificationRequestDto;
import com.ssafy.soltravel.v2.repository.GroupRepository;
import com.ssafy.soltravel.v2.service.NotificationService;
import com.ssafy.soltravel.v2.service.account.AccountService;
import com.ssafy.soltravel.v2.service.exchange.ExchangeService;
import com.ssafy.soltravel.v2.service.group.GroupService;
import com.ssafy.soltravel.v2.util.LogUtil;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private final AccountService accountService;
    private final NotificationService notificationService;

    /*
     * 환전 배치 설정
     */
    @Bean
    public Job exchangeRateJob(Step exchangeRateStep) {
        return new JobBuilder("exchangeRateJob", jobRepository)
            .start(exchangeRateStep)
            .build();
    }

    @Bean
    public Step exchangeRateStep(Tasklet exchangeRateTasklet) {
        return new StepBuilder("exchangeRateStep", jobRepository)
            .tasklet(exchangeRateTasklet, transactionManager)
            .build();
    }

    @Bean
    public Tasklet exchangeRateTasklet(ExchangeService exchangeService) {
        return (contribution, chunkContext) -> {
            //RabbitMQ를 통해 받은 message
            String message = (String) chunkContext.getStepContext().getJobParameters().get("message");

            // 메시지 파싱
            String[] parts = message.split(", ");
            String currencyCode = parts[0].split(": ")[1];
            Double exchangeRate = Double.parseDouble(parts[1].split(": ")[1]);
            String timeLastUpdateUtc = parts[2].split(": ")[1];
            int exchangeMin = Integer.parseInt(parts[3].split(": ")[1]);

            // 이전 환율 가져오기
            ExchangeRateCacheDto cachedDto = exchangeService.getExchangeRateFromCache(currencyCode);

            if (cachedDto == null || !exchangeRate.equals(cachedDto.getExchangeRate())) {
                exchangeService.updateExchangeRateCache(
                    new ExchangeRateCacheDto(currencyCode, exchangeRate, timeLastUpdateUtc, String.valueOf(exchangeMin))
                );

//                exchangeService.processCurrencyConversions(currencyCode, exchangeRate);
            } else {

                LogUtil.info(String.format("환율 변동 없음. 통화 코드: %s, 기존 환율: %s, 새로운 환율: %s",
                    currencyCode, cachedDto.getExchangeRate(), exchangeRate));
            }
            /**
             * 시연테스트용
             */
            exchangeService.processCurrencyConversions(currencyCode, exchangeRate);
            return RepeatStatus.FINISHED;
        };
    }


    /*
     * 알림 배치 설정
     */

    @Bean
    public Job travelNotificationJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Job job = new JobBuilder("travelNotificationJob", jobRepository)
            .start(travelNotificationStep(jobRepository, transactionManager))
            .build();
        return job;
    }

    public Step travelNotificationStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        Step step = new StepBuilder("travelNotificationStep", jobRepository)
            .tasklet(travelNotificationTasklet(), transactionManager)
            .build();
        return step;
    }

    public Tasklet travelNotificationTasklet() {

        return ((contribution, chunkContext) -> {
            System.out.println("SpringBatch - travelNotificationTasklet 실행");

            LocalDate currentDate = LocalDate.now();
            LocalDate endDate = currentDate.plusDays(7);

            List<TravelGroup> groupList = groupRepository.findGroupsWithinRange(currentDate, endDate);

            for (TravelGroup group : groupList) {

                LocalDate travelStartDate = group.getTravelStartDate();

                long dDay = ChronoUnit.DAYS.between(currentDate, travelStartDate);

                User user = group.getParticipants().get(0).getUser();

                LogUtil.info("user", user.getUserId());

                AccountDto accountDto = accountService.getByAccountNo(group.getGroupAccountNo(), user);

                List<MoneyBoxDto> moneyBoxDtos = accountDto.getMoneyBoxDtos();

                MoneyBoxDto moneyBoxDto = null;

                if (moneyBoxDtos.size() > 1) {
                    moneyBoxDto = moneyBoxDtos.get(1);

                }

                if (moneyBoxDto != null && moneyBoxDto.getBalance() == 0) {

                    String title = String.format("여행 D-%s", dDay);

                    if (dDay == 0) {
                        title = String.format("여행 D-Day!");
                    }

                    String message = String.format("아직 환전이 진행되지 않았어요!\n서둘러 환전을 진행해 주세요");

                    PushNotificationRequestDto pushNotificationRequestDto = PushNotificationRequestDto.builder()
                        .targetUserId(user.getUserId())
                        .notificationType(NotificationType.GD)
                        .title(title)
                        .message(message)
                        .icon("/icons/favicon.ico")
                        .groupId(group.getGroupId())
                        .accountNo(accountDto.getAccountNo())
                        .currencyType(moneyBoxDto.getCurrencyCode())
                        .build();

                    notificationService.sendExchangeSuggestNotification(pushNotificationRequestDto);
                }

            }

            return RepeatStatus.FINISHED;
        });
    }

}
