package pl.justmedia.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.justmedia.entity.*;
import pl.justmedia.service.dto.AddSubscriptionForm;
import pl.justmedia.service.dto.RegisteredSubscription;
import pl.justmedia.service.dto.RemoveSubscriptionForm;
import pl.justmedia.service.exception.SubscriptionException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PlayerSubscriptionService {
    @NonNull
    private UserRepository userRepository;
    @NonNull
    EventsRepository eventsRepository;
    @NonNull
    SubscriptionRepository subscriptionRepository;

    public RegisteredSubscription addSubscripton(@NonNull AddSubscriptionForm form){
        if(userRepository.getById(form.getUserId()) == null){
            throw new SubscriptionException("");
        }
        if (!(userRepository.getById(form.getUserId()).getUserType().equals(UserType.PLAYER))) {
            throw new SubscriptionException("Given user is not a Player");
            }

        Player player = userRepository.getPlayerByUserId(form.getUserId());
        Subscription subscription = new Subscription(
                form.isSubscriptionPaymentDone(),
                LocalDateTime.now(),
                form.isSubscriptionApproved(),
                form.getEvent(),player);
        player.addSubscription(subscription);
        userRepository.save(player);
        return new RegisteredSubscription(player.getUserId(),subscription.getSubscriptionId());
    }

    public void removeSubscription(@NonNull RemoveSubscriptionForm form){
        Player player = userRepository.getPlayerByUserId(form.getUserId());
        Event event = eventsRepository.getById(form.getEvent().getEventId());
        Subscription subscription= subscriptionRepository.findByEvent_EventIdAndPlayer_UserId(event.getEventId(),player.getUserId());
        player.removeSubscription(event);
        event.removeSubscription(subscription);
        userRepository.save(player);
    }

}
