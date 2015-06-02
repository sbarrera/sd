package edu.ub.sd.onlinereslib.services;

import edu.ub.sd.onlinereslib.beans.User;
import edu.ub.sd.onlinereslib.services.exceptions.NotEnoughTokensException;
import edu.ub.sd.onlinereslib.webframework.ServiceController;
import edu.ub.sd.onlinereslib.webframework.annotations.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserStore extends ServiceController {

    private static final boolean NOT_ALLOW_MULTIPLE_SESSIONS = false;

    private static final int InitialTokenAmount = 400;

    private UserBase userbase;
    private UserSession userSession;
    private UserBank userbank;

    @Override
    public void initialize() {
        userbase = new UserBase();
        userbank = new UserBank();
        userSession = new UserSession();

        try {
            String realPath = getServletContext().getRealPath("/WEB-INF/users");

            File users = new File(realPath);
            File[] userFiles = users.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.getName().endsWith(".properties");
                }
            });

            for ( File file : userFiles ) {
                importUserProperties(file);
            }
        } catch ( Exception ex ) {
            log(ex.getMessage(), ex);
        }
    }

    @Override
    public void shutdown() {
        log("User store shutting down");
        try {
            for (User user : userbase.values()) {
                saveUser(user);
            }
        } catch ( Exception ex ) {
            log(ex.getMessage(), ex);
        }
    }

    public Collection<User> getUserList() {
        return userbase.values();
    }

    public User getUser(HttpServletRequest request) {
        String tomcatUsername = request.getRemoteUser();

        if ( tomcatUsername == null ) {
            return null;
        }

        if ( NOT_ALLOW_MULTIPLE_SESSIONS ) {
            if (userSession.containsKey(tomcatUsername)) {
                HttpSession session = userSession.get(tomcatUsername);
                if (!session.equals(request.getSession())) {
                    log(String.format("Detected another session trying to access with user [%s]", tomcatUsername));
                    return null;
                }
            } else {
                log(String.format("Attaching current session for user [%s]", tomcatUsername));
                userSession.put(tomcatUsername, request.getSession());
            }
        }

        if ( !userbase.containsKey(tomcatUsername) ) {
            createUser(tomcatUsername, tomcatUsername, InitialTokenAmount, true);
        }

        return userbase.get(tomcatUsername);
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession();

        if ( NOT_ALLOW_MULTIPLE_SESSIONS ) {
            if (userSession.containsValue(session)) {
                String tomcatUsername = request.getRemoteUser();
                userSession.remove(tomcatUsername);
            }
        }

        session.invalidate();
    }

    public int getTokens(User user) {
        return userbank.get(user).get();
    }

    public int removeTokens(User user, int tokens) throws NotEnoughTokensException {
        AtomicInteger userTokens = userbank.get(user);

        if ( ( userTokens.get() - tokens ) < 0 )
            throw new NotEnoughTokensException();

        return userTokens.addAndGet(-tokens);
    }

    private void importUserProperties(File file) throws Exception {
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));

        String tomcatUsername = file.getName();
        tomcatUsername = tomcatUsername.replace(".properties", "");

        User user = createUser(tomcatUsername, properties.getProperty("username"), Integer.parseInt(properties.getProperty("tokens")), false);

        log(String.format("Loaded new user %s", user));
    }

    private void saveUser(User user) throws Exception {
        log(String.format("Saving user %s", user));

        String realPath = getServletContext().getRealPath("/WEB-INF/users");
        AtomicInteger tokens = userbank.get(user);

        Properties properties = new Properties();
        properties.setProperty("username", user.getName());
        properties.setProperty("tokens", "" + tokens.get());

        File propertiesFile = new File(String.format("%s/%s.properties", realPath, user.getTomcatUsername()));
        FileWriter writer = new FileWriter(propertiesFile);
        properties.store(writer, "");
        writer.close();
    }

    private User createUser(String tomcatUsername, String name, int tokens, boolean isNew) {
        User user = new User();
        user.setTomcatUsername(tomcatUsername);
        user.setName(name);
        user.setIsNew(isNew);

        userbase.put(tomcatUsername, user);
        userbank.put(user, new AtomicInteger(tokens));

        return user;
    }

    class UserBase extends ConcurrentHashMap<String, User> {}

    class UserSession extends ConcurrentHashMap<String, HttpSession> {}

    class UserBank extends ConcurrentHashMap<User, AtomicInteger> {}

}
