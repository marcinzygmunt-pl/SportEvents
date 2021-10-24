package pl.justmedia.entity;

import org.hibernate.mapping.Subclass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {
    //@Query("from Subscription s where s.event = (?1) and s.player = (?2)")
    Subscription findByEvent_EventIdAndPlayer_UserId(UUID event_id, UUID user_id);
}
