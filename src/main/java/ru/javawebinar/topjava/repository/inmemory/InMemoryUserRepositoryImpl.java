package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    public static  final Comparator<User> USER_COMPARATOR = (u1, u2) -> u1.getName().compareTo(u2.getName());

    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    {
       fillList();
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        Objects.requireNonNull(user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(user.getId(), (id, oldMeal) -> user);
    }

    @Override
    public User get(int id) {
        Objects.requireNonNull(id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll {}");
        return repository.values().stream().sorted(USER_COMPARATOR).collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        Objects.requireNonNull(email);
       return getAll().stream().filter(u ->u.getEmail().equals(email)).findFirst().orElse(null);
    }

    private void fillList(){
        save(new User(null,"Bob","mail@mail1","pass", Role.ROLE_USER));
        save(new User(null,"Max","mail@mail2","pass", Role.ROLE_USER));
        save(new User(null,"Anton","mail@mail3","pass", Role.ROLE_USER));
        save(new User(null,"Eva","mail@mail4","pass", Role.ROLE_USER));
        save(new User(null,"Vovchick","mail@mail5","pass", Role.ROLE_USER));
        save(new User(null,"Admin","mail@mail6","pass", Role.ROLE_ADMIN));
    }

}
