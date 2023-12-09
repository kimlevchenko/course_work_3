package pro.sky.telegrambot.notifier;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.repository.NotificationRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


@Service
@EnableScheduling
public class TaskNotifier {
    private final static Logger logger = LoggerFactory.getLogger(TaskNotifier.class);

    private final TelegramBot bot;
    private final NotificationRepository repository;

    public TaskNotifier(TelegramBot bot, NotificationRepository repository) {
        this.bot = bot;
        this.repository = repository;
    }

    @Scheduled(cron = "0 0/1 * * * *")
    public void notifyTask() {
        repository.findAllByDateTime(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
                .forEach(notificationTask -> {
                    bot.execute(new SendMessage(notificationTask.getChatId(), notificationTask.getText()));
                    repository.delete(notificationTask);
                    logger.info("Notification has been sent!");
                });
    }
}