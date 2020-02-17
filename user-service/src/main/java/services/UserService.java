package services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.arc.Arc;
import io.quarkus.oidc.IdToken;
import io.quarkus.security.identity.SecurityIdentity;
import models.User;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.hibernate.Filter;
import org.hibernate.Session;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.NotFoundException;
import java.util.List;

@RequestScoped
public class UserService {

    @Claim(standard = Claims.preferred_username)
    String username;

    @Claim(standard = Claims.sub)
    String subject;

    @Claim(standard = Claims.iss)
    String issuer;

    @Inject
    SecurityIdentity identity;

    @Inject
    ObjectMapper objectMapper;



    public List<User> findAll() {
        return User.findAll().list();
    }

    public User findById(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        return user;
    }

    public User create(User user) {
        user.persist();
        return user;
    }

    public void deleteById(Long id) {
        User user = User.findById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        user.delete();
    }

    public User update(Long id, User newUser) {
        User user = User.findById(id);
        if (user == null) {
            throw new NotFoundException("User not found");
        }
        user.setUsername(newUser.getUsername());
        user.setUuid(newUser.getUuid());
        user.persist();
        return user;
    }

    public User findByUsernameOrCreate(SecurityIdentity identity) {
        User user = User.find("username = ?1", identity.getPrincipal().getName()).firstResult();
        if (user == null) {
            user = new User();
            user.setUuid(subject);
            user.setUsername(username);
            user.setTenant(issuer);
            user.persistAndFlush();
        }
        return user;
    }



    @PostConstruct
    public void hibernateFilter() {
        Session session = Arc.container().instance(EntityManager.class).get().unwrap(Session.class);
        Filter filter = session.enableFilter("tenantFilter");
        filter.setParameter("tenant", issuer);
        filter.validate();
    }

}
