package com.larry.fc.finalproject.batch.job;


import com.larry.fc.finalproject.core.domain.entity.Engagement;
import com.larry.fc.finalproject.core.domain.entity.Schedule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SendEmailAlarmJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private static final int CHUNK_SIZE = 3;

    @Bean
    public Job sendEmailAlarmJob(
            Step sendScheduleAlarmStep,    //Step에는 reader, processor, writer 가 있다. but processor는 안씀
            Step sendEngagementAlarmStep                    //reader가 바라봐야할 대상이 Engagement, Schedule 두 개다 그래서 Step 두 개로 쪼개줌
    ) {
        return jobBuilderFactory.get("sendEmailAlarmJob")
                .start(sendScheduleAlarmStep)
                .next(sendEngagementAlarmStep)
                .build();
    }

    @Bean
    public Step sendScheduleAlarmStep(
            ItemReader<SendMailBatchReq> sendScheduleAlarmReader,
            ItemWriter<SendMailBatchReq> sendAlarmWriter
    ) {
        return stepBuilderFactory.get("sendScheduleAlarmStep")
                .<SendMailBatchReq, SendMailBatchReq>chunk(CHUNK_SIZE)
                .reader(sendScheduleAlarmReader)
                .writer(sendAlarmWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Step sendEngagementAlarmStep(
            ItemReader<SendMailBatchReq> sendEngagementAlarmReader,
            ItemWriter<SendMailBatchReq> sendAlarmWriter
    ) {
        return stepBuilderFactory.get("sendEngagementAlarmStep")
                .<SendMailBatchReq, SendMailBatchReq>chunk(CHUNK_SIZE)
                .reader(sendEngagementAlarmReader)
                .writer(sendAlarmWriter)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<SendMailBatchReq> sendScheduleAlarmReader(){
        return new JdbcCursorItemReaderBuilder<SendMailBatchReq>()
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(SendMailBatchReq.class))
                .sql("select s.id, s.start_at, s.title, u.email as user_email\n" +
                        "from schedules s\n" +
                        "        inner join users u on s.writer_id = u.id\n" +
                        "where s.start_at >= now() + interval 10 minute\n" +
                        "  and s.start_at < now() + interval 11 minute")
                .name("jdbcCursorItemReader")
                .build();
    }

    @Bean
    public JdbcCursorItemReader<SendMailBatchReq> sendEngagementAlarmReader(){
        return new JdbcCursorItemReaderBuilder<SendMailBatchReq>()
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(SendMailBatchReq.class))
                .sql("select s.id, s.start_at, s.title, u.email as user_email\n" +
                        "from engagements e\n" +
                        "         inner join schedules s on e.schedule_id = s.id\n" +
                        "         inner join users u on s.writer_id = u.id\n" +
                        "where s.start_at >= now() + interval 10 minute\n" +
                        "  and s.start_at < now() + interval 11 minute\n" +
                        "  and e.request_status = 'ACCEPTED'")
                .name("jdbcCursorItemReader")
                .build();
    }

    @Bean
    public ItemWriter<SendMailBatchReq> sendEmailAlarmWriter(){
        return list -> log.info("write items.\n" +
                list.stream()
                        .map(s -> s.toString())
                        .collect(Collectors.joining("\n")));
    }
}
