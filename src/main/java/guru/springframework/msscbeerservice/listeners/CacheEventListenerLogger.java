package guru.springframework.msscbeerservice.listeners;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

@Slf4j
public class CacheEventListenerLogger implements CacheEventListener<Object, Object> {
    @Override
    public void onEvent(
            CacheEvent<? extends Object, ? extends Object> cacheEvent) {
        log.info(">> CACHE EVENT: Key [{}} Old-Value [{}} New-Value [{}}",
                cacheEvent.getKey(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}
